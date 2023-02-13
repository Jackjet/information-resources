package d1.project.d1project.message.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.d1project.message.entity.AnnouncementEntity;
import d1.project.d1project.message.model.AnnouncementFindAllVm;
import d1.project.d1project.message.model.AnnouncementInsertVm;
import d1.project.d1project.message.model.AnnouncementUpdateVm;
import d1.project.d1project.message.service.AnnouncementService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 管理端_消息_通知公告
 *
 * @author kikki
 * @date 2020-09-14 22:04
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/message/announcement")
public class AnnouncementController {

    final
    AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    /**
     * 新增
     */
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody AnnouncementInsertVm model, HttpServletRequest request) throws DoValidException {
        announcementService.insert(model, request);
        return ResultUtil.success("");
    }

    /**
     * 删除
     */
    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id, HttpServletRequest request) throws DoValidException {
        announcementService.delete(id, request);
        return ResultUtil.success("");
    }

    /**
     * 更新
     */
    @PutMapping(value = "/update")
    public Result<String> update(@Valid @RequestBody AnnouncementUpdateVm model, HttpServletRequest request) throws DoValidException {
        announcementService.update(model, request);
        return ResultUtil.success("");
    }

    /**
     * 分页查询
     */
    @GetMapping(value = "/findAll")
    public Result<AnnouncementEntity> findAll(AnnouncementFindAllVm model) throws Exception {
        return ResultUtil.success("", announcementService.findAll(model));
    }

    /**
     * 查询详情
     */
    @GetMapping(value = "/find")
    public Result<AnnouncementEntity> find(String id) throws Exception {
        return ResultUtil.success("", announcementService.find(id).orElseThrow(() -> new DoValidException("信息不存在")));
    }


}
