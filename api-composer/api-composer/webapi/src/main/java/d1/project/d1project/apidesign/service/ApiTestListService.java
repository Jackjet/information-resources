package d1.project.d1project.apidesign.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.d1project.apidesign.dao.ApiTestListDao;
import d1.project.d1project.apidesign.entity.ApiTestList;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author baozh
 */
@Service
public class ApiTestListService {
    private final ApiTestListDao apiTestListDao;

    public ApiTestListService(ApiTestListDao apiTestListDao) {
        this.apiTestListDao = apiTestListDao;
    }

    /**
     * 根据条件查询
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public Page<ApiTestList> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<ApiTestList> builder = new SpecificationBuilder<>();
        Specification<ApiTestList> specification = builder.init(params)
                .sEqual("apiId", "apiId")
                .tBetween("requestTime", "startTime", "endTime", "yyyy-MM-dd HH:mm:ss")
                .dOrder("requestTime").build();
        return apiTestListDao.findAll(specification, builder.getPageable());
    }
}
