package d1.project.dataintegration.system.service.sso;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.dataintegration.log.service.UserLogService;
import d1.project.dataintegration.system.dao.WebAdminUserDao;
import d1.project.dataintegration.system.entity.WebAdminUserEntity;
import d1.project.dataintegration.system.model.MySignInResult;
import d1.project.dataintegration.system.model.WebAdminUserExcelImport;
import d1.project.dataintegration.system.model.sso.SSOUserToken;
import d1.project.dataintegration.system.model.vm.*;
import d1.project.dataintegration.system.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-07 15:21
 */
public class WebAdminUserServiceSSOPartialUseImpl extends WebAdminUserServiceImpl {
    private final static String cannotDeleted = "admin";
    private final SSOService ssoService;

    public WebAdminUserServiceSSOPartialUseImpl(
            WebAdminUserDao webAdminUserDao,
            UserLogService userLogService,
            OrganizationUserService organizationUserService,
            OrganizationService organizationService,
            RoleUserService roleUserService,
            RoleService roleService,
            BaseService baseService,
            SSOService ssoService) {
        super(webAdminUserDao, userLogService, organizationUserService, organizationService, roleUserService, roleService, baseService);
        this.ssoService = ssoService;
    }

    @Override
    public WebAdminUserEntity insert(WebAdminUserInsertVm model, HttpServletRequest request) throws DoValidException {
        WebAdminUserEntity entity = ssoService.createUser(super.insert(model, request));
        save(entity);
        return entity;
    }

    @Override
    public void insertAll(List<WebAdminUserExcelImport> model, HttpServletRequest request) throws DoValidException {
        super.insertAll(model, request, webAdminUserEntity -> {
            if (ssoService.existsFindByName(webAdminUserEntity.getAccount())) {
                return true;
            } else {
                try {
                    save(ssoService.createUser(webAdminUserEntity));
                } catch (DoValidException e) {
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void deleteAll(List<String> ids, HttpServletRequest request) throws DoValidException {
        if (ids.contains(cannotDeleted)) {
            throw new DoValidException("初始账号不可删除");
        }
        for (String id : ids) {
            delete(id, request);
        }
    }

    @Override
    public WebAdminUserEntity delete(String id, HttpServletRequest request) throws DoValidException {
        WebAdminUserEntity entity = super.delete(id, request);
        ssoService.deleteUser(entity.getSsoId());
        return entity;
    }

    @Override
    public WebAdminUserEntity update(WebAdminUserUpdateVm model, HttpServletRequest request) throws DoValidException {
        WebAdminUserEntity entity = super.update(model, request);
        ssoService.updateUser(entity);
        return entity;
    }

    /**
     * 单点登录模式无需实现
     */
    @Override
    public WebAdminUserEntity updatePassword(WebAdminUserUpdatePasswordVm model, HttpServletRequest request) throws DoValidException {
        WebAdminUserEntity entity = super.updatePassword(model, request);
        ssoService.updatePassword(entity);
        return entity;
    }

    @Override
    public List<WebAdminUserEntity> updatePasswordReset(WebAdminUserUpdatePasswordResetVm model, HttpServletRequest request) throws DoValidException {
        List<WebAdminUserEntity> entities = super.updatePasswordReset(model, request);
        for (WebAdminUserEntity entity : entities) {
            ssoService.updatePassword(entity);
        }
        return entities;
    }

    @Override
    public List<WebAdminUserEntity> updateAllEnable(WebAdminUserUpdateAllEnableVm model, HttpServletRequest request) throws DoValidException {
        List<WebAdminUserEntity> entities = super.updateAllEnable(model, request);
        for (WebAdminUserEntity entity : entities) {
            ssoService.updateEnable(entity);
        }
        return entities;
    }

    @Override
    public WebAdminUserEntity updateEnable(WebAdminUserUpdateEnableVm model, HttpServletRequest request) throws DoValidException {
        WebAdminUserEntity entity = super.updateEnable(model, request);
        ssoService.updateEnable(entity);
        return entity;
    }


    @Override
    public MySignInResult ssoLogin(String accessToken, HttpServletRequest request) throws DoValidException {
        ResponseEntity<SSOUserToken> ssoUserTokenResponseEntity = ssoService.ssoSignIn(accessToken);
        if (!ssoUserTokenResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
            throw new DoValidException("登录异常");
        }
        SSOUserToken body = ssoUserTokenResponseEntity.getBody();
        if (body == null) {
            throw new DoValidException("登录异常");
        }
        Optional<WebAdminUserEntity> byAccount = super.findByAccount(body.getPreferredUsername());
        if (byAccount.isPresent()) {
            WebAdminUserEntity webAdminUserEntity = byAccount.get();
            webAdminUserEntity.setSsoId(body.getSub());
            save(webAdminUserEntity);
            return super.login(webAdminUserEntity.getAccount(), webAdminUserEntity.getPassword(), request);
        } else {
            WebAdminUserInsertVm model = new WebAdminUserInsertVm();
            model.setAccount(body.getPreferredUsername());
            model.setName(body.getName());
            model.setSex(0);
            WebAdminUserEntity webAdminUserEntity = super.insert(model, request);
            webAdminUserEntity.setSsoId(body.getSub());
            save(webAdminUserEntity);
            return login(webAdminUserEntity.getAccount(), webAdminUserEntity.getPassword(), request);
        }
    }
}
