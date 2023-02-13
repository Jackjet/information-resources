package d1.project.api.integration.apimanage.view.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.api.integration.apimanage.view.dao.ApiAuthAppDao;
import d1.project.api.integration.apimanage.view.entity.ApiAuthApp;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author baozh
 */
@Service
public class ApiAuthAppService {
    private final ApiAuthAppDao apiAuthAppDao;

    public ApiAuthAppService(ApiAuthAppDao apiAuthAppDao) {
        this.apiAuthAppDao = apiAuthAppDao;
    }

    /**
     * 根据条件查询
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public Page<ApiAuthApp> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<ApiAuthApp> builder = new SpecificationBuilder<>();
        Specification<ApiAuthApp> specification = builder.init(params)
                .sEqual("apiId", "apiId")
                .sContain("appId", "appId")
                .sContain("appName", "appName")
                .dOrder("createTime").build();
        return apiAuthAppDao.findAll(specification, builder.getPageable());
    }
}
