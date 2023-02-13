package d1.project.resourcesharingmgmt.system.service.sso;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.resourcesharingmgmt.log.service.UserLogService;
import d1.project.resourcesharingmgmt.system.dao.WebAdminUserDao;
import d1.project.resourcesharingmgmt.system.entity.WebAdminUserEntity;
import d1.project.resourcesharingmgmt.system.model.MySignInResult;
import d1.project.resourcesharingmgmt.system.model.WebAdminUserExcelImport;
import d1.project.resourcesharingmgmt.system.model.sso.SSOUserToken;
import d1.project.resourcesharingmgmt.system.model.vm.*;
import d1.project.resourcesharingmgmt.system.service.*;
import org.keycloak.representations.idm.UserRepresentation;
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
        super(webAdminUserDao, userLogService, organizationUserService, organizationService, roleUserService, roleService, baseService, ssoService);
        this.ssoService = ssoService;
    }

    @Override
    public WebAdminUserEntity insert(WebAdminUserInsertVm model, HttpServletRequest request) throws DoValidException {
        WebAdminUserEntity entity = super.insert(model, request);
        try {
            if (ssoService.existsFindByName(entity.getAccount())) {
                UserRepresentation ssoUser = ssoService.findByName(entity.getAccount());
                //如果SSO存在此用户就更新SSOID，不然会报错
                entity.setSsoId(ssoUser.getId());
                entity.setName(ssoUser.getFirstName());
            } else {
                entity = ssoService.createUser(entity);
            }
        }catch (Exception e){
            throw new DoValidException("账号创建失败");
        }
        save(entity);
        return entity;
    }

    @Override
    public void insertAll(List<WebAdminUserExcelImport> model, HttpServletRequest request) throws DoValidException {
        super.insertAll(model, request, webAdminUserEntity -> {
            try {
                if (ssoService.existsFindByName(webAdminUserEntity.getAccount())) {
                    return true;
                } else {
                    save(ssoService.createUser(webAdminUserEntity));
                    return false;
                }
            } catch (Exception e) {
                return true;
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
        try {
            if (ssoService.existsFindByName(entity.getAccount())) {
                ssoService.deleteUser(entity.getSsoId());
            }
        }catch (Exception e){
            throw new DoValidException("账号删除失败");
        }
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
        ssoService.updatePassword(entity, model.getNewPassword());
        return entity;
    }

    @Override
    public List<WebAdminUserEntity> updatePasswordReset(WebAdminUserUpdatePasswordResetVm model, HttpServletRequest request) throws DoValidException {
        List<WebAdminUserEntity> entities = super.updatePasswordReset(model, request);
        for (WebAdminUserEntity entity : entities) {
            ssoService.updatePasswordReset(entity);
        }
        return entities;
    }

    @Override
    public List<WebAdminUserEntity> updateAllEnable(WebAdminUserUpdateAllEnableVm model, HttpServletRequest request) throws Exception {
        List<WebAdminUserEntity> entities = super.updateAllEnable(model, request);
        for (WebAdminUserEntity entity : entities) {
            ssoService.updateEnable(entity);
        }
        return entities;
    }

    @Override
    public WebAdminUserEntity updateEnable(WebAdminUserUpdateEnableVm model, HttpServletRequest request) throws Exception {
        WebAdminUserEntity entity = super.updateEnable(model, request);
        ssoService.updateEnable(entity);
        return entity;
    }


//    @Override
//    public MySignInResult ssoLogin(String accessToken, HttpServletRequest request) throws DoValidException {
//        ResponseEntity<SSOUserToken> ssoUserTokenResponseEntity = ssoService.ssoSignIn(accessToken);
//        if (!ssoUserTokenResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
//            throw new DoValidException("登录异常");
//        }
//        SSOUserToken body = ssoUserTokenResponseEntity.getBody();
//        if (body == null) {
//            throw new DoValidException("登录异常");
//        }
//        Optional<WebAdminUserEntity> byAccount = super.findByAccount(body.getPreferredUsername());
//        if (byAccount.isPresent()) {
//            WebAdminUserEntity webAdminUserEntity = byAccount.get();
//            webAdminUserEntity.setSsoId(body.getSub());
//            save(webAdminUserEntity);
//            return super.login(webAdminUserEntity.getAccount(), webAdminUserEntity.getPassword(), request);
//        } else {
//            WebAdminUserInsertVm model = new WebAdminUserInsertVm();
//            model.setAccount(body.getPreferredUsername());
//            model.setName(body.getName());
//            model.setSex(0);
//            WebAdminUserEntity webAdminUserEntity = super.insert(model, request);
//            webAdminUserEntity.setSsoId(body.getSub());
//            save(webAdminUserEntity);
//            return login(webAdminUserEntity.getAccount(), webAdminUserEntity.getPassword(), request);
//        }
//    }
}
