package d1.project.dcrun.center.webapi.resource.dao;

import d1.project.dcrun.center.webapi.resource.entity.GroupInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author baozh
 */
public interface GroupInfoDao extends JpaRepository<GroupInfo, String>, JpaSpecificationExecutor<GroupInfo> {
    /**
     * find all by type , order by update time desc
     *
     * @param type group type
     * @return group list
     */
    List<GroupInfo> findAllByTypeOrderByUpdateTimeAsc(String type);

    /**
     * check repeat info by parent id & name
     *
     * @param parentId group parent id
     * @param name     group name
     * @return exists return true otherwise false;
     */
    boolean existsAllByParentIdAndName(String parentId, String name);

    /**
     * check repeat info by type & name
     *
     * @param type group type
     * @param name group name
     * @return exists return true otherwise false;
     */
    boolean existsAllByTypeAndName(String type, String name);


    /**
     * check repeat info which id is not this by parent id & name
     *
     * @param parentId group parent id
     * @param name     group name
     * @param id       group id
     * @return exists return true otherwise false;
     */
    boolean existsAllByParentIdAndNameAndIdNot(String parentId, String name, String id);

    /**
     * find all by parent id
     *
     * @param parentId parent id
     * @return group list
     */
    List<GroupInfo> findAllByParentId(String parentId);
}
