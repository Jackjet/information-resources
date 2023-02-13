package d1.project.resource.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-07 16:00
 */

public class BaseUtils {

    private static final Logger logger = LoggerFactory.getLogger("api");

    private static final String[] HAN_ARR = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    private static final String[] UNIT_ARR = {"十", "百", "千", "万", "十", "白", "千", "亿", "十", "百", "千"};

    public static String generate32Id() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }


    /**
     * 获取真实ip地址，避免获取代理ip
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = null;
        String ipAddresses = request.getHeader("X-Forwarded-For");
        String unknown = "unknown";
        if (ipAddresses == null || ipAddresses.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("X-Real-IP");
        }
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String toHanStr(Integer number) {
        String numStr = number + "";
        StringBuilder result = new StringBuilder();
        int numLen = numStr.length();

        for (int i = 0; i < numLen; i++) {
            int num = numStr.charAt(i) - 48;
            if (i != numLen - 1 && num != 0) {
                result.append(HAN_ARR[num]).append(UNIT_ARR[numLen - 2 - i]);
                if (number >= 10 && number < 20) {
                    result = new StringBuilder(result.substring(1));
                }
            } else {
                if (!(number >= 10 && number % 10 == 0)) {
                    result.append(HAN_ARR[num]);
                }
            }
        }

        return result.toString();

    }

    public static String timeFormat(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 导出文本内容
     *
     * @param content  文本内容
     * @param response 响应信息
     */
    public static void exportText(String content, HttpServletResponse response) {
        response.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
        response.setContentType("application/json");
        response.addHeader("Content-Disposition", "attachment;filename=testCase.json");
        BufferedOutputStream buff = null;
        ServletOutputStream outStr = null;
        try {
            outStr = response.getOutputStream();
            buff = new BufferedOutputStream(outStr);
            buff.write(content.getBytes(StandardCharsets.UTF_8));
            buff.flush();
            buff.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (buff != null) {
                    buff.close();
                }
                if (outStr != null) {
                    outStr.close();
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    /**
     * 获取用户信息
     *
     * @param request 请求头信息
     * @return 用户信息
     */
    public static JSONObject getUserInfo(HttpServletRequest request) {
        String URL = "";
        try {
            final Properties properties = new Properties();
            final String filePath = System.getProperty("user.dir") + "/config/application.properties";
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            properties.load(in);
            URL = properties.getProperty("d1.project.user.manage");
        }catch (Exception e){

        }
        String accessToken = getTokenByHttpServletRequest(request);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "token " + accessToken);
        ResponseEntity<JSONObject> res = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<String>(headers), JSONObject.class);
        return Objects.requireNonNull(res.getBody()).getJSONObject("data");
    }

    private static String getTokenByHttpServletRequest(HttpServletRequest request) {
        String authPrefix = "token";
        String auth = request.getHeader("Authorization");
        if (StringUtils.isEmpty(auth)) {
            return null;
        } else {
            String[] authArray = auth.split(" ");
            return authPrefix.equals(authArray[0]) && authArray.length > 1 ? authArray[1] : null;
        }
    }
}
