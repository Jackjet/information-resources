package com.digitalchina.resourcecatalog.db.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.digitalchina.resourcecatalog.db.domain.SysDict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 * @author LiangChuanQi
 * @since 2020-05-09
 */
public interface SysDictMapper extends BaseMapper<SysDict> {
    IPage<List<SysDict>> getNotUseList(IPage page,@Param("pid") Integer pid);
}
