package d1.project.dataintegration.task.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.dataintegration.task.model.TaskInfoForContainerVm;
import d1.project.dataintegration.task.model.TaskListForContainerVm;
import d1.project.dataintegration.task.service.TaskContainerService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhengyang
 */
@RestController
@RequestMapping("/webadmin/task/container")
public class TaskContainerController {
    private final TaskContainerService taskContainerService;

    public TaskContainerController(TaskContainerService taskContainerService) {
        this.taskContainerService = taskContainerService;
    }

    @Auth("noauth")
    @GetMapping(value = "/findAll")
    public Result<Page<TaskListForContainerVm>> findAll(TaskListForContainerFindAllVm model) throws Exception {
        return ResultUtil.success("SUCCESS", taskContainerService.findAllVm((JSONObject) JSON.toJSON(model)));
    }

    /**
     * 添加信息
     */
    @Auth("noauth")
    @GetMapping(value = "/findInfo")
    public Result<TaskInfoForContainerVm> findInfo(TaskInfoForContainerFindVm model, HttpServletRequest request) throws DoValidException {
        try {
            return ResultUtil.success("SUCCESS",taskContainerService.findInfo((JSONObject) JSON.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }
}

class TaskListForContainerFindAllVm extends PageableVm {
    @ApiModelProperty("任务名称")
    private String name;

    @ApiModelProperty("ip")
    private String ip;

    @ApiModelProperty("端口")
    private String port;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}

class TaskInfoForContainerFindVm {
    @ApiModelProperty("ip")
    private String ip;

    @ApiModelProperty("端口")
    private String port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}