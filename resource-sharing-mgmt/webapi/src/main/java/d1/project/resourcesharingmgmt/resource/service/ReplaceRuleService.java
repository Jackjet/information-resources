package d1.project.resourcesharingmgmt.resource.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenManager;
import d1.project.resourcesharingmgmt.common.model.OperationLog;
import d1.project.resourcesharingmgmt.common.service.IOperationLogService;
import d1.project.resourcesharingmgmt.common.utils.BaseUtils;
import d1.project.resourcesharingmgmt.resource.dao.ReplaceRuleDao;
import d1.project.resourcesharingmgmt.resource.entity.ReplaceRuleEntity;
import d1.project.resourcesharingmgmt.resource.model.ReplaceRule.ReplaceRuleFindAllListVm;
import d1.project.resourcesharingmgmt.resource.model.ReplaceRule.ReplaceRuleFindAllVm;
import d1.project.resourcesharingmgmt.resource.model.ReplaceRule.ReplaceRuleInsertVm;
import d1.project.resourcesharingmgmt.resource.model.ReplaceRule.ReplaceRuleUpdateVm;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * 脱敏规则
 *
 * @author zhengyang
 * @date 2021-11-05 16:17
 */
@Service
public class ReplaceRuleService {
    private final ReplaceRuleDao replaceRuleDao;
    private final IOperationLogService operationLogService;

    public ReplaceRuleService(ReplaceRuleDao replaceRuleDao, IOperationLogService operationLogService) {
        this.replaceRuleDao = replaceRuleDao;
        this.operationLogService = operationLogService;
    }

    public void insert(ReplaceRuleInsertVm model, HttpServletRequest request) throws DoValidException {
        if (replaceRuleDao.existsByName(model.getName())) {
            throw new DoValidException("规则已存在");
        }
        ReplaceRuleEntity entity = new ReplaceRuleEntity();
        BeanUtils.copyProperties(model, entity);
        entity.setId(BaseUtils.generate32Id());
        entity.setSqlValue(createSql(model));
        entity.setHasSystem("0");
        TokenManager.getInstance().updateCreateIdAndTime(request, entity);

        replaceRuleDao.save(entity);

        operationLogService.insert(new OperationLog("纠错管理_新增纠错", "新增纠错", "新增纠错" + entity.getId(), JSON.toJSONString(entity), 1), request);
    }

    /**
     * 更新
     *
     * @param model
     * @param request
     * @throws DoValidException
     */
    public void update(ReplaceRuleUpdateVm model, HttpServletRequest request) throws DoValidException {
        ReplaceRuleEntity entity = replaceRuleDao.findById(model.getId()).orElseThrow(() -> new DoValidException("规则不存在"));
        if (replaceRuleDao.existsByNameAndIdNot(model.getName(), entity.getId())) {
            throw new DoValidException("规则已存在");
        }
        if("1".equals(entity.getHasSystem())){
            throw new DoValidException("系统初始化数据不允许修改");
        }

        BeanUtils.copyProperties(model, entity);
        entity.setSqlValue(createSql(model));
        TokenManager.getInstance().updateUpdateIdAndTime(request, entity);

        replaceRuleDao.save(entity);
        operationLogService.insert(new OperationLog("纠错管理_审核", "审核", "审核" + entity.getId(), JSON.toJSONString(entity), 1), request);
    }

    /**
     * 详情
     *
     * @param id id
     */
    public Optional<ReplaceRuleEntity> find(String id) {
        return replaceRuleDao.findById(id);
    }

    /**
     * 查询所有
     *
     * @param model
     * @return
     * @throws Exception
     */
    public Page<ReplaceRuleEntity> findAll(ReplaceRuleFindAllVm model) throws Exception {
        SpecificationBuilder<ReplaceRuleEntity> builder = new SpecificationBuilder<>();
        JSONObject paramObject = (JSONObject) JSON.toJSON(model);
        Specification<ReplaceRuleEntity> specification = builder.init(paramObject)
                .sContain("name", "name")
                .sEqual("hasSystem", "hasSystem")
                .dOrder("createTime")
                .build();
        return replaceRuleDao.findAll(specification, builder.getPageable());
    }

    /**
     * 查询所有
     *
     * @param model
     * @return
     * @throws Exception
     */
    public List<ReplaceRuleEntity> findAllList(ReplaceRuleFindAllListVm model) throws Exception {
        SpecificationBuilder<ReplaceRuleEntity> builder = new SpecificationBuilder<>();
        JSONObject paramObject = (JSONObject) JSON.toJSON(model);
        Specification<ReplaceRuleEntity> specification = builder.init(paramObject)
                .sContain("name", "name")
                .dOrder("createTime")
                .build();
        return replaceRuleDao.findAll(specification);
    }

    /**
     * 删除纠错规则
     *
     * @param id
     * @throws DoValidException
     */
    public void delete(String id) throws DoValidException {
        ReplaceRuleEntity entity = replaceRuleDao.findById(id).orElseThrow(() -> new DoValidException("规则不存在"));
        if("1".equals(entity.getHasSystem())){
            throw new DoValidException("系统初始化数据不允许修改");
        }
        replaceRuleDao.deleteById(id);
    }

    private String createSql(ReplaceRuleInsertVm model) {
        JSONObject sql = new JSONObject();
        String mysql = "";
        String postgre = "";
        String oracle = "";
        String sqlserver = "";
        String dameng = "";

        //判断类型（1替换位数，2从字符开始替换）
        if ("1".equals(model.getIndexType())) {
            mysql = "CONCAT(LEFT(field_name_xxx," + model.getIndexOf() + "), '" + model.getReplaceValue() + "' ,RIGHT(field_name_xxx," + model.getLastIndexOf() + ")) AS field_name_xxx";
            postgre = "CONCAT(LEFT(field_name_xxx," + model.getIndexOf() + "), '" + model.getReplaceValue() + "' ,RIGHT(field_name_xxx," + model.getLastIndexOf() + ")) AS field_name_xxx";
            oracle = "LEFT(field_name_xxx," + model.getIndexOf() + ") || '" + model.getReplaceValue() + "' || RIGHT(field_name_xxx," + model.getLastIndexOf() + ") AS field_name_xxx";
            sqlserver = "LEFT(field_name_xxx," + model.getIndexOf() + ") + '" + model.getReplaceValue() + "' + RIGHT(field_name_xxx," + model.getLastIndexOf() + ") AS field_name_xxx";
            dameng = "LEFT(field_name_xxx," + model.getIndexOf() + ") || '" + model.getReplaceValue() + "' || RIGHT(field_name_xxx," + model.getLastIndexOf() + ") AS field_name_xxx";
        } else {
            mysql = "CONCAT(SUBSTRING_INDEX(field_name_xxx,'" + model.getIndexOf() + "',1),'" + model.getReplaceValue() + "',SUBSTRING_INDEX(field_name_xxx,'" + model.getLastIndexOf() + "',-1)) AS field_name_xxx";
            postgre = "CONCAT(SPLIT_PART(field_name_xxx,'" + model.getIndexOf() + "',1),'" + model.getReplaceValue() + "',SPLIT_PART(field_name_xxx,'" + model.getLastIndexOf() + "',2)) AS field_name_xxx";
            oracle = "SUBSTR(field_name_xxx,1, INSTR(field_name_xxx,'" + model.getIndexOf() + "',-1)-1) || '" + model.getReplaceValue() + "' || SUBSTR(field_name_xxx, INSTR(field_name_xxx,'" + model.getIndexOf() + "',-1)+1) AS field_name_xxx";
            sqlserver = "REPLACE(field_name_xxx,SUBSTRING(field_name_xxx,CHARINDEX('" + model.getIndexOf() + "',field_name_xxx), len(field_name_xxx)-CHARINDEX('" + model.getLastIndexOf() + "',field_name_xxx)+1), '" + model.getReplaceValue() + "') AS field_name_xxx";
            dameng = "SUBSTR(field_name_xxx,1, INSTR(field_name_xxx,'" + model.getIndexOf() + "',-1)-1) || '" + model.getReplaceValue() + "' || SUBSTR(field_name_xxx, INSTR(field_name_xxx,'" + model.getIndexOf() + "',-1)+1) AS field_name_xxx";
        }

        sql.put("Mysql", mysql);
        sql.put("Postgresql", postgre);
        sql.put("Oracle", oracle);
        sql.put("SqlServer", sqlserver);
        sql.put("Dameng", dameng);
        return sql.toJSONString();
    }
}
