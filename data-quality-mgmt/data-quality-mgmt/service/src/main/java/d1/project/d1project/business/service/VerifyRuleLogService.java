package d1.project.d1project.business.service;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.d1project.business.dao.VerifyRuleLogDao;
import d1.project.d1project.business.entity.VerifyRuleLog;
import d1.project.d1project.business.model.VerifyRuleLogFindAllVm;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class VerifyRuleLogService {
    private final VerifyRuleLogDao verifyRuleLogDao;
    private final JdbcTemplate jdbcTemplate;

    public VerifyRuleLogService(VerifyRuleLogDao verifyRuleLogDao, JdbcTemplate jdbcTemplate) {
        this.verifyRuleLogDao = verifyRuleLogDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 新增
     *
     * @param model 传输模型
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(VerifyRuleLog model) throws DoValidException {
        VerifyRuleLog verifyRuleLogOpt = verifyRuleLogDao.save(model);
    }


    /**
     * 分页查询所有
     *
     * @param model 查询参数
     */
    public List<Map<String, Object>> findByRuleId(VerifyRuleLogFindAllVm model) throws Exception {
        /*SpecificationBuilder<VerifyRuleLog> builder = new SpecificationBuilder<>();
        Specification<VerifyRuleLog> specification = builder.init((JSONObject) JSON.toJSON(model))
                .sEqual("verifyruleid", "verifyruleid")
                .sContain("verifyruleid", "verifyruleid")
                .build();*/
        String sql = "select * from d1_verify_rule_logs a where verifyruleid = '" + model.getVerifyruleid() + "'";
        List list = jdbcTemplate.queryForList(sql);
        return list;
    }

    /**
     * @param verifyruleid 规则ID
     */
    public Map<String, Object> countByRuleId(String verifyruleid) throws Exception {
        /*SpecificationBuilder<VerifyRuleLog> builder = new SpecificationBuilder<>();
        Specification<VerifyRuleLog> specification = builder.init((JSONObject) JSON.toJSON(model))
                .sEqual("verifyruleid", "verifyruleid")
                .sContain("verifyruleid", "verifyruleid")
                .build();*/
        String sql = "select count(0) count from d1_verify_rule_logs a where verifyruleid = '" + verifyruleid + "'";
        Map map = jdbcTemplate.queryForMap(sql);
        return map;
    }
}
