package com.digitalchina.resourcecatalog.db.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digitalchina.resourcecatalog.db.domain.DesensitizationRule;

/**
 * <p>
 * 信息项脱敏规则表 服务类
 * </p>
 *
 * @author zhaoyan
 * @since 2020-06-17
 */
public interface DesensitizationRuleService extends IService<DesensitizationRule> {
	
	DesensitizationRule getInfo(Integer itemId);
	
	Integer add(DesensitizationRule desensitizationRule);
	
	void update(DesensitizationRule desensitizationRule);

}
