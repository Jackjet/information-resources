package d1.project.resourcesharingmgmt.resource.service;

import d1.project.resourcesharingmgmt.resource.dao.AssetCateExDao;
import d1.project.resourcesharingmgmt.resource.entity.AssetCateExEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 信息资源目录与信息类型关联表
 *
 * @author zhengyang
 */
@Service
public class AssetCateExService {
    private final AssetCateExDao assetCateExDao;

    public AssetCateExService(AssetCateExDao assetCateExDao) {
        this.assetCateExDao = assetCateExDao;
    }

    /**
     * 详情
     *
     * @param id id
     */
    public Optional<AssetCateExEntity> find(String id) {
        return assetCateExDao.findById(id);
    }

    public boolean existsByInfoIdAndAndTypId(String infoId, String typId) {
        return assetCateExDao.existsByInfoIdAndAndTypId(infoId, typId);
    }

    public void deleteByInfoId(String infoId) {
        assetCateExDao.deleteByInfoId(infoId);
    }

    public void insert(AssetCateExEntity entity) {
        assetCateExDao.save(entity);
    }
}
