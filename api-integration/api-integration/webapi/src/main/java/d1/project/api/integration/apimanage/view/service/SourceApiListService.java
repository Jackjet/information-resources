package d1.project.api.integration.apimanage.view.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.api.integration.apimanage.view.dao.SourceApiListDao;
import d1.project.api.integration.apimanage.view.entity.SourceApiList;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author baozh
 */
@Service
public class SourceApiListService {
    private final SourceApiListDao sourceApiListDao;

    public SourceApiListService(SourceApiListDao sourceApiListDao) {
        this.sourceApiListDao = sourceApiListDao;
    }

    /**
     * 根据条件查询
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public Page<SourceApiList> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<SourceApiList> builder = new SpecificationBuilder<>();
        Specification<SourceApiList> specification = builder.init(params)
                .sContain("sourceApiName", "sourceApiName")
                .dOrder("createTime").build();
        return sourceApiListDao.findAll(specification, builder.getPageable());
    }
}
