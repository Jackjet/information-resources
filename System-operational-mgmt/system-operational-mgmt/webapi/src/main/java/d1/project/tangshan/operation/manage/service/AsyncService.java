package d1.project.tangshan.operation.manage.service;
import d1.project.tangshan.operation.manage.entity.AutomatedDeploymentEntity;
import d1.project.tangshan.operation.manage.entity.operations.module.Node;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


public interface AsyncService {


//    void pushDeploy(HttpServletRequest request, AutomatedDeploymentEntity automatedDeploymentEntity, Node node, boolean rollbackBoolean) throws Exception;

    void pushHttp(AutomatedDeploymentEntity automatedDeploymentEntity, Map<String, Object> parameters) throws Exception;

}