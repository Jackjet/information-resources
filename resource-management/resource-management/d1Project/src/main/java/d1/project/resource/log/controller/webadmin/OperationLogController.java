package d1.project.resource.log.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resource.log.entity.OperationLogEntity;
import d1.project.resource.log.model.vm.OperationLogFindAllVm;
import d1.project.resource.log.service.OperationLogServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/log/operationLog")
public class OperationLogController {

    private final OperationLogServiceImpl operationLogService;

    public OperationLogController(OperationLogServiceImpl operationLogService) {
        this.operationLogService = operationLogService;
    }

    @GetMapping(value = "/findAll")
    public Result<Page<OperationLogEntity>> findAll(OperationLogFindAllVm model, HttpServletRequest request) throws Exception {
        return ResultUtil.success("", operationLogService.findAll(model, request));
    }

    @GetMapping(value = "/find")
    public Result<OperationLogEntity> find(String id) throws DoValidException {
        return ResultUtil.success("", operationLogService.find(id).orElseThrow(() -> new DoValidException("操作日志不存在")));
    }

    /**
     * 导出操作日志
     */
    @PostMapping("/export")
    public Result<String> export(OperationLogFindAllVm model, HttpServletResponse response, HttpServletRequest request) throws Exception {
        operationLogService.export(model, response, request);
        return null;
    }

}
