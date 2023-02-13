package d1.project.container.task.controller;

import com.alibaba.fastjson.JSONObject;
import d1.project.container.task.http.Result;
import d1.project.container.task.http.ResultUtil;
import d1.project.container.task.service.KettleService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author baozh
 */
@RestController
@RequestMapping("/client/kettle")
public class KettleController {

    private final KettleService kettleService;

    public KettleController(KettleService kettleService) {
        this.kettleService = kettleService;
    }

    /**
     * 运行kettle
     */
    @PostMapping(value = "/runKettle")
    public Result<JSONObject> runKettle(@RequestPart("id") String id, @RequestPart(value = "fileName") String name) {
        try {
            kettleService.runKettle(id, name);
            return ResultUtil.success("SUCCESS", id);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    /**
     * 停止作业
     */
    @GetMapping(value = "/stopJob")
    public Result<JSONObject> stopJob(String id) {
        try {
            kettleService.stopJob(id);
            return ResultUtil.success("SUCCESS");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    /**
     * 上传kettle
     */
    @PostMapping(value = "/uploadJob")
    public Result<JSONObject> uploadJob(@RequestPart("id") String id, @RequestPart(value = "files") MultipartFile[] files) {
        try {
            kettleService.uploadJob(files, id);
            return ResultUtil.success("SUCCESS", id);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    /**
     * 删除kettle
     */
    @GetMapping(value = "/deleteJob")
    public Result<JSONObject> deleteJob(String id) {
        try {
            kettleService.deleteJob(id);
            return ResultUtil.success("SUCCESS", id);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }
}
