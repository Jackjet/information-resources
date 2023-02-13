package d1.project.dcrun.center.webapi.dcapi.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.dcrun.center.webapi.common.util.MyUtils;
import d1.project.dcrun.center.webapi.dcapi.dao.AccessControlPlanDao;
import d1.project.dcrun.center.webapi.dcapi.entity.AccessControlPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author lin
 */
@Service
public class AccessControlPlanService {
    @Autowired
    private AccessControlPlanDao accessControlPlanDao;
    @Autowired
    private TokenService tokenService;

    /**
     * 查询访问控制策略信息
     *
     * @param params 访问控制策略信息查询params
     */
    public Page<AccessControlPlan> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<AccessControlPlan> builder = new SpecificationBuilder<>();
        Specification specification = builder.init(params)
                .sEqual("integrationId", "integrationId")
                .sEqual("sysServiceId", "sysServiceId")
                .sContain("name", "name")
                .sEqual("type", "type")
                .sEqual("operation", "operation")
                .dOrder("createTime").build();

        Page result = accessControlPlanDao.findAll(specification, builder.getPageable());
        return result;
    }

    /**
     * 添加访问控制策略信息
     *
     * @param params  访问控制策略信息添加params
     * @param request 浏览器请求
     */
    public void insert(JSONObject params, HttpServletRequest request) throws Exception {
        AccessControlPlan data = JSONObject.parseObject(params.toJSONString(), AccessControlPlan.class);
        // ip地址去除两边空格
        data.setIps(MyUtils.eliminateSpaces(data.getIps()));

        data.setId(MyUtils.generatePrimaryKeyId());
        beforeInsert(data);
        tokenService.updateCreateIdAndTime(request, data);
        accessControlPlanDao.save(data);
    }

    /**
     * 修改访问控制策略信息
     *
     * @param params  访问控制策略信息修改params
     * @param request 浏览器请求
     */
    public void update(JSONObject params, HttpServletRequest request) throws Exception {
        String id = params.getString("id");
        String name = params.getString("name");
        String type = params.getString("type");
        String operation = params.getString("operation");
        String ips = MyUtils.eliminateSpaces(params.getString("ips"));
        String appIds = params.getString("appIds");

        AccessControlPlan data = this.accessControlPlanDao.findById(id).orElseThrow(() -> new ValidException("访问控制策略信息不存在"));

        data.setName(name);
        data.setType(type);
        data.setOperation(operation);
        data.setIps(ips);
        data.setAppIds(appIds);
        beforeUpdate(data);
        tokenService.updateUpdateIdAndTime(request, data);
        accessControlPlanDao.save(data);
    }

    protected void beforeInsert(AccessControlPlan data) throws Exception {
        if (accessControlPlanDao.existsByNameAndSysServiceId(data.getName(), data.getSysServiceId())) {
            throw new ValidException("访问控制策略名称已存在");
        }

        checkIpsAndAppIds(data);
    }

    protected void beforeUpdate(AccessControlPlan data) throws Exception {
        String id = data.getId();
        if (StringUtils.isEmpty(id) || !accessControlPlanDao.existsById(id)) {
            throw new ValidException("id不能为空或找不到这个id对应的资源");
        }

        if (accessControlPlanDao.existsByNameAndSysServiceIdAndIdNot(data.getName(), data.getSysServiceId(), data.getId())) {
            throw new ValidException("访问控制策略名称已存在");
        }

        checkIpsAndAppIds(data);
    }

    private void checkIpsAndAppIds(AccessControlPlan data) throws Exception {
        if ("0".equals(data.getType()) && StringUtils.isEmpty(data.getIps())) {
            throw new ValidException("Ip地址不可为空");
        }

        if ("1".equals(data.getType()) && StringUtils.isEmpty(data.getAppIds())) {
            throw new ValidException("账户号不可为空");
        }
    }

    public AccessControlPlan findById(String id) {
        Optional<AccessControlPlan> accessControlPlanOptional = accessControlPlanDao.findById(id);
        return accessControlPlanOptional.orElse(null);
    }

    public void delete(String id) {
        accessControlPlanDao.deleteById(id);
    }
}
