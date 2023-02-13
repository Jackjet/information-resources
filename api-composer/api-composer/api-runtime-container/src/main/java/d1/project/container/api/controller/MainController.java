package d1.project.container.api.controller;

import d1.project.container.api.entity.ApiEntity;
import d1.project.container.api.http.Result;
import d1.project.container.api.http.ResultUtil;
import d1.project.container.api.model.RunModel;
import d1.project.container.api.model.RunModelFindAll;
import d1.project.container.api.service.MainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;


/**
 * @author maorui
 */

@RestController
@RequestMapping("api/container")
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private final MainService service;

    public MainController(MainService service) {
        this.service = service;
    }

    @GetMapping("/status")
    public Result<String> status() {
        return ResultUtil.success();
    }

    @GetMapping(value = "/log")
    public Result<List<String>> log(@Valid @RequestParam String day) throws Exception {
        return ResultUtil.success("", service.log(day));
    }

    @GetMapping(value = "/findAll")
    public Result<Page<ApiEntity>> findAll(@Valid RunModelFindAll model) throws Exception {
        return ResultUtil.success("", service.findAll(model));
    }


    @PostMapping("/run")
    public Result<String> run(@Valid @RequestBody RunModel model) throws Exception {
        service.run(model);
        return ResultUtil.success();
    }

    @DeleteMapping(value = "/delete")
    public Result<String> delete(@Valid @NotBlank(message = "id不能为空") String id) throws Exception {
        service.remove(id);
        return ResultUtil.success();
    }

}
