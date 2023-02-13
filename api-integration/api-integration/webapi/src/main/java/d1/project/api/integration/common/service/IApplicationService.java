package d1.project.api.integration.common.service;

import d1.project.api.integration.application.entity.Application;

public interface IApplicationService {
    void save(Application application);
    Application findById(String id) throws Exception;
}
