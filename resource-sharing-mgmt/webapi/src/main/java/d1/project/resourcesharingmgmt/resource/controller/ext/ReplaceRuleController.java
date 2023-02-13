package d1.project.resourcesharingmgmt.resource.controller.ext;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.entity.ReplaceRuleEntity;
import d1.project.resourcesharingmgmt.resource.model.ReplaceRule.ReplaceRuleFindAllListVm;
import d1.project.resourcesharingmgmt.resource.service.ReplaceRuleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 对外接口-脱敏规则
 * @author zhengyang
 */
@Auth("noauth")
@RestController("/ext/replaceRule")
@RequestMapping("/ext/replaceRule")
public class ReplaceRuleController {
    private final ReplaceRuleService replaceRuleService;

    public ReplaceRuleController(ReplaceRuleService replaceRuleService){
        this.replaceRuleService = replaceRuleService;
    }

    /**
     * 查询所有
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/findAllList")
    public Result<List<ReplaceRuleEntity>> findAllList(ReplaceRuleFindAllListVm model) throws Exception {
        return ResultUtil.success("",replaceRuleService.findAllList(model));
    }
}
