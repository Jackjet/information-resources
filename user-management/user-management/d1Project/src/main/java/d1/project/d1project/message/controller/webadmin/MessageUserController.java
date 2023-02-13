package d1.project.d1project.message.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.d1project.message.entity.UserMessageEntity;
import d1.project.d1project.message.model.UserMessageInsertVm;
import d1.project.d1project.message.service.UserMessageServiceImpl;
import d1.project.d1project.system.model.vm.UserMessageFindAllVm;
import d1.project.d1project.system.model.vm.UserMessageUpdateStatusAllVm;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-14 22:04
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/message/messageUser")
public class MessageUserController {

    final
    UserMessageServiceImpl userMessageService;

    public MessageUserController(UserMessageServiceImpl userMessageService) {
        this.userMessageService = userMessageService;
    }

    /**
     * 删除列表
     */
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody UserMessageInsertVm model, HttpServletRequest request) throws DoValidException {
        userMessageService.send(model.getMessage(), model.getUserIds());
        return ResultUtil.success("");
    }

    /**
     * 删除列表
     */
    @DeleteMapping(value = "/deleteAll")
    public Result<String> deleteAll(@Valid @Size(min = 1, message = "无效操作") @RequestParam("ids") List<String> ids, HttpServletRequest request) throws DoValidException {
        userMessageService.deleteAll(ids, request);
        return ResultUtil.success("");
    }

    /**
     * 删除
     */
    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id, HttpServletRequest request) throws DoValidException {
        userMessageService.delete(id, request);
        return ResultUtil.success("");
    }

    /**
     * 根据列表修改状态
     */
    @PutMapping(value = "/updateAllStatus")
    public Result<String> updateAllStatus(@Valid @RequestBody UserMessageUpdateStatusAllVm model, HttpServletRequest request) throws DoValidException {
        userMessageService.updateAllStatus(model, request);
        return ResultUtil.success("");
    }

    /**
     * 当前用户所有修改为已读
     */
    @PutMapping(value = "/updateStatusAll")
    public Result<String> updateStatusAll(HttpServletRequest request) throws DoValidException {
        userMessageService.updateStatusAll(request);
        return ResultUtil.success("");
    }

    /**
     * 用户信息
     */
    @GetMapping(value = "/findAll")
    public Result<UserMessageEntity> findAll(UserMessageFindAllVm model, HttpServletRequest request) throws Exception {
        return ResultUtil.success("", userMessageService.findAll(model, request));
    }

    /**
     * 详情
     */
    @GetMapping(value = "/find")
    public Result<UserMessageEntity> find(String id, HttpServletRequest request) throws Exception {
        return ResultUtil.success("", userMessageService.find(id, request).orElseThrow(() -> new DoValidException("信息不存在")));
    }


}
