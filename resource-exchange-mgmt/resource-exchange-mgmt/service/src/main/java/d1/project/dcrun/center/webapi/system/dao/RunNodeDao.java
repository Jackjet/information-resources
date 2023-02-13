package d1.project.dcrun.center.webapi.system.dao;

import d1.project.dcrun.center.webapi.system.entity.RunNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author libin
 */
public interface RunNodeDao extends JpaRepository<RunNode, String>, JpaSpecificationExecutor<RunNode> {
    /**
     * 根据父节点查询子节点
     *
     * @param parentNodeId 父节点
     * @return 节点信息
     */
    List<RunNode> findAllByParentNodeIdOrderByCreateTimeDesc(String parentNodeId);

    /**
     * 根据集成项目id查询节点信息
     *
     * @param integrationId 集成项目id
     * @param path          路径
     * @return 节点信息
     */
    RunNode findFirstByIntegrationIdAndPath(String integrationId, String path);

    /**
     * 根据节点名称查询节点信息
     *
     * @param name         节点名称
     * @param parentNodeId 父节点id
     * @return
     */
    RunNode findFirstByNameAndParentNodeIdAndIntegrationId(String name, String parentNodeId,String integrationId);

    /**
     * 根据集成项目id查询根节点节点信息
     *
     * @param integrationId 集成项目id
     * @return 节点信息
     */
    List<RunNode> findAllByIntegrationIdAndParentNodeIdOrderByCreateTimeDesc(String integrationId, String parentNodeId);
}
