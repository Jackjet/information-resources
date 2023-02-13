package d1.project.dcrun.center.webapi.task.service;

import com.alibaba.fastjson.JSONObject;
import d1.project.dcrun.center.webapi.task.dao.TaskDao;
import d1.project.dcrun.center.webapi.task.dao.TaskLogsDao;
import d1.project.dcrun.center.webapi.task.model.TaskInfoForContainerVm;
import d1.project.dcrun.center.webapi.task.model.TaskListForContainerVm;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

/**
 * @author zhengyang
 */
@Service
public class TaskContainerService {
    private final TaskDao taskDao;
    private final TaskLogsDao taskLogsDao;

    public TaskContainerService(TaskDao taskDao, TaskLogsDao taskLogsDao) {
        this.taskDao = taskDao;
        this.taskLogsDao = taskLogsDao;
    }

    /**
     * 查询容器所有任务
     *
     * @param model 模型
     */
    public Page<TaskListForContainerVm> findAllVm(JSONObject model) throws Exception {
        String name = model.getString("name");
        String ip = model.getString("ip");
        String port = model.getString("port");
        int page = model.getIntValue("page")-1;
        int size = model.getIntValue("size");
        if(name == null ){
            name= "";
        }
        Pageable pageable = PageRequest.of(page,size);
        return taskDao.findTaskListForContainer(name, ip, port, pageable);
    }

    /**
     * 查询容器统计信息
     *
     * @param model 模型
     */
    public TaskInfoForContainerVm findInfo(JSONObject model) throws Exception {
        String ip = model.getString("ip");
        String port = model.getString("port");
        TaskInfoForContainerVm taskInfoForContainerVm = taskDao.findTaskInfoForContainer(ip, port);
        return taskInfoForContainerVm;
    }
}
