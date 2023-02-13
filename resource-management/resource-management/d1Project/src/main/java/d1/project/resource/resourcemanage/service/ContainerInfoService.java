package d1.project.resource.resourcemanage.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.resource.resourcemanage.dao.ContainerInfoDao;
import d1.project.resource.resourcemanage.entity.ContainerInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 容器管理
 *
 * @author baozh
 */
@Service
public class ContainerInfoService {
    private final ContainerInfoDao containerInfoDao;

    public ContainerInfoService(ContainerInfoDao containerInfoDao) {
        this.containerInfoDao = containerInfoDao;
    }

    public Page<ContainerInfo> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<ContainerInfo> builder = new SpecificationBuilder<>();
        Specification<ContainerInfo> specification = builder.init(params)
                .sContain("name", "name")
                .sContain("tagName", "tagName")
                .sContain("tagValue", "tagValue")
                .sEqual("groupId", "groupId")
                .sEqual("type","type")
                .dOrder("updateTime").build();
        return containerInfoDao.findAll(specification, builder.getPageable());
    }

    public ContainerInfo findById(String id) {
        Optional<ContainerInfo> data = containerInfoDao.findById(id);
        return data.orElse(null);
    }

    public void insert(ContainerInfo containerInfo) {
        containerInfoDao.save(containerInfo);
    }

    public void deleteById(String id) {
        containerInfoDao.deleteById(id);
    }

    public boolean existsAllByName(String name) {
        return containerInfoDao.existsAllByName(name);
    }

    public boolean existsAllByIpAndPort(String ip, String port) {
        return containerInfoDao.existsAllByIpAndPort(ip, port);
    }

    public boolean existsAllByNameAndIdNot(String name, String id) {
        return containerInfoDao.existsAllByNameAndIdNot(name, id);
    }

    public boolean existsAllByIpAndPortAndIdNot(String ip, String port, String id) {
        return containerInfoDao.existsAllByIpAndPortAndIdNot(ip, port, id);
    }

    public boolean existsAllByGroupId(String groupId) {
        return containerInfoDao.existsAllByGroupId(groupId);
    }

    public void save (ContainerInfo containerInfo) {
        containerInfoDao.save(containerInfo);
    }
}
