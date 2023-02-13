package com.digitalchina.resourcecatalog.db.dao;

import com.digitalchina.resourcecatalog.db.domain.CataInfoReport;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 信息资源目录报告 Mapper 接口
 * </p>
 *
 * @author baokd
 * @since 2020-05-25
 */
public interface CataInfoReportMapper extends BaseMapper<CataInfoReport> {

	IPage<List<CataInfoReport>> selectPages(Page page, @Param("depId") Integer depId, @Param("startDate") String startDate,
			@Param("endDate") String endDate);

}
