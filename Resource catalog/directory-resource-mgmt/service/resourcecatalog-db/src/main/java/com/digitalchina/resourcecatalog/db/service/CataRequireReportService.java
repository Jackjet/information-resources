package com.digitalchina.resourcecatalog.db.service;

import com.digitalchina.resourcecatalog.db.domain.CataInfoReport;
import com.digitalchina.resourcecatalog.db.domain.CataRequireReport;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 信息需求目录报告 服务类
 * </p>
 *
 * @author baokd
 * @since 2020-05-25
 */
public interface CataRequireReportService extends IService<CataRequireReport> {

	IPage<List<CataInfoReport>> selectPages(Page page, Integer depId, String startDate, String endDate);

	void delete(CataRequireReport bean);

	String generateReport(Integer depId);

}
