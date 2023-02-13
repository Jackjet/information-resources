package com.digitalchina.resourcecatalog.db.dao;

import com.digitalchina.resourcecatalog.db.domain.CataInfoTemp;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 信息资源目录(暂存表) Mapper 接口
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
public interface CataInfoTempMapper extends BaseMapper<CataInfoTemp> {

	IPage<List<CataInfoTemp>> selectPages(Page page, @Param("uviewNo") String uviewNo, @Param("uviewNm") String uviewNm,
	  @Param("provOrgId") String provOrgId, @Param("auditStatus") String auditStatus, @Param("typeId") Integer typeId, @Param("shareLv") String shareLv, @Param("pubSts")String pubSts, @Param("cityCataCode")String cityCataCode);

    List<Map<String, Object>> waitReviewInfoList();

	int updateRelBusi(@Param("newValue")Integer newValue, @Param("newMsg")String newMsg, @Param("infoIds")List<Integer> infoIds);

	int updateLimitReadedStatus(@Param("infoId")Integer infoId);

	int updateLimitReadedStatusAll(@Param("uviewNo") String uviewNo, @Param("uviewNm") String uviewNm, @Param("orgId")Integer orgId, @Param("updateCyc") String updateCyc,
								   @Param("dayLimit") Timestamp dayLimit, @Param("weekLimit") Timestamp weekLimit, @Param("monthLimit") Timestamp monthLimit, @Param("quarterLimit")Timestamp quarterLimit, @Param("halfYearLimit")Timestamp halfYearLimit, @Param("yearLimit")Timestamp yearLimit);

	IPage<CataInfoTemp> queryUpdateLimitPages(Page page, @Param("uviewNo") String uviewNo, @Param("uviewNm") String uviewNm, @Param("orgId")Integer orgId, @Param("updateCyc") String updateCyc,
													@Param("dayLimit") Timestamp dayLimit, @Param("weekLimit") Timestamp weekLimit, @Param("monthLimit") Timestamp monthLimit, @Param("quarterLimit")Timestamp quarterLimit, @Param("halfYearLimit")Timestamp halfYearLimit, @Param("yearLimit")Timestamp yearLimit);
}
