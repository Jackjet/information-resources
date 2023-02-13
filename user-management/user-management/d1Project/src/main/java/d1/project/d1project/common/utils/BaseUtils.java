package d1.project.d1project.common.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-07 16:00
 */
public class BaseUtils {

    private static final String[] HAN_ARR = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    private static final String[] UNIT_ARR = {"十", "百", "千", "万", "十", "白", "千", "亿", "十", "百", "千"};
    private static Map<String, ScheduledFuture<?>> timedMap = new HashMap<>(1);

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

    public static Map<String, ScheduledFuture<?>> getTimedMap() {
        return timedMap;
    }

    public static void setTimedMap(Map<String, ScheduledFuture<?>> timedMap) {
        BaseUtils.timedMap = timedMap;
    }
}
