package d1.project.d1project.apidesign.controller;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.d1project.apidesign.business.ApiDesignBusiness;
import d1.project.d1project.apidesign.entity.ApiDesign;
import d1.project.d1project.apidesign.model.ApiDesignFindVm;
import d1.project.d1project.common.annotation.ApiAuth;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * API设计接口
 *
 * @author baozh
 */

//@Auth("webadmin")
@ApiAuth("webadmin")
@RestController
@RequestMapping("/webadmin/apiDesign")
public class ApiDesignController {
    private final ApiDesignBusiness apiDesignBusiness;

    public ApiDesignController(ApiDesignBusiness apiDesignBusiness) {
        this.apiDesignBusiness = apiDesignBusiness;
    }

    @GetMapping(value = "/findAll")
    public Result<Page<ApiDesign>> findAll(ApiDesignFindVm findVm) {
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(findVm));
        try {
            return ResultUtil.success("成功", apiDesignBusiness.findAllApiDesign(json));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/findById")
    public Result<ApiDesign> findById(String id) {
        try {
            return ResultUtil.success("成功", apiDesignBusiness.findApiDesign(id));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @PostMapping(value = "/insert")
    public Result<String> insert(@RequestBody ApiDesign apiDesign, HttpServletRequest request) {
        try {
            return ResultUtil.success("成功",apiDesignBusiness.insertApiDesign(apiDesign, request));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @PutMapping(value = "/update")
    public Result<String> update(@RequestBody ApiDesign apiDesign, HttpServletRequest request) {
        try {
            return ResultUtil.success("成功",apiDesignBusiness.updateApiDesign(apiDesign, request));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @DeleteMapping(value = "/deleteById")
    public Result<String> deleteById(String id,HttpServletRequest request) {
        try {
            apiDesignBusiness.deleteApiDesign(id,request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }
}