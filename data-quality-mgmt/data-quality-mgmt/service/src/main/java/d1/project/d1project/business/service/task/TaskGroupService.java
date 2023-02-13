package d1.project.d1project.business.service.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.project.d1project.business.dao.TaskDao;
import d1.project.d1project.business.dao.TaskGroupDao;
import d1.project.d1project.business.entity.TaskGroup;
import d1.project.d1project.business.model.task.group.TaskGroupInsertPostVm;
import d1.project.d1project.business.model.task.group.TaskGroupUpdatePutVm;
import d1.project.d1project.common.service.ITaskGroupService;
import d1.project.d1project.common.utils.BaseUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author libin
 */
@Service
public class TaskGroupService implements ITaskGroupService {
    private final TaskGroupDao taskGroupDao;
    private final TaskDao taskDao;

    public TaskGroupService(TaskGroupDao taskGroupDao, TaskDao taskDao) {
        this.taskGroupDao = taskGroupDao;
        this.taskDao = taskDao;
    }

    public JSONArray getTaskGroup (){
        List<TaskGroup> taskGroups = taskGroupDao.findAll();
        Map<String,List<TaskGroup>> maps = taskGroups.stream().collect(Collectors.groupingBy(TaskGroup::getParentId));
        return generateForest(maps);
    }

    public void insert(TaskGroupInsertPostVm params) throws DoValidException {
        TaskGroup taskGroup = new TaskGroup();
        BeanUtils.copyProperties(params,taskGroup);

        if(taskGroupDao.existsByName(taskGroup.getName())){
            throw new DoValidException("已存在同名组");
        }

        taskGroup.setId(BaseUtils.generate32Id());

        if(StringUtils.isEmpty(taskGroup.getParentId())){
            taskGroup.setParentId("0");
        }
        taskGroupDao.save(taskGroup);
    }

    public void update(TaskGroupUpdatePutVm params) throws DoValidException {
        TaskGroup taskGroup = new TaskGroup();
        BeanUtils.copyProperties(params,taskGroup);

        if(taskGroupDao.existsByNameAndIdNot(taskGroup.getName(),taskGroup.getId())){
            throw new DoValidException("已存在同名组");
        }

        TaskGroup data = taskGroupDao.findById(taskGroup.getId()).orElseThrow(() -> new DoValidException("组信息不存在"));
        data.setName(taskGroup.getName());

        taskGroupDao.save(data);
    }

    public void delete(String id) throws DoValidException {
        if(taskGroupDao.existsByParentId(id)){
            throw new DoValidException("节点下存在子节点，请先删除子节点");
        }

        if(taskDao.existsByGroupId(id)) {
            throw new DoValidException("节点下存在任务，请将任务删除或转移至其它分组");
        }
        taskGroupDao.deleteById(id);
    }

    //================================================================
    private JSONArray generateForest(Map<String,List<TaskGroup>> maps){
        JSONArray forest = generateRoots(maps);
        return generateChildren(maps,forest);
    }

    private JSONArray generateRoots(Map<String,List<TaskGroup>> maps){
        List<TaskGroup> roots = maps.get("0");
        if(roots == null) {
            return new JSONArray();
        }
        return  (JSONArray) JSON.toJSON(roots);
    }

    private JSONArray generateChildren(Map<String,List<TaskGroup>> maps,JSONArray forest){
        for(Object obj : forest){
            JSONObject node = (JSONObject) obj;
            List<TaskGroup> children = maps.get(node.get("id"));
            if(children != null){
                node.put("children", generateChildren(maps,(JSONArray)JSON.toJSON(children)));
            } else {
                node.put("children",new JSONArray());
            }
        }

        return forest;
    }

    @Override
    public List<TaskGroup> findAll() {
        return taskGroupDao.findAll();
    }
}
