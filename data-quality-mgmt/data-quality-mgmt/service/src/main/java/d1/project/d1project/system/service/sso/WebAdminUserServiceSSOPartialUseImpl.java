package d1.project.d1project.system.service.sso;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.d1project.log.service.UserLogService;
import d1.project.d1project.system.dao.WebAdminUserDao;
import d1.project.d1project.system.entity.WebAdminUserEntity;
import d1.project.d1project.system.model.WebAdminUserExcelImport;
import d1.project.d1project.system.model.vm.*;
import d1.project.d1project.system.service.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
            throw new DoValidException("????????????????????????");
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
     * ??????????????????????????????
     */
    @Override
    public WebAdminUserEntity updatePassword(WebAdminUserUpdatePasswordVm model, HttpServletRequest request) throws DoValidException {
        WebAdminUserEntity entity = super.updatePassword(model, request);
        ssoService.updatePassword(entity, model.getTextPassword());
        return entity;
    }

    @Override
    public List<WebAdminUserEntity> updatePasswordReset(WebAdminUserUpdatePasswordResetVm model, HttpServletRequest request) throws DoValidException {
        List<WebAdminUserEntity> entities = super.updatePasswordReset(model, request);
        for (WebAdminUserEntity entity : entities) {
            ssoService.updatePassword(entity, "123456");
        }
        return entities;
    }

    @Override
    public List<WebAdminUserEntity> updateAllEnable(WebAdminUserUpdateAllEnableVm model, HttpServletRequest request) throws Exception {
        List<WebAdminUserEntity> entities = super.updateAllEnable(model, request);
        for (WebAdminUserEntity entity : entities) {
            ssoService.setEnabled(entity.getSsoId(), entity.isEnable());
        }
        return entities;
    }

    @Override
    public WebAdminUserEntity updateEnable(WebAdminUserUpdateEnableVm model, HttpServletRequest request) throws Exception {
        WebAdminUserEntity entity = super.updateEnable(model, request);
        ssoService.setEnabled(entity.getSsoId(), entity.isEnable());
        return entity;
    }


//    @Override
//    public MySignInResult ssoLogin(String accessToken, HttpServletRequest request) throws DoValidException {
//        ResponseEntity<SSOUserToken> ssoUserTokenResponseEntity = ssoService.ssoSignIn(accessToken);
//        if (!ssoUserTokenResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
//            throw new DoValidException("????????????");
//        }
//        SSOUserToken body = ssoUserTokenResponseEntity.getBody();
//        if (body == null) {
//            throw new DoValidException("????????????");
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
