package d1.project.api.integration.apianalysis.view.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.api.integration.apianalysis.view.dao.ApiAnalysisListDao;
import d1.project.api.integration.apianalysis.view.entity.ApiAnalysisList;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author baozh
 */

@Service
public class ApiAnalysisListService {

    private final ApiAnalysisListDao apiAnalysisListDao;

    public ApiAnalysisListService(ApiAnalysisListDao apiAnalysisListDao) {
        this.apiAnalysisListDao = apiAnalysisListDao;
    }

    /**
     * 根据条件查询
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public Page<ApiAnalysisList> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<ApiAnalysisList> builder = new SpecificationBuilder<>();
        Specification<ApiAnalysisList> specification = builder.init(params)
                .sContain("apiName", "apiName")
                .sContain("routeInfo", "routeInfo")
                .dOrder("createTime").build();
        return apiAnalysisListDao.findAll(specification, builder.getPageable());
    }
}
