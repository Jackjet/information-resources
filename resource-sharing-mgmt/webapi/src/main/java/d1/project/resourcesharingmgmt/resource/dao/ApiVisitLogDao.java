package d1.project.resourcesharingmgmt.resource.dao;

import d1.project.resourcesharingmgmt.resource.entity.ApiVisitLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * API访问日志
 *
 * @author zhengyang
 */
public interface ApiVisitLogDao extends JpaRepository<ApiVisitLogEntity, String>, JpaSpecificationExecutor<ApiVisitLogEntity> {
    /**
     * 查找某一条访问的数据量
     *
     * @param day
     * @return
     */
    @Query(value = "select count(id) from d1_api_visit_log where to_char(request_time\\:\\:DATE, 'YYYY-MM-DD')=?1 ", nativeQuery = true)
    Long countByDay(String day);

    /**
     * 查找部门访问的数据量
     *
     * @param orgId
     * @return
     */
    @Query(value = "select count(av.id) from ApiVisitLogEntity as av join ResourceUseInfoEntity ru on av.apiId=ru.kongApiId where ru.createDeptId =?1 ")
    Long countUseByOrgId(String orgId);

    /**
     * 查找部门提供的数据量
     *
     * @param orgId
     * @return
     */
    @Query(value = "select count(av.id) from ApiVisitLogEntity as av join ResourceUseInfoEntity ru on av.apiId=ru.kongApiId where ru.provOrgId =?1 ")
    Long countGiveByOrgId(String orgId);
}
