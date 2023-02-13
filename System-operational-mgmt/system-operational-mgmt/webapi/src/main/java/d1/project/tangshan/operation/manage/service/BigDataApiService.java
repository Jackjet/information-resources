package d1.project.tangshan.operation.manage.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.dao.BigDataApiDao;
import d1.project.tangshan.operation.manage.entity.BigDataApiEntity;
import d1.project.tangshan.operation.manage.service.operations.log.OperationLogService;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import net.dcrun.component.http.HttpService;
import net.dcrun.component.webservice.client.WebserviceClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author Buter
 * @date 2020/3/16 6:50
 */
@Service
public class BigDataApiService {
    private final BigDataApiDao bigDataApiDao;
    private final TokenService tokenService;
    private final OperationLogService operationLogService;

    @Autowired
    public BigDataApiService(BigDataApiDao bigDataApiDao, TokenService tokenService, OperationLogService operationLogService) {
        this.bigDataApiDao = bigDataApiDao;
        this.tokenService = tokenService;
        this.operationLogService = operationLogService;
    }

    public Page<BigDataApiEntity> findAll(JSONObject params, HttpServletRequest request) throws Exception {
        params.put("createById", tokenService.getUserByToken(request).getString("id"));
        SpecificationBuilder<BigDataApiEntity> builder = new SpecificationBuilder<>();
        Specification<BigDataApiEntity> specification = builder.init(params)
                .sContain("name", "name")
                .sEqual("type", "type")
                .sNotEqual("type", "excludeType")
                .dOrder("createTime").build();

        return this.bigDataApiDao.findAll(specification, builder.getPageable());
    }

    public BigDataApiEntity findById(String id) throws Exception {
        return bigDataApiDao.findById(id).orElseThrow(() -> new ValidException("该数据服务接口不存在"));
    }


    public void insert(JSONObject params, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        BigDataApiEntity entity = JSON.toJavaObject(params, BigDataApiEntity.class);
        String name = entity.getName();
        try {
            MyUtils.throwMsg(bigDataApiDao.existsByName(entity.getName()), "接口名称已存在");
            MyUtils.throwMsg(0 < bigDataApiDao.existsByTypeAndUrlOrIpAndMethodType(entity.getType(), entity.getUrlOrIp(), entity.getMethodType()), "该服务类型下已存在请求方法和请求URL/IP相同的接口，请勿重复添加");
            entity.setId(MyUtils.generate32Id());
            tokenService.updateCreateIdAndTime(request, entity);
            bigDataApiDao.save(entity);

            operationLogService.setLog("数据服务接口管理—添加", userName, "大数据处理服务-数据服务接口管理", "添加了一个数据服务接口:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("数据服务接口管理—添加", userName, "大数据处理服务-数据服务接口管理", "添加了一个数据服务接口:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    public void update(JSONObject params, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            String id = params.getString("id");
            BigDataApiEntity entity = bigDataApiDao.findById(id).orElseThrow(() -> new ValidException("找不到记录：" + id));
            name = entity.getName();
            String type = params.getString("type");
            String url = params.getString("urlOrIp");
            String methodType = params.getString("methodType");
            if (!type.equals(entity.getType()) || !url.equals(entity.getUrlOrIp()) || !methodType.equals(entity.getMethodType())) {
                MyUtils.throwMsg(0 < bigDataApiDao.existsByTypeAndUrlOrIpAndMethodType(type, url, methodType), "该服务类型下已存在请求方法和请求URL/IP相同的接口，请勿重复添加");
                entity.setType(type);
                entity.setUrlOrIp(url);
                entity.setMethodType(methodType);
            }
            entity.setRemark(params.getString("remark"));
            tokenService.updateUpdateIdAndTime(request, entity);
            bigDataApiDao.save(entity);
            operationLogService.setLog("数据服务接口管理—编辑", userName, "大数据处理服务-数据服务接口管理", "编辑了一个数据服务接口:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("数据服务接口管理—编辑", userName, "大数据处理服务-数据服务接口管理", "编辑了一个数据服务接口:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    public void delete(String id, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            BigDataApiEntity entity = bigDataApiDao.findById(id).orElseThrow(() -> new ValidException("找不到记录：" + id));
            name = entity.getName();
            bigDataApiDao.delete(entity);

            operationLogService.setLog("数据服务接口管理—删除", userName, "大数据处理服务-数据服务接口管理", "删除了一个数据服务接口:" + name, "1", request);
        } catch (ValidException e) {
            operationLogService.setLog("数据服务接口管理—删除", userName, "大数据处理服务-数据服务接口管理", "删除了一个数据服务接口:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    /**
     * 测试接口
     *
     * @param params 请求参数
     * @return java.lang.String
     * @author Kikki  2020/6/26 23:12
     */
    public String testInterface(JSONObject params, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        String type = "未知";
        String methodType = "未知";
        try {
            String id = params.getString("id");
            BigDataApiEntity bigDataApiEntity = bigDataApiDao.findById(id).orElseThrow(() -> new ValidException("无效的接口"));
            type = bigDataApiEntity.getType();
            methodType = bigDataApiEntity.getMethodType();
            name = bigDataApiEntity.getName();
            /*接口类型，http/webservice/socket*/
            String urlOrIp = bigDataApiEntity.getUrlOrIp();
            switch (bigDataApiEntity.getType()) {
                case "http":
                    String body1 = params.getString("body");
                    body1 = (body1 == null) ? "" : body1;
                    String header = params.getString("header");
                    header = (header == null) ? "" : header;
                    String urlParameter1 = params.getString("urlParameter");
                    urlParameter1 = (urlParameter1 == null) ? "" : urlParameter1;

                    HttpService httpService = new HttpService();
                    httpService.setTimeout(10 * 60, 10 * 60, 10 * 60);
                    /*get/post/put/delete*/
                    JSONObject headers = JSON.parseObject(header);
                    JSONObject parameters = JSON.parseObject(body1);
                    String contentType = "application/json";
                    switch (bigDataApiEntity.getMethodType()) {
                        case "get":
                            operationLogService.setLog("数据服务接口管理—测试", userName, "大数据处理服务-数据服务接口管理", "测试了" + type + "服务类型" + methodType + "服务方法的" + name + "接口", "1", request);
                            return httpService.get(urlOrIp + urlParameter1, headers, parameters);
                        case "post":
                            operationLogService.setLog("数据服务接口管理—测试", userName, "大数据处理服务-数据服务接口管理", "测试了" + type + "服务类型" + methodType + "服务方法的" + name + "接口", "1", request);
                            return httpService.post(urlOrIp + urlParameter1, headers, parameters, contentType);
                        case "put":
                            operationLogService.setLog("数据服务接口管理—测试", userName, "大数据处理服务-数据服务接口管理", "测试了" + type + "服务类型" + methodType + "服务方法的" + name + "接口", "1", request);
                            return httpService.put(urlOrIp + urlParameter1, headers, parameters, contentType);
                        case "delete":
                            operationLogService.setLog("数据服务接口管理—测试", userName, "大数据处理服务-数据服务接口管理", "测试了" + type + "服务类型" + methodType + "服务方法的" + name + "接口", "1", request);
                            return httpService.delete(urlOrIp + urlParameter1, headers, parameters, contentType);
                        default:
                            throw new Exception("无效的请求方式:get/post/put/delete");
                    }

                case "webservice":
                    String urlParameter2 = params.getString("urlParameter");
                    urlParameter2 = (urlParameter2 == null) ? "" : urlParameter2;

                    WebserviceClientService service = new WebserviceClientService();
                    Object[] serviceParams = new Object[0];
                    String body2 = params.getString("body");
                    body2 = (body2 == null) ? "" : body2;
                    body2.replace("，", ",");
                    String[] bodyArr = body2.split("\\^");
                    if (bodyArr.length == 2) {
                        String[] paramArr = bodyArr[1].split(",");
                        if (bodyArr[0].equals("int")) {
                            serviceParams = new Integer[paramArr.length];
                            for (int i = 0; i < paramArr.length; i++) {
                                serviceParams[i] = Integer.parseInt(paramArr[i]);
                            }
                        } else if (bodyArr[0].equals("long")) {
                            serviceParams = new Long[paramArr.length];
                            for (int i = 0; i < paramArr.length; i++) {
                                serviceParams[i] = Long.parseLong(paramArr[i]);
                            }
                        } else if (bodyArr[0].equals("float")) {
                            serviceParams = new Float[paramArr.length];
                            for (int i = 0; i < paramArr.length; i++) {
                                serviceParams[i] = Float.parseFloat(paramArr[i]);
                            }
                        } else if (bodyArr[0].equals("double")) {
                            serviceParams = new Double[paramArr.length];
                            for (int i = 0; i < paramArr.length; i++) {
                                serviceParams[i] = Double.parseDouble(paramArr[i]);
                            }
                        } else {
                            serviceParams = paramArr;
                        }
                    }
                    operationLogService.setLog("数据服务接口管理—测试", userName, "大数据处理服务-数据服务接口管理", "测试了" + type + "服务类型" + methodType + "服务方法的" + name + "接口", "1", request);
                    return service.request(urlOrIp + urlParameter2, bigDataApiEntity.getMethodType(), serviceParams);
                case "socket":
                    boolean contains = urlOrIp.contains(":");
                    if (!contains) {
                        throw new Exception("ip格式不对");
                    }
                    //发送请求
                    String[] split = urlOrIp.split(":");
                    Socket socket = new Socket(split[0], Integer.parseInt(split[1]));
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    String body3 = params.getString("body");
                    body3 = (body3 == null) ? "" : body3;
                    out.write(body3.getBytes(StandardCharsets.UTF_8));

                    //接收返回信息
                    InputStream is = socket.getInputStream();
                    DataInputStream dis = new DataInputStream(is);
                    byte[] buffer = new byte[8 * 1024];
                    int len;
                    String result = "";
                    if ((len = dis.read(buffer)) != -1) {
                        result = new String(buffer, 0, len, StandardCharsets.UTF_8);
                    }
                    operationLogService.setLog("数据服务接口管理—测试", userName, "大数据处理服务-数据服务接口管理", "测试了" + type + "服务类型" + methodType + "服务方法的" + name + "接口", "1", request);
                    return result;
                default:
                    throw new Exception("无效的接口类型");

            }

        } catch (Exception e) {
            operationLogService.setLog("数据服务接口管理—测试", userName, "大数据处理服务-数据服务接口管理", "测试了" + type + "服务类型" + methodType + "服务方法的" + name + "接口", "0", request);
            throw new ValidException(e.getMessage());
        }
    }
}
