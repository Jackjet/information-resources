package com.digitalchina.resourcecatalog.db.dao;

import com.digitalchina.resourcecatalog.db.domain.CataInfoHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 信息资源目录历史信息记录 Mapper 接口
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
public interface CataInfoHistoryMapper extends BaseMapper<CataInfoHistory> {
    public boolean updateAllStatus(@Param("infoId") Integer infoId);
}
