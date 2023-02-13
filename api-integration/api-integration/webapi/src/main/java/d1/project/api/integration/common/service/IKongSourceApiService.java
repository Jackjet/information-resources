package d1.project.api.integration.common.service;

import d1.project.api.integration.apimanage.entity.KongSourceApi;

public interface IKongSourceApiService {
    void save(KongSourceApi kongSourceApi);
    KongSourceApi findByApiId(String sourceId);
}
