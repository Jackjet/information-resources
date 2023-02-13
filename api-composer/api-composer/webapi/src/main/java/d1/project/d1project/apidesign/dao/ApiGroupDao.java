package d1.project.d1project.apidesign.dao;

import d1.project.d1project.apidesign.entity.ApiGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author zhengyang
 */
public interface ApiGroupDao extends JpaRepository<ApiGroup, String>, JpaSpecificationExecutor<ApiGroup> {
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
