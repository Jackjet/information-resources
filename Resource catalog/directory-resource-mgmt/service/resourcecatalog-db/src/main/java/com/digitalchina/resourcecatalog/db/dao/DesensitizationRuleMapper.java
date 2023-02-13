package com.digitalchina.resourcecatalog.db.dao;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalchina.resourcecatalog.db.domain.DesensitizationRule;

/**
 * <p>
 * 信息项脱敏规则表 Mapper 接口
 * </p>
 *
 * @author zhaoyan
 * @since 2020-06-17
 */
public interface DesensitizationRuleMapper extends BaseMapper<DesensitizationRule> {
	
	Integer insertSelective(DesensitizationRule desensitizationRule);
	
	DesensitizationRule selectByExid(@Param("itemId") Integer itemId);
	
	void updateSelective(DesensitizationRule desensitizationRule);

}
