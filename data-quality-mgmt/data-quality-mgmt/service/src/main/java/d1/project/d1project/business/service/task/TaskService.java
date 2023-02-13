package d1.project.d1project.business.service.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenManager;
import d1.project.d1project.business.cron.CronTaskRegistrar;
import d1.project.d1project.business.cron.SchedulingRunnable;
import d1.project.d1project.business.dao.TaskDao;
import d1.project.d1project.business.dao.TaskRuleDao;
import d1.project.d1project.business.dao.VerifyRuleDao;
import d1.project.d1project.business.entity.Task;
import d1.project.d1project.business.entity.TaskRule;
import d1.project.d1project.business.entity.VerifyRule;
import d1.project.d1project.business.model.task.TaskInsertPostVm;
import d1.project.d1project.business.model.task.TaskUpdatePutVm;
import d1.project.d1project.business.model.template.MetadataDataModel;
import d1.project.d1project.business.service.datasource.DataSourceService;
import d1.project.d1project.business.utils.Constants;
import d1.project.d1project.business.workers.RunTaskChannel;
import d1.project.d1project.business.workers.bean.RunRule;
import d1.project.d1project.business.workers.bean.RunTask;
import d1.project.d1project.common.utils.BaseUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskDao taskDao;
    private final TaskRuleDao taskRuleDao;
    private final VerifyRuleDao verifyRuleDao;
    private final CronTaskRegistrar cronTaskRegistrar;
    private final DataSourceService dataSourceService;
    private final RunTaskChannel runTaskChannel;

    public TaskService(TaskDao taskDao, TaskRuleDao taskRuleDao, VerifyRuleDao verifyRuleDao, CronTaskRegistrar cronTaskRegistrar, DataSourceService dataSourceService, RunTaskChannel runTaskChannel) {
        this.taskDao = taskDao;
        this.taskRuleDao = taskRuleDao;
        this.verifyRuleDao = verifyRuleDao;
        this.cronTaskRegistrar = cronTaskRegistrar;
        this.dataSourceService = dataSourceService;
        this.runTaskChannel = runTaskChannel;
    }

    public JSONObject findAll(JSONObject params) throws Exception {
        SpecificationBuilder<Task> builder = new SpecificationBuilder<>();
        Specification<Task> specification = builder.init(params)
                .sContain("name", "name")
                .sEqual("status", "status")
                .sEqual("groupId", "groupId")
                .dOrder("createTime")
                .build();

        Page<Task> data = taskDao.findAll(specification, builder.getPageable());

        JSONObject result = (JSONObject) JSON.toJSON(data);

        JSONArray content = result.getJSONArray("content");
        for (Object obj : content) {
            JSONObject task = (JSONObject) obj;
            int count = taskRuleDao.countAllByTaskId(task.getString("id"));
            task.put("count", count);
            task.put("lastTime",task.getDate("lastTime"));
        }

        return result;
    }

    @Transactional(rollbackOn = Exception.class)
    public void insert(HttpServletRequest request, TaskInsertPostVm params) throws DoValidException {
        if (taskDao.existsByName(params.getName())) {
            throw new DoValidException("任务名称已存在");
        }

        Task task = new Task();
        BeanUtils.copyProperties(params, task);

        task.setId(BaseUtils.generate32Id());
        task.setStatus(Constants.TASK_STATUS_ENABLE);
        TokenManager.getInstance().updateCreateIdAndTime(request, task);

        //生成时间表达式
        task.setCronStr(generateCronStr(task.getCycle(), task.getExecutionTime(), task.getExecutionWeek()));
        taskDao.save(task);

        //添加任务与规则间的关系映射
        insertTaskRule(task.getId(), params.getRuleIds());

        this.addTask(task);
    }

    @Transactional(rollbackOn = Exception.class)
    public void update(TaskUpdatePutVm params) throws DoValidException {
        Task data = taskDao.findById(params.getId()).orElseThrow(() -> new DoValidException("任务信息不存在"));

        if(Constants.TASK_STATUS_ENABLE.equals(data.getStatus())) {
            throw new DoValidException("任务已启用，请先停止任务");
        }

        if (taskDao.existsByNameAndIdNot(params.getName(), params.getId())) {
            throw new DoValidException("任务名称已存在");
        }

        BeanUtils.copyProperties(params,data);

        //生成时间表达式
        data.setCronStr(generateCronStr(data.getCycle(), data.getExecutionTime(), data.getExecutionWeek()));
        taskDao.save(data);

        //删除所有的映射关系
        taskRuleDao.deleteAllByTaskId(data.getId());
        //添加任务与规则间的关系映射
        insertTaskRule(data.getId(), params.getRuleIds());

        if(Constants.TASK_STATUS_ENABLE.equals(data.getStatus())){
            this.addTask(data);
        }
    }

    public void taskEnableOrForbidden(String id) throws DoValidException {
        Task data = taskDao.findById(id).orElseThrow(() -> new DoValidException("任务信息不存在"));

        if (Constants.TASK_STATUS_ENABLE.equals(data.getStatus())) {
            data.setStatus(Constants.TASK_STATUS_UNENABLE);
            this.removeTask(data.getId());
        } else {
            data.setStatus(Constants.TASK_STATUS_ENABLE);
            this.addTask(data);
        }

        taskDao.save(data);
    }

    public void executeTask(String id) throws Exception {
        Task task = taskDao.findById(id).orElseThrow(() -> new DoValidException("任务信息不存在"));
        List<TaskRule> taskRules = taskRuleDao.findAllByTaskId(task.getId());

        List<String> ruleIds = taskRules.stream().map(TaskRule::getRuleId).collect(Collectors.toList());
        //TODO 效率低
        List<VerifyRule> verifyRules = verifyRuleDao.findAllByIdIn(ruleIds);

        RunTask runTask = new RunTask();
        BeanUtils.copyProperties(task, runTask);
        joinRunTask(verifyRules, runTask);
    }


    /**
     * 把任务添加到运行任务线程中
     *
     * @param verifyRules 检查规则列表
     * @param runTask     运行的任务
     * @throws Exception 异常
     */
    public void joinRunTask(List<VerifyRule> verifyRules, RunTask runTask) throws Exception {
        List<RunRule> runRules = new ArrayList<>();
        for (VerifyRule rule : verifyRules) {
            RunRule runRule = new RunRule();
            runRule.setId(rule.getId());
            runRule.setName(rule.getName());
            runRule.setSql(rule.getSql());
            runRule.setTemplateId(rule.getRuleTemplateId());

            MetadataDataModel metadataDataModel = JSONObject.parseObject(rule.getMetadataData(), MetadataDataModel.class);
            runRule.setDbType(metadataDataModel.getType());
            runRule.setTableName(metadataDataModel.getSource().getName());

            //查询数据库获取数据源
            runRule.setDataSource(dataSourceService.findById(metadataDataModel.getSourceId()));
            runRules.add(runRule);
        }
        runTask.setRunRules(runRules);

        //添加任务到线程池去执行
        runTaskChannel.addTask(runTask);
    }

    public List<Task> findAll() {
        return taskDao.findAll();
    }

    public void save(Task task) {
        taskDao.save(task);
    }

    public JSONObject findById(String id) throws DoValidException {
        Task task = taskDao.findById(id).orElseThrow(() -> new DoValidException("任务信息不存在"));

        JSONObject result = (JSONObject) JSON.toJSON(task);
        JSONArray verifyRules = new JSONArray();

        //查询任务规则映射关系
        List<TaskRule> taskRules = taskRuleDao.findAllByTaskId(task.getId());
        if (taskRules.size() > 0) {
            List<String> ruleIds = taskRules.stream().map(TaskRule::getRuleId).collect(Collectors.toList());
            List<VerifyRule> verifyRuleList = verifyRuleDao.findAllByIdIn(ruleIds);

            verifyRules = (JSONArray) JSON.toJSON(verifyRuleList);
        }
        result.put("verifyRules", verifyRules);

        return result;
    }

    @Transactional(rollbackOn = Exception.class)
    public void deleteById(String id) throws DoValidException {
        Task task = taskDao.findById(id).orElseThrow(() -> new DoValidException("任务信息不存在"));

        if (Constants.TASK_STATUS_ENABLE.equals(task.getStatus())) {
            throw new DoValidException("任务已启动，请先停止任务");
        }

        taskRuleDao.deleteAllByTaskId(task.getId());
        taskDao.delete(task);
    }

    //===================================================================================
    private String generateCronStr(String cycle, String executionTime, int executionWeek) throws DoValidException {
        String cronStr = "";
        switch (cycle) {
            case Constants.TASK_CYCLE_ONCE:
                cronStr = generateOnce(executionTime);
                break;
            case Constants.TASK_CYCLE_DAILY:
                cronStr = generateDaily(executionTime);
                break;
            case Constants.TASK_CYCLE_WEEKLY:
                cronStr = generateWeekly(executionTime, executionWeek);
                break;
            case Constants.TASK_CYCLE_MONTHLY:
                cronStr = generateMonthly(executionTime);
                break;
            default:
                throw new DoValidException("执行周期不正确");
        }
        return cronStr;
    }


    /**
     * 执行一次
     */
    private String generateOnce(String executionTime) throws DoValidException {
        String cronFormat = "%d %d %d %d %d ?";
        String[] dayAnTime = executionTime.split(" ");
        String[] days = dayAnTime[0].split("-");
        String[] times = dayAnTime[1].split(":");

        return String.format(cronFormat, Integer.parseInt(times[2]), Integer.parseInt(times[1]), Integer.parseInt(times[0]), Integer.parseInt(days[2]), Integer.parseInt(days[1]));
    }

    /**
     * 每天一次
     */
    private String generateDaily(String executionTime) throws DoValidException {
        if (StringUtils.isEmpty(executionTime)) {
            throw new DoValidException("执行时间不能为空");
        }

        String cronFormat = "%d %d %d * * ?";
        String[] times = executionTime.split(":");

        return String.format(cronFormat, Integer.parseInt(times[2]), Integer.parseInt(times[1]), Integer.parseInt(times[0]));
    }

    /**
     * 每月一次
     */
    private String generateMonthly(String executionTime) throws DoValidException {
        String cronFormat = "%d %d %d %d * ?";
        String[] dayAndTime = executionTime.split(" ");

        String[] times = dayAndTime[1].split(":");
        return String.format(cronFormat, Integer.parseInt(times[2]), Integer.parseInt(times[1]), Integer.parseInt(times[0]), Integer.parseInt(dayAndTime[0]));
    }

    /**
     * 每周一次
     */
    private String generateWeekly(String executionTime, int week) throws DoValidException {
        if (StringUtils.isEmpty(executionTime)) {
            throw new DoValidException("执行时间不能为空");
        }
        if (StringUtils.isEmpty(week)) {
            throw new DoValidException("周不能为空");
        }

        String cronFormat = "%d %d %d ? * %d";
        String[] times = executionTime.split(":");
        return String.format(cronFormat, Integer.parseInt(times[2]), Integer.parseInt(times[1]), Integer.parseInt(times[0]), week);
    }

    /**
     * 添加任务与质量检查规则的映射关系
     */
    private void insertTaskRule(String taskId, List<String> ruleIds) throws DoValidException {
        //添加任务与规则间的关系映射
        List<TaskRule> taskRules = new Vector<>();
        for (String ruleId : ruleIds) {
            if (!verifyRuleDao.existsById(ruleId)) {
                throw new DoValidException("质量检测规则不存在");
            }

            TaskRule taskRule = new TaskRule();
            taskRule.setId(BaseUtils.generate32Id());
            taskRule.setTaskId(taskId);
            taskRule.setRuleId(ruleId);

            taskRules.add(taskRule);
        }

        taskRuleDao.saveAll(taskRules);
    }

    private void addTask(Task task) {
        List<TaskRule> taskRules = taskRuleDao.findAllByTaskId(task.getId());
        List<String> ruleIds = taskRules.stream().map(TaskRule::getRuleId).collect(Collectors.toList());
        List<VerifyRule> verifyRules = verifyRuleDao.findAllByIdIn(ruleIds);

        SchedulingRunnable schedulingRunnable = new SchedulingRunnable(this, task, verifyRules);
        cronTaskRegistrar.addCronTask(task.getId(), schedulingRunnable, task.getCronStr());
    }

    private void removeTask(String id){
        cronTaskRegistrar.removeCronTask(id);
    }
}
