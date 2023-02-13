package com.digitalchina.resourcecatalog.db.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.cell.CellUtil;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalchina.resourcecatalog.db.dao.CataExportMapper;
import com.digitalchina.resourcecatalog.db.domain.*;
import com.digitalchina.resourcecatalog.db.service.*;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.net.URLEncoder;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangys
 * @since 2020-05-19
 */
@Service
public class CataExportServiceImpl extends ServiceImpl<CataExportMapper, CataExport> implements CataExportService {
    @Autowired
    CataExportService cataExportService;
    @Autowired
    SysStorageService sysStorageService;
    @Autowired
    private ArchOrgService archOrgService;
    @Autowired
    private ArchBusiService archBusiService;
    @Autowired
    private DictAssetCateService dictAssetCateService;
    @Autowired
    ArchBusiUviewExService archBusiUviewExService;
    @Autowired
    ArchBusiUviewStrExService archBusiUviewStrExService;
    @Autowired
    ArchBusiUviewStrConfigService archBusiUviewStrConfigService;
    @Autowired
    SysDictService sysDictService;
    @Override
    public IPage<List<CataExport>> selectPages(Page page, Integer depId, String startDate, String endDate){
        return this.baseMapper.selectPages(page, depId, startDate, endDate);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        CataExport bean = this.getById(id);
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
    public Workbook export(Integer depId) {
        // 创建工作薄
        XSSFWorkbook wb = new XSSFWorkbook();
        getSheet1(wb,depId);
//        getSheet2(wb,depId);
        getSheet3(wb,depId);
//        getSheet4(wb,depId);
        getSheet5(wb,depId);
        return wb;
    }

    @Override
    public Workbook exportCity(Integer depId) {
        // 创建工作薄
        XSSFWorkbook wb = new XSSFWorkbook();
        getSheetCity(wb,depId);
        return wb;
    }
    private void getSheetCity(XSSFWorkbook wb,Integer depId) {
        StringBuffer stringBuffer = new StringBuffer();
        XSSFSheet sheet3 = wb.createSheet("信息资源");
        sheet3.setDefaultColumnWidth((short) 25);// 默认列宽
        XSSFRow row1 = sheet3.createRow((short) 0);
        cteateCell(wb, row1, (short) 0, "信息类信息", true);
        CellRangeAddress cra1 = new CellRangeAddress(0, 0, 0, 19);
        sheet3.addMergedRegion(cra1);
        setBorder(1, cra1, sheet3);

        XSSFRow row2 = sheet3.createRow((short) 1);
        cteateCell(wb, row2, (short) 0, "信息类名称*", true);
        cteateCell(wb, row2, (short) 1, "信息资源格式*", true);
        cteateCell(wb, row2, (short) 2, "信息资源格式分类*", true);
        cteateCell(wb, row2, (short) 3, "信息类摘要*", true);
        cteateCell(wb, row2, (short) 4, "共享类型*", true);
        cteateCell(wb, row2, (short) 5, "开放类型*", true);
        cteateCell(wb, row2, (short) 6, "共享条件*", true);
        cteateCell(wb, row2, (short) 7, "开放条件*", true);
        cteateCell(wb, row2, (short) 8, "更新频率*", true);
        cteateCell(wb, row2, (short) 9, "数据范围*", true);
        cteateCell(wb, row2, (short) 10, "共享方式*", true);
        cteateCell(wb, row2, (short) 11, "共享方式类型*", true);
        cteateCell(wb, row2, (short) 12, "是否系统支撑*", true);
        cteateCell(wb, row2, (short) 13, "信息项信息", true);
        XSSFRow row3 = sheet3.createRow((short) 2);
        cteateCell(wb, row3, (short) 13, "信息项名称*", true);
        cteateCell(wb, row3, (short) 14, "是否主键*", true);
        cteateCell(wb, row3, (short) 15, "数据元编码", true);
        cteateCell(wb, row3, (short) 16, "数据元名称", true);
        cteateCell(wb, row3, (short) 17, "数据元类别*", true);
        cteateCell(wb, row3, (short) 18, "数据元类型*", true);
        cteateCell(wb, row3, (short) 19, "信息项数据长度*", true);
        CellRangeAddress cra2 = new CellRangeAddress(1, 1, 13, 19);
        sheet3.addMergedRegion(cra2);
        setBorder(1, cra2, sheet3);
        for (int i = 0; i < 13; i++) {
            CellRangeAddress cra = new CellRangeAddress(1, 2, i, i);
            sheet3.addMergedRegion(cra);
            setBorder(1, cra, sheet3);
        }
        //数据准备
        //信息资源格式
        Map<String, String> sysDictParentMap = new HashMap<>(), sysDictChildMap = new HashMap<>(), sysDictUpdateMap = new HashMap<>();
        QueryWrapper<SysDict> qwSysDict = new QueryWrapper<SysDict>();
        qwSysDict.eq("type", "format_type");
        List<SysDict> sysDictList = sysDictService.list(qwSysDict);
        for (int i = 0; i < sysDictList.size(); i++) {
            SysDict sysDict = sysDictList.get(i);
            sysDictParentMap.put(sysDict.getValue(), sysDict.getName());
            //信息资源格式
            QueryWrapper<SysDict> qwSysDictChild = new QueryWrapper<SysDict>();
            qwSysDictChild.eq("type", "format_type_" + sysDict.getValue());
            List<SysDict> sysDictListChild = sysDictService.list(qwSysDictChild);
            for (int j = 0; j < sysDictListChild.size(); j++) {
                SysDict sysDictChild = sysDictListChild.get(j);
                sysDictChildMap.put(sysDict.getValue() + "-" + sysDictChild.getValue(), sysDictChild.getName());
            }
        }
        //更新周期
        QueryWrapper<SysDict> qwUpdate = new QueryWrapper<SysDict>();
        qwUpdate.eq("type", "update_cycle");
        List<SysDict> sysDictUpdateList = sysDictService.list(qwUpdate);
        sysDictUpdateList.forEach(item -> sysDictUpdateMap.put(item.getValue(), item.getName()));

        List<ArchBusiUviewEx> cataInfoList = archBusiUviewExService.list(new QueryWrapper<ArchBusiUviewEx>().lambda()
                .eq(ArchBusiUviewEx::getProvOrgId, depId)
                .eq(ArchBusiUviewEx::getDeleted,0)
                .eq(ArchBusiUviewEx::getAuditStatus,"5")
                .and(wrapper -> wrapper.isNull(ArchBusiUviewEx::getCityCataCode).or().eq(ArchBusiUviewEx::getCityCataCode, ""))
        );
        List<ArchBusiUviewStrEx> archBusiUviewStrExList = archBusiUviewStrExService.getListByDeptId(depId);
//        List<ArchBusiUviewStrEx> archBusiUviewStrExList = archBusiUviewStrExService.list(null);
        Map<Integer, ArrayList<ArchBusiUviewStrEx>> uviewIdMap = new HashMap<>();
        for (int i = 0; i < archBusiUviewStrExList.size(); i++) {
            ArchBusiUviewStrEx archBusiUviewStrEx = archBusiUviewStrExList.get(i);
            if (!uviewIdMap.containsKey(archBusiUviewStrEx.getUviewId())) {
                uviewIdMap.put(archBusiUviewStrEx.getUviewId(), new ArrayList<ArchBusiUviewStrEx>());
            }
            uviewIdMap.get(archBusiUviewStrEx.getUviewId()).add(archBusiUviewStrEx);
        }
        int rowIndex = 3;
        for (int i = 0; i < cataInfoList.size(); i++) {
            ArchBusiUviewEx archBusiUviewEx = cataInfoList.get(i);
            ArrayList<ArchBusiUviewStrEx> arrayList = uviewIdMap.get(archBusiUviewEx.getUviewId());
            if (arrayList == null || arrayList.size() == 0) {
                XSSFRow row = sheet3.createRow((short) rowIndex);
                cteateCell(wb, row, (short) 0, archBusiUviewEx.getUviewNm(), false);
                cteateCell(wb, row, (short) 1, sysDictParentMap.get(archBusiUviewEx.getMediaFmt()), false);
                cteateCell(wb, row, (short) 2, sysDictChildMap.get(archBusiUviewEx.getMediaFmt() + "-" + archBusiUviewEx.getMediaFmtType()), false);
                cteateCell(wb, row, (short) 3, archBusiUviewEx.getUviewDesc(), false);
                cteateCell(wb, row, (short) 4, getShareType(archBusiUviewEx.getShareLv()), false);
                cteateCell(wb, row, (short) 5, getPubType(archBusiUviewEx.getPubSts()), false);
                cteateCell(wb, row, (short) 6, StringUtils.isEmpty(archBusiUviewEx.getShareCondition()) ? "无" : archBusiUviewEx.getShareCondition(), false);
                cteateCell(wb, row, (short) 7, StringUtils.isEmpty(archBusiUviewEx.getPubCondition()) ? "无" : archBusiUviewEx.getPubCondition(), false);
                cteateCell(wb, row, (short) 8, sysDictUpdateMap.get(archBusiUviewEx.getUpdateCyc()), false);
                cteateCell(wb, row, (short) 9, archBusiUviewEx.getDataScope(), false);
                cteateCell(wb, row, (short) 10, "共享平台方式", false);
                cteateCell(wb, row, (short) 11, "接口", false);
                cteateCell(wb, row, (short) 12, "否*", false);
                rowIndex++;
            } else {
                cteateCell(wb, row3, (short) 13, "信息项名称*", true);
                cteateCell(wb, row3, (short) 14, "是否主键*", true);
                cteateCell(wb, row3, (short) 15, "数据元编码", true);
                cteateCell(wb, row3, (short) 16, "数据元名称", true);
                cteateCell(wb, row3, (short) 17, "数据元类别*", true);
                cteateCell(wb, row3, (short) 18, "数据元类型*", true);
                cteateCell(wb, row3, (short) 19, "信息项数据长度*", true);
                for (int j = 0; j < arrayList.size(); j++) {
                    XSSFRow row = sheet3.createRow((short) rowIndex);
                    ArchBusiUviewStrEx archBusiUviewStrEx = arrayList.get(j);
                    cteateCell(wb, row, (short) 0, archBusiUviewEx.getUviewNm(), false);
                    cteateCell(wb, row, (short) 1, sysDictParentMap.get(archBusiUviewEx.getMediaFmt()), false);
                    cteateCell(wb, row, (short) 2, sysDictChildMap.get(archBusiUviewEx.getMediaFmt() + "-" + archBusiUviewEx.getMediaFmtType()), false);
                    cteateCell(wb, row, (short) 3, archBusiUviewEx.getUviewDesc(), false);
                    cteateCell(wb, row, (short) 4, getShareType(archBusiUviewEx.getShareLv()), false);
                    cteateCell(wb, row, (short) 5, getPubType(archBusiUviewEx.getPubSts()), false);
                    cteateCell(wb, row, (short) 6, StringUtils.isEmpty(archBusiUviewEx.getShareCondition()) ? "无" : archBusiUviewEx.getShareCondition(), false);
                    cteateCell(wb, row, (short) 7, StringUtils.isEmpty(archBusiUviewEx.getPubCondition()) ? "无" : archBusiUviewEx.getPubCondition(), false);
                    cteateCell(wb, row, (short) 8, sysDictUpdateMap.get(archBusiUviewEx.getUpdateCyc()), false);
                    cteateCell(wb, row, (short) 9, archBusiUviewEx.getDataScope(), false);
                    cteateCell(wb, row, (short) 10, "共享平台方式", false);
                    cteateCell(wb, row, (short) 11, "接口", false);
                    cteateCell(wb, row, (short) 12, "否", false);
                    cteateCell(wb, row, (short) 13, archBusiUviewStrEx.getSrcField(), false);
                    cteateCell(wb, row, (short) 14, "否", false);
                    cteateCell(wb, row, (short) 15, "", false);
                    cteateCell(wb, row, (short) 16, "", false);
                    cteateCell(wb, row, (short) 17, getExtStrLeibie(archBusiUviewStrEx.getSrcDataTyp()), false);
                    cteateCell(wb, row, (short) 18, getExtStrLeixing(archBusiUviewStrEx.getSrcDataTyp()), false);
                    cteateCell(wb, row, (short) 19, archBusiUviewStrEx.getDataLen(), false);
                    rowIndex++;
                }
            }

        }
    }
    private String getExtStrLeixing(String dataType){
        if(dataType.equals("STRING") || dataType.equals("TEXT")){
            return "Varchar-Varchar";
        }else if(dataType.equals("DATE")){
            return "Date-Date";
        }else if(dataType.equals("DATETIME")){
            return "Datetime-Datetime";
        }else if(dataType.equals("DECIMAL")){
            return "Decimal-Decimale";
        }else if( dataType.equals("DOUBLE")){
            return "Float-Float";
        }else if( dataType.equals("FLOAT")){
            return "Float-Float";
        }else if( dataType.equals("INTEGER")){
            return "Integer-Integer";
        }
        return "";
    }
    private String getExtStrLeibie(String dataType){
        if(dataType.equals("STRING") || dataType.equals("TEXT")){
            return "01-字符型";
        }else if(dataType.equals("DATE") || dataType.equals("DATETIME")){
            return "03-日期型";
        }else if(dataType.equals("DECIMAL") || dataType.equals("DOUBLE")|| dataType.equals("FLOAT")|| dataType.equals("INTEGER")){
            return "02-数值型";
        }else{
            return "05-其他类型";
        }
    }
    private String getShareType(String shareLv){
        if(shareLv.equals("01")){
            return "无条件共享";
        }else if(shareLv.equals("02")){
            return "有条件共享";
        }else{
            return "不予共享";
        }
    }
    private String getPubType(String pubSts){
        if("01".equals(pubSts)){
            return "无条件开放";
        }else if("02".equals(pubSts)){
            return "有条件开放";
        }else{
            return "不予开放";
        }
    }
    private void getSheet1(XSSFWorkbook wb,Integer depId){
        ArchOrg archOrg = archOrgService.getById(depId);
        List<ArchOrg> archOrgList = archOrgService.list(new QueryWrapper<ArchOrg>().eq(ArchOrg.PAR_ORG_ID,depId).orderByAsc(ArchOrg.DISPALY_SN));
        XSSFSheet sheet1 = wb.createSheet("1部门职能清单");
        sheet1.setDefaultColumnWidth((short)25);// 默认列宽

        XSSFRow row1 = sheet1.createRow((short) 0);
        cteateCell(wb, row1, (short) 0, "一、部门职能清单",true);
        XSSFRow row2 = sheet1.createRow((short) 1);
        cteateCell(wb, row2, (short) 0, "1、联系人基本信息",true);
        XSSFRow row3 = sheet1.createRow((short) 2);
        cteateCell(wb, row3, (short) 0, "单位名称",true);
        if(StringUtils.isEmpty(archOrg.getOrgNm())){
            cteateCell(wb, row3, (short) 1, "",false);
        }else{
            cteateCell(wb, row3, (short) 1, archOrg.getOrgNm(),false);
        }
        cteateCell(wb, row3, (short) 3, "统一社会信用代码",true);
        if(StringUtils.isEmpty(archOrg.getSocialCreditCd())){
            cteateCell(wb, row3, (short) 4, "",false);
        }else{
            cteateCell(wb, row3, (short) 4, archOrg.getSocialCreditCd(),false);
        }
        XSSFRow row4 = sheet1.createRow((short) 3);
        cteateCell(wb, row4, (short) 0, "联系人姓名",true);
        if(StringUtils.isEmpty(archOrg.getOrgLinkman())){
            cteateCell(wb, row4, (short) 1, "",false);
        }else{
            cteateCell(wb, row4, (short) 1, archOrg.getOrgLinkman(),false);
        }
        cteateCell(wb, row4, (short) 3, "联系人电话",true);
        if(StringUtils.isEmpty(archOrg.getOrgTel())){
            cteateCell(wb, row4, (short) 4, "",false);
        }else{
            cteateCell(wb, row4, (short) 4, archOrg.getOrgTel(),false);
        }
        XSSFRow row5 = sheet1.createRow((short) 4);
        cteateCell(wb, row5, (short) 0, "所在处（室/局）",true);
        cteateCell(wb, row5, (short) 1, "",false);
        cteateCell(wb, row5, (short) 3, "联系人邮箱",true);
        if(StringUtils.isEmpty(archOrg.getOrgMail())){
            cteateCell(wb, row5, (short) 4, "",false);
        }else{
            cteateCell(wb, row5, (short) 4, archOrg.getOrgMail(),false);
        }
        XSSFRow row6 = sheet1.createRow((short) 5);
        cteateCell(wb, row6, (short) 0, "传真",true);
        if(StringUtils.isEmpty(archOrg.getOrgFax())){
            cteateCell(wb, row6, (short) 1, "",false);
        }else{
            cteateCell(wb, row6, (short) 1, archOrg.getOrgFax(),false);
        }
        cteateCell(wb, row6, (short) 3, "填报时间",true);
        cteateCell(wb, row6, (short) 4, "",false);
        XSSFRow row7 = sheet1.createRow((short) 6);
        cteateCell(wb, row7, (short) 0, "通讯地址",true);
        if(StringUtils.isEmpty(archOrg.getAddr())){
            cteateCell(wb, row7, (short) 1, "",false);
        }else{
            cteateCell(wb, row7, (short) 1, archOrg.getAddr(),false);
        }
        XSSFRow row8 = sheet1.createRow((short) 7);
        cteateCell(wb, row8, (short) 0, "2、部门职能（最新颁布）",true);
        XSSFRow row9 = sheet1.createRow((short) 8);
        row9.setHeight((short)2000);
        if(StringUtils.isEmpty(archOrg.getOrgDuty())){
            cteateCell(wb, row9, (short) 0, "",false);
        }else{
            cteateCell(wb, row9, (short) 0, archOrg.getOrgDuty(),false);
        }
        XSSFRow row10 = sheet1.createRow((short) 9);
        cteateCell(wb, row10, (short) 0, "内设部门名称",true);
        cteateCell(wb, row10, (short) 1, "内设部门职责及业务范围",true);
        if(null != archOrgList) {
            for (int i = 0; i < archOrgList.size(); i++) {
                XSSFRow row = sheet1.createRow((short) 10+i);
                row.setHeight((short)1000);
                cteateCell(wb, row, (short) 0, archOrgList.get(i).getOrgNm(),false);
                if(StringUtils.isEmpty(archOrgList.get(i).getOrgDuty())){
                    cteateCell(wb, row, (short) 1, "",false);
                }else{
                    cteateCell(wb, row, (short) 1, archOrgList.get(i).getOrgDuty(),false);
                }
            }
        }

        //参数说明：1：开始行 2：结束行  3：开始列 4：结束列
        List<CellRangeAddress> craList = new ArrayList<CellRangeAddress>();
        craList.add(new CellRangeAddress(0,0,0,5));
        craList.add(new CellRangeAddress(1,1,0,5));
        craList.add(new CellRangeAddress(2,2,1,2));
        craList.add(new CellRangeAddress(2,2,4,5));
        craList.add(new CellRangeAddress(3,3,1,2));
        craList.add(new CellRangeAddress(3,3,4,5));
        craList.add(new CellRangeAddress(4,4,1,2));
        craList.add(new CellRangeAddress(4,4,4,5));
        craList.add(new CellRangeAddress(5,5,1,2));
        craList.add(new CellRangeAddress(5,5,4,5));
        craList.add(new CellRangeAddress(6,6,1,5));
        craList.add(new CellRangeAddress(7,7,0,5));
        craList.add(new CellRangeAddress(8,8,0,5));
        craList.add(new CellRangeAddress(9,9,1,5));
        for (CellRangeAddress cellRangeAddress : craList) {
            sheet1.addMergedRegion(cellRangeAddress);
            setBorder(1, cellRangeAddress, sheet1);
        }
        if(null != archOrgList){
            for (int i = 0; i < archOrgList.size(); i++) {
                CellRangeAddress cra = new CellRangeAddress(10+i,10+i,1,5);
                sheet1.addMergedRegion(cra);
                setBorder(1, cra, sheet1);
            }
        }

    }
    private void getSheet2(XSSFWorkbook wb,Integer depId){
        ExcelWriter writer = new ExcelWriter(wb, "2部门职责清单");
        //2.定义基础数据
        List<String> rowHead = CollUtil.newArrayList("职权信息");
        List<String> rowHead1 = CollUtil.newArrayList("实施主体","职责", "职权信息");
        List<String> rowHead2 = CollUtil.newArrayList("","","职权名称","职权类别","职权编码");
        //3.通过ExcelUtil.getBigWriter()创建Writer对象，BigExcelWriter用于大数据量的导出，不会引起溢出；
        //4.写入标题
        writer.writeRow(rowHead);
        writer.writeRow(rowHead1);
        writer.writeRow(rowHead2);

        List<CellRangeAddress> craList = new ArrayList<CellRangeAddress>();
        craList.add(new CellRangeAddress(0,0,0,4));
        craList.add(new CellRangeAddress(1,2,0,0));
        craList.add(new CellRangeAddress(1,2,1,1));
        craList.add(new CellRangeAddress(1,1,2,4));
        //最后合并单元格
        for (CellRangeAddress cellRangeAddress : craList) {
            writer.getSheet().addMergedRegion(cellRangeAddress);
            setBorder(1, cellRangeAddress, writer.getSheet());
        }
        //5.实现核心逻辑
        try {
            //6.定义容器保存人物数据
            List<List<Object>> rows = new LinkedList<>();
            //判断用户的权限
            List<ArchBusi> busiList = archBusiService.selectListContainsOrgNm(Arrays.asList(depId));
            //第一级 key机构名称 value一级权责集合
            Map<String, List<ArchBusi>> orgFirstMap = new HashMap<>();
            Map<Integer, ArchBusi> pIdMap = new HashMap<>();
            //解析数据
            int currentRowIndex = 3;
            String currentOrgName = "";
            String currentFirstName = "";
            int lastOrgRow = currentRowIndex, lastFirstRow = currentRowIndex;
            int orgStep = 0, firstStep = 0;
            for(int i=0;i< busiList.size();i++){
                ArchBusi archBusi = busiList.get(i);
                if(archBusi.getpId()==null || archBusi.getpId()==0){//一级数据
                    if(!pIdMap.containsKey(archBusi.getBusiId())){
                        pIdMap.put(archBusi.getBusiId(), archBusi);
                    }
                }else{//二级数据
                    List<Object> perList = new ArrayList<>();
                    perList.add(archBusi.getOrgNm());
                    perList.add(pIdMap.get(archBusi.getpId()).getBusiNm());
                    perList.add(archBusi.getBusiNm());
                    perList.add(archBusi.getBusiType());
                    perList.add(archBusi.getBusiNo());
                    rows.add(perList);
                    //单元格合并
                    if(currentRowIndex==3){
                        currentOrgName = archBusi.getOrgNm();
                        currentFirstName = pIdMap.get(archBusi.getpId()).getBusiNm();
                    }
                    if(currentOrgName.equals(archBusi.getOrgNm())){
                        orgStep++;
                    }else{
                        currentOrgName = archBusi.getOrgNm();
                        if(orgStep>1){
                            CellUtil.mergingCells(writer.getSheet(), lastOrgRow,lastOrgRow  + orgStep - 1,0,0, null);

                        }
                        lastOrgRow = lastOrgRow  + orgStep;
                        orgStep = 1;
                    }
                    if(currentFirstName.equals(pIdMap.get(archBusi.getpId()).getBusiNm())){
                        firstStep++;
                    }else{
                        currentFirstName = pIdMap.get(archBusi.getpId()).getBusiNm();
                        if(firstStep>1){
                            CellUtil.mergingCells(writer.getSheet(), lastFirstRow,lastFirstRow  + firstStep - 1,1,1, null);

                        }
                        lastFirstRow = lastFirstRow  + firstStep;
                        firstStep = 1;
                    }
                    //最后一行处理
                    if(i==busiList.size()-1){
                        if(orgStep>1){
                            CellUtil.mergingCells(writer.getSheet(), lastOrgRow,lastOrgRow  + orgStep - 1,0,0, null);

                        }
                        if(firstStep>1){
                            CellUtil.mergingCells(writer.getSheet(), lastFirstRow,lastFirstRow  + firstStep - 1,1,1, null);
                        }
                    }
                    currentRowIndex++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //关闭writer，释放内存
            writer.close();
        }
    }
//    private void getSheet2(XSSFWorkbook wb,Integer depId){
//        StringBuffer stringBuffer = new StringBuffer();
//        List<ArchBusi> archBusiList = archBusiService.getExportBusisByDepId(depId);
//        XSSFSheet sheet2 = wb.createSheet("2部门职责清单");
//        sheet2.setDefaultColumnWidth((short)25);// 默认列宽
//
//        XSSFRow row1 = sheet2.createRow((short) 0);
//        cteateCell(wb, row1, (short) 0, "二、部门职责清单",true);
//        XSSFRow row2 = sheet2.createRow((short) 1);
//        cteateCell(wb, row2, (short) 0, "序号",true);
//        cteateCell(wb, row2, (short) 1, "内设部门名称",true);
//        cteateCell(wb, row2, (short) 2, "内设部门职责及业务范围",true);
//        cteateCell(wb, row2, (short) 3, "权责清单编码",true);
//        cteateCell(wb, row2, (short) 4, "权责清单",true);
//        cteateCell(wb, row2, (short) 5, "服务对象",true);
//        cteateCell(wb, row2, (short) 6, "权责清单所需材料",true);
//        cteateCell(wb, row2, (short) 7, "权责清单产生材料",true);
//        cteateCell(wb, row2, (short) 8, "支撑的信息系统名称",true);
//        cteateCell(wb, row2, (short) 9, "备注",true);
//        CellRangeAddress cra = new CellRangeAddress(0,0,0,9);
//        sheet2.addMergedRegion(cra);
//        setBorder(1, cra, sheet2);
//
//
//        String orgName = "";
//        //合并单元格起始点
//        int rowNum = 2;
//        for (int i = 0; i < archBusiList.size(); i++) {
//            XSSFRow row = sheet2.createRow((short) i+2);
//            cteateCell(wb, row, (short) 0, String.valueOf(i+1),false);
//            cteateCell(wb, row, (short) 1, archBusiList.get(i).getOrgNm() == null? "" : archBusiList.get(i).getOrgNm(),false);
//            cteateCell(wb, row, (short) 2, archBusiList.get(i).getOrgDuty() == null? "" : archBusiList.get(i).getOrgDuty(),false);
//            cteateCell(wb, row, (short) 3, archBusiList.get(i).getBusiNo() == null? "" : archBusiList.get(i).getBusiNo(),false);
//            cteateCell(wb, row, (short) 4, archBusiList.get(i).getBusiNm() == null? "" : archBusiList.get(i).getBusiNm(),false);
//            cteateCell(wb, row, (short) 5, archBusiList.get(i).getServiceObjName() == null? "" : archBusiList.get(i).getServiceObjName(),false);
//            cteateCell(wb, row, (short) 6, "",false);
//            cteateCell(wb, row, (short) 7, "",false);
//            List<ArchAppSys> appList = archBusiService.getAppListByBusiId(archBusiList.get(i).getBusiId());
//            stringBuffer.setLength(0);
//            if(null != appList && appList.size()>0){
//                for (int j = 1; j <= appList.size(); j++) {
//                    if(j==1){
//                        stringBuffer.append(j+"."+appList.get(j-1).getAppsysNm());
//                    }else{
//                        stringBuffer.append("\r\n"+j+"."+appList.get(j-1).getAppsysNm());
//                    }
//                }
//            }
//            cteateCell(wb, row, (short) 8, stringBuffer.toString(),false);
//            cteateCell(wb, row, (short) 9, archBusiList.get(i).getRemark() == null? "" : archBusiList.get(i).getRemark(),false);
//
//            //判断是否合并单元格
//            orgName = archBusiList.get(i).getOrgNm();
//            if(!StringUtils.isEmpty(orgName)){
//                //最后一行
//                if(i+1 == archBusiList.size() || !orgName.equals(archBusiList.get(i+1).getOrgNm())){
//                    if(i+2>=rowNum){
//                        //大于一行才合并
//                        if(i+2>rowNum){
//                            CellRangeAddress cra1 = new CellRangeAddress(rowNum,i+2,1,1);
//                            sheet2.addMergedRegion(cra1);
//                            setBorder(1, cra1, sheet2);
//
//                            CellRangeAddress cra2 = new CellRangeAddress(rowNum,i+2,2,2);
//                            sheet2.addMergedRegion(cra2);
//                            setBorder(1, cra2, sheet2);
//                        }
//                        //设置下次合并起始行
//                        rowNum = i+3;
//                    }
//                }
//            }
//
//        }
//    }

    private void getSheet3(XSSFWorkbook wb,Integer depId) {
        QueryWrapper<ArchOrg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ArchOrg.PAR_ORG_ID, depId);
        List<ArchOrg> archOrgList = archOrgService.list(queryWrapper);
        Map<String, String> archOrgIdNmMap = new HashMap<>();
        archOrgList.forEach(item ->{
            archOrgIdNmMap.put(item.getOrgId().toString(), item.getOrgNm());
        });
        StringBuffer stringBuffer = new StringBuffer();
        List<Map<String,Object>> cataItemList = this.baseMapper.getCataItemExportList(depId);
        XSSFSheet sheet3 = wb.createSheet("2部门信息资源产生清单");
        sheet3.setDefaultColumnWidth((short) 25);// 默认列宽
        DictAssetCate dictAssetCate = null;
        //找出最多层的部门类,规划列数
        String fullTypeCode = "";
        for (Map<String, Object> stringObjectMap : cataItemList) {
            if(null != stringObjectMap.get("TYPE_ID")){
                int typeId = Integer.parseInt(stringObjectMap.get("TYPE_ID").toString());
                dictAssetCate = dictAssetCateService.getById(typeId);
                if(null != dictAssetCate){
                    if(dictAssetCate.getFullTypCd().length() > fullTypeCode.length()){
                        fullTypeCode = dictAssetCate.getFullTypCd();
                    }
                }
            }
        }
        int typeCol = 0;
        if(fullTypeCode.length() == 0) {
            typeCol = 0;
        }else if(fullTypeCode.length() == 1){
            typeCol =1;
        }else if(fullTypeCode.length() == 3){
            typeCol =2;
        }else{
            typeCol =fullTypeCode.length()/2;
        }
        if(typeCol <4){
            typeCol = 4;
        }
        if(typeCol >= 4){
            XSSFRow row1 = sheet3.createRow((short) 0);
            cteateCell(wb, row1, (short) 0, "二、政务信息资源产生清单",true);
            XSSFRow row2 = sheet3.createRow((short) 1);
            cteateCell(wb, row2, (short) 0, "序号",true);
            cteateCell(wb, row2, (short) 1, "部门类信息资源分类",true);
            cteateCell(wb, row2, (short) 2, "项分类",true);
            cteateCell(wb, row2, (short) 3, "目分类",true);
            cteateCell(wb, row2, (short) 4, "细目分类1",true);
            for (int i = 0; i < typeCol - 4; i++) {
                cteateCell(wb, row2, (short) (i+5), "细目分类"+(i+2),true);
            }
            cteateCell(wb, row2, (short) (typeCol+1), "关联分类",true);
            cteateCell(wb, row2, (short) (typeCol+2), "信息资源代码",true);
            cteateCell(wb, row2, (short) (typeCol+3), "信息资源名称",true);
            cteateCell(wb, row2, (short) (typeCol+4), "内设部门名称",true);
            cteateCell(wb, row2, (short) (typeCol+5), "信息资源摘要",true);
            cteateCell(wb, row2, (short) (typeCol+6), "信息资源格式",true);
            cteateCell(wb, row2, (short) (typeCol+9), "信息项信息",true);
            cteateCell(wb, row2, (short) (typeCol+14), "共享属性",true);
            cteateCell(wb, row2, (short) (typeCol+16), "开放属性",true);
            cteateCell(wb, row2, (short) (typeCol+18), "更新周期",true);
            cteateCell(wb, row2, (short) (typeCol+19), "发布日期",true);

            XSSFRow row3 = sheet3.createRow((short) 2);
            cteateCell(wb, row3, (short) (typeCol+6), "信息资源格式分类",true);
            cteateCell(wb, row3, (short) (typeCol+7), "信息资源格式类型",true);
            cteateCell(wb, row3, (short) (typeCol+8), "其他类型资源格式描述",true);
            cteateCell(wb, row3, (short) (typeCol+9), "信息项名称",true);
            cteateCell(wb, row3, (short) (typeCol+10), "数据元",true);
            cteateCell(wb, row3, (short) (typeCol+11), "关联代码集",true);
            cteateCell(wb, row3, (short) (typeCol+12), "数据类型",true);
            cteateCell(wb, row3, (short) (typeCol+13), "数据长度",true);
            cteateCell(wb, row3, (short) (typeCol+14), "共享类型",true);
            cteateCell(wb, row3, (short) (typeCol+15), "共享条件/不予共享依据",true);
            cteateCell(wb, row3, (short) (typeCol+16), "是否向社会开放",true);
            cteateCell(wb, row3, (short) (typeCol+17), "开放条件",true);
            List<CellRangeAddress> craList = new ArrayList<CellRangeAddress>();
            craList.add(new CellRangeAddress(0,0,0,typeCol+19));
            craList.add(new CellRangeAddress(1,1,typeCol+6,typeCol+8));
            craList.add(new CellRangeAddress(1,1,typeCol+9,typeCol+13));
            craList.add(new CellRangeAddress(1,1,typeCol+14,typeCol+15));
            craList.add(new CellRangeAddress(1,1,typeCol+16,typeCol+17));
            for (int j = 0; j < typeCol+6; j++) {
                craList.add(new CellRangeAddress(1,2,j,j));
            }
            craList.add(new CellRangeAddress(1,2,typeCol+18,typeCol+18));
            craList.add(new CellRangeAddress(1,2,typeCol+19,typeCol+19));

            for (CellRangeAddress cellRangeAddress : craList) {
                sheet3.addMergedRegion(cellRangeAddress);
                setBorder(1, cellRangeAddress, sheet3);
            }

            String cataName = "";
            //合并单元格起始点
            int rowNum = 3;
            for (int i = 0; i < cataItemList.size(); i++) {
                XSSFRow row = sheet3.createRow((short) i+3);
                cteateCell(wb, row, (short) 0, String.valueOf(i+1),false);
                if(null != cataItemList.get(i).get("TYPE_ID")){
                    int typeId = Integer.parseInt(cataItemList.get(i).get("TYPE_ID").toString());
                    dictAssetCate = dictAssetCateService.getById(typeId);
                }else{
                    dictAssetCate = null;
                }
                if(null != dictAssetCate){
                    String fullTypCd = dictAssetCate.getFullTypCd();
                    if(fullTypCd.length()>=1){
                        dictAssetCate = dictAssetCateService.getOne(new QueryWrapper<DictAssetCate>()
                                .eq(DictAssetCate.FULL_TYP_CD,fullTypCd.substring(0,1)));
                        if(null != dictAssetCate){
                            cteateCell(wb, row, (short) 1, dictAssetCate.getTypNm(),false);
                        }else{
                            cteateCell(wb, row, (short) 1, "",false);
                        }
                    }else{
                        cteateCell(wb, row, (short) 1, "",false);
                    }
                    if(fullTypCd.length()>=3){
                        dictAssetCate = dictAssetCateService.getOne(new QueryWrapper<DictAssetCate>()
                                .eq(DictAssetCate.FULL_TYP_CD,fullTypCd.substring(0,3)));
                        if(null != dictAssetCate){
                            cteateCell(wb, row, (short) 2, dictAssetCate.getTypNm(),false);
                        }else{
                            cteateCell(wb, row, (short) 2, "",false);
                        }
                    }else{
                        cteateCell(wb, row, (short) 2, "",false);
                    }

                    if(fullTypCd.length()>=6){
                        dictAssetCate = dictAssetCateService.getOne(new QueryWrapper<DictAssetCate>()
                                .eq(DictAssetCate.FULL_TYP_CD,fullTypCd.substring(0,6)));
                        if(null != dictAssetCate){
                            cteateCell(wb, row, (short) 3, dictAssetCate.getTypNm(),false);
                        }else{
                            cteateCell(wb, row, (short) 3, "",false);
                        }
                    }else{
                        cteateCell(wb, row, (short) 3, "",false);
                    }
                    if(fullTypCd.length()>=8){
                        dictAssetCate = dictAssetCateService.getOne(new QueryWrapper<DictAssetCate>()
                                .eq(DictAssetCate.FULL_TYP_CD,fullTypCd.substring(0,8)));
                        if(null != dictAssetCate){
                            cteateCell(wb, row, (short) 4, dictAssetCate.getTypNm(),false);
                        }else{
                            cteateCell(wb, row, (short) 4, "",false);
                        }
                    }else{
                        cteateCell(wb, row, (short) 4, "",false);
                    }
                    //部门分类多余4个标准列的情况
                    for (int j = 0; j < typeCol - 4; j++) {
                        if(fullTypCd.length()>=(j+5)*2){
                            dictAssetCate = dictAssetCateService.getOne(new QueryWrapper<DictAssetCate>()
                                    .eq(DictAssetCate.FULL_TYP_CD,fullTypCd.substring(0,(j+5)*2)));
                            if(null != dictAssetCate){
                                cteateCell(wb, row, (short)(j+5), dictAssetCate.getTypNm(),false);
                            }else{
                                cteateCell(wb, row, (short) (j+5), "",false);
                            }
                        }else{
                            cteateCell(wb, row, (short) (j+5), "",false);
                        }
                    }
                }else{
                    cteateCell(wb, row, (short) 1, "",false);
                    cteateCell(wb, row, (short) 2, "",false);
                    cteateCell(wb, row, (short) 3, "",false);
                    cteateCell(wb, row, (short) 4, "",false);
                    for (int j = 0; j < typeCol - 4; j++) {
                        cteateCell(wb, row, (short) (j+5), "",false);
                    }
                }
                if(null != cataItemList.get(i).get("UVIEW_ID")){
                    List<String> typeList = this.baseMapper.getTypeListByInfoId(Integer.parseInt(cataItemList.get(i).get("UVIEW_ID").toString()));
                    stringBuffer.setLength(0);
                    if(null != typeList && typeList.size()>0){
                        for (int j = 1; j <= typeList.size(); j++) {
                            if(j==1){
                                stringBuffer.append(j+"."+typeList.get(j-1));
                            }else{
                                stringBuffer.append("\r\n"+j+"."+typeList.get(j-1));
                            }
                        }
                    }
                    cteateCell(wb, row, (short) (typeCol+1), stringBuffer.toString(),false);
                }else{
                    cteateCell(wb, row, (short) (typeCol+1), "",false);
                }

                cteateCell(wb, row, (short) (typeCol+2), cataItemList.get(i).get("UVIEW_NO") == null ? ""
                        :cataItemList.get(i).get("UVIEW_NO").toString(),false);
                cteateCell(wb, row, (short) (typeCol+3), cataItemList.get(i).get("UVIEW_NM") == null ? ""
                        :cataItemList.get(i).get("UVIEW_NM").toString(),false);
                cteateCell(wb, row, (short) (typeCol+4), getBelongName(cataItemList.get(i).get("BELONG_TO"), archOrgIdNmMap),false);
                cteateCell(wb, row, (short) (typeCol+5), cataItemList.get(i).get("UVIEW_DESC") == null ? ""
                        :cataItemList.get(i).get("UVIEW_DESC").toString(),false);
                cteateCell(wb, row, (short) (typeCol+6), cataItemList.get(i).get("MF_NAME") == null ? ""
                        :cataItemList.get(i).get("MF_NAME").toString(),false);
                cteateCell(wb, row, (short) (typeCol+7), cataItemList.get(i).get("MFT_NAME") == null ? ""
                        :cataItemList.get(i).get("MFT_NAME").toString(),false);
                cteateCell(wb, row, (short) (typeCol+8), "",false);
                cteateCell(wb, row, (short) (typeCol+9), cataItemList.get(i).get("SRC_FIELD") == null ? ""
                        :cataItemList.get(i).get("SRC_FIELD").toString(),false);
                cteateCell(wb, row, (short) (typeCol+10), "",false);
                cteateCell(wb, row, (short) (typeCol+11), "",false);
                cteateCell(wb, row, (short) (typeCol+12), cataItemList.get(i).get("DT_NAME") == null ? ""
                        :cataItemList.get(i).get("DT_NAME").toString(),false);
                cteateCell(wb, row, (short) (typeCol+13), cataItemList.get(i).get("DATA_LEN") == null ? ""
                        :cataItemList.get(i).get("DATA_LEN").toString(),false);
                cteateCell(wb, row, (short) (typeCol+14), cataItemList.get(i).get("SL_NAME") == null ? ""
                        :cataItemList.get(i).get("SL_NAME").toString(),false);
                cteateCell(wb, row, (short) (typeCol+15), cataItemList.get(i).get("SHARE_CONDITION") == null ? ""
                        :cataItemList.get(i).get("SHARE_CONDITION").toString(),false);
                cteateCell(wb, row, (short) (typeCol+16), cataItemList.get(i).get("PS_NAME") == null ? ""
                        :cataItemList.get(i).get("PS_NAME").toString(),false);
                cteateCell(wb, row, (short) (typeCol+17), cataItemList.get(i).get("PUB_CONDITION") == null ? ""
                        :cataItemList.get(i).get("PUB_CONDITION").toString(),false);
                cteateCell(wb, row, (short) (typeCol+18), cataItemList.get(i).get("UC_NAME") == null ? ""
                        :cataItemList.get(i).get("UC_NAME").toString(),false);
                cteateCell(wb, row, (short) (typeCol+19), cataItemList.get(i).get("PUB_DT") == null ? ""
                        :cataItemList.get(i).get("PUB_DT").toString(),false);
                //判断是否合并单元格
                cataName = String.valueOf(cataItemList.get(i).get("UVIEW_NM"));
                if(!StringUtils.isEmpty(cataName)){
                    //最后一行或者下一行值不一样合并
                    if(i+1 == cataItemList.size() || !cataName.equals(cataItemList.get(i+1).get("UVIEW_NM"))){
                        if(i+3 >=rowNum){
                            //大于一行才合并
                            if(i+3 >rowNum){
                                CellRangeAddress cra1 = new CellRangeAddress(rowNum,i+3,typeCol+3,typeCol+3);
                                sheet3.addMergedRegion(cra1);
                                setBorder(1, cra1, sheet3);

                                CellRangeAddress cra2 = new CellRangeAddress(rowNum,i+3,typeCol+5,typeCol+5);
                                sheet3.addMergedRegion(cra2);
                                setBorder(1, cra2, sheet3);
                            }
                            //设置下次合并的开始行
                            rowNum = i+4;
                        }

                    }
                }

            }
        }

    }
    private String getBelongName(Object belong, Map<String, String> orgIdNmMap){
        if(belong!=null && !StringUtils.isEmpty(belong.toString())){
            String[] idStrArray = belong.toString().split(",");
            List<String> nmList = new ArrayList<>();
            for (String s : idStrArray) {
                nmList.add(orgIdNmMap.get(s));
            }
            return String.join(",", nmList);
        }
        return "";
    }
    private void getSheet4(XSSFWorkbook wb,Integer depId) {
        List<Map<String,Object>> cataRequireList = this.baseMapper.getCataRequireExportList(depId);
        XSSFSheet sheet4 = wb.createSheet("4部门信息资源需求清单");
        sheet4.setDefaultColumnWidth((short) 25);// 默认列宽

        XSSFRow row1 = sheet4.createRow((short) 0);
        cteateCell(wb, row1, (short) 0, "四、政务信息资源需求表", true);
        XSSFRow row2 = sheet4.createRow((short) 1);
        cteateCell(wb, row2, (short) 0, "序号",true);
        cteateCell(wb, row2, (short) 1, "所需政务信息资源名称",true);
        cteateCell(wb, row2, (short) 2, "政务信息资源提供方",true);
        cteateCell(wb, row2, (short) 3, "政务信息资源描述",true);
        cteateCell(wb, row2, (short) 4, "是否已获取",true);
        cteateCell(wb, row2, (short) 5, "建议获取方式",true);
        cteateCell(wb, row2, (short) 6, "期望数据使用频率",true);
        cteateCell(wb, row2, (short) 7, "用途",true);
        cteateCell(wb, row2, (short) 8, "所需机构",true);

        CellRangeAddress cra = new CellRangeAddress(0,0,0,8);
        sheet4.addMergedRegion(cra);
        setBorder(1, cra, sheet4);

        for (int i = 0; i < cataRequireList.size(); i++) {
            XSSFRow row = sheet4.createRow((short) i + 2);
            cteateCell(wb, row, (short) 0, String.valueOf(i + 1), false);
            cteateCell(wb, row, (short) 1, cataRequireList.get(i).get("REQUIRE_NAME") == null ? ""
                    : cataRequireList.get(i).get("REQUIRE_NAME").toString(), false);
            cteateCell(wb, row, (short) 2, cataRequireList.get(i).get("DEP_NAME") == null ? ""
                    : cataRequireList.get(i).get("DEP_NAME").toString(), false);
            cteateCell(wb, row, (short) 3, cataRequireList.get(i).get("REMARK") == null ? ""
                    : cataRequireList.get(i).get("REMARK").toString(), false);
            cteateCell(wb, row, (short) 4, cataRequireList.get(i).get("IS_ACCESS") == null ? ""
                    : cataRequireList.get(i).get("IS_ACCESS").toString(), false);
            cteateCell(wb, row, (short) 5, cataRequireList.get(i).get("ACCESS_WAY") == null ? ""
                    : cataRequireList.get(i).get("ACCESS_WAY").toString(), false);
            cteateCell(wb, row, (short) 6, cataRequireList.get(i).get("UPDATE_CYC") == null ? ""
                    : cataRequireList.get(i).get("UPDATE_CYC").toString(), false);
            cteateCell(wb, row, (short) 7, cataRequireList.get(i).get("PURPOSE") == null ? ""
                    : cataRequireList.get(i).get("PURPOSE").toString(), false);
            cteateCell(wb, row, (short) 8, cataRequireList.get(i).get("ORG_NAME") == null ? ""
                    : cataRequireList.get(i).get("ORG_NAME").toString(), false);
        }
    }

    private void getSheet5(XSSFWorkbook wb,Integer depId) {
        StringBuffer stringBuffer = new StringBuffer();
        List<Map<String,Object>> appSysList = this.baseMapper.getAppSysExportList(depId);
        XSSFSheet sheet5 = wb.createSheet("3部门信息系统清单");
        sheet5.setDefaultColumnWidth((short) 25);// 默认列宽

        XSSFRow row1 = sheet5.createRow((short) 0);
        cteateCell(wb, row1, (short) 0, "三、部门信息系统清单", true);
        XSSFRow row2 = sheet5.createRow((short) 1);
        cteateCell(wb, row2, (short) 0, "序号",true);
        cteateCell(wb, row2, (short) 1, "内设部门名称",true);
        cteateCell(wb, row2, (short) 2, "信息系统名称",true);
        cteateCell(wb, row2, (short) 3, "信息系统代码",true);
        cteateCell(wb, row2, (short) 4, "信息系统功能描述",true);
        cteateCell(wb, row2, (short) 5, "信息系统产生的信息资源",true);
        cteateCell(wb, row2, (short) 6, "信息系统建设性质",true);
        cteateCell(wb, row2, (short) 7, "信息系统部署位置",true);
        cteateCell(wb, row2, (short) 8, "信息系统接入网络类型",true);
        cteateCell(wb, row2, (short) 9, "备注",true);
        CellRangeAddress cra = new CellRangeAddress(0,0,0,9);
        sheet5.addMergedRegion(cra);
        setBorder(1, cra, sheet5);

        for (int i = 0; i < appSysList.size(); i++) {
            XSSFRow row = sheet5.createRow((short) i + 2);
            cteateCell(wb, row, (short) 0, String.valueOf(i + 1), false);
            cteateCell(wb, row, (short) 1, appSysList.get(i).get("ORG_NAME") == null ? ""
                    : appSysList.get(i).get("ORG_NAME").toString(), false);
            cteateCell(wb, row, (short) 2, appSysList.get(i).get("APPSYS_NAME") == null ? ""
                    : appSysList.get(i).get("APPSYS_NAME").toString(), false);
            cteateCell(wb, row, (short) 3, appSysList.get(i).get("APPSYS_NO") == null ? ""
                    : appSysList.get(i).get("APPSYS_NO").toString(), false);
            cteateCell(wb, row, (short) 4, appSysList.get(i).get("APPSYS_DESC") == null ? ""
                    : appSysList.get(i).get("APPSYS_DESC").toString(), false);

            if(null != appSysList.get(i).get("APPSYS_ID")){
                List<String> cataList = this.baseMapper.getCataListByAppId(Integer.parseInt(appSysList.get(i).get("APPSYS_ID").toString()));
                stringBuffer.setLength(0);
                if(null != cataList && cataList.size()>0){
                    for (int j = 1; j <= cataList.size(); j++) {
                        if(j==1){
                            stringBuffer.append(j+"."+cataList.get(j-1));
                        }else{
                            stringBuffer.append("\r\n"+j+"."+cataList.get(j-1));
                        }
                    }
                }
                cteateCell(wb, row, (short)5, stringBuffer.toString(),false);
            }

            cteateCell(wb, row, (short) 6, appSysList.get(i).get("BUILD_NATURE") == null ? ""
                    : appSysList.get(i).get("BUILD_NATURE").toString(), false);
            cteateCell(wb, row, (short) 7, appSysList.get(i).get("DEPLOY_LOCATION") == null ? ""
                    : appSysList.get(i).get("DEPLOY_LOCATION").toString(), false);
            cteateCell(wb, row, (short) 8, appSysList.get(i).get("NETWORK_TYPE") == null ? ""
                    : appSysList.get(i).get("NETWORK_TYPE").toString(), false);
            cteateCell(wb, row, (short) 9, appSysList.get(i).get("REMARK") == null ? ""
                    : appSysList.get(i).get("REMARK").toString(), false);
        }
    }
    private void cteateCell(XSSFWorkbook wb, XSSFRow row, short col,String val,boolean isTitle) {
        //设置行高
        //row.setHeight((short) 480);
        XSSFCell cell = row.createCell(col);
        cell.setCellValue(val);
        XSSFCellStyle cellstyle = wb.createCellStyle();
        XSSFFont font = wb.createFont();
        if(isTitle){
            font.setBold(true);
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 11);
        }else{
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 10);
        }
        cellstyle.setFont(font);
        cellstyle.setWrapText(true);
        cellstyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellstyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellstyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
        cellstyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
        cellstyle.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
        cellstyle.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
        cell.setCellStyle(cellstyle);
    }

     private void setBorder(int border, CellRangeAddress cra, Sheet sheet){
         // 使用RegionUtil类为合并后的单元格添加边框
         RegionUtil.setBorderBottom(border, cra, sheet); // 下边框
         RegionUtil.setBorderLeft(border, cra, sheet); // 左边框
         RegionUtil.setBorderRight(border, cra, sheet); // 有边框
         RegionUtil.setBorderTop(border, cra, sheet); // 上边框
     }
}
