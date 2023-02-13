package d1.project.resourcesharingmgmt.resource.dao;

import d1.project.resourcesharingmgmt.resource.entity.ArchBusiUviewExEntity;
import d1.project.resourcesharingmgmt.resource.model.Screen.OrgUseCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * 信息资源目录
 *
 * @author zhengyang
 */
public interface ArchBusiUviewExDao extends JpaRepository<ArchBusiUviewExEntity, String>, JpaSpecificationExecutor<ArchBusiUviewExEntity> {
    /**
     * 根据资源ID查询数据
     * @param uviewId
     * @return
     */
    Optional<ArchBusiUviewExEntity> findByUviewId(String uviewId);

    Optional<ArchBusiUviewExEntity> findByUviewNo(String uviewNo);

    boolean existsByUviewId(String uviewId);

    boolean existsByUviewNo(String uviewNo);

    long countByStatus(String status);

    Optional<ArchBusiUviewExEntity> findFirstByOrderByUpdateTimeDesc();

    long countByMediaFmt(String mediaFmt);

    long countByUpdateCyc(String updateCyc);

    long countByHookStatusOrFileHookStatusOrDataHookStatus(String hookStatus, String fileHookStatus, String dataHookStatus);

    long countByProvOrgId(String orgId);

    /**
     * 统计机构挂接了多少目录
     * @return
     */
    @Query(value = "select new d1.project.resourcesharingmgmt.resource.model.Screen.OrgUseCount(org.name,count(*)) from ArchBusiUviewExEntity as arch join OrganizationEntity as org on arch.provOrgId=org.id and org.level='1' where arch.hookStatus='1' or arch.fileHookStatus='1' or arch.dataHookStatus='1' group by org.name ")
    List<OrgUseCount> countOrgAndCenterCount();

    @Query("select count(1) from ArchBusiUviewExEntity arch where arch.provOrgId=?1 and (arch.hookStatus='1' or arch.fileHookStatus='1' or arch.dataHookStatus='1')")
    long countExByOrgId(String orgId);
}
