package d1.project.d1project.apidesign.dao;

import d1.project.d1project.apidesign.entity.ApiTestCase;
import d1.project.d1project.apidesign.model.ApiTestCaseVm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Calendar;
import java.util.List;

/**
 * @author baozh
 */
public interface ApiTestCaseDao extends JpaRepository<ApiTestCase, String>, JpaSpecificationExecutor<ApiTestCase> {

    /**
     * 根据ApiId 查询测试用例
     *
     * @param apiId ApiId
     * @param st    ApiId
     * @param ed    ApiId
     * @return 查询结果
     */
    @Query(value = "SELECT new d1.project.d1project.apidesign.model.ApiTestCaseVm(info.id,info.method,info.ip,info.createTime) FROM ApiTestCase as info WHERE info.apiId = ?1 AND info.createTime BETWEEN ?2 AND ?3 ORDER BY info.createTime DESC")
    List<ApiTestCaseVm> findCaseByApiId(String apiId, Calendar st, Calendar ed);
}
