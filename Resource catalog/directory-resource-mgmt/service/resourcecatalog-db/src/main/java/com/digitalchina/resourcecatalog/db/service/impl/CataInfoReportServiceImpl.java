package com.digitalchina.resourcecatalog.db.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.digitalchina.resourcecatalog.db.domain.ArchBusiUviewStrEx;
import com.digitalchina.resourcecatalog.db.domain.ArchOrg;
import com.digitalchina.resourcecatalog.db.domain.AssetCateEx;
import com.digitalchina.resourcecatalog.db.domain.CataInfoReport;
import com.digitalchina.resourcecatalog.db.domain.DictAssetCate;
import com.digitalchina.resourcecatalog.db.domain.SysDict;
import com.digitalchina.resourcecatalog.db.domain.SysStorage;
import com.digitalchina.resourcecatalog.db.dto.CataInfoDto;
import com.digitalchina.resourcecatalog.db.dto.CataInfoReportDto;
import com.digitalchina.resourcecatalog.db.dao.CataInfoReportMapper;
import com.digitalchina.resourcecatalog.db.service.ArchBusiUviewExService;
import com.digitalchina.resourcecatalog.db.service.ArchBusiUviewStrConfigService;
import com.digitalchina.resourcecatalog.db.service.ArchBusiUviewStrExService;
import com.digitalchina.resourcecatalog.db.service.ArchOrgService;
import com.digitalchina.resourcecatalog.db.service.AssetCateExService;
import com.digitalchina.resourcecatalog.db.service.CataInfoReportService;
import com.digitalchina.resourcecatalog.db.service.DictAssetCateService;
import com.digitalchina.resourcecatalog.db.service.SysDictService;
import com.digitalchina.resourcecatalog.db.service.SysStorageService;
import com.digitalchina.resourcecatalog.db.util.CreateWord;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 信息资源目录报告 服务实现类
 * </p>
 *
 * @author baokd
 * @since 2020-05-25
 */
@Service
public class CataInfoReportServiceImpl extends ServiceImpl<CataInfoReportMapper, CataInfoReport> implements CataInfoReportService {
    @Autowired
	SysStorageService sysStorageService;
    @Autowired
    ArchBusiUviewExService archBusiUviewExService;
    @Autowired
    DictAssetCateService dictAssetCateService;
    @Autowired
    ArchOrgService archOrgService;
    @Autowired
    ArchBusiUviewStrExService archBusiUviewStrExService;
    @Autowired
    SysDictService sysDictService;
    @Autowired
    AssetCateExService assetCateExService;
    @Autowired
    ArchBusiUviewStrConfigService archBusiUviewStrConfigService;
    
	@Value("${templatepath}")
	String templatepath;
    
	String[] types = new String[] { "format_type", "format_type_01", "format_type_02", "format_type_03",
			"format_type_04", "format_type_05", "format_type_06", "update_cycle", "share_type","is_open" };
    List<SysDict> dictList=null;
	String[] itemTypes = new String[] { "data_type" };
    List<SysDict> dictItemList=null;
    
	@Override
    @Transactional
    public void delete(CataInfoReport bean) {
        bean.setDeleted(1);
        if(this.updateById(bean)){
            SysStorage sysFileStore =sysStorageService.getOne(new QueryWrapper<SysStorage>().eq(SysStorage.KEY, bean.getFileKey()));
            if(null != sysFileStore){
                sysFileStore.setDeleted(1);
                sysStorageService.updateById(sysFileStore);
            }
        }
    }
	@Override
	public IPage<List<CataInfoReport>> selectPages(Page page, Integer depId, String startDate, String endDate) {
		return this.baseMapper.selectPages(page, depId,startDate,endDate);
	}
	@Override
	public String generateReport(Integer depId) {
		List<ArchOrg> archOrgList = archOrgService.list(
				new QueryWrapper<ArchOrg>().lambda()
						.eq(ArchOrg::getParOrgId,depId));
		Map<String, String> orgMp = new HashMap<>();
		archOrgList.forEach(item -> orgMp.put(item.getOrgId().toString(), item.getOrgNm()));
		try {
			Map<String, Object> dataMap = new HashMap<String, Object>();
			List<Integer> orgIds = new ArrayList<Integer>();
			orgIds.add(depId);
			DictAssetCate dictAssetCate = dictAssetCateService
					.getOne(new QueryWrapper<DictAssetCate>().eq(DictAssetCate.TYP_CD, "3"));
			List<DictAssetCate> dictAssetCateList = dictAssetCateService.getTreeByPidAndOrgId(dictAssetCate, orgIds);
			dictAssetCate.setChildren(dictAssetCateList);
			ArchOrg archOrg = archOrgService.getById(depId);
			dataMap.put("deptName", " " + archOrg.getOrgNm());
			SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
			dataMap.put("downloadTime", datef.format(new Date()));
			dataMap.put("typeList", archBusiUviewExService.selectForType(depId));
			int topCount = 0, infoCount = 0, itemCount = 0;
			String topNames = "";
			List<Map<String, Object>> termList = new ArrayList<Map<String, Object>>();
			for (DictAssetCate assetCate : dictAssetCate.getChildren()) {
				Map<String, Object> termMap = new HashMap<String, Object>();
				termMap.put("termName", assetCate.getTypNm());//项
				List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
				for (DictAssetCate term : assetCate.getChildren()) {
					Map<String, Object> order = new HashMap<String, Object>();
					order.put("orderName", term.getTypNm());// 目
					List<CataInfoReportDto> detailsList = new ArrayList<CataInfoReportDto>();
					for (DictAssetCate detail : term.getChildren()) {
						CataInfoReportDto info = new CataInfoReportDto();
						info.setTypNm(detail.getTypNm());//细目
						topNames += detail.getTypNm() + "，";
						List<CataInfoDto> infoList = archBusiUviewExService.selectByTypeId(depId, detail.getTypId());//信息资源目录
						if (infoList != null) {
							for (CataInfoDto cataInfoDto : infoList) {
								if(!StringUtils.isEmpty(cataInfoDto.getBelongTo())){
									List<String> orgNmList = Arrays.asList(cataInfoDto.getBelongTo().split(","));
									List<String> tmpCdList = new ArrayList<>();
									orgNmList.forEach(item -> {
										tmpCdList.add(orgMp.get(item));
									});
									cataInfoDto.setBelongToName(String.join(",", tmpCdList));
								}
								List<ArchBusiUviewStrEx> itemList = archBusiUviewStrExService
										.list(new QueryWrapper<ArchBusiUviewStrEx>().eq(ArchBusiUviewStrEx.UVIEW_ID,
												cataInfoDto.getUviewId()));
								dictBackfillForItem(itemList);
								cataInfoDto.setItemList(itemList);// 信息项
								itemCount = itemCount + itemList.size();
								dictBackfill(cataInfoDto);
							}
						}
						info.setInfoList(infoList);// 一级细目下属信息资源目录
						infoCount = infoCount + infoList.size();
						detailsList.add(info);
					}
					order.put("detailsList", detailsList);// 一级细目
					topCount = topCount + detailsList.size();
					orderList.add(order);
				}
				termMap.put("orderList", orderList);
				termList.add(termMap);
			}
			dataMap.put("termList", termList);
			dataMap.put("topCount", topCount);
			if (topNames != null && topNames != "") {
				topNames = topNames.substring(0, topNames.length() - 1);
			}
			dataMap.put("topNames", topNames);
			dataMap.put("infoCount", infoCount);
			dataMap.put("itemCount", itemCount);
			CreateWord createWord = new CreateWord();
			String outPath = "信息资源报告-" + archOrg.getOrgNm() + ".docx";
			createWord.createWord(dataMap, "info", templatepath, outPath);
			return outPath;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 详情数据回填
	 * @param itemList
	 * @param itemConfigList 
	 */
	private void dictBackfillForItem(List<ArchBusiUviewStrEx> itemList){
		if (dictItemList == null) {
			dictItemList = sysDictService
					.list(new QueryWrapper<SysDict>().in(SysDict.TYPE, itemTypes).orderByAsc(SysDict.TYPE));
		}
		if(itemList!=null&&itemList.size()>0){
			for (ArchBusiUviewStrEx archBusiUviewStrEx : itemList) {
				for (SysDict sysDict : dictItemList) {
					if ("data_type".equals(sysDict.getType()) && sysDict.getValue().equals(archBusiUviewStrEx.getSrcDataTyp())) {
						archBusiUviewStrEx.setSrcDataTyp(sysDict.getName());
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 详情数据回填
	 * @param cataInfoDto
	 */
	private void dictBackfill(CataInfoDto cataInfoDto) {
		if (dictList == null) {
			dictList = sysDictService
					.list(new QueryWrapper<SysDict>().in(SysDict.TYPE, types).orderByAsc(SysDict.TYPE));
		}
		for (SysDict sysDict : dictList) {
			if ("format_type".equals(sysDict.getType()) && sysDict.getValue().equals(cataInfoDto.getMediaFmt())) {
				cataInfoDto.setMediaFmtName(sysDict.getName());
			} else if (("format_type_" + cataInfoDto.getMediaFmt()).equals(sysDict.getType())
					&& sysDict.getValue().equals(cataInfoDto.getMediaFmtType())) {
				cataInfoDto.setMediaFmtTypeName(sysDict.getName());
			} else if ("update_cycle".equals(sysDict.getType())
					&& sysDict.getValue().equals(cataInfoDto.getUpdateCyc())) {
				cataInfoDto.setUpdateCycName(sysDict.getName());
			} else if ("share_type".equals(sysDict.getType()) && sysDict.getValue().equals(cataInfoDto.getShareLv())) {
				cataInfoDto.setShareLvName(sysDict.getName());
			} else if ("is_open".equals(sysDict.getType()) && sysDict.getValue().equals(cataInfoDto.getPubSts())) {
				cataInfoDto.setPubSts(sysDict.getName());
			}
		}
		List<AssetCateEx> typeList = assetCateExService
				.list(new QueryWrapper<AssetCateEx>().eq(AssetCateEx.INFO_ID, cataInfoDto.getUviewId()));
		List<DictAssetCate> dictAssetCateList = dictAssetCateService
				.list(new QueryWrapper<DictAssetCate>().eq(DictAssetCate.STATUS, "1"));
		for (DictAssetCate dictAssetCate : dictAssetCateList) {
			for (AssetCateEx assetCateEx : typeList) {
				if (dictAssetCate.getTypId().equals(assetCateEx.getTypeId())) {
					if ("1".equals(assetCateEx.getType()) || "2".equals(assetCateEx.getType())) {
						if (cataInfoDto.getTypeName() != null && cataInfoDto.getTypeName() != "") {
							cataInfoDto.setTypeName(cataInfoDto.getTypeName() + "," + dictAssetCate.getTypNm());
						} else {
							cataInfoDto.setTypeName(dictAssetCate.getTypNm());
						}
					} else if ("3".equals(assetCateEx.getType())) {
						cataInfoDto.setDeptTypeName(dictAssetCate.getTypNm());
					}
				}
			}
		}
	}
}
