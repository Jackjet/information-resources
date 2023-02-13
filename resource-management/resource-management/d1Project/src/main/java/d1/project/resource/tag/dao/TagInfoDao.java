package d1.project.resource.tag.dao;

import d1.project.resource.tag.entity.TagInfo;
import d1.project.resource.tag.model.TagReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author baozh
 */
public interface TagInfoDao extends JpaRepository<TagInfo, String>, JpaSpecificationExecutor<TagInfo> {

    /**
     * get tag list
     *
     * @return tag list
     */
    @Query("SELECT new d1.project.resource.tag.model.TagReturn(info.name) FROM TagInfo AS info ")
    List<TagReturn> getTagList();

    /**
     * check name&groupId
     *
     * @param name tag name
     * @return if exists return true else false
     */
    boolean existsAllByName(String name);

    /**
     * check name&groupId
     *
     * @param name tag name
     * @param id   data id
     * @return if exists return true else false
     */
    boolean existsAllByNameAndIdNot(String name, String id);

    /**
     * exists by group id
     *
     * @param groupId group id
     * @return if exists return true otherwise false
     */
    boolean existsAllByGroupId(String groupId);
}
