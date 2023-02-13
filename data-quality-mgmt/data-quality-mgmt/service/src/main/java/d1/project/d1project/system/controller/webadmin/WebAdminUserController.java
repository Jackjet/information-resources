package d1.project.d1project.system.controller.webadmin;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.exception.ExcelCommonException;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.SignInResult;
import d1.framework.webapi.utils.CacheManger;
import d1.framework.webapi.utils.TokenManager;
import d1.project.d1project.common.Constants;
import d1.project.d1project.common.utils.BaseUtils;
import d1.project.d1project.system.entity.WebAdminUserEntity;
import d1.project.d1project.system.model.WebAdminUserExcelExport;
import d1.project.d1project.system.model.WebAdminUserExcelImport;
import d1.project.d1project.system.model.WebAdminUserVm;
import d1.project.d1project.system.model.vm.*;
import d1.project.d1project.system.service.IWebAdminUserService;
import d1.project.d1project.system.service.WebAdminUserUploadDataListener;
import net.dcrun.component.ehcache.IEhcacheService;
import net.dcrun.component.file.server.IFileServerService;
import net.dcrun.component.file.server.UploadResult;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理端_系统管理_用户管理
 *
 * @author kikki
 * @date 2020-09-07 15:25
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/system/webAdminUser")
public class WebAdminUserController {


    private final IFileServerService fileServerService;
    private final IWebAdminUserService webAdminUserService;
    public WebAdminUserController(IWebAdminUserService webAdminUserService, IFileServerService fileServerService ) {
        this.webAdminUserService = webAdminUserService;
        this.fileServerService = fileServerService;
    }

    /**
     * 新增
     */
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody WebAdminUserInsertVm model, HttpServletRequest request) throws DoValidException {
        webAdminUserService.insert(model, request);
        return ResultUtil.success("");
    }

    /**
     * 批量删除
     */
    @DeleteMapping(value = "/deleteAll")
    public Result<String> delete(@Valid @Size(min = 1, message = "无效操作") @RequestParam("ids") List<String> ids, HttpServletRequest request) throws DoValidException {
        webAdminUserService.deleteAll(ids, request);
        return ResultUtil.success("");
    }
    /**
     * 删除
     */
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@Valid @NotBlank(message = "无效操作") String id, HttpServletRequest request) throws Exception {
        webAdminUserService.delete(id, request);
        return ResultUtil.success("");
    }
    /**
     * 更新
     */
    @PutMapping(value = "/update")
    public Result<String> update(@Valid @RequestBody WebAdminUserUpdateVm model, HttpServletRequest request) throws Exception {
        webAdminUserService.update(model, request);
        return ResultUtil.success("");
    }

    /**
     * 修改密码
     */
    @PutMapping(value = {"/updatePassword"})
    public Result<String> updatePassword(@Valid @RequestBody WebAdminUserUpdatePasswordVm model, HttpServletRequest request) throws DoValidException {
        webAdminUserService.updatePassword(model, request);
        return ResultUtil.success("");
    }

    /**
     * 重置密码列表
     */
    @PutMapping(value = {"/updatePasswordReset"})
    public Result<String> updatePasswordReset(@Valid @RequestBody WebAdminUserUpdatePasswordResetVm model, HttpServletRequest request) throws DoValidException {
        webAdminUserService.updatePasswordReset(model, request);
        return ResultUtil.success("");
    }

    /**
     * 启用禁用列表
     */
    @PutMapping(value = {"/updateAllEnable"})
    public Result<String> updateAllEnable(@Valid @RequestBody WebAdminUserUpdateAllEnableVm model, HttpServletRequest request) throws Exception {
        webAdminUserService.updateAllEnable(model, request);
        return ResultUtil.success("");
    }

    /**
     * 启用禁用
     */
    @PutMapping(value = {"/updateEnable"})
    public Result<String> updateAllEnable(@Valid @RequestBody WebAdminUserUpdateEnableVm model, HttpServletRequest request) throws Exception {
        webAdminUserService.updateEnable(model, request);
        return ResultUtil.success("");
    }

    /**
     * 查询所有
     */
    @GetMapping(value = "/findAll")
    public Result<Page<WebAdminUserEntity>> findAll(WebAdminUserFindAllVm model) throws Exception {
        return ResultUtil.success("", webAdminUserService.findAll(model));
    }

    /**
     * 详情
     */
    @GetMapping(value = "/find")
    public Result<WebAdminUserVm> find(@RequestParam String id) throws DoValidException {
        return ResultUtil.success("", webAdminUserService.find(id));
    }


    /**
     * 登录
     */
    @Auth("noauth")
    @GetMapping(value = {"/login"})
    public Result<SignInResult> login(String username,  String password, String xWidth, String checkMoveId, HttpServletRequest request) throws DoValidException {
        return ResultUtil.success("", webAdminUserService.login(username, password, xWidth, checkMoveId, request));
    }

    /**
     * sso登录  不再使用
     */
//    @Auth("noauth")
//    @GetMapping(value = {"/ssoLogin"})
//    public Result<SignInResult> ssoLogin(String accessToken, HttpServletRequest request) throws DoValidException {
//        return ResultUtil.success("", webAdminUserService.ssoLogin(accessToken, request));
//    }

    /**
     * 登出
     */
    @Auth("noauth")
    @GetMapping(value = {"/logout"})
    public Result<String> logOut( HttpServletRequest request) throws ServletException {
        request.logout();
        TokenManager.getInstance().removeToken(request);
        return ResultUtil.success("success");
    }

    /**
     * 启用禁用
     */
    @GetMapping(value = "/signOut")
    public Result<String> signOut(HttpServletRequest request) throws DoValidException {
        webAdminUserService.singOut(request);
        return ResultUtil.success("");
    }

    /**
     * 导出
     */
    @PostMapping("/export")
    public Result<String> export(WebAdminUserFindAllVm model, HttpServletResponse response) throws Exception {
        List<WebAdminUserExcelExport> allList = webAdminUserService.findAllList(model);
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        webAdminUserService.easyExcelWrite(response, allList);
        return null;
    }


    /**
     * 根据id导出
     */
    @PostMapping("/exportByIds")
    public Result<String> export(@Valid @RequestBody IdsBaseVm model, HttpServletResponse response) throws Exception {
        List<WebAdminUserExcelExport> allList = webAdminUserService.findAllByIds(model.getIds());
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        webAdminUserService.easyExcelWrite(response, allList);
        return null;
    }


    /**
     * 导出模板
     */
    @PostMapping("/exportModel")
    public Result<String> exportModel(HttpServletResponse response) throws Exception {
        List<WebAdminUserExcelImport> data = new ArrayList<>();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("用户", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), WebAdminUserExcelImport.class).sheet("用户").doWrite(data);
        return null;
    }

    /**
     * 导入
     */
    @PostMapping("/import")
    public Result<String> upload(MultipartFile file, HttpServletRequest request) throws IOException, DoValidException {
        String uuId = "webAdminUserImport/" + BaseUtils.generate32Id();
        IEhcacheService cache = CacheManger.getInstance().getCache();
        List<Object> objects = null;
        try {
            objects = EasyExcel.read(file.getInputStream(), WebAdminUserExcelImport.class, new WebAdminUserUploadDataListener(cache, webAdminUserService, request, uuId)).sheet().doReadSync();
        } catch (ExcelCommonException exception) {
            throw new DoValidException("文件格式错误");
        }
        if (objects.isEmpty()) {
            throw new DoValidException("导入数据不能为空");
        }
        if (cache.containsKey(uuId)) {
            String string = cache.getString(uuId);
            cache.removeData(uuId);
            throw new DoValidException(string);
        }
        return ResultUtil.success("");
    }

    /**
     * 上传用户头像
     *
     * @param file 文件
     * @return 文件上传信息
     * @throws Exception 异常
     */
    @PostMapping(value = "/upload/users/avatar")
    public UploadResult uploadAttachment(@RequestParam("file") MultipartFile file) throws Exception {
        return fileServerService.uploadFile(file, Constants.USERS, "webadmin/system/webAdminUser/download/users/avatar");
    }

    /**
     * 下载用户头像
     *
     * @param fileName 文件名称
     * @param request  request
     * @return 下载流
     * @throws Exception 异常
     */
    @Auth("noauth")
    @RequestMapping(value = "/download/users/avatar/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadAttachment(@PathVariable String fileName, HttpServletRequest request) throws Exception {
        return fileServerService.downloadFile(request, Constants.USERS, fileName);
    }

    /**
     * 获取当前登录用户
     */
    @Auth("noauth")
    @GetMapping(value = {"/getLoginUser"})
    public Result<String> getLoginUser(HttpServletRequest request) throws Exception {
        return ResultUtil.success("", webAdminUserService.getLoginUser(request));
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

    /**
     * 同步用户
     */
    @GetMapping(value = "/sync")
    public Result<String> sync(HttpServletRequest request) throws Exception {
        webAdminUserService.sync(request);
        return ResultUtil.success("");
    }
}
