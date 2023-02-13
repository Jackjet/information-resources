package d1.project.resourcesharingmgmt.resource.dao;

import d1.project.resourcesharingmgmt.resource.entity.DemandedInfoEntity;
import d1.project.resourcesharingmgmt.resource.model.DemandedInfoAnalysisVm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 需求
 *
 * @author zhengyang
 */
public interface DemandedInfoDao extends JpaRepository<DemandedInfoEntity, String>, JpaSpecificationExecutor<DemandedInfoEntity> {
    @Query(value = "select new d1.project.resourcesharingmgmt.resource.model.DemandedInfoAnalysisVm(COALESCE(sum(CASE WHEN di.requestType = '1' THEN 1 ELSE 0 END),0),COALESCE(sum(CASE WHEN (di.requestType = '1' and di.status='0') THEN 1 ELSE 0 END),0),COALESCE(sum(CASE WHEN di.requestType = '0' THEN 1 ELSE 0 END),0),COALESCE(sum(CASE WHEN (di.requestType = '0' and di.status='0') THEN 1 ELSE 0 END),0),COALESCE(sum(CASE WHEN di.requestType = '3' THEN 1 ELSE 0 END),0),COALESCE(sum(CASE WHEN (di.requestType = '3' and di.status='0') THEN 1 ELSE 0 END),0),COALESCE(sum(CASE WHEN di.requestType = '2' THEN 1 ELSE 0 END),0),COALESCE(sum(CASE WHEN (di.requestType = '2' and di.status='0') THEN 1 ELSE 0 END),0)) from DemandedInfoEntity as di where di.createById=?1 ")
    DemandedInfoAnalysisVm findAnalysis(String createById);

    boolean existsByTitleAndAcceptDeptId(String title, String acceptDeptId);

    Integer countByStatus(String status);

    /**
     *
     * @param status
     * @param acceptDeptId 部门id
     * @return
     */
    Integer countByStatusAndAcceptDeptId(String status,String acceptDeptId);

    long countByReason(String reason);

    @Query(value = "SELECT count(*) from (select create_dept_id from d1_demanded_info GROUP BY create_dept_id) a", nativeQuery = true)
    long countByOrgNumDistinct();

    long countByRequestType(String requestType);

    long countByCreateDeptId(String createDeptId);
}
