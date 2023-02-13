package d1.project.resource.resourcemanage.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.resource.resourcemanage.dao.DataSourceInfoDao;
import d1.project.resource.resourcemanage.entity.DataSourceInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author baozh
 */
@Service
public class DataSourceInfoService {
    private final DataSourceInfoDao dataSourceInfoDao;

    public DataSourceInfoService(DataSourceInfoDao dataSourceInfoDao) {
        this.dataSourceInfoDao = dataSourceInfoDao;
    }

    public Page<DataSourceInfo> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<DataSourceInfo> builder = new SpecificationBuilder<>();
        Specification<DataSourceInfo> specification = builder.init(params)
                .sContain("name", "name")
                .sContain("tagName", "tagName")
                .sContain("tagValue", "tagValue")
                .sEqual("groupId", "groupId")
                .dOrder("updateTime").build();
        return dataSourceInfoDao.findAll(specification, builder.getPageable());
    }

    public DataSourceInfo findById(String id) {
        Optional<DataSourceInfo> data = dataSourceInfoDao.findById(id);
        return data.orElse(null);
    }

    public void insert(DataSourceInfo dataSourceInfo) {
        dataSourceInfoDao.save(dataSourceInfo);
    }

    public void deleteById(String id) {
        dataSourceInfoDao.deleteById(id);
    }

    public boolean existsAllByName(String name) {
        return dataSourceInfoDao.existsAllByName(name);
    }

    public boolean existsAllByHostAndPortAndDataName(String host, String port, String dataName) {
        return dataSourceInfoDao.existsAllByHostAndPortAndDataName(host, port, dataName);
    }

    public boolean existsAllByNameAndIdNot(String name, String id) {
        return dataSourceInfoDao.existsAllByNameAndIdNot(name, id);
    }

    public boolean existsAllByHostAndPortAndDataNameAndIdNot(String host, String port, String dataName, String id) {
        return dataSourceInfoDao.existsAllByHostAndPortAndDataNameAndIdNot(host, port, dataName, id);
    }

    public boolean existsAllByGroupId(String groupId) {
        return dataSourceInfoDao.existsAllByGroupId(groupId);
    }
}
