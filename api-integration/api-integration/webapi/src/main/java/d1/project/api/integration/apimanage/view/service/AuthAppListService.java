package d1.project.api.integration.apimanage.view.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.api.integration.apimanage.view.dao.AuthAppListDao;
import d1.project.api.integration.apimanage.view.entity.AuthAppList;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author baozh
 */

@Service
public class AuthAppListService {
    private final AuthAppListDao authAppListDao;

    public AuthAppListService(AuthAppListDao authAppListDao) {
        this.authAppListDao = authAppListDao;
    }

    /**
     * 根据条件查询
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public Page<AuthAppList> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<AuthAppList> builder = new SpecificationBuilder<>();
        Specification<AuthAppList> specification = builder.init(params)
                .sContain("id", "id")
                .sContain("appName", "appName")
                .dOrder("createTime").build();
        return authAppListDao.findAll(specification, builder.getPageable());
    }
}
