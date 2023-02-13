package com.digitalchina.resourcecatalog.db.dao;

import com.digitalchina.resourcecatalog.db.domain.ArchAppSys;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalchina.resourcecatalog.db.domain.ArchBusi;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 应用系统信息 Mapper 接口
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
public interface ArchAppSysMapper extends BaseMapper<ArchAppSys> {

    List<ArchBusi> getBusiRelList(@Param("appsysId") Integer appsysId);

    List<Map<String, Object>> getNodes(@Param("appId") Integer appId);

    List<Map<String, Object>> getLinks(@Param("appId") Integer appId);
}
