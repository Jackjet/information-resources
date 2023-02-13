package d1.project.api.integration.apianalysis.view.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.api.integration.apianalysis.view.dao.ApiAppListDao;
import d1.project.api.integration.apianalysis.view.entity.ApiAppList;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author baozh
 */
@Service
public class ApiAppListService {
    private final ApiAppListDao apiAppListDao;

    public ApiAppListService(ApiAppListDao apiAppListDao) {
        this.apiAppListDao = apiAppListDao;
    }

    /**
     * 根据条件查询
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public Page<ApiAppList> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<ApiAppList> builder = new SpecificationBuilder<>();
        Specification<ApiAppList> specification = builder.init(params)
                .sEqual("apiId", "apiId")
                .sContain("appName", "appName")
                .sContain("appId", "appId")
                .dOrder("createTime").build();
        return apiAppListDao.findAll(specification, builder.getPageable());
    }
}
