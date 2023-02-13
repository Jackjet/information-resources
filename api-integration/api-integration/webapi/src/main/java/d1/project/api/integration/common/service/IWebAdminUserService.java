package d1.project.api.integration.common.service;

import d1.project.api.integration.system.entity.WebAdminUserEntity;

import java.util.List;

public interface IWebAdminUserService {
    List<WebAdminUserEntity> findAllByName(String name);
}
