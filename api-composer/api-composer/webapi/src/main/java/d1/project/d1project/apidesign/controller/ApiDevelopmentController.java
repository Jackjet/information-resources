package d1.project.d1project.apidesign.controller;

import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.d1project.apidesign.business.ApiDesignBusiness;
import d1.project.d1project.apidesign.entity.ApiDevelopment;
import d1.project.d1project.apidesign.model.GenerateSqlVm;
import d1.project.d1project.apidesign.model.RequestMetaDataVm;
import d1.project.d1project.common.annotation.ApiAuth;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * API开发接口
 *
 * @author baozh
 */

//@Auth("webadmin")
@ApiAuth("webadmin")
@RestController
@RequestMapping("/webadmin/apiDevelopment")
public class ApiDevelopmentController {
    private final ApiDesignBusiness apiDesignBusiness;

    public ApiDevelopmentController(ApiDesignBusiness apiDesignBusiness) {
        this.apiDesignBusiness = apiDesignBusiness;
    }

    @GetMapping(value = "/findByApiId")
    public Result<ApiDevelopment> findByApiId(String apiId) {
        try {
            return ResultUtil.success("成功", apiDesignBusiness.findApiDevelopment(apiId));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    /**
     * 获取api的请求参数，包括 parameter、header、body
     */
    @GetMapping(value = "/findRequestMetaData")
    public Result<RequestMetaDataVm> findRequestMetaData(String apiId) {
        try {
            return ResultUtil.success("成功", apiDesignBusiness.findRequestMetaData(apiId));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    /**
     * 生成sql语句
     */
    @PostMapping(value = "/generateSql")
    public Result<String> generateSql(@RequestBody GenerateSqlVm model) {
        try {
            return ResultUtil.success("成功", apiDesignBusiness.generateSql(model));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @PostMapping(value = "/insert")
    public Result<String> insert(@RequestBody ApiDevelopment insertVm, HttpServletRequest request) {
        try {
            apiDesignBusiness.insertApiDevelopment(insertVm, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/exportCase")
    public Result<String> exportCase(String id, HttpServletResponse response) {
        try {
            apiDesignBusiness.exportCase(id, response);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }
}
