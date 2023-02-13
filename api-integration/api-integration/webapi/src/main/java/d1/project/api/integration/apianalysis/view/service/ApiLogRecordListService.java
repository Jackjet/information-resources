package d1.project.api.integration.apianalysis.view.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.api.integration.apianalysis.view.dao.ApiLogRecordListDao;
import d1.project.api.integration.apianalysis.view.entity.ApiLogRecordList;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author baozh
 */
@Service
public class ApiLogRecordListService {
    private final ApiLogRecordListDao apiLogRecordListDao;

    public ApiLogRecordListService(ApiLogRecordListDao apiLogRecordListDao) {
        this.apiLogRecordListDao = apiLogRecordListDao;
    }

    /**
     * 根据条件查询
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public Page<ApiLogRecordList> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<ApiLogRecordList> builder = new SpecificationBuilder<>();
        Specification<ApiLogRecordList> specification = builder.init(params)
                .sEqual("appId", "appId")
                .sEqual("apiId", "apiId")
                .sContain("appName", "appName")
                .sContain("apiName", "apiName")
                .tBetween("requestTime", "startTime", "endTime", "yyyy-MM-dd HH:mm:ss")
                .dOrder("requestTime").build();
        return apiLogRecordListDao.findAll(specification, builder.getPageable());
    }
}
