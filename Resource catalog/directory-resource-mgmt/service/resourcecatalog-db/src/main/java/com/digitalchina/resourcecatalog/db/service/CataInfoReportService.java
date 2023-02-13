package com.digitalchina.resourcecatalog.db.service;

import com.digitalchina.resourcecatalog.db.domain.CataInfoReport;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 信息资源目录报告 服务类
 * </p>
 *
 * @author baokd
 * @since 2020-05-25
 */
public interface CataInfoReportService extends IService<CataInfoReport> {

	void delete(CataInfoReport cataInfoReport);

	IPage<List<CataInfoReport>> selectPages(Page page1, Integer depId, String startDate, String endDate);

	String generateReport(Integer depId);

}
