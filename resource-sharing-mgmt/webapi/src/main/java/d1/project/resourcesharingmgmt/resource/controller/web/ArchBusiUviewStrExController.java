package d1.project.resourcesharingmgmt.resource.controller.web;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
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
 * 门户端_信息项
 *
 * @author zhengyang
 */
@Auth("noauth")
@RestController("/web/archBusiUviewStrEx")
@RequestMapping("/web/archBusiUviewStrEx")
public class ArchBusiUviewStrExController {

    private final ArchBusiUviewStrExService archBusiUviewStrExService;

    public ArchBusiUviewStrExController(ArchBusiUviewStrExService archBusiUviewStrExService) {
        this.archBusiUviewStrExService = archBusiUviewStrExService;
    }

    /**
     * 信息项分页查询
     * @param model
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/findAll")
    public Result<Page<ArchBusiUviewStrExEntity>> findAll(ArchBusiUviewStrExFindAllVm model) throws Exception {
        return ResultUtil.success("", archBusiUviewStrExService.findAll(model));
    }

    /**
     * 信息项详情
     * @param id
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/find")
    public Result<ArchBusiUviewStrExEntity> find(String id) throws DoValidException {
        return ResultUtil.success("", archBusiUviewStrExService.find(id));
    }
}
