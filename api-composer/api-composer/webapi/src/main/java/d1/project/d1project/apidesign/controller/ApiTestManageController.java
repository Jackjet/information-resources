package d1.project.d1project.apidesign.controller;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.d1project.apidesign.business.ApiTestBusiness;
import d1.project.d1project.apidesign.entity.ApiTestCase;
import d1.project.d1project.apidesign.entity.ApiTestList;
import d1.project.d1project.apidesign.model.*;
import d1.project.d1project.common.annotation.ApiAuth;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author baozh
 */
//@Auth("webadmin")
@ApiAuth("webadmin")
@RestController
@RequestMapping("/webadmin/apiTestManage")
public class ApiTestManageController {

    private final ApiTestBusiness apiTestBusiness;

    public ApiTestManageController(ApiTestBusiness apiTestBusiness) {
        this.apiTestBusiness = apiTestBusiness;
    }

    @GetMapping(value = "/historyList")
    public Result<List<ApiLogReturnVm>> historyList(String apiId) {
        try {
            return ResultUtil.success("成功", apiTestBusiness.findApiLogList(apiId));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/apiTestParams")
    public Result<ApiTestDetailVm> apiTestParams(String apiId, String appId) {
        try {
            return ResultUtil.success("成功", apiTestBusiness.apiTestParams(apiId, appId));
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
            return ResultUtil.success("成功", apiTestBusiness.getApiTestHeader(testApiVm));
        } catch (Exception e) {
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
}