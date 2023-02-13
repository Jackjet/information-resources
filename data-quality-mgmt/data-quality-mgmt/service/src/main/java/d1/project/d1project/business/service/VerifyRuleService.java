package d1.project.d1project.business.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.d1project.business.dao.RuleTemplateDao;
import d1.project.d1project.business.dao.TaskRuleDao;
import d1.project.d1project.business.dao.VerifyRuleDao;
import d1.project.d1project.business.entity.RuleTemplate;
import d1.project.d1project.business.entity.TaskRule;
import d1.project.d1project.business.entity.VerifyRule;
import d1.project.d1project.business.factory.SqlTemplateFactory;
import d1.project.d1project.business.model.VerifyRuleFindAllVm;
import d1.project.d1project.business.model.VerifyRuleInsertVm;
import d1.project.d1project.business.model.VerifyRuleStatusUpdateVm;
import d1.project.d1project.business.model.VerifyRuleUpdateVm;
import d1.project.d1project.business.model.template.MetadataDataModel;
import d1.project.d1project.business.service.template.base.SqlTemplateService;
import d1.project.d1project.business.utils.DatabaseType;
import d1.project.d1project.business.utils.Utils;
import d1.project.d1project.common.service.IVerifyRuleService;
import d1.project.d1project.common.utils.BaseUtils;
import org.apache.http.util.TextUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class VerifyRuleService implements IVerifyRuleService {

    private final VerifyRuleDao verifyRuleDao;
    private final RuleTemplateDao ruleTemplateDao;
    private final TaskRuleDao taskRuleDao;
    private final SqlTemplateFactory sqlTemplateFactory;
    private final JdbcTemplate jdbcTemplate;

    public VerifyRuleService(VerifyRuleDao verifyRuleDao, RuleTemplateDao ruleTemplateDao, TaskRuleDao taskRuleDao, JdbcTemplate jdbcTemplate) {
        this.verifyRuleDao = verifyRuleDao;
        this.ruleTemplateDao = ruleTemplateDao;
        this.taskRuleDao = taskRuleDao;
        this.sqlTemplateFactory = SqlTemplateFactory.getInstance();
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 分页查询所有
     *
     * @param model 查询参数
     */
    @Override
    public Page<VerifyRule> findAll(VerifyRuleFindAllVm model) throws Exception {
        SpecificationBuilder<VerifyRule> builder = new SpecificationBuilder<>();
        Specification<VerifyRule> specification = builder.init((JSONObject) JSON.toJSON(model))
                .sContain("name", "name")
                .sContain("ruleTemplateName", "ruleTemplateName")
                .sEqual("status", "status")
                .dOrder("createTime")
                .build();
        return verifyRuleDao.findAll(specification, builder.getPageable());
    }

    /**
     * 新增
     *
     * @param model 传输模型
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(VerifyRuleInsertVm model, HttpServletRequest request) throws Exception {
        RuleTemplate ruleTemplate = ruleTemplateDao.findById(model.getRuleTemplateId()).orElseThrow(() -> new DoValidException("规则模板不存在"));

        //判断规则名称是否重复
        Optional<VerifyRule> verifyRuleOpt = verifyRuleDao.findFirstByName(model.getName());
        if (verifyRuleOpt.isPresent()) {
            throw new DoValidException("名称:“" + model.getName() + "”已经存在!");
        }

        VerifyRule entity = new VerifyRule();
        BeanUtils.copyProperties(model, entity);

        entity.setId(BaseUtils.generate32Id());
        entity.setRuleTemplateName(ruleTemplate.getName());
        entity.setStatus(1);
        entity.setSql(getVerifyRuleSql(ruleTemplate.getCode(), model.getMetadataData()));
        entity.setCreateTime(Calendar.getInstance());
        entity.setCreateById(Utils.getCurrentUserId(request));
        entity.setCreateByName(Utils.getCurrentUserName(request));
        verifyRuleDao.save(entity);
    }


    /**
     * 删除
     *
     * @param id id
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) throws DoValidException {
        //判断是否关联任务，关联任务不让删除
        Optional<TaskRule> taskRuleOpt = taskRuleDao.findFirstByRuleId(id);
        if (taskRuleOpt.isPresent()) {
            throw new DoValidException("已有关联任务,不能删除");
        }
        verifyRuleDao.deleteById(id);
    }

    /**
     * 更新
     *
     * @param model 传输模型
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(VerifyRuleUpdateVm model, HttpServletRequest request) throws DoValidException {
        VerifyRule entity = checkVerifyRule(model.getId());

        entity.setDescription(model.getDescription());
        entity.setUpdateTime(Calendar.getInstance());
        entity.setUpdateById(Utils.getCurrentUserId(request));
        verifyRuleDao.save(entity);
    }

    /**
     * 更新状态
     *
     * @param model 传输模型
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(VerifyRuleStatusUpdateVm model, HttpServletRequest request) throws DoValidException {
        VerifyRule entity = checkVerifyRule(model.getId());

        entity.setStatus(model.getStatus());
        entity.setUpdateTime(Calendar.getInstance());
        entity.setUpdateById(Utils.getCurrentUserId(request));
        verifyRuleDao.save(entity);
    }


    /////////////////////
    public VerifyRule checkVerifyRule(String id) throws DoValidException {
        return verifyRuleDao.findById(id).orElseThrow(() -> new DoValidException("规则不存在"));
    }


    //获取检查规则sql语句
    private String getVerifyRuleSql(String code, String metadataData) throws Exception {
        if (TextUtils.isEmpty(metadataData) || TextUtils.isEmpty(code)) {
            throw new DoValidException("getVerifyRuleSql fail, metadataData or code is null");
        }

        MetadataDataModel metadataDataModel = JSONObject.parseObject(metadataData, MetadataDataModel.class);
        //获取数据库类型
        String type = metadataDataModel.getType();
        if (!DatabaseType.check(type)) {
            throw new DoValidException("不支持的数据库类型:" + type + ",目前支持的数据库类型‘" + String.join(",", DatabaseType.getNames()) + "’");
        }

        SqlTemplateService sqlTemplateService = sqlTemplateFactory.getSqlTemplateService(type);
        return sqlTemplateService.getSql(code, metadataDataModel);
    }

    @Override
    public List<VerifyRule> findAllByIdIn(List<String> ids) {
        return verifyRuleDao.findAllByIdIn(ids);
    }

    /**
     * 获取模板引用次数
     */
    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> getRuleTemplateCount() throws DoValidException {
        String sql = "select a.rule_template_id,count(a.rule_template_id) as use_count from d1_verify_rule a GROUP BY a.rule_template_id";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }
}
