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
public class ResourceCatalogBusiImport {
    private static final String FILE_PATH = "D:\\项目\\天津河北\\资源目录\\河北区目录系统目录权责关联情况-init.xlsx";
    private static final String CREATEDBY = "System";

    @Autowired
    ArchOrgService archOrgService;

    @Autowired
    CataInfoTempService cataInfoTempService;

    @Autowired
    ArchBusiService archBusiService;

    @Autowired
    private CataBusInfoRelService cataBusInfoRelService;

    private List<Exception> info = new ArrayList();
    //所有顶级部门  key-部门名称 value-顶级部门
    Map<String, ArchOrg> orgMap = new HashMap<>();

    //所有权责
    Map<Integer, Map<String, ArchBusi>> busiMap = new HashMap<>();

    //所有目录
    Map<String, CataInfoTemp> cataMap = new HashMap<>();

    List<ArchBusi> repeatList = new ArrayList<>();
    /***
     * 总导入入口
     *
     * 目录与权责关联导入完成后需要执行该语句进行设置
     * update  cata_info_temp set rel_busi = 1 where uview_id in ( select  distinct info_id from cata_bus_info_rel )
     *
     *
     * @throws ParseException
     *
     */
    @Test
    public void totalImport() throws ParseException {
        //部门
        //顶级部门数据first level
        QueryWrapper<ArchOrg> qwOrgFirst = new QueryWrapper<ArchOrg>();
        qwOrgFirst.lambda().select(ArchOrg::getOrgId, ArchOrg::getOrgNm).isNull(ArchOrg::getParOrgId);
        List<ArchOrg> orgList = archOrgService.list(qwOrgFirst);
        orgList.forEach(org->orgMap.put(org.getOrgNm(), org));

        QueryWrapper<ArchBusi> qwBusFirst = new QueryWrapper<ArchBusi>();
        qwBusFirst.lambda().select(ArchBusi::getBusiId, ArchBusi::getBusiNm, ArchBusi::getDeptId).apply(" length(busi_no) >10");
        List<ArchBusi> busiList = archBusiService.list(qwBusFirst);
        busiList.forEach(busi-> {
            if(!busiMap.containsKey(busi.getDeptId())){
                busiMap.put(busi.getDeptId(), new HashMap<String, ArchBusi>());
            }
            Map<String, ArchBusi> deptMap = busiMap.get(busi.getDeptId());
            if(deptMap.containsKey(busi.getBusiNm())){
                repeatList.add(busi);
            }
            deptMap.put(busi.getBusiNm().replace("\r", "").replace("\n", ""), busi);
        });

        //目录
        QueryWrapper<CataInfoTemp> qwCataFirst = new QueryWrapper<CataInfoTemp>();
        qwCataFirst.lambda().select(CataInfoTemp::getUviewNo, CataInfoTemp::getUviewId, CataInfoTemp::getUviewNm, CataInfoTemp::getProvOrgId);
        List<CataInfoTemp> cataList = cataInfoTempService.list(qwCataFirst);
        cataList.forEach(cata->cataMap.put(cata.getCityCataCode(), cata));
        //导入公安局-验收交付版
        importSheel2();
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

        List<CataBusInfoRel> cataBusInfoRelList = new ArrayList<>(result.size());

        for (List<Object> item : result) {
            try {
                if(item.size()<5){
                    current++;
                    continue;
                }
                //
                String deptName = subCellStr(item.get(0));
                String uview_nm = subCellStr(item.get(1));
                String uview_cd = subCellStr(item.get(2));
                String busi_nm_str = subCellStr(item.get(4)).trim();
                busi_nm_str = busi_nm_str.replace("\r", "").replace("\n", "");
                if(StringUtils.isEmpty(busi_nm_str)){
                    current++;
                    continue;
                }
                if(!orgMap.containsKey(deptName)){
                    sb.append("importSheel2---序号：" + current + "，出现错误！！，异常" + deptName + "未找到 \n ");
                    current++;
                    continue;
                }
                ArchOrg org = orgMap.get(deptName);

                if(!busiMap.containsKey(org.getOrgId())){
                    sb.append("importSheel2---序号：" + current + "，出现错误！！，异常部门ID" + org.getOrgId() + "未找到 \n ");
                    current++;
                    continue;
                }
                Map<String, ArchBusi> deptMap = busiMap.get(org.getOrgId());
                if(deptMap.containsKey(busi_nm_str)){
                    if(cataMap.containsKey(uview_cd)){
                        CataBusInfoRel rel = new CataBusInfoRel();
                        rel.setType(0);
                        rel.setBusId(deptMap.get(busi_nm_str).getBusiId());
                        rel.setInfoId(cataMap.get(uview_cd).getUviewId());
                        cataBusInfoRelList.add(rel);
                        successCount++;
                    }else{
                        sb.append("importSheel2---序号：" + current + "，出现错误！！，异常目录" + uview_cd + "未找到 \n ");
                    }
                }else{
                    String[] busiNmArr = busi_nm_str.split(",");
                    for(int j=0; j < busiNmArr.length;j++){
                        String tmpStr = busiNmArr[j].replace("\n","");
                        if(deptMap.containsKey(tmpStr)){
                            if(cataMap.containsKey(uview_cd)){
                                CataBusInfoRel rel = new CataBusInfoRel();
                                rel.setType(0);
                                rel.setBusId(deptMap.get(tmpStr).getBusiId());
                                rel.setInfoId(cataMap.get(uview_cd).getUviewId());
                                cataBusInfoRelList.add(rel);
                                successCount++;
                            }else{
                                sb.append("importSheel2---序号：" + current + "，出现错误！！，异常目录" + uview_cd + "未找到 \n ");
                            }
                        }else{
                            String tmpBusNm = tmpStr;
                            boolean find = false;
                            while(j < busiNmArr.length-1){
                                tmpBusNm = tmpBusNm + "," +  busiNmArr[j+1].replace("\n","");
                                if(deptMap.containsKey(tmpBusNm)){
                                    if(cataMap.containsKey(uview_cd)){
                                        CataBusInfoRel rel = new CataBusInfoRel();
                                        rel.setType(0);
                                        rel.setBusId(deptMap.get(tmpBusNm).getBusiId());
                                        rel.setInfoId(cataMap.get(uview_cd).getUviewId());
                                        cataBusInfoRelList.add(rel);
                                        successCount++;
                                        find = true;
                                        j++;
                                        break;
                                    }else{
                                        sb.append("importSheel2---序号：" + current + "，出现错误！！，异常目录" + uview_cd + "未找到 \n ");
                                    }
                                }
                                j++;
                            }
                            if(!find){
                                System.out.println("------" + tmpBusNm);
                                sb.append("importSheel2---序号：" + current + "，出现错误！！，异常权责" + busiNmArr[j] + "未找到 \n ");
                            }
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                errorCount++;
                sb.append("importSheel2---序号：" + current + "，出现错误！！，异常" + e.getMessage());
                sb.append("\r\n");
                info.add(e);
            }
            current++;
        }
        //重复权责
        if(repeatList.size()>0){
            repeatList.forEach(i->System.out.println(i));
        }
        //
        if(cataBusInfoRelList.size()>0){
            cataBusInfoRelService.saveBatch(cataBusInfoRelList, 1000);
        }
        //打印错误信息
        System.out.println("importSheel2---成功导入：" + successCount + "条，失败导入：" + errorCount + "条");
        System.out.println("importSheel2---错误原因：" + sb.toString());
        info.forEach(e -> {
            e.printStackTrace();
            System.out.println("================");
        });
    }
    private String subCellStr(Object object) {
        return subCellStr(object, 10000);
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
}
