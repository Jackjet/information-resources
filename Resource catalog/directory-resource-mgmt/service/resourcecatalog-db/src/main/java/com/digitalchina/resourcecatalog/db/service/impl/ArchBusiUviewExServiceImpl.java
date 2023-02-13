package com.digitalchina.resourcecatalog.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.digitalchina.resourcecatalog.db.domain.*;
import com.digitalchina.resourcecatalog.db.dao.ArchBusiUviewExMapper;
import com.digitalchina.resourcecatalog.db.dto.CataInfoDto;
//import com.digitalchina.resourcecatalog.db.rabbitmq.RabbitConfig;
import com.digitalchina.resourcecatalog.db.service.ArchBusiUviewExService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalchina.resourcecatalog.db.service.ArchBusiUviewStrExService;
import com.digitalchina.resourcecatalog.db.service.AssetCateExService;
import com.digitalchina.resourcecatalog.db.service.DesensitizationRuleHistoryService;
import com.digitalchina.resourcecatalog.db.service.DesensitizationRuleService;
import com.digitalchina.resourcecatalog.db.service.DesensitizationRuleTempService;
//import com.digitalchina.resourcecatalog.db.service.MqResourceService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 信息资源目录(正式表) 服务实现类
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
@Service
public class ArchBusiUviewExServiceImpl extends ServiceImpl<ArchBusiUviewExMapper, ArchBusiUviewEx>
		implements ArchBusiUviewExService {
	@Autowired
	private ArchBusiUviewStrExService archBusiUviewStrExService;

	@Autowired
	private AssetCateExService assetCateExService;
//
//	@Autowired
//	private MqResourceService mqResourceService;

	@Autowired
	private DesensitizationRuleHistoryService desensitizationRuleHistoryService;

	@Autowired
	private DesensitizationRuleTempService desensitizationRuleTempService;

	@Autowired
	private DesensitizationRuleService desensitizationRuleService;

	@Transactional
	public boolean saveArchBuisUviewEx(ArchBusiUviewEx archBusiUviewEx, List<ArchBusiUviewStrEx> archBusiUviewStrExList,
			List<AssetCateEx> assetCateExList, String userName, List<CataInfoItemTempRel> cataInfoItemTempRelList) {
		try {
//			Integer type = RabbitConfig.CATA_UPDATE;
			if (!StringUtils.isEmpty(this.getById(archBusiUviewEx.getUviewId()))) {
				archBusiUviewEx.setUpdater(userName);
				archBusiUviewEx.setUpdateDt(new Date());
				this.baseMapper.updateById(archBusiUviewEx);
				archBusiUviewStrExService.remove(new QueryWrapper<ArchBusiUviewStrEx>().eq(ArchBusiUviewStrEx.UVIEW_ID,
						archBusiUviewEx.getUviewId()));
				assetCateExService
						.remove(new QueryWrapper<AssetCateEx>().eq(AssetCateEx.INFO_ID, archBusiUviewEx.getUviewId()));
//				if ("02".equals(archBusiUviewEx.getPubSts())) {
//					type = RabbitConfig.CATA_UPDATE_SHARE;
//				}
			} else {
				archBusiUviewEx.setCreator(userName);
				archBusiUviewEx.setCrtDt(new Date());
				this.baseMapper.insert(archBusiUviewEx);
//				if ("02".equals(archBusiUviewEx.getPubSts())) {
//					type = RabbitConfig.CATA_UPDATE_SHARE;
//				}
			}
			archBusiUviewStrExService.saveBatch(archBusiUviewStrExList);
			assetCateExService.saveBatch(assetCateExList);
			List<DesensitizationRule> rules = new ArrayList<DesensitizationRule>();
			List<DesensitizationRuleHistory> ruleHistories = new ArrayList<DesensitizationRuleHistory>();
			for (ArchBusiUviewStrEx archBusiUviewStrEx : archBusiUviewStrExList) {
				for (CataInfoItemTempRel cataInfoItemTempRel : cataInfoItemTempRelList) {
					DesensitizationRuleTemp desensitizationRuleTemp = desensitizationRuleTempService
							.getInfo(cataInfoItemTempRel.getUviewId());
					if (null != desensitizationRuleTemp) {
						DesensitizationRule desensitizationRule = new DesensitizationRule();
						DesensitizationRuleHistory desensitizationRuleHistory = new DesensitizationRuleHistory();
						BeanUtils.copyProperties(desensitizationRuleTemp, desensitizationRule);
						// 增加信息项脱敏规则
						desensitizationRule.setId(null);
						desensitizationRule.setItemId(archBusiUviewStrEx.getUviewId());
						desensitizationRule.setUpdateBy(userName);
						desensitizationRule.setUpdateTime(new Date());
						rules.add(desensitizationRule);
						// 增加信息项(历史表)脱敏规则
						BeanUtils.copyProperties(desensitizationRule, desensitizationRuleHistory);
						desensitizationRule.setId(null);
						ruleHistories.add(desensitizationRuleHistory);
					}
				}
			}
			desensitizationRuleService.saveBatch(rules);
			desensitizationRuleHistoryService.saveBatch(ruleHistories);
			Map<String, Object> message = new HashMap<String, Object>();
			message.put("archBusiUviewEx", archBusiUviewEx);
			message.put("archBusiUviewStrExList", archBusiUviewStrExList);
			message.put("assetCateExList", assetCateExList);
			//TODO  duanyan
//			mqResourceService.sendMessage(type, message);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public List<Map<String, Object>> getNewTop5Info(List<Integer> deptIds) {
		return this.baseMapper.getNewTop5Info(deptIds);
	}

	@Override
	public List<CataInfoDto> selectByTypeId(Integer depId, Integer typId) {
		return this.baseMapper.selectByTypeId(depId, typId);
	}

	@Override
	public List<Map<String, Object>> selectForType(Integer depId) {
		return this.baseMapper.selectForType(depId);
	}
}
