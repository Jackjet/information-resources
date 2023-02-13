package d1.project.d1project.business.service.task;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.d1project.business.dao.TaskRuleLogDao;
import d1.project.d1project.business.dao.TaskRuleLogViewDao;
import d1.project.d1project.business.view.TaskRuleLogView;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TaskRuleLogService {
    private final TaskRuleLogDao taskRuleLogDao;
    private final TaskRuleLogViewDao taskRuleLogViewDao;

    public TaskRuleLogService(TaskRuleLogDao taskRuleLogDao, TaskRuleLogViewDao taskRuleLogViewDao) {
        this.taskRuleLogDao = taskRuleLogDao;
        this.taskRuleLogViewDao = taskRuleLogViewDao;
    }

    public Page<TaskRuleLogView> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<TaskRuleLogView> builder = new SpecificationBuilder<>();
        Specification<TaskRuleLogView> specification = builder.init(params)
                .sEqual("taskLogId", "taskLogId")
                .aOrder("id").build();

        return taskRuleLogViewDao.findAll(specification, builder.getPageable());
    }

    public TaskRuleLogView findById(String id) throws DoValidException {
        return taskRuleLogViewDao.findById(id).orElseThrow(() -> new DoValidException("问题信息不存在"));
    }


    public Object statisticsCountGroupByRuleTemplateId(String createDay) {
        return taskRuleLogDao.statisticsCountGroupByRuleTemplateId(createDay);
    }


}
