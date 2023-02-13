package com.digitalchina.resourcecatalog.db;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.digitalchina.resourcecatalog.db.dao.SysSystemMapper;
import com.digitalchina.resourcecatalog.db.domain.SysSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperReturnTest {

    @Autowired
    private SysSystemMapper systemMapper;

    @Test
    public void test() {
        SysSystem system = new SysSystem();
        system.setKeyName("test-system-key");
        system.setKeyValue("test-system-value");
        int updates = systemMapper.insert(system);
        Assert.assertEquals(updates, 1);

        updates = systemMapper.deleteById(system.getId());
        Assert.assertEquals(updates, 1);

        updates = systemMapper.updateById(system);
        Assert.assertEquals(updates, 0);
    }

}

