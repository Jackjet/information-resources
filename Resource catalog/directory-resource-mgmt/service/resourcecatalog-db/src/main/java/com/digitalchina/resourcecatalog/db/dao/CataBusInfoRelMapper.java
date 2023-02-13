package com.digitalchina.resourcecatalog.db.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalchina.resourcecatalog.db.domain.CataBusInfoRel;
import com.digitalchina.resourcecatalog.db.domain.ArchBusi;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权责清单与信息资源关联表 Mapper 接口
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
public interface CataBusInfoRelMapper extends BaseMapper<CataBusInfoRel> {

    IPage<List<Map<String, Object>>> cataInfoPage(Page page, @Param("busiId") Integer busiId, @Param("type") Integer type);
	IPage<List<ArchBusi>> selectPages(Page page, @Param("busiNoAndBusiNm") String busiNoAndBusiNm,
			@Param("busiNo") String busiNo, @Param("busiNm") String busiNm, @Param("infoId") Integer infoId,
			@Param("type") Integer type);
	IPage<List<ArchBusi>> selectBusPage(Page page, @Param("busiNoAndBusiNm") String busiNoAndBusiNm,
			@Param("busiNo") String busiNo, @Param("busiNm") String busiNm, @Param("infoId") Integer infoId,
			@Param("type") Integer type,@Param("deptId") Integer deptId);
}
