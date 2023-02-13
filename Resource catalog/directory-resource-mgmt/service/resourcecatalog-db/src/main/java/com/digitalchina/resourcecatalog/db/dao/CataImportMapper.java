package com.digitalchina.resourcecatalog.db.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalchina.resourcecatalog.db.domain.CataImport;
import com.digitalchina.resourcecatalog.db.domain.CataInfoTemp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangys
 * @since 2020-05-12
 */
public interface CataImportMapper extends BaseMapper<CataImport> {
    int getSeqNext(@Param("seqName") String seqName);
    IPage<List<CataImport>> selectPages(Page page, @Param("depId") Integer depId,
                                        @Param("startDate") String startDate,
                                        @Param("endDate") String endDate);
}
