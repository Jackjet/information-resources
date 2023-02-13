package d1.project.resourcesharingmgmt.common.utils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.function.Function;
import java.util.function.Predicate;

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

    /**
     * 去重
     *
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public static String getMD5String(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            //一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 计算百分比
     *
     * @param x
     * @param y
     * @return
     */
    public static String getPercent(long x, long y) {
        double d1 = x * 1.0;
        double d2 = y * 1.0;
        NumberFormat percentInstance = NumberFormat.getPercentInstance();
        // 设置保留几位小数，这里设置的是保留两位小数
        percentInstance.setMinimumFractionDigits(2);
        return percentInstance.format(d1 / d2);
    }

    public static float getPercentFloat(long x, long y) {
        if(x == 0L){
            return 0;
        }
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(0);
        return (float) x / y;
    }
}
