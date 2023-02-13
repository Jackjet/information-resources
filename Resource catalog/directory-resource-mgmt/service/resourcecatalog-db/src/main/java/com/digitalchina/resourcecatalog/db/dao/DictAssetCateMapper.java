package com.digitalchina.resourcecatalog.db.dao;

import com.digitalchina.resourcecatalog.db.domain.DictAssetCate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 目录分类管理 Mapper 接口
 * </p>
 *
 * @author baokd
 * @since 2020-05-12
 */
public interface DictAssetCateMapper extends BaseMapper<DictAssetCate> {
    Integer getMaxCodeByPid(@Param("pid") Integer pid);
}
