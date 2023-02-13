package d1.project.resource.resourcemanage.controller;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.resource.resourcemanage.business.ResourceBusiness;
import d1.project.resource.resourcemanage.entity.ContainerInfo;
import d1.project.resource.resourcemanage.model.ContainerInsertVm;
import d1.project.resource.resourcemanage.model.ContainerUpdateVm;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 容器管理
 *
 * @author baozh
 */

//@ApiAuth("webadmin")
@RestController
@RequestMapping("/webadmin/containerInfo")
public class ContainerInfoController {
    private final ResourceBusiness resourceBusiness;

    public ContainerInfoController(ResourceBusiness resourceBusiness) {
        this.resourceBusiness = resourceBusiness;
    }

    @GetMapping(value = "/findAll")
    public Result<Page<ContainerInfo>> findAll(ContainerFindVm findVm) {
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(findVm));
        try {
            return ResultUtil.success("成功", resourceBusiness.findAllContainerInfo(json));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/findById")
    public Result<ContainerInfo> findById(String id) {
        try {
            return ResultUtil.success("成功", resourceBusiness.findContainerInfo(id));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @PostMapping(value = "/insert")
    public Result<String> insert(@RequestBody ContainerInsertVm insertVm, HttpServletRequest request) {
        try {
            resourceBusiness.insertContainerInfo(insertVm, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @PutMapping(value = "/update")
    public Result<String> update(@RequestBody ContainerUpdateVm updateVm, HttpServletRequest request) {
        try {
            resourceBusiness.updateContainerInfo(updateVm, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @DeleteMapping(value = "/deleteById")
    public Result<String> deleteById(String id, HttpServletRequest request) {
        try {
            resourceBusiness.deleteContainerInfo(id, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/testConnection")
    public Result<String> testConnection(String id, HttpServletRequest request) {
        try {
            resourceBusiness.testContainer(id, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/testController")
    @Auth("noauth")
    public Result<String> testController() {
        return ResultUtil.success("成功");
    }

    /**
     * 更新容器初始化状态
     * @param param
     * @return
     * @throws DoValidException
     */
    @PutMapping(value = "/updateInit")
    public Result<String> updateInit(@RequestBody @Valid UpdateInitVm param) throws DoValidException {
        resourceBusiness.updateInit(param.getId());
        return ResultUtil.success("成功");
    }
}

class ContainerFindVm extends PageableVm {
    private String name;
    private String tagName;
    private String tagValue;
    private String groupId;
    private String type;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

class UpdateInitVm{
    /**
     * 容器id
     */
    @NotBlank(message = "容器id不能为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}