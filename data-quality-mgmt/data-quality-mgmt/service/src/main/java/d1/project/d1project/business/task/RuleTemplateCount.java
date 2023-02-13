package d1.project.d1project.business.task;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.d1project.business.model.RuleTemplateUpdateVm;
import d1.project.d1project.business.service.RuleTemplateService;
import d1.project.d1project.business.service.VerifyRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class RuleTemplateCount {
    @Autowired
    private VerifyRuleService verifyRuleService;
    @Autowired
    private RuleTemplateService ruleTemplateService;

    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate = 5000)
    private void configureTasks() {
        try {
            List<Map<String, Object>> list = verifyRuleService.getRuleTemplateCount();
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = list.get(i);
                    String rule_template_id = (String) map.get("rule_template_id");
                    Integer useCount = Integer.parseInt(map.get("use_count").toString());
                    RuleTemplateUpdateVm vm = new RuleTemplateUpdateVm();
                    vm.setId(rule_template_id);
                    vm.setUseCount(useCount);
                    ruleTemplateService.updateRuleTemplate(vm);
                }
            }
        } catch (DoValidException e) {
            e.printStackTrace();
        }

    }
}
