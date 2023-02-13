package d1.project.resource.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.aspectj.util.FileUtil;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

public class HmacUtils {

    public static String getAppKeyById(String appId) throws Exception {
        File hmacFile = getResFile("hmac.json");
        if (hmacFile != null && hmacFile.exists()) {
            String content = new String(FileUtil.readAsByteArray(hmacFile), StandardCharsets.UTF_8);
            JSONArray array = JSONArray.parseArray(content);
            String appkey = getObjectByKey(array, appId);
            if (appkey == null) {
                throw new Exception("没找到对应的appid:" + appId);
            } else {
                return appkey;
            }
        } else {
            throw new Exception("没找到对应的hmac配置文件");
        }
    }


    private static String getObjectByKey(JSONArray array, String appid) {
        for (int i = 0; i < array.size(); ++i) {
            JSONObject obj = array.getJSONObject(i);
            if (obj.getString("appid").equals(appid)) {
                return obj.getString("appkey");
            }
        }

        return null;
    }

    private static File getResFile(String filename) throws FileNotFoundException {
        File file = new File(filename);
        if (!file.exists()) {
            file = new File("config/" + filename);
        }

        Resource resource = new FileSystemResource(file);
        if (!resource.exists()) {
            file = ResourceUtils.getFile("classpath:" + filename);
        }

        return file;
    }


}
