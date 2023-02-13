package d1.project.api.integration.external.service;

import d1.project.api.integration.external.dao.ApplicationUserDao;
import d1.project.api.integration.external.entity.ApplicationUser;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService {
    private final ApplicationUserDao applicationUserDao;

    public ApplicationUserService(ApplicationUserDao applicationUserDao) {
        this.applicationUserDao = applicationUserDao;
    }

    public ApplicationUser findFirstByUserId(String userId){
        return applicationUserDao.findFirstByUserId(userId);
    }

    public void save(ApplicationUser applicationUser){
        applicationUserDao.save(applicationUser);
    }
}
