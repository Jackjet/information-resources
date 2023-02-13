package d1.project.resourcesharingmgmt.resource.dao;

import d1.project.resourcesharingmgmt.resource.entity.DictAssetCateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 目录分类管理
 *
 * @author zhengyang
 */
public interface DictAssetCateDao extends JpaRepository<DictAssetCateEntity, String>, JpaSpecificationExecutor<DictAssetCateEntity> {
    /**
     * 根据id判断是否存在目录分类信息
     * @param typId
     * @return
     */
    boolean existsByTypId(String typId);

    /**
     * 根据分类
     * @param parTypId
     * @return
     */
    List<DictAssetCateEntity> findAllByParTypId(String parTypId);

    /**
     * 删除分类
     * @param typId
     */
    void deleteByTypId(String typId);

    @Modifying
    @Query("delete from DictAssetCateEntity s where s.typId in (?1)")
    void deleteBatchByTypId(List<String> ids);
}
