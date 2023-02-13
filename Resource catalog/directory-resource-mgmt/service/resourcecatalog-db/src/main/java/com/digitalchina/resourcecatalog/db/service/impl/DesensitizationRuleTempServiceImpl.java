package com.digitalchina.resourcecatalog.db.service.impl;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalchina.resourcecatalog.db.dao.DesensitizationRuleTempMapper;
import com.digitalchina.resourcecatalog.db.domain.DesensitizationRuleTemp;
import com.digitalchina.resourcecatalog.db.service.DesensitizationRuleTempService;

/**
 * <p>
 * 信息项（暂存表）脱敏规则表 服务实现类
 * </p>
 *
 * @author zhaoyan
 * @since 2020-06-18
 */
@Service
public class DesensitizationRuleTempServiceImpl extends ServiceImpl<DesensitizationRuleTempMapper, DesensitizationRuleTemp> implements DesensitizationRuleTempService {

	@Override
	public void update(@Valid DesensitizationRuleTemp desensitizationRuleTemp) {
		this.baseMapper.updateByPrimaryKeySelective(desensitizationRuleTemp);
	}

	@Override
	public Integer add(@Valid DesensitizationRuleTemp desensitizationRuleTemp) {
		this.baseMapper.insert(desensitizationRuleTemp);
		return desensitizationRuleTemp.getId();
	}

	@Override
	public DesensitizationRuleTemp getInfo(Integer itemId) {
		DesensitizationRuleTemp desensitizationRuleTemp = this.baseMapper.selectByTeId(itemId);
		return desensitizationRuleTemp;
	}

	@Override
	public void deleteAll() {
		this.baseMapper.updateAll();
		
	}

}
