package d1.project.resourcesharingmgmt.resource.controller.web;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.entity.AssetApiExEntity;
import d1.project.resourcesharingmgmt.resource.model.AssetApiEx.AssetApiExFindAllVm;
import d1.project.resourcesharingmgmt.resource.model.AssetApiEx.AssetApiExVm;
import d1.project.resourcesharingmgmt.resource.service.AssetApiExService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 门户端_云接口
 *
 * @author zhengyang
 */
@Auth("noauth")
@RestController("/web/assetApiEx")
@RequestMapping("/web/assetApiEx")
public class AssetApiExController {

    private final AssetApiExService assetAapiExService;

    public AssetApiExController(AssetApiExService assetAapiExService) {
        this.assetAapiExService = assetAapiExService;
    }

    /**
     * 查询资源目录下，云接口(分页)
     * @param model
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/findAll")
    public Result<Page<AssetApiExEntity>> findAll(AssetApiExFindAllVm model) throws Exception {
        return ResultUtil.success("", assetAapiExService.findAll(model));
    }

    /**
     * 云接口详情
     * @param id
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/find")
    public Result<AssetApiExVm> find(String id, HttpServletRequest request) throws DoValidException {
        return ResultUtil.success("", assetAapiExService.findById(id, request));
    }
}
