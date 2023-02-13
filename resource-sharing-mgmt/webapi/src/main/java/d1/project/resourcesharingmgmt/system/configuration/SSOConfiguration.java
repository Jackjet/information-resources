package d1.project.resourcesharingmgmt.system.configuration;

import d1.project.resourcesharingmgmt.log.service.UserLogService;
import d1.project.resourcesharingmgmt.system.dao.WebAdminUserDao;
import d1.project.resourcesharingmgmt.system.service.*;
import d1.project.resourcesharingmgmt.system.service.sso.WebAdminUserServiceSSOPartialUseImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MY_JAR_NAME
 *
 * @author kikki
 * @date 2020-11-24 14:45
 */
@Configuration
public class SSOConfiguration {
    @Value("${sso.use:doNotUse}")
    private String use;

    @Value("${d1.project.login.show-image-captcha:false}")
    private boolean isShowImageCaptcha;

    private final OrganizationUserService organizationUserService;
    private final OrganizationService organizationService;
    private final RoleUserService roleUserService;
    private final RoleService roleService;
    private final WebAdminUserDao webAdminUserDao;
    private final UserLogService userLogService;
    private final BaseService baseService;
    private final SSOService ssoService;

    public SSOConfiguration(WebAdminUserDao webAdminUserDao,
                            UserLogService userLogService,
                            OrganizationUserService organizationUserService,
                            OrganizationService organizationService,
                            RoleUserService roleUserService,
                            RoleService roleService,
                            BaseService baseService,
                            SSOService ssoService) {

        this.webAdminUserDao = webAdminUserDao;
        this.userLogService = userLogService;
        this.organizationUserService = organizationUserService;
        this.organizationService = organizationService;
        this.roleUserService = roleUserService;
        this.roleService = roleService;
        this.baseService = baseService;
        this.ssoService = ssoService;
    }

    /*partialUse部分使用,useAll全部使用(未实现),doNotUse不使用*/
    @Bean
    public IWebAdminUserService iWebAdminUserService() {
        IWebAdminUserService service;
        switch (use) {
            case "partialUse":
                service = new WebAdminUserServiceSSOPartialUseImpl(webAdminUserDao, userLogService, organizationUserService, organizationService, roleUserService, roleService, baseService, ssoService);
                break;
            case "useAll":
            case "doNotUse":
            default:
                service = new WebAdminUserServiceImpl(webAdminUserDao, userLogService, organizationUserService, organizationService, roleUserService, roleService, baseService, ssoService);
                break;
        }

        service.setShowImageCaptcha(isShowImageCaptcha);
        return service;
    }

}
