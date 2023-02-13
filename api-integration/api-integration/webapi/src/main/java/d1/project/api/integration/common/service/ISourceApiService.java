package d1.project.api.integration.common.service;

import d1.project.api.integration.apimanage.entity.SourceApi;

public interface ISourceApiService {
    void save(SourceApi sourceApi);
    SourceApi findFirstByApiId(String apiId);
}
