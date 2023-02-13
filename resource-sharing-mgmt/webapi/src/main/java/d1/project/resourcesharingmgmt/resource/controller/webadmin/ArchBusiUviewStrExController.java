package d1.project.resourcesharingmgmt.resource.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.entity.ArchBusiUviewStrExEntity;
import d1.project.resourcesharingmgmt.resource.model.ArchBusiUviewStrExFindAllVm;
import d1.project.resourcesharingmgmt.resource.service.ArchBusiUviewStrExService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 信息资源项
 * @author maoyuying
 */
@Auth("webadmin")
@RestController("/webadmin/archBusiUviewStrEx")
@RequestMapping("/webadmin/archBusiUviewStrEx")
public class ArchBusiUviewStrExController {
    private final ArchBusiUviewStrExService archBusiUviewStrExService;

    public ArchBusiUviewStrExController(ArchBusiUviewStrExService archBusiUviewStrExService) {
        this.archBusiUviewStrExService = archBusiUviewStrExService;
    }

    @GetMapping(value = "/findAll")
    public Result<Page<ArchBusiUviewStrExEntity>> findAll(ArchBusiUviewStrExFindAllVm model) throws Exception {
        return ResultUtil.success("", archBusiUviewStrExService.findAll(model));
    }
}
