package d1.project.d1project.business.controller;

import com.alibaba.fastjson.JSON;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.d1project.business.entity.VerifyRule;
import d1.project.d1project.business.entity.VerifyRuleLog;
import d1.project.d1project.business.model.VerifyRuleFindAllVm;
import d1.project.d1project.business.model.VerifyRuleLogFindAllVm;
import d1.project.d1project.business.service.VerifyRuleLogService;
import d1.project.d1project.business.service.VerifyRuleService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 管理端_质量检查规则变更记录
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/verifyRuleLog")
public class VerifyRuleLogController {
    private final VerifyRuleLogService verifyRuleLogService;

    public VerifyRuleLogController(VerifyRuleLogService verifyRuleLogService) {
        this.verifyRuleLogService = verifyRuleLogService;
    }

    /**
     * 分页查询
     */
    @GetMapping(value = "/findByRuleId")
    public Result<List<VerifyRuleLog>> findByRuleId(VerifyRuleLogFindAllVm model) throws Exception {
        System.out.println("------------------------" + JSON.toJSONString(verifyRuleLogService.findByRuleId(model)));
        return ResultUtil.success("", verifyRuleLogService.findByRuleId(model));
    }

    /**
     * 分页查询
     */
    @GetMapping(value = "/countByRuleId")
    public Result<List<VerifyRuleLog>> countByRuleId(@RequestParam(value = "verifyruleid", required = true) String verifyruleid) throws Exception {
//        System.out.println("------------------------" + JSON.toJSONString(verifyRuleLogService.findByRuleId(verifyruleid)));
        return ResultUtil.success("", verifyRuleLogService.countByRuleId(verifyruleid));
    }
}
