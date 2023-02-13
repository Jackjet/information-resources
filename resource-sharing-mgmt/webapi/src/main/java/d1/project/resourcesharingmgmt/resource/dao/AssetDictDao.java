package d1.project.resourcesharingmgmt.resource.dao;

import d1.project.resourcesharingmgmt.resource.entity.AssetDictEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 资源目录字典
 *
 * @author zhengyang
 */
public interface AssetDictDao extends JpaRepository<AssetDictEntity, String>, JpaSpecificationExecutor<AssetDictEntity> {
    boolean existsById(String id);

    boolean existsByValueAndType(String value,String type);

    @Modifying
    @Query("delete from AssetDictEntity s where s.id in (?1)")
    void deleteBatch(List<String> ids);
}
