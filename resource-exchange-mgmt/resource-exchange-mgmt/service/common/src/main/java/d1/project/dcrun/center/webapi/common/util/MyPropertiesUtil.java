package d1.project.dcrun.center.webapi.common.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author xuaa
 */
public class MyPropertiesUtil {


    /**
     * 通过类装载器 初始化Properties
     */
    public static Properties init(String path) throws Exception {

        Properties p = new Properties();
        //转换成流
        InputStream inputStream = new BufferedInputStream(new FileInputStream(path));
        //从输入流中读取属性列表（键和元素对）
        p.load(inputStream);
        return p;
    }

    /**
     * 通过key获取value
     *
     * @param key key
     * @return valu
     */
    public static String get(String key, Properties p) {
        return p.getProperty(key);
    }

    /**
     * 修改或者新增key
     *
     * @param key   key
     * @param value value
     */
    public static void update(String key, String value, String path, Properties p) throws Exception {
        p.setProperty(key, value);
        FileOutputStream oFile = new FileOutputStream(path);
        //将Properties中的属性列表（键和元素对）写入输出流
        p.store(oFile, "");
        oFile.close();
    }

    /**
     * 通过key删除value
     *
     * @param key key
     */
    public static void delete(String key, String path, Properties p) throws Exception {
        p.remove(key);
        FileOutputStream oFile = new FileOutputStream(path);
        p.store(oFile, "");
        oFile.close();
    }
}
