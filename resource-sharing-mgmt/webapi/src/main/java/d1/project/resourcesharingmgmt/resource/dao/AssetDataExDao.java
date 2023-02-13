package d1.project.resourcesharingmgmt.resource.dao;

import d1.project.resourcesharingmgmt.resource.entity.AssetDataExEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * 信息资源目录与云数据库关联表
 *
 * @author zhengyang
 */
public interface AssetDataExDao extends JpaRepository<AssetDataExEntity, String>, JpaSpecificationExecutor<AssetDataExEntity> {
   int countByUviewId(String uviewId);

   List<AssetDataExEntity> findByUviewId(String uviewId);

   void deleteByUviewId(String uviewId);

   AssetDataExEntity findFirstByUviewIdOrderByCreateTimeDesc(String uviewId);

   @Query("select count(1) from AssetDataExEntity as d JOIN ArchBusiUviewExEntity e on e.uviewId=d.uviewId where e.provOrgId=?1")
   long countByOrgId(String orgId);
}
