package d1.project.dcrun.center.webapi.system.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.dcrun.center.webapi.common.service.management.config.ManagementConfig;
import d1.project.dcrun.center.webapi.common.service.management.config.ManagementConfigService;
import d1.project.dcrun.center.webapi.common.util.MyUtils;
import d1.project.dcrun.center.webapi.system.dao.TenantsDao;
import d1.project.dcrun.center.webapi.system.entity.Tenants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author libin
 */
@Service
public class TenantsService {
    @Autowired
    TokenService tokenService;
    @Autowired
    private TenantsDao tenantsDao;
    @Autowired
    private ManagementConfigService managementConfigService;

    /**
     * 根据租户名称分页查询
     *
     * @param name     租户名称
     * @param page     页码
     * @param size 页的大小
     * @return 租户信息
     * @throws Exception 分页查询错误信息
     */
    public Page<Tenants> findAllByName(String name, Integer page, Integer size) throws Exception {
        JSONObject params = new JSONObject();
        params.put("name", name);
        params.put("page", page);
        params.put("size", size);

        SpecificationBuilder<Tenants> builder = new SpecificationBuilder<Tenants>();
        Specification specification = builder.init(params)
                .sContain("name", "name")
                .dOrder("createTime").build();

        Page result = this.tenantsDao.findAll(specification, builder.getPageable());
        return result;
    }

    /**
     * 创建租户信息
     *
     * @param request
     * @param params  租户信息
     * @throws Exception 租户信息校验
     */
    public void insert(HttpServletRequest request, JSONObject params) throws Exception {

        // 自定义配置不允许为空
        List<ManagementConfig> manageConfigServiceList = managementConfigService.findAll();
        for (ManagementConfig config : manageConfigServiceList) {
            if (StringUtils.isEmpty(config.getConfigValue())) {
                throw new ValidException("请先填写\"其他服务配置\"信息！");
            }
        }

        String name = params.getString("name");
        Integer integrationProjectAmount = params.getInteger("integrationProjectAmount");
        String remark = params.getString("remark");

        Tenants tenants = this.tenantsDao.findFirstByName(name);
        if (tenants != null) {
            throw new ValidException("租户名称已存在");
        }

        tenants = new Tenants();
        tenants.setName(name);
        tenants.setIntegrationProjectAmount(integrationProjectAmount);
        tenants.setRemark(remark);

        String password = "123456";
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        tenants.setPassword(password);
        tenants.setId(MyUtils.generatePrimaryKeyId());
        tokenService.updateCreateIdAndTime(request, tenants);
        tenantsDao.save(tenants);
    }

    /**
     * 修改租户信息
     *
     * @param request
     * @param params  租户信息
     * @throws Exception 租户信息校验
     */
    public void update(HttpServletRequest request, JSONObject params) throws Exception {
        String id = params.getString("id");
        Integer integrationProjectAmount = params.getInteger("integrationProjectAmount");
        String remark = params.getString("remark");

        Tenants tenants = this.tenantsDao.findById(id).orElseThrow(() -> new ValidException("您所查询的租户信息不存在"));

        tenants.setIntegrationProjectAmount(integrationProjectAmount);
        tenants.setRemark(remark);
        tokenService.updateUpdateIdAndTime(request, tenants);
        tenantsDao.save(tenants);
    }

    /**
     * 重置密码
     *
     * @param request
     * @param id      唯一标识
     * @throws Exception 租户信息校验
     */
    public void resetPassword(HttpServletRequest request, String id) throws Exception {
        if (StringUtils.isEmpty(id)) {
            throw new ValidException("唯一标识不能为空");
        }

        Tenants tenants = this.tenantsDao.findById(id).orElseThrow(() -> new ValidException("您所重置的租户信息不存在"));

        String password = "123456";
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        tenants.setPassword(password);
        tokenService.updateUpdateIdAndTime(request, tenants);
        tenantsDao.save(tenants);
    }

    public Tenants findById(String id) {
        return tenantsDao.findById(id).orElse(null);
    }

    public void delete(String id) {
        tenantsDao.deleteById(id);
    }
}
