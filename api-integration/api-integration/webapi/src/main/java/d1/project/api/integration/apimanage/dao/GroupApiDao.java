package d1.project.api.integration.apimanage.dao;

import d1.project.api.integration.apimanage.entity.GroupApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author baozh
 */
public interface GroupApiDao extends JpaRepository<GroupApi, String>, JpaSpecificationExecutor<GroupApi> {

    /**
     * 查询该分组ID下是否有数据存在
     *
     * @param groupId 分组ID
     * @return 查询结果
     */
    boolean existsAllByGroupId(String groupId);

    /**
     * 根据ID批量删除
     *
     * @param ids ID列表
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteAllByIdIn(List<String> ids);

    /**
     * 根据分组ID查询
     *
     * @param groupId 分组ID
     * @return 查询结果
     */
    List<GroupApi> findAllByGroupId(String groupId);
}
