package d1.project.dcrun.center.webapi.common.service.sys.user;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author maoyy
 */
@Service
public class SysUserService {

    private final SysUserDao sysUserDao;

    @Autowired
    public SysUserService(SysUserDao sysUserDao) {
        this.sysUserDao = sysUserDao;
    }

    /**
     * 查询列表
     *
     * @param params 请求参数
     * @return 返回参数
     * @throws Exception 抛出异常
     */
    public Page<SysUser> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<SysUser> builder = new SpecificationBuilder<>();
        Specification specification = builder.init(params)
                .sEqual("integrationId", "integrationId")
                .sContain("name", "name")
                .dOrder("createTime").build();

        return this.sysUserDao.findAll(specification, builder.getPageable());
    }

    public List<SysUser> findAllByIntegrationId(String integrationId) throws Exception {
        return sysUserDao.findAllByIntegrationId(integrationId);
    }

    public Optional<SysUser> findByIntegrationIdAndAppid(String integrationId, String appid) throws Exception {
        return sysUserDao.findByIntegrationIdAndAppid(integrationId, appid);
    }

    public Optional<SysUser> findAllByIntegrationIdAndName(String integrationId, String name) throws Exception {
        return sysUserDao.findAllByIntegrationIdAndName(integrationId, name);
    }

    public Boolean existsById(String id) {
        return sysUserDao.existsById(id);
    }

    public void deleteById(String id) {
        sysUserDao.deleteById(id);
    }

    public SysUser findById(String id) {
        return sysUserDao.findById(id).orElse(null);
    }

    public void insert(SysUser entity) {
        sysUserDao.save(entity);
    }

    public void update(SysUser sysUser) {
        sysUserDao.save(sysUser);
    }
}
