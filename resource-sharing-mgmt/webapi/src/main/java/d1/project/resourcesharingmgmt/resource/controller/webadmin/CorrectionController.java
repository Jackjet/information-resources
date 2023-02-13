package d1.project.resourcesharingmgmt.resource.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.entity.CorrectionEntity;
import d1.project.resourcesharingmgmt.resource.model.correction.CorrectionFindAllVm;
import d1.project.resourcesharingmgmt.resource.model.correction.CorrectionUpdateVm;
import d1.project.resourcesharingmgmt.resource.service.CorrectionService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 管理端-纠错功能
 * @author zhengyang
 */
@Auth("webadmin")
@RestController("/webadmin/correction")
@RequestMapping("/webadmin/correction")
public class CorrectionController {

    private final CorrectionService correctionService;

    public CorrectionController(CorrectionService correctionService) {
        this.correctionService = correctionService;
    }

    /**
     * 查询纠错列表
     * @return
     * @throws Exception
     */
    @Auth("noauth")
    @GetMapping(value = "/findAll")
    public Result<Page<CorrectionEntity>> findAll(CorrectionFindAllVm model) throws Exception {
        return ResultUtil.success("", correctionService.findAll(model));
    }

    /**
     * 审批纠错
     * @param model
     * @param request
     * @return
     * @throws DoValidException
     */
    @PutMapping(value = "/update")
    public Result<String> update(@Valid @RequestBody CorrectionUpdateVm model, HttpServletRequest request) throws DoValidException {
        correctionService.update(model, request);
        return ResultUtil.success("");
    }

    /**
     * 查询纠错详情
     * @param id
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/find")
    public Result<CorrectionEntity> find(String id) throws DoValidException {
        return ResultUtil.success("", correctionService.find(id).orElseThrow(() -> new DoValidException("纠错不存在")));
    }
}
