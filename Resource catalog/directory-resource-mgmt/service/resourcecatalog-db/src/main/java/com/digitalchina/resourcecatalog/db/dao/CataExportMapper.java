package com.digitalchina.resourcecatalog.db.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalchina.resourcecatalog.db.domain.CataExport;
import com.digitalchina.resourcecatalog.db.domain.CataImport;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangys
 * @since 2020-05-19
 */
public interface CataExportMapper extends BaseMapper<CataExport> {
    IPage<List<CataExport>> selectPages(Page page, @Param("depId") Integer depId,
                                        @Param("startDate") String startDate,
                                        @Param("endDate") String endDate);
    List<Map<String,Object>> getCataItemExportList( @Param("depId") Integer depId);
    List<Map<String,Object>> getCataRequireExportList( @Param("depId") Integer depId);
    List<Map<String,Object>> getAppSysExportList( @Param("depId") Integer depId);
    List<String> getCataListByAppId( @Param("appId") Integer appId);
    List<String> getTypeListByInfoId( @Param("infoId") Integer infoId);
}
