package d1.project.resourcesharingmgmt.resource.dao;

import d1.project.resourcesharingmgmt.resource.entity.ResourceUseInfoEntity;
import d1.project.resourcesharingmgmt.resource.model.ResourceUseInfoAnalysisVm;
import d1.project.resourcesharingmgmt.resource.model.Screen.SupportBusinessCountVm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * 资源使用
 *
 * @author zhengyang
 */
public interface ResourceUseInfoDao extends JpaRepository<ResourceUseInfoEntity, String>, JpaSpecificationExecutor<ResourceUseInfoEntity> {
    @Query(value = "select new d1.project.resourcesharingmgmt.resource.model.ResourceUseInfoAnalysisVm(COALESCE(sum(CASE WHEN rui.id is not null THEN 1 ELSE 0 END),0),COALESCE(sum(CASE WHEN rui.status='0' THEN 1 ELSE 0 END),0)) from ResourceUseInfoEntity as rui where rui.createById=?1 ")
    ResourceUseInfoAnalysisVm findAnalysis(String createById);

    /**
     * 判断用户是否已经申请了云接口
     *
     * @param uviewApiId
     * @param createById
     * @return
     */
    boolean existsByUviewApiIdAndCreateById(String uviewApiId, String createById);

    /**
     * 根据用户查询已经申请了云接口
     *
     * @param uviewApiId
     * @param createById
     * @return
     */
    ResourceUseInfoEntity findFirstByUviewApiIdAndCreateByIdOrderByCreateTimeDesc(String uviewApiId, String createById);

    /**
     * 判断用户是否已经申请了云接口
     *
     * @param fileId
     * @param createById
     * @return
     */
    boolean existsByFileIdAndCreateById(String fileId, String createById);

    /**
     * 根据用户查询已经申请了云文件
     *
     * @param fileId
     * @param createById
     * @return
     */
    ResourceUseInfoEntity findFirstByFileIdAndCreateByIdOrderByCreateTimeDesc(String fileId, String createById);

    /**
     * 判断用户是否已经申请了云数据
     *
     * @param dataId
     * @param createById
     * @return
     */
    boolean existsByDataIdAndCreateById(String dataId, String createById);

    /**
     * 根据用户查询已经申请了云数据
     *
     * @param dataId
     * @param createById
     * @return
     */
    ResourceUseInfoEntity findFirstByDataIdAndCreateByIdOrderByCreateTimeDesc(String dataId, String createById);

    Integer countByStatus(String status);

    Integer countByStatusAndProvOrgId(String status, String provOrgId);

    Optional<ResourceUseInfoEntity> findFirstByCreateByIdAndApiIdAndUviewId(String createById, String apiId, String uviewId);

    @Query(value = "SELECT count(*) from (select create_dept_id from d1_resource_use_info GROUP BY create_dept_id) a", nativeQuery = true)
    long countByOrgNumDistinct();

    long countByType(String type);

    @Query(value = "select count(id) from ResourceUseInfoEntity where to_char(create_time,'yyyy-MM') =?1 ")
    long countByMonth(String month);

    Optional<ResourceUseInfoEntity> findFirstByCreateDeptIdAndFileDownloadUri(String createDeptId, String fileDownloadUri);

    long countByCreateDeptIdAndStatus(String createDeptId, String status);

    @Query(value = "select new d1.project.resourcesharingmgmt.resource.model.Screen.SupportBusinessCountVm(rui.supportBusiness, count(*)) from ResourceUseInfoEntity as rui where rui.status='2' and rui.supportBusiness is not null GROUP BY rui.supportBusiness ")
    List<SupportBusinessCountVm> findSupportBusinessCount();
}
