package d1.project.dcrun.center.webapi.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dcrun.component.security.HmacSignService;
import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.*;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author chengh
 **/
public class MyUtils {

    /**
     * 用来生成主键id
     *
     * @return 唯一标识符
     */
    public static String generatePrimaryKeyId() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    /**
     * 初始化分页信息
     *
     * @param page 页数
     * @param size 每页大小
     * @param sort 排序
     * @return
     */
    public static Pageable initPageable(Integer page, Integer size, Sort sort) {
        Pageable pageable;
        page = page != null && page >= 1 ? page - 1 : 0;
        size = size != null && size >= 1 ? size : 10;
        if (sort != null) {
            pageable = PageRequest.of(page, size, sort);
        } else {
            pageable = PageRequest.of(page, size);
        }
        return pageable;
    }

    /**
     * 分页
     */
    public static <T> Page<T> pagination(List<T> findAll, JSONObject params) {
        Pageable pageable = MyUtils.initPageable(params.getInteger("page"), params.getInteger("size"), null);
        try {
            // 当前页第一条数据在List中的位置
            int start = (int) pageable.getOffset();
            // 当前页最后一条数据在List中的位置
            int end = (start + pageable.getPageSize()) > findAll.size() ? findAll.size() : (start + pageable.getPageSize());
            // 配置分页数据
            return new PageImpl<>(findAll.subList(start, end), pageable, findAll.size());
        } catch (Exception ex) {
            return new PageImpl<>(findAll.subList(0, 0), pageable, findAll.size());
        }

    }

    public static Map<String, Object> getHeader() {
        String currentTime = String.valueOf(System.currentTimeMillis());
        HmacSignService signService = new HmacSignService();
        Map<String, Object> map = new HashMap<>(3);
        try {
            String sighStr = signService.sign(Constants.VERIFY_SIGN_APPID, currentTime, Constants.VERIFY_SIGN_APPKEY);
            map.put("Authorization", "sign " + sighStr);
            map.put("appid", Constants.VERIFY_SIGN_APPID);
            map.put("timestamp", currentTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static void replaceForTemplate(File file, JSONObject params) throws Exception {
        String sourceText = FileUtils.readFileToString(file);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            sourceText = sourceText.replace(entry.getKey(), (String) entry.getValue());
        }
        FileUtils.write(file, sourceText, StandardCharsets.UTF_8);
    }

    public static String eliminateSpaces(String source) {
        if (StringUtils.isEmpty(source)) {
            return source;
        }
        return source.replace(" ", "");
    }

    public static int getLocalLastVersion(String sourceCodePath) throws Exception {
        File infoFile = new File(sourceCodePath, "info.json");
        if (!infoFile.exists()) {
            return -1;
        }
        String content = FileUtils.readFileToString(infoFile, StandardCharsets.UTF_8);
        JSONObject infoJson = JSON.parseObject(content);
        return infoJson.getIntValue("version");
    }

    public static String getZipNameFromInfoFile(String sourceCodePath) throws Exception {
        File infoFile = new File(sourceCodePath, "info.json");
        if (!infoFile.exists()) {
            return null;
        }
        String content = FileUtils.readFileToString(infoFile, StandardCharsets.UTF_8);
        JSONObject infoJson = JSON.parseObject(content);
        return infoJson.getString("fileName");
    }

    /**
     * 从本地版本号文件中获取版本号，并比较与远程版本号是否相等
     *
     * @param infoPath      本地版本号文件路径
     * @param remoteVersion 远程版本号
     * @return
     */
    public static boolean isVersionEquals(String infoPath, String remoteVersion) throws Exception {
        File infoFile = new File(infoPath, "info.json");
        if (!infoFile.exists()) {
            return false;
        }
        JSONObject infoJson = JSON.parseObject(FileUtils.readFileToString(infoFile, StandardCharsets.UTF_8));
        return infoJson.getString("version").equals(remoteVersion);
    }

    /**
     * 从开发者中心下载系统服务安装包及源码
     *
     * @param sourceUrl 下载ip
     * @param url       文件相对路径
     * @return
     */
    public static String getDownloadUrl(String sourceUrl, String url) {
        return sourceUrl + "service/file/" + url;
    }

    public static String getJsPath(String developerId, String componentName, String version) {
        return getJSAndJsonPathPrefix(developerId, componentName, version) + ".js";
    }

    public static String getJsonPath(String developerId, String componentName, String version) {
        return getJSAndJsonPathPrefix(developerId, componentName, version) + ".json";
    }

    private static String getJSAndJsonPathPrefix(String developerId, String componentName, String version) {
        String prefix = Constants.FILES_BASIC_WEBAPI_COMPONENTS + developerId + File.separator + componentName + File.separator + version + File.separator;
        if ("net".equals(developerId)) {
            developerId = "dc";
        }
        return prefix + developerId + "_" + componentName;
    }


    /**
     * 判断节点在线离线
     *
     * @return true 在线 、false 离线
     */
    public static boolean isOnline(long reportTime) {
        long time = System.currentTimeMillis() - reportTime;
        return (time / (60 * 1000)) < 5;
    }

}
