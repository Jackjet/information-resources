package d1.project.resourcesharingmgmt.resource.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.entity.DownloadInfoEntity;
import d1.project.resourcesharingmgmt.resource.model.way.*;
import d1.project.resourcesharingmgmt.resource.service.WayService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author dy
 * 服务指引
 */
@Auth("webadmin")
@RestController("/webadmin/guide")
@RequestMapping("/webadmin/guide")
public class GuideController {

    private final WayService wayService;

    public GuideController(WayService wayService) {
        this.wayService = wayService;
    }

    /**
     * 服务指引查询列表
     *
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/findAll")
    public Result<DownloadInfoEntity> findAll(WayGuideFindAllVm model) throws Exception {
        return ResultUtil.success("", wayService.guideFindAll(model));
    }

    /**
     * 服务指引新增
     *
     * @param model
     * @return
     * @throws DoValidException
     */
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody WayGuideInsertVm model) throws DoValidException {
        wayService.guideInsert(model);
        return ResultUtil.success("添加成功");
    }

    /**
     * 服务指引修改
     *
     * @param model
     * @return
     * @throws DoValidException
     */
    @PutMapping(value = "/update")
    public Result<String> update(@Valid @RequestBody WayGuideUpdateVm model) throws DoValidException {
        wayService.guideUpdate(model);
        return ResultUtil.success("修改成功");
    }

    /**
     * 服务指引删除
     *
     * @param id 记录Id
     * @return
     * @throws DoValidException
     */
    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id) throws DoValidException {
        wayService.guideDelete(id);
        return ResultUtil.success("删除成功");
    }

    /**
     * 服务指引上移
     *
     * @param id 记录Id
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/up")
    public Result<String> up(String id) throws DoValidException {
        wayService.guideUp(id);
        return ResultUtil.success("上移成功");
    }

    /**
     * 服务指引下移
     *
     * @param id 记录Id
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/down")
    public Result<String> down(String id) throws DoValidException {
        wayService.guideDown(id);
        return ResultUtil.success("下移成功");
    }

    /**
     * 获取服务指引列表--通过角色Id标记已选列表
     *
     * @param model
     * @return
     */
    @GetMapping(value = "/guideRoleGet")
    public Result<WayRoleResVm> guideRoleGet(WayRoleFindVm model) {
        WayRoleResVm res = wayService.guideRoleGet(model);
        return ResultUtil.success("查询成功", res);
    }

    /**
     * 保存服务指引列表--关联角色保存
     *
     * @param model
     * @return
     */
    @PostMapping(value = "/guideRoleInsert")
    public Result<WayRoleResVm> guideRoleInsert(@Valid @RequestBody WayRoleInsertVm model) {
        wayService.guideRoleInsert(model);
        return ResultUtil.success("保存成功");
    }
}
