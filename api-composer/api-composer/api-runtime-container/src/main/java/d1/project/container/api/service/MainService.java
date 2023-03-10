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
     * ??????????????????api
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

                //????????????api
                addMapping(model.getId(), MyUtils.parseMethod(model.getMethod()), model.getContentType(), model.getPath());

                //???????????????
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
        //??????api
        createApi(model);

        LogService.localLog(model.getId(), "api init...");
        //???????????????
        processNode(context, model.getNodes());

        LogService.localLog(model.getId(), "api process finished");
    }

    public void remove(String id) throws Exception {
        LogService.localLog(id, "delete api...");
        //??????context
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

            //????????????
            context.destroy();
        }

        //??????????????????
        removeMapping(id);

        //?????????????????????
        if (apiDao.existsById(id)) {
            apiDao.deleteById(id);
        }

        LogService.localLog(id, "delete api successful");
    }

    public String[] log(String day) throws Exception {
        File logFile = new File(Constants.LOG_ROOT, day);
        if (!logFile.exists()) {
            throw new Exception(day + "???????????????");
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
        //??????id???????????????????????????????????????
        Optional<ApiEntity> apiEntityOptional = apiDao.findById(model.getId());
        if (apiEntityOptional.isPresent()) {
            save(model, apiEntityOptional.get(), false);

            //???????????????
            removeMapping(model.getId());
        } else {
            //??????id ??????????????????path+method????????????????????????????????????
            apiEntityOptional = apiDao.findFirstByPathAndMethod(model.getPath(), model.getMethod());
            if (apiEntityOptional.isPresent()) {
                throw new Exception("api????????????,path+method????????????,(path=" + model.getPath() + ", method=" + model.getMethod() + ")");
            }

            //?????????????????????
            save(model, new ApiEntity(), true);
        }

        //??????????????????
        addMapping(model.getId(), MyUtils.parseMethod(model.getMethod()), model.getContentType(), model.getPath());
    }

    private void processNode(Context context, List<Node> nodes) throws Exception {
        if (nodes == null || nodes.size() < 2) {
            throw new Exception("??????????????????????????????");
        }

        //????????????
        List<BaseNodeService> nodeServices = new ArrayList<>();
        Map<String, BaseNodeService> maps = new HashMap<>();
        for (Node node : nodes) {
            BaseNodeService nodeService = nodeServiceFactory.createNodeService(node.getType());
            BeanUtils.copyProperties(node, nodeService);
            //?????????
            nodeService.init(node.getProperties());
            //????????????
            nodeServices.add(nodeService);

            maps.put(nodeService.getId(), nodeService);
        }
        context.setNodes(maps);

        //?????????????????????
        context.setFirstNode(nodeServices.get(0));
    }

    private void save(RunModel model, ApiEntity apiEntity, boolean isSave) {
        BeanUtils.copyProperties(model, apiEntity);

        //??????nodes
        apiEntity.setNodes(JSON.toJSONString(model.getNodes()));
        if (isSave) {
            apiEntity.setCreateTime(Calendar.getInstance());
        } else {
            apiEntity.setUpdateTime(Calendar.getInstance());
        }
        //??????????????????
        apiDao.save(apiEntity);
    }

    /**
     * ?????? api????????? ??????
     *
     * @param method      ????????????
     * @param contentType ??????????????????
     * @param path        ??????
     * @throws NoSuchMethodException ??????
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
    //??????????????????api???????????????
    public ResponseEntity<String> handleRequests(HttpServletRequest request) {
        Context context = null;
        try {
            context = recordApiService.getContext(request.getServletPath(), request.getMethod());
            LogService.localLog(context.getId(), "http receiver start...");

            //?????????????????????
            Payload payload = new Payload();
            payload.setHeaders(MyUtils.getHeaders(request));
            payload.setParams(MyUtils.getParams(request));
            payload.setBody(MyUtils.getBody(request));
            context.setPayload(payload);

            LogService.localLog(context.getId(), "receiver data???" + context.getPayloadJson().toJSONString());

            //?????????????????????
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
     * ??????????????????
     */
    private Specification<ApiEntity> getSpecification(JSONObject paramObject, SpecificationBuilder<ApiEntity> builder) throws Exception {
        SpecificationBuilder<ApiEntity> specificationBuilder = builder.init(paramObject)
                .sContain("name", "name")
                .sEqual("method", "method")
                .dOrder("createTime");
        return specificationBuilder.build();
    }

}
