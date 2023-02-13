package d1.project.api.integration.external.business;

import d1.project.api.integration.common.service.IApplicationService;
import d1.project.api.integration.external.entity.ApplicationUser;
import d1.project.api.integration.external.service.ApplicationUserService;
import org.springframework.stereotype.Service;

@Service
public class ApplicationExtBusiness {
    private final IApplicationService iApplicationService;
    private final ApplicationUserService applicationUserService;

    public ApplicationExtBusiness(IApplicationService iApplicationService, ApplicationUserService applicationUserService) {
        this.iApplicationService = iApplicationService;
        this.applicationUserService = applicationUserService;
    }

    public String findKeyByUserId(String userId) throws Exception {
        ApplicationUser applicationUser = applicationUserService.findFirstByUserId(userId);
        return iApplicationService.findById(applicationUser.getAppid()).getKey();
    }
}
