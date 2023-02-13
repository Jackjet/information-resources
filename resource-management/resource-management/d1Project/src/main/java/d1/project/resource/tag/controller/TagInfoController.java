package d1.project.resource.tag.controller;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.resource.resourcemanage.entity.ContainerInfo;
import d1.project.resource.tag.business.TagInfoBusiness;
import d1.project.resource.tag.entity.TagInfo;
import d1.project.resource.tag.model.TagVm;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 容器管理
 *
 * @author baozh
 */
//@ApiAuth("webadmin")
@RestController
@RequestMapping("/webadmin/tagInfo")
public class TagInfoController {
    private final TagInfoBusiness tagInfoBusiness;

    public TagInfoController(TagInfoBusiness tagInfoBusiness) {
        this.tagInfoBusiness = tagInfoBusiness;
    }

    @GetMapping(value = "/findAll")
    public Result<Page<TagInfo>> findAll(FindTagVm findVm) {
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(findVm));
        try {
            return ResultUtil.success("成功", tagInfoBusiness.findTags(json));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/findList")
    public Result<List<TagInfo>> findList(String name) {
        JSONObject json = new JSONObject(1);
        json.put("name", name);
        try {
            return ResultUtil.success("成功", tagInfoBusiness.findTagsList(json));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/findById")
    public Result<ContainerInfo> findById(String id) {
        try {
            return ResultUtil.success("成功", tagInfoBusiness.findTagById(id));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @PostMapping(value = "/insert")
    public Result<String> insert(@RequestBody TagVm insertVm, HttpServletRequest request) {
        try {
            tagInfoBusiness.insertTag(insertVm, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @PutMapping(value = "/update")
    public Result<String> update(@RequestBody TagVm updateVm, HttpServletRequest request) {
        try {
            tagInfoBusiness.updateTag(updateVm, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @DeleteMapping(value = "/deleteById")
    public Result<String> deleteById(String id, HttpServletRequest request) {
        try {
            tagInfoBusiness.deleteById(id, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }
}

class FindTagVm extends PageableVm {
    private String name;
    private String groupId;

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
}

