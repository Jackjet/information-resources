package com.digitalchina.resourcecatalog.db.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.digitalchina.resourcecatalog.db.domain.ArchOrg;
import com.digitalchina.resourcecatalog.db.domain.CataImport;
import com.digitalchina.resourcecatalog.db.domain.CataImportError;
import com.digitalchina.resourcecatalog.db.domain.SysUser;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangys
 * @since 2020-05-12
 */
public interface CataImportService extends IService<CataImport> {
    void saveData(ArchOrg archOrg, SysUser user, Workbook wb, List<String> errorList, List<String> infoList);
    IPage<List<CataImport>> selectPages(Page page,Integer depId, String startDate, String endDate);
    void saveCataImport(CataImport cataImport);
    void delete(Integer id);
    List<CataImportError> getErrorList(Integer id);

    int getSeqNext( String seqName);
}
