package com.digitalchina.resourcecatalog.db.service;

import com.digitalchina.resourcecatalog.db.domain.ArchBusi;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalchina.resourcecatalog.db.domain.CataBusInfoRel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权责清单与信息资源关联表 服务类
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
public interface CataBusInfoRelService extends IService<CataBusInfoRel> {
    /***
     * 关联信息资源
     * @param cataBusInfoRel
     */
    boolean relCataInfo(CataBusInfoRel cataBusInfoRel);

    /***
     * 关联信息资源列表分页
     * @param page
     * @param busiId
     * @param type
     * @return
     */
    IPage<List<Map<String, Object>>> cataInfoPage(Page page, Integer busiId, Integer type);
	IPage<List<ArchBusi>> selectPages(Page page1, String busiNoAndBusiNm, String busiNo, String busiNm, Integer infoId, Integer type);
	String saveInfo(Integer[] ids, Integer infoId, Integer type);

	IPage<List<ArchBusi>> selectBusPage(Page page1, String busiNoAndBusiNm, String busiNo, String busiNm, Integer infoId,
			Integer type, Integer deptId);
}
