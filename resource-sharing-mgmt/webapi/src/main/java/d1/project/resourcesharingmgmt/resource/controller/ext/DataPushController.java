package d1.project.resourcesharingmgmt.resource.controller.ext;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.common.utils.BaseUtils;
import d1.project.resourcesharingmgmt.resource.entity.DataPushLogEntity;
import d1.project.resourcesharingmgmt.resource.model.DataPush.DataPushLogInsertVm;
import d1.project.resourcesharingmgmt.resource.service.DataPushLogService;
import d1.project.resourcesharingmgmt.system.entity.OrganizationEntity;
import d1.project.resourcesharingmgmt.system.service.OrganizationService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Optional;

/**
 * 对外接口-数据推送
 *
 * @author zhengyang
 */
@Auth("noauth")
@RestController("/ext/dataPush")
@RequestMapping("/ext/dataPush")
public class DataPushController {
    private final DataPushLogService dataPushLogService;
    private final OrganizationService organizationService;

    public DataPushController(DataPushLogService dataPushLogService, OrganizationService organizationService) {
        this.dataPushLogService = dataPushLogService;
        this.organizationService = organizationService;
    }

    /**
     * 推送数据详情
     *
     * @param from
     * @param to
     * @param num
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/info")
    public Result<String> findAllList(@Valid @RequestBody DataPushLogInsertVm model) throws Exception {
        Optional<OrganizationEntity> fromOrg = organizationService.findFirstByNameLike("%" + model.getFrom() + "%");
        Optional<OrganizationEntity> toOrg = organizationService.findFirstByNameLike("%" + model.getTo() + "%");

        if (!fromOrg.isPresent() || !toOrg.isPresent()) {
            throw new Exception("机构未找到");
        }

        DataPushLogEntity entity = new DataPushLogEntity();
        entity.setId(BaseUtils.generate32Id());
        entity.setCreateTime(Calendar.getInstance());
        entity.setProvOrgId(fromOrg.get().getId());
        entity.setCreateDeptId(toOrg.get().getId());
        entity.setNum(model.getNum());
        dataPushLogService.save(entity);
        return ResultUtil.success("");
    }
}
