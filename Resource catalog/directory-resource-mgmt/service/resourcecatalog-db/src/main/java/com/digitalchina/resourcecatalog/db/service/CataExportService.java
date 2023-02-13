package com.digitalchina.resourcecatalog.db.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.digitalchina.resourcecatalog.db.domain.CataExport;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangys
 * @since 2020-05-19
 */
public interface CataExportService extends IService<CataExport> {
    IPage<List<CataExport>> selectPages(Page page, Integer depId, String startDate, String endDate);
    void delete(Integer id);
    Workbook export(Integer depId);
    Workbook exportCity(Integer depId);
}
