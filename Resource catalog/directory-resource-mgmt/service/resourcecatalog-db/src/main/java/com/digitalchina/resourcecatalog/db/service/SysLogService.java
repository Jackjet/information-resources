package com.digitalchina.resourcecatalog.db.service;

import com.digitalchina.resourcecatalog.db.domain.SysLog;

import org.apache.poi.ss.usermodel.Workbook;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 操作日志表 服务类
 * </p>
 *
 * @author wangmh
 * @since 2020-04-30
 */
public interface SysLogService extends IService<SysLog> {

	Workbook export(String name, String startDate, String endDate);

}
