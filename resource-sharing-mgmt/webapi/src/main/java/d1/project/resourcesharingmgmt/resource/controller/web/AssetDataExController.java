package d1.project.resourcesharingmgmt.resource.controller.web;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.entity.AssetDataExEntity;
import d1.project.resourcesharingmgmt.resource.model.AssetApiEx.AssetDataExFindAllVm;
import d1.project.resourcesharingmgmt.resource.service.AssetDataExService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 门户端_云数据
 *
 * @author zhengyang
 */
@Auth("noauth")
@RestController("/web/assetDataEx")
@RequestMapping("/web/assetDataEx")
public class AssetDataExController {

    private final AssetDataExService assetDataExService;

    public AssetDataExController(AssetDataExService assetDataExService) {
        this.assetDataExService = assetDataExService;
    }

    /**
     * 查询资源目录下，云数据(分页)
     * @param model
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/findAll")
    public Result<Page<AssetDataExEntity>> findAll(AssetDataExFindAllVm model) throws Exception {
        return ResultUtil.success("", assetDataExService.findAll(model));
    }

    /**
     * 云数据详情
     * @param id
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/find")
    public Result<AssetDataExEntity> find(String id) throws DoValidException {
        return ResultUtil.success("", assetDataExService.find(id));
    }
}
