package d1.project.api.integration.apimanage.view.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.api.integration.apimanage.view.dao.ApiWithSourceApiDao;
import d1.project.api.integration.apimanage.view.entity.ApiWithSourceApi;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author baozh
 */
@Service
public class ApiWithSourceApiService {

    private final ApiWithSourceApiDao apiWithSourceApiDao;

    public ApiWithSourceApiService(ApiWithSourceApiDao apiWithSourceApiDao) {
        this.apiWithSourceApiDao = apiWithSourceApiDao;
    }

    /**
     * 根据条件查询
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public Page<ApiWithSourceApi> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<ApiWithSourceApi> builder = new SpecificationBuilder<>();
        Specification<ApiWithSourceApi> specification = builder.init(params)
                .sContain("name", "name")
                .sContain("sourceName", "sourceName")
                .sIn("id","associationsIds")
                .sEqual("container","container")
                .sEqual("groupId", "groupId")
                .sEqual("status","status")
                .dOrder("updateTime").build();
        return apiWithSourceApiDao.findAll(specification, builder.getPageable());
    }

    /**
     * 根据条件查询
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public Page<ApiWithSourceApi> findByIdIn(JSONObject params) throws Exception {
        SpecificationBuilder<ApiWithSourceApi> builder = new SpecificationBuilder<>();
        Specification<ApiWithSourceApi> specification = builder.init(params)
                .sContain("apiName", "apiName")
                .sContain("sourceApiName", "sourceApiName")
                .sNotIn("id", "id")
                .dOrder("updateTime").build();
        return apiWithSourceApiDao.findAll(specification, builder.getPageable());
    }


}
