package d1.project.dcrun.center.webapi.resource.controller;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.dcrun.center.webapi.resource.business.ResourceBusiness;
import d1.project.dcrun.center.webapi.resource.entity.DataSourceInfo;
import d1.project.dcrun.center.webapi.resource.model.DataSourceInsertVm;
import d1.project.dcrun.center.webapi.resource.model.DataSourceUpdateVm;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 数据源管理
 *
 * @author baozh
 */
@Auth("integration")
@RestController
@RequestMapping("/webadmin/dataSourceInfo")
public class DataSourceInfoController {

    private final ResourceBusiness resourceBusiness;

    public DataSourceInfoController(ResourceBusiness resourceBusiness) {
        this.resourceBusiness = resourceBusiness;
    }

    @GetMapping(value = "/findAll")
    public Result<Page<DataSourceInfo>> findAll(DataSourceFindVm findVm) {
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(findVm));
        try {
            return ResultUtil.success("成功", resourceBusiness.findAllDataSource(json));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/findById")
    public Result<DataSourceInfo> findById(String id) {
        try {
            return ResultUtil.success("成功", resourceBusiness.findDataSource(id));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @PostMapping(value = "/insert")
    public Result<String> insert(@RequestBody DataSourceInsertVm insertVm, HttpServletRequest request) {
        try {
            resourceBusiness.insertDataSource(insertVm, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @PutMapping(value = "/update")
    public Result<String> update(@RequestBody DataSourceUpdateVm updateVm, HttpServletRequest request) {
        try {
            resourceBusiness.updateDataSource(updateVm, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @DeleteMapping(value = "/deleteById")
    public Result<String> deleteById(String id) {
        try {
            resourceBusiness.deleteDataSource(id);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/testConnection")
    public Result<String> testConnection(String id, HttpServletRequest request) {
        try {
            resourceBusiness.testConnection(id, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }


    @PostMapping(value = "/testConnectionButton")
    public Result<String> testConnectionButton(@RequestBody DataSourceInfo data, HttpServletRequest request) {
        try {
            resourceBusiness.testConnectionButton(data, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

}

class DataSourceFindVm extends PageableVm {
    private String name;
    private String tagName;
    private String tagValue;
    private String groupId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}