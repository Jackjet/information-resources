package d1.project.d1project.business.controller;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.d1project.business.entity.RuleTemplate;
import d1.project.d1project.business.model.RuleTemplateFindAllVm;
import d1.project.d1project.business.model.RuleTemplateInsertVm;
import d1.project.d1project.business.model.RuleTemplateStatusUpdateVm;
import d1.project.d1project.business.model.RuleTemplateUpdateVm;
import d1.project.d1project.business.service.RuleTemplateService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 管理端_规则模板管理
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/ruleTemplate")
public class RuleTemplateController {

    private final RuleTemplateService ruleTemplateService;

    public RuleTemplateController(RuleTemplateService ruleTemplateService) {
        this.ruleTemplateService = ruleTemplateService;
    }


    /**
     * 分页查询
     */
    @GetMapping(value = "/findAll")
    public Result<Page<RuleTemplate>> findAll(RuleTemplateFindAllVm model) throws Exception {
        return ResultUtil.success("", ruleTemplateService.findAll(model));
    }

    /**
     * 新增
     */
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody RuleTemplateInsertVm model, HttpServletRequest request) throws DoValidException {
        ruleTemplateService.insert(model, request);
        return ResultUtil.success("");
    }

    /**
     * 删除
     */
    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id) throws DoValidException {
        ruleTemplateService.delete(id);
        return ResultUtil.success("");
    }

    /**
     * 更新
     */
    @PutMapping(value = "/update")
    public Result<String> update(@Valid @RequestBody RuleTemplateUpdateVm model, HttpServletRequest request) throws DoValidException {
        ruleTemplateService.update(model, request);
        return ResultUtil.success("");
    }

    /**
     * 更新规则模板状态
     */
    @PutMapping(value = "/updateStatus")
    public Result<String> updateStatus(@Valid @RequestBody RuleTemplateStatusUpdateVm model, HttpServletRequest request) throws DoValidException {
        ruleTemplateService.updateStatus(model, request);
        return ResultUtil.success("");
    }

    /**
     * 更新
     */
    @GetMapping(value = "/findOne")
    public Result<RuleTemplate> getInfo(@Valid @RequestParam("id") String id, HttpServletRequest request) throws DoValidException {
        try {
            RuleTemplate info = ruleTemplateService.findOneById(id, request);
            return ResultUtil.success("成功", info);

        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }
}
