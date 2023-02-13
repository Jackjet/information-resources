package com.digitalchina.resourcecatalog.db.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalchina.resourcecatalog.db.dao.DesensitizationRuleMapper;
import com.digitalchina.resourcecatalog.db.domain.DesensitizationRule;
import com.digitalchina.resourcecatalog.db.service.DesensitizationRuleService;

/**
 * <p>
 * 信息项脱敏规则表 服务实现类
 * </p>
 *
 * @author zhaoyan
 * @since 2020-06-17
 */
@Service
public class DesensitizationRuleServiceImpl extends ServiceImpl<DesensitizationRuleMapper, DesensitizationRule> implements DesensitizationRuleService {


	@Override
	public DesensitizationRule getInfo(Integer itemId) {
		DesensitizationRule desensitizationRule = this.baseMapper.selectByExid(itemId);
		return desensitizationRule;
	}

	@Override
	@Transactional
	public Integer add(DesensitizationRule desensitizationRule) {
		this.baseMapper.insert(desensitizationRule);
		return desensitizationRule.getId();
	}

	@Override
	@Transactional
	public void update(DesensitizationRule desensitizationRule) {
		this.baseMapper.updateSelective(desensitizationRule);
		
	}

}
