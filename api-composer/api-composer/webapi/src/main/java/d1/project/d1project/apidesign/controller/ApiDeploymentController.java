package d1.project.d1project.apidesign.controller;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.d1project.apidesign.business.ApiDesignBusiness;
import d1.project.d1project.apidesign.entity.ApiDeployment;
import d1.project.d1project.apidesign.entity.ApiDesign;
import d1.project.d1project.apidesign.model.ApiDesignedFindVm;
import d1.project.d1project.apidesign.model.DeployApiVm;
import d1.project.d1project.common.annotation.ApiAuth;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * API部署
 *
 * @author baozh
 */

//@Auth("webadmin")
@ApiAuth("webadmin")
@RestController
@RequestMapping("/webadmin/apiDeployment")
public class ApiDeploymentController {
    private final ApiDesignBusiness apiDesignBusiness;

    public ApiDeploymentController(ApiDesignBusiness apiDesignBusiness) {
        this.apiDesignBusiness = apiDesignBusiness;
    }

    @GetMapping(value = "/findDesign")
    public Result<Page<ApiDesign>> findDesign(ApiDesignedFindVm findVm) {
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(findVm));
        try {
            return ResultUtil.success("成功", apiDesignBusiness.findDesignedList(json));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @DeleteMapping(value = "/deleteById")
    public Result<String> deleteById(String id, HttpServletRequest request) {
        try {
            apiDesignBusiness.deleteDeploy(id, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @PostMapping(value = "/deployApi")
    public Result<String> deployApi(@RequestBody DeployApiVm deployApiVm, HttpServletRequest request) {
        try {
            return ResultUtil.success("成功", apiDesignBusiness.deployApi(deployApiVm, request));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/testInfo")
    public Result<List<ApiDeployment>> testInfo(String id) {
        try {
            return ResultUtil.success("成功", apiDesignBusiness.findTestList(id));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }
}

