package d1.project.d1project.common.controller;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.d1project.business.entity.TaskGroup;
import d1.project.d1project.common.service.ITaskGroupService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 通用接口_任务组
 *
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/common/task/group")
public class CommonTaskGroupController {
    private final ITaskGroupService iTaskGroupService;

    public CommonTaskGroupController(ITaskGroupService iTaskGroupService) {
        this.iTaskGroupService = iTaskGroupService;
    }

    /**
     * 查询任务组列表
     */
    @GetMapping("/findAll")
    public Result<List<TaskGroup>> findAll() {
        try {
            return ResultUtil.success("成功", iTaskGroupService.findAll());
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }
}
