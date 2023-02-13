package d1.project.tangshan.operation.manage.dao.operations.module;

import d1.project.tangshan.operation.manage.entity.operations.module.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author lin
 */
public interface NodeDao extends JpaRepository<Node, String>, JpaSpecificationExecutor<Node> {
    Boolean existsByName(String name);

    Node findByPublicIp(String publicIp);

    Node findByIntranetIp(String intranetIp);

    Boolean existsByPublicIp(String publicIp);

    Boolean existsByIntranetIp(String intranetIp);

}
