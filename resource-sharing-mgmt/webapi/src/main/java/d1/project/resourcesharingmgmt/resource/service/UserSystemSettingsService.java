package d1.project.resourcesharingmgmt.resource.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.project.resourcesharingmgmt.common.utils.BaseUtils;
import d1.project.resourcesharingmgmt.resource.dao.UserSystemSettingsDao;
import d1.project.resourcesharingmgmt.resource.entity.UserSystemSettingsEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * d1Project
 *
 * @author zhengyang
 * @date 2021-11-05 16:17
 */
@Service
public class UserSystemSettingsService {
    private final UserSystemSettingsDao userSystemSettingsDao;

    public UserSystemSettingsService(UserSystemSettingsDao userSystemSettingsDao) {
        this.userSystemSettingsDao = userSystemSettingsDao;
    }

    public UserSystemSettingsEntity findByAccount(String account) {
        Optional<UserSystemSettingsEntity> entityOptional = userSystemSettingsDao.findByAccount(account);
        if(entityOptional.isPresent()){
            return entityOptional.get();
        }
        return null;
    }

    public void insert (JSONObject model) throws DoValidException {
        String account = model.getString("account");
        String value = model.getString("value");
        UserSystemSettingsEntity entity = new UserSystemSettingsEntity();
        Optional<UserSystemSettingsEntity> entityOptional = userSystemSettingsDao.findByAccount(account);
        if(!entityOptional.isPresent()){
            entity.setId(BaseUtils.generate32Id());
        }else{
            entity = entityOptional.get();
        }
        entity.setAccount(account);
        entity.setValue(value);
        userSystemSettingsDao.save(entity);
    }
}
