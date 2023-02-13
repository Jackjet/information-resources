package com.digitalchina.resourcecatalog.admin;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.digitalchina.resourcecatalog.core.util.bcrypt.BCryptPasswordEncoder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class BcryptTest {

    @Test
    public void test() {
        String rawPassword = "aaaaaa";
        String encodedPassword = "";
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        encodedPassword = bCryptPasswordEncoder.encode(rawPassword);

        System.out.println("rawPassword=" + rawPassword + " encodedPassword=" + encodedPassword);

        Assert.assertTrue(bCryptPasswordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    public void test1() {
        String regEx = "^[a-zA-Z]+\\w+$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher("bbbb111");
        System.out.println(m.find());
    }
}
