package com.digitalchina.resourcecatalog.db.dao;

import com.digitalchina.resourcecatalog.db.domain.CataRequire;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 信息需求目录 Mapper 接口
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
public interface CataRequireMapper extends BaseMapper<CataRequire> {

	Integer selectSequence(@Param("seq")String seq);

	IPage<CataRequire> selectPages(Page page, @Param("name") String name, @Param("code") String code,
			@Param("deptId") String deptId, @Param("nameAndCode") String nameAndCode);

}
