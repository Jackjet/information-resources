package com.digitalchina.resourcecatalog.db.service;

import javax.validation.Valid;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digitalchina.resourcecatalog.db.domain.DesensitizationRuleHistory;

/**
 * <p>
 * 信息项（历史表）脱敏规则表 服务类
 * </p>
 *
 * @author zhaoyan
 * @since 2020-06-18
 */
public interface DesensitizationRuleHistoryService extends IService<DesensitizationRuleHistory> {

	DesensitizationRuleHistory getInfo(Integer itemId);

	void update(@Valid DesensitizationRuleHistory desensitizationRuleHistory);

	Integer add(@Valid DesensitizationRuleHistory desensitizationRuleHistory);

}
