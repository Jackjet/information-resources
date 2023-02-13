package d1.project.dataintegration.task.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.dataintegration.common.annotation.ApiAuth;
import d1.project.dataintegration.task.model.TaskVm;
import d1.project.dataintegration.task.model.vm.TaskInsertVm;
import d1.project.dataintegration.task.model.vm.TaskUpdateVm;
import d1.project.dataintegration.task.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author zhengyang
 */
//@Auth("webadmin")
@ApiAuth("webadmin")
@RestController
@RequestMapping("/webadmin/task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody TaskInsertVm model, HttpServletRequest request) throws DoValidException {
        taskService.insert(model, request);
        return ResultUtil.success("新增成功");
    }

    @DeleteMapping(value = "/deleteById")
    public Result<String> deleteById(String id, HttpServletRequest request) throws DoValidException {
        taskService.deleteById(id, request);
        return ResultUtil.success("删除成功");
    }

    @PutMapping(value = "/update")
    public Result<String> update(@Valid @RequestBody TaskUpdateVm model, HttpServletRequest request) throws DoValidException {
        taskService.update(model, request);
        return ResultUtil.success("");
    }

    @GetMapping(value = "/findAll")
    public Result<Page<TaskVm>> findAll(TaskFindAllVm model) throws Exception {
        return ResultUtil.success("", taskService.findAllVm((JSONObject) JSON.toJSON(model)));
    }

    @GetMapping(value = "/run")
    public Result<String> run(String id, HttpServletRequest request) throws DoValidException {
        taskService.run(id, request);
        return ResultUtil.success("");
    }

    @GetMapping(value = "/stop")
    public Result<String> stop(String id, HttpServletRequest request) throws DoValidException {
        taskService.stop(id, request);
        return ResultUtil.success("");
    }

    @GetMapping(value = "/getStatus")
    public Result<String> getStatus(String id, HttpServletRequest request) throws DoValidException {
        taskService.updateStatus(id, request);
        return ResultUtil.success("");
    }
}

class TaskFindAllVm extends PageableVm {
    @ApiModelProperty("任务名称")
    private String name;

    @ApiModelProperty("所属资源组")
    private String groupId;

    @ApiModelProperty("标签KEY列表")
    private String metaKey;

    @ApiModelProperty("标签Value列表")
    private String metaValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMetaKey() {
        return metaKey;
    }

    public void setMetaKey(String metaKey) {
        this.metaKey = metaKey;
    }

    public String getMetaValue() {
        return metaValue;
    }

    public void setMetaValue(String metaValue) {
        this.metaValue = metaValue;
    }
}