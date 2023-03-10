//package com.digitalchina.resourcecatalog.admin;
//
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.poi.excel.ExcelReader;
//import cn.hutool.poi.excel.ExcelUtil;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.digitalchina.resourcecatalog.admin.util.GB2Alpha;
//import com.digitalchina.resourcecatalog.core.util.BeanUtils;
//import com.digitalchina.resourcecatalog.db.domain.*;
//import com.digitalchina.resourcecatalog.db.rabbitmq.RabbitConfig;
//import com.digitalchina.resourcecatalog.db.service.*;
//import jodd.util.StringUtil;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.util.StringUtils;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.time.LocalDateTime;
//import java.util.*;
//
///**
// * @author: create by wangmh
// * @name: ImportTest.java
// * @description:
// * @date:2020/6/16
// **/
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
//public class ImportTest {
//    private static final String FILE_PATH = "C:\\Users\\Karma\\Desktop\\data.xlsx";
//    private static final String CREATEDBY = "wangmh";
//
//    @Autowired
//    ArchOrgService archOrgService;
//
//    @Autowired
//    DictAssetCateService dictAssetCateService;
//
//    @Autowired
//    CataInfoTempService cataInfoTempService;
//
//    @Autowired
//    SysDictService sysDictService;
//
//    @Autowired
//    CataInfoItemTempRelService cataInfoItemTempRelService;
//
//    @Autowired
//    CataInfoTempTypeRelService cataInfoTempTypeRelService;
//
//    @Autowired
//    AssetCateExService assetCateExService;
//
//    @Autowired
//    ArchBusiUviewStrExService archBusiUviewStrExService;
//
//    @Autowired
//    ArchBusiUviewExService archBusiUviewExService;
//
//    @Autowired
//    CataInfoHistoryService cataInfoHistoryService;
//
//    @Autowired
//    CataInfoItemHistoryRelService cataInfoItemHistoryRelService;
//
//    @Autowired
//    CataInfoHistoryTypeRelService cataInfoHistoryTypeRelService;
//
//    @Autowired
//    MqResourceService mqResourceService;
//
//    private List info = new ArrayList();
//
//    /***
//     * ???????????????
//     * @throws ParseException
//     */
//    @Test
//    public void totalImport() throws ParseException {
//        //??????????????????-???????????????
//        importSheel1();
//        //???????????????-???????????????
//        importSheel2();
//        //????????????????????????
//        importSheet0();
//        //?????????????????????????????????????????????????????????????????????????????????
//        cpoyTempToExAndHistory();
//        //??????????????????
//        info.forEach(item -> {
//            System.out.println(item.toString());
//        });
//    }
//
//    /**
//     * ????????????-???????????????
//     */
//    @Test
//    public void importSheel1() throws ParseException {
//        ExcelReader reader;
//        reader = ExcelUtil.getReader(FILE_PATH, 1);
//        List<List<Object>> result = reader.read(1);
//        int successCount = 0;
//        int errorCount = 0;
//        StringBuilder sb = new StringBuilder();
//        result.remove(0);
//        result.remove(0);
//        for (List<Object> item : result) {
//            String current = subCellStr(item.get(0));
//            try {
//                importCata(item);
//                successCount++;
//            } catch (Exception e) {
//                errorCount++;
//                sb.append("importSheel1---?????????" + current + "??????????????????????????????" + e.getMessage());
//                sb.append("\r\n");
//            }
//        }
//        System.out.println("importSheel1---???????????????" + successCount + "?????????????????????" + errorCount + "???");
//        System.out.println("importSheel1---???????????????" + sb.toString());
//        info.add("importSheel1---???????????????" + successCount + "?????????????????????" + errorCount + "???");
//        info.add("importSheel1---???????????????" + sb.toString());
//    }
//
//    /***
//     * ?????????-???????????????
//     */
//    @Test
//    public void importSheel2() throws ParseException {
//        ExcelReader reader;
//        reader = ExcelUtil.getReader(FILE_PATH, 2);
//        List<List<Object>> result = reader.read(1);
//        int successCount = 0;
//        int errorCount = 0;
//        StringBuilder sb = new StringBuilder();
//        result.remove(0);
//        result.remove(0);
//        int current = 1;
//        for (List<Object> item : result) {
//            try {
//                importCata(item);
//                successCount++;
//            } catch (Exception e) {
//                errorCount++;
//                sb.append("importSheel2---?????????" + current + "??????????????????????????????" + e.getMessage());
//                sb.append("\r\n");
//                current++;
//            }
//        }
//        System.out.println("importSheel2---???????????????" + successCount + "?????????????????????" + errorCount + "???");
//        System.out.println("importSheel2---???????????????" + sb.toString());
//        info.add("importSheel2---???????????????" + successCount + "?????????????????????" + errorCount + "???");
//        info.add("importSheel2---???????????????" + sb.toString());
//    }
//
//    /***
//     * ??????????????????????????????????????????????????????????????????
//     */
//    @Test
//    public void importSheet0() {
//        ExcelReader reader;
//        reader = ExcelUtil.getReader(FILE_PATH, 0);
//        List<List<Object>> result = reader.read(2);
//        int successCount = 0;
//        int errorCount = 0;
//        StringBuilder sb = new StringBuilder();
//        int current = 1;
//        for (List<Object> item : result) {
//            try {
//                importType(item);
//                successCount++;
//            } catch (Exception e) {
//                errorCount++;
//                sb.append("importSheet0---?????????" + current + "??????????????????????????????" + e.getMessage());
//                sb.append("\r\n");
//                current++;
//            }
//        }
//        System.out.println("importSheet0---???????????????" + successCount + "?????????????????????" + errorCount + "???");
//        System.out.println("importSheet0---???????????????" + sb.toString());
//        info.add("importSheet0---???????????????" + successCount + "?????????????????????" + errorCount + "???");
//        info.add("importSheet0---???????????????" + sb.toString());
//    }
//
//    private void importCata(List<Object> item) throws ParseException {
//        //?????????
//        String typeName1 = subCellStr(item.get(0));
//        String typeName2 = subCellStr(item.get(1));
//        String typeName3 = subCellStr(item.get(2));
//        String typeName4 = subCellStr(item.get(3));
//        String typeName5 = subCellStr(item.get(4));
//        //???
//        String typeName6 = subCellStr(item.get(5));
//        //???
//        String typeName7 = subCellStr(item.get(6));
//        //??????
//        String typeName8 = subCellStr(item.get(6));
//        //????????????
//        String uviewNm = subCellStr(item.get(8));
//        //??????????????????
//        String uviewNo = subCellStr(item.get(9));
//        //???????????????????????????
//        String provOrgName = subCellStr(item.get(11));
//        //??????????????????
//        String uviewDesc = subCellStr(item.get(12));
//        //????????????????????????
//        String mediaFmt = subCellStr(item.get(13));
//        //????????????????????????
//        String mediaFmtType = subCellStr(item.get(14));
//        //????????????
//        String updateCyc = subCellStr(item.get(15));
//        //??????????????????
//        String srcField = subCellStr(item.get(17));
//        //?????????????????? ??? ????????????
//        String engCd = (new GB2Alpha()).String2Alpha(srcField);
//        //?????????????????????
//        String srcDataTyp = subCellStr(item.get(18)).equals("????????????") ? "STRING" : "DATE";
//        //????????????
//        String dataLen = subCellStr(item.get(19));
//        //????????????
//        String shareLv = subCellStr(item.get(20)).equals("????????????") ? "02" : "01";
//        //????????????
//        String shareCondition = subCellStr(item.get(21));
//        //????????????????????????02-??????01-??????
//        String pubSts = subCellStr(item.get(22)).equals("???") ? "01" : "02";
//        //????????????
//        String pubCondition = StringUtil.isEmpty(subCellStr(item.get(23))) ? "???" : subCellStr(item.get(23));
//        //????????????
//        Date updateDt = getDate(subCellStr(item.get(24)));
//        //??????
//        String remark = subCellStr(item.get(25));
//
//        //TODO ??????????????????
//        ArchOrg archOrg = archOrgService.getOne(new QueryWrapper<ArchOrg>().lambda().eq(ArchOrg::getOrgNm, provOrgName));
//        if (archOrg == null) {
//            archOrg = new ArchOrg();
//            archOrg.setOrgNm(provOrgName);
//            Integer maxCd = archOrgService.getMaxOrgCdByPidIsNull();
//            if (maxCd == null) {
//                archOrg.setOrgCd("01");
//                archOrg.setDispalySn(1);
//            } else {
//                maxCd++;
//                archOrg.setOrgCd(maxCd.toString().length() % 2 == 0 ? maxCd.toString() : "0" + maxCd);
//                archOrg.setDispalySn(maxCd);
//            }
//            archOrg.setCrtDt(new Date());
//            archOrg.setCreator(CREATEDBY);
//            archOrgService.save(archOrg);
//            mqResourceService.sendMessage(RabbitConfig.ORG_UPDATE, archOrg);
//        }
//        //????????????id
//        Integer orgId = archOrg.getOrgId();
//        //????????????Cd
//        String orgCd = archOrg.getOrgCd();
//
//        //TODO ??????????????????-?????????
//        QueryWrapper<DictAssetCate> cateQueryWrapper = new QueryWrapper<>();
//        cateQueryWrapper.lambda().eq(DictAssetCate::getParTypId, 4);
//        cateQueryWrapper.lambda().eq(DictAssetCate::getTypNm, typeName6);
//        cateQueryWrapper.lambda().eq(DictAssetCate::getStatus, "1");
//        cateQueryWrapper.lambda().eq(DictAssetCate::getTypType, "???");
//        DictAssetCate dictAssetCate1 = dictAssetCateService.getOne(cateQueryWrapper);
//        if (dictAssetCate1 == null) {
//            dictAssetCate1 = new DictAssetCate();
//            dictAssetCate1.setTypNm(typeName6);
//            DictAssetCate serviceOne = dictAssetCateService.getOne(new QueryWrapper<DictAssetCate>().lambda().eq(DictAssetCate::getParTypId, 4).orderByDesc(DictAssetCate::getCrtDt));
//            if (serviceOne == null) {
//                dictAssetCate1.setTypCd("01");
//                dictAssetCate1.setDisplaySn(1);
//                dictAssetCate1.setFullTypCd("301");
//            } else {
//                Integer cd = Integer.parseInt(serviceOne.getTypCd()) + 1;
//                String newCd = cd.toString().length() % 2 == 0 ? cd.toString() : "0" + cd;
//                dictAssetCate1.setTypCd(newCd);
//                dictAssetCate1.setDisplaySn(cd);
//                dictAssetCate1.setFullTypCd("3" + newCd);
//            }
//            dictAssetCate1.setStatus("1");
//            dictAssetCate1.setTypType("???");
//            dictAssetCate1.setCreator(CREATEDBY);
//            dictAssetCate1.setCrtDt(LocalDateTime.now());
//            dictAssetCate1.setParTypId(4);
//            dictAssetCateService.save(dictAssetCate1);
//        }
//        //???id???Cd
//        Integer typId = dictAssetCate1.getTypId();
//        String typCd = dictAssetCate1.getTypCd();
//
//        //TODO ------???????????????
//        QueryWrapper<DictAssetCate> cateQueryWrapper1 = new QueryWrapper<>();
//        cateQueryWrapper1.lambda().eq(DictAssetCate::getParTypId, typId);
//        cateQueryWrapper1.lambda().eq(DictAssetCate::getTypNm, typeName7);
//        cateQueryWrapper1.lambda().eq(DictAssetCate::getStatus, "1");
//        cateQueryWrapper1.lambda().eq(DictAssetCate::getTypType, "???");
//        DictAssetCate dictAssetCate2 = dictAssetCateService.getOne(cateQueryWrapper1);
//        if (dictAssetCate2 == null) {
//            dictAssetCate2 = new DictAssetCate();
//            dictAssetCate2.setTypNm(typeName7);
//            DictAssetCate serviceOne = dictAssetCateService.getOne(new QueryWrapper<DictAssetCate>().lambda().eq(DictAssetCate::getParTypId, typId).orderByDesc(DictAssetCate::getCrtDt));
//            if (serviceOne == null) {
//                dictAssetCate2.setTypCd("001");
//                dictAssetCate2.setDisplaySn(1);
//                dictAssetCate2.setFullTypCd("3" + typCd + "001");
//            } else {
//                Integer cd = Integer.parseInt(serviceOne.getTypCd()) + 1;
//                int i = cd.toString().length() % 3;
//                String newCd = "";
//                if (i == 0) {
//                    newCd = cd.toString();
//                }
//                if (i == 1) {
//                    newCd = "00" + cd.toString();
//                }
//                if (i == 2) {
//                    newCd = "0" + cd.toString();
//                }
//                dictAssetCate2.setTypCd(newCd);
//                dictAssetCate2.setDisplaySn(cd);
//                dictAssetCate2.setFullTypCd("3" + typCd + newCd);
//            }
//            dictAssetCate2.setStatus("1");
//            dictAssetCate2.setTypType("???");
//            dictAssetCate2.setOrgId(orgId);
//            dictAssetCate2.setCreator(CREATEDBY);
//            dictAssetCate2.setCrtDt(LocalDateTime.now());
//            dictAssetCate2.setParTypId(typId);
//            dictAssetCateService.save(dictAssetCate2);
//        }
//        //???id???Cd
//        Integer typId1 = dictAssetCate2.getTypId();
//        String typCd1 = dictAssetCate2.getTypCd();
//
//        //TODO ------????????????
//        QueryWrapper<DictAssetCate> cateQueryWrapper2 = new QueryWrapper<>();
//        cateQueryWrapper2.lambda().eq(DictAssetCate::getParTypId, typId1);
//        cateQueryWrapper2.lambda().eq(DictAssetCate::getTypNm, typeName8);
//        cateQueryWrapper2.lambda().eq(DictAssetCate::getStatus, "1");
//        cateQueryWrapper2.lambda().eq(DictAssetCate::getTypType, "??????");
//        DictAssetCate dictAssetCate3 = dictAssetCateService.getOne(cateQueryWrapper2);
//        if (dictAssetCate3 == null) {
//            dictAssetCate3 = new DictAssetCate();
//            dictAssetCate3.setTypNm(typeName8);
//            DictAssetCate serviceOne = dictAssetCateService.getOne(new QueryWrapper<DictAssetCate>().lambda().eq(DictAssetCate::getParTypId, typId1).orderByDesc(DictAssetCate::getCrtDt));
//            if (serviceOne == null) {
//                dictAssetCate3.setTypCd("01");
//                dictAssetCate3.setDisplaySn(1);
//                dictAssetCate3.setFullTypCd("3" + typCd + typCd1 + "01");
//            } else {
//                Integer cd = Integer.parseInt(serviceOne.getTypCd()) + 1;
//                String newCd = cd.toString().length() % 2 == 0 ? cd.toString() : "0" + cd;
//                dictAssetCate3.setTypCd(newCd);
//                dictAssetCate3.setDisplaySn(cd);
//                dictAssetCate3.setFullTypCd("3" + typCd + typCd1 + newCd);
//            }
//            dictAssetCate3.setStatus("1");
//            dictAssetCate3.setTypType("??????");
//            dictAssetCate3.setOrgId(orgId);
//            dictAssetCate3.setCreator(CREATEDBY);
//            dictAssetCate3.setCrtDt(LocalDateTime.now());
//            dictAssetCate3.setParTypId(typId1);
//            dictAssetCateService.save(dictAssetCate3);
//        }
//
//        //TODO ???????????????????????????????????????
//        CataInfoTemp cataInfoTemp = cataInfoTempService.getOne(new QueryWrapper<CataInfoTemp>().lambda().eq(CataInfoTemp::getUviewNo, uviewNo));
//        if (cataInfoTemp == null) {
//            cataInfoTemp = new CataInfoTemp();
//            cataInfoTemp.setUviewNm(uviewNm);
//            cataInfoTemp.setUviewNo(uviewNo);
//            cataInfoTemp.setProvOrgId(orgId);
//            cataInfoTemp.setProvOrgCode(orgCd);
//            //??????????????????????????????
//            if (mediaFmt.equals("????????????")) {
//                mediaFmt = "????????????";
//            }
//            SysDict sysDict = sysDictService.getOne(new QueryWrapper<SysDict>().lambda().eq(SysDict::getName, mediaFmt).eq(SysDict::getType, "format_type").eq(SysDict::getPid, 7));
//            if (sysDict == null) {
//                sysDict = new SysDict();
//                sysDict.setName(mediaFmt);
//                SysDict one = sysDictService.getOne(new QueryWrapper<SysDict>().lambda().eq(SysDict::getPid, 7).orderByDesc(SysDict::getCreateDate));
//                if (one == null) {
//                    sysDict.setValue("01");
//                    sysDict.setSort(1);
//                } else {
//                    Integer i = Integer.parseInt(one.getValue()) + 1;
//                    sysDict.setValue(i.toString().length() % 2 == 0 ? i.toString() : "0" + i);
//                    sysDict.setSort(i);
//                }
//                sysDict.setType("format_type");
//                sysDict.setCreateBy(520);
//                sysDict.setCreateDate(LocalDateTime.now());
//                sysDict.setPid(7);
//                sysDict.setIsInter(0);
//                sysDictService.save(sysDict);
//            }
//            cataInfoTemp.setMediaFmt(sysDict.getValue());
//            SysDict sysDict1 = sysDictService.getOne(new QueryWrapper<SysDict>().lambda().eq(SysDict::getName, mediaFmtType).eq(SysDict::getType, "format_type_" + sysDict.getValue()).eq(SysDict::getPid, sysDict.getId()));
//            if (sysDict1 == null) {
//                sysDict1 = new SysDict();
//                sysDict1.setName(mediaFmtType);
//                SysDict one = sysDictService.getOne(new QueryWrapper<SysDict>().lambda().eq(SysDict::getPid, sysDict.getId()).orderByDesc(SysDict::getCreateDate));
//                if (one == null) {
//                    sysDict1.setValue(sysDict.getValue() + "01");
//                    sysDict1.setSort(1);
//                } else {
//                    Integer i = Integer.parseInt(one.getValue()) + 1;
//                    sysDict1.setValue(i.toString().length() % 2 == 0 ? i.toString() : "0" + i);
//                    sysDict1.setSort(one.getSort() + 1);
//                }
//                sysDict1.setType("format_type_" + sysDict.getValue());
//                sysDict1.setCreateBy(520);
//                sysDict1.setCreateDate(LocalDateTime.now());
//                sysDict1.setPid(sysDict.getId());
//                sysDict1.setIsInter(0);
//                sysDictService.save(sysDict1);
//            }
//            cataInfoTemp.setMediaFmtType(sysDict1.getValue());
//
//            QueryWrapper<SysDict> dictQueryWrapper = new QueryWrapper<>();
//            if (updateCyc.equals("???")) {
//                dictQueryWrapper.lambda().eq(SysDict::getName, "???" + updateCyc);
//            } else {
//                dictQueryWrapper.lambda().like(SysDict::getName, updateCyc);
//            }
//            dictQueryWrapper.lambda().eq(SysDict::getType, "update_cycle");
//            dictQueryWrapper.lambda().eq(SysDict::getPid, 8);
//            SysDict sysDict2 = sysDictService.getOne(dictQueryWrapper);
//            if (sysDict2 == null) {
//                sysDict2 = new SysDict();
//                sysDict2.setName(updateCyc);
//                SysDict one = sysDictService.getOne(new QueryWrapper<SysDict>().lambda().eq(SysDict::getPid, 8).orderByDesc(SysDict::getCreateDate));
//                if (one == null) {
//                    sysDict2.setValue("01");
//                    sysDict2.setSort(1);
//                } else {
//                    Integer i = Integer.parseInt(one.getValue()) + 1;
//                    sysDict2.setValue(i.toString().length() % 2 == 0 ? i.toString() : "0" + i);
//                    sysDict2.setSort(one.getSort() + 1);
//                }
//                sysDict2.setType("update_cycle");
//                sysDict2.setCreateBy(520);
//                sysDict2.setCreateDate(LocalDateTime.now());
//                sysDict2.setPid(8);
//                sysDict2.setIsInter(0);
//                sysDictService.save(sysDict2);
//            }
//            cataInfoTemp.setUpdateCyc(sysDict2.getValue());
//            cataInfoTemp.setPubDt(updateDt);
//            cataInfoTemp.setUviewDesc(uviewDesc);
//            cataInfoTemp.setShareLv(shareLv);
//            cataInfoTemp.setShareCondition(shareCondition);
//            cataInfoTemp.setPubSts(pubSts);
//            cataInfoTemp.setVersion("1.0");
//            cataInfoTemp.setPubCondition(pubCondition);
//            cataInfoTemp.setAuditStatus("3");
//            cataInfoTemp.setCreator(CREATEDBY);
//            cataInfoTemp.setCrtDt(new Date());
//            cataInfoTemp.setDeleted(0);
//            cataInfoTempService.save(cataInfoTemp);
//        }
//        //??????????????????Id
//        Integer uviewId = cataInfoTemp.getUviewId();
//
//        //TODO ??????????????????????????????
//        QueryWrapper<CataInfoItemTempRel> tempRelQueryWrapper = new QueryWrapper<>();
//        tempRelQueryWrapper.lambda().eq(CataInfoItemTempRel::getUviewId, uviewId);
//        tempRelQueryWrapper.lambda().eq(CataInfoItemTempRel::getSrcField, srcField);
//        tempRelQueryWrapper.lambda().eq(CataInfoItemTempRel::getEngCd, engCd);
//        tempRelQueryWrapper.lambda().eq(CataInfoItemTempRel::getSrcDataTyp, srcDataTyp);
//        tempRelQueryWrapper.lambda().eq(CataInfoItemTempRel::getDataLen, dataLen);
//        CataInfoItemTempRel cataInfoItemTempRel = cataInfoItemTempRelService.getOne(tempRelQueryWrapper);
//        if (cataInfoItemTempRel == null) {
//            cataInfoItemTempRel = new CataInfoItemTempRel();
//            cataInfoItemTempRel.setUviewId(uviewId);
//            cataInfoItemTempRel.setSrcField(srcField);
//            if (cataInfoItemTempRelService.getOne(new QueryWrapper<CataInfoItemTempRel>().lambda().eq(CataInfoItemTempRel::getEngCd, engCd)) != null) {
//                cataInfoItemTempRel.setEngCd(engCd + "01");
//            } else {
//                cataInfoItemTempRel.setEngCd(engCd);
//            }
//            cataInfoItemTempRel.setSrcDataTyp(srcDataTyp);
//            cataInfoItemTempRel.setDataLen(dataLen);
//            cataInfoItemTempRel.setSno(1);
//            cataInfoItemTempRel.setCreateTime(new Date());
//            cataInfoItemTempRelService.save(cataInfoItemTempRel);
//        }
//
//        //TODO ?????????????????????????????????????????????????????????
//        saveCataInfoTempTypeRel(uviewId, "3", dictAssetCate3.getTypId());
//    }
//
//    /***
//     * ?????????????????????????????????????????????????????????????????????????????????
//     */
//    @Test
//    public void cpoyTempToExAndHistory() {
//        //TODO ???????????????????????????????????????????????????
//        List<CataInfoTemp> cataInfoTempList = cataInfoTempService.list(new QueryWrapper<CataInfoTemp>());
//        List<ArchBusiUviewEx> archBusiUviewExes = new ArrayList<>();
//        List<CataInfoHistory> cataInfoHistories = new ArrayList<>();
//        ArchBusiUviewEx archBusiUviewEx = null;
//        CataInfoHistory cataInfoHistory = null;
//        for (CataInfoTemp cataInfoTemp : cataInfoTempList) {
//            archBusiUviewEx = BeanUtils.doToDto(cataInfoTemp, ArchBusiUviewEx.class);
//            archBusiUviewExes.add(archBusiUviewEx);
//            cataInfoHistory = BeanUtils.dtoToDo(cataInfoTemp, CataInfoHistory.class);
//            cataInfoHistory.setIsCurrent("1");
//            cataInfoHistories.add(cataInfoHistory);
//        }
//        archBusiUviewExService.saveBatch(archBusiUviewExes);
//        cataInfoHistoryService.saveBatch(cataInfoHistories);
//
//        //TODO ????????????????????????????????????????????????
//        List<CataInfoItemTempRel> cataInfoItemTempRels = cataInfoItemTempRelService.list(new QueryWrapper<CataInfoItemTempRel>());
//        List<ArchBusiUviewStrEx> archBusiUviewStrExes = new ArrayList<>();
//        List<CataInfoItemHistoryRel> cataInfoItemHistoryRels = new ArrayList<>();
//        ArchBusiUviewStrEx archBusiUviewStrEx = null;
//        CataInfoItemHistoryRel cataInfoItemHistoryRel = null;
//        for (CataInfoItemTempRel cataInfoItemTempRel : cataInfoItemTempRels) {
//            archBusiUviewStrEx = BeanUtils.doToDto(cataInfoItemTempRel, ArchBusiUviewStrEx.class);
//            archBusiUviewStrExes.add(archBusiUviewStrEx);
//            cataInfoItemHistoryRel = BeanUtils.dtoToDo(cataInfoItemTempRel, CataInfoItemHistoryRel.class);
//
//            cataInfoItemHistoryRels.add(cataInfoItemHistoryRel);
//        }
//        archBusiUviewStrExService.saveBatch(archBusiUviewStrExes);
//        cataInfoItemHistoryRelService.saveBatch(cataInfoItemHistoryRels);
//
//        //TODO ????????????????????????????????????????????????
//        List<CataInfoTempTypeRel> cataInfoTempTypeRels = cataInfoTempTypeRelService.list(new QueryWrapper<CataInfoTempTypeRel>());
//        List<AssetCateEx> assetCateExes = new ArrayList<>();
//        List<CataInfoHistoryTypeRel> cataInfoHistoryTypeRels = new ArrayList<>();
//        AssetCateEx assetCateEx = null;
//        CataInfoHistoryTypeRel cataInfoHistoryTypeRel = null;
//        for (CataInfoTempTypeRel cataInfoTempTypeRel : cataInfoTempTypeRels) {
//            assetCateEx = BeanUtils.doToDto(cataInfoTempTypeRel, AssetCateEx.class);
//            assetCateExes.add(assetCateEx);
//            cataInfoHistoryTypeRel = BeanUtils.dtoToDo(cataInfoTempTypeRel, CataInfoHistoryTypeRel.class);
//            cataInfoHistoryTypeRels.add(cataInfoHistoryTypeRel);
//        }
//        assetCateExService.saveBatch(assetCateExes);
//        cataInfoHistoryTypeRelService.saveBatch(cataInfoHistoryTypeRels);
//        //??????mq?????????????????????
//        for (ArchBusiUviewEx info : archBusiUviewExes) {
//            Map<String, Object> message = new HashMap<String, Object>();
//            message.put("archBusiUviewEx", info);
//            List<ArchBusiUviewStrEx> archBusiUviewStrExList = archBusiUviewStrExService.list(new QueryWrapper<ArchBusiUviewStrEx>().eq(ArchBusiUviewStrEx.UVIEW_ID, info.getUviewId()));
//            message.put("archBusiUviewStrExList", archBusiUviewStrExList);
//            List<AssetCateEx> assetCateExList = assetCateExService.list(new QueryWrapper<AssetCateEx>().eq(AssetCateEx.INFO_ID, info.getUviewId()));
//            message.put("assetCateExList", assetCateExList);
//            if ("02".equals(info.getPubSts())) {
//                mqResourceService.sendMessage(RabbitConfig.CATA_UPDATE_SHARE, message);
//            } else {
//                mqResourceService.sendMessage(RabbitConfig.CATA_UPDATE, message);
//            }
//        }
//    }
//
//    /***
//     * ??????????????????????????????????????????????????????????????????
//     * @param item
//     */
//    private void importType(List<Object> item) {
//        //????????????
//        String uviewNm = subCellStr(item.get(2));
//        //????????????
//        String typName1 = subCellStr(item.get(3));
//        //????????????
//        String typName2 = subCellStr(item.get(5));
//        DictAssetCate dictAssetCate = null;
//        //??????id
//        Integer typeId1 = null;
//        //??????id
//        Integer typeId2 = null;
//        //????????????id
//        if (!StringUtils.isEmpty(typName1)) {
//            dictAssetCate = dictAssetCateService.getOne(new QueryWrapper<DictAssetCate>().lambda().like(DictAssetCate::getTypNm, typName1).eq(DictAssetCate::getParTypId, 3).eq(DictAssetCate::getStatus, "1"));
//            if (dictAssetCate == null) {
//                dictAssetCate = new DictAssetCate();
//                dictAssetCate.setTypNm(typName1);
//                DictAssetCate one = dictAssetCateService.getOne(new QueryWrapper<DictAssetCate>().lambda().eq(DictAssetCate::getParTypId, 3).eq(DictAssetCate::getStatus, "1").orderByDesc(DictAssetCate::getCrtDt));
//                if (one == null) {
//                    dictAssetCate.setTypCd("01");
//                    dictAssetCate.setDisplaySn(1);
//                    dictAssetCate.setFullTypCd("201");
//                } else {
//                    Integer newCount = (Integer.parseInt(one.getTypCd()) + 1);
//                    String newStr = newCount.toString().length() % 2 == 0 ? newCount.toString() : "0" + newCount;
//                    dictAssetCate.setTypCd(newStr);
//                    dictAssetCate.setDisplaySn(newCount);
//                    dictAssetCate.setFullTypCd("2" + newStr);
//                }
//                dictAssetCate.setStatus("1");
//                dictAssetCate.setTypType("???");
//                dictAssetCate.setCrtDt(LocalDateTime.now());
//                dictAssetCate.setCreator(CREATEDBY);
//                dictAssetCate.setParTypId(3);
//                dictAssetCateService.save(dictAssetCate);
//            }
//            typeId1 = dictAssetCate.getTypId();
//        }
//
//        if (!StringUtils.isEmpty(typName2)) {
//            if (typName2.equals("????????????")) {
//                typName2 = "????????????";
//            }
//            dictAssetCate = dictAssetCateService.getOne(new QueryWrapper<DictAssetCate>().lambda().like(DictAssetCate::getTypNm, typName2).eq(DictAssetCate::getParTypId, 2).eq(DictAssetCate::getStatus, "1"));
//            if (dictAssetCate == null) {
//                dictAssetCate = new DictAssetCate();
//                dictAssetCate.setTypNm(typName2);
//                DictAssetCate one = dictAssetCateService.getOne(new QueryWrapper<DictAssetCate>().lambda().eq(DictAssetCate::getParTypId, 3).eq(DictAssetCate::getStatus, "1").orderByDesc(DictAssetCate::getCrtDt));
//                if (one == null) {
//                    dictAssetCate.setTypCd("01");
//                    dictAssetCate.setDisplaySn(1);
//                    dictAssetCate.setFullTypCd("101");
//                } else {
//                    Integer newCount = (Integer.parseInt(one.getTypCd()) + 1);
//                    String newStr = newCount.toString().length() % 2 == 0 ? newCount.toString() : "0" + newCount;
//                    dictAssetCate.setTypCd(newStr);
//                    dictAssetCate.setDisplaySn(newCount);
//                    dictAssetCate.setFullTypCd("1" + newStr);
//                }
//                dictAssetCate.setStatus("1");
//                dictAssetCate.setTypType("???");
//                dictAssetCate.setCrtDt(LocalDateTime.now());
//                dictAssetCate.setCreator(CREATEDBY);
//                dictAssetCate.setParTypId(2);
//                dictAssetCateService.save(dictAssetCate);
//            }
//            typeId2 = dictAssetCate.getTypId();
//        }
//
//        //?????????????????????????????????id
//        CataInfoTemp cataInfoTemp = cataInfoTempService.getOne(new QueryWrapper<CataInfoTemp>().lambda().eq(CataInfoTemp::getUviewNm, uviewNm));
//        if (cataInfoTemp != null) {
//            //??????????????????Id
//            if (typeId1 != null) {
//                saveCataInfoTempTypeRel(cataInfoTemp.getUviewId(), "2", typeId1);
//            }
//            //??????????????????Id
//            if (typeId2 != null) {
//                saveCataInfoTempTypeRel(cataInfoTemp.getUviewId(), "1", typeId2);
//            }
//        }
//
//    }
//
//    //????????????????????????????????????????????????????????? type 1-???????????? 2-???????????? 3-????????????
//    private void saveCataInfoTempTypeRel(Integer infoId, String type, Integer typeId) {
//        QueryWrapper<CataInfoTempTypeRel> queryWrapper = new QueryWrapper<>();
//        queryWrapper.lambda().eq(CataInfoTempTypeRel::getInfoId, infoId);
//        queryWrapper.lambda().eq(CataInfoTempTypeRel::getType, type);
//        queryWrapper.lambda().eq(CataInfoTempTypeRel::getTypeId, typeId);
//        if (cataInfoTempTypeRelService.count(queryWrapper) == 0) {
//            CataInfoTempTypeRel cataInfoTempTypeRel = new CataInfoTempTypeRel();
//            cataInfoTempTypeRel.setInfoId(infoId);
//            cataInfoTempTypeRel.setType(type);
//            cataInfoTempTypeRel.setTypeId(typeId);
//            cataInfoTempTypeRelService.save(cataInfoTempTypeRel);
//        }
//    }
//
//    private String subCellStr(Object object) {
//        return subCellStr(object, 100);
//    }
//
//    private static String subCellStr(Object obj, Integer length) {
//        if (obj == null) {
//            return "";
//        } else {
//            return StrUtil.subSufByLength(StrUtil.trim(obj.toString()), length);
//        }
//    }
//
//    private Date getDate(String dateStr) throws ParseException {
//        if (StringUtil.isEmpty(dateStr)) {
//            return null;
//        }
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//???????????????MM
//        return simpleDateFormat.parse(dateStr);
//    }
//
//    @Test
//    public void test() {
//        List<ArchBusiUviewEx> archBusiUviewExes = archBusiUviewExService.list(new QueryWrapper<ArchBusiUviewEx>().eq(ArchBusiUviewEx.DELETED, 0));
//        for (ArchBusiUviewEx info : archBusiUviewExes) {
//            Map<String, Object> message = new HashMap<String, Object>();
//            message.put("archBusiUviewEx", info);
//            List<ArchBusiUviewStrEx> archBusiUviewStrExList = archBusiUviewStrExService.list(new QueryWrapper<ArchBusiUviewStrEx>().eq(ArchBusiUviewStrEx.UVIEW_ID, info.getUviewId()));
//            message.put("archBusiUviewStrExList", archBusiUviewStrExList);
//            List<AssetCateEx> assetCateExList = assetCateExService.list(new QueryWrapper<AssetCateEx>().eq(AssetCateEx.INFO_ID, info.getUviewId()));
//            message.put("assetCateExList", assetCateExList);
//            if ("02".equals(info.getPubSts())) {
//                mqResourceService.sendMessage(RabbitConfig.CATA_UPDATE_SHARE, message);
//            } else {
//                mqResourceService.sendMessage(RabbitConfig.CATA_UPDATE, message);
//            }
//        }
//    }
//
//    @Test
//    public void update() {
//        List<CataInfoTempTypeRel> cataInfoTempTypeRels = cataInfoTempTypeRelService.list(new QueryWrapper<CataInfoTempTypeRel>().lambda().eq(CataInfoTempTypeRel::getType, "3"));
//        CataInfoTempTypeRel cataInfoTempTypeRel = new CataInfoTempTypeRel();
//        cataInfoTempTypeRels.forEach(item -> {
//            DictAssetCate one = dictAssetCateService.getOne(new QueryWrapper<DictAssetCate>().lambda().eq(DictAssetCate::getParTypId, item.getTypeId()));
//            cataInfoTempTypeRel.setInfoId(item.getInfoId());
//            cataInfoTempTypeRel.setType("3");
//            cataInfoTempTypeRel.setTypeId(one.getTypId());
//            cataInfoTempTypeRelService.update(cataInfoTempTypeRel, new QueryWrapper<CataInfoTempTypeRel>().lambda().eq(CataInfoTempTypeRel::getInfoId, item.getInfoId()).eq(CataInfoTempTypeRel::getType, "3"));
//        });
//
//        List<CataInfoHistoryTypeRel> cataInfoHistoryTypeRels = cataInfoHistoryTypeRelService.list(new QueryWrapper<CataInfoHistoryTypeRel>().lambda().eq(CataInfoHistoryTypeRel::getType, "3"));
//        CataInfoHistoryTypeRel cataInfoHistoryTypeRel = new CataInfoHistoryTypeRel();
//        cataInfoHistoryTypeRels.forEach(item -> {
//            DictAssetCate one = dictAssetCateService.getOne(new QueryWrapper<DictAssetCate>().lambda().eq(DictAssetCate::getParTypId, item.getTypeId()));
//            cataInfoHistoryTypeRel.setInfoId(item.getInfoId());
//            cataInfoHistoryTypeRel.setType("3");
//            cataInfoHistoryTypeRel.setTypeId(one.getTypId());
//            cataInfoHistoryTypeRelService.update(cataInfoHistoryTypeRel, new QueryWrapper<CataInfoHistoryTypeRel>().lambda().eq(CataInfoHistoryTypeRel::getInfoId, item.getInfoId()).eq(CataInfoHistoryTypeRel::getType, "3"));
//        });
//
//        List<AssetCateEx> assetCateExes = assetCateExService.list(new QueryWrapper<AssetCateEx>().lambda().eq(AssetCateEx::getType, "3"));
//        AssetCateEx assetCateEx = new AssetCateEx();
//        assetCateExes.forEach(item -> {
//            DictAssetCate one = dictAssetCateService.getOne(new QueryWrapper<DictAssetCate>().lambda().eq(DictAssetCate::getParTypId, item.getTypeId()));
//            assetCateEx.setInfoId(item.getInfoId());
//            assetCateEx.setType("3");
//            assetCateEx.setTypeId(one.getTypId());
//            assetCateExService.update(assetCateEx, new QueryWrapper<AssetCateEx>().lambda().eq(AssetCateEx::getInfoId, item.getInfoId()).eq(AssetCateEx::getType, "3"));
//        });
//    }
//}
