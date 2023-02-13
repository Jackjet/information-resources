package d1.project.resourcesharingmgmt.system.controller.web;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.SignInResult;
import d1.project.resourcesharingmgmt.system.service.IWebAdminUserService;
import net.dcrun.component.file.server.IFileServerService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * d1project
 * 系统管理_用户管理
 *
 * @author kikki
 * @date 2020-09-07 15:25
 */
@Auth("noauth")
@RestController("/web/system/webUser")
@RequestMapping("/web/system/webUser")
public class WebUserController {
    private final IFileServerService fileServerService;
    private final IWebAdminUserService webAdminUserService;

    public WebUserController(IWebAdminUserService webAdminUserService, IFileServerService fileServerService) {
        this.webAdminUserService = webAdminUserService;
        this.fileServerService = fileServerService;
    }

    /**
     * 登录
     */
    @GetMapping(value = {"/login"})
    public Result<SignInResult> login(@Valid @NotBlank(message = "用户名不可为空") String username, @Valid @NotBlank(message = "密码不可为空") String password, String xWidth, String checkMoveId, HttpServletRequest request) throws DoValidException {
        try {
            return ResultUtil.success("", webAdminUserService.login(username, password, xWidth, checkMoveId, request));
        } catch (DoValidException e) {
            return ResultUtil.fail("登录失败");
        }

    }

    /**
     * 启用禁用
     */
    @GetMapping(value = "/signOut")
    public Result<String> signOut(HttpServletRequest request) throws Exception {
        request.logout();
        webAdminUserService.singOut(request);
        return ResultUtil.success("");
    }

    /**
     * 获取当前登录用户
     */
    @Auth("noauth")
    @GetMapping(value = {"/getLoginUser"})
    public Result<String> getLoginUser(HttpServletRequest request) throws Exception {
        try {
            return ResultUtil.success("", webAdminUserService.getLoginUser(request));
        } catch (Exception e) {
            return ResultUtil.fail("");
        }
    }

    /**
     * 清空SsoCookie
     */
    @Auth("noauth")
    @GetMapping(value = {"/clearSsoCookie"})
    public Result<String> clearSsoCookie(HttpServletRequest request, HttpServletResponse response) throws Exception {
        webAdminUserService.clearSsoCookie(request, response);
        return ResultUtil.success("");
    }
}
