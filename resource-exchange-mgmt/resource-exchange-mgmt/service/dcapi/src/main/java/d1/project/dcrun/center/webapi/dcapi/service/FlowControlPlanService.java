package d1.project.dcrun.center.webapi.dcapi.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.dcrun.center.webapi.common.util.MyUtils;
import d1.project.dcrun.center.webapi.dcapi.dao.FlowControlPlanDao;
import d1.project.dcrun.center.webapi.dcapi.entity.FlowControlPlan;
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
public class FlowControlPlanService {
    @Autowired
    private FlowControlPlanDao flowControlPlanDao;

    @Autowired
    private TokenService tokenService;

    /**
     * 查询流量控制策略信息
     *
     * @param params 流量控制策略信息查询params
     */
    public Page<FlowControlPlan> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<FlowControlPlan> builder = new SpecificationBuilder<>();
        Specification specification = builder.init(params)
                .sEqual("integrationId", "integrationId")
                .sEqual("sysServiceId", "sysServiceId")
                .sContain("name", "name")
                .sEqual("type", "type")
                .dOrder("createTime").build();

        return flowControlPlanDao.findAll(specification, builder.getPageable());
    }

    /**
     * 添加流量控制策略信息
     *
     * @param params  流量控制策略信息添加params
     * @param request 浏览器请求
     */
    public void insert(JSONObject params, HttpServletRequest request) throws Exception {
        FlowControlPlan data = JSONObject.parseObject(params.toJSONString(), FlowControlPlan.class);
        data.setId(MyUtils.generatePrimaryKeyId());
        beforeInsert(data);
        tokenService.updateCreateIdAndTime(request, data);
        flowControlPlanDao.save(data);
    }

    /**
     * 修改流量控制策略信息
     *
     * @param params  流量控制策略信息修改params
     * @param request 浏览器请求
     */
    public void update(JSONObject params, HttpServletRequest request) throws Exception {
        String id = params.getString("id");
        String name = params.getString("name");
        String type = params.getString("type");
        Integer time = params.getInteger("time");
        Integer apiFlowLimit = params.getInteger("apiFlowLimit");
        Integer appIdFlowLimit = params.getInteger("appIdFlowLimit");
        Integer ipFlowLimit = params.getInteger("ipFlowLimit");
        String description = params.getString("description");

        FlowControlPlan data = this.flowControlPlanDao.findById(id).orElseThrow(() -> new ValidException("找不到这个id对应的资源"));

        data.setName(name);
        data.setType(type);
        data.setTime(time);
        data.setApiFlowLimit(apiFlowLimit);
        data.setAppIdFlowLimit(appIdFlowLimit);
        data.setIpFlowLimit(ipFlowLimit);
        data.setDescription(description);
        beforeUpdate(data);
        tokenService.updateUpdateIdAndTime(request, data);
        flowControlPlanDao.save(data);
    }

    protected void beforeInsert(FlowControlPlan data) throws Exception {
        String integrationId = data.getIntegrationId();
        String sysServiceId = data.getSysServiceId();
        String name = data.getName();
        if (flowControlPlanDao.existsByIntegrationIdAndSysServiceIdAndName(integrationId, sysServiceId, name)) {
            throw new ValidException("访问流量策略名称已存在");
        }
        if (StringUtils.isEmpty(data.getType())) {
            data.setType("0");
        }

        checkApiFlowLimitAndAppIdFlowLimit(data);
    }

    protected void beforeUpdate(FlowControlPlan data) throws Exception {
        String id = data.getId();
        String integrationId = data.getIntegrationId();
        String sysServiceId = data.getSysServiceId();
        String name = data.getName();

        if (flowControlPlanDao.existsByIntegrationIdAndSysServiceIdAndNameAndIdNot(integrationId, sysServiceId, name, id)) {
            throw new ValidException("流量控制策略名称名称已存在");
        }

        if (StringUtils.isEmpty(data.getType())) {
            data.setType("0");
        }

        checkApiFlowLimitAndAppIdFlowLimit(data);
    }

    private void checkApiFlowLimitAndAppIdFlowLimit(FlowControlPlan data) throws Exception {
        Integer apiFlowLimit = data.getApiFlowLimit();

        switch (data.getType()) {
            case "2":
                if (StringUtils.isEmpty(apiFlowLimit)) {
                    throw new ValidException("API流量限制不可为空");
                }
                if (StringUtils.isEmpty(data.getIpFlowLimit())) {
                    throw new ValidException("源ip流量限制不可为空");
                }
                if (apiFlowLimit != -1 && data.getIpFlowLimit() > apiFlowLimit) {
                    throw new ValidException("单个IP流量限制次数不能大于API访问限制次数");
                }
                break;
            case "1":
                if (StringUtils.isEmpty(apiFlowLimit)) {
                    throw new ValidException("API流量限制不可为空");
                }
                if (StringUtils.isEmpty(data.getAppIdFlowLimit())) {
                    throw new ValidException("用户流量限制不可为空");
                }
                if (apiFlowLimit != -1 && data.getAppIdFlowLimit() > apiFlowLimit) {
                    throw new ValidException("单个账号流量限制次数不能大于API访问限制次数");
                }
                break;
            default:
                break;
        }
    }

    public FlowControlPlan findById(String id) {
        Optional<FlowControlPlan> flowControlPlanOptional = flowControlPlanDao.findById(id);
        return flowControlPlanOptional.orElse(null);
    }

    public void delete(String id) {
        flowControlPlanDao.deleteById(id);
    }
}
