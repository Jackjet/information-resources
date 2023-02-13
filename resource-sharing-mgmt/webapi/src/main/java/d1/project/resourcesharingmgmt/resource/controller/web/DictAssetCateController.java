package d1.project.resourcesharingmgmt.resource.controller.web;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.business.DictAssetCateBusiness;
import d1.project.resourcesharingmgmt.resource.entity.DictAssetCateEntity;
import d1.project.resourcesharingmgmt.resource.model.DicAssetCateTree.DictAssetCateTreeVm;
import d1.project.resourcesharingmgmt.resource.service.DictAssetCateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 门户端_资源目录分类
 *
 * @author zhengyang
 */
@Auth("noauth")
@RestController("/web/dictAssetCate")
@RequestMapping("/web/dictAssetCate")
public class DictAssetCateController {
    private final DictAssetCateBusiness dictAssetCateBusiness;
    private final DictAssetCateService dictAssetCateService;

    public DictAssetCateController(DictAssetCateBusiness dictAssetCateBusiness, DictAssetCateService dictAssetCateService) {
        this.dictAssetCateBusiness = dictAssetCateBusiness;
        this.dictAssetCateService = dictAssetCateService;
    }

    /**
     * 查询资源目录分类树
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/findAll")
    public Result<List<DictAssetCateTreeVm>> findAll(HttpServletRequest request) throws Exception {
        return ResultUtil.success("", dictAssetCateBusiness.getWebTreeList(request));
    }

    /**
     * 根据父节点ID查询所有分类目录,2:基础信息 3:主题信息 4:部门信息
     * @param parTypId 2:基础信息 3:主题信息 4:部门信息
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/findAllByParTypId")
    public Result<List<DictAssetCateTreeVm>> findAllByParTypId(String parTypId) throws Exception {
        return ResultUtil.success("", dictAssetCateService.findAllByParTypId(parTypId));
    }

    /**
     * 云数据详情
     * @param id
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/find")
    public Result<DictAssetCateEntity> find(String id) throws DoValidException {
        return ResultUtil.success("", dictAssetCateService.find(id));
    }
}
