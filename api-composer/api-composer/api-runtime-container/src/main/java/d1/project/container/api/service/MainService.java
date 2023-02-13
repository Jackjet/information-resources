package d1.project.container.api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.project.container.api.base.Context;
import d1.project.container.api.base.bean.Input;
import d1.project.container.api.base.bean.Node;
import d1.project.container.api.base.bean.Payload;
import d1.project.container.api.base.service.BaseNodeService;
import d1.project.container.api.base.service.LogService;
import d1.project.container.api.base.utils.Constants;
import d1.project.container.api.dao.ApiDao;
import d1.project.container.api.entity.ApiEntity;
import d1.project.container.api.factory.NodeServiceFactory;
import d1.project.container.api.http.Result;
import d1.project.container.api.http.ResultUtil;
import d1.project.container.api.model.RunModel;
import d1.project.container.api.model.RunModelFindAll;
import d1.project.container.api.utils.MyUtils;
import d1.project.container.api.utils.SpecificationBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

/**
 * @author maorui
 */
@Service
public class MainService {
    private final ApiDao apiDao;
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final NodeServiceFactory nodeServiceFactory;
    private final RecordApiService recordApiService;

    private Map<String, RequestMappingInfo> mapping = new HashMap<>();

    public MainService(RequestMappingHandlerMapping mapping, ApiDao apiDao) {
        this.nodeServiceFactory = NodeServiceFactory.getInstance();
        this.recordApiService = RecordApiService.getInstance();

        this.requestMappingHandlerMapping = mapping;
        this.apiDao = apiDao;
    }

    /**
     * 读取数据库的api
     */
    public void init() {
        mapping.clear();
        List<ApiEntity> entities = apiDao.findAll();
        for (ApiEntity entity : entities) {
            RunModel model = new RunModel();
            BeanUtils.copyProperties(entity, model);

            List<Node> nodes = JSON.parseArray(entity.getNodes(), Node.class);
            model.setNodes(nodes);
            try {
                Context context = new Context();
                context.setId(model.getId());
                recordApiService.registerApi(model.getPath(), model.getMethod(), context);

                //动态创建api
                addMapping(model.getId(), MyUtils.parseMethod(model.getMethod()), model.getContentType(), model.getPath());

                //初始化节点
                processNode(context, model.getNodes());

                LogService.localLog(model.getId(), "api " + model.getId() + " created");
            } catch (Exception e) {
                LogService.localError(model.getId(), e);
            }
        }
    }

    public void run(RunModel model) throws Exception {
        LogService.localLog(model.getId(), "receive api flow, processing...");

        Context context = new Context();
        context.setId(model.getId());
        recordApiService.registerApi(model.getPath(), model.getMethod(), context);

        LogService.localLog(model.getId(), "create api...");
        //创建api
        createApi(model);

        LogService.localLog(model.getId(), "api init...");
        //初始化节点
        processNode(context, model.getNodes());

        LogService.localLog(model.getId(), "api process finished");
    }

    public void remove(String id) throws Exception {
        LogService.localLog(id, "delete api...");
        //删除context
        Optional<ApiEntity> apiOpt = apiDao.findById(id);
        if (apiOpt.isPresent()) {
            ApiEntity apiEntity = apiOpt.get();
            Context context = recordApiService.getContext(apiEntity.getPath(), apiEntity.getMethod());
            if (!id.equals(context.getId())) {
                String errorMsg = "delete fail apiId don't agree id=" + context.getId();
                LogService.localLog(id, errorMsg);
                throw new Exception(errorMsg);
            }

            recordApiService.unRegisterApi(apiEntity.getPath(), apiEntity.getMethod());

            //释放资源
            context.destroy();
        }

        //从内存中删除
        removeMapping(id);

        //从数据库中删除
        if (apiDao.existsById(id)) {
            apiDao.deleteById(id);
        }

        LogService.localLog(id, "delete api successful");
    }

    public String[] log(String day) throws Exception {
        File logFile = new File(Constants.LOG_ROOT, day);
        if (!logFile.exists()) {
            throw new Exception(day + "日志不存在");
        }
        return logFile.list();
    }

    public Page<ApiEntity> findAll(RunModelFindAll model) throws Exception {
        JSONObject paramObject = (JSONObject) JSON.toJSON(model);
        SpecificationBuilder<ApiEntity> builder = new SpecificationBuilder<>();
        return apiDao.findAll(getSpecification(paramObject, builder), builder.getPageable());
    }

    ////////////////
    private void createApi(RunModel model) throws Exception {
        //判断id是否存在，如果存在就去修改
        Optional<ApiEntity> apiEntityOptional = apiDao.findById(model.getId());
        if (apiEntityOptional.isPresent()) {
            save(model, apiEntityOptional.get(), false);

            //删除内存中
            removeMapping(model.getId());
        } else {
            //如果id 不存在，判断path+method是否存在，如果存在就报错
            apiEntityOptional = apiDao.findFirstByPathAndMethod(model.getPath(), model.getMethod());
            if (apiEntityOptional.isPresent()) {
                throw new Exception("api创建失败,path+method已经存在,(path=" + model.getPath() + ", method=" + model.getMethod() + ")");
            }

            //保存到数据库中
            save(model, new ApiEntity(), true);
        }

        //添加到内存中
        addMapping(model.getId(), MyUtils.parseMethod(model.getMethod()), model.getContentType(), model.getPath());
    }

    private void processNode(Context context, List<Node> nodes) throws Exception {
        if (nodes == null || nodes.size() < 2) {
            throw new Exception("节点个数必须大于两个");
        }

        //解析节点
        List<BaseNodeService> nodeServices = new ArrayList<>();
        Map<String, BaseNodeService> maps = new HashMap<>();
        for (Node node : nodes) {
            BaseNodeService nodeService = nodeServiceFactory.createNodeService(node.getType());
            BeanUtils.copyProperties(node, nodeService);
            //初始化
            nodeService.init(node.getProperties());
            //设置属性
            nodeServices.add(nodeService);

            maps.put(nodeService.getId(), nodeService);
        }
        context.setNodes(maps);

        //设置第一个节点
        context.setFirstNode(nodeServices.get(0));
    }

    private void save(RunModel model, ApiEntity apiEntity, boolean isSave) {
        BeanUtils.copyProperties(model, apiEntity);

        //设置nodes
        apiEntity.setNodes(JSON.toJSONString(model.getNodes()));
        if (isSave) {
            apiEntity.setCreateTime(Calendar.getInstance());
        } else {
            apiEntity.setUpdateTime(Calendar.getInstance());
        }
        //修改数据库中
        apiDao.save(apiEntity);
    }

    /**
     * 添加 api与方法 映射
     *
     * @param method      请求方式
     * @param contentType 请求文本协议
     * @param path        路径
     * @throws NoSuchMethodException 异常
     */
    private void addMapping(String id, RequestMethod method, String contentType, String path) throws NoSuchMethodException {
        RequestMappingInfo requestMappingInfo = RequestMappingInfo
                .paths(path)
//                .headers(header)
//                .params(params)
                .methods(method)
                .produces(contentType)
                .build();

        requestMappingHandlerMapping.registerMapping(requestMappingInfo, this, MainService.class.getDeclaredMethod("handleRequests", HttpServletRequest.class));

        mapping.put(id, requestMappingInfo);
    }

    private void removeMapping(String id) {
        RequestMappingInfo requestMappingInfo = mapping.remove(id);
        requestMappingHandlerMapping.unregisterMapping(requestMappingInfo);
    }


    /////////////////////
    //所有动态创建api的处理方法
    public ResponseEntity<String> handleRequests(HttpServletRequest request) {
        Context context = null;
        try {
            context = recordApiService.getContext(request.getServletPath(), request.getMethod());
            LogService.localLog(context.getId(), "http receiver start...");

            //设置携带的数据
            Payload payload = new Payload();
            payload.setHeaders(MyUtils.getHeaders(request));
            payload.setParams(MyUtils.getParams(request));
            payload.setBody(MyUtils.getBody(request));
            context.setPayload(payload);

            LogService.localLog(context.getId(), "receiver data：" + context.getPayloadJson().toJSONString());

            //执行第一个节点
            Object result = context.getFirstNode().run(context, new Input());

            Result<Object> successResult = ResultUtil.success();
            successResult.setData(result);
            return new ResponseEntity<>(JSON.toJSONString(successResult), HttpStatus.OK);
        } catch (Exception e) {
            if (context != null) {
                LogService.error(context.getId(), e);
            }
            return new ResponseEntity<>(JSON.toJSONString(ResultUtil.fail(e)), HttpStatus.OK);
        }
    }

    /**
     * 获取查询规则
     */
    private Specification<ApiEntity> getSpecification(JSONObject paramObject, SpecificationBuilder<ApiEntity> builder) throws Exception {
        SpecificationBuilder<ApiEntity> specificationBuilder = builder.init(paramObject)
                .sContain("name", "name")
                .sEqual("method", "method")
                .dOrder("createTime");
        return specificationBuilder.build();
    }

}
