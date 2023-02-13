package com.digitalchina.resourcecatalog.db.service.impl;

import com.digitalchina.resourcecatalog.db.domain.ArchBusiUviewStrConfig;
import com.digitalchina.resourcecatalog.db.domain.ArchOrg;
import com.digitalchina.resourcecatalog.db.domain.CataInfoHistory;
import com.digitalchina.resourcecatalog.db.domain.CataInfoHistoryTypeRel;
import com.digitalchina.resourcecatalog.db.domain.CataInfoItemHistoryRel;
import com.digitalchina.resourcecatalog.db.domain.CataInfoItemTempRel;
import com.digitalchina.resourcecatalog.db.domain.CataInfoTemp;
import com.digitalchina.resourcecatalog.db.domain.CataInfoTempTypeRel;
import com.digitalchina.resourcecatalog.db.domain.DesensitizationRuleHistory;
import com.digitalchina.resourcecatalog.db.domain.DesensitizationRuleTemp;
import com.digitalchina.resourcecatalog.db.domain.DictAssetCate;
import com.digitalchina.resourcecatalog.db.domain.SysDict;
import com.digitalchina.resourcecatalog.db.dto.CataInfoCompareDto;
import com.digitalchina.resourcecatalog.db.dto.CataInfoHistoryDto;
import com.digitalchina.resourcecatalog.db.dto.CataInfoTempTypeRelDto;
import com.digitalchina.resourcecatalog.db.dto.CataItemCompareDto;
import com.digitalchina.resourcecatalog.db.dao.CataInfoHistoryMapper;
import com.digitalchina.resourcecatalog.db.service.ArchBusiUviewStrConfigService;
import com.digitalchina.resourcecatalog.db.service.ArchOrgService;
import com.digitalchina.resourcecatalog.db.service.CataInfoHistoryService;
import com.digitalchina.resourcecatalog.db.service.CataInfoHistoryTypeRelService;
import com.digitalchina.resourcecatalog.db.service.CataInfoItemHistoryRelService;
import com.digitalchina.resourcecatalog.db.service.CataInfoItemTempRelService;
import com.digitalchina.resourcecatalog.db.service.CataInfoTempService;
import com.digitalchina.resourcecatalog.db.service.CataInfoTempTypeRelService;
import com.digitalchina.resourcecatalog.db.service.DesensitizationRuleHistoryService;
import com.digitalchina.resourcecatalog.db.service.DesensitizationRuleTempService;
import com.digitalchina.resourcecatalog.db.service.DictAssetCateService;
import com.digitalchina.resourcecatalog.db.service.SysDictService;

import cn.hutool.core.util.ObjectUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.lang.reflect.Method;
import java.util.*;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 信息资源目录历史信息记录 服务实现类
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
@Service
public class CataInfoHistoryServiceImpl extends ServiceImpl<CataInfoHistoryMapper, CataInfoHistory>
		implements CataInfoHistoryService {

	@Autowired
	private CataInfoHistoryTypeRelService cataInfoHistoryTypeRelService;
	@Autowired
	private CataInfoTempService cataInfoTempService;
	@Autowired
	private CataInfoTempTypeRelService cataInfoTempTypeRelService;
	@Autowired
	private CataInfoItemHistoryRelService cataInfoItemHistoryRelService;
	@Autowired
	private CataInfoItemTempRelService cataInfoItemTempRelService;
	@Autowired
	private SysDictService sysDictService;
	@Autowired
	private ArchOrgService archOrgService;
	@Autowired
	private DictAssetCateService dictAssetCateService;
	@Autowired
	private ArchBusiUviewStrConfigService archBusiUviewStrConfigService;
	@Autowired
	private CataInfoHistoryMapper cataInfoHistoryMapper;
	@Autowired
	private DesensitizationRuleTempService desensitizationRuleTempService;
	@Autowired
	private DesensitizationRuleHistoryService desensitizationRuleHistoryService;

	@Override
	@Transactional
	public CataInfoHistory rollback(Integer id, String userName) {
		CataInfoHistory cata = this
				.getOne(new QueryWrapper<CataInfoHistory>().eq(CataInfoHistory.ID, id).eq(CataInfoHistory.DELETED, 0));
		if (cata != null) {
			CataInfoTemp cataInfo = new CataInfoTemp();
			BeanUtils.copyProperties(cata, cataInfo,CataInfoTemp.VERSION);
			cataInfo.setAuditStatus("0");
			cataInfo.setUpdater(userName);
			cataInfo.setUpdateDt(new Date());
			cataInfoTempService.updateById(cataInfo);
			// 关联信息资源分类替换
			List<CataInfoHistoryTypeRel> hList = cataInfoHistoryTypeRelService
					.list(new QueryWrapper<CataInfoHistoryTypeRel>().eq(CataInfoHistoryTypeRel.INFO_ID, id));
			cataInfoTempTypeRelService.remove(
					new QueryWrapper<CataInfoTempTypeRel>().eq(CataInfoTempTypeRel.INFO_ID, cataInfo.getUviewId()));
			if (hList != null && hList.size() > 0) {
				List<CataInfoTempTypeRel> list = new LinkedList<CataInfoTempTypeRel>();
				for (CataInfoHistoryTypeRel cataInfoHistoryTypeRel : hList) {
					CataInfoTempTypeRel cataInfoTempTypeRel = new CataInfoTempTypeRel();
					BeanUtils.copyProperties(cataInfoHistoryTypeRel, cataInfoTempTypeRel);
					cataInfoTempTypeRel.setInfoId(cataInfo.getUviewId());
					list.add(cataInfoTempTypeRel);
				}
				cataInfoTempTypeRelService.saveBatch(list);
			}
			// 关联信息项替换
			List<CataInfoItemHistoryRel> rList = cataInfoItemHistoryRelService
					.list(new QueryWrapper<CataInfoItemHistoryRel>().eq(CataInfoItemHistoryRel.UVIEW_ID, id));
			cataInfoItemTempRelService.remove(
					new QueryWrapper<CataInfoItemTempRel>().eq(CataInfoItemTempRel.UVIEW_ID, cataInfo.getUviewId()));
			if (rList != null && rList.size() > 0) {
				List<CataInfoItemTempRel> list = new LinkedList<CataInfoItemTempRel>();
				for (CataInfoItemHistoryRel cataInfoItemHistoryRel : rList) {
					CataInfoItemTempRel cataInfoItemTempRel = new CataInfoItemTempRel();
					BeanUtils.copyProperties(cataInfoItemHistoryRel, cataInfoItemTempRel);
					cataInfoItemTempRel.setUviewId(cataInfo.getUviewId());
					list.add(cataInfoItemTempRel);
				}
				cataInfoItemTempRelService.saveBatch(list);
				//删除信息项（暂存表）脱敏规则
				desensitizationRuleTempService.deleteAll();
				List<DesensitizationRuleTemp> ruleTemps = new ArrayList<DesensitizationRuleTemp>();
				for(CataInfoItemTempRel temTempRel:list) {
					DesensitizationRuleHistory desensitizationRuleHistory = desensitizationRuleHistoryService.getInfo(temTempRel.getUviewId());
					if(null != desensitizationRuleHistory) {
						DesensitizationRuleTemp desensitizationRuleTemp = new DesensitizationRuleTemp();
						BeanUtils.copyProperties(desensitizationRuleHistory, desensitizationRuleTemp);
						desensitizationRuleTemp.setId(null);
						ruleTemps.add(desensitizationRuleTemp);
					}
				}
				//增加信息项（暂存表）脱敏规则
				desensitizationRuleTempService.saveBatch(ruleTemps);
			}
			return cata;
		}
		return null;
	}

	@Override
	public CataInfoCompareDto compare(Integer id) {
		CataInfoCompareDto cataInfoCompareDto = new CataInfoCompareDto();
		CataInfoHistory compareInfo = this.baseMapper.selectById(id);
		if(compareInfo==null){
			return null;
		}
		CataInfoHistory currentInfo = this.baseMapper
				.selectOne(new QueryWrapper<CataInfoHistory>().eq(CataInfoHistory.UVIEW_ID, compareInfo.getUviewId())
						.eq(CataInfoHistory.IS_CURRENT, "1").eq(CataInfoHistory.DELETED, 0));
		if (!compareInfo.getId().equals(currentInfo.getId())) {
			CataInfoHistoryDto compareDto = new CataInfoHistoryDto();
			CataInfoHistoryDto currentDto = new CataInfoHistoryDto();
			BeanUtils.copyProperties(compareInfo, compareDto);
			BeanUtils.copyProperties(currentInfo, currentDto);
			List<CataInfoItemHistoryRel> itemList = cataInfoItemHistoryRelService
					.list(new QueryWrapper<CataInfoItemHistoryRel>().in(CataInfoItemHistoryRel.UVIEW_ID,
							new Integer[] { compareDto.getId(), currentDto.getId() }));
			List<CataInfoItemHistoryRel> compareItemList=new ArrayList<CataInfoItemHistoryRel>();
			List<CataInfoItemHistoryRel> currentItemList=new ArrayList<CataInfoItemHistoryRel>();
			List<String> compareList=new ArrayList<String>();
			List<String> currentList=new ArrayList<String>();
			for (CataInfoItemHistoryRel cataInfoItemHistoryRel : itemList) {
				if(cataInfoItemHistoryRel.getUviewId().equals(compareDto.getId())){
					compareItemList.add(cataInfoItemHistoryRel);
					compareList.add(cataInfoItemHistoryRel.getEngCd());
				}else if(cataInfoItemHistoryRel.getUviewId().equals(currentDto.getId())){
					currentItemList.add(cataInfoItemHistoryRel);
					currentList.add(cataInfoItemHistoryRel.getEngCd());
				}
			}
			//比较两边多余项
			ArrayList<String> tempList = new ArrayList<String>();
			tempList.addAll(compareList);
			compareList.removeAll(currentList);
			compareDto.setSurplusList(compareList);
			currentList.removeAll(tempList);
			currentDto.setSurplusList(currentList);
			//比较两边信息项数据
			List<ArchBusiUviewStrConfig> configList=archBusiUviewStrConfigService.list(new QueryWrapper<ArchBusiUviewStrConfig>().eq(ArchBusiUviewStrConfig.IS_INSIDE, "0").orderByAsc(ArchBusiUviewStrConfig.ORDER_NUM));
			List<CataItemCompareDto> cataItemCompareList = new LinkedList<CataItemCompareDto>();
			for (CataInfoItemHistoryRel currentItemRel : currentItemList) {
				for (CataInfoItemHistoryRel compareItemRel : compareItemList) {
					if(currentItemRel.getEngCd().equals(compareItemRel.getEngCd())){
						compare(currentItemRel,compareItemRel,configList,cataItemCompareList);
						break;
					}
				}
			}
			dictBackfill(compareDto, currentDto);
			cataInfoCompareDto.setCataInfoHistoryCompare(compareDto);
			cataInfoCompareDto.setCataInfoHistoryCurrent(currentDto);
			cataInfoCompareDto.setCataItemCompareList(cataItemCompareList);
		}
		return cataInfoCompareDto;
	}

	/**
	 * 字典名称回填
	 * 
	 * @param compareDto
	 * @param currentDto
	 */
	private void dictBackfill(CataInfoHistoryDto compareDto, CataInfoHistoryDto currentDto) {
		String[] types = new String[] { "format_type", "format_type_"+compareDto.getMediaFmt(),"format_type_"+currentDto.getMediaFmt(), "update_cycle", "share_type" };
		List<SysDict> dictList = sysDictService
				.list(new QueryWrapper<SysDict>().in(SysDict.TYPE, types).orderByAsc(SysDict.TYPE));
		for (SysDict sysDict : dictList) {
			if ("format_type".equals(sysDict.getType())) {
				if (sysDict.getValue().equals(compareDto.getMediaFmt()))
					compareDto.setMediaFmtName(sysDict.getName());
				if (sysDict.getValue().equals(currentDto.getMediaFmt()))
					currentDto.setMediaFmtName(sysDict.getName());
			} else if (("format_type_"+compareDto.getMediaFmt()).equals(sysDict.getType())||("format_type_"+currentDto.getMediaFmt()).equals(sysDict.getType())) {
				if (sysDict.getValue().equals(compareDto.getMediaFmtType()))
					compareDto.setMediaFmtTypeName(sysDict.getName());
				if (sysDict.getValue().equals(currentDto.getMediaFmtType()))
					currentDto.setMediaFmtTypeName(sysDict.getName());
			} else if ("update_cycle".equals(sysDict.getType())) {
				if (sysDict.getValue().equals(compareDto.getUpdateCyc()))
					compareDto.setUpdateCycName(sysDict.getName());
				if (sysDict.getValue().equals(currentDto.getUpdateCyc()))
					currentDto.setUpdateCycName(sysDict.getName());
			} else if ("share_type".equals(sysDict.getType())) {
				if (sysDict.getValue().equals(compareDto.getShareLv()))
					compareDto.setShareLvName(sysDict.getName());
				if (sysDict.getValue().equals(currentDto.getShareLv()))
					currentDto.setShareLvName(sysDict.getName());
			}
		}
		List<Integer> ids = new ArrayList<>();
		if(compareDto.getProvOrgId()!=null){
			ids.add(compareDto.getProvOrgId());
		}
		if(!StringUtils.isEmpty(compareDto.getBelongTo())){
			String[] belongs = compareDto.getBelongTo().split(",");
			for (int i=0;i<belongs.length;i++){
				ids.add(Integer.parseInt(belongs[i]));
			}
		}
		if(currentDto.getProvOrgId()!=null){
			ids.add(currentDto.getProvOrgId());
		}
		if(!StringUtils.isEmpty(currentDto.getBelongTo())){
			String[] belongs = currentDto.getBelongTo().split(",");
			for (int i=0;i<belongs.length;i++){
				ids.add(Integer.parseInt(belongs[i]));
			}
		}
		QueryWrapper<ArchOrg> queryWrapper = new QueryWrapper<ArchOrg>();
		queryWrapper.select(ArchOrg.ORG_ID, ArchOrg.PAR_ORG_ID, ArchOrg.ORG_CD, ArchOrg.ORG_NM, ArchOrg.SOCIAL_CREDIT_CD);
		queryWrapper.and(w -> w.in(ArchOrg.ORG_ID, ids));
		List<ArchOrg> deptList = archOrgService.list(queryWrapper);
		Map<Integer,String> deptMap = new HashMap<>();
		deptList.forEach(item ->{deptMap.put(item.getOrgId(), item.getOrgNm());});
		if(compareDto.getProvOrgId()!=null){
			compareDto.setProvOrgName(deptMap.get(compareDto.getProvOrgId()));
		}
		if(!StringUtils.isEmpty(compareDto.getBelongTo())){
			String[] belongs = compareDto.getBelongTo().split(",");
			List<String> belongNames = new ArrayList<>();
			for (int i=0;i<belongs.length;i++){
				belongNames.add(deptMap.get(Integer.parseInt(belongs[i])));
			}
			compareDto.setBelongToName(String.join(",", belongNames));
		}
		if(currentDto.getProvOrgId()!=null){
			currentDto.setProvOrgName(deptMap.get(currentDto.getProvOrgId()));
		}
		if(!StringUtils.isEmpty(currentDto.getBelongTo())){
			String[] belongs = currentDto.getBelongTo().split(",");
			List<String> belongNames = new ArrayList<>();
			for (int i=0;i<belongs.length;i++){
				belongNames.add(deptMap.get(Integer.parseInt(belongs[i])));
			}
			currentDto.setBelongToName(String.join(",", belongNames));
		}
		Integer[] typeIds = new Integer[] { currentDto.getId(), compareDto.getId() };
		List<CataInfoHistoryTypeRel> typeList = cataInfoHistoryTypeRelService
				.list(new QueryWrapper<CataInfoHistoryTypeRel>().in(CataInfoHistoryTypeRel.INFO_ID, typeIds));
		List<CataInfoTempTypeRelDto> currentDtoTypeList = new ArrayList<CataInfoTempTypeRelDto>();
		List<CataInfoTempTypeRelDto> compareDtoTypeList = new ArrayList<CataInfoTempTypeRelDto>();
		List<DictAssetCate> dictAssetCateList = dictAssetCateService
				.list(new QueryWrapper<DictAssetCate>().eq(DictAssetCate.STATUS, "1"));
		for (DictAssetCate dictAssetCate : dictAssetCateList) {
			for (CataInfoHistoryTypeRel cataInfoTempTypeRel : typeList) {
				if (dictAssetCate.getTypId().equals(cataInfoTempTypeRel.getTypeId())) {
					CataInfoTempTypeRelDto dto = new CataInfoTempTypeRelDto();
					BeanUtils.copyProperties(cataInfoTempTypeRel, dto);
					dto.setTypeName(dictAssetCate.getTypNm());
					if (currentDto.getId().equals(cataInfoTempTypeRel.getInfoId())) {
						currentDtoTypeList.add(dto);
					} else if (compareDto.getId().equals(cataInfoTempTypeRel.getInfoId())) {
						compareDtoTypeList.add(dto);
					}
				}
			}
		}
		compareDto.setCataInfoHistoryTypeRelDtoList(compareDtoTypeList);
		currentDto.setCataInfoHistoryTypeRelDtoList(currentDtoTypeList);
	}
	
	/**
	 * 比较字段内容
	 * @param currentItemRel
	 * @param compareItemRel
	 * @param configList
	 * @param cataItemCompareList
	 */
	private void compare(CataInfoItemHistoryRel currentItemRel, CataInfoItemHistoryRel compareItemRel, List<ArchBusiUviewStrConfig> configList, List<CataItemCompareDto> cataItemCompareList){
		CataItemCompareDto cataItemCompareDto=new CataItemCompareDto();
		if(currentItemRel.getSrcField()!=null&&!currentItemRel.getSrcField().equals(compareItemRel.getSrcField())){
			cataItemCompareDto.setCode(currentItemRel.getEngCd());
			cataItemCompareDto.setType("信息项名称");
			cataItemCompareDto.setCurrentVersion(currentItemRel.getSrcField());
			cataItemCompareDto.setCompareVersion(compareItemRel.getSrcField());
			cataItemCompareDto.setNumber(cataItemCompareList.size()+1);
			cataItemCompareList.add(cataItemCompareDto);
		}
		if(currentItemRel.getSrcDataTyp()!=null&&!currentItemRel.getSrcDataTyp().equals(compareItemRel.getSrcDataTyp())){
			cataItemCompareDto=new CataItemCompareDto();
			cataItemCompareDto.setCode(currentItemRel.getEngCd());
			cataItemCompareDto.setType("信息项数据类型");
			cataItemCompareDto.setCurrentVersion(currentItemRel.getSrcDataTyp());
			cataItemCompareDto.setCompareVersion(compareItemRel.getSrcDataTyp());
			cataItemCompareDto.setNumber(cataItemCompareList.size()+1);
			cataItemCompareList.add(cataItemCompareDto);
		}
		if(currentItemRel.getDataLen()!=null&&!currentItemRel.getDataLen().equals(compareItemRel.getDataLen())){
			cataItemCompareDto=new CataItemCompareDto();
			cataItemCompareDto.setCode(currentItemRel.getEngCd());
			cataItemCompareDto.setType("信息项数据长度");
			cataItemCompareDto.setCurrentVersion(currentItemRel.getDataLen());
			cataItemCompareDto.setCompareVersion(compareItemRel.getDataLen());
			cataItemCompareDto.setNumber(cataItemCompareList.size()+1);
			cataItemCompareList.add(cataItemCompareDto);
		}
		if(currentItemRel.getItemRemark()!=null){
			if(!currentItemRel.getItemRemark().equals(compareItemRel.getItemRemark())){
				cataItemCompareDto=new CataItemCompareDto();
				cataItemCompareDto.setCode(currentItemRel.getEngCd());
				cataItemCompareDto.setType("信息项描述");
				cataItemCompareDto.setCurrentVersion(currentItemRel.getItemRemark());
				cataItemCompareDto.setCompareVersion(compareItemRel.getItemRemark()==null?"-":compareItemRel.getItemRemark());
				cataItemCompareDto.setNumber(cataItemCompareList.size()+1);
				cataItemCompareList.add(cataItemCompareDto);
			}
		}else{
			if(compareItemRel.getItemRemark()!=null&&!compareItemRel.getItemRemark().equals(currentItemRel.getItemRemark())){
				cataItemCompareDto=new CataItemCompareDto();
				cataItemCompareDto.setCode(currentItemRel.getEngCd());
				cataItemCompareDto.setType("信息项描述");
				cataItemCompareDto.setCurrentVersion("-");
				cataItemCompareDto.setCompareVersion(compareItemRel.getItemRemark());
				cataItemCompareDto.setNumber(cataItemCompareList.size()+1);
				cataItemCompareList.add(cataItemCompareDto);
			}
		}
		if(currentItemRel.getSno()!=null&&!currentItemRel.getSno().equals(compareItemRel.getSno())){
			cataItemCompareDto=new CataItemCompareDto();
			cataItemCompareDto.setCode(currentItemRel.getEngCd());
			cataItemCompareDto.setType("显示序号");
			cataItemCompareDto.setCurrentVersion(currentItemRel.getSno()+"");
			cataItemCompareDto.setCompareVersion(compareItemRel.getSno()+"");
			cataItemCompareDto.setNumber(cataItemCompareList.size()+1);
			cataItemCompareList.add(cataItemCompareDto);
		}
		if(configList!=null&&configList.size()>0){
			Class<?> c1 = CataInfoItemHistoryRel.class;
			for (int i=1;i<configList.size()+1&&i<21;i++) {
				String methodName = ("getExt" + i);
				try {
					Method method = c1.getMethod(methodName);
					Object current=method.invoke(currentItemRel);
					Object compare=method.invoke(compareItemRel);
					if(!ObjectUtil.isEmpty(current)){
						if(!current.equals(compare)){
							cataItemCompareDto=new CataItemCompareDto();
							cataItemCompareDto.setCode(currentItemRel.getEngCd());
							cataItemCompareDto.setType(configList.get(i-1).getItemName());
							cataItemCompareDto.setCurrentVersion(current+"");
							cataItemCompareDto.setCompareVersion(compare==null?"-":compare+"");
							cataItemCompareDto.setNumber(cataItemCompareList.size()+1);
							cataItemCompareList.add(cataItemCompareDto);
						}
					}else{
						if(ObjectUtil.isEmpty(compare)){
							continue;
						}else{
							cataItemCompareDto=new CataItemCompareDto();
							cataItemCompareDto.setCode(currentItemRel.getEngCd());
							cataItemCompareDto.setType(configList.get(i-1).getItemName());
							cataItemCompareDto.setCurrentVersion("-");
							cataItemCompareDto.setCompareVersion(compare+"");
							cataItemCompareDto.setNumber(cataItemCompareList.size()+1);
							cataItemCompareList.add(cataItemCompareDto);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					new RuntimeException("信息项对比出现问题："+e);
				}
			}
		}
	}

	/**
	 * 插入历史表
	 * @return 是否成功
	 */
	public boolean saveCataInfoHistory(CataInfoHistory cataInfoHistory, List<CataInfoHistoryTypeRel> cataInfoHistoryTypeRelList,
									   List<CataInfoItemHistoryRel> cataInfoItemHistoryRelList,String userName){
		try{
			cataInfoHistory.setCreator(userName);
			cataInfoHistory.setCrtDt(new Date());
			cataInfoHistory.setIsCurrent("1");
			this.baseMapper.insert(cataInfoHistory);
			Integer history_id = cataInfoHistory.getId();
			for (CataInfoHistoryTypeRel cataInfoHistoryTypeRel:cataInfoHistoryTypeRelList) {
				cataInfoHistoryTypeRel.setInfoId(history_id);
			}
			cataInfoHistoryTypeRelService.saveBatch(cataInfoHistoryTypeRelList);
			for (CataInfoItemHistoryRel cataInfoItemHistoryRel:cataInfoItemHistoryRelList) {
				cataInfoItemHistoryRel.setUviewId(history_id);
			}
			cataInfoItemHistoryRelService.saveBatch(cataInfoItemHistoryRelList);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateAllStatus(Integer infoId){
		return cataInfoHistoryMapper.updateAllStatus(infoId);
	}
}
