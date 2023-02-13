package com.digitalchina.resourcecatalog.db.dao;

import javax.validation.Valid;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalchina.resourcecatalog.db.domain.DesensitizationRuleHistory;

/**
 * <p>
 * 信息项（历史表）脱敏规则表 Mapper 接口
 * </p>
 *
 * @author zhaoyan
 * @since 2020-06-18
 */
public interface DesensitizationRuleHistoryMapper extends BaseMapper<DesensitizationRuleHistory> {

	void updateByPrimaryKeySelective(@Valid DesensitizationRuleHistory desensitizationRuleHistory);

	DesensitizationRuleHistory selectByHrId(@Param("itemId")Integer itemId);

}
