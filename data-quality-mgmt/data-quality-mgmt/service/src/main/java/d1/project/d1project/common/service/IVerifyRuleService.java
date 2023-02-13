package d1.project.d1project.common.service;

import d1.project.d1project.business.entity.VerifyRule;
import d1.project.d1project.business.model.VerifyRuleFindAllVm;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IVerifyRuleService {
    List<VerifyRule> findAllByIdIn(List<String> ids);
    Page<VerifyRule> findAll(VerifyRuleFindAllVm model) throws Exception ;
}
