package d1.project.dcrun.center.webapi.task.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.dcrun.center.webapi.common.Constants;
import d1.project.dcrun.center.webapi.common.model.OperationLog;
import d1.project.dcrun.center.webapi.common.service.IOperationLogService;
import d1.project.dcrun.center.webapi.common.utils.BaseUtils;
import d1.project.dcrun.center.webapi.resource.dao.ContainerInfoDao;
import d1.project.dcrun.center.webapi.resource.entity.ContainerInfo;
import d1.project.dcrun.center.webapi.task.dao.TaskDao;
import d1.project.dcrun.center.webapi.task.dao.TaskKettleFilesDao;
import d1.project.dcrun.center.webapi.task.entity.Task;
import d1.project.dcrun.center.webapi.task.entity.TaskKettleFiles;
import d1.project.dcrun.center.webapi.task.model.TaskVm;
import d1.project.dcrun.center.webapi.task.model.vm.TaskInsertVm;
import d1.project.dcrun.center.webapi.task.model.vm.TaskUpdateVm;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * @author zhengyang
 */
@Service
public class TaskService {
    private final TaskDao taskDao;
    private final TaskKettleFilesDao taskKettleFilesDao;
    private final TokenService tokenService;
    private final IOperationLogService iOperationLogService;
    private final ContainerInfoDao containerInfoDao;
    @Value("${d1.project.root.utl}")
    private String rootUrl;
    @Value("${d1.project.node.run.url}")
    private String nodeRunKettle;
    @Value("${d1.project.node.stop.url}")
    private String nodeStopKettle;

    public TaskService(TaskDao taskDao, TaskKettleFilesDao taskKettleFilesDao, TokenService tokenService, IOperationLogService iOperationLogService, ContainerInfoDao containerInfoDao) {
        this.taskDao = taskDao;
        this.taskKettleFilesDao = taskKettleFilesDao;
        this.tokenService = tokenService;
        this.iOperationLogService = iOperationLogService;
        this.containerInfoDao = containerInfoDao;
    }

    /**
     * 查询所有
     *
     * @param model 模型
     */
    public Page<TaskVm> findAll(JSONObject model) throws Exception {
        String groupId = model.getString("groupId");
        if ("1".equals(groupId)) {
            model.put("groupId", "");
        }
        SpecificationBuilder<Task> builder = new SpecificationBuilder<>();
        Specification<Task> specification = builder.init(model)
                .sContain("name", "name")
                .sEqual("groupId", "groupId")
                .dOrder("createTime")
                .build();

        Page<Task> taskPage = taskDao.findAll(specification, builder.getPageable());
        List<TaskVm> taskVmList = new ArrayList<>();
        List<ContainerInfo> containerInfoList = containerInfoDao.findAll();
        for (Task task : taskPage.getContent()) {
            TaskVm taskVm = new TaskVm();
            BeanUtils.copyProperties(task, taskVm);
            ContainerInfo containerInfo = containerInfoList.stream().filter(o -> o.getId().equals(task.getContainer())).findAny().orElse(null);
            if(containerInfo != null) {
                taskVm.setContainerName(containerInfo.getName());
            }
            taskVmList.add(taskVm);
        }
        return new PageImpl(taskVmList, taskPage.getPageable(), taskPage.getTotalElements());
    }

    /**
     * 新增
     *
     * @param model 传输模型
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(TaskInsertVm model, HttpServletRequest request) throws Exception {
        if(taskDao.existsAllByNameAndGroupId(model.getName(),model.getGroupId())){
            throw new Exception(Constants.TASK_REPEAT);
        }
        Task task = new Task();
        BeanUtils.copyProperties(model, task);
        task.setId(BaseUtils.generate32Id());
        tokenService.updateCreateIdAndTime(request, task);
        tokenService.updateUpdateIdAndTime(request, task);
        taskDao.save(task);
        iOperationLogService.insert(new OperationLog("任务", "添加任务", "添加任务", "添加任务:" + task.getName(), 1), request);
    }

    /**
     * 删除
     *
     * @param id id
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id, HttpServletRequest request) throws Exception {
        Task task = taskDao.findById(id).orElseThrow(() -> new Exception("任务不存在"));
        if(task.getStatus() == 1){
            throw new Exception(Constants.TASK_STATUS_ERR);
        }
        taskDao.deleteById(task.getId());
        iOperationLogService.insert(new OperationLog("任务", "删除任务", "删除任务", "删除任务:" + task.getName(), 1), request);
    }

    /**
     * 更新
     *
     * @param model 传输模型
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(TaskUpdateVm model, HttpServletRequest request) throws Exception {
        Task task = taskDao.findById(model.getId()).orElseThrow(() -> new Exception("任务不存在"));
        if(taskDao.existsAllByNameAndGroupIdAndIdNot(model.getName(),model.getGroupId(), model.getId())){
            throw new Exception(Constants.TASK_REPEAT);
        }
        if(task.getStatus() == 1){
            throw new Exception(Constants.TASK_STATUS_ERR);
        }
        BeanUtils.copyProperties(model, task);
        tokenService.updateUpdateIdAndTime(request, task);
        taskDao.save(task);
        iOperationLogService.insert(new OperationLog("任务", "更新任务", "更新任务", "更新任务:" + task.getName(), 1), request);
    }

    /**
     * 详情
     *
     * @param id id
     */
    public Task findById(String id) {
        Optional<Task> taskOptional = taskDao.findById(id);
        if (taskOptional.isPresent()) {
            return taskOptional.get();
        }
        return null;
    }

    /**
     * 运行
     *
     * @param id id
     */
    @Transactional(rollbackFor = Exception.class)
    public void run(String id, HttpServletRequest request) throws Exception {
        Task task = taskDao.findById(id).orElseThrow(() -> new Exception("任务不存在"));
        operationTask(task, 0);
        iOperationLogService.insert(new OperationLog("任务", "运行任务", "运行任务", "运行任务:" + task.getName(), 1), request);
    }

    /**
     * 停止
     *
     * @param id id
     */
    @Transactional(rollbackFor = Exception.class)
    public void stop(String id, HttpServletRequest request) throws Exception {
        Task task = taskDao.findById(id).orElseThrow(() -> new Exception("任务不存在"));
        operationTask(task, 1);
        iOperationLogService.insert(new OperationLog("任务", "停止任务", "停止任务", "停止任务:" + task.getName(), 1), request);
    }

    /**
     * 运行或停止任务
     *
     * @param task
     * @return 运行结果
     * @throws Exception 数据查询异常
     */
    public JSONObject operationTask(Task task, int status) throws Exception {
        int mode = task.getMode();
        if (mode == 0) {
            throw new Exception(Constants.TASK_IS_DESIGN);
        }
        JSONObject result = new JSONObject();
        int taskStatus = task.getStatus();
        if (taskStatus != status && status == 0) {
            throw new Exception(Constants.TASK_IS_RUN);
        } else if (taskStatus != status && status == 1) {
            throw new Exception(Constants.TASK_IS_STOP);
        }
        Optional<ContainerInfo> containerInfoOptional = containerInfoDao.findById(task.getContainer());
        if(!containerInfoOptional.isPresent()){
            throw new Exception(Constants.CONTAINER_IS_NULL);
        }
        ContainerInfo containerInfo = containerInfoOptional.get();
        String containerPath = containerInfo.getIp() + ":" + containerInfo.getPort();
        String runUrl = "http://" + containerPath + "/" + nodeRunKettle;
        String stopUrl = "http://" + containerPath + "/" + nodeStopKettle;
        switch (status) {
            /*启动任务*/
            case 0:
                result = runKettle(task, runUrl);
                task.setStatus(1);
                break;
            /*停止任务*/
            case 1:
                result = stopKettle(task.getId(), stopUrl);
                task.setStatus(0);
                break;
            default:
                throw new Exception(Constants.TYPE_ERROR);
        }
        task.setUpdateTime(Calendar.getInstance());
        taskDao.save(task);
        return result;
    }

    /**
     * 运行kettle文件
     *
     * @param task 数据集成信息
     * @return 运行结果
     */
    private JSONObject runKettle(Task task, String runUrl) throws Exception {
        String taskId = task.getId();
        TaskKettleFiles taskKettleFiles = taskKettleFilesDao.findByTaskIdAndType(taskId, "kjb");
        if (taskKettleFiles == null) {
            throw new Exception(Constants.DATA_ERROR);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> postData = new LinkedMultiValueMap<>();
        postData.add("id", taskId);
        postData.add("fileName", taskKettleFiles.getName());
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(postData, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(runUrl, HttpMethod.POST, httpEntity, JSONObject.class);
        JSONObject result = responseEntity.getBody();
        if (result != null) {
            String code = result.getString("code");
            String fail = "0";
            if (fail.equals(code)) {
                throw new Exception(result.getString("msg"));
            }
        }
        return result;
    }

    /**
     * 停止kettle运行
     *
     * @param taskId 数据集成信息ID
     * @return 运行结果
     */
    private JSONObject stopKettle(String taskId, String stopUrl) throws Exception {
        String url = stopUrl + "?id=" + taskId;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> responseEntity = restTemplate.getForEntity(url, JSONObject.class);
        JSONObject result = responseEntity.getBody();
        if (result != null) {
            String code = result.getString("code");
            String fail = "0";
            if (fail.equals(code)) {
                throw new Exception(result.getString("msg"));
            }
        }
        return result;
    }
}
