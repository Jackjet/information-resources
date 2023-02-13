package d1.project.resourcesharingmgmt.system.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.common.model.OperationLogAspect;
import d1.project.resourcesharingmgmt.common.model.OperationLogParametersAspect;
import d1.project.resourcesharingmgmt.system.model.MenuTreeTree;
import d1.project.resourcesharingmgmt.system.model.OrganizationTree;
import d1.project.resourcesharingmgmt.system.model.vm.MenuTreeInsertVm;
import d1.project.resourcesharingmgmt.system.model.vm.MenuTreeUpdateSeqVm;
import d1.project.resourcesharingmgmt.system.model.vm.MenuTreeUpdateVm;
import d1.project.resourcesharingmgmt.system.service.MenuTreeService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * d1Project
 * 系统管理_菜单管理
 *
 * @author kikki
 * @date 2020-09-10 15:26
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/system/menuTree")
public class MenuTreeController {

    private final MenuTreeService menuTreeService;

    public MenuTreeController(MenuTreeService menuTreeService) {
        this.menuTreeService = menuTreeService;
    }

    @PostMapping(value = "/insert")
    @OperationLogAspect(model = "系统管理_菜单管理", api = "新增", displayContent = {@OperationLogParametersAspect(contentParameters = "name", contentMsg = "新增名称")})
    public Result<String> insert(@Valid @RequestBody MenuTreeInsertVm model, HttpServletRequest request) throws DoValidException {
        menuTreeService.insert(model, request);
        return ResultUtil.success("");
    }

    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id, HttpServletRequest request) throws DoValidException {
        menuTreeService.delete(id, request);
        return ResultUtil.success("");
    }

    @DeleteMapping(value = "/deleteAll")
    public Result<String> deleteAll(@Valid @Size(min = 1, message = "无效操作") @RequestParam("ids") List<String> ids, HttpServletRequest request) throws DoValidException {
        menuTreeService.deleteAll(ids, request);
        return ResultUtil.success("");
    }


    @PutMapping(value = "/update")
    public Result<String> update(@Valid @RequestBody MenuTreeUpdateVm model, HttpServletRequest request) throws DoValidException {
        menuTreeService.update(model, request);
        return ResultUtil.success("");
    }

    /**
     * 序列更新
     */
    @PutMapping(value = "/updateSeq")
    public Result<String> updateSeq(@Valid @RequestBody MenuTreeUpdateSeqVm model, HttpServletRequest request) throws DoValidException {
        menuTreeService.updateSeq(model, request);
        return ResultUtil.success("");
    }

    /**
     * 全部菜单树
     */
    @GetMapping(value = "/findAll")
    public Result<MenuTreeTree> findAll() {
        return ResultUtil.success("", menuTreeService.findAllFormatIntoVm());
    }

    /**
     * 根据id获取菜单树
     */
    @GetMapping(value = "/find")
    public Result<OrganizationTree> find(String id) {
        return ResultUtil.success("", menuTreeService.find(id));
    }


}
