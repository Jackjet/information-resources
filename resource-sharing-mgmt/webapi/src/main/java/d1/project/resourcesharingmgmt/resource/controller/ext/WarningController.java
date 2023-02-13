package d1.project.resourcesharingmgmt.resource.controller.ext;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.common.utils.BaseUtils;
import d1.project.resourcesharingmgmt.resource.business.WarningBusiness;
import d1.project.resourcesharingmgmt.resource.entity.ArchBusiUviewExEntity;
import d1.project.resourcesharingmgmt.resource.entity.DataPushLogEntity;
import d1.project.resourcesharingmgmt.resource.model.ArchBusiUviewEx.ArchBusiUviewExWarningVm;
import d1.project.resourcesharingmgmt.resource.model.DataPush.DataPushLogInsertVm;
import d1.project.resourcesharingmgmt.resource.model.WarningVm;
import d1.project.resourcesharingmgmt.resource.service.DataPushLogService;
import d1.project.resourcesharingmgmt.system.entity.OrganizationEntity;
import d1.project.resourcesharingmgmt.system.service.OrganizationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.Optional;

/**
 * 对外接口-目录资源更新警告
 *
 * @author zhengyang
 */
@Auth("noauth")
@RestController("/warning")
@RequestMapping("/warning")
public class WarningController {
    private final WarningBusiness warningBusiness;

    public WarningController(WarningBusiness warningBusiness) {
        this.warningBusiness = warningBusiness;
    }

    /**
     * 目录资源更新警告列表
     *
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/findAll")
    public Result<Page<ArchBusiUviewExWarningVm>> findAll(WarningVm model, HttpServletRequest request) throws Exception {
        return ResultUtil.success("", warningBusiness.findAll(model, request));
    }
}
