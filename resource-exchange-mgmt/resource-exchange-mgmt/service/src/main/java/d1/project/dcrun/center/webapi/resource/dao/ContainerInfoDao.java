package d1.project.dcrun.center.webapi.resource.dao;

import d1.project.dcrun.center.webapi.resource.entity.ContainerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author baozh
 */
public interface ContainerInfoDao extends JpaRepository<ContainerInfo, String>, JpaSpecificationExecutor<ContainerInfo> {

    /**
     * 查询当前名称是否存在
     *
     * @param name 名称
     * @return 查询结果
     */
    boolean existsAllByName(String name);

    /**
     * 查询当前名称是否存在
     *
     * @param name 名称
     * @param id   数据id
     * @return 查询结果
     */
    boolean existsAllByNameAndIdNot(String name, String id);

    /**
     * 查询当前地址是否存在
     *
     * @param ip   地址
     * @param port 端口号
     * @return 查询结果
     */
    boolean existsAllByIpAndPort(String ip, String port);

    /**
     * 查询当前地址是否存在
     *
     * @param ip   地址
     * @param port 端口号
     * @param id   数据id
     * @return 查询结果
     */
    boolean existsAllByIpAndPortAndIdNot(String ip, String port, String id);

    /**
     * exists by group id
     *
     * @param groupId group id
     * @return if exists return true otherwise false
     */
    boolean existsAllByGroupId(String groupId);
}
