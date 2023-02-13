package com.digitalchina.resourcecatalog.db.service.impl;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalchina.resourcecatalog.db.dao.DesensitizationRuleHistoryMapper;
import com.digitalchina.resourcecatalog.db.domain.DesensitizationRuleHistory;
import com.digitalchina.resourcecatalog.db.service.DesensitizationRuleHistoryService;

/**
 * <p>
 * 信息项（历史表）脱敏规则表 服务实现类
 * </p>
 *
 * @author zhaoyan
 * @since 2020-06-18
 */
@Service
public class DesensitizationRuleHistoryServiceImpl extends ServiceImpl<DesensitizationRuleHistoryMapper, DesensitizationRuleHistory> implements DesensitizationRuleHistoryService {

	@Override
	public DesensitizationRuleHistory getInfo(Integer itemId) {
		DesensitizationRuleHistory desensitizationRuleHistory = this.baseMapper.selectByHrId(itemId);
		return desensitizationRuleHistory;
	}

	@Override
	public void update(@Valid DesensitizationRuleHistory desensitizationRuleHistory) {
		this.baseMapper.updateByPrimaryKeySelective(desensitizationRuleHistory);
		
	}

	@Override
	public Integer add(@Valid DesensitizationRuleHistory desensitizationRuleHistory) {
		this.baseMapper.insert(desensitizationRuleHistory);
		return desensitizationRuleHistory.getId();
		
	}

}
