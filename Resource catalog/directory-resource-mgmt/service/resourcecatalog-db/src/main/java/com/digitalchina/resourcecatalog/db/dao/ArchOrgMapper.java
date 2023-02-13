package com.digitalchina.resourcecatalog.db.dao;

import com.digitalchina.resourcecatalog.db.domain.ArchOrg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalchina.resourcecatalog.db.domain.Result;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 组织机构 Mapper 接口
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
public interface ArchOrgMapper extends BaseMapper<ArchOrg> {

    ArchOrg detail(@Param("orgId") Integer orgId);

    Integer getMaxOrgCdByPidIsNull();

    Integer getMaxOrgCdByPid(@Param("parOrgId") Integer parOrgId);

    List<ArchOrg> getDeptTreeList(@Param("deptIds") List<Integer> deptIds);

    List<ArchOrg> selectListAs();

    List<Result> top10Dept(@Param("shareType") String shareType);

    Integer getDispalySn(@Param("parOrgId") Integer parOrgId);

    List<Map<String, Object>> getMaxSecondLevel();
}
