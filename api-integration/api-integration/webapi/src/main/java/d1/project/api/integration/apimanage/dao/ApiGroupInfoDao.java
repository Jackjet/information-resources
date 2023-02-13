package d1.project.api.integration.apimanage.dao;

import d1.project.api.integration.apimanage.entity.ApiGroupInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author baozh
 */
public interface ApiGroupInfoDao extends JpaRepository<ApiGroupInfo, String>, JpaSpecificationExecutor<ApiGroupInfo> {

    /**
     * 查询名称是否存在
     *
     * @param name 名称
     * @return 查询结果
     */
    boolean existsAllByName(String name);

    /**
     * 查询名称是否存在
     *
     * @param name 名称
     * @param id   数据ID
     * @return 查询结果
     */
    boolean existsAllByNameAndIdNot(String name, String id);

    List<ApiGroupInfo> findAllByNameContainsAndParentId(String name,String parentId);

    List<ApiGroupInfo> findAllByNameContains(String name);

    boolean existsByParentId(String parenId);
}
