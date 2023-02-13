package d1.project.resourcesharingmgmt.resource.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.business.DictAssetCateBusiness;
import d1.project.resourcesharingmgmt.resource.model.DicAssetCateTree.DictAssetCateTreeVm;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 目录分类--树形结构
 * @author maoyuying
 */
@Auth("webadmin")
@RestController("/webadmin/dictAssetCate")
@RequestMapping("/webadmin/dictAssetCate")
public class DictAssetCateController {
    private final DictAssetCateBusiness dictAssetCateBusiness;

    public DictAssetCateController(DictAssetCateBusiness dictAssetCateBusiness){
        this.dictAssetCateBusiness = dictAssetCateBusiness;
    }

    @GetMapping(value = "/findAll")
    public Result<DictAssetCateTreeVm> findAll(HttpServletRequest request) throws Exception {
        return ResultUtil.success("",dictAssetCateBusiness.getDictAssetTreeList(request));
    }
}
