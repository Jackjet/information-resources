package d1.project.d1project.apidesign.controller;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.d1project.apidesign.service.ResourcesService;
import d1.project.d1project.common.annotation.ApiAuth;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * API设计接口
 *
 * @author baozh
 */

//@Auth("webadmin")
@ApiAuth("webadmin")
@RestController
@RequestMapping("/webadmin/resources")
public class ResourcesController {
    private final ResourcesService resourcesService;

    public ResourcesController(ResourcesService resourcesService) {
        this.resourcesService = resourcesService;
    }

    @GetMapping(value = "/findAllMetas")
    public Result<JSONObject> findAllMetas(String name) {
        try {
            return ResultUtil.success("成功", resourcesService.findAllMetas(name));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/findAllDataSource")
    public Result<JSONObject> findAllDataSource() {
        try {
            return ResultUtil.success("成功", resourcesService.findAllDataSource());
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/findAllContainer")
    public Result<JSONObject> findAllContainer() {
        try {
            return ResultUtil.success("成功", resourcesService.findAllContainer());
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @PostMapping(value = "/apiDesignInsert")
    public Result<JSONObject> apiDesignInsert(String apiId, HttpServletRequest request) {
        try {
            return ResultUtil.success("成功", resourcesService.apiDesignInsert(apiId, request));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }
}