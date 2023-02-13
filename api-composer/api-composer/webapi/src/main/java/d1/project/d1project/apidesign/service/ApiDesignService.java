package d1.project.d1project.apidesign.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.d1project.apidesign.dao.ApiDesignDao;
import d1.project.d1project.apidesign.entity.ApiDesign;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * API设计
 *
 * @author baozh
 */
@Service
public class ApiDesignService {
    private final ApiDesignDao apiDesignDao;

    public ApiDesignService(ApiDesignDao apiDesignDao) {
        this.apiDesignDao = apiDesignDao;
    }

    public Page<ApiDesign> findAll(JSONObject params) throws Exception {
        String groupId = params.getString("groupId");
        if("1".equals(groupId)){
            params.put("groupId","");
        }
        SpecificationBuilder<ApiDesign> builder = new SpecificationBuilder<>();
        Specification<ApiDesign> specification = builder.init(params)
                .sContain("name", "name")
                .sNotEqual("status", "status")
                .sEqual("groupId","groupId")
                .sContain("metaKey","metaKey")
                .sContain("metaValue","metaValue")
                .dOrder("updateTime").build();
        return apiDesignDao.findAll(specification, builder.getPageable());
    }

    public ApiDesign findById(String id) {
        Optional<ApiDesign> data = apiDesignDao.findById(id);
        return data.orElse(null);
    }

    public void insert(ApiDesign apiDesign) {
        apiDesignDao.save(apiDesign);
    }

    public void deleteById(String id) {
        apiDesignDao.deleteById(id);
    }

    public boolean existsAllByName(String name) {
        return apiDesignDao.existsAllByName(name);
    }

    public boolean existsAllByNameAndIdNot(String name, String id) {
        return apiDesignDao.existsAllByNameAndIdNot(name, id);
    }

    public boolean existsAllByRequestUrl(String requestUrl) {
        return apiDesignDao.existsAllByRequestUrl(requestUrl);
    }

    public boolean existsAllByRequestUrlAndIdNot(String requestUrl,String id) {
        return apiDesignDao.existsAllByRequestUrlAndIdNot(requestUrl,id);
    }
}
