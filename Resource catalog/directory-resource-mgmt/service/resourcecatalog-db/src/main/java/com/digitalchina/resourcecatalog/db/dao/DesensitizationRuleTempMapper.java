package com.digitalchina.resourcecatalog.db.dao;

import javax.validation.Valid;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalchina.resourcecatalog.db.domain.DesensitizationRuleTemp;

/**
 * <p>
 * 信息项（暂存表）脱敏规则表 Mapper 接口
 * </p>
 *
 * @author zhaoyan
 * @since 2020-06-18
 */
public interface DesensitizationRuleTempMapper extends BaseMapper<DesensitizationRuleTemp> {

	void updateByPrimaryKeySelective(@Valid DesensitizationRuleTemp desensitizationRuleTemp);

	DesensitizationRuleTemp selectByTeId(@Param("itemId")Integer itemId);

	void updateAll();

}
