package com.digitalchina.resourcecatalog.db.dao;

import com.digitalchina.resourcecatalog.db.domain.ArchBusiUviewEx;
import com.digitalchina.resourcecatalog.db.dto.CataInfoDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 信息资源目录(正式表) Mapper 接口
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
public interface ArchBusiUviewExMapper extends BaseMapper<ArchBusiUviewEx> {

    List<Map<String, Object>> getNewTop5Info(@Param("deptIds") List<Integer> deptIds);

	List<CataInfoDto> selectByTypeId(@Param("depId")Integer depId,@Param("typId") Integer typId);

	List<Map<String, Object>> selectForType(@Param("depId")Integer depId);
}
