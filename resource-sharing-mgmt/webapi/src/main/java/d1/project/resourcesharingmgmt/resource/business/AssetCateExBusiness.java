package d1.project.resourcesharingmgmt.resource.business;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.resourcesharingmgmt.common.service.IOperationLogService;
import d1.project.resourcesharingmgmt.resource.entity.AssetCateExEntity;
import d1.project.resourcesharingmgmt.resource.service.AssetCateExService;
import org.springframework.stereotype.Service;

/**
 * 信息资源目录与信息类型关联表
 * @author JungYoung
 */
@Service
public class AssetCateExBusiness {
    private final AssetCateExService assetCateExService;

    public AssetCateExBusiness(AssetCateExService assetCateExService) {
        this.assetCateExService = assetCateExService;
    }

    public AssetCateExEntity find(String id) throws DoValidException {
        AssetCateExEntity assetCateExEntity = assetCateExService.find(id).orElseThrow(() -> new DoValidException("信息资源目录与信息类型关联表不存在"));
        return assetCateExEntity;
    }
}
