package d1.project.resourcesharingmgmt.resource.controller;

import d1.framework.webapi.annotation.Auth;
import d1.project.resourcesharingmgmt.resource.entity.AssetDictEntity;
import d1.project.resourcesharingmgmt.resource.model.AssetDict.AssetDictFindAllVm;
import d1.project.resourcesharingmgmt.resource.service.AssetDictService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资源目录字典查询
 *
 * @author zhengyang
 */
@Auth("webadmin")
@RestController("/webadmin/assetDict")
@RequestMapping("/webadmin/assetDict")
public class AssetDictController {

    private final AssetDictService assetDictService;

    public AssetDictController(AssetDictService assetDictService) {
        this.assetDictService = assetDictService;
    }

    /**
     * 查询所有字典
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/findAll")
    public List<AssetDictEntity> findAll(AssetDictFindAllVm model) throws Exception {
        return assetDictService.findAll(model);
    }
}
