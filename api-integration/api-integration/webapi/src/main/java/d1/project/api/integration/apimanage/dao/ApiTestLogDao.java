package d1.project.api.integration.apimanage.dao;

import d1.project.api.integration.apimanage.entity.ApiTestLog;
import d1.project.api.integration.apimanage.model.ApiLogVm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Calendar;
import java.util.List;

/**
 * @author baozh
 */
public interface ApiTestLogDao extends JpaRepository<ApiTestLog, String>, JpaSpecificationExecutor<ApiTestLog> {

    /**
     * 根据API ID 查询历史记录
     *
     * @param apiId     API ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 查询结果
     */
    @Query(value = "SELECT NEW d1.project.api.integration.apimanage.model.ApiLogVm(info.id,info.method,info.requestTime,info.responseCode,info.responseTime) FROM ApiTestLog AS info WHERE info.apiId = ?1 AND info.requestTime BETWEEN ?2 AND ?3 ORDER BY info.requestTime DESC")
    List<ApiLogVm> findLogByApiId(String apiId, Calendar startTime, Calendar endTime);
}
