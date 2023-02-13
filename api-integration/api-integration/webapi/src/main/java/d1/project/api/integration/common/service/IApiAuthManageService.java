package d1.project.api.integration.common.service;

import d1.project.api.integration.apimanage.entity.ApiAuthManage;

import java.util.List;

public interface IApiAuthManageService {
    boolean existsAllByAppId(List<String> appIdList);
    boolean existsAllByAppId(String appId);
    void saveInfo(ApiAuthManage insertVm);
    ApiAuthManage findFirstByApiIdAndAppId(String apiId, String appId);

    void deleteAllByAppId(String appId);
}
