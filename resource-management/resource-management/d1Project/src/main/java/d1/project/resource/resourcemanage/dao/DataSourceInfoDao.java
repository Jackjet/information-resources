package d1.project.resource.resourcemanage.dao;

import d1.project.resource.resourcemanage.entity.DataSourceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author baozh
 */
public interface DataSourceInfoDao extends JpaRepository<DataSourceInfo, String>, JpaSpecificationExecutor<DataSourceInfo> {

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
     * 查询该数据库链接是否存在
     *
     * @param host     数据库IP
     * @param port     端口号
     * @param dataName 数据库名
     * @return 结果
     */
    boolean existsAllByHostAndPortAndDataName(String host, String port, String dataName);

    /**
     * 查询非当前ID下该数据库链接是否存在
     *
     * @param host     数据库IP
     * @param port     端口号
     * @param dataName 数据库名
     * @param id       数据ID
     * @return 结果
     */
    boolean existsAllByHostAndPortAndDataNameAndIdNot(String host, String port, String dataName, String id);

    /**
     * exists by group id
     *
     * @param groupId group id
     * @return if exists return true otherwise false
     */
    boolean existsAllByGroupId(String groupId);
}
