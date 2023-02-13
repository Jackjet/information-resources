package d1.project.api.integration.apimanage.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.api.integration.apimanage.business.ApiAuthManageBusiness;
import d1.project.api.integration.apimanage.entity.ApiAuthManage;
import d1.project.api.integration.apimanage.entity.KongApi;
import d1.project.api.integration.apimanage.model.ApiAuthInsertVm;
import d1.project.api.integration.apimanage.model.GroupAuthInsertVm;
import d1.project.api.integration.apimanage.view.entity.*;
import d1.project.api.integration.common.annotation.ApiAuth;
import d1.project.api.integration.kong.business.KongBusiness;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Vector;

/**
 * @author baozh
 */
@ApiAuth("webadmin")
@RestController
@RequestMapping("/webadmin/authManage")
public class ApiAuthManageController {

    private final ApiAuthManageBusiness apiAuthManageBusiness;
    private final KongBusiness kongBusiness;

    public ApiAuthManageController(ApiAuthManageBusiness apiAuthManageBusiness, KongBusiness kongBusiness) {
        this.apiAuthManageBusiness = apiAuthManageBusiness;
        this.kongBusiness = kongBusiness;
    }

    @GetMapping(value = "/apiList")
    public Result<Page<ApiWithSourceApi>> apiList(AuthApiFindVm findVm) {
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(findVm));
        try {
            return ResultUtil.success("成功", apiAuthManageBusiness.findApiList(json));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/groupList")
    public Result<JSONArray> groupList(AuthGroupApiFindVm findVm) {
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(findVm));
        try {
            return ResultUtil.success("成功", apiAuthManageBusiness.findGroupList(json));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/groupApiList")
    public Result<Page<GroupApiList>> groupApiList(AuthGroupApiFindVm findVm) {
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(findVm));
        try {
            return ResultUtil.success("成功", apiAuthManageBusiness.findGroupApiList(json));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/appList")
    public Result<Page<AuthAppList>> appList(AuthAppFindVm findVm) {
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(findVm));
        try {
            return ResultUtil.success("成功", apiAuthManageBusiness.findAppList(json));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody ApiAuthInsertVm insertVm, HttpServletRequest request) {
        try {
            apiAuthManageBusiness.insertAuth(insertVm, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/authList")
    public Result<Page<ApiAuthApp>> authList(AuthAppListFindVm findVm) {
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(findVm));
        try {
            return ResultUtil.success("成功", apiAuthManageBusiness.findApiAuthAppList(json));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    //TODO 回滚
    @DeleteMapping(value = "/authDelete")
    public Result<String> authDelete(@RequestParam List<String> ids, HttpServletRequest request) {
        try {
            List<KongApiView> kongApiViews = apiAuthManageBusiness.authDelete(ids, request);
            List<KongApi> kongApis = new Vector<>();
            for(KongApiView kongApiView : kongApiViews) {
                KongApi kongApi = new KongApi();
                BeanUtils.copyProperties(kongApiView,kongApi);

                kongApi.setAclGroup("");
                kongBusiness.setPermission(kongApiView.getContainer(),kongApi);

                kongApis.add(kongApi);
            }

            apiAuthManageBusiness.saveKongApis(kongApis);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/getAuthNum")
    public Result<Boolean> getAuthNum(String appIds) {
        try {
            return ResultUtil.success("成功", apiAuthManageBusiness.getAuthNum(appIds));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @PostMapping(value = "/addGroup")
    public Result<String> addGroup (@RequestBody GroupAuthInsertVm param,HttpServletRequest request) {
        try {
            apiAuthManageBusiness.insertGroupAuth(param,request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }
}

class AuthApiFindVm extends PageableVm {
    private String name;
    private String sourceApiName;

    private String container;

    private String key;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceApiName() {
        return sourceApiName;
    }

    public void setSourceApiName(String sourceApiName) {
        this.sourceApiName = sourceApiName;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

class AuthGroupFindVm extends PageableVm {
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

class AuthGroupApiFindVm extends PageableVm{
    /**
     * 组名
     */
    private String name;
    private String groupId;
    private String methods;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMethods() {
        return methods;
    }

    public void setMethods(String methods) {
        this.methods = methods;
    }
}

class AuthAppFindVm extends PageableVm {
    private String id;
    private String appName;
    private String method;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}

class AuthAppListFindVm extends PageableVm {
    private String apiId;
    private String appId;
    private String appName;

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
