package d1.project.resourcesharingmgmt.resource.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.model.way.WayRoleInsertVm;
import d1.project.resourcesharingmgmt.resource.model.way.WayRoleFindVm;
import d1.project.resourcesharingmgmt.resource.model.way.WayRoleResVm;
import d1.project.resourcesharingmgmt.resource.service.WayService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author dy
 * 代办--代办数据为系统初始化，无需curd功能。只有角色关联查询、保存功能。
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/charge")
public class ChargeController {
    private final WayService wayService;

    public ChargeController(WayService wayService) {
        this.wayService = wayService;
    }

    /**
     * 获取代办列表--通过角色Id标记已选列表
     */
    @GetMapping("/chargeRoleGet")
    public Result<WayRoleResVm> chargeRoleGet(WayRoleFindVm model) {
        return ResultUtil.success("成功", wayService.chargeRoleGet(model));
    }

    /**
     * 保存代办列表--关联角色保存
     */
    @PostMapping("/chargeRoleInsert")
    public Result<String> chargeRoleInsert(@Valid @RequestBody WayRoleInsertVm model) {
        wayService.chargeRoleInsert(model);
        return ResultUtil.success("成功");
    }
}
