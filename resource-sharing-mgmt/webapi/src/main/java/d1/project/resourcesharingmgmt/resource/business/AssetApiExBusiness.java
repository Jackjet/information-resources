package d1.project.resourcesharingmgmt.resource.business;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.resourcesharingmgmt.common.service.IOperationLogService;
import d1.project.resourcesharingmgmt.resource.entity.AssetApiExEntity;
import d1.project.resourcesharingmgmt.resource.service.AssetApiExService;
import org.springframework.stereotype.Service;

/**
 * 信息资源目录与云数据关联表
 * @author JungYoung
 */
@Service
public class AssetApiExBusiness {
    private final AssetApiExService assetApiExService;

    public AssetApiExBusiness(AssetApiExService assetApiExService) {
        this.assetApiExService = assetApiExService;
    }

    public AssetApiExEntity find(String id) throws DoValidException {
        AssetApiExEntity assetApiExEntity = assetApiExService.find(id).orElseThrow(() -> new DoValidException("信息资源目录与云数据关联表不存在"));
        return assetApiExEntity;
    }


}
