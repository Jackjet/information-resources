package d1.project.dataintegration.task.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.dataintegration.common.Constants;
import d1.project.dataintegration.common.utils.BaseUtils;
import d1.project.dataintegration.task.dao.TaskDao;
import d1.project.dataintegration.task.dao.TaskKettleFilesDao;
import d1.project.dataintegration.task.entity.Task;
import d1.project.dataintegration.task.entity.TaskKettleFiles;
import d1.project.dataintegration.task.model.MetasReturnVm;
import d1.project.dataintegration.task.model.TaskVm;
import d1.project.dataintegration.task.model.vm.TaskInsertVm;
import d1.project.dataintegration.task.model.vm.TaskUpdateVm;
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
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
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
    private final WebAdminUserService webAdminUserService;

    @Value("${d1.project.node.run.url}")
    private String nodeRunKettle;
    @Value("${d1.project.node.stop.url}")
    private String nodeStopKettle;

    public TaskService(TaskDao taskDao, TaskKettleFilesDao taskKettleFilesDao, WebAdminUserService webAdminUserService) {
        this.taskDao = taskDao;
        this.taskKettleFilesDao = taskKettleFilesDao;
        this.webAdminUserService = webAdminUserService;
    }

    /**
     * 查询所有
     *
     * @param model 模型
     */
    public Page<TaskVm> findAllVm(JSONObject model) throws Exception {
        Page<Task> taskList = findAll(model);
        List<TaskVm> taskVmList = new ArrayList<>();
        for (Task task : taskList.getContent()) {
            taskVmList.add(copyMetasEntityToVm(task));
        }
        return new PageImpl(taskVmList, taskList.getPageable(), taskList.getTotalElements());
    }

    /**
     * 查询所有
     *
     * @param model 模型
     */
    public Page<Task> findAll(JSONObject model) throws Exception {
        String groupId = model.getString("groupId");
        if ("1".equals(groupId)) {
            model.put("groupId", "");
        }
        SpecificationBuilder<Task> builder = new SpecificationBuilder<>();
        Specification<Task> specification = builder.init(model)
                .sContain("name", "name")
                .sEqual("groupId", "groupId")
                .sContain("metaKey", "metaKey")
                .sContain("metaValue", "metaValue")
                .dOrder("createTime")
                .build();
        return taskDao.findAll(specification, builder.getPageable());
    }

    /**
     * 新增
     *
     * @param model 传输模型
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(TaskInsertVm model, HttpServletRequest request) throws DoValidException {
        Task task = new Task();
        BeanUtils.copyProperties(model, task);
        task.setId(BaseUtils.generate32Id());
        Task entity = copyMetasVmToEntity(model.getMetas());
        task.setMetaKey(entity.getMetaKey());
        task.setMetaValue(entity.getMetaValue());
        webAdminUserService.updateCreateIdAndTime(request, task);
        taskDao.save(task);
    }

    /**
     * 删除
     *
     * @param id id
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id, HttpServletRequest request) throws DoValidException {
        Task task = taskDao.findById(id).orElseThrow(() -> new DoValidException("任务不存在"));
        taskDao.deleteById(task.getId());
    }

    /**
     * 更新
     *
     * @param model 传输模型
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(TaskUpdateVm model, HttpServletRequest request) throws DoValidException {
        Task task = taskDao.findById(model.getId()).orElseThrow(() -> new DoValidException("任务不存在"));
        BeanUtils.copyProperties(model, task);
        webAdminUserService.updateCreateIdAndTime(request, task);
        Task entity = copyMetasVmToEntity(model.getMetas());
        task.setMetaKey(entity.getMetaKey());
        task.setMetaValue(entity.getMetaValue());
        taskDao.save(task);
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
    public void run(String id, HttpServletRequest request) throws DoValidException {
        Task task = taskDao.findById(id).orElseThrow(() -> new DoValidException("任务不存在"));
        operationTask(task, 0);
    }

    /**
     * 停止
     *
     * @param id id
     */
    @Transactional(rollbackFor = Exception.class)
    public void stop(String id, HttpServletRequest request) throws DoValidException {
        Task task = taskDao.findById(id).orElseThrow(() -> new DoValidException("任务不存在"));
        operationTask(task, 1);
    }

    /**
     * 刷新任务状态
     *
     * @param id id
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(String id, HttpServletRequest request) throws DoValidException {
        Task task = taskDao.findById(id).orElseThrow(() -> new DoValidException("任务不存在"));

    }

    /**
     * 运行或停止任务
     *
     * @param task
     * @return 运行结果
     * @throws Exception 数据查询异常
     */
    public JSONObject operationTask(Task task, int status) throws DoValidException {
        int mode = task.getMode();
        if (mode == 0) {
            throw new DoValidException(Constants.TASK_IS_DESIGN);
        }
        JSONObject result = new JSONObject();
        int taskStatus = task.getStatus();
        if (taskStatus != status && status == 0) {
            throw new DoValidException(Constants.TASK_IS_RUN);
        } else if (taskStatus != status && status == 1) {
            throw new DoValidException(Constants.TASK_IS_STOP);
        }
        JSONObject container = JSONObject.parseObject(task.getContainer());
        String containerPath = container.get("ip") + ":" + container.get("port");
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
                throw new DoValidException(Constants.TYPE_ERROR);
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
    private JSONObject runKettle(Task task, String runUrl) throws DoValidException {
        String taskId = task.getId();
        TaskKettleFiles taskKettleFiles = taskKettleFilesDao.findByTaskIdAndType(taskId, "kjb");
        if (taskKettleFiles == null) {
            throw new DoValidException(Constants.TASK_NOFILE);
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
                throw new DoValidException(result.getString("msg"));
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
    private JSONObject stopKettle(String taskId, String stopUrl) throws DoValidException {
        String url = stopUrl + "?id=" + taskId;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> responseEntity = restTemplate.getForEntity(url, JSONObject.class);
        JSONObject result = responseEntity.getBody();
        if (result != null) {
            String code = result.getString("code");
            String fail = "0";
            if (fail.equals(code)) {
                throw new DoValidException(result.getString("msg"));
            }
        }
        return result;
    }

    /**
     * 标签组List转字符串
     *
     * @param metasReturnVmList
     * @return
     */
    private Task copyMetasVmToEntity(List<MetasReturnVm> metasReturnVmList) {
        Task task = new Task();
        JSONArray keyArray = new JSONArray();
        JSONArray valueArray = new JSONArray();
        for (MetasReturnVm metasReturnVm : metasReturnVmList) {
            if (StringUtils.isEmpty(metasReturnVm.getMetaKey()) && StringUtils.isEmpty(metasReturnVm.getMetaValue())) {
                continue;
            }
            JSONObject metaKey = new JSONObject();
            metaKey.put("id", metasReturnVm.getId());
            metaKey.put("metaKey", metasReturnVm.getMetaKey());
            JSONObject metaValue = new JSONObject();
            metaValue.put("id", metasReturnVm.getId());
            metaValue.put("metaValue", metasReturnVm.getMetaValue());
            keyArray.add(metaKey);
            valueArray.add(metaValue);
        }
        task.setMetaKey(keyArray.toJSONString());
        task.setMetaValue(valueArray.toJSONString());
        return task;
    }

    /**
     * 标签组字符串转List
     *
     * @param task
     * @return
     */
    private TaskVm copyMetasEntityToVm(Task task) {
        TaskVm taskVm = new TaskVm();
        BeanUtils.copyProperties(task, taskVm);
        if (StringUtils.isEmpty(task.getMetaKey())) {
            return taskVm;
        }
        JSONArray keyArray = JSONArray.parseArray(task.getMetaKey());
        JSONArray valueArray = JSONArray.parseArray(task.getMetaValue());
        List<MetasReturnVm> metasReturnVmList = new ArrayList<>();
        for (int i = 0; i < keyArray.size(); i++) {
            MetasReturnVm metasReturnVm = new MetasReturnVm();
            JSONObject metaKeyObject = keyArray.getJSONObject(i);
            String keyId = metaKeyObject.getString("id");
            String metaKey = metaKeyObject.getString("metaKey");
            for (int j = 0; j < valueArray.size(); j++) {
                JSONObject metaValueObject = valueArray.getJSONObject(j);
                String valueId = metaValueObject.getString("id");
                String metaValue = metaValueObject.getString("metaValue");
                if (keyId.equals(valueId)) {
                    metasReturnVm.setId(keyId);
                    metasReturnVm.setMetaKey(metaKey);
                    metasReturnVm.setMetaValue(metaValue);
                    metasReturnVmList.add(metasReturnVm);
                }
            }
        }
        taskVm.setMetas(metasReturnVmList);
        return taskVm;
    }
}
