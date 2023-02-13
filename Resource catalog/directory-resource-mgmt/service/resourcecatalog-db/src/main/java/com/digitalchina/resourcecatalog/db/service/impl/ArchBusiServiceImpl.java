package com.digitalchina.resourcecatalog.db.service.impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalchina.resourcecatalog.db.dao.ArchBusiMapper;
import com.digitalchina.resourcecatalog.db.dao.ArchOrgMapper;
import com.digitalchina.resourcecatalog.db.dao.CataBusInfoRelMapper;
import com.digitalchina.resourcecatalog.db.dao.CataOrgItemRelMapper;
import com.digitalchina.resourcecatalog.db.domain.*;
import com.digitalchina.resourcecatalog.db.service.ArchBusiService;
import com.digitalchina.resourcecatalog.db.service.ArchOrgService;
import com.digitalchina.resourcecatalog.db.service.CataAppBusRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * 权责清单 服务实现类
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
@Service
public class ArchBusiServiceImpl extends ServiceImpl<ArchBusiMapper, ArchBusi> implements ArchBusiService {

    @Autowired
    ArchOrgMapper archOrgMapper;

    @Autowired
    CataBusInfoRelMapper cataBusInfoRelMapper;

    @Autowired
    CataOrgItemRelMapper cataOrgItemRelMapper;

    @Autowired
    CataAppBusRelService cataAppBusRelService;

    @Autowired
    ArchOrgService archOrgService;

    @Value("${resourcecatalog.tree.init.dept}")
    private String initDept;

    @Override
    public List tree(boolean idSuperAdmin, List<Integer> deptIds){
        //判断用户的权限
        QueryWrapper<ArchOrg> queryWrapper = new QueryWrapper<ArchOrg>();
        queryWrapper.select(ArchOrg.ORG_ID, ArchOrg.PAR_ORG_ID, ArchOrg.ORG_CD, ArchOrg.ORG_NM, ArchOrg.SOCIAL_CREDIT_CD);

        if (!idSuperAdmin) {//只显示该部门
            if (deptIds == null || deptIds.size() < 1) {
                return null;
            }
            queryWrapper.in(ArchOrg.ORG_ID, deptIds).orderByAsc(ArchOrg.DISPALY_SN);
        } else {//全部
            queryWrapper.isNull(ArchOrg.PAR_ORG_ID).orderByAsc(ArchOrg.DISPALY_SN);
        }
        List<ArchOrg> archOrgList = archOrgMapper.selectList(queryWrapper);
        QueryWrapper<ArchBusi> busiWrapper = new QueryWrapper<ArchBusi>();
        busiWrapper.eq(ArchBusi.P_ID, 0).orderByAsc(ArchBusi.DEPT_ID).select(ArchBusi.DEPT_ID, ArchBusi.BUSI_NM, ArchBusi.BUSI_ID, ArchBusi.P_ID, ArchBusi.BUSI_NO);
        List<ArchBusi> archBusiList = this.list(busiWrapper);
        for (ArchOrg archOrg : archOrgList) {
            archOrg.setBusiNm(archOrg.getOrgNm());
            archOrg.setBusiNo(archOrg.getOrgCd());
            List<ArchBusi> targetList = archBusiList.stream().filter(item -> item.getDeptId()!=null && item.getDeptId().equals(archOrg.getOrgId())).collect(Collectors.toList());
            archOrg.setChildren(targetList);
        }
        ArchBusi archBusi = new ArchBusi();
        archBusi.setChildren(archOrgList);
        archBusi.setBusiNm(initDept);
        archBusi.setBusiId(0);
        archBusi.setBusiNo("0");
        List archOrgs = new ArrayList<>();
            archOrgs.add(archBusi);
            return archOrgs;
    }
    @Override
    public List treeByDeptId(Integer deptId){
        List<ArchBusi> tree = new ArrayList<ArchBusi>();
        List<ArchBusi> archBusis = this.baseMapper.selectList(new QueryWrapper<ArchBusi>().lambda().eq(ArchBusi::getDeptId, deptId));
        for (ArchBusi archBusi : archBusis) {
            if(archBusi.getBusiNm().length()>32){
                archBusi.setBusiNm(archBusi.getBusiNm().substring(0, 32)+ "...");
            }
            if (archBusi.getpId()==null || archBusi.getpId()==0){
                recursion(archBusis, archBusi);
                tree.add(archBusi);
            }
        }
        return tree;
    }
    private void recursion(List<ArchBusi> archBusiList, ArchBusi archBusi){
        for (ArchBusi tmp : archBusiList) {
            if(tmp.getBusiNm().length()>32){
                tmp.setBusiNm(tmp.getBusiNm().substring(0, 32) + "...");
            }
            if (tmp.getpId()!=null && tmp.getpId().equals(archBusi.getBusiId())){
                List<ArchBusi> children = archBusi.getChildren();
                if(children==null){
                    children = new ArrayList<>();
                }
                children.add(tmp);
                archBusi.setChildren(children);
                recursion(archBusiList, tmp);
            }
        }
    }
    /***
     * 权责清单Tree
     * @param pId
     * @return
     */
    private List<ArchBusi> treeBusi(Integer pId) {
        List<ArchBusi> archBusis = this.baseMapper.selectList(new QueryWrapper<ArchBusi>().lambda().eq(ArchBusi::getpId, pId).orderByAsc(ArchBusi::getBusiNo));
        for (ArchBusi archBusi : archBusis) {
            archBusi.setChildren(treeBusi(archBusi.getBusiId()));
        }
        return archBusis;
    }


    /***
     * 权责清单集合
     * @param name
     * @param isDept
     * @param id
     * @return
     */
    @Override
    public List selectByPage(String name, Integer isDept, Integer id, Integer filterCata) {
        List<ArchBusi> list = new ArrayList();
        if (isDept == 0) {//部门级 id就是部门id
            list = this.baseMapper.selectByDeptId(id, name, filterCata);
        }
        if (isDept == 1) {//权责清单级 id就是权责清单id
            if (id == 0) {
                return this.baseMapper.selectByDeptId(null, name, filterCata);
            }
//            treeBusiList(id, list);
//            list = search(name, list);
            list = this.baseMapper.selectByPid(id, filterCata);
        }
        return list;
    }

    @Override
    public ArchBusi detail(Integer busiId) {
        return this.baseMapper.detail(busiId);
    }

    @Override
    public Long getBusiNoMaxByPId(Integer pId, Integer deptId) {
        return this.baseMapper.getBusiNoMaxByPId(pId, deptId);
    }

    @Override
    @Transactional
    public boolean delete(Integer busiId) {
        QueryWrapper<ArchBusi> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ArchBusi::getpId, busiId);
        List<ArchBusi> archBusis = this.baseMapper.selectList(wrapper);
        //删除子节点
        for (ArchBusi archBusi : archBusis) {
            deleteTree(archBusi.getBusiId());
        }
        //如果是部门，则删除部门关联表
        if (this.baseMapper.selectById(busiId).getpId() == null) {
            cataOrgItemRelMapper.delete(new QueryWrapper<CataOrgItemRel>().lambda().eq(CataOrgItemRel::getItemId, busiId));
        }
        //删除信息资源关联表
        cataBusInfoRelMapper.delete(new QueryWrapper<CataBusInfoRel>().lambda().eq(CataBusInfoRel::getBusId, busiId));
        //删除自己
        this.baseMapper.deleteById(busiId);
        return true;
    }

    @Override
    public List<ArchBusi> getBusInfoByOrgId(Integer orgId, String containsChild) {
        QueryWrapper<ArchBusi> archBusiQueryWrapper = new QueryWrapper<>();
        archBusiQueryWrapper.eq(ArchBusi.DEPT_ID, orgId);
        if(containsChild.equals("0")){
            archBusiQueryWrapper.and(wrapper -> wrapper.isNull("p_id").or(wrapper2 -> wrapper2.eq("p_id", 0)));
        }
        archBusiQueryWrapper.orderByAsc("p_id", "busi_no");
       return this.list(archBusiQueryWrapper);
//        return this.baseMapper.getBusInfoByOrgId(orgId, containsChild);
    }

    @Override
    @Transactional
    public boolean saveOrUpdateInfo(ArchBusi busi, List<Integer> appIds) {
        boolean flag = false;
        if (busi.getBusiId() == null) {//新增
            this.save(busi);
            flag = true;
        } else {
            this.updateById(busi);
            //删除关联项
            cataAppBusRelService.remove(new QueryWrapper<CataAppBusRel>().lambda().eq(CataAppBusRel::getItemId, busi.getBusiId()));
        }
        if (appIds != null && appIds.size() > 0) {
            List<CataAppBusRel> list = new ArrayList<>(appIds.size());
            CataAppBusRel cataAppBusRel;
            for (Integer appId : appIds) {
                cataAppBusRel = new CataAppBusRel();
                cataAppBusRel.setItemId(busi.getBusiId());
                cataAppBusRel.setAppId(appId);
                list.add(cataAppBusRel);
            }
            cataAppBusRelService.saveBatch(list);
        }
        return flag;
    }

    @Override
    public List<ArchAppSys> getAppListByBusiId(Integer busiId) {
        return this.baseMapper.getAppListByBusiId(busiId);
    }

    @Override
    public IPage<List<Map<String, Object>>> cataInfoPage(Page page, Integer orgId, String cataName, List<Integer> infoIds, Integer type) {
        return this.baseMapper.cataInfoPage(page, orgId, cataName, infoIds, type);
    }

    @Override
    public int countByDepts(List<Integer> deptIds) {
        return this.baseMapper.countByDepts(deptIds);
    }


    /***
     * 删除信息资源关联表
     * 删除树
     * @param pId
     */
    private void deleteTree(Integer pId) {
        QueryWrapper<ArchBusi> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ArchBusi::getpId, pId);
        List<ArchBusi> archBusis = this.baseMapper.selectList(wrapper);
        for (ArchBusi archBusi : archBusis) {
            deleteTree(archBusi.getBusiId());
        }
        //删除信息资源关联表
        cataBusInfoRelMapper.delete(new QueryWrapper<CataBusInfoRel>().lambda().eq(CataBusInfoRel::getBusId, pId));
        //删除自己
        this.baseMapper.deleteById(pId);
    }


    /***
     * 获取权责清单子节点集合
     * @param pId
     * @param list
     */
//    private void treeBusiList(Integer pId, List<ArchBusi> list) {
//        List<ArchBusi> busisList = this.baseMapper.selectByPid(pId);
//        list.addAll(busisList);
//        for (ArchBusi archBusi : busisList) {
//            treeBusiList(archBusi.getBusiId(), list);
//        }
//    }

    /**
     * 手动模糊查询
     *
     * @param name
     * @param list
     * @return
     */
    private List search(String name, List<ArchBusi> list) {
        if (StringUtils.isEmpty(name)) {
            return list;
        }
        List results = new ArrayList();
        Pattern pattern = Pattern.compile(name);
        for (int i = 0; i < list.size(); i++) {
            Matcher matcher = pattern.matcher(list.get(i).getBusiNm());
            if (matcher.find()) {
                results.add(list.get(i));
            }
        }
        return results;
    }

    @Override
    public List<ArchBusi> getExportBusisByDepId(Integer depId) {
        return this.baseMapper.getExportBusisByDepId(depId);
    }

    @Override
    public String importBusisExcel(InputStream inputStream) throws IOException {
        //所有的机构
        List<ArchOrg> orgList = archOrgMapper.selectList(null);
        Map<String, ArchOrg> archOrgMap = new HashMap<>();
        orgList.forEach(item-> {
            item.setOrgDuty("");
            archOrgMap.put(item.getOrgNm().trim(), item);
        });
        //已存在的权责清单
        List<ArchBusi> busiList = this.baseMapper.selectListContainsOrgNm(null);
        Map<String, ArchBusi> archBusiMap = new HashMap<>();
        busiList.forEach(item-> {
            archBusiMap.put(item.getOrgNm().trim() + "-" + item.getBusiNm().trim(), item);
        });
        Map<Integer, Integer> firstLevelMaxMap = new HashMap<>();
        this.baseMapper.getFirstLevelMaxDeptBusiNo().forEach(map->firstLevelMaxMap.put(map.get("deptid"),map.get("maxno")));
        try{
            ExcelReader reader = ExcelUtil.getReader(inputStream, 0);
            List<List<Object>> result = reader.read(3);
            ArchOrg currentArchOrg = null;//当前处理的机构
            ArchBusi currentArchBusi = null;//当前处理的一级全责清单
            List<ArchBusi> insertList = new ArrayList<>(), updateList = new ArrayList<>();
            StringBuffer orgDuty = new StringBuffer();
            for(int i=0; i< result.size();i++){
                //机构
                String orgName = result.get(i).get(0).toString();
                if(!StringUtils.isEmpty(orgName)){
                    if(currentArchOrg!=null){//上一个 更新机构
                        currentArchOrg.setOrgDuty(orgDuty.toString());
                        archOrgMapper.updateById(currentArchOrg);
                        orgDuty = new StringBuffer();
                    }
                    //第一次和有值时
                    currentArchOrg = archOrgMap.get(orgName.trim());
                }
                //第一级//实时保存
                String firstBusiName = result.get(i).get(1).toString();
                if(!StringUtils.isEmpty(firstBusiName)){
                    orgDuty.append(firstBusiName).append("\n");
                    String key = currentArchOrg.getOrgNm().trim() + "-" + firstBusiName.trim();
                    if(archBusiMap.containsKey(key)){
                        currentArchBusi = archBusiMap.get(key);//不用更新了
                    }else{
                        Integer deptmax = firstLevelMaxMap.get(currentArchOrg.getOrgId());
                        if(deptmax==null){
                            deptmax =0;
                        }
                        deptmax++;
                        firstLevelMaxMap.put(currentArchOrg.getOrgId(), deptmax);
                        StringBuffer buffer = new StringBuffer();
                        buffer.append("B");
                        buffer.append(currentArchOrg.getOrgCd().length() % 2 == 0 ? currentArchOrg.getOrgCd() : "0" + currentArchOrg.getOrgCd());
                        buffer.append(String.format("%04d",deptmax));
                        currentArchBusi = initArchBusi(firstBusiName, buffer.toString(), 0, currentArchOrg.getOrgId(), currentArchOrg.getOrgNm(), null);
                        this.baseMapper.insert(currentArchBusi);
                    }
                }
                //第二级
                String secondBusiName = result.get(i).get(2).toString();
                String secondBusiType = result.get(i).get(3).toString();
                String secondBusiNo = result.get(i).get(4).toString();
                String secondKey = currentArchOrg.getOrgNm().trim() + "-" + secondBusiName.trim();
                ArchBusi archBusiSecond = null;
                orgDuty.append("  ").append(secondBusiName).append("\n");
                if(archBusiMap.containsKey(secondKey)){
                    archBusiSecond = archBusiMap.get(secondKey);//不用更新了
                    setArchBusi(archBusiSecond, secondBusiName, secondBusiNo, currentArchBusi.getBusiId(), currentArchOrg.getOrgId(), currentArchOrg.getOrgNm(), secondBusiType);
                    updateList.add(archBusiSecond);
                }else{
                    archBusiSecond = initArchBusi(secondBusiName, secondBusiNo, currentArchBusi.getBusiId(), currentArchOrg.getOrgId(), currentArchOrg.getOrgNm(), secondBusiType);
                    insertList.add(archBusiSecond);
                }
            }
            if(currentArchOrg!=null){
                currentArchOrg.setOrgDuty(orgDuty.toString());
                archOrgMapper.updateById(currentArchOrg);
            }
            if(updateList.size()>0){
                this.updateBatchById(updateList, 500);
            }
            if(insertList.size()>0){
                this.saveBatch(insertList, 500);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            inputStream.close();
        }
        return null;
    }
    private ArchBusi setArchBusi(ArchBusi archBusi, String busiNm, String busiNo, Integer pId, Integer deptId, String orgNm, String busiType) {
        archBusi.setBusiNm(busiNm);
        archBusi.setBusiNo(busiNo);
        archBusi.setpId(pId);
        archBusi.setDeptId(deptId);
        archBusi.setOrgNm(orgNm);
        archBusi.setOrgDuty(busiNm);
        archBusi.setBusiType(busiType);
        archBusi.setCreatedTime(new Date());
        archBusi.setCreatedBy("System");
        archBusi.setUpdatedTime(new Date());
        archBusi.setUpdatedBy("System");
        archBusi.setRemark("");
        return archBusi;
    }
    private ArchBusi initArchBusi(String busiNm, String busiNo, Integer pId, Integer deptId, String orgNm, String busiType) {
        ArchBusi archBusi = new ArchBusi();
        return setArchBusi(archBusi, busiNm, busiNo, pId, deptId, orgNm, busiType);
    }
    public List<ArchBusi> selectListContainsOrgNm(List<Integer> deptIds){
        return this.baseMapper.selectListContainsOrgNm(deptIds);
    }

    @Override
    public Map getBusiNo(Integer deptId, Integer pId) {
        Map map = new HashMap();
        StringBuffer buffer = new StringBuffer();
        Long busiNoMax = getBusiNoMaxByPId(pId, deptId);
        if (pId == null|| pId==0) {//可能是第一级第一个 或属于第一级
            ArchOrg byId = archOrgService.getById(deptId);
            buffer.append("B");
            buffer.append(byId.getOrgCd().length() % 2 == 0 ? byId.getOrgCd() : "0" + byId.getOrgCd());
            if (busiNoMax == null) {//说明是第一级第一个
                map.put("busiNo", buffer.append("0001"));
                return map;
            }else{
                map.put("busiNo", buffer.append(String.format("%04d",busiNoMax + 1)));
            }

        } else {
            QueryWrapper<ArchBusi> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(ArchBusi::getBusiId, pId);
            ArchBusi one = getOne(wrapper);
            buffer.append(one.getBusiNo());
            if (busiNoMax == null) {//可能是子集第一个
                map.put("busiNo", buffer.append("0001"));
            }else{
                map.put("busiNo", buffer.append(String.format("%04d",busiNoMax + 1)));
            }
        }
        return map;
    }
}
