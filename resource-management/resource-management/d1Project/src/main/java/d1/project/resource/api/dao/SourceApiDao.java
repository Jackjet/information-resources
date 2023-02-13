package d1.project.resource.api.dao;

import d1.project.resource.api.entity.SourceApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author baozh
 */
public interface SourceApiDao extends JpaRepository<SourceApi, String>, JpaSpecificationExecutor<SourceApi> {
    /**
     * find all by api name
     *
     * @param name api name
     * @return api list
     */
    List<SourceApi> findAllByNameIn(List<String> name);

    /**
     * exists by api name
     *
     * @param name api name
     * @return if exists return true else false
     */
    Boolean existsByName(String name);

    /**
     * exists by group id
     *
     * @param groupId group id
     * @return if exists return true otherwise false
     */
    boolean existsAllByGroupId(String groupId);

    /**
     * exists name which not this id
     *
     * @param name data name
     * @param id   data id
     * @return if exists return true otherwise false
     */
    boolean existsByNameAndIdNot(String name, String id);
}
