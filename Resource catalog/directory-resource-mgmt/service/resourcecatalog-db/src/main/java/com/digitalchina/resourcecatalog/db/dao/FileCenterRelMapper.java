package com.digitalchina.resourcecatalog.db.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.digitalchina.resourcecatalog.db.domain.FileCenterRel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 资料中心文件关联表 Mapper 接口
 * </p>
 *
 * @author wangmh
 * @since 2020-05-21
 */
public interface FileCenterRelMapper extends BaseMapper<FileCenterRel> {

    IPage<Map<String, Object>> myPage(IPage page, @Param("fileName") String fileName);

    Map<String, Object> detail(@Param("id") Integer id);
}
