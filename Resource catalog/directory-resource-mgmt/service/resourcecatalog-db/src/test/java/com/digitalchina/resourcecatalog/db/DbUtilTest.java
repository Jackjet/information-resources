package com.digitalchina.resourcecatalog.db;

import org.junit.Test;
import com.digitalchina.resourcecatalog.db.util.DbUtil;

import java.io.File;

public class DbUtilTest {
    @Test
    public void testBackup() {
        File file = new File("test.sql");
        DbUtil.backup(file, "Resourcecatalog", "Resourcecatalog123456", "Resourcecatalog");
    }

//    这个测试用例会重置Resourcecatalog数据库，所以比较危险，请开发者注意
//    @Test
    public void testLoad() {
        File file = new File("test.sql");
        DbUtil.load(file, "Resourcecatalog", "Resourcecatalog123456", "Resourcecatalog");
    }
}
