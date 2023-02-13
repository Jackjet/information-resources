package d1.project.api.integration.log.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.api.integration.log.entity.OperationLogEntity;
import d1.project.api.integration.log.model.vm.UserLogFindAllVm;
import d1.project.api.integration.log.service.UserLogService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * d1Project
 * 日志_用户日志
 *
 * @author kikki
 * @date 2020-09-09 21:46
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/log/userLog")
public class UserLogController {

    private final UserLogService userLogService;

    public UserLogController(UserLogService userLogService) {
        this.userLogService = userLogService;
    }

    @GetMapping(value = "/findAll")
    public Result<Page<OperationLogEntity>> findAll(UserLogFindAllVm model, HttpServletRequest request) throws Exception {
        return ResultUtil.success("", userLogService.findAll(model, request));
    }

    @GetMapping(value = "/find")
    public Result<OperationLogEntity> findAll(String id) throws DoValidException {
        return ResultUtil.success("", userLogService.find(id).orElseThrow(() -> new DoValidException("用户日志不存在")));
    }

    /**
     * 导出用户日志
     */
    @PostMapping("/export")
    public Result<String> export(UserLogFindAllVm model, HttpServletResponse response, HttpServletRequest request) throws Exception {
        userLogService.export(model, response, request);
        return null;
    }
}
