package com.digitalchina.resourcecatalog.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.digitalchina.resourcecatalog.db.domain.CataUserTypeRel;
import com.digitalchina.resourcecatalog.db.domain.DictAssetCate;
import com.digitalchina.resourcecatalog.db.dao.DictAssetCateMapper;
import com.digitalchina.resourcecatalog.db.domain.SysDict;
import com.digitalchina.resourcecatalog.db.service.CataUserTypeRelService;
import com.digitalchina.resourcecatalog.db.service.DictAssetCateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 目录分类管理 服务实现类
 * </p>
 *
 * @author baokd
 * @since 2020-05-12
 */
@Service
public class DictAssetCateServiceImpl extends ServiceImpl<DictAssetCateMapper, DictAssetCate> implements DictAssetCateService {
    @Autowired
    private CataUserTypeRelService cataUserTypeRelService;

    public Integer getMaxCodeByPid(Integer pid){
        return this.baseMapper.getMaxCodeByPid(pid);
    }

    public List<DictAssetCate> getTreeByPid(Integer pid){
        QueryWrapper<DictAssetCate> qw = new QueryWrapper<DictAssetCate>();
        qw.eq(DictAssetCate.STATUS,"1");
        qw.orderByAsc(DictAssetCate.DISPLAY_SN, DictAssetCate.TYP_CD);
        List<DictAssetCate> dictAssetCateListAll = this.baseMapper.selectList(qw);
        List<DictAssetCate> topList = null;
        if(StringUtils.isEmpty(pid)){
            topList = dictAssetCateListAll.stream().filter(item -> item.getParTypId()==null).collect(Collectors.toList());
        }else{
            topList = dictAssetCateListAll.stream().filter(item -> item.getParTypId()!=null && item.getParTypId().equals(pid)).collect(Collectors.toList());
        }
        dictAssetCateListAll.removeAll(topList);
        for (DictAssetCate dictAssetCate : topList) {
            dictAssetCate.setChildren(getChildrenTree(dictAssetCate.getTypId(), dictAssetCateListAll));
        }
        return topList;
    }

    public List<DictAssetCate> getTreeByPidAndOrgId(DictAssetCate dictAssetCate,List<Integer> orgIds){
        QueryWrapper<DictAssetCate> qw = new QueryWrapper<DictAssetCate>();
        if(StringUtils.isEmpty(dictAssetCate.getTypId())){
            qw.isNull(DictAssetCate.PAR_TYP_ID);
        }else{
            qw.eq(DictAssetCate.PAR_TYP_ID,dictAssetCate.getTypId());
        }
        if(!StringUtils.isEmpty(orgIds) && dictAssetCate.getFullTypCd().length() >= 3){
            qw.in(DictAssetCate.ORG_ID,orgIds);
        }
        qw.eq(DictAssetCate.STATUS,"1");
        qw.orderByAsc(DictAssetCate.DISPLAY_SN);
        qw.orderByAsc(DictAssetCate.TYP_CD);
        List<DictAssetCate> dictAssetCateList = this.baseMapper.selectList(qw);
        for (DictAssetCate dictAssetCateOne : dictAssetCateList) {
            dictAssetCateOne.setChildren(getChildrenTreeByPidAndOrgId(dictAssetCateOne,orgIds));
        }
        if("3".equals(dictAssetCate.getFullTypCd())){
	    	List<DictAssetCate> dictAssetList=new LinkedList<DictAssetCate>();
	    	for (DictAssetCate dictAsset : dictAssetCateList) {
				if(dictAsset.getChildren()!=null&&dictAsset.getChildren().size()>0){
					dictAssetList.add(dictAsset);
				}
			}
	    	return dictAssetList;
        }
        return dictAssetCateList;
    }

    public List<DictAssetCate> getTreeByPower(Integer userId){
        List<CataUserTypeRel> cataUserTypeRelList = cataUserTypeRelService.list(new QueryWrapper<CataUserTypeRel>().lambda()
                .eq(CataUserTypeRel::getUserId,userId));
        List<Integer> typeIdList = new ArrayList<Integer>();
        if(StringUtils.isEmpty(cataUserTypeRelList) || cataUserTypeRelList.size() == 0){
            return null;
        }else{
            for (CataUserTypeRel cataUserTypeRel:cataUserTypeRelList) {
                typeIdList.add(cataUserTypeRel.getTypeId());
            }
        }
        QueryWrapper<DictAssetCate> qw = new QueryWrapper<DictAssetCate>();
        qw.isNull(DictAssetCate.PAR_TYP_ID);
        qw.in(DictAssetCate.TYP_ID,typeIdList);
        qw.eq(DictAssetCate.STATUS,"1");
        qw.orderByAsc(DictAssetCate.DISPLAY_SN);
        qw.orderByAsc(DictAssetCate.TYP_CD);
        List<DictAssetCate> dictAssetCateList = this.baseMapper.selectList(qw);
        for (DictAssetCate dictAssetCate : dictAssetCateList) {
            dictAssetCate.setChildren(getChildrenTreeByPower(dictAssetCate.getTypId(),typeIdList));
        }
        return dictAssetCateList;
    }

    /***
     * 递归获取树
     * @param parentId
     * @return
     */
    private List<DictAssetCate> getChildrenTree(Integer parentId ,List<DictAssetCate> dictAssetCateListAll){
        List<DictAssetCate> list = new ArrayList<>();
        List<DictAssetCate> children = dictAssetCateListAll.stream().filter(item -> item.getParTypId()!=null && item.getParTypId().equals(parentId)).collect(Collectors.toList());
        dictAssetCateListAll.removeAll(children);
        for (DictAssetCate dictAssetCate : children) {
            dictAssetCate.setChildren(getChildrenTree(dictAssetCate.getTypId(), dictAssetCateListAll));
            list.add(dictAssetCate);
        }
        return list;
    }

    /***
     * 递归获取树
     * @param
     * @return
     */
    private List<DictAssetCate> getChildrenTreeByPidAndOrgId(DictAssetCate dictAssetCateOne,List<Integer> orgIds){
        List<DictAssetCate> list = new ArrayList<>();
        QueryWrapper<DictAssetCate> qw = new QueryWrapper<DictAssetCate>();
        qw.eq(DictAssetCate.PAR_TYP_ID,dictAssetCateOne.getTypId());
        if(!StringUtils.isEmpty(orgIds) && dictAssetCateOne.getFullTypCd().length() >= 3){
            qw.in(DictAssetCate.ORG_ID,orgIds);
        }
        qw.eq(DictAssetCate.STATUS,"1");
        qw.orderByAsc(DictAssetCate.DISPLAY_SN);
        qw.orderByAsc(DictAssetCate.TYP_CD);
        List<DictAssetCate> children = this.baseMapper.selectList(qw);
        for (DictAssetCate dictAssetCateTwo : children) {
            dictAssetCateTwo.setChildren(getChildrenTreeByPidAndOrgId(dictAssetCateTwo,orgIds));
            list.add(dictAssetCateTwo);
        }
        return list;
    }

    private List<DictAssetCate> getChildrenTreeByPower(Integer parentId,List<Integer> typeIdList) {
        List<DictAssetCate> list = new ArrayList<>();
        List<DictAssetCate> children = this.baseMapper.selectList(new QueryWrapper<DictAssetCate>().lambda().eq(DictAssetCate::getParTypId, parentId).in(DictAssetCate::getTypId,typeIdList).orderByAsc(DictAssetCate::getDisplaySn).orderByAsc(DictAssetCate::getTypCd));
        for (DictAssetCate dictAssetCate : children) {
            dictAssetCate.setChildren(getChildrenTreeByPower(dictAssetCate.getTypId(),typeIdList));
            list.add(dictAssetCate);
        }
        return list;
    }

    /**
     * 保存目录分类并更新权限
     * @param dictAssetCate
     * @return
     */
    @Transactional
    public boolean saveAndUpdateUserType(DictAssetCate dictAssetCate,Integer userId){
        //1、保存目录分类
        boolean flag1 = this.save(dictAssetCate);
        //2、根据UserId And Pid查询出数据，把isLeaf更新成 0 否
        LambdaQueryWrapper<CataUserTypeRel> qw = new QueryWrapper<CataUserTypeRel>().lambda()
        .eq(CataUserTypeRel::getUserId,userId)
        .eq(CataUserTypeRel::getTypeId,dictAssetCate.getParTypId());
        CataUserTypeRel cataUserTypeRel = cataUserTypeRelService.getOne(qw);
        cataUserTypeRel.setIsLeaf(0);
        boolean flag2 = cataUserTypeRelService.update(cataUserTypeRel,qw);
        //3、插入一条此用户的权限
        CataUserTypeRel cataUserTypeRelNew = new CataUserTypeRel();
        cataUserTypeRelNew.setUserId(userId);
        cataUserTypeRelNew.setTypeId(dictAssetCate.getTypId());
        cataUserTypeRelNew.setIsLeaf(1);
        boolean flag3 = cataUserTypeRelService.save(cataUserTypeRelNew);
        return flag1 && flag2 && flag3;
    }

    /**
     * 删除目录分类并更新权限
     * @param dictAssetCate
     * @return
     */
    @Transactional
    public boolean deleteAndUpdateUserType(DictAssetCate dictAssetCate){
        //1、删除目录分类
        boolean flag1 = this.removeById(dictAssetCate.getTypId());
        //2、删除cata_user_type_rel表中typeId为当前Id的记录
        boolean flag2 = cataUserTypeRelService.remove(new QueryWrapper<CataUserTypeRel>().lambda()
                .eq(CataUserTypeRel::getTypeId,dictAssetCate.getTypId()));
        //3、通过pid获取所有子类，把TypeId拼成List作为查询条件
        List<DictAssetCate> dictAssetCateList = this.list(new QueryWrapper<DictAssetCate>().lambda()
                .eq(DictAssetCate::getParTypId,dictAssetCate.getParTypId()));
        List<Integer> typeIdList = new ArrayList<Integer>();
        for (DictAssetCate dictAssetCateOne:dictAssetCateList) {
            typeIdList.add(dictAssetCateOne.getTypId());
        }
        //4、查询出所有有这个PID的用户
        List<CataUserTypeRel> cataUserTypeRelList = cataUserTypeRelService.list(
                new QueryWrapper<CataUserTypeRel>().lambda()
                .eq(CataUserTypeRel::getTypeId,dictAssetCate.getParTypId()));
        //4、查询所有用户下，cata_user_type_rel表中是否有这些子类
        for (CataUserTypeRel cataUserTypeRel:cataUserTypeRelList) {
            if(cataUserTypeRelService.count(new QueryWrapper<CataUserTypeRel>().lambda()
                .eq(CataUserTypeRel::getUserId,cataUserTypeRel.getUserId())
                .in(CataUserTypeRel::getTypeId,typeIdList)) == 0){
                //如果没有这些子类，更新此节点为叶子节点
                cataUserTypeRel.setIsLeaf(1);
                cataUserTypeRelService.update(cataUserTypeRel,new QueryWrapper<CataUserTypeRel>().lambda()
                        .eq(CataUserTypeRel::getTypeId,cataUserTypeRel.getTypeId())
                        .eq(CataUserTypeRel::getUserId,cataUserTypeRel.getUserId()));
            }//否则什么都不做
        }
        return flag1 && flag2;
    }

    @Override
    public Map<String, Object> generateCode(Integer pId) {
        Integer oldCode = getMaxCodeByPid(pId);
        oldCode = (oldCode == null ? 0 : oldCode);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (StringUtils.isEmpty(pId)) {
            //如果为NULL，则是类目录,类目录不能大于9

                String newCode = Integer.toString(oldCode + 1);
                resultMap.put("code", newCode);
                resultMap.put("fullCode", newCode);
                resultMap.put("typType", "类");

        } else {
            DictAssetCate dictAssetCate = getById(pId);
            //如果不为NULL,则通过full_typ_cd（详细编码）来判断是哪一层
            String currentFullTypeCode = dictAssetCate.getFullTypCd();
            if (currentFullTypeCode.length() == 1) {
                //如果当前详细编码长度为1，则是项目录，二位编码，不能大于99

                    String newCode = Integer.toString(oldCode + 1);
                    newCode = newCode.length() == 2 ? newCode : "0" + newCode;
                    resultMap.put("code", newCode);
                    resultMap.put("fullCode", currentFullTypeCode + newCode);
                    resultMap.put("typType", "项");


            } else if (currentFullTypeCode.length() == 3) {
                //如果当前详细编码长度为3，则是目目录，三位编码，不能大于999

                    String newCode = Integer.toString(oldCode + 1);
                    newCode = newCode.length() == 3 ? newCode : (newCode.length() == 2 ? "0" + newCode : "00" + newCode);
                    resultMap.put("code", newCode);
                    resultMap.put("fullCode", currentFullTypeCode + newCode);
                    resultMap.put("typType", "目");

            } else {
                //如果不是1或者3，则是细目，二位编码，不能大于99

                    String newCode = Integer.toString(oldCode + 1);
                    newCode = newCode.length() == 2 ? newCode : "0" + newCode;
                    resultMap.put("code", newCode);
                    resultMap.put("fullCode", currentFullTypeCode + newCode);
                    resultMap.put("typType", "细目");
                    resultMap.put("orgId", dictAssetCate.getOrgId());

                }
            }
        return resultMap;
    }
}
