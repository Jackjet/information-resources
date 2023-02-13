package d1.project.api.integration.apimanage.dao;

import d1.project.api.integration.apimanage.entity.ApiBaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author baozh
 */
public interface ApiBaseInfoDao extends JpaRepository<ApiBaseInfo, String>, JpaSpecificationExecutor<ApiBaseInfo> {

    /**
     * 根据名称查询API是否存在
     *
     * @param name 名称
     * @return 查询结果
     */
    boolean existsAllByName(String name);

    /**
     * 根据名称查询API是否存在
     *
     * @param name 名称
     * @param id   ID
     * @return 查询结果
     */
    boolean existsAllByNameAndIdNot(String name, String id);

    /**
     * 根据API名称查询结果
     *
     * @param name 名称
     * @return 查询结果
     */
    ApiBaseInfo findFirstByName(String name);

    /**
     * 根据ID查询结果
     *
     * @param ids ID集合
     * @return 查询结果
     */
    List<ApiBaseInfo> findAllByIdIn(List<String> ids);


    int countAllByGroupId(String groupId);

    List<ApiBaseInfo> findAllByGroupId(String groupId);

    List<ApiBaseInfo> findAllByContainer(String container);

    int countByGroupId(String groupId);
}
