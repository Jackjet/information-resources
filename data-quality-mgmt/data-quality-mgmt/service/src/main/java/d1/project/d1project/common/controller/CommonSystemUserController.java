package d1.project.d1project.common.controller;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.d1project.system.entity.WebAdminUserEntity;
import d1.project.d1project.system.service.IWebAdminUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 通用接口_查询系统用户
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/common/system/user")
public class CommonSystemUserController {
    private final IWebAdminUserService iWebAdminUserService;

    public CommonSystemUserController(IWebAdminUserService iWebAdminUserService) {
        this.iWebAdminUserService = iWebAdminUserService;
    }

    /**
     * 查询系统用户
     */
    @GetMapping("/findAll")
    public Result<List<WebAdminUserEntity>> findAll() {
        try {
            return ResultUtil.success("成功", iWebAdminUserService.findAll());
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }
}
