package com.digitalchina.resourcecatalog.db.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalchina.resourcecatalog.db.domain.ArchAppSys;
import com.digitalchina.resourcecatalog.db.domain.ArchBusi;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权责清单 Mapper 接口
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
public interface ArchBusiMapper extends BaseMapper<ArchBusi> {

    IPage<Map<String, Object>> selectByPage(Page page, @Param("name") String name);

    List<ArchBusi> getBusInfoByOrgId(@Param("orgId") Integer orgId,@Param("containsChild")  String containsChild);
    List<ArchBusi> selectListContainsOrgNm(@Param("deptIds") List<Integer> deptIds);
    List<ArchBusi> getBusInfoByDept(@Param("orgId") Integer orgId);

    List<ArchBusi> selectByDeptId(@Param("id") Integer id, @Param("name") String name, @Param("filterCata")  Integer filterCata);

    List<ArchBusi> selectByPid(@Param("pId") Integer pId, @Param("filterCata")  Integer filterCata);

    ArchBusi detail(@Param("busiId") Integer busiId);

    Long getBusiNoMaxByPId(@Param("pId") Integer pId, @Param("deptId") Integer deptId);

    List<ArchAppSys> getAppListByBusiId(@Param("busiId") Integer busiId);

    IPage<List<Map<String, Object>>> cataInfoPage(Page page, @Param("orgId") Integer orgId, @Param("cataName") String cataName, @Param("infoIds") List<Integer> infoIds, @Param("type") Integer type);

    int countByDepts(@Param("deptIds") List<Integer> deptIds);

    List<ArchBusi> getExportBusisByDepId(@Param("depId") Integer depId);

    List<Map<Integer, Integer>> getFirstLevelMaxDeptBusiNo();
}
