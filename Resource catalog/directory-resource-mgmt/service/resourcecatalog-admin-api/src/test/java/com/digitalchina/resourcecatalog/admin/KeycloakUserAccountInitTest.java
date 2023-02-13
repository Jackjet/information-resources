package com.digitalchina.resourcecatalog.admin;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.digitalchina.resourcecatalog.db.dao.SysSystemMapper;
import com.digitalchina.resourcecatalog.db.domain.*;
import com.digitalchina.resourcecatalog.db.service.impl.KeycloakSynchUserOneServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest
public class KeycloakUserAccountInitTest {
    private static final String FILE_PATH = "D:\\项目\\天津河北\\本区登录账号密码-汇总信息.xlsx";

    @Autowired
    private KeycloakSynchUserOneServiceImpl keycloakSynchUserOneService;

    /**
     * TODO 修改 KeycloakSynchUserOneServiceImpl
     * 73行
     * cr.setValue(user.getPassword());
     * @throws ParseException
     */
    @Test
    public void importSheel2() throws ParseException {
        ExcelReader reader;
        reader = ExcelUtil.getReader(FILE_PATH, 1);
        List<List<Object>> result = reader.read(2);
        int successCount = 0;
        for (List<Object> item : result) {
            //目录
            String account_bz = subCellStr(item.get(2));
            String pwd_bz = subCellStr(item.get(3));
            String account_sh = subCellStr(item.get(4));
            String pwd_sh = subCellStr(item.get(5));

            SysUser user_bz = new SysUser();
            user_bz.setUsername(account_bz);
            user_bz.setPassword(pwd_bz);
            user_bz.setDisabled(0);
            user_bz.setName(account_bz);
            System.out.println(user_bz);
            keycloakSynchUserOneService.synchUser("add", user_bz);
            successCount++;

            SysUser user_sh = new SysUser();
            user_sh.setUsername(account_sh);
            user_sh.setPassword(pwd_sh);
            user_sh.setDisabled(0);
            user_sh.setName(account_sh);
            System.out.println(user_sh);
            keycloakSynchUserOneService.synchUser("add", user_sh);

            successCount++;
        }
        System.out.println(successCount);
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

}

