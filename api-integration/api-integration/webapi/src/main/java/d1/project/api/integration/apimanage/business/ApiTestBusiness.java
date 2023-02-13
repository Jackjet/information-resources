package d1.project.api.integration.apimanage.business;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.project.api.integration.apimanage.entity.ApiAuthManage;
import d1.project.api.integration.apimanage.entity.ApiBaseInfo;
import d1.project.api.integration.apimanage.entity.ApiTestCase;
import d1.project.api.integration.apimanage.entity.ApiTestLog;
import d1.project.api.integration.apimanage.model.*;
import d1.project.api.integration.apimanage.service.ApiAuthManageService;
import d1.project.api.integration.apimanage.service.ApiBaseInfoService;
import d1.project.api.integration.apimanage.service.ApiTestCaseService;
import d1.project.api.integration.apimanage.service.ApiTestLogService;
import d1.project.api.integration.apimanage.view.entity.ApiTestList;
import d1.project.api.integration.apimanage.view.entity.ApiWithSourceApi;
import d1.project.api.integration.apimanage.view.service.ApiTestListService;
import d1.project.api.integration.apimanage.view.service.ApiWithSourceApiService;
import d1.project.api.integration.application.service.ApplicationService;
import d1.project.api.integration.application.service.NormalConsumerService;
import d1.project.api.integration.common.Constants;
import d1.project.api.integration.common.model.OperationLog;
import d1.project.api.integration.common.service.IOperationLogService;
import d1.project.api.integration.common.utils.BaseUtils;
import d1.project.api.integration.kong.entity.KongConsumer;
import d1.project.api.integration.kong.service.DefaultConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author baozh
 */
@Service
public class ApiTestBusiness {
    private static final Logger logger = LoggerFactory.getLogger("api");
    private final ApiWithSourceApiService apiWithSourceApiService;
    private final ApiTestLogService apiTestLogService;
    private final ApiBaseInfoService apiBaseInfoService;
    private final ApiTestCaseService apiTestCaseService;
    private final ApiTestListService apiTestListService;
    private final ApplicationService applicationService;
    private final IOperationLogService iOperationLogService;
    private final ApiAuthManageService apiAuthManageService;
    private final DefaultConsumerService defaultConsumerService;
    private final NormalConsumerService normalConsumerService;

    @Value("${kong.base.http}")
    private String kongServiceHttp;
    @Value("${kong.base.https}")
    private String kongServiceHttps;

    public ApiTestBusiness(ApiWithSourceApiService apiWithSourceApiService, ApiTestLogService apiTestLogService, ApiBaseInfoService apiBaseInfoService, ApiTestCaseService apiTestCaseService, ApiTestListService apiTestListService, ApplicationService applicationService, IOperationLogService iOperationLogService, ApiAuthManageService apiAuthManageService, DefaultConsumerService defaultConsumerService, NormalConsumerService normalConsumerService) {
        this.apiWithSourceApiService = apiWithSourceApiService;
        this.apiTestLogService = apiTestLogService;
        this.apiBaseInfoService = apiBaseInfoService;
        this.apiTestCaseService = apiTestCaseService;
        this.apiTestListService = apiTestListService;
        this.applicationService = applicationService;
        this.iOperationLogService = iOperationLogService;
        this.apiAuthManageService = apiAuthManageService;
        this.defaultConsumerService = defaultConsumerService;
        this.normalConsumerService = normalConsumerService;
    }

    /**
     * 查询API列表
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public Page<ApiWithSourceApi> findApiList(JSONObject params) throws Exception {
        return apiWithSourceApiService.findAll(params);
    }

    /**
     * 根据API ID获取最近七天的历史记录
     *
     * @param apiId API ID
     * @return 查询结果
     */
    public List<ApiLogReturnVm> findApiLogList(String apiId) {
        Calendar endTime = BaseUtils.todayLastTime();
        Calendar startTime = BaseUtils.sevenLastTime();
        List<ApiLogReturnVm> returnVmList = new Vector<>();
        List<ApiLogVm> list = apiTestLogService.findLogList(apiId, startTime, endTime);
        if (list.size() > 0) {
            Map<String, List<ApiLogVm>> groups = list.stream().collect(Collectors.groupingBy(ApiLogVm::getRequestDay));
            Integer id = 0;
            for (Map.Entry<String, List<ApiLogVm>> entry : groups.entrySet()) {
                ApiLogReturnVm apiLogReturnVm = new ApiLogReturnVm();
                apiLogReturnVm.setTime(entry.getKey());
                apiLogReturnVm.setLogList(entry.getValue());
                apiLogReturnVm.setId(id);
                returnVmList.add(apiLogReturnVm);
                id++;
            }
        }
        return returnVmList.stream().sorted(Comparator.comparing(ApiLogReturnVm::getTime).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 删除API测试记录
     *
     * @param id ID
     */
    public void deleteApiTestLog(String id, HttpServletRequest request) throws Exception {
        apiTestLogService.deleteById(id);
        iOperationLogService.insert(new OperationLog("API测试", "删除测试记录", "删除测试记录", "删除测试记录" + id, 1), request);
    }

    /**
     * 根据KEY获取签名
     *
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public JSONObject getHeader(JSONObject params) throws Exception {
        String appId = params.getString("appId");
        KongConsumer consumer = null;
        if(StringUtils.isEmpty(appId)){
            consumer = defaultConsumerService.findByContainer(params.getString("container"));
        } else {
            consumer = normalConsumerService.findByAppid(appId);
        }

        ApiBaseInfo apiBaseInfo = apiBaseInfoService.findById(params.getString("apiId"));

        JSONObject header = null;
        switch (apiBaseInfo.getVerificationMode()){
            case "0":
                header = getHmacHeader(consumer,params);
                break;
            case "1":
                header = getKeyAuthHeader(consumer,params);
                break;
            default:
                throw new DoValidException("非法鉴权类型");
        }
        return header;
    }

    /**
     * 查询测试用例
     *
     * @param id 数据ID
     * @return 查询结果
     */
    public ApiTestCase findApiTestCaseById(String id) throws Exception {
        return apiTestCaseService.findById(id);
    }


    /**
     * 保存测试用例
     *
     * @param insertVm 添加数据
     * @param request  请求信息
     * @throws Exception 抛出添加异常
     */
    public void saveApiTestCase(ApiTestCaseInsertVm insertVm, HttpServletRequest request) throws Exception {
        String id = insertVm.getId();
        ApiTestCase apiTestCase;
        String userId = BaseUtils.getUserId(request);
        if (StringUtils.isEmpty(id)) {
            apiTestCase = new ApiTestCase();
            BeanUtils.copyProperties(insertVm, apiTestCase);
            apiTestCase.setCreateById(userId);
            apiTestCase.setCreateTime(Calendar.getInstance());
            apiTestCase.setId(BaseUtils.generate32Id());
        } else {
            apiTestCase = apiTestCaseService.findById(id);
            BeanUtils.copyProperties(insertVm, apiTestCase);
        }
        apiTestCaseService.saveInfo(apiTestCase);
        iOperationLogService.insert(new OperationLog("API测试", "保存测试记录", "保存测试记录", "保存测试记录" + id, 1), request);
    }

    /**
     * 添加测试结果
     *
     * @param str 测试信息
     */
    public void insertTestResult(InsertTestRecord str, HttpServletRequest request) throws Exception {
        ApiTestLog apiTestLog = new ApiTestLog();
        apiTestLog.setApiId(str.getApiId());
        apiTestLog.setRequestTime(Calendar.getInstance());
        String resultStr = str.getResultStr();
        JSONObject json = JSONObject.parseObject(resultStr);
        apiTestLog.setResponseCode(json.getString("status"));
        apiTestLog.setRequestContent(str.getRequestStr());
        apiTestLog.setResponseTime(str.getTime());
        apiTestLog.setResponseContent(json.toJSONString());
        apiTestLog.setMethod(str.getMethod());
        apiTestLog.setId(BaseUtils.generate32Id());
        apiTestLogService.saveInfo(apiTestLog);
        iOperationLogService.insert(new OperationLog("API测试", "添加测试结果", "添加测试结果", "添加测试结果", 1), request);
    }

    /**
     * 查询测试用例列表
     *
     * @param apiId API ID
     * @return 查询结果
     */
    public List<ApiTestCaseReturnVm> findApiTestCase(String apiId) {
        Calendar endTime = BaseUtils.todayLastTime();
        Calendar startTime = BaseUtils.sevenLastTime();
        List<ApiTestCaseReturnVm> returnVmList = new Vector<>();
        List<ApiTestCaseVm> list = apiTestCaseService.findCaseByApiId(apiId, startTime, endTime);
        if (list.size() > 0) {
            Map<String, List<ApiTestCaseVm>> groups = list.stream().collect(Collectors.groupingBy(ApiTestCaseVm::getTime));
            Integer id = 0;
            for (Map.Entry<String, List<ApiTestCaseVm>> entry : groups.entrySet()) {
                ApiTestCaseReturnVm apiTestCaseReturnVm = new ApiTestCaseReturnVm();
                apiTestCaseReturnVm.setTime(entry.getKey());
                apiTestCaseReturnVm.setCaseList(entry.getValue());
                apiTestCaseReturnVm.setId(id);
                returnVmList.add(apiTestCaseReturnVm);
                id++;
            }
        }
        return returnVmList.stream().sorted(Comparator.comparing(ApiTestCaseReturnVm::getTime).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 删除API测试用例
     *
     * @param id ID
     */
    public void deleteApiTestCase(String id, HttpServletRequest request) throws Exception {
        apiTestCaseService.deleteById(id);
        iOperationLogService.insert(new OperationLog("API测试", "删除API测试记录", "删除API测试记录", "删除API测试记录" + id, 1), request);
    }

    /**
     * 获取请求/响应内容
     *
     * @param id   数据ID
     * @param type 获取内容类型
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public String getContent(String id, Integer type) throws Exception {
        ApiTestLog apiTestLog = apiTestLogService.findById(id);
        String msg;
        switch (type) {
            case 0:
                msg = apiTestLog.getRequestContent();
                break;
            case 1:
                msg = apiTestLog.getResponseContent();
                break;
            default:
                msg = "暂无数据。";
        }
        return msg;
    }

    /**
     * 查询测试记录
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public Page<ApiTestList> findApiTestLogList(JSONObject params) throws Exception {
        return apiTestListService.findAll(params);
    }

    /**
     * 导入测试用例
     *
     * @param file    测试用例文件
     * @param request 请求
     * @throws Exception 异常处理
     */
    public void importCase(MultipartFile file, HttpServletRequest request, String apiId, String method) throws Exception {
        Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(reader);
        StringBuilder stb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            if (!StringUtils.isEmpty(line)) {
                stb.append(line);
            }
        }
        br.close();
        reader.close();
        String jsonStr = stb.toString();
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        if (jsonObject == null) {
            throw new Exception(Constants.FILE_ERROR);
        }
        String ip = jsonObject.getString("ip");
        if (StringUtils.isEmpty(ip)) {
            throw new Exception(Constants.FILE_ERROR);
        }
        String content = jsonObject.getJSONObject("content").toJSONString();
        if (StringUtils.isEmpty(content)) {
            throw new Exception(Constants.FILE_ERROR);
        }
        ApiTestCase apiTestCase = new ApiTestCase();
        apiTestCase.setIp(ip);
        if (StringUtils.isEmpty(method)) {
            method = "GET";
        }
        apiTestCase.setMethod(method);
        apiTestCase.setSaveResult(0);
        apiTestCase.setContent(content);
        apiTestCase.setApiId(apiId);
        apiTestCase.setCreateById(BaseUtils.getUserId(request));
        apiTestCase.setCreateTime(Calendar.getInstance());
        apiTestCase.setId(BaseUtils.generate32Id());
        apiTestCaseService.saveInfo(apiTestCase);
        iOperationLogService.insert(new OperationLog("API测试", "导入测试记录", "导入测试记录", "导入测试记录", 1), request);
    }

    /**
     * 导出测试用例JSON文件
     *
     * @param id 数据ID
     * @throws Exception 抛出异常
     */
    public void exportCase(String id, HttpServletResponse response, HttpServletRequest request) throws Exception {
        ApiTestCase apiTestCase = apiTestCaseService.findById(id);
        String apiId = apiTestCase.getApiId();
        ApiBaseInfo apiBaseInfo = apiBaseInfoService.findById(apiId);
        BaseUtils.exportText(apiBaseInfo.getName(), apiTestCase.getIp(), apiTestCase.getMethod(), apiTestCase.getContent(), response);
        iOperationLogService.insert(new OperationLog("API测试", "导出测试记录", "导出测试记录", "导出测试记录", 1), request);
    }

    /**
     * 查询API是否存在授权
     *
     * @param apiId API Id
     * @return 查询结果
     */
    public Boolean hasAuth(String apiId) {
        return apiAuthManageService.existsAllByApiId(apiId);
    }

    //============================================================================
    private JSONObject getHmacHeader(KongConsumer consumer, JSONObject params) throws InvalidKeyException, NoSuchAlgorithmException {
        StringBuilder stb = new StringBuilder();
        String hDate = BaseUtils.getGmtTime();
        String content = stb.append("x-date: ").append(hDate)
                .toString();
        String signature = BaseUtils.getSignature(content.getBytes(StandardCharsets.UTF_8), consumer.getKey().getBytes(StandardCharsets.UTF_8));
        String authorization = "hmac username=\"" + consumer.getUsername() + "\",algorithm=\"hmac-sha256\",headers=\"x-date\",signature=\"" + signature + "\"";

        JSONObject headers = JSONObject.parseObject(params.getString("headers"));
        if(headers == null) {
            headers = new JSONObject();
        }

        headers.put("x-date", hDate);
        headers.put("Authorization", authorization);
        return headers;
    }

    private JSONObject getKeyAuthHeader(KongConsumer consumer,JSONObject params) {
        JSONObject headers = JSONObject.parseObject(params.getString("headers"));
        if(headers == null) {
            headers = new JSONObject();
        }

        headers.put("apikey", consumer.getKey());
        return headers;
    }
}
