package d1.project.dataintegration.task.controller;

import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dataintegration.common.annotation.ApiAuth;
import d1.project.dataintegration.task.entity.TaskKettleFiles;
import d1.project.dataintegration.task.model.FileChild;
import d1.project.dataintegration.task.service.TaskKettleFilesService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author zhengyang
 */
//@Auth("webadmin")
@ApiAuth("webadmin")
@RestController
@RequestMapping("/webadmin/task/kettleFiles")
public class TaskKettleFilesController {
    private final TaskKettleFilesService taskKettleFilesService;

    public TaskKettleFilesController(TaskKettleFilesService taskKettleFilesService) {
        this.taskKettleFilesService = taskKettleFilesService;
    }

    /**
     * 查找任务的job
     */
    @GetMapping(value = "/findByTaskId")
    public Result<List<TaskKettleFiles>> findByTaskId(String taskId){
        try {
            return ResultUtil.success("SUCCESS",taskKettleFilesService.findByTaskId(taskId));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    /**
     * 根据任务ID和名称查找任务的文件
     */
    @GetMapping(value = "/findByTaskIdAndName")
    public Result<TaskKettleFiles> findByTaskIdAndName(String taskId, String name){
        try {
            return ResultUtil.success("SUCCESS",taskKettleFilesService.findByTaskIdAndName(taskId, name));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    /**
     * 根据任务ID和类别查找任务的文件
     */
    @GetMapping(value = "/findByTaskIdAndType")
    public Result<TaskKettleFiles> findByTaskIdAndType(String taskId, String type){
        try {
            return ResultUtil.success("SUCCESS",taskKettleFilesService.findByTaskIdAndType(taskId, type));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    /**
     * 创建作业
     */
    @PostMapping(value = "/uploadFile")
    public Result<List<FileChild>> uploadFile(@RequestPart("taskId") String taskId, @RequestPart(value = "file") List<MultipartFile> files, HttpServletRequest request) {
        try {
            return ResultUtil.success("SUCCESS",taskKettleFilesService.uploadFile(taskId, files, request));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    /**
     * 下载作业
     */
    @GetMapping(value = "/downloadFile")
    public Result<String> downloadFile(String taskId, HttpServletRequest request) {
        try {
            return ResultUtil.success("SUCCESS",taskKettleFilesService.downloadFile(taskId, request));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    /**
     * 删除作业
     */
    @DeleteMapping(value = "/deleteFile")
    public Result<String> deleteFile(String taskId, HttpServletRequest request) {
        try {
            taskKettleFilesService.deleteFile(taskId, request);
            return ResultUtil.success("SUCCESS");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }
}
