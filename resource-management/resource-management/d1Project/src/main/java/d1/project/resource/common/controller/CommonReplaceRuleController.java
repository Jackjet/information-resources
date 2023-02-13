package d1.project.resource.common.controller;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resource.common.service.ReplaceRuleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公用数据源接口
 */
@Auth("noauth")
@RestController
@RequestMapping("/common/replaceRule")
public class CommonReplaceRuleController {
    private final ReplaceRuleService replaceRuleService;

    public CommonReplaceRuleController(ReplaceRuleService replaceRuleService) {
        this.replaceRuleService = replaceRuleService;
    }

    /**
     * 查询数据源
     */
    @GetMapping("/findAllList")
    public Result<JSONObject> findAll(String name){
        try{
            return ResultUtil.success("成功",replaceRuleService.findAllList(name));
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }
}
