package d1.project.api.integration.apianalysis.view.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.api.integration.apianalysis.view.dao.AppAnalysisListDao;
import d1.project.api.integration.apianalysis.view.entity.AppAnalysisList;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author baozh
 */
@Service
public class AppAnalysisListService {
    private final AppAnalysisListDao appAnalysisListDao;

    public AppAnalysisListService(AppAnalysisListDao appAnalysisListDao) {
        this.appAnalysisListDao = appAnalysisListDao;
    }

    /**
     * 根据条件查询
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public Page<AppAnalysisList> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<AppAnalysisList> builder = new SpecificationBuilder<>();
        Specification<AppAnalysisList> specification = builder.init(params)
                .sContain("id", "id")
                .sContain("appName", "appName")
                .dOrder("createTime").build();
        return appAnalysisListDao.findAll(specification, builder.getPageable());
    }
}
