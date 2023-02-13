package com.digitalchina.resourcecatalog.db.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.digitalchina.resourcecatalog.db.domain.CataInfoTemp;
import com.digitalchina.resourcecatalog.db.dto.CataInfoTempDto;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 信息资源目录(暂存表) 服务类
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
public interface CataInfoTempService extends IService<CataInfoTemp> {

    Map<String, Object> saveInfoTemp(CataInfoTempDto cata, String userName);

    String deleteByIds(List<Integer> ids);

    IPage<List<CataInfoTemp>> selectPages(Page page, String uviewNo, String uviewNm, String provOrgId, String auditStatus, Integer typeId, String shareLv, String pubSts, String cityCataCode);

    Integer saveInfoTemp_import(CataInfoTempDto cata);

    List<Map<String, Object>> waitReviewInfoList();

    String getCode(String fullTypCd);

    int updateRelBusi(Integer newValue,String newMsg, List<Integer> infoIds);

    IPage<CataInfoTemp> queryUpdateLimitPages(Page page, String uviewNo, String uviewNm, Integer orgId, String updateCyc);

    int updateLimitReadedStatus(Integer infoId);

    int updateLimitReadedStatusAll(String uviewNo, String uviewNm, Integer orgId, String updateCyc);
}
