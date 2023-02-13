package com.digitalchina.resourcecatalog.admin;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.digitalchina.resourcecatalog.admin.util.GB2Alpha;
import com.digitalchina.resourcecatalog.core.util.BeanUtils;
import com.digitalchina.resourcecatalog.db.domain.*;
import com.digitalchina.resourcecatalog.db.service.*;
import jodd.util.StringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: create by wangmh
 * @name: ImportTest.java
 * @description:
 * @date:2020/6/16
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ResourceCatalogImport {
    private static final String FILE_PATH = "D:\\项目\\天津河北\\资源目录\\5.23\\信息类情况.xlsx";
    private static final String CREATEDBY = "System";

    @Autowired
    ArchOrgService archOrgService;

    @Autowired
    DictAssetCateService dictAssetCateService;

    @Autowired
    CataInfoTempService cataInfoTempService;

    @Autowired
    SysDictService sysDictService;

    @Autowired
    CataInfoItemTempRelService cataInfoItemTempRelService;

    @Autowired
    CataInfoTempTypeRelService cataInfoTempTypeRelService;

    @Autowired
    AssetCateExService assetCateExService;

    @Autowired
    ArchBusiUviewStrExService archBusiUviewStrExService;

    @Autowired
    ArchBusiUviewExService archBusiUviewExService;

    @Autowired
    CataInfoHistoryService cataInfoHistoryService;

    @Autowired
    CataInfoItemHistoryRelService cataInfoItemHistoryRelService;

    @Autowired
    CataInfoHistoryTypeRelService cataInfoHistoryTypeRelService;

    @Autowired
    CataImportService cataImportService;

    private List<Exception> info = new ArrayList();
    //所有顶级部门  key-部门名称 value-顶级部门
    Map<String, ArchOrg> orgMap = new HashMap<>();

    //当前部门表中的最大ID
    int maxOrgId = 1;
    //二级部门中最大的部门编码（3-4位） key-顶级部门id value-最大编码
    Map<Integer, Integer> orgMaxSecondLevelMap = new HashMap<>();
    //二级部门 key-(顶级部门名称-内设部门名称) value-内设部门
    Map<String, ArchOrg> orgSecondMap = new HashMap<>();

    //信息分类-部门类细目级别  key-顶级部门ID value-分类
    Map<Integer, DictAssetCate> defaultDictMap = new HashMap<>();
    //信息资源格式
    Map<String, SysDict> sysDictParentMap = new HashMap<>();
    //信息资源格式分类
    Map<String, SysDict> sysDictChildMap = new HashMap<>();
    //更新周期
    Map<String, SysDict> sysDictUpdateMap = new HashMap<>();
    //信息资源目录的部门分类对应的编码 key-部门类的fullTypCd value-当前最大值
    Map<String, Integer> dictAssetMap = new HashMap<>();
    /***
     * 总导入入口
     * @throws ParseException
     *
     */
    @Test
    public void totalImport() throws ParseException {
        //部门
        //顶级部门数据first level
        QueryWrapper<ArchOrg> qwOrgFirst = new QueryWrapper<ArchOrg>();
        qwOrgFirst.isNull(ArchOrg.PAR_ORG_ID);
        List<ArchOrg> orgList = archOrgService.list(qwOrgFirst);
        orgMap.clear();
        orgList.forEach(org->orgMap.put(org.getOrgNm(), org));
        //机构表中最大id
        QueryWrapper<ArchOrg> qwOrgMaxId = new QueryWrapper<ArchOrg>();
        qwOrgMaxId.orderByDesc(ArchOrg.ORG_ID);
        qwOrgMaxId.last("limit 1");
        ArchOrg maxArchOrg = archOrgService.getOne(qwOrgMaxId);
        if(maxArchOrg!=null){
            maxOrgId = maxArchOrg.getOrgId();
        }
        //二级部门的最大id
        List<Map<String, Object>> maxSecondLevelList = archOrgService.getMaxSecondLevel();
        maxSecondLevelList.forEach(
                item->{
                    Integer orgId = Integer.parseInt(item.get("par_org_id").toString());
                    Integer maxSn = Integer.parseInt(item.get("maxsn").toString());
                    orgMaxSecondLevelMap.put(orgId, maxSn);
                }
        );
        //默认的目录分类到细目
        QueryWrapper<DictAssetCate> qwDict = new QueryWrapper<DictAssetCate>();
        qwDict.eq(DictAssetCate.TYP_TYPE, "细目");
        qwDict.orderByAsc(DictAssetCate.DISPLAY_SN);
        List<DictAssetCate> dictList = dictAssetCateService.list(qwDict);
        dictList.forEach(dict->{
//            for (int i=0;i< orgList.size();i++){
//                if(orgList.get(i).getOrgId().equals(dict.getOrgId()) && !defaultDictMap.containsKey(dict.getOrgId())){
                    defaultDictMap.put(dict.getOrgId(), dict);
//                }
//            }
        });
        //信息资源格式
        QueryWrapper<SysDict> qwSysDict = new QueryWrapper<SysDict>();
        qwSysDict.eq("type", "format_type");
        List<SysDict> sysDictList = sysDictService.list(qwSysDict);
        for (int i=0; i< sysDictList.size();i++){
            SysDict sysDict = sysDictList.get(i);
            sysDictParentMap.put(sysDict.getName(), sysDict);
            //信息资源格式
            QueryWrapper<SysDict> qwSysDictChild = new QueryWrapper<SysDict>();
            qwSysDictChild.eq("type", "format_type_" + sysDict.getValue());
            List<SysDict> sysDictListChild = sysDictService.list(qwSysDictChild);
            for (int j=0;j<sysDictListChild.size();j++){
                SysDict sysDictChild = sysDictListChild.get(j);
                sysDictChildMap.put(sysDict.getName()+"-" + sysDictChild.getName(), sysDictChild);
            }
        }
        //更新周期
        QueryWrapper<SysDict> qwUpdate = new QueryWrapper<SysDict>();
        qwUpdate.eq("type", "update_cycle");
        List<SysDict> sysDictUpdateList = sysDictService.list(qwUpdate);
        sysDictUpdateList.forEach(item -> sysDictUpdateMap.put(item.getName(), item));

        //导入公安局-验收交付版
        importSheel2();
    }
    private String getCataCodeByOrgName(String orgName){
        ArchOrg archOrg = orgMap.get(orgName);
        DictAssetCate dict = defaultDictMap.get(archOrg.getOrgId());
        Integer current = 0;
        if(dictAssetMap.containsKey(dict.getFullTypCd())){
            current = dictAssetMap.get(dict.getFullTypCd());
        }
        current++;
        dictAssetMap.put(dict.getFullTypCd(), current);
        return dict.getFullTypCd()+"/"+String.format("%06d",current);
    }
    /***
     * 公安局-验收交付版
     */
    public void importSheel2() throws ParseException {
        ExcelReader reader;
        reader = ExcelUtil.getReader(FILE_PATH, 0);
        List<List<Object>> result = reader.read(1);
        int successCount = 0;
        int errorCount = 0;
        StringBuilder sb = new StringBuilder();
        int current = 1;

        int currentCataTmpId = cataImportService.getSeqNext("cata_info_temp_seq");
        String currentCataTmpNm = "";
        int cataItemOrder = 1;
        List<CataInfoTemp> cataInfoTempList = new ArrayList<>(result.size());
        List<CataInfoItemTempRel> cataInfoItemTempRelList = new ArrayList<>(result.size());
        List<CataInfoTempTypeRel> cataInfoTempTypeRelList = new ArrayList<>(result.size());
        List<ArchOrg> secondArchOrgList = new ArrayList<>();
        for (List<Object> item : result) {
            try {
//                importCata(item);
                //++++++++++++++++++++++++++++++++++++++++++++++

                //目录
                String city_cata_code = subCellStr(item.get(0));
                String uview_nm = subCellStr(item.get(1));
                String uview_desc = subCellStr(item.get(2));
                String media_fmt_all = subCellStr(item.get(3));
                String audit_status = "5";
                //共享类型
                String share_lv_str = subCellStr(item.get(11));
                String share_lv = null;
                if(share_lv_str.contains("无条件")){
                    share_lv = "01";
                }else if(share_lv_str.contains("有条件")){
                    share_lv = "02";
                }else{
                    share_lv = "03";
                }
                //共享条件
                String share_condition = subCellStr(item.get(12));
                //共享方式
                String share_way = subCellStr(item.get(13));
                //开放类型（02-否；01-是）
                String pubSts_str = subCellStr(item.get(14));
                String pubSts  = null;
                if(pubSts_str.contains("无条件")){
                    pubSts = "01";
                }else if(pubSts_str.contains("有条件")){
                    pubSts = "02";
                }else{
                    pubSts = "03";
                }
                //开放条件
                String pub_condition = subCellStr(item.get(15));
                //更新周期
                String updateCyc = subCellStr(item.get(16));
                //数据范围
                String data_scope = subCellStr(item.get(20));

                String orgSecondName = subCellStr(item.get(21));

                String orgName = subCellStr(item.get(22));

                //发布日期
                Date updateDt = getDate(subCellStr(item.get(25)));

                if(!uview_nm.equals(currentCataTmpNm)){ //新的目录数据

                    //
                    cataItemOrder = 1;
                    currentCataTmpId++;
                    currentCataTmpNm = uview_nm;

                    CataInfoTemp cataInfoTemp = new CataInfoTemp();
                    List<String> secondOrgNameList = null;
                    //部门科室
                    if(!orgSecondName.contains(orgName)){
                        if(orgSecondName.contains("、")){
                            secondOrgNameList = Arrays.asList(orgSecondName.split("、"));
                        }else if(orgSecondName.contains("，")){
                            secondOrgNameList = Arrays.asList(orgSecondName.split("，"));
                        }else{
                            secondOrgNameList = Arrays.asList(orgSecondName);
                        }
                        List<ArchOrg> belongArchOrgs = addSecondArchOrgReturnFirst(orgMap.get(orgName), secondOrgNameList, secondArchOrgList);
                        if(belongArchOrgs!=null && belongArchOrgs.size()>0){
                            List<String> belongTo = new ArrayList<>(), belongToCode= new ArrayList<>();
                            for(int j=0;j< belongArchOrgs.size();j++){
                                belongTo.add(belongArchOrgs.get(j).getOrgId().toString());
                                belongToCode.add(belongArchOrgs.get(j).getOrgCd());
                            }
                            cataInfoTemp.setBelongTo(String.join(",", belongTo));
                            cataInfoTemp.setBelongToCode(String.join(",", belongToCode));
                        }
                    }

                    //信息资源目录
                    cataInfoTemp.setUviewNm(uview_nm==null?"":uview_nm);
                    cataInfoTemp.setUviewNo(getCataCodeByOrgName(orgName));
                    cataInfoTemp.setProvOrgId(orgMap.get(orgName).getOrgId());
                    cataInfoTemp.setProvOrgCode(orgMap.get(orgName).getSocialCreditCd());
                    cataInfoTemp.setMediaFmt(sysDictParentMap.get(media_fmt_all.split("-")[0]).getValue());
                    SysDict mediaChild = sysDictChildMap.get(media_fmt_all);
                    if(mediaChild==null){
                        mediaChild = sysDictChildMap.get(media_fmt_all.split("-")[0] + "-其他");
                    }
                    cataInfoTemp.setMediaFmtType(mediaChild.getValue());
                    cataInfoTemp.setUpdateCyc(sysDictUpdateMap.get(updateCyc).getValue());
                    cataInfoTemp.setPubDt(updateDt);
                    cataInfoTemp.setUviewDesc(uview_desc==null ? "": uview_desc);
                    cataInfoTemp.setShareLv(share_lv);
                    cataInfoTemp.setShareCondition(share_condition==null? "无" : share_condition);
                    cataInfoTemp.setPubSts(pubSts);
                    cataInfoTemp.setPubCondition(pub_condition==null?"无":pub_condition);
                    cataInfoTemp.setAuditStatus(audit_status);
                    cataInfoTemp.setDeleted(0);
                    cataInfoTemp.setShareScope("全市");
                    cataInfoTemp.setDataScope(data_scope==null?"":data_scope);
                    cataInfoTemp.setSecret(0);
                    cataInfoTemp.setRelBusi(0);
                    cataInfoTemp.setRelBusiMsg("导入不关联");
                    cataInfoTemp.setShareWay(share_way==null?"":share_way);
                    cataInfoTemp.setCityCataCode(city_cata_code);
                    cataInfoTemp.setCreator(CREATEDBY);
                    cataInfoTemp.setCrtDt(updateDt);
                    cataInfoTemp.setUpdater(CREATEDBY);
                    cataInfoTemp.setUpdateDt(updateDt);
                    cataInfoTempList.add(cataInfoTemp);
                    //信息资源与信息类型关联表（临时表）
                    CataInfoTempTypeRel cataInfoTempTypeRel = new CataInfoTempTypeRel();
                    cataInfoTempTypeRel.setInfoId(currentCataTmpId);
                    cataInfoTempTypeRel.setType("3");
                    cataInfoTempTypeRel.setTypeId(defaultDictMap.get(orgMap.get(orgName).getOrgId()).getTypId());
                    cataInfoTempTypeRelList.add(cataInfoTempTypeRel);
                }
                //信息项
                //存入ext20
                String code = subCellStr(item.get(5));
                String src_field = subCellStr(item.get(6));
                src_field = StringUtils.isEmpty(src_field)? "信息项名称未填写" : src_field;
                String src_data_typ = subCellStr(item.get(8));
                String data_len = subCellStr(item.get(9));
                String item_remark = subCellStr(item.get(10));

                //信息项英文名 无 需要生成
                String engCd = StringUtils.isEmpty(src_field)? "" : (new GB2Alpha()).String2Alpha(src_field);
                //信息项数据类型
                String srcDataTyp = StringUtils.isEmpty(src_data_typ)? "STRING" : (src_data_typ.equals("Varchar2") ? "STRING" : "DATE");

                CataInfoItemTempRel rel = new CataInfoItemTempRel();
                rel.setUviewId(currentCataTmpId);
                rel.setSrcField(src_field==null?"":src_field);
                rel.setEngCd(engCd==null?"":engCd);
                rel.setExt20(code==null?"":code);
                rel.setSrcDataTyp(srcDataTyp==null?"":srcDataTyp);
                rel.setDataLen(data_len==null?"":data_len);
                rel.setItemRemark(item_remark==null?"":item_remark);
                rel.setSno(cataItemOrder++);
                cataInfoItemTempRelList.add(rel);

                //+++++++++++++++++++++++++++++++++++++++++++++
                successCount++;
            } catch (Exception e) {
                e.printStackTrace();
                errorCount++;
                sb.append("importSheel2---序号：" + current + "，出现错误！！，异常" + e.getMessage());
                sb.append("\r\n");
                info.add(e);
            }
            current++;
        }
        //机构
        if(secondArchOrgList.size()>0){
            archOrgService.saveBatch(secondArchOrgList, 1000);
        }
        //
        if(cataInfoTempList.size()>0){
            cataInfoTempService.saveBatch(cataInfoTempList, 1000);
        }
        System.out.println("======================================");
        if(cataInfoItemTempRelList.size()>0){
            cataInfoItemTempRelService.saveBatch(cataInfoItemTempRelList, 1000);
        }
        System.out.println("======================================");

        if(cataInfoTempTypeRelList.size()>0){
            cataInfoTempTypeRelService.saveBatch(cataInfoTempTypeRelList, 1000);
        }
        //复制资源目录、信息项、关联表临时表数据到正式表、历史表
        cpoyTempToExAndHistory();
        //打印错误信息
        System.out.println("importSheel2---成功导入：" + successCount + "条，失败导入：" + errorCount + "条");
        System.out.println("importSheel2---错误原因：" + sb.toString());
        info.forEach(e -> {
            e.printStackTrace();
            System.out.println("================");
        });
    }
    /***
     * 复制资源目录、信息项、关联表临时表数据到正式表、历史表
     */
    @Test
    public void cpoyTempToExAndHistory() {
        //TODO 同步资源目录临时表到正式表、历史表
        List<CataInfoTemp> cataInfoTempList = cataInfoTempService.list(new QueryWrapper<CataInfoTemp>());
        List<ArchBusiUviewEx> archBusiUviewExes = new ArrayList<>();
        List<CataInfoHistory> cataInfoHistories = new ArrayList<>();
        ArchBusiUviewEx archBusiUviewEx = null;
        CataInfoHistory cataInfoHistory = null;
        for (CataInfoTemp cataInfoTemp : cataInfoTempList) {
            archBusiUviewEx = BeanUtils.doToDto(cataInfoTemp, ArchBusiUviewEx.class);
            archBusiUviewExes.add(archBusiUviewEx);
            cataInfoHistory = BeanUtils.dtoToDo(cataInfoTemp, CataInfoHistory.class);
            cataInfoHistory.setIsCurrent("1");
            cataInfoHistories.add(cataInfoHistory);
        }
        archBusiUviewExService.saveBatch(archBusiUviewExes);
        cataInfoHistoryService.saveBatch(cataInfoHistories);

        //TODO 同步信息项临时表到正式表、历史表
        List<CataInfoItemTempRel> cataInfoItemTempRels = cataInfoItemTempRelService.list(new QueryWrapper<CataInfoItemTempRel>());
        List<ArchBusiUviewStrEx> archBusiUviewStrExes = new ArrayList<>();
        List<CataInfoItemHistoryRel> cataInfoItemHistoryRels = new ArrayList<>();
        ArchBusiUviewStrEx archBusiUviewStrEx = null;
        CataInfoItemHistoryRel cataInfoItemHistoryRel = null;
        for (CataInfoItemTempRel cataInfoItemTempRel : cataInfoItemTempRels) {
            archBusiUviewStrEx = BeanUtils.doToDto(cataInfoItemTempRel, ArchBusiUviewStrEx.class);
            archBusiUviewStrExes.add(archBusiUviewStrEx);
            cataInfoItemHistoryRel = BeanUtils.dtoToDo(cataInfoItemTempRel, CataInfoItemHistoryRel.class);

            cataInfoItemHistoryRels.add(cataInfoItemHistoryRel);
        }
        archBusiUviewStrExService.saveBatch(archBusiUviewStrExes);
        cataInfoItemHistoryRelService.saveBatch(cataInfoItemHistoryRels);

        //TODO 同步关联表临时表到正式表、历史表
        List<CataInfoTempTypeRel> cataInfoTempTypeRels = cataInfoTempTypeRelService.list(new QueryWrapper<CataInfoTempTypeRel>());
        List<AssetCateEx> assetCateExes = new ArrayList<>();
        List<CataInfoHistoryTypeRel> cataInfoHistoryTypeRels = new ArrayList<>();
        AssetCateEx assetCateEx = null;
        CataInfoHistoryTypeRel cataInfoHistoryTypeRel = null;
        for (CataInfoTempTypeRel cataInfoTempTypeRel : cataInfoTempTypeRels) {
            assetCateEx = BeanUtils.doToDto(cataInfoTempTypeRel, AssetCateEx.class);
            assetCateExes.add(assetCateEx);
            cataInfoHistoryTypeRel = BeanUtils.dtoToDo(cataInfoTempTypeRel, CataInfoHistoryTypeRel.class);
            cataInfoHistoryTypeRels.add(cataInfoHistoryTypeRel);
        }
        assetCateExService.saveBatch(assetCateExes);
        cataInfoHistoryTypeRelService.saveBatch(cataInfoHistoryTypeRels);
    }

    private String subCellStr(Object object) {
        return subCellStr(object, 100);
    }

    private static String subCellStr(Object obj, Integer length) {
        if (obj == null) {
            return "";
        } else {
            return StrUtil.subSufByLength(StrUtil.trim(obj.toString()), length);
        }
    }

    private Date getDate(String dateStr) throws ParseException {
        if (StringUtil.isEmpty(dateStr)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//注意月份是MM
        return simpleDateFormat.parse(dateStr);
    }
    private List<ArchOrg> addSecondArchOrgReturnFirst(ArchOrg parentArchOrg, List<String> secondNameList,  List<ArchOrg> secondArchOrgList){
        List<ArchOrg> currentList = new ArrayList<>();
        for (int i=0; i< secondNameList.size();i++){
            String secondName = secondNameList.get(i);
            if(!orgSecondMap.containsKey(parentArchOrg.getOrgNm() + "-" + secondName)){
                maxOrgId++;
                ArchOrg secondArchOrg = new ArchOrg();
                secondArchOrg.setOrgId(maxOrgId);
                secondArchOrg.setOrgNm(secondName);
                String parentFormatter = String.format("%02d",parentArchOrg.getOrgId());
                if(!orgMaxSecondLevelMap.containsKey(parentArchOrg.getOrgId())){
                    orgMaxSecondLevelMap.put(parentArchOrg.getOrgId(), 0);
                }
                int currentMax = orgMaxSecondLevelMap.get(parentArchOrg.getOrgId());
                currentMax++;
                orgMaxSecondLevelMap.put(parentArchOrg.getOrgId(), currentMax);
                String secondFormatter = String.format("%02d",currentMax);
                secondArchOrg.setOrgCd(parentFormatter+secondFormatter);

                secondArchOrg.setCreator(CREATEDBY);
                secondArchOrg.setCrtDt(new Date());
                secondArchOrg.setOrgAlias(secondName);
                secondArchOrg.setDispalySn(i+1);
                secondArchOrg.setParOrgId(parentArchOrg.getOrgId());

                orgSecondMap.put(parentArchOrg.getOrgNm() + "-" + secondName, secondArchOrg);
                secondArchOrgList.add(secondArchOrg);
            }
            currentList.add(orgSecondMap.get(parentArchOrg.getOrgNm() + "-" + secondName));
        }
//        if(secondNameList.size()>0){
//            return orgSecondMap.get(parentArchOrg.getOrgNm() + "-" + secondNameList.get(0));
//        }
        return currentList;
    }
    @Test
    public void update() {
        List<CataInfoTempTypeRel> cataInfoTempTypeRels = cataInfoTempTypeRelService.list(new QueryWrapper<CataInfoTempTypeRel>().lambda().eq(CataInfoTempTypeRel::getType, "3"));
        CataInfoTempTypeRel cataInfoTempTypeRel = new CataInfoTempTypeRel();
        cataInfoTempTypeRels.forEach(item -> {
            DictAssetCate one = dictAssetCateService.getOne(new QueryWrapper<DictAssetCate>().lambda().eq(DictAssetCate::getParTypId, item.getTypeId()));
            cataInfoTempTypeRel.setInfoId(item.getInfoId());
            cataInfoTempTypeRel.setType("3");
            cataInfoTempTypeRel.setTypeId(one.getTypId());
            cataInfoTempTypeRelService.update(cataInfoTempTypeRel, new QueryWrapper<CataInfoTempTypeRel>().lambda().eq(CataInfoTempTypeRel::getInfoId, item.getInfoId()).eq(CataInfoTempTypeRel::getType, "3"));
        });

        List<CataInfoHistoryTypeRel> cataInfoHistoryTypeRels = cataInfoHistoryTypeRelService.list(new QueryWrapper<CataInfoHistoryTypeRel>().lambda().eq(CataInfoHistoryTypeRel::getType, "3"));
        CataInfoHistoryTypeRel cataInfoHistoryTypeRel = new CataInfoHistoryTypeRel();
        cataInfoHistoryTypeRels.forEach(item -> {
            DictAssetCate one = dictAssetCateService.getOne(new QueryWrapper<DictAssetCate>().lambda().eq(DictAssetCate::getParTypId, item.getTypeId()));
            cataInfoHistoryTypeRel.setInfoId(item.getInfoId());
            cataInfoHistoryTypeRel.setType("3");
            cataInfoHistoryTypeRel.setTypeId(one.getTypId());
            cataInfoHistoryTypeRelService.update(cataInfoHistoryTypeRel, new QueryWrapper<CataInfoHistoryTypeRel>().lambda().eq(CataInfoHistoryTypeRel::getInfoId, item.getInfoId()).eq(CataInfoHistoryTypeRel::getType, "3"));
        });

        List<AssetCateEx> assetCateExes = assetCateExService.list(new QueryWrapper<AssetCateEx>().lambda().eq(AssetCateEx::getType, "3"));
        AssetCateEx assetCateEx = new AssetCateEx();
        assetCateExes.forEach(item -> {
            DictAssetCate one = dictAssetCateService.getOne(new QueryWrapper<DictAssetCate>().lambda().eq(DictAssetCate::getParTypId, item.getTypeId()));
            assetCateEx.setInfoId(item.getInfoId());
            assetCateEx.setType("3");
            assetCateEx.setTypeId(one.getTypId());
            assetCateExService.update(assetCateEx, new QueryWrapper<AssetCateEx>().lambda().eq(AssetCateEx::getInfoId, item.getInfoId()).eq(AssetCateEx::getType, "3"));
        });
    }
}
