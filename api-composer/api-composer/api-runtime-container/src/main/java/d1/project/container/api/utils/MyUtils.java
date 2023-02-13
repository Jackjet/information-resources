package d1.project.container.api.utils;

import com.alibaba.fastjson.JSONObject;
import d1.project.container.api.base.utils.Utils;
import d1.project.container.api.factory.ServiceFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Administrator
 */
public class MyUtils {
    private static final Logger logger = LoggerFactory.getLogger(MyUtils.class);

    public static String getCurrentFormatTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }

    public static String getCurrentFormatDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    /**
     * 获取随机值
     *
     * @return 32位随机值
     */
    public static String getUUID32() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    public static RequestMethod parseMethod(String method) throws Exception {
        //GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE
        switch (method.toLowerCase()) {
            case "get":
                return RequestMethod.GET;
            case "head":
                return RequestMethod.HEAD;
            case "post":
                return RequestMethod.POST;
            case "put":
                return RequestMethod.PUT;
            case "patch":
                return RequestMethod.PATCH;
            case "delete":
                return RequestMethod.DELETE;
            case "options":
                return RequestMethod.OPTIONS;
            case "trace":
                return RequestMethod.TRACE;
            default:
                throw new Exception("不支持的请求类型“" + method + "”");
        }
    }

    public static String[] jsonToArray(List<Object> array) {
        if (array == null) {
            return new String[0];
        }
        String[] strings = new String[array.size()];
        for (int i = 0; i < array.size(); i++) {
            strings[i] = array.get(i).toString();
        }
        return strings;
    }


    /**
     * js对象转化成 jsonObject对象
     *
     * @param obj js对象
     * @return
     */
    public static JSONObject convert(Object obj) {

        if (obj == null || !(obj instanceof ScriptObjectMirror)) {
            return null;
        }

        JSONObject jsonObject = new JSONObject();
        ScriptObjectMirror object = (ScriptObjectMirror) obj;

        Set<Map.Entry<String, Object>> entrySet = object.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            String key = entry.getKey();
            Object value = entry.getValue();

            jsonObject.put(key, value);
        }

        return jsonObject;
    }

    /**
     * js对象转化成 数组对象
     *
     * @param obj js对象
     * @return
     */
    public static List<String> convertToList(Object obj) {

        if (obj == null || !(obj instanceof ScriptObjectMirror)) {
            return null;
        }

        List<String> list = new ArrayList<>();
        ScriptObjectMirror object = (ScriptObjectMirror) obj;

        Set<Map.Entry<String, Object>> entrySet = object.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            list.add(entry.getValue().toString());
        }

        return list;
    }

    /**
     * 回调js 方法
     *
     * @param jsFunction js function
     */
    public static void callJsFunc(Object jsFunction, Object... arg) {
        try {
            if (jsFunction == null || !(jsFunction instanceof ScriptObjectMirror)) {
                return;
            }

            ScriptObjectMirror scriptObjectMirror = (ScriptObjectMirror) jsFunction;
            if (!scriptObjectMirror.isFunction()) {
                return;
            }
            scriptObjectMirror.call(scriptObjectMirror, arg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getSign(String appKey, String appId, String timestamp) throws Exception {
        return ServiceFactory.getHmacSignService().sign(appId, timestamp, appKey);
    }

    public static boolean verifySign(String appKey, String appId, String timestamp, String sign) throws Exception {
        return ServiceFactory.getHmacSignService().verifySign(appId, timestamp, appKey, sign);
    }


    /**
     * 获取当前java应用程序的pid
     *
     * @return
     */
    public static String getPid() {
        return ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    }

    public static void deleteTempFile() {
        try {

            File[] files = new File(".").listFiles();
            for (File f : files) {
                if (Pattern.matches(".{32}-tcp[0-9]+", f.getName())) {
                    FileUtils.deleteDirectory(f);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>(4);
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    public static Map<String, String> getParams(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>(4);
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = request.getParameter(key);
            map.put(key, value);
        }
        return map;
    }

    public static Map<String, String> getBody(HttpServletRequest request) throws IOException {
        Map<String, String> map = new HashMap<>(4);
        BufferedReader br = request.getReader();

        StringBuffer sb = new StringBuffer();
        String str;
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }

        if (!Utils.isEmpty(sb.toString())) {
            return new HashMap(JSONObject.parseObject(sb.toString()));
        }
        return map;
    }

    public static String upperFirstCase(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }
}

