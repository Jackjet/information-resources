package d1.project.api.integration.system.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.project.api.integration.system.entity.WebAdminUserEntity;
import d1.project.api.integration.system.model.MySignInResult;
import d1.project.api.integration.system.model.WebAdminUserExcelExport;
import d1.project.api.integration.system.model.WebAdminUserExcelImport;
import d1.project.api.integration.system.model.WebAdminUserVm;
import d1.project.api.integration.system.model.vm.*;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-07 15:21
 */

public interface IWebAdminUserService {

    void setShowImageCaptcha(boolean isShowImageCaptcha);

    /**
     * 新增
     *
     * @param model 传输模型
     */
    @Transactional(rollbackFor = Exception.class)
    WebAdminUserEntity insert(WebAdminUserInsertVm model, HttpServletRequest request) throws DoValidException;

    @Transactional(rollbackFor = Exception.class)
    void insertAll(List<WebAdminUserExcelImport> model, HttpServletRequest request) throws DoValidException;

    /**
     * 删除
     *
     * @param ids id列表
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteAll(List<String> ids, HttpServletRequest request) throws DoValidException;

    /**
     * 删除
     *
     * @param id id
     */
    @Transactional(rollbackFor = Exception.class)
    WebAdminUserEntity delete(String id, HttpServletRequest request) throws DoValidException;

    /**
     * 更新
     *
     * @param model 传输模型
     */
    WebAdminUserEntity update(WebAdminUserUpdateVm model, HttpServletRequest request) throws DoValidException;

    /**
     * 修改密码
     */
    @Transactional(rollbackFor = Exception.class)
    WebAdminUserEntity updatePassword(WebAdminUserUpdatePasswordVm model, HttpServletRequest request) throws DoValidException;

    /**
     * 重置密码列表
     */
    @Transactional(rollbackFor = Exception.class)
    List<WebAdminUserEntity> updatePasswordReset(WebAdminUserUpdatePasswordResetVm model, HttpServletRequest request) throws DoValidException;

    /**
     * 更新所有启用
     */
    @Transactional(rollbackFor = Exception.class)
    List<WebAdminUserEntity> updateAllEnable(WebAdminUserUpdateAllEnableVm model, HttpServletRequest request) throws DoValidException;

    /**
     * 更新启用
     */
    @Transactional(rollbackFor = Exception.class)
    WebAdminUserEntity updateEnable(WebAdminUserUpdateEnableVm model, HttpServletRequest request) throws DoValidException;

    Page<WebAdminUserVm> findAll(WebAdminUserFindAllVm model) throws Exception;

    List<WebAdminUserExcelExport> findAllList(WebAdminUserFindAllVm model) throws Exception;

    List<WebAdminUserExcelExport> findAllByIds(List<String> ids) throws Exception;

    WebAdminUserVm find(String id) throws DoValidException;

    Optional<WebAdminUserEntity> findById(String id);

    /**
     * 登录
     *
     * @param username 账号
     * @param password 密码
     */
    MySignInResult login(String username, String password, String xWidth, String checkMoveId, HttpServletRequest request) throws DoValidException;

    /**
     * sso登录
     *
     * @param username 账号
     * @param password 密码
     */
    MySignInResult ssoLogin(String accessToken, HttpServletRequest request) throws DoValidException;


    /**
     * 退出登录
     */
    void singOut(HttpServletRequest request) throws DoValidException;

    /**
     * 返回excel
     */
    void easyExcelWrite(HttpServletResponse response, List<WebAdminUserExcelExport> allList) throws IOException;

    /**
     * 获取当前登录用户
     *
     */
    JSONObject getLoginUser(HttpServletRequest request) throws Exception;

    /**
     * 清空SsoCookie
     *
     */
    void clearSsoCookie(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
