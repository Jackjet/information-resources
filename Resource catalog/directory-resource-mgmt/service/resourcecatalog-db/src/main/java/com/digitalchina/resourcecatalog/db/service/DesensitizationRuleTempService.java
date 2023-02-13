package com.digitalchina.resourcecatalog.db.service;

import javax.validation.Valid;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digitalchina.resourcecatalog.db.domain.DesensitizationRuleTemp;

/**
 * <p>
 * 信息项（暂存表）脱敏规则表 服务类
 * </p>
 *
 * @author zhaoyan
 * @since 2020-06-18
 */
public interface DesensitizationRuleTempService extends IService<DesensitizationRuleTemp> {

	void update(@Valid DesensitizationRuleTemp desensitizationRuleTemp);

	Integer add(@Valid DesensitizationRuleTemp desensitizationRuleTemp);

	DesensitizationRuleTemp getInfo(Integer itemId);

	void deleteAll();

}
