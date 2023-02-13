package d1.project.api.integration.apimanage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.api.integration.apimanage.business.ApiManageBusiness;
import d1.project.api.integration.apimanage.business.ApiTestBusiness;
import d1.project.api.integration.apimanage.entity.ApiTestCase;
import d1.project.api.integration.apimanage.model.*;
import d1.project.api.integration.apimanage.view.entity.ApiTestList;
import d1.project.api.integration.apimanage.view.entity.ApiWithSourceApi;
import d1.project.api.integration.common.annotation.ApiAuth;
import d1.project.api.integration.common.model.TimePageableVm;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author baozh
 */
@ApiAuth("webadmin")
@RestController
@RequestMapping("/webadmin/apiTestManage")
public class ApiTestManageController {

    private final ApiTestBusiness apiTestBusiness;
    private final ApiManageBusiness apiManageBusiness;

    public ApiTestManageController(ApiTestBusiness apiTestBusiness, ApiManageBusiness apiManageBusiness) {
        this.apiTestBusiness = apiTestBusiness;
        this.apiManageBusiness = apiManageBusiness;
    }

    @GetMapping(value = "/apiList")
    public Result<Page<ApiWithSourceApi>> apiList(ApiTestFindVm findVm) {
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(findVm));
        try {
            return ResultUtil.success("成功", apiTestBusiness.findApiList(json));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/historyList")
    public Result<List<ApiLogReturnVm>> historyList(String apiId) {
        try {
            return ResultUtil.success("成功", apiTestBusiness.findApiLogList(apiId));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @DeleteMapping(value = "/deleteApiTestLog")
    public Result<String> deleteApiTestLog(String id, HttpServletRequest request) {
        try {
            apiTestBusiness.deleteApiTestLog(id, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @PostMapping(value = "/saveApiTestCase")
    public Result<String> saveApiTestCase(@RequestBody ApiTestCaseInsertVm insertVm, HttpServletRequest request) {
        try {
            apiTestBusiness.saveApiTestCase(insertVm, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @PostMapping(value = "/getApiTestHeader")
    public Result<JSONObject> getApiTestHeader(@RequestBody ApiTestHeader testApiVm) {
        try {
            return ResultUtil.success("成功", apiTestBusiness.getHeader((JSONObject) JSON.toJSON(testApiVm)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/testCaseList")
    public Result<List<ApiTestCaseReturnVm>> testCaseList(String apiId) {
        try {
            return ResultUtil.success("成功", apiTestBusiness.findApiTestCase(apiId));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @DeleteMapping(value = "/deleteApiTestCase")
    public Result<String> deleteApiTestCase(String id, HttpServletRequest request) {
        try {
            apiTestBusiness.deleteApiTestCase(id, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/getContent")
    public Result<String> getContent(String id, Integer type) {
        try {
            return ResultUtil.success("成功", apiTestBusiness.getContent(id, type));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/apiTestLogList")
    public Result<Page<ApiTestList>> apiTestLogList(ApiTestLogFindVm findVm) {
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(findVm));
        try {
            return ResultUtil.success("成功", apiTestBusiness.findApiTestLogList(json));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @PostMapping(value = "/importCase")
    public Result<String> importCase(@RequestParam("file") MultipartFile file, @RequestParam("method") String method, @RequestParam("apiId") String apiId, HttpServletRequest request) {
        try {
            apiTestBusiness.importCase(file, request, apiId, method);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/exportCase")
    public Result<String> exportCase(String id, HttpServletResponse response, HttpServletRequest request) {
        try {
            apiTestBusiness.exportCase(id, response, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/findApiTestCaseById")
    public Result<ApiTestCase> findApiTestCaseById(String id) {
        try {
            return ResultUtil.success("成功", apiTestBusiness.findApiTestCaseById(id));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @PostMapping(value = "/insertTestResult")
    public Result<String> insertTestResult(@RequestBody InsertTestRecord str, HttpServletRequest request) {
        try {
            apiTestBusiness.insertTestResult(str, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/hasAuth")
    public Result<Boolean> hasAuth(String apiId) {
        try {
            return ResultUtil.success("成功", apiTestBusiness.hasAuth(apiId));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/findById")
    public Result<JSONObject> findById(String id) {
        try {
            return ResultUtil.success("成功", apiManageBusiness.findById(id));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }
}


class ApiTestFindVm extends PageableVm {
    private String apiName;
    private String sourceApiName;

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getSourceApiName() {
        return sourceApiName;
    }

    public void setSourceApiName(String sourceApiName) {
        this.sourceApiName = sourceApiName;
    }
}

class ApiTestLogFindVm extends TimePageableVm {
    private String apiId;

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }
}

class GetSignVm {
    private String key;
    private String apiId;
    private String ip;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}