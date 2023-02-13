package d1.project.api.integration.apimanage.view.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.api.integration.apimanage.view.dao.GroupApiListDao;
import d1.project.api.integration.apimanage.view.entity.GroupApiList;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author baozh
 */
@Service
public class GroupApiListService {
    private final GroupApiListDao groupApiListDao;

    public GroupApiListService(GroupApiListDao groupApiListDao) {
        this.groupApiListDao = groupApiListDao;
    }

    /**
     * 根据条件查询
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public Page<GroupApiList> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<GroupApiList> builder = new SpecificationBuilder<>();
        Specification<GroupApiList> specification = builder.init(params)
                .sEqual("groupId", "groupId")
                .sContain("name", "name")
                .sContain("methods", "methods")
                .dOrder("updateTime").build();
        return groupApiListDao.findAll(specification, builder.getPageable());
    }
}
