package d1.project.tangshan.operation.manage.service.operations.log;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.tangshan.operation.manage.dao.operations.log.SystemLogDao;
import d1.project.tangshan.operation.manage.entity.operations.log.OperationLog;
import d1.project.tangshan.operation.manage.entity.operations.log.SystemLog;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author lin
 */
@Service
public class SystemLogService {
    private final SystemLogDao systemLogDao;

    public SystemLogService(SystemLogDao systemLogDao) {
        this.systemLogDao = systemLogDao;
    }

    public Page<SystemLog> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<SystemLog> builder = new SpecificationBuilder<>();
        Specification<SystemLog> specification = builder.init(params)
                .sContain("source", "source")
                .tThanEqual("createTime", "startTime", "yyyy-MM-dd HH:mm:ss")
                .tLessEqual("createTime", "endTime", "yyyy-MM-dd HH:mm:ss")
                .dOrder("createTime")
                .build();
        return systemLogDao.findAll(specification, builder.getPageable());
    }

    public void insert(SystemLog systemLog) {
        systemLogDao.save(systemLog);
    }
}
