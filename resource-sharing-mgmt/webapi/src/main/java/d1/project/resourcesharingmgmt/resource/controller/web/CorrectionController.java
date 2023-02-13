package d1.project.resourcesharingmgmt.resource.controller.web;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.model.correction.CorrectionInsertVm;
import d1.project.resourcesharingmgmt.resource.service.CorrectionService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 门户端-纠错功能
 * @author zhengyang
 */
@Auth("webadmin")
@RestController("/web/correction")
@RequestMapping("/web/correction")
public class CorrectionController {

    private final CorrectionService correctionService;

    public CorrectionController(CorrectionService correctionService) {
        this.correctionService = correctionService;
    }

    /**
     * 新增纠错
     * @param model
     * @param request
     * @return
     * @throws DoValidException
     */
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody CorrectionInsertVm model, HttpServletRequest request) throws DoValidException {
        correctionService.insert(model, request);
        return ResultUtil.success("提交成功");
    }
}
