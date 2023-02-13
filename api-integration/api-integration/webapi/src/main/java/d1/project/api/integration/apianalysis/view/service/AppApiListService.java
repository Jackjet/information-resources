package d1.project.api.integration.apianalysis.view.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.api.integration.apianalysis.view.dao.AppApiListDao;
import d1.project.api.integration.apianalysis.view.entity.AppApiList;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author baozh
 */
@Service
public class AppApiListService {
    private final AppApiListDao appApiListDao;

    public AppApiListService(AppApiListDao appApiListDao) {
        this.appApiListDao = appApiListDao;
    }

    /**
     * 根据条件查询
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public Page<AppApiList> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<AppApiList> builder = new SpecificationBuilder<>();
        Specification<AppApiList> specification = builder.init(params)
                .sContain("appId", "appId")
                .sContain("apiName", "apiName")
                .dOrder("createTime").build();
        return appApiListDao.findAll(specification, builder.getPageable());
    }
}
