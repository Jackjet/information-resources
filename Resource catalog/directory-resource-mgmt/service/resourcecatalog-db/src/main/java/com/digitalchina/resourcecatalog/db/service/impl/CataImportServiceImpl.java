package com.digitalchina.resourcecatalog.db.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalchina.resourcecatalog.db.dao.CataImportMapper;
import com.digitalchina.resourcecatalog.db.domain.*;
import com.digitalchina.resourcecatalog.db.dto.CataInfoTempDto;
import com.digitalchina.resourcecatalog.db.service.*;
import com.digitalchina.resourcecatalog.db.util.ChinesePinyinUtil;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangys
 * @since 2020-05-12
 */
@Service
public class CataImportServiceImpl extends ServiceImpl<CataImportMapper, CataImport> implements CataImportService {
    @Autowired
    ArchOrgService archOrgService;
    @Autowired
    CataInfoTempService cataInfoTempService;
    @Autowired
    CataInfoTempTypeRelService cataInfoTempTypeRelService;
    @Autowired
    CataInfoItemTempRelService cataInfoItemTempRelService;
    @Autowired
    DictAssetCateService dictAssetCateService;
    @Autowired
    SysDictService sysDictService;
    @Autowired
    CataRequireService cataRequireService;
    @Autowired
    ArchAppSysService archAppSysService;
    @Autowired
    ArchBusiService archBusiService;
    @Autowired
    CataAppBusRelService cataAppBusRelService;
    @Autowired
    CataOrgItemRelService cataOrgItemRelService;
    @Autowired
    CataImportErrorService cataImportErrorService;
    @Autowired
    private SysStorageService sysStorageService;

    @Override
    public void saveData(ArchOrg archOrg,SysUser user,Workbook wb,List<String> errorList,List<String> infoList) {
        Date currentDate = new Date();
        //资源目录
        List<CataInfoTempDto> cataList =new ArrayList<CataInfoTempDto>();

        //3部门信息资源产生清单
        getCataInfo(wb.getSheetAt(0),archOrg,errorList,cataList);

        //处理数据
        if(errorList.size() == 0){
            //导入资源目录
            //如果同部门有相同的资源目录，替换掉
            for (CataInfoTempDto cataInfoTempDto : cataList) {
                CataInfoTemp oldBean = cataInfoTempService.getOne(
                        new QueryWrapper<CataInfoTemp>().lambda()
                                .eq(CataInfoTemp::getProvOrgId,archOrg.getOrgId())
                                .eq(CataInfoTemp::getUviewNm,cataInfoTempDto.getUviewNm()));
                if(null != oldBean){
                    if("1".equals(oldBean.getAuditStatus())){
                        infoList.add("部门《"+archOrg.getOrgNm()+"》已存在名称为《"+oldBean.getUviewNm()+"》的待审核信息资源记录。");
                    }else if("3".equals(oldBean.getAuditStatus())){
                        infoList.add("部门《"+archOrg.getOrgNm()+"》已存在名称为《"+oldBean.getUviewNm()+"》的初审通过待终审信息资源记录。");
                    } else{
                        cataInfoTempDto.setUviewId(oldBean.getUviewId());
                        cataInfoTempDto.setUviewNo(oldBean.getUviewNo());
                        cataInfoTempDto.setAuditStatus("0");
                        cataInfoTempDto.setPubDt(currentDate);
                        cataInfoTempDto.setUpdateDt(currentDate);
                        cataInfoTempDto.setUpdater(user.getUsername());
                        cataInfoTempDto.setProvOrgId(archOrg.getOrgId());
                        cataInfoTempDto.setProvOrgCode(archOrg.getSocialCreditCd());
                        cataInfoTempService.saveInfoTemp_import(cataInfoTempDto);
                    }
                }else{
                    String fullTypCd = dictAssetCateService.getOne(new QueryWrapper<DictAssetCate>()
                            .eq(DictAssetCate.TYP_ID,cataInfoTempDto.getCataInfoTempTypeRelList().get(0).getTypeId())).getFullTypCd();
                    CataInfoTemp cata=cataInfoTempService.getOne(new QueryWrapper<CataInfoTemp>().likeRight(CataInfoTemp.UVIEW_NO, fullTypCd+"/").eq(CataInfoTemp.DELETED, 0).orderByDesc(CataInfoTemp.UVIEW_NO));
                    if(cata!=null){
                        String code=cata.getUviewNo().split("/")[1];
                        int temp=Integer.parseInt(code)+1;
                        code=String.format("%06d",temp);
                        cataInfoTempDto.setUviewNo(fullTypCd+"/"+code);
                    }else{
                        cataInfoTempDto.setUviewNo(fullTypCd+"/000001");
                    }
                    cataInfoTempDto.setAuditStatus("0");
                    cataInfoTempDto.setPubDt(currentDate);
                    cataInfoTempDto.setCrtDt(currentDate);
                    cataInfoTempDto.setCreator(user.getUsername());
                    cataInfoTempDto.setProvOrgId(archOrg.getOrgId());
                    cataInfoTempDto.setProvOrgCode(archOrg.getSocialCreditCd());
                    cataInfoTempService.saveInfoTemp_import(cataInfoTempDto);
                }
            }

        }
    }

    @Override
    public void saveCataImport(CataImport cataImport) {

    }

    @Override
    public List<CataImportError> getErrorList(Integer id){
        return cataImportErrorService.list(new QueryWrapper<CataImportError>().eq(CataImportError.IMPORT_ID,id));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        CataImport bean = this.getById(id);
        bean.setDeleted(1);
        if(this.updateById(bean)){
            cataImportErrorService.remove(new QueryWrapper<CataImportError>().eq(CataImportError.IMPORT_ID, bean.getId()));
            SysStorage sysFileStore =sysStorageService.getOne(new QueryWrapper<SysStorage>().eq(SysStorage.KEY, bean.getFileKey()));
            if(null != sysFileStore){
                sysFileStore.setDeleted(1);
                sysStorageService.updateById(sysFileStore);
            }
        }
    }

    @Override
    public IPage<List<CataImport>> selectPages(Page page, Integer depId, String startDate, String endDate){
        return this.baseMapper.selectPages(page, depId, startDate, endDate);
    }

    //1部门职能清单
    private ArchOrg getArchOrg(Sheet sheet,List<String>errorList,List<ArchOrg> belongToList){
        ArchOrg archOrg = new ArchOrg();
        Cell cell = null;
        Row row = null;
        int rowNum = sheet.getLastRowNum();
        if(rowNum > 1000){
            errorList.add("《1部门职能清单》页，异常原因：行数超过1000行。");
            return new ArchOrg();
        }else if(rowNum<9){
            errorList.add("《1部门职能清单》页，异常原因：模板不正确，行数不够。");
        }else{
            //第3行
            row = sheet.getRow(2);
            int colNum = row.getLastCellNum();
            if(colNum<5){
                errorList.add("《1部门职能清单》页，第3行，异常原因：模板不正确，列数不够。");
            }else{
                //单位名称
                cell = row.getCell(1);
                cell.setCellType(CellType.STRING);
                String orgNm = cell.getStringCellValue().trim();
                if(StringUtils.isEmpty(orgNm)){
                    errorList.add("《1部门职能清单》页，第3行，异常原因：单位名称不能为空。");
                }else{
                    archOrg = archOrgService.getOne(new QueryWrapper<ArchOrg>().lambda()
                            .eq(ArchOrg::getOrgNm,orgNm).isNull(ArchOrg::getParOrgId));
                    if(archOrg == null){
                        errorList.add("《1部门职能清单》页，第3行，异常原因：组织机构【"+orgNm+"】未维护。");
                        archOrg = new ArchOrg();
                    }
                }
                //统一社会信用代码
                cell = row.getCell(4);
                cell.setCellType(CellType.STRING);
                String socialCreditCd = cell.getStringCellValue().trim();
                if(!StringUtils.isEmpty(socialCreditCd)){
                    archOrg.setSocialCreditCd(socialCreditCd);
                }else{
                    archOrg.setSocialCreditCd("");
                }
            }
            //第4行
            row = sheet.getRow(3);
            colNum = row.getLastCellNum();
            if(colNum<5){
                errorList.add("《1部门职能清单》页，第4行，异常原因：模板不正确，列数不够。");
            }else{
                //联系人姓名
                cell = row.getCell(1);
                cell.setCellType(CellType.STRING);
                String orgLinkman = cell.getStringCellValue().trim();
                if(!StringUtils.isEmpty(orgLinkman)){
                    archOrg.setOrgLinkman(orgLinkman);
                }else{
                    archOrg.setOrgLinkman("");
                }
                //联系人电话
                cell = row.getCell(4);
                cell.setCellType(CellType.STRING);
                String orgTel = cell.getStringCellValue().trim();
                if(!StringUtils.isEmpty(orgTel)){
                    archOrg.setOrgTel(orgTel);
                }else{
                    archOrg.setOrgTel("");
                }
            }
            //第5行
            row = sheet.getRow(4);
            colNum = row.getLastCellNum();
            if(colNum<5){
                errorList.add("《1部门职能清单》页，第5行，异常原因：模板不正确，列数不够。");
            }else{
                //所在处（室/局）不存表，不用验证了
                //联系人邮箱
                cell = row.getCell(4);
                cell.setCellType(CellType.STRING);
                String orgMail = cell.getStringCellValue().trim();
                if(!StringUtils.isEmpty(orgMail)){
                    archOrg.setOrgMail(orgMail);
                }else{
                    archOrg.setOrgMail("");
                }
            }
            //第6行
            row = sheet.getRow(5);
            colNum = row.getLastCellNum();
            if(colNum<2){
                errorList.add("《1部门职能清单》页，第6行，异常原因：模板不正确，列数不够。");
            }else{
                //填报时间取系统时间，不用验证了
                //传真
                cell = row.getCell(1);
                cell.setCellType(CellType.STRING);
                String orgFax = cell.getStringCellValue().trim();
                if(!StringUtils.isEmpty(orgFax)){
                    archOrg.setOrgFax(orgFax);
                }else{
                    archOrg.setOrgFax("");
                }
            }
            //第7行
            row = sheet.getRow(6);
            colNum = row.getLastCellNum();
            if(colNum<2){
                errorList.add("《1部门职能清单》页，第7行，异常原因：模板不正确，列数不够。");
            }else{
                //通讯地址
                cell = row.getCell(1);
                cell.setCellType(CellType.STRING);
                String addr = cell.getStringCellValue().trim();
                if(!StringUtils.isEmpty(addr)){
                    archOrg.setAddr(addr);
                }else{
                    archOrg.setAddr("");
                }
            }
            //第9行
            row = sheet.getRow(8);
            //部门职能
            cell = row.getCell(0);
            cell.setCellType(CellType.STRING);
            String orgDuty = cell.getStringCellValue().trim();
            if(StringUtils.isEmpty(orgDuty)){
                errorList.add("《1部门职能清单》页，第9行，异常原因：部门职能不能为空。");
            }else{
                archOrg.setOrgDuty(orgDuty);
            }

            //部门存在的情况，检查内设机构
            if(null != archOrg.getOrgId()){
                //第11行开始
                for (int i = 10; i <= rowNum; i++) {
                    ArchOrg belongToBean = null;
                    row = sheet.getRow(i);
                    if (row.getLastCellNum() < 2) {
                        errorList.add("《1部门职能清单》页，第"+(i+1)+"行，异常原因：模板不正确，列数不够。");
                    } else {
                        //内设机构名称
                        cell = row.getCell(0);
                        cell.setCellType(CellType.STRING);
                        String belongTo = cell.getStringCellValue().trim();
                        if(!StringUtils.isEmpty(belongTo)){
                            belongToBean = archOrgService.getOne(
                                    new QueryWrapper<ArchOrg>().lambda()
                                            .eq(ArchOrg::getOrgNm,belongTo)
                                            .eq(ArchOrg::getParOrgId,archOrg.getOrgId()));
                            if(belongToBean == null){
                                errorList.add("《1部门职能清单》页，第"+(i+1)+"行，异常原因：内设部门名称【"+belongTo+"】未维护。");
                            }
                        }else{
//                            errorList.add("《1部门职能清单》页，第"+(i+1)+"行，异常原因：内设部门名称不能为空。");
                        }

                        //内设机构职责及业务范围
                        cell = row.getCell(1);
                        cell.setCellType(CellType.STRING);
                        String belongToDuty = cell.getStringCellValue().trim();
                        if(!StringUtils.isEmpty(belongToDuty)){
                            if(null != belongToBean){
                                belongToBean.setOrgDuty(belongToDuty);
                                belongToList.add(belongToBean);
                            }
                        }else{
//                            errorList.add("《1部门职能清单》页，第"+(i+1)+"行，异常原因：内设部门名称职责及业务范围不能为空。");
                        }
                    }
                }
            }
        }
        return archOrg;
    }

    //3部门信息资源产生清单
    private void getCataInfo(Sheet sheet,ArchOrg depArchOrg, List<String> errorList,List<CataInfoTempDto> cataList) {
        Cell cell = null;
        Row row = null;
        CataInfoTempDto cataInfoTempDto = null;
        CataInfoTempTypeRel cataInfoTempTypeRel = null;
        CataInfoItemTempRel cataInfoItemTempRel = null;
        List<CataInfoItemTempRel> cateItemList = null;
        int rowNum = sheet.getLastRowNum();
        if (rowNum > 1000) {
            errorList.add("《2部门信息资源产生清单》页，异常原因：行数超过1000行。");
            return;
        } else if (rowNum < 2) {
            errorList.add("《2部门信息资源产生清单》页，异常原因：模板不正确，行数不够。");
        }
        //第4行开始
        String uviewNm = "";
        int typId = 0;
        for (int i = 3; i <= rowNum; i++) {
            row = sheet.getRow(i);
            if (row.getLastCellNum() < 20) {
                errorList.add("《2部门信息资源产生清单》页，第" + (i + 1) + "行，异常原因：模板不正确，列数不够。");
            }

            //信息资源名称
            cell = row.getCell(5);
            cell.setCellType(CellType.STRING);
            String assetName = cell.getStringCellValue().trim();
            if (StringUtils.isEmpty(assetName)) {
                errorList.add("《2部门信息资源产生清单》页，第" + (i + 1) + "行，异常原因：信息资源名称不能为空。");
            }

            //cataList中不存在
            List<CataInfoTempDto> cataListOlds = cataList.stream().filter(t -> t.getUviewNm() == assetName).collect(toList());
            if (cataListOlds.size() < 1) {

                cataInfoTempDto = new CataInfoTempDto();
                cataInfoTempDto.setUviewNm(assetName);

                //获取部门分类
                //部门类信息资源分类
                cell = row.getCell(1);
                cell.setCellType(CellType.STRING);
                String dep = cell.getStringCellValue().trim();
                DictAssetCate dictAssetCate = dictAssetCateService.getOne(
                        new QueryWrapper<DictAssetCate>().lambda()
                                .eq(DictAssetCate::getTypNm, dep).eq(DictAssetCate::getFullTypCd, '3'));
                if (dictAssetCate == null) {
                    errorList.add("《2部门信息资源产生清单》页，第" + (i + 1) + "行，异常原因：部门类【" + dep + "】未维护。");
                } else {
                    typId = this.getDepCate(row, i, 2, dep, dictAssetCate.getTypId(), errorList);
                }

                //下设机构
                cell = row.getCell(6);
                cell.setCellType(CellType.STRING);
                String orgNm = cell.getStringCellValue().trim();
                if (!StringUtils.isEmpty(orgNm)) {
                    List<String> orgNmList = Arrays.asList(orgNm.split(","));
                    List<ArchOrg> archOrgList = archOrgService.list(
                            new QueryWrapper<ArchOrg>().lambda()
                                    .in(ArchOrg::getOrgNm, orgNmList)
                                    .eq(ArchOrg::getParOrgId, depArchOrg.getOrgId()));
                    if (archOrgList == null || archOrgList.size() == 0) {
                        errorList.add("《2部门信息资源产生清单》页，第" + (i + 1) + "行，异常原因：内设部门名称【" + orgNm + "】未维护。");
                    } else {
                        List<String> tmpIdList = new ArrayList<>();
                        List<String> tmpCdList = new ArrayList<>();
                        archOrgList.forEach(item -> {
                            tmpIdList.add(item.getOrgId().toString());
                            tmpCdList.add(item.getOrgCd());
                        });
                        cataInfoTempDto.setBelongTo(String.join(",", tmpIdList));
                        cataInfoTempDto.setBelongToCode(String.join(",", tmpCdList));
                    }
                } else {
                    cataInfoTempDto.setBelongTo("");
                    cataInfoTempDto.setBelongToCode("");
                }

                //信息资源摘要
                cell = row.getCell(7);
                cell.setCellType(CellType.STRING);
                String uviewDesc = cell.getStringCellValue().trim();
                if (!StringUtils.isEmpty(uviewDesc)) {
                    cataInfoTempDto.setUviewDesc(uviewDesc);
                } else {
                    errorList.add("《2部门信息资源产生清单》页，第" + (i + 1) + "行，异常原因：信息资源摘要不能为空。");
                }

                //信息资源格式
                cell = row.getCell(8);
                //信息资源格式分类
                cell.setCellType(CellType.STRING);
                String mediaFmt = cell.getStringCellValue().trim();
                if (!StringUtils.isEmpty(mediaFmt)) {
                    SysDict mediaFmtBean = sysDictService.getOne(new QueryWrapper<SysDict>().lambda().eq(SysDict::getName, mediaFmt).eq(SysDict::getType, "format_type"));
                    if (null != mediaFmtBean) {
                        cataInfoTempDto.setMediaFmt(mediaFmtBean.getValue());
                        //信息资源格式类型
                        cell = row.getCell(9);
                        cell.setCellType(CellType.STRING);
                        String mediaFmtType = cell.getStringCellValue().trim();
                        if (!StringUtils.isEmpty(mediaFmtType)) {
                            SysDict mediaFmtTypeBean = sysDictService.getOne(new QueryWrapper<SysDict>().lambda().eq(SysDict::getPid, mediaFmtBean.getId()).eq(SysDict::getName, mediaFmtType));
                            if (null != mediaFmtTypeBean) {
                                cataInfoTempDto.setMediaFmtType(mediaFmtTypeBean.getValue());
                            } else {
                                errorList.add("《2部门信息资源产生清单》页，第" + (i + 1) + "行，异常原因：信息资源格式类型【" + mediaFmtType + "】未维护。");
                            }
                        } else {
                            errorList.add("《2部门信息资源产生清单》页，第" + (i + 1) + "行，异常原因：信息资源格式类型不能为空。");
                        }
                    } else {
                        errorList.add("《2部门信息资源产生清单》页，第" + (i + 1) + "行，异常原因：信息资源格式分类【" + mediaFmt + "】未维护。");
                    }
                } else {
                    errorList.add("《2部门信息资源产生清单》页，第" + (i + 1) + "行，异常原因：信息资源格式分类不能为空。");
                }

                //共享类型
                cell = row.getCell(15);
                cell.setCellType(CellType.STRING);
                String shareLv = cell.getStringCellValue().trim();
                if (!StringUtils.isEmpty(shareLv)) {
                    SysDict shareBean = sysDictService.getOne(new QueryWrapper<SysDict>().lambda().eq(SysDict::getName, shareLv).eq(SysDict::getType, "share_type"));
                    if (null != shareBean) {
                        cataInfoTempDto.setShareLv(shareBean.getValue());
                    } else {
                        errorList.add("《2部门信息资源产生清单》页，第" + (i + 1) + "行，异常原因：共享类型【" + shareLv + "】未维护。");
                    }
                } else {
                    errorList.add("《2部门信息资源产生清单》页，第" + (i + 1) + "行，异常原因：共享类型不能为空。");
                }
                //共享依据
                cell = row.getCell(16);
                cell.setCellType(CellType.STRING);
                String shareCondition = cell.getStringCellValue().trim();
                if (!StringUtils.isEmpty(shareCondition)) {
                    cataInfoTempDto.setShareCondition(shareCondition);
                } else {
                    errorList.add("《2部门信息资源产生清单》页，第" + (i + 1) + "行，异常原因：共享依据不能为空。");
                }

                //是否向社会开放
                cell = row.getCell(17);
                cell.setCellType(CellType.STRING);
                String pubSts = cell.getStringCellValue().trim();
                if (!StringUtils.isEmpty(pubSts)) {
                    SysDict pubStsBean = sysDictService.getOne(new QueryWrapper<SysDict>().lambda().eq(SysDict::getName, pubSts).eq(SysDict::getType, "is_open"));
                    if (null != pubStsBean) {
                        cataInfoTempDto.setPubSts(pubStsBean.getValue());
                    } else {
                        errorList.add("《2部门信息资源产生清单》页，第" + (i + 1) + "行，异常原因：是否向社会开放【" + pubSts + "】未维护。");
                    }
                } else {
                    errorList.add("《2部门信息资源产生清单》页，第" + (i + 1) + "行，异常原因：是否向社会开放不能为空。");
                }
                //开放条件
                cell = row.getCell(18);
                cell.setCellType(CellType.STRING);
                String pubCondition = cell.getStringCellValue().trim();
                if (!StringUtils.isEmpty(pubCondition)) {
                    cataInfoTempDto.setPubCondition(pubCondition);
                } else {
                    errorList.add("《2部门信息资源产生清单》页，第" + (i + 1) + "行，异常原因：开放条件不能为空。");
                }
                //更新周期
                cell = row.getCell(19);
                cell.setCellType(CellType.STRING);
                String updateCyc = cell.getStringCellValue().trim();
                if (!StringUtils.isEmpty(updateCyc)) {
                    SysDict updateCycBean = sysDictService.getOne(new QueryWrapper<SysDict>().lambda().eq(SysDict::getName, updateCyc).eq(SysDict::getType, "update_cycle"));
                    if (null != updateCycBean) {
                        cataInfoTempDto.setUpdateCyc(updateCycBean.getValue());
                    } else {
                        errorList.add("《2部门信息资源产生清单》页，第" + (i + 1) + "行，异常原因：更新周期【" + updateCyc + "】未维护。");
                    }
                } else {
                    errorList.add("《2部门信息资源产生清单》页，第" + (i + 1) + "行，异常原因：更新周期不能为空。");
                }
                //共享范围、是否涉密、数据范围、是否关联权责、权责清单补充说明
                cataInfoTempDto.setShareScope("全市");
                cataInfoTempDto.setSecret(0);
                cataInfoTempDto.setDataScope("区本级数据");
                cataInfoTempDto.setRelBusi(0);
                cataInfoTempDto.setRelBusiMsg("无");
                List<CataInfoTempTypeRel> cataInfoTempTypeRelList = new ArrayList<CataInfoTempTypeRel>();
                cataInfoTempTypeRel = new CataInfoTempTypeRel();
                cataInfoTempTypeRel.setType("3");
                cataInfoTempTypeRel.setTypeId(typId);
                cataInfoTempTypeRelList.add(cataInfoTempTypeRel);
                cataInfoTempDto.setCataInfoTempTypeRelList(cataInfoTempTypeRelList);
                cataInfoTempDto.setCataInfoTempItemRelList(new ArrayList<>());
                cataList.add(cataInfoTempDto);
            }

            //数据项
            cateItemList = new ArrayList<CataInfoItemTempRel>();

            cataInfoItemTempRel = new CataInfoItemTempRel();
            cataInfoItemTempRel.setCreateTime(new Date());
            //数据项名称
            cell = row.getCell(11);
            cell.setCellType(CellType.STRING);
            String cataItemName = cell.getStringCellValue().trim();
            if (!StringUtils.isEmpty(cataItemName)) {
                cataInfoItemTempRel.setSrcField(cataItemName);
            } else {
                errorList.add("《2部门信息资源产生清单》页，第" + (i + 1) + "行，异常原因：信息项名称不能为空。");
            }

            //英文名称
            cell = row.getCell(12);
            cell.setCellType(CellType.STRING);
            String engCd = cell.getStringCellValue().trim();
            if (!StringUtils.isEmpty(engCd)) {
                cataInfoItemTempRel.setEngCd(engCd);
            } else {
                errorList.add("《2部门信息资源产生清单》页，第" + (i + 1) + "行，异常原因：英文名称不能为空。");
            }

            //数据类型
            cell = row.getCell(13);
            cell.setCellType(CellType.STRING);
            String dataType = cell.getStringCellValue().trim();
            if (!StringUtils.isEmpty(dataType)) {
                SysDict dataTypeBean = sysDictService.getOne(
                        new QueryWrapper<SysDict>().lambda()
                                .eq(SysDict::getName, dataType).eq(SysDict::getType, "data_type"));
                if (null != dataTypeBean) {
                    cataInfoItemTempRel.setSrcDataTyp(dataTypeBean.getValue());
                } else {
                    errorList.add("《2部门信息资源产生清单》页，第" + (i + 1) + "行，异常原因：数据类型【" + dataType + "】未维护。");
                }
            } else {
                errorList.add("《2部门信息资源产生清单》页，第" + (i + 1) + "行，异常原因：数据类型不能为空。");
            }

            //数据长度
            cell = row.getCell(14);
            cell.setCellType(CellType.STRING);
            String dataLen = cell.getStringCellValue().trim();
            if (!StringUtils.isEmpty(dataLen)) {
                cataInfoItemTempRel.setDataLen(dataLen);
            } else {
                errorList.add("《2部门信息资源产生清单》页，第" + (i + 1) + "行，异常原因：数据长度不能为空。");
            }

            //cataList中数据
            List<CataInfoTempDto> cataListSecond = cataList.stream().filter(t -> t.getUviewNm() == assetName).collect(toList());
            cataListSecond.get(0).getCataInfoTempItemRelList().add(cataInfoItemTempRel);
        }
    }

    //4部门信息资源需求清单
    private void  getCataRequire(Sheet sheet,ArchOrg depArchOrg, List<String> errorList, List<CataRequire> cataRequireList){
        CataRequire cataRequire = null;
        Cell cell = null;
        Row row = null;
        ArchOrg archOrg = null;
        int rowNum = sheet.getLastRowNum();
        if(rowNum > 1000){
            errorList.add("《4部门信息资源需求清单》页，异常原因：行数超过1000行。");
            return ;
        }else if(rowNum<1){
            errorList.add("《4部门信息资源需求清单》页，异常原因：模板不正确，行数不够。");
        }else{
            //第3行开始
            for (int i = 2; i <= rowNum; i++){
                row = sheet.getRow(i);
                if(row.getLastCellNum() < 9){
                    errorList.add("《4部门信息资源需求清单》页，异常原因：模板不正确，列数不够。");
                }else{
                    cataRequire = new CataRequire();
                    cataRequire.setDeptId(depArchOrg.getOrgId());
                    cataRequire.setDeleted(0);
                    //所需政务信息资源名称
                    cell = row.getCell(1);
                    cell.setCellType(CellType.STRING);
                    String name = cell.getStringCellValue().trim();
                    if(StringUtils.isEmpty(name)){
                        errorList.add("《4部门信息资源需求清单》页，第"+(i+1)+"行，异常原因：所需政务信息资源名称不能为空。");
                    }else{
                        cataRequire.setName(name);
                    }
                    //政务信息资源提供方
                    cell = row.getCell(2);
                    cell.setCellType(CellType.STRING);
                    String depName = cell.getStringCellValue().trim();
                    if(StringUtils.isEmpty(depName)){
                        errorList.add("《4部门信息资源需求清单》页，第"+(i+1)+"行，异常原因：政务信息资源提供方不能为空。");
                    }else{
                        archOrg = archOrgService.getOne(new QueryWrapper<ArchOrg>().lambda()
                                .eq(ArchOrg::getOrgNm,depName).isNull(ArchOrg::getParOrgId));
                        if(null != archOrg){
                            cataRequire.setProvOrgId(archOrg.getOrgId());
                            cataRequire.setProvOrgCode(archOrg.getOrgCd());
                        }else{
                            cataRequire.setProvOrgCode(depName);
                        }
                    }
                    //政务信息资源描述
                    cell = row.getCell(3);
                    cell.setCellType(CellType.STRING);
                    String remark = cell.getStringCellValue().trim();
                    if(!StringUtils.isEmpty(remark)){
                        cataRequire.setRemark(remark);
                    }else{
                        cataRequire.setRemark("");
                    }
                    //是否已获取
                    cell = row.getCell(4);
                    cell.setCellType(CellType.STRING);
                    String isAccess = cell.getStringCellValue().trim();
                    if(!StringUtils.isEmpty(isAccess)){
                        cataRequire.setIsAccess("是".equals(isAccess) ? "0" : "1");
                    }else{
                        errorList.add("《4部门信息资源需求清单》页，第"+(i+1)+"行，异常原因：是否已获取不能为空。");
                    }
                    //建议获取方式
                    cell = row.getCell(5);
                    cell.setCellType(CellType.STRING);
                    String accessWay = cell.getStringCellValue().trim();
                    if(!StringUtils.isEmpty(accessWay)){
                        SysDict accessWayTypeBean = sysDictService.getOne(
                                new QueryWrapper<SysDict>().lambda()
                                        .eq(SysDict::getName,accessWay)
                                        .eq(SysDict::getType,"share_way")
                        );
                        if(null != accessWayTypeBean){
                            cataRequire.setAccessWay(accessWayTypeBean.getValue());
                        }else{
                            errorList.add("《4部门信息资源需求清单》页，第"+(i+1)+"行，异常原因：建议获取方式【"+accessWay+"】未维护。");
                        }
                    }else{
                        errorList.add("《4部门信息资源需求清单》页，第"+(i+1)+"行，异常原因：建议获取方式不能为空。");
                    }
                    //期望数据使用频率
                    cell = row.getCell(6);
                    cell.setCellType(CellType.STRING);
                    String useFrequency = cell.getStringCellValue().trim();
                    if(!StringUtils.isEmpty(useFrequency)){
                        SysDict useFrequencyBean = sysDictService.getOne(
                                new QueryWrapper<SysDict>().lambda()
                                        .eq(SysDict::getName,useFrequency)
                                        .eq(SysDict::getType,"update_cycle")
                        );
                        if(null != useFrequencyBean){
                            cataRequire.setUseFrequency(useFrequencyBean.getValue());
                        }else{
                            errorList.add("《4部门信息资源需求清单》页，第"+(i+1)+"行，异常原因：期望数据使用频率【"+useFrequency+"】未维护。");
                        }
                    }else{
                        errorList.add("《4部门信息资源需求清单》页，第"+(i+1)+"行，异常原因：期望数据使用频率不能为空。");
                    }
                    //用途
                    cell = row.getCell(7);
                    cell.setCellType(CellType.STRING);
                    String purpose = cell.getStringCellValue().trim();
                    if(!StringUtils.isEmpty(purpose)){
                        cataRequire.setPurpose(purpose);
                    }else{
                        cataRequire.setPurpose("");
                    }
                    //所需机构
                    cell = row.getCell(8);
                    cell.setCellType(CellType.STRING);
                    String orgNm = cell.getStringCellValue().trim();
                    if(!StringUtils.isEmpty(orgNm)){
                        archOrg = archOrgService.getOne(
                                new QueryWrapper<ArchOrg>().lambda()
                                        .eq(ArchOrg::getOrgNm,orgNm)
                                        .eq(ArchOrg::getParOrgId,depArchOrg.getOrgId()));
                        if(archOrg == null){
                            errorList.add("《4部门信息资源需求清单》页，第"+(i+1)+"行，异常原因：组织机构【"+orgNm+"】未维护。");
                        }else{
                            cataRequire.setOrgId(archOrg.getOrgId());
                        }
                    }
                    cataRequireList.add(cataRequire);
                }

            }
        }
    }
    //5部门信息系统清单
    private void getArchAppSys(Sheet sheet,ArchOrg depArchOrg, List<String> errorList, List<ArchAppSys> archAppSysList){
        Cell cell = null;
        Row row = null;
        ArchOrg archOrg = null;
        SysDict sysDict = null;
        ArchAppSys archAppSys = null;
        int rowNum = sheet.getLastRowNum();
        if(rowNum > 1000){
            errorList.add("《3部门信息系统清单》页，异常原因：行数超过1000行。");
            return ;
        }else if(rowNum<1){
            errorList.add("《3部门信息系统清单》页，异常原因：模板不正确，行数不够。");
        }else{
            //第3行开始
            for (int i = 2; i <= rowNum; i++) {
                row = sheet.getRow(i);
                if (row.getLastCellNum() < 10) {
                    errorList.add("《3部门信息系统清单》页，第"+(i+1)+"行异常原因：模板不正确，列数不够。");
                } else {
                    archAppSys = new ArchAppSys();
                    archAppSys.setProvOrgId(depArchOrg.getOrgId());
                    //下设机构名称
                    cell = row.getCell(1);
                    cell.setCellType(CellType.STRING);
                    String belongTo = cell.getStringCellValue().trim();
                    //有部门的情况才继续查它的下设机构
                    if(null != depArchOrg){
                        if(!StringUtils.isEmpty(belongTo)){
                            archOrg = archOrgService.getOne(
                                    new QueryWrapper<ArchOrg>().lambda()
                                            .eq(ArchOrg::getOrgNm,belongTo)
                                            .eq(ArchOrg::getParOrgId,depArchOrg.getOrgId()));
                            if(archOrg == null){
                                errorList.add("《3部门信息系统清单》页，第"+(i+1)+"行，异常原因：内设部门名称【"+belongTo+"】未维护。");
                            }else{
                                archAppSys.setBelongTo(archOrg.getOrgId());
                            }
                        }else{
//                            errorList.add("《3部门信息系统清单》页，第"+(i+1)+"行，异常原因：内设部门名称不能为空。");
                        }
                    }
                    //信息系统名称
                    cell = row.getCell(2);
                    cell.setCellType(CellType.STRING);
                    String appsysNm = cell.getStringCellValue().trim();
                    if(!StringUtils.isEmpty(appsysNm)){
                        archAppSys.setAppsysNm(appsysNm);
                    }else{
                        errorList.add("《3部门信息系统清单》页，第"+(i+1)+"行，异常原因：信息系统名称不能为空。");
                    }

                    //信息系统功能描述
                    cell = row.getCell(4);
                    cell.setCellType(CellType.STRING);
                    String appsysDesc = cell.getStringCellValue().trim();
                    if(!StringUtils.isEmpty(appsysDesc)){
                        archAppSys.setAppsysDesc(appsysDesc);
                    }else{
                        archAppSys.setAppsysDesc("");
                    }
                    //信息系统建设性质
                    cell = row.getCell(6);
                    cell.setCellType(CellType.STRING);
                    String nature = cell.getStringCellValue().trim();
                    if(!StringUtils.isEmpty(nature)){
                        sysDict = sysDictService.getOne(
                                new QueryWrapper<SysDict>().lambda()
                                        .eq(SysDict::getName,nature)
                                        .eq(SysDict::getType,"build_nature")
                        );
                        if(null != sysDict){
                            archAppSys.setNature(sysDict.getValue());
                        }else{
                            errorList.add("《3部门信息系统清单》页，第"+(i+1)+"行，异常原因：信息系统建设性质【"+nature+"】未维护。");
                        }
                    }else{
                        archAppSys.setNature("");
                    }

                    //信息系统部署位置
                    cell = row.getCell(7);
                    cell.setCellType(CellType.STRING);
                    String position = cell.getStringCellValue().trim();
                    if(!StringUtils.isEmpty(position)){
                        sysDict = sysDictService.getOne(
                                new QueryWrapper<SysDict>().lambda()
                                        .eq(SysDict::getName,position)
                                        .eq(SysDict::getType,"deploy_location")
                        );
                        if(null != sysDict){
                            archAppSys.setPosition(sysDict.getValue());
                        }else{
                            errorList.add("《3部门信息系统清单》页，第"+(i+1)+"行，异常原因：信息系统部署位置【"+position+"】未维护。");
                        }
                    }else{
                        archAppSys.setPosition("");
                    }

                    //信息系统接入网络类型
                    cell = row.getCell(8);
                    cell.setCellType(CellType.STRING);
                    String netType = cell.getStringCellValue().trim();
                    if(!StringUtils.isEmpty(netType)){
                        sysDict = sysDictService.getOne(
                                new QueryWrapper<SysDict>().lambda()
                                        .eq(SysDict::getName,netType)
                                        .eq(SysDict::getType,"network_type")
                        );
                        if(null != sysDict){
                            archAppSys.setNetType(sysDict.getValue());
                        }else{
                            errorList.add("《3部门信息系统清单》页，第"+(i+1)+"行，异常原因：信息系统接入网络类型【"+netType+"】未维护。");
                        }
                    }else{
                        archAppSys.setNetType("");
                    }

                    //备注
                    cell = row.getCell(9);
                    cell.setCellType(CellType.STRING);
                    String remark = cell.getStringCellValue().trim();
                    if(!StringUtils.isEmpty(remark)){
                        archAppSys.setRemark(remark);
                    }else{
                        archAppSys.setRemark("");
                    }
                    archAppSysList.add(archAppSys);
                }
            }
        }
    }

    //2部门业务清单
    private void getArchBusi(Sheet sheet,ArchOrg depArchOrg, List<String> errorList, List<ArchBusi> archBusiList){
        Cell cell = null;
        Row row = null;
        ArchBusi archBusi;
        List<ArchAppSys> appList = null;
        ArchAppSys archAppSys = null;
        int rowNum = sheet.getLastRowNum();
        if(rowNum > 1000){
            errorList.add("《2部门业务清单》页，异常原因：行数超过1000行。");
            return ;
        }else if(rowNum<2){
            errorList.add("《2部门业务清单》页，异常原因：模板不正确，行数不够。");
        }else {
            //第3行开始
            String belongToStr = "";
            int belongToInt = 0;
            for (int i = 2; i <= rowNum; i++) {
                row = sheet.getRow(i);
                if (row.getLastCellNum() < 10) {
                    errorList.add("《2部门业务清单》页，第"+(i+1)+"行，异常原因：模板不正确，列数不够。");
                } else {
                    //内设机构
                    cell = row.getCell(1);
                    cell.setCellType(CellType.STRING);
                    String belongTo = cell.getStringCellValue().trim();
                    if(StringUtils.isEmpty(belongTo)){
                        if(StringUtils.isEmpty(belongToStr)){
                            errorList.add("《2部门业务清单》页，第"+(i+1)+"行，异常原因：内设部门名称不能为空。");
                        }
                    }else{
                        belongToStr = belongTo;
                        ArchOrg archOrg = archOrgService.getOne(
                                new QueryWrapper<ArchOrg>().lambda()
                                        .eq(ArchOrg::getOrgNm,belongTo)
                                        .eq(ArchOrg::getParOrgId,depArchOrg.getOrgId()));
                        if(archOrg == null){
                            errorList.add("《2部门业务清单》页，第"+(i+1)+"行，异常原因：内设部门名称【"+belongTo+"】未维护。");
                            belongToInt = 0;
                        }else{
                            belongToInt = archOrg.getOrgId();
                        }

                    }

                    archBusi = new ArchBusi();
                    appList = new ArrayList<ArchAppSys>();
                    if(belongToInt > 0){
                        //添加内设机构
                        archBusi.setBelongTo(belongToInt);
                    }
                    //权责清单
                    cell = row.getCell(4);
                    cell.setCellType(CellType.STRING);
                    String busiName = cell.getStringCellValue().trim();
                    if(!StringUtils.isEmpty(busiName)){
                        archBusi.setBusiNm(busiName);
                    }else{
                        errorList.add("《2部门业务清单》页，第"+(i+1)+"行，异常原因：权责清单不能为空。");
                    }

                    //服务对象
                    cell = row.getCell(5);
                    cell.setCellType(CellType.STRING);
                    String serviceObj = cell.getStringCellValue().trim();
                    if(!StringUtils.isEmpty(serviceObj)){
                        SysDict serviceObjBean = sysDictService.getOne(
                                new QueryWrapper<SysDict>().lambda()
                                        .eq(SysDict::getName,serviceObj).eq(SysDict::getType,"service_obj"));
                        if(null != serviceObjBean){
                            archBusi.setServiceObj(serviceObjBean.getValue());
                        }else{
                            errorList.add("《2部门业务清单》页，第"+(i+1)+"行，异常原因：服务对象【"+serviceObj+"】未维护。");
                        }
                    }else{
                        archBusi.setServiceObj("");
                    }

                    //支撑的信息系统名称
                    cell = row.getCell(8);
                    cell.setCellType(CellType.STRING);
                    String appSysName = cell.getStringCellValue().trim();
                    if(!StringUtils.isEmpty(appSysName)){
                        String[] appNames=appSysName.trim().split("\n|\r");
                        for (String name : appNames) {
                            archAppSys = new ArchAppSys();
                            archAppSys.setAppsysNm(name.replaceAll("^[1-9][0-9]*[.|、]",""));
                            appList.add(archAppSys);
                        }
                        archBusi.setAppList(appList);
                    }

                    //备注
                    cell = row.getCell(9);
                    cell.setCellType(CellType.STRING);
                    String remark = cell.getStringCellValue().trim();
                    if(!StringUtils.isEmpty(remark)){
                        archBusi.setRemark(remark);
                    }else{
                        archBusi.setRemark("");
                    }

                    archBusiList.add(archBusi);

                }
            }
        }
    }

    //获取部门分类
    private int getDepCate(Row row,int rowNum,int colNum,String depName,int typId,List<String> errorList){
        Cell cell = row.getCell(colNum);
        cell.setCellType(CellType.STRING);
        String depCate = cell.getStringCellValue().trim();
        if(StringUtils.isEmpty(depCate)||colNum>=5){
            return typId;
        }else{
            DictAssetCate dictAssetCate = dictAssetCateService.getOne(new QueryWrapper<DictAssetCate>().lambda().eq(DictAssetCate::getParTypId,typId).eq(DictAssetCate::getTypNm,depCate));
            depName += "-"+depCate;
            if(dictAssetCate == null){
                errorList.add("《2部门信息资源产生清单》页，第"+(rowNum+1)+"行，异常原因：部门类【"+depName+"】未维护。");
                return typId;
            }else{
                return getDepCate(row,rowNum,++colNum,depName,dictAssetCate.getTypId(),errorList);
            }
        }

    }

    //获取应用系统编码，手动录的存在的排除掉
    private String getAppSysNo(String depCode){
        //防止死循环
        int i = 9999;
        String appSysNo="";
        while(i-->0 ){
            int seq=this.baseMapper.getSeqNext("arch_app_sys_code_seq");
            appSysNo = "P"+depCode+String.format("%04d",seq);
            ArchAppSys oldBean = archAppSysService.getOne(
                    new QueryWrapper<ArchAppSys>().lambda()
                            .eq(ArchAppSys::getAppsysNo,appSysNo));
            if(oldBean == null){
                break;
            }
        }
        return appSysNo;
    }
    @Override
    public int getSeqNext( String seqName){
        return this.baseMapper.getSeqNext(seqName);
    }
}
