package d1.project.tangshan.operation.manage.service;

import d1.project.tangshan.operation.manage.entity.AutomatedDeploymentEntity;
import d1.project.tangshan.operation.manage.entity.operations.module.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@Service
public class AsyncServiceImpl implements AsyncService {
    final
    AutomatedDeploymentService automatedDeploymentService;

    public AsyncServiceImpl(AutomatedDeploymentService automatedDeploymentService) {
        this.automatedDeploymentService = automatedDeploymentService;
    }

    @Override
    @Async("asyncServiceExecutor")
    public void pushHttp(AutomatedDeploymentEntity automatedDeploymentEntity, Map<String, Object> parameters) throws Exception {
        automatedDeploymentService.pushHttp(automatedDeploymentEntity, parameters);
    }
}
