package d1.project.d1project.apidesign.dao;

import d1.project.d1project.apidesign.entity.ApiDevelopment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;

/**
 * @author baozh
 */
public interface ApiDevelopmentDao extends JpaRepository<ApiDevelopment, String>, JpaSpecificationExecutor<ApiDevelopment> {

    /**
     * 根据API设计ID删除API开发信息
     *
     * @param apiDesignId api设计ID
     */
    @Transactional(rollbackOn = Exception.class)
    @Modifying
    void deleteAllByApiDesignId(String apiDesignId);

    /**
     * 根据API ID查询信息
     *
     * @param apiId api id
     * @return 查询结果
     */
    ApiDevelopment findFirstByApiDesignId(String apiId);
}
