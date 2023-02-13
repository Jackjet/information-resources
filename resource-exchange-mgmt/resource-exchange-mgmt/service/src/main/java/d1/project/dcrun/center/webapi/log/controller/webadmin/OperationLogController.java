package d1.project.dcrun.center.webapi.log.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.log.entity.OperationLogEntity;
import d1.project.dcrun.center.webapi.log.model.vm.OperationLogFindAllVm;
import d1.project.dcrun.center.webapi.log.service.OperationLogServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * d1Project
 * 日志_操作日志
 *
 * @author kikki
 * @date 2020-09-09 21:46
 */
@Auth("integration")
@RestController
@RequestMapping("/webadmin/log/operationLog")
public class OperationLogController {

    private final OperationLogServiceImpl operationLogService;

    public OperationLogController(OperationLogServiceImpl operationLogService) {
        this.operationLogService = operationLogService;
    }

    @GetMapping(value = "/findAll")
    public Result<Page<OperationLogEntity>> findAll(OperationLogFindAllVm model, HttpServletRequest request) {
        try {
            return ResultUtil.success("", operationLogService.findAll(model, request));
        }catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    @GetMapping(value = "/find")
    public Result<OperationLogEntity> find(String id) {
        try {
            return ResultUtil.success("", operationLogService.find(id).orElseThrow(() -> new Exception("操作日志不存在")));
        }catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    /**
     * 导出操作日志
     */
    @GetMapping("/export")
    public Result<String> export(OperationLogFindAllVm model, HttpServletResponse response, HttpServletRequest request) {
        try {
            operationLogService.export(model, response, request);
            return null;
        }catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

}
