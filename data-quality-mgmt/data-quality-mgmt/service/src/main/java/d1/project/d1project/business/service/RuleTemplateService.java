package d1.project.d1project.business.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.d1project.business.dao.RuleTemplateDao;
import d1.project.d1project.business.entity.RuleTemplate;
import d1.project.d1project.business.model.RuleTemplateFindAllVm;
import d1.project.d1project.business.model.RuleTemplateInsertVm;
import d1.project.d1project.business.model.RuleTemplateStatusUpdateVm;
import d1.project.d1project.business.model.RuleTemplateUpdateVm;
import d1.project.d1project.business.utils.Utils;
import d1.project.d1project.common.utils.BaseUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Optional;

@Service
public class RuleTemplateService {

    private final RuleTemplateDao ruleTemplateDao;

    private final JdbcTemplate jdbcTemplate;

    public RuleTemplateService(RuleTemplateDao ruleTemplateDao, JdbcTemplate jdbcTemplate) {
        this.ruleTemplateDao = ruleTemplateDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 分页查询所有
     *
     * @param model 查询参数
     */
    public Page<RuleTemplate> findAll(RuleTemplateFindAllVm model) throws Exception {
        SpecificationBuilder<RuleTemplate> builder = new SpecificationBuilder<>();
        Specification<RuleTemplate> specification = builder.init((JSONObject) JSON.toJSON(model))
                .sContain("name", "name")
                .sEqual("status", "status")
                .sEqual("type", "type")
                .dOrder("createTime")
                .build();
        return ruleTemplateDao.findAll(specification, builder.getPageable());
    }

    /**
     * 获取对象
     *
     * @param id 查询参数
     */
    public Optional<RuleTemplate> findById(String id) {
        return ruleTemplateDao.findById(id);
    }

    /**
     * 新增
     *
     * @param model 传输模型
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(RuleTemplateInsertVm model, HttpServletRequest request) throws DoValidException {
        RuleTemplate entity = new RuleTemplate();
        BeanUtils.copyProperties(model, entity);

        entity.setId(BaseUtils.generate32Id());
        entity.setType(1);
        entity.setStatus(1);
        entity.setCreateTime(Calendar.getInstance());
        entity.setCreateById(Utils.getCurrentUserId(request));
        ruleTemplateDao.save(entity);
    }

    /**
     * 删除
     *
     * @param id id
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) throws DoValidException {
        checkRuleTemplate(id);
        ruleTemplateDao.deleteById(id);
    }

    /**
     * 更新
     *
     * @param model 传输模型
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(RuleTemplateUpdateVm model, HttpServletRequest request) throws DoValidException {
        RuleTemplate entity = checkRuleTemplate(model.getId());

        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        entity.setUpdateTime(Calendar.getInstance());
        entity.setUpdateById(Utils.getCurrentUserId(request));
        ruleTemplateDao.save(entity);
    }

    public int updateRuleTemplate(RuleTemplateUpdateVm model) throws DoValidException {
        String sql = "UPDATE d1_rule_template SET use_count = " + model.getUseCount() + " WHERE id = '" + model.getId() + "'";
        return jdbcTemplate.update(sql);
    }


    /**
     * 更新状态
     *
     * @param model 传输模型
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(RuleTemplateStatusUpdateVm model, HttpServletRequest request) throws DoValidException {
        RuleTemplate entity = checkRuleTemplate(model.getId());

        entity.setStatus(model.getStatus());
        entity.setUpdateTime(Calendar.getInstance());
        entity.setUpdateById(Utils.getCurrentUserId(request));
        ruleTemplateDao.save(entity);
    }


    /////////////////////
    private RuleTemplate checkRuleTemplate(String id) throws DoValidException {
        RuleTemplate entity = ruleTemplateDao.findById(id).orElseThrow(() -> new DoValidException("规则模板不存在"));

        //内置不能修改
        if (entity.getType() == 0) {
            throw new DoValidException("内置规则模板不可修改");
        }

        return entity;
    }

    public RuleTemplate findOneById(String id, HttpServletRequest request) {
        return ruleTemplateDao.getOne(id);
    }
}
