package d1.project.resourcesharingmgmt.resource.business;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.TokenManager;
import d1.project.resourcesharingmgmt.resource.entity.DictAssetCateEntity;
import d1.project.resourcesharingmgmt.resource.model.DicAssetCateTree.DictAssetCateTreeVm;
import d1.project.resourcesharingmgmt.resource.service.DictAssetCateService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 目录分类管理
 *
 * @author JungYoung
 */
@Service
public class DictAssetCateBusiness {
    private final DictAssetCateService dictAssetCateService;

    public DictAssetCateBusiness(DictAssetCateService dictAssetCateService) {
        this.dictAssetCateService = dictAssetCateService;
    }

    public DictAssetCateEntity find(String id) throws DoValidException {
        DictAssetCateEntity dictAssetCateEntity = dictAssetCateService.find(id).orElseThrow(() -> new DoValidException("目录分类管理不存在"));
        return dictAssetCateEntity;
    }

    /**
     * 获取门户树
     *
     * @return
     * @throws Exception
     */
    public List<DictAssetCateTreeVm> getWebTreeList(HttpServletRequest request) throws Exception {
        List<DictAssetCateEntity> dictAssetCateEntityList = dictAssetCateService.findAll();
        List<DictAssetCateTreeVm> dictAssetCateTreeVmList = getDictAssetList(dictAssetCateEntityList, "");
        return dictAssetCateTreeVmList;
    }

    /**
     * 信息资源分类树
     *
     * @return
     * @throws Exception
     */
    public List<DictAssetCateTreeVm> getDictAssetTreeList(HttpServletRequest request) throws Exception {
        JSONObject webAdminUserVm = TokenManager.getInstance().getUserByToken(request);
        String orgId = "";
        if (!webAdminUserVm.getString("roleId").contains("admin")) {
            orgId = webAdminUserVm.getString("organizationId");
        }

        List<DictAssetCateEntity> dictAssetCateEntityList = dictAssetCateService.findAll();
        List<DictAssetCateTreeVm> chlidrenDicAsset = getDictAssetList(dictAssetCateEntityList, orgId);
        return chlidrenDicAsset;
    }

    /**
     * 目录分类与部门绑定
     *
     * @param entityList 目录分类树
     * @param orgId      部门id
     * @return
     */
    private List<DictAssetCateTreeVm> getDictAssetListByOrgId(List<DictAssetCateTreeVm> entityList, String orgId) {
        for (DictAssetCateTreeVm dictAssetCateTreeVm : entityList) {
            if ("4".equals(dictAssetCateTreeVm.getTypId())) {
                for (DictAssetCateTreeVm dictAssetCateTreeVm1 : dictAssetCateTreeVm.getChildren()) {
                    if (dictAssetCateTreeVm1.getOrgId() != null) {
                        List<DictAssetCateTreeVm> resultList = dictAssetCateTreeVm1.getChildren().stream().filter(t -> orgId.equals(t.getOrgId().toString())).collect(Collectors.toList());
                        dictAssetCateTreeVm1.setChildren(resultList);
                    }
                }

                for (DictAssetCateTreeVm cateTreeVm : dictAssetCateTreeVm.getChildren()) {
                    List<DictAssetCateTreeVm> list = new ArrayList<>();
                    if (cateTreeVm.getChildren() != null && cateTreeVm.getChildren().size() > 0) {
                        list = dictAssetCateTreeVm.getChildren().stream().filter(t -> t.getTypId().equals(cateTreeVm.getTypId())).collect(Collectors.toList());
                        dictAssetCateTreeVm.setChildren(list);
                    }
                }
            }
        }
        return entityList;
    }

    /**
     * 获取根节点的目录分类
     *
     * @param dictAssetCateEntityList
     * @return
     * @throws Exception
     */
    private List<DictAssetCateTreeVm> getDictAssetList(List<DictAssetCateEntity> dictAssetCateEntityList, String orgId) throws Exception {
        List<DictAssetCateEntity> frists = dictAssetCateEntityList.stream().filter(t -> t.getParTypId() == null || t.getParTypId().equals("")).collect(Collectors.toList());
        List<DictAssetCateTreeVm> assetCateTreeVmList = new ArrayList<>();
        for (DictAssetCateEntity entity : frists) {
            DictAssetCateTreeVm assetCateTreeVm = new DictAssetCateTreeVm();
            BeanUtils.copyProperties(entity, assetCateTreeVm);
            DictAssetCateTreeVm result = getChildList(dictAssetCateEntityList, assetCateTreeVm, 1, orgId);
            assetCateTreeVmList.add(result);
        }
        return assetCateTreeVmList;
    }

    /**
     * 递归，建立子树形的目录分类
     *
     * @param entityList
     * @param entityVm
     * @return
     */
    private DictAssetCateTreeVm getChildList(List<DictAssetCateEntity> entityList, DictAssetCateTreeVm entityVm, int root, String orgId) {
        if (root <= 2) {
            List<DictAssetCateEntity> list = new ArrayList<>();
            //当前部门不为空的时候就不添加部门类下面的类目
            if (!"4".equals(entityVm.getTypId())) {
                list = entityList.stream().filter(t -> entityVm.getTypId().equals(t.getParTypId())).collect(Collectors.toList());
            } else {
                if (StringUtils.isEmpty(orgId)) {
                    list = entityList.stream().filter(t -> entityVm.getTypId().equals(t.getParTypId())).collect(Collectors.toList());
                }
            }
            if (list.size() > 0) {
                List<DictAssetCateTreeVm> listVm = list.stream().map(e -> {
                    DictAssetCateTreeVm d = new DictAssetCateTreeVm();
                    BeanUtils.copyProperties(e, d);
                    return d;
                }).collect(Collectors.toList());
                entityVm.setChildren(listVm);
                for (DictAssetCateTreeVm assetCateTreeVm : listVm) {
                    getChildList(entityList, assetCateTreeVm, root + 1, orgId);
                }
            }
        }
        return entityVm;
    }
}
