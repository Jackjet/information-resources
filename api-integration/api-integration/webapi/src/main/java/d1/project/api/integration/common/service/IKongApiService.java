package d1.project.api.integration.common.service;

import d1.project.api.integration.apimanage.entity.KongApi;

public interface IKongApiService {
    KongApi findByApiId(String apiId);

    void save(KongApi kongApi);
}
