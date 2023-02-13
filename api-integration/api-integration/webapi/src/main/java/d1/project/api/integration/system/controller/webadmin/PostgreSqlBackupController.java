package d1.project.api.integration.system.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.api.integration.system.model.vm.PostgreSqlBackupVm;
import d1.project.api.integration.system.service.PostgreSqlBackupService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

/**
 * d1Project
 * 系统设置_数据备份
 *
 * @author kikki
 * @date 2020-09-14 19:56
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/system/timedTask")
public class PostgreSqlBackupController {
    private final PostgreSqlBackupService postgreSqlBackupService;

    public PostgreSqlBackupController(PostgreSqlBackupService postgreSqlBackupService) {
        this.postgreSqlBackupService = postgreSqlBackupService;
    }

    @PutMapping(value = "/update")
    public Result<String> update(@Valid @RequestBody PostgreSqlBackupVm model, HttpServletRequest request) throws Exception {
        postgreSqlBackupService.update(model, request);
        return ResultUtil.success("");
    }

    @GetMapping(value = "/find")
    public Result<PostgreSqlBackupVm> find() throws IOException {
        return ResultUtil.success("", postgreSqlBackupService.find());
    }

}
