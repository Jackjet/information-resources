package com.digitalchina.resourcecatalog.db.dao;

import com.digitalchina.resourcecatalog.db.domain.ArchBusiUviewStrEx;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 信息项(正式表) Mapper 接口
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
public interface ArchBusiUviewStrExMapper extends BaseMapper<ArchBusiUviewStrEx> {

	IPage<List<ArchBusiUviewStrEx>> getList(Page page, @Param("pubSts")String pubSts,@Param("uviewId") Integer uviewId);
	List<ArchBusiUviewStrEx> getListByDeptId(Integer deptId);
}
