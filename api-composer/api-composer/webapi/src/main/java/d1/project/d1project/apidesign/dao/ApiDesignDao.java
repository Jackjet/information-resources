package d1.project.d1project.apidesign.dao;

import d1.project.d1project.apidesign.entity.ApiDesign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author baozh
 */
public interface ApiDesignDao extends JpaRepository<ApiDesign, String>, JpaSpecificationExecutor<ApiDesign> {

    /**
     * 查询该名称是否存在
     *
     * @param name 名称
     * @return 结果
     */
    boolean existsAllByName(String name);

    /**
     * 查询非当前ID下该名称是否存在
     *
     * @param name 名称
     * @param id   数据ID
     * @return 结果
     */
    boolean existsAllByNameAndIdNot(String name, String id);

    /**
     * 查询请求路径是否存在
     *
     * @param requestUrl 名称
     * @return 结果
     */
    boolean existsAllByRequestUrl(String requestUrl);

    /**
     * 查询查询非当前ID下请求路径是否存在
     *
     * @param requestUrl 名称
     * @return 结果
     */
    boolean existsAllByRequestUrlAndIdNot(String requestUrl, String id);

    /**
     * 根据组ID查询是否存在数据
     *
     * @param groupId 组ID
     * @return 查询结果
     */
    boolean existsAllByGroupId(String groupId);
}
