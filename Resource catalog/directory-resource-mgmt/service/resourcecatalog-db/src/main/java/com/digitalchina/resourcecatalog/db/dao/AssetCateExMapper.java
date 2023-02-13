package com.digitalchina.resourcecatalog.db.dao;

import com.digitalchina.resourcecatalog.db.domain.AssetCateEx;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 信息资源目录与信息类型关联表(正式表) Mapper 接口
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
public interface AssetCateExMapper extends BaseMapper<AssetCateEx> {

	IPage<List<Map<String,Object>>> getList(Page page1, @Param("type") String type, @Param("infoId") Integer infoId, @Param("typeId") Integer typeId);

}
