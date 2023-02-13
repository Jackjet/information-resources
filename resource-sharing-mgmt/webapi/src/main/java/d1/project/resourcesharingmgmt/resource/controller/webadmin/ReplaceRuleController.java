package d1.project.resourcesharingmgmt.resource.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.entity.ReplaceRuleEntity;
import d1.project.resourcesharingmgmt.resource.model.ReplaceRule.ReplaceRuleFindAllVm;
import d1.project.resourcesharingmgmt.resource.model.ReplaceRule.ReplaceRuleInsertVm;
import d1.project.resourcesharingmgmt.resource.model.ReplaceRule.ReplaceRuleUpdateVm;
import d1.project.resourcesharingmgmt.resource.service.ReplaceRuleService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 脱敏规则
 * @author zhengyang
 */
@Auth("webadmin")
@RestController("/webadmin/replaceRule")
@RequestMapping("/webadmin/replaceRule")
public class ReplaceRuleController {
    private final ReplaceRuleService replaceRuleService;

    public ReplaceRuleController(ReplaceRuleService replaceRuleService){
        this.replaceRuleService = replaceRuleService;
    }

    /**
     * 新增脱敏规则
     * @param model
     * @param request
     * @return
     * @throws DoValidException
     */
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody ReplaceRuleInsertVm model, HttpServletRequest request) throws DoValidException {
        replaceRuleService.insert(model, request);
        return ResultUtil.success("添加成功");
    }

    /**
     * 修改脱敏规则
     * @param model
     * @param request
     * @return
     * @throws DoValidException
     */
    @PutMapping(value = "/update")
    public Result<String> update(@Valid @RequestBody ReplaceRuleUpdateVm model, HttpServletRequest request) throws DoValidException {
        replaceRuleService.update(model, request);
        return ResultUtil.success("修改成功");
    }

    /**
     * 查询详情
     * @param id
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/find")
    public Result<ReplaceRuleEntity> find(String id) throws DoValidException {
        return ResultUtil.success("", replaceRuleService.find(id).orElseThrow(() -> new DoValidException("脱敏规则不存在")));
    }

    /**
     * 查询所有
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/findAll")
    public Result<ReplaceRuleEntity> findAll(ReplaceRuleFindAllVm model) throws Exception {
        return ResultUtil.success("",replaceRuleService.findAll(model));
    }

    /**
     * 脱敏规则删除
     *
     * @param id 记录Id
     * @return
     * @throws DoValidException
     */
    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id) throws DoValidException {
        replaceRuleService.delete(id);
        return ResultUtil.success("删除成功");
    }
}
