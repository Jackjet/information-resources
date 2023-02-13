package d1.project.resourcesharingmgmt.resource.dao;

import d1.project.resourcesharingmgmt.resource.entity.AssetCateExEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * 信息资源目录与信息类型关联表
 *
 * @author zhengyang
 */
public interface AssetCateExDao extends JpaRepository<AssetCateExEntity, String>, JpaSpecificationExecutor<AssetCateExEntity> {
    /**
     * 根据资源目录ID和目录分类ID判断是否存在记录
     * @param infoId
     * @param typId
     * @return
     */
    boolean existsByInfoIdAndAndTypId(String infoId,String typId);

    void deleteByInfoId(String infoId);
}
