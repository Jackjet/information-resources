package d1.project.resourcesharingmgmt.system.service;

import d1.project.resourcesharingmgmt.system.dao.SystemSettingsDao;
import d1.project.resourcesharingmgmt.system.entity.SystemSettingsEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-14 10:17
 */
@Service
public class SystemSettingsService {
    private final SystemSettingsDao systemSettingsDao;

    public SystemSettingsService(SystemSettingsDao systemSettingsDao) {
        this.systemSettingsDao = systemSettingsDao;
    }

    public Optional<SystemSettingsEntity> findByType(String type) {
        return systemSettingsDao.findByType(type);
    }

    public void save (SystemSettingsEntity systemSettingsEntity){
        systemSettingsDao.save(systemSettingsEntity);
    }
}
