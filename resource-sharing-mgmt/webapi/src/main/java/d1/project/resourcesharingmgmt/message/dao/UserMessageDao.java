package d1.project.resourcesharingmgmt.message.dao;

import d1.project.resourcesharingmgmt.message.entity.UserMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-14 22:04
 */
public interface UserMessageDao extends JpaRepository<UserMessageEntity, String>, JpaSpecificationExecutor<UserMessageEntity> {

    /**
     * 按ID和用户ID查找
     *
     * @param id     id列表
     * @param userId 用户id
     * @return 实体
     */
    Optional<UserMessageEntity> findByIdAndUserId(String id, String userId);

    /**
     * 按ID列表和用户ID查找
     *
     * @param id     id列表
     * @param userId 用户id
     * @return 列表
     */
    List<UserMessageEntity> findByIdInAndUserId(Collection<String> id, String userId);

    /**
     * 按用户ID和状态查找
     *
     * @param userId 用户id
     * @param status 状态
     * @return 列表
     */
    List<UserMessageEntity> findByUserIdAndStatus(String userId, int status);


    /**
     * 按用户ID列表删除
     *
     * @param userId 用户id
     */
    void deleteByUserIdIn(Collection<String> userId);
}
