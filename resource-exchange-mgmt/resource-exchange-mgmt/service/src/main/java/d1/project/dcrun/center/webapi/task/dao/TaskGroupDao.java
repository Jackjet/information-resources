package d1.project.dcrun.center.webapi.task.dao;

import d1.project.dcrun.center.webapi.task.entity.TaskGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author zhengyang
 */
public interface TaskGroupDao extends JpaRepository<TaskGroup, String>, JpaSpecificationExecutor<TaskGroup> {
    /**
     * 根据父级ID和名称查询是否已存在
     *
     * @param name     名称
     * @param parentId 父级ID
     * @return 查询结果
     */
    boolean existsAllByNameAndParentId(String name, String parentId);

    /**
     * 查询非当前ID以及该父级下是否存在同名
     *
     * @param name     名称
     * @param parentId 父级ID
     * @param id       数据ID
     * @return 查询结果
     */
    boolean existsAllByNameAndParentIdAndIdNot(String name, String parentId, String id);

    /**
     * 根据父级ID查询是否已存在
     *
     * @param parentId 父级ID
     * @return 查询结果
     */
    boolean existsAllByParentId(String parentId);
}
