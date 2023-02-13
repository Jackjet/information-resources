package d1.project.api.integration.apimanage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.api.integration.apimanage.business.ApiGroupBusiness;
import d1.project.api.integration.apimanage.entity.ApiGroupInfo;
import d1.project.api.integration.apimanage.model.ApiGroupVm;
import d1.project.api.integration.apimanage.view.entity.ApiGroupList;
import d1.project.api.integration.apimanage.view.entity.ApiWithSourceApi;
import d1.project.api.integration.apimanage.view.entity.GroupApiList;
import d1.project.api.integration.common.annotation.ApiAuth;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @author baozh
 */
@ApiAuth("webadmin")
@RestController
@RequestMapping("/webadmin/apiGroupManage")
public class ApiGroupManageController {

    private final ApiGroupBusiness apiGroupBusiness;

    public ApiGroupManageController(ApiGroupBusiness apiGroupBusiness) {
        this.apiGroupBusiness = apiGroupBusiness;
    }

    @GetMapping(value = "/tree")
    public Result<JSONArray> tree() {
        try {
            return ResultUtil.success("成功", apiGroupBusiness.tree());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody ApiGroupInfo insertVm, HttpServletRequest request) {
        try {
            apiGroupBusiness.insertApiGroupInfo(insertVm, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @PutMapping(value = "/update")
    public Result<String> update(@RequestBody ApiGroupUpdatePutVm updateVm, HttpServletRequest request) {
        try {
            apiGroupBusiness.updateApiGroupInfo(updateVm.getId(),updateVm.getName(), request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id, HttpServletRequest request) {
        try {
            apiGroupBusiness.deleteGroup(id, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }
}

class GroupFindVm extends PageableVm {
    private String name;
    private String userName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

class GroupApiFindVm extends PageableVm {
    private String groupId;
    private String apiName;
    private String sourceApiName;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

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

class ApiGroupUpdatePutVm{
    /**
     * id
     */
    private String id;
    /**
     * 组名
     */
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}