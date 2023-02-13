package d1.project.d1project.system.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.d1project.system.configuration.SSOConfig;
import d1.project.d1project.system.model.sso.SSOConfigVm;
import d1.project.d1project.system.service.BaseService;
import net.dcrun.component.osinfo.OsInfoService;
import net.dcrun.component.osinfo.model.OsInfoDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端_系统管理_基础功能
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
     * 获取sso信息
     */
    @Auth("noauth")
    @GetMapping(value = "/ssoConfig")
    public Result<SSOConfigVm> ssoConfig() throws Exception {
        return ResultUtil.success("", new SSOConfigVm(ssoConfig));
    }

    /**
     * 获取操作系统的基本信息
     * <p>
     * ALL 全部信息
     * CPU CPU信息
     * RAM 内存信息
     * BS  基本信息
     * IP  IP信息
     * HARDDISK 硬盘信息
     * OsInfo 返回操作系统基本信息
     * 硬盘信息
     * avail→本地硬盘可用大小
     * total→本地硬盘总大小
     * used→本地硬盘已经使大小
     * free→本地硬盘空闲大小
     * freeIdleRate→空闲率
     * 内存信息
     * memTotal→内存大小
     * memRam→内存使用量
     * memUsed→内存使用中
     * memFree→可用内存（空闲内存）
     * memoryUsage→内存使用率
     * 操作系统基本信息
     * osName→操作系统名称
     * osVersion→操作系统版本
     * CPU信息
     * cupFreeIdle→CPU平均空闲率
     * combined→CPU平均使用率
     * 获取IP地址信息
     * lanIp→内网IP
     * wanIp→外网IP
     */
    @GetMapping(value = "/findOsInfo")
    public Result<OsInfoDetail> findOsInfo() throws Exception {
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
    public Result<Boolean> getImageVerifyCodeState() {
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
