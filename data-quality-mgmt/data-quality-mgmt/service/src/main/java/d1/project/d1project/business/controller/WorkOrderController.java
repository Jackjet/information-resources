package d1.project.d1project.business.controller;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.d1project.business.entity.WorkOrder;
import d1.project.d1project.business.model.WorkOrderFindAllByMyVm;
import d1.project.d1project.business.model.WorkOrderFindAllVm;
import d1.project.d1project.business.model.WorkOrderHandleVm;
import d1.project.d1project.business.model.WorkOrderInsertVm;
import d1.project.d1project.business.service.WorkOrderService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 管理端_数据质量工单
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/workOrder")
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    public WorkOrderController(WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }


    /**
     * 分页查询
     */
    @GetMapping(value = "/findAll")
    public Result<Page<WorkOrder>> findAll(WorkOrderFindAllVm model, HttpServletRequest request) throws Exception {
        return ResultUtil.success("", workOrderService.findAll(model, request));
    }

    /**
     * 分页查询与我相关的工单
     */
    @GetMapping(value = "/findAllByMy")
    public Result<Page<WorkOrder>> findAllByMy(WorkOrderFindAllByMyVm model, HttpServletRequest request) throws Exception {
        return ResultUtil.success("", workOrderService.findAllByMy(model, request));
    }

    /**
     * 查询详情
     */
    @GetMapping(value = "/find")
    public Result<WorkOrder> find(@Valid @NotBlank(message = "工单id不能为空") String id) throws DoValidException {
        return ResultUtil.success("", workOrderService.findById(id));
    }

    /**
     * 根据当前用户查询对应的工单数量
     */
    @GetMapping(value = "/count")
    public Result<String> count(HttpServletRequest request) throws DoValidException {
        return ResultUtil.success("", workOrderService.getCount(request));
    }

    /**
     * 新增
     */
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody WorkOrderInsertVm model, HttpServletRequest request) throws Exception {
        workOrderService.insert(model, request);
        return ResultUtil.success("");
    }

    /**
     * 工单处理
     */
    @PutMapping(value = "/handle")
    public Result<String> handle(@Valid @RequestBody WorkOrderHandleVm model, HttpServletRequest request) throws Exception {
        workOrderService.handle(model, request);
        return ResultUtil.success("");
    }
}
