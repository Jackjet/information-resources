package d1.project.d1project.business.controller;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.d1project.business.entity.DataQualityReport;
import d1.project.d1project.business.model.DataQualityReportFindAllVm;
import d1.project.d1project.business.model.DataQualityReportInsertVm;
import d1.project.d1project.business.model.DataQualityReportStatisticsVm;
import d1.project.d1project.business.model.DataQualityReportUpdateVm;
import d1.project.d1project.business.service.DataQualityReportService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.text.ParseException;

/**
 * 管理端_数据质量报告
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/dataQualityReport")
public class DataQualityReportController {

    private final DataQualityReportService dataQualityReportService;

    public DataQualityReportController(DataQualityReportService dataQualityReportService) {
        this.dataQualityReportService = dataQualityReportService;
    }


    /**
     * 分页查询
     */
    @GetMapping(value = "/findAll")
    public Result<Page<DataQualityReport>> findAll(DataQualityReportFindAllVm model) throws Exception {
        return ResultUtil.success("", dataQualityReportService.findAll(model));
    }

    /**
     * 查询详情
     */
    @GetMapping(value = "/find")
    public Result<DataQualityReport> find(@Valid @NotBlank(message = "工单id不能为空") String id) throws DoValidException {
        return ResultUtil.success("", dataQualityReportService.find(id));
    }

    /**
     * 报告详情的统计数据
     */
    @GetMapping(value = "/statistics")
    public Result<String> statistics(@Valid DataQualityReportStatisticsVm model, HttpServletRequest request) throws DoValidException, ParseException {
        return ResultUtil.success("", dataQualityReportService.statistics(model.getStartTime(), model.getEndTime(), request));
    }

    /**
     * 新增
     */
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody DataQualityReportInsertVm model, HttpServletRequest request) throws Exception {
        dataQualityReportService.insert(model, request);
        return ResultUtil.success("");
    }

    /**
     * 删除
     */
    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id) {
        dataQualityReportService.delete(id);
        return ResultUtil.success("");
    }

    /**
     * 更新
     */
    @PutMapping(value = "/update")
    public Result<String> update(@Valid @RequestBody DataQualityReportUpdateVm model, HttpServletRequest request) throws DoValidException {
        dataQualityReportService.update(model, request);
        return ResultUtil.success("");
    }

}
