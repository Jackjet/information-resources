package d1.project.resource.group.controller;

import com.alibaba.fastjson.JSONArray;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resource.common.annotation.ApiAuth;
import d1.project.resource.group.business.GroupInfoBusiness;
import d1.project.resource.group.model.GroupTree;
import d1.project.resource.group.model.GroupVm;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author baozh
 */
@ApiAuth("webadmin")
@RestController
@RequestMapping("/webadmin/groupInfo")
public class GroupInfoController {
    private final GroupInfoBusiness groupInfoBusiness;

    public GroupInfoController(GroupInfoBusiness groupInfoBusiness) {
        this.groupInfoBusiness = groupInfoBusiness;
    }


    /**
     * get tree list
     */
    @GetMapping(value = "/getTree")
    public Result<List<GroupTree>> getTree(String type) {
        try {
            return ResultUtil.success("SUCCESS", groupInfoBusiness.findTreeList(type));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    /**
     * insert group info
     */
    @PostMapping(value = "/insert")
    public Result<String> insert(@RequestBody GroupVm insertVm, HttpServletRequest request) {
        try {
            groupInfoBusiness.insertGroup(insertVm, request);
            return ResultUtil.success("SUCCESS");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }


    /**
     * update group info
     */
    @PutMapping(value = "/update")
    public Result<String> update(@RequestBody GroupVm updateVm, HttpServletRequest request) {
        try {
            groupInfoBusiness.updateGroup(updateVm, request);
            return ResultUtil.success("SUCCESS");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }


    /**
     * 删除名称
     */
    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id, HttpServletRequest request) {
        try {
            groupInfoBusiness.deleteGroupById(id, request);
            return ResultUtil.success("SUCCESS");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    /**
     * get tree list
     */
    @GetMapping(value = "/getGroupList")
    public Result<JSONArray> getGroupList(String type) {
        try {
            return ResultUtil.success("SUCCESS", groupInfoBusiness.findGroupList(type));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }
}
