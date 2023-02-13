package d1.project.d1project.apidesign.business;

import com.alibaba.fastjson.JSONObject;
import d1.project.d1project.apidesign.entity.ApiDesign;
import d1.project.d1project.apidesign.entity.ApiTestCase;
import d1.project.d1project.apidesign.entity.ApiTestList;
import d1.project.d1project.apidesign.entity.ApiTestLog;
import d1.project.d1project.apidesign.model.*;
import d1.project.d1project.apidesign.service.*;
import d1.project.d1project.common.Constants;
import d1.project.d1project.common.model.OperationLog;
import d1.project.d1project.common.service.IOperationLogService;
import d1.project.d1project.common.utils.BaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author baozh
 */
@Service
public class ApiTestBusiness {
    private static final Logger logger = LoggerFactory.getLogger("api");
    private final ApiTestLogService apiTestLogService;
    private final ApiTestCaseService apiTestCaseService;
    private final IOperationLogService iOperationLogService;
    private final ApiDesignService apiDesignService;
    private final ApiTestListService apiTestListService;
    private final WebAdminUserService webAdminUserService;

    public ApiTestBusiness(ApiTestLogService apiTestLogService, ApiTestCaseService apiTestCaseService, IOperationLogService iOperationLogService, ApiDesignService apiDesignService, ApiTestListService apiTestListService, WebAdminUserService webAdminUserService) {
        this.apiTestLogService = apiTestLogService;
        this.apiTestCaseService = apiTestCaseService;
        this.iOperationLogService = iOperationLogService;
        this.apiDesignService = apiDesignService;
        this.apiTestListService = apiTestListService;
        this.webAdminUserService = webAdminUserService;
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
     * 获取API的具体测试参数
     *
     * @param apiId api id
     * @param appId app Id
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public ApiTestDetailVm apiTestParams(String apiId, String appId) throws Exception {
        if (StringUtils.isEmpty(apiId)) {
            throw new Exception(Constants.API_IS_NULL);
        }

        ApiDesign apiDesign = apiDesignService.findById(apiId);

        ApiTestDetailVm apiTestDetailVm = new ApiTestDetailVm();
        apiTestDetailVm.setApiId(apiId);
        apiTestDetailVm.setName(apiDesign.getName());
        //apiTestDetailVm.setHost(sourceApi.getHost());
        apiTestDetailVm.setPath(apiDesign.getRequestUrl());
        apiTestDetailVm.setMethod(apiDesign.getMethod());
        //apiTestDetailVm.setRouteInfo(apiBaseInfo.getRouteInfo());
        apiTestDetailVm.setParams(apiDesign.getParams());
        return apiTestDetailVm;
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
        if (StringUtils.isEmpty(id)) {
            apiTestCase = new ApiTestCase();
            BeanUtils.copyProperties(insertVm, apiTestCase);
            apiTestCase.setId(BaseUtils.generate32Id());
            webAdminUserService.updateCreateIdAndTime(request, apiTestCase);
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
        apiTestCase.setId(BaseUtils.generate32Id());
        webAdminUserService.updateCreateIdAndTime(request, apiTestCase);
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
        ApiDesign apiDesign = apiDesignService.findById(apiId);
        BaseUtils.exportText(apiDesign.getName(), apiTestCase.getIp(), apiTestCase.getMethod(), apiTestCase.getContent(), response);
        iOperationLogService.insert(new OperationLog("API测试", "导出测试记录", "导出测试记录", "导出测试记录", 1), request);
    }


    /**
     * 拼装空头部信息
     *
     * @param testApiVm 测试Api数据
     * @return 测试结果
     */
    public JSONObject getApiTestHeader(ApiTestHeader testApiVm) {
        String sign = testApiVm.getSign();
        String hDate = testApiVm.gethDate();
        String strHeaders = testApiVm.getHeaders();
        JSONObject headers = JSONObject.parseObject(strHeaders);
        return getTempKongHeaders(sign, headers, hDate, "");
    }

    private JSONObject getTempKongHeaders(String signature, JSONObject headerInfo, String hDate, String username) {
        String authorization = "hmac username=\"" + username + "\",algorithm=\"hmac-sha256\",headers=\"x-date\",signature=\"" + signature + "\"";

        JSONObject headers = new JSONObject();
        headers.put("x-date", hDate);
        headers.put("Authorization", authorization);
        headers.put("signature", signature);
        if (headerInfo != null) {
            for (Map.Entry<String, Object> entry : headerInfo.entrySet()) {
                headers.put(entry.getKey(), entry.getValue().toString());
            }
        }
        return headers;
    }
}
