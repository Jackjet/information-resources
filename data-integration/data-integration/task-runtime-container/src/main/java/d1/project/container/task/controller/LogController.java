package d1.project.container.task.controller;

import d1.project.container.task.http.Result;
import d1.project.container.task.http.ResultUtil;
import d1.project.container.task.service.LogService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author baozh
 */
@RestController
@RequestMapping("/task/container")
public class LogController {
    private LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    /**
     * 查看日志文件列表
     * @param day 2021-01-01
     * @return
     */
    @GetMapping(value = "/log")
    public Result<List<String>> log(String day) {
        try {
            return ResultUtil.success("SUCCESS", logService.findLogs(day));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    /**
     * 下载文件
     * @param day 2021-01-01
     * @param fileName
     * @return
     * @throws Throwable
     */
    @RequestMapping("/download")
    public ResponseEntity<FileSystemResource> getUserExcel(String day, String fileName) throws Throwable {
        return logService.export(day, fileName);
    }

    /**
     * 检测是否在线
     */
    @GetMapping(value = "/status")
    public Result<String> status(String day) {
        try {
            return ResultUtil.success("SUCCESS");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }
}
