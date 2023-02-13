package d1.project.resourcesharingmgmt.resource.dao;

import d1.project.resourcesharingmgmt.resource.entity.AssetApiExEntity;
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
public interface AssetApiExDao extends JpaRepository<AssetApiExEntity, String>, JpaSpecificationExecutor<AssetApiExEntity> {
   /**
    * 根据资源目录id和apiId查询信息
    * @param uviewId
    * @param sourceApiId
    * @return
    */
   Boolean existsByUviewIdAndSourceApiId(String uviewId, String sourceApiId);

   Optional<AssetApiExEntity> findByUviewIdAndSourceApiId(String uviewId, String sourceApiId);

   int countByUviewId(String uviewId);

   List<AssetApiExEntity> findByUviewId(String uviewId);

   @Query("select count(1) from AssetApiExEntity as a JOIN ArchBusiUviewExEntity e on e.uviewId=a.uviewId where e.provOrgId=?1")
   long countByOrgId(String orgId);

   void deleteByUviewId(String uviewId);
}
