package d1.project.dataintegration.task.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.dataintegration.common.Constants;
import d1.project.dataintegration.common.utils.BaseUtils;
import d1.project.dataintegration.task.dao.TaskDao;
import d1.project.dataintegration.task.dao.TaskGroupDao;
import d1.project.dataintegration.task.entity.TaskGroup;
import d1.project.dataintegration.task.model.vm.TaskGroupInsertVm;
import d1.project.dataintegration.task.model.vm.TaskGroupTree;
import d1.project.dataintegration.task.model.vm.TaskGroupUpdateVm;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhengyang
 */
@Service
public class TaskGroupService {

    private final WebAdminUserService webAdminUserService;
    private final TaskGroupDao taskGroupDao;
    private final TaskDao taskDao;

    public TaskGroupService(TaskGroupDao taskGroupDao, TaskDao taskDao, WebAdminUserService webAdminUserService) {
        this.taskGroupDao = taskGroupDao;
        this.taskDao = taskDao;
        this.webAdminUserService = webAdminUserService;
    }

    public List<TaskGroupTree> findTreeList(String name) throws Exception {
        JSONObject json = new JSONObject();
        if (!StringUtils.isEmpty(name)) {
            json.put("name", name);
        }
        List<TaskGroup> list = findAll(json);
        List<TaskGroupTree> returnList = new ArrayList<>();
        returnList = getTree("", list, 0);
        return returnList;
    }

    /**
     * 新增
     *
     * @param model 传输模型
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(TaskGroupInsertVm model, HttpServletRequest request) throws DoValidException {
        TaskGroup taskGroup = new TaskGroup();
        BeanUtils.copyProperties(model, taskGroup);
        if (StringUtils.isEmpty(model.getParentId())) {
            taskGroup.setParentId("1");
        }
        if (taskGroupDao.existsAllByNameAndParentId(model.getName(), model.getParentId())) {
            throw new DoValidException(Constants.DATA_NAME_REPEAT);
        }
        taskGroup.setId(BaseUtils.generate32Id());
        webAdminUserService.updateCreateIdAndTime(request, taskGroup);
        taskGroupDao.save(taskGroup);
    }

    /**
     * 删除
     *
     * @param id id
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) throws DoValidException {
        TaskGroup taskGroup = taskGroupDao.findById(id).orElseThrow(() -> new DoValidException("任务组不存在"));
        //查询分组下面是否有分组
        if (taskGroupDao.existsAllByParentId(id)) {
            throw new DoValidException(Constants.DATA_NEXT_HAS);
        }
        //查询分组下面是否有任务
        if (taskDao.existsAllByGroupId(id)) {
            throw new DoValidException(Constants.TASK_TYPE_HAS);
        }
        taskGroupDao.deleteById(taskGroup.getId());
    }

    /**
     * 更新
     *
     * @param model 传输模型
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(TaskGroupUpdateVm model, HttpServletRequest request) throws DoValidException {
        TaskGroup taskGroup = taskGroupDao.findById(model.getId()).orElseThrow(() -> new DoValidException("任务不存在"));
        if (taskGroupDao.existsAllByNameAndParentIdAndIdNot(model.getName(), taskGroup.getParentId(), model.getId())) {
            throw new DoValidException(Constants.DATA_NAME_REPEAT);
        }
        BeanUtils.copyProperties(model, taskGroup);
        webAdminUserService.updateUpdateIdAndTime(request, taskGroup);
        taskGroupDao.save(taskGroup);
    }

    /**
     * 查询所有
     *
     * @param model 模型
     */
    private List<TaskGroup> findAll(JSONObject model) throws Exception {
        SpecificationBuilder<TaskGroup> builder = new SpecificationBuilder<>();
        Specification<TaskGroup> specification = builder.init(model)
                .sContain("name", "name")
                .dOrder("createTime")
                .build();
        return taskGroupDao.findAll(specification);
    }

    private List<TaskGroupTree> getTree(String startPoint, List<TaskGroup> list, int level) {
        List<TaskGroupTree> treeList = new ArrayList<>();
        for (TaskGroup taskGroup : list) {
            String parentId = taskGroup.getParentId();
            String name = taskGroup.getName();
            String id = taskGroup.getId();
            if ("1".equals(parentId) && level == 0 || startPoint.equals(parentId)) {
                TaskGroupTree taskGroupTree = new TaskGroupTree();
                taskGroupTree.setName(name);
                taskGroupTree.setLevel(level);
                taskGroupTree.setId(id);
                taskGroupTree.setChild(getTree(id, list, level + 1));
                treeList.add(taskGroupTree);
            }
        }
        return treeList;
    }
}
