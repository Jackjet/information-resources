package d1.project.resourcesharingmgmt.resource.dao;

import d1.project.resourcesharingmgmt.resource.entity.AssetFileExEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * 信息资源目录与云数据关联表
 *
 * @author zhengyang
 */
public interface AssetFileExDao extends JpaRepository<AssetFileExEntity, String>, JpaSpecificationExecutor<AssetFileExEntity> {
   int countByUviewId(String uviewId);

   List<AssetFileExEntity> findByUviewId(String uviewId);

   boolean existsByUviewIdAndName(String uviewId, String name);

   Optional<AssetFileExEntity> findFirstByUviewIdAndName(String uviewId, String name);

   @Query("select count(1) from AssetFileExEntity as f JOIN ArchBusiUviewExEntity e on e.uviewId=f.uviewId where e.provOrgId=?1")
   long countByOrgId(String orgId);

   void deleteByUviewId(String uviewId);
}
