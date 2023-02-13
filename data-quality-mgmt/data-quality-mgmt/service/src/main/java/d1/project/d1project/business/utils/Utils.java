package d1.project.d1project.business.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.TokenManager;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: maorui
 * @date: 2020/9/27
 */
public class Utils {
    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }

    public static String getCurrentUserId(HttpServletRequest request) throws DoValidException {
        JSONObject userToken = TokenManager.getInstance().getUserByToken(request);
        return userToken.getString("id");
    }

    public static String getCurrentUserName(HttpServletRequest request) throws DoValidException {
        JSONObject userToken = TokenManager.getInstance().getUserByToken(request);
        return userToken.getString("name");
    }

    public static Date stringToDate(String dateStr) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        return simpleDateFormat.parse(dateStr);
    }

    public static String dataToString(Date date, String format) throws ParseException {
        if (StringUtils.isEmpty(format)) {
            format = "yyyy-MM-ddHH:mm:ss";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date.getTime());
    }


//    public static boolean verfiyHmacSign(String auth, String appId, String timestamp, String appKey) throws Exception {
//        return ServiceFactory.getHmacSignService().verifySign(appId, timestamp, appKey, auth.split(" ")[1]);
//    }

    public static String getAppKey(String appId) throws IOException {
        File hmacFile = new File("./config/hmac.json");
        if (!hmacFile.exists()) {
            return null;
        }

        JSONArray array = JSONArray.parseArray(IoUtils.readFile(hmacFile));
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String appIdStr = obj.getString("appId");
            if (appId.equals(appIdStr)) {
                return obj.getString("appKey");
            }
        }
        return null;
    }

    public static String formatTime(long timeMs) {
        long second = 1000;
        long minute = second * 60;
        long hour = minute * 60;
        long day = hour * 24;
        long week = 7 * day;

        StringBuilder mFormatBuilder = new StringBuilder();
        if (timeMs > week) {
            long weeks = timeMs / week;
            mFormatBuilder.append(weeks).append("周");
            timeMs = timeMs % week;
        }

        if (timeMs > day) {
            long days = timeMs / day;
            mFormatBuilder.append(days).append("天");
            timeMs = timeMs % day;
        }

        if (timeMs > hour) {
            long hours = timeMs / hour;
            mFormatBuilder.append(hours).append("时");
            timeMs = timeMs % hour;
        }

        if (timeMs > minute) {
            long minutes = timeMs / minute;
            mFormatBuilder.append(minutes).append("分");
            timeMs = timeMs % minute;
        }

        long seconds = 0;
        if (timeMs > second) {
            seconds = timeMs / second;
            timeMs = timeMs % second;
        }

        if (timeMs >= 500) {
            seconds++;
        }

        mFormatBuilder.append(seconds + "秒");

        return mFormatBuilder.toString();
    }
}
