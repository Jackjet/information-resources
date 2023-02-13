package d1.project.api.integration.common.service;

import com.alibaba.fastjson.JSONObject;
import d1.project.api.integration.apimanage.entity.ApiBaseInfo;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IApiService {
    Page<ApiBaseInfo> findAllByContainer(JSONObject param) throws Exception ;
    List<ApiBaseInfo> findAllByContainer(String container);
    void saveInfo(ApiBaseInfo apiBaseInfo);
    boolean existsAllByName(String name);
    ApiBaseInfo findFirstByName(String name);
}
