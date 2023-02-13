package d1.project.d1project.business.service.task;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.d1project.business.dao.TaskLogDao;
import d1.project.d1project.business.entity.TaskLog;
import d1.project.d1project.business.model.task.log.TaskLogInsertPostVm;
import d1.project.d1project.common.utils.BaseUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TaskLogService {
    private final TaskLogDao taskLogDao;

    public TaskLogService(TaskLogDao taskLogDao) {
        this.taskLogDao = taskLogDao;
    }

    public Page<TaskLog> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<TaskLog> builder = new SpecificationBuilder<>();
        Specification<TaskLog> specification = builder.init(params)
                .sEqual("taskId","taskId")
                .dOrder("executionTime")
                .build();

        return taskLogDao.findAll(specification, builder.getPageable());
    }

    public void insert(TaskLogInsertPostVm params) {
        TaskLog taskLog = new TaskLog();
        BeanUtils.copyProperties(params,taskLog);

        taskLog.setId(BaseUtils.generate32Id());

        taskLogDao.save(taskLog);
    }
}
