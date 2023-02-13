package d1.project.api.integration.apimanage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.api.integration.apimanage.business.ApiManageBusiness;
import d1.project.api.integration.apimanage.entity.ApiBaseInfo;
import d1.project.api.integration.apimanage.model.ApiInsertVm;
import d1.project.api.integration.apimanage.model.ApiUpdateVm;
import d1.project.api.integration.apimanage.view.entity.ApiWithSourceApi;
import d1.project.api.integration.apimanage.view.entity.SourceApiList;
import d1.project.api.integration.common.annotation.ApiAuth;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @author baozh
 */
@ApiAuth("webadmin")
@RestController
@RequestMapping("/webadmin/apiManage")
public class ApiManageController {

    private final ApiManageBusiness apiManageBusiness;

    public ApiManageController(ApiManageBusiness apiManageBusiness) {
        this.apiManageBusiness = apiManageBusiness;
    }

    @GetMapping(value = "/list")
    public Result<JSONObject> findAll(FindVm findVm) {
        try {
            return ResultUtil.success("成功", apiManageBusiness.findApiList((JSONObject) JSON.toJSON(findVm)));
        } catch (Exception e) {
            e.printStackTrace();
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

    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody ApiInsertVm insertVm, HttpServletRequest request) {
        try {
            apiManageBusiness.insertApiBaseInfo(insertVm, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @PutMapping(value = "/update")
    public Result<String> update(@RequestBody ApiUpdateVm updateVm, HttpServletRequest request) {
        try {
            apiManageBusiness.updateApiBaseInfo(updateVm, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/copy")
    public Result<String> copy(String id, HttpServletRequest request) {
        try {
            apiManageBusiness.copyApiInfo(id, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id, HttpServletRequest request) {
        try {
            apiManageBusiness.deleteApi(id, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/sourceApiList")
    public Result<Page<SourceApiList>> sourceApiList(SourceApiFindVm findVm) {
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(findVm));
        try {
            return ResultUtil.success("成功", apiManageBusiness.sourceApiList(json));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @PutMapping(value = "/publish")
    public Result<String> publish(@RequestBody ApiPublishPutVm param) throws Exception {
        try {
            apiManageBusiness.publish(param.getId(),param.getContainer());
            return ResultUtil.success("成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @PutMapping(value = "/revocation")
    public Result<String> revocation(@RequestBody ApiRevocationPutVm param) {
        try {
            apiManageBusiness.revocation(param.getId());
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/exportApiDoc")
    public Result<String> exportApiDoc(@RequestParam("ids") List<String> ids,@RequestParam("type") String type, @RequestParam("name") String name,HttpServletResponse response) {
        try {
            if("0".equals(type)) {
                apiManageBusiness.doc(response, ids);
            } else if("1".equals(type)){
                apiManageBusiness.exportTxt(response, ids,name);
            }
            return null;
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }
}

class FindVm extends PageableVm {
    private String name;
    private String sourceName;
    private String groupId;

    private String container;

    private String key;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

class SourceApiFindVm extends PageableVm {

    private String sourceApiName;

    public String getSourceApiName() {
        return sourceApiName;
    }

    public void setSourceApiName(String sourceApiName) {
        this.sourceApiName = sourceApiName;
    }
}

class ApiPublishPutVm{
    private String id;
    private String container;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }
}

class ApiRevocationPutVm{
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
