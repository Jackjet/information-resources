package d1.project.resourcesharingmgmt.system.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.system.configuration.SSOConfig;
import d1.project.resourcesharingmgmt.system.model.sso.SSOConfigVm;
import d1.project.resourcesharingmgmt.system.service.BaseService;
import net.dcrun.component.osinfo.OsInfoService;
import net.dcrun.component.osinfo.model.OsInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * d1Project
 * 系统管理_系统设置_数据备份
 *
 * @author kikki
 * @date 2020-09-12 23:56
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/system/base")
public class BaseController {
    @Value("${d1.project.login.show-image-captcha:false}")
    private boolean isShowImageCaptcha;

    final
    SSOConfig ssoConfig;


    private final BaseService baseService;

    public BaseController(BaseService baseService, SSOConfig ssoConfig) {
        this.baseService = baseService;
        this.ssoConfig = ssoConfig;
    }

    /**
     * 获取系统信息
     */
    @Auth("noauth")
    @GetMapping(value = "/ssoConfig")
    public Result<OsInfo> ssoConfig() throws Exception {

        return ResultUtil.success("", new SSOConfigVm(ssoConfig));
    }

    /**
     * 获取系统信息
     */
    @GetMapping(value = "/findOsInfo")
    public Result<OsInfo> findOsInfo() throws Exception {
        OsInfoService osInfoService = new OsInfoService();
        return ResultUtil.success("", osInfoService.getOsInfo());
    }

    /**
     * 获取数据库信息
     */
    @GetMapping(value = "/findDatabaseInfo")
    public Result<String> findDatabaseInfo() {
        return ResultUtil.success("", baseService.findDatabaseInfo());
    }

    /**
     * 判断是否显示滑块
     */
    @Auth("noauth")
    @GetMapping(value = "/getImageVerifyCodeState")
    public Result<String> getImageVerifyCodeState() {
        return ResultUtil.success("", isShowImageCaptcha);
    }


    /**
     * 获取验证滑块
     */
    @Auth("noauth")
    @PostMapping(value = "/getImageVerifyCode")
    public Result<String> getImageVerifyCode() throws Exception {
        return ResultUtil.success("", baseService.getImageVerifyCode());
    }


    /**
     * 验证滑块
     */
    @Auth("noauth")
    @GetMapping(value = {"/imageVerify"})
    public Result<String> imageVerify(String xWidth, String checkMoveId) throws DoValidException {
        baseService.checkMove(xWidth, checkMoveId);
        return ResultUtil.success();
    }

}
