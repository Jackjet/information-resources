package d1.project.resourcesharingmgmt.resource.controller.web;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.entity.AssetFileExEntity;
import d1.project.resourcesharingmgmt.resource.model.AssetApiEx.AssetFileExFindAllVm;
import d1.project.resourcesharingmgmt.resource.service.AssetFileExService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 门户端_云文件
 *
 * @author zhengyang
 */
@Auth("noauth")
@RestController("/web/assetFileEx")
@RequestMapping("/web/assetFileEx")
public class AssetFileExController {

    private final AssetFileExService assetFileExService;

    public AssetFileExController(AssetFileExService assetFileExService) {
        this.assetFileExService = assetFileExService;
    }

    /**
     * 查询资源目录下，云文件(分页)
     * @param model
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/findAll")
    public Result<Page<AssetFileExEntity>> findAll(AssetFileExFindAllVm model) throws Exception {
        return ResultUtil.success("", assetFileExService.findAll(model));
    }

    /**
     * 云文件详情
     * @param id
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/find")
    public Result<AssetFileExEntity> find(String id) throws DoValidException {
        return ResultUtil.success("", assetFileExService.find(id));
    }
}
