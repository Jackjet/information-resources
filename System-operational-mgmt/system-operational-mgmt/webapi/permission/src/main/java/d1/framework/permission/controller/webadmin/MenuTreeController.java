package d1.framework.permission.controller.webadmin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.permission.annotation.Permission;
import d1.framework.permission.entity.MenuTree;
import d1.framework.permission.model.MenuTreeGN;
import d1.framework.permission.model.MenuTreeGetVm;
import d1.framework.permission.service.MenuTreeService;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.entity.BaseEntity;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Auth("webadmin")
@Permission("nopermission")
@RestController
@RequestMapping(value = "/webadmin/permission/MenuTree")
@Api(value = "/webadmin/permission/MenuTree", tags = "功能树管理")
public class MenuTreeController {

    @Autowired
    private MenuTreeService menuTreeService;

    @ApiOperation(value = "创建功能树")
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody MenuTreePostVm model) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(model);
            menuTreeService.insert(jsonObject);
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "删除数据", notes = "根据id来指定删除数据")
    @DeleteMapping(value = "/deleteById")
    public Result<MenuTree> delete(@Valid @NotBlank(message = "id不可为空") String id) {
        try {
            menuTreeService.delete(id);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(),e);
        }
        return ResultUtil.success("删除成功");
    }

    @ApiOperation(value = "更新数据")
    @PutMapping(value = "/update")
    public Result<String> update(@RequestBody MenuTreePutVm model) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(model);
            menuTreeService.update(jsonObject);
            return ResultUtil.success("编辑成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "获取全部功能树")
    @GetMapping(value = "/findAll")
    public Result<List<MenuTreeGetVm>> findAllTree() {
        try {
            List<MenuTreeGetVm> mo = menuTreeService.findAllTree();
            return ResultUtil.success("获取全部功能树成功", mo);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "根据menuId获取功能树")
    @GetMapping(value = "/findByMenuId")
    public Result<MenuTreeGetVm> findByMenuId(String menuId) {
        try {
            MenuTreeGetVm mo = menuTreeService.findByMenuId(menuId);
            return ResultUtil.success("获取全部功能树成功", mo);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "获取数据详细信息", notes = "根据id来获取详细信息")
    @GetMapping(value = "/findById")
    public Result<MenuTreeGN> findById(@Valid @NotBlank(message = "id不可为空") String id) {

        try {
            MenuTreeGN mUser = menuTreeService.findByIdGn(id);
            return ResultUtil.success("获取详细信息", mUser);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

}

class MenuTreePostVm extends BaseEntity {

    @ApiModelProperty(value = "父级Id")
    private String parentId;

    @ApiModelProperty(value = "名称")
    @NotBlank(message = "名称不可为空")
    private String name;

    @ApiModelProperty(value = "图标选中")
    private String icon;

    @ApiModelProperty(value = "类型  menu菜单、action操作")
    @NotBlank(message = "类型不可为空")
    private String type;

    @ApiModelProperty(value = "路由", notes = "当类型是menu时，route值是vue的页面地址。当类型是action时，route值是后台controller中方法的路由值")
    @NotBlank(message = "路由不可为空")
    private String route;

    @ApiModelProperty(value = "图标未选中")
    private String icon2;

    @ApiModelProperty(value = "排序")
    @NotNull(message = "排序不可为空")
    private Integer orderNum;

    @ApiModelProperty(value = "页面是否外部打开")
    private String outOpen;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "所属平台")
    private String platform;

    @ApiModelProperty(value = "是否有子集")
    @NotBlank(message = "是否有子集不可为空")
    private String haveChild;

    public String getIcon2() {
        return icon2;
    }

    public void setIcon2(String icon2) {
        this.icon2 = icon2;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getOutOpen() {
        return outOpen;
    }

    public void setOutOpen(String outOpen) {
        this.outOpen = outOpen;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getHaveChild() {
        return haveChild;
    }

    public void setHaveChild(String haveChild) {
        this.haveChild = haveChild;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}


class MenuTreePutVm extends BaseEntity {

    @ApiModelProperty(value = "父级Id")
    private String parentId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "图标选中")
    private String icon;

    @ApiModelProperty(value = "类型  menu菜单、action操作")
    private String type;

    @ApiModelProperty(value = "路由", notes = "当类型是menu时，route值是vue的页面地址。当类型是action时，route值是后台controller中方法的路由值")
    private String route;

    @ApiModelProperty(value = "图标未选中")
    private String icon2;

    @ApiModelProperty(value = "排序")
    private Integer orderNum;

    @ApiModelProperty(value = "页面是否外部打开")
    private String outOpen;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "所属平台")
    private String platform;

    @ApiModelProperty(value = "是否有子集")
    private String haveChild;

    public String getIcon2() {
        return icon2;
    }

    public void setIcon2(String icon2) {
        this.icon2 = icon2;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getOutOpen() {
        return outOpen;
    }

    public void setOutOpen(String outOpen) {
        this.outOpen = outOpen;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getHaveChild() {
        return haveChild;
    }

    public void setHaveChild(String haveChild) {
        this.haveChild = haveChild;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}
