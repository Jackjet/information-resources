package com.digitalchina.resourcecatalog.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.digitalchina.resourcecatalog.db.domain.ArchBusi;
import com.digitalchina.resourcecatalog.db.domain.DictAssetCate;
import com.digitalchina.resourcecatalog.db.service.ArchBusiService;
import com.digitalchina.resourcecatalog.db.service.DictAssetCateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: create by wangmh
 * @name: ImportTest.java
 * @description:
 * @date:2020/6/16
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DictAssetCateImport {
    @Autowired
    ArchBusiService archBusiService;

    @Autowired
    DictAssetCateService dictAssetCateService;

    /***
     * 总导入入口
     * @throws ParseException
     */
    @Test
    public void totalImport() throws ParseException {
        QueryWrapper<DictAssetCate> qwDict = new QueryWrapper<DictAssetCate>();
        qwDict.eq(DictAssetCate.TYP_TYPE, "目");
        qwDict.orderByAsc(DictAssetCate.DISPLAY_SN);
        List<DictAssetCate> dictList = dictAssetCateService.list(qwDict);
        List<DictAssetCate> saveList = new ArrayList<>();
        for (int i=0;i<dictList.size();i++){
            DictAssetCate dict = dictList.get(i);
//            QueryWrapper<ArchBusi> qwAb = new QueryWrapper<ArchBusi>();
//            qwAb.eq(ArchBusi.DEPT_ID, dict.getOrgId());
//            qwAb.isNull("p_id");
//            List<ArchBusi> abList = archBusiService.list(qwAb);
//            for (int j=0;j<abList.size();j++){
//                ArchBusi archBusi = abList.get(j);
                Map<String, Object> map = dictAssetCateService.generateCode(dict.getTypId());
                DictAssetCate dictAssetCate = new DictAssetCate();
                dictAssetCate.setCrtDt(LocalDateTime.now());
                dictAssetCate.setStatus("1");
                dictAssetCate.setTypNm(dict.getTypNm());
                dictAssetCate.setTypCd(map.get("code").toString());
                dictAssetCate.setDisplaySn(i+1);
                dictAssetCate.setFullTypCd(map.get("fullCode").toString());
                dictAssetCate.setTypType(map.get("typType").toString());
                dictAssetCate.setParTypId(dict.getTypId());
                dictAssetCate.setOrgId(dict.getOrgId());
                saveList.add(dictAssetCate);
//                dictAssetCateService.save(dictAssetCate);
//            }
        }
        System.out.println(saveList.toString());
        if(saveList.size()>0){
            dictAssetCateService.saveBatch(saveList);
        }
    }
}
