package d1.project.api.integration.apimanage.view.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.api.integration.apimanage.view.dao.ApiGroupListDao;
import d1.project.api.integration.apimanage.view.entity.ApiGroupList;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author baozh
 */
@Service
public class ApiGroupListService {
    private final ApiGroupListDao apiGroupListDao;

    public ApiGroupListService(ApiGroupListDao apiGroupListDao) {
        this.apiGroupListDao = apiGroupListDao;
    }

    /**
     * 根据条件查询
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public Page<ApiGroupList> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<ApiGroupList> builder = new SpecificationBuilder<>();
        Specification<ApiGroupList> specification = builder.init(params)
                .sContain("name", "name")
                .sContain("userName", "userName")
                .dOrder("updateTime").build();
        return apiGroupListDao.findAll(specification, builder.getPageable());
    }
}
