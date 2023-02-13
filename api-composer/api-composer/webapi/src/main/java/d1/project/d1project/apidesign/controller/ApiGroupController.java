package d1.project.d1project.apidesign.controller;

import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.d1project.apidesign.model.ApiGroupInsertVm;
import d1.project.d1project.apidesign.model.ApiGroupTree;
import d1.project.d1project.apidesign.model.ApiGroupUpdateVm;
import d1.project.d1project.apidesign.service.ApiGroupService;
import d1.project.d1project.common.annotation.ApiAuth;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author zhengyang
 */
//@Auth("webadmin")
@ApiAuth("webadmin")
@RestController
@RequestMapping("/webadmin/api/group")
public class ApiGroupController {
    private final ApiGroupService apiGroupService;

    public ApiGroupController(ApiGroupService apiGroupService) {
        this.apiGroupService = apiGroupService;
    }

    /**
     * 获取树形结构
     */
    @GetMapping(value = "/getTree")
    public Result<List<ApiGroupTree>> getTree(String name) {
        try {
            return ResultUtil.success("SUCCESS", apiGroupService.findTreeList(name));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    /**
     * 添加信息
     */
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody ApiGroupInsertVm insertVm, HttpServletRequest request) throws DoValidException {
        try {
            apiGroupService.insert(insertVm, request);
            return ResultUtil.success("SUCCESS");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    /**
     * 修改名称
     */
    @PutMapping(value = "/update")
    public Result<String> update(@RequestBody ApiGroupUpdateVm updateVm, HttpServletRequest request) throws DoValidException {
        try {
            apiGroupService.update(updateVm, request);
            return ResultUtil.success("SUCCESS");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    /**
     * 删除名称
     */
    @DeleteMapping(value = "/deleteById")
    public Result<String> deleteById(String id) throws DoValidException {
        try {
            apiGroupService.deleteById(id);
            return ResultUtil.success("SUCCESS");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }
}