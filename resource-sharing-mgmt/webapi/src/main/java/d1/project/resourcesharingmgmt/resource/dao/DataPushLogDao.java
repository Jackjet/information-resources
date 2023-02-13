package d1.project.resourcesharingmgmt.resource.dao;

import d1.project.resourcesharingmgmt.resource.entity.DataPushLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据推送日志
 *
 * @author zhengyang
 */
public interface DataPushLogDao extends JpaRepository<DataPushLogEntity, String>, JpaSpecificationExecutor<DataPushLogEntity> {
    @Query(value = "select coalesce(sum(num),0) from DataPushLogEntity ")
    Long countSum();

    /**
     * 查找某一条访问的数据量
     *
     * @param day
     * @return
     */
    @Query(value = "select coalesce(sum(num),0) from d1_data_push_log where to_char(create_time\\:\\:DATE, 'YYYY-MM-DD') =?1 ", nativeQuery = true)
    Long countByDay(String day);

    /**
     * 查找部门访问的数据量
     *
     * @param orgId
     * @return
     */
    @Query(value = "select coalesce(sum(num),0) from DataPushLogEntity where createDeptId =?1 ")
    Long countUseByOrgId(String orgId);

    /**
     * 查找部门提供的数据量
     *
     * @param orgId
     * @return
     */
    @Query(value = "select coalesce(sum(num),0) from DataPushLogEntity where provOrgId =?1 ")
    Long countGiveByOrgId(String orgId);
}
