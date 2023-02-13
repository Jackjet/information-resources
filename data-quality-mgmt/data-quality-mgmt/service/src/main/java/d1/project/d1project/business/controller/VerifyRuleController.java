package d1.project.d1project.business.controller;

import com.alibaba.fastjson.JSON;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.d1project.business.entity.VerifyRule;
import d1.project.d1project.business.entity.VerifyRuleLog;
import d1.project.d1project.business.model.*;
import d1.project.d1project.business.service.RuleTemplateService;
import d1.project.d1project.business.service.VerifyRuleLogService;
import d1.project.d1project.business.service.VerifyRuleService;
import d1.project.d1project.business.utils.Utils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * 管理端_质量检查规则
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/verifyRule")
public class VerifyRuleController {

    private final VerifyRuleService verifyRuleService;
    private final VerifyRuleLogService verifyRuleLogService;


    public VerifyRuleController(VerifyRuleService verifyRuleService, VerifyRuleLogService verifyRuleLogService) {
        this.verifyRuleService = verifyRuleService;
        this.verifyRuleLogService = verifyRuleLogService;
    }


    /**
     * 分页查询
     */
    @GetMapping(value = "/findAll")
    public Result<Page<VerifyRule>> findAll(VerifyRuleFindAllVm model) throws Exception {
        return ResultUtil.success("", verifyRuleService.findAll(model));
    }

    /**
     * 新增
     */
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody VerifyRuleInsertVm model, HttpServletRequest request) throws Exception {
        verifyRuleService.insert(model, request);
        return ResultUtil.success("");
    }

    /**
     * 删除
     */
    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id) throws DoValidException {
        verifyRuleService.delete(id);
        return ResultUtil.success("");
    }

    /**
     * 更新
     */
    @PutMapping(value = "/update")
    public Result<String> update(@Valid @RequestBody VerifyRuleUpdateVm model, HttpServletRequest request) throws DoValidException {
        try {
            VerifyRuleLog verifyRuleLog = new VerifyRuleLog();
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            verifyRuleLog.setId(uuid);
            verifyRuleLog.setUpdatetime(new Date());
            verifyRuleLog.setVerifyruleid(model.getId());

            VerifyRule entity = verifyRuleService.checkVerifyRule(model.getId());
            entity.setUpdateTime(Calendar.getInstance());
            entity.setUpdateById(Utils.getCurrentUserId(request));

            verifyRuleLog.setUpdatalog(JSON.toJSONString(entity));
//            verifyRuleLog.setUpdatalog(JSON.toJSONString(model)+JSON.toJSONString(entity));
            verifyRuleLogService.insert(verifyRuleLog);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        verifyRuleService.update(model, request);
        return ResultUtil.success("");
    }

    /**
     * 更新规则状态
     */
    @PutMapping(value = "/updateStatus")
    public Result<String> updateStatus(@Valid @RequestBody VerifyRuleStatusUpdateVm model, HttpServletRequest request) throws DoValidException {
        verifyRuleService.updateStatus(model, request);
        return ResultUtil.success("");
    }

}
