package d1.project.tangshan.operation.manage.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import d1.framework.webapi.entity.BaseCreateEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.*;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Buter
 * @date 2020/3/20 15:16
 */
public class MyUtils {

    public static String generate32Id() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    /**
     * 分页
     */
    public static <T> Page<T> pagination(List<T> findAll, JSONObject params) {
        Pageable pageable = getPageable(params.getInteger("page"), params.getInteger("size"), null);
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

    public static Pageable getPageable(Integer page, Integer size, Sort sort) {
        page = page != null && page >= 1 ? page - 1 : 0;
        if (size == null || size < 1) {
            size = 10;
        }
        return sort != null ? PageRequest.of(page, size, sort) : PageRequest.of(page, size);
    }

    public static Calendar getCalendar(Long aLong) {
        Calendar cal2 = Calendar.getInstance();
        Date date = new Date(aLong);
        cal2.setTime(date);
        return cal2;
    }

    public static Calendar getCalendar(String date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate = sdf.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(newDate);
        return calendar;
    }

    public static <T> T initInsert(T t, String userId) {
        if (t instanceof BaseCreateEntity) {
            ((BaseCreateEntity) t).setId(generate32Id());
            ((BaseCreateEntity) t).setCreateTime(Calendar.getInstance());
            ((BaseCreateEntity) t).setCreateById(userId);
        }
        if (t instanceof BaseCreateAndUpdateEntity) {
            ((BaseCreateAndUpdateEntity) t).setId(generate32Id());
            ((BaseCreateAndUpdateEntity) t).setCreateById(userId);
            ((BaseCreateAndUpdateEntity) t).setCreateTime(Calendar.getInstance());
            ((BaseCreateAndUpdateEntity) t).setUpdateById("");
            ((BaseCreateAndUpdateEntity) t).setUpdateTime(null);
        }
        return t;
    }

    public static <T> T initUpdate(T t, String userId) {
        if (t instanceof BaseCreateAndUpdateEntity) {
            ((BaseCreateAndUpdateEntity) t).setUpdateById(userId);
            ((BaseCreateAndUpdateEntity) t).setUpdateTime(Calendar.getInstance());
        }
        return t;
    }

    public static <S, T> Page<S> listEntity2Model(Page<T> lists, Class<S> cls) {
        String json = JSON.toJSON(lists.getContent()).toString();
        List<S> s = JSON.parseArray(json, cls);
        return new PageImpl<>(s, lists.getPageable(), lists.getTotalElements());
    }

    public static <S, T> List<S> listEntity2Model(List<T> lists, Class<S> cls) {
        String json = JSON.toJSON(lists).toString();
        return JSON.parseArray(json, cls);
    }

    public static <T> T model2Entity(Object object, Class<T> entityClass) {
        String jsonObject = JSON.toJSON(object).toString();
        return JSON.parseObject(jsonObject, entityClass);
    }

    /**
     * 复制属性
     *
     * @param source 来源
     * @param target 目标
     * @return T
     * @author Kikki  2020/3/28 21:18
     */
    public static <T> T copyProperties(T source, T target) {
        String[] nullProperties = getNullProperties(source);
        BeanUtils.copyProperties(source, target, nullProperties);
        return target;
    }

    /**
     * 获取对象的空属性
     */
    public static String[] getNullProperties(Object src) {
        // 1.获取Bean
        BeanWrapper srcBean = new BeanWrapperImpl(src);
        // 2.获取Bean的属性描述
        PropertyDescriptor[] pds = srcBean.getPropertyDescriptors();
        // 3.获取Bean的空属性
        Set<String> properties = new HashSet<>();
        for (PropertyDescriptor propertyDescriptor : pds) {
            String propertyName = propertyDescriptor.getName();
            Object propertyValue = srcBean.getPropertyValue(propertyName);
//            if (StringUtils.isEmpty(propertyValue)) {
            if (propertyValue == null) {
                srcBean.setPropertyValue(propertyName, null);
                properties.add(propertyName);
            }
        }
        return properties.toArray(new String[0]);
    }

    public static void throwMsg(boolean b, String msg) throws Exception {
        if (b) {
            throw new ValidException(msg);
        }
    }

    public static void jsonTimePageableFormat(JSONObject model) {
        String startTime = model.getString("startTime");
        String endTime = model.getString("endTime");

        if (!StringUtils.isEmpty(startTime)) {
            startTime = startTime + " 00:00:00";
            model.put("startTime", startTime);
        }

        if (!StringUtils.isEmpty(endTime)) {
            endTime = endTime + " 23:59:59";
            model.put("endTime", endTime);
        }
    }

    /**
     * 将元为单位的转换为分 替换小数点，支持以逗号区分的金额
     *
     * @param amount
     * @return
     */
    public static String changeBranch(String amount) {
        String currency = amount.replaceAll("\\$|\\￥|\\,", ""); // 处理包含, ￥
        // 或者$的金额
        int index = currency.indexOf(".");
        int length = currency.length();
        Long amLong;
        if (index == -1) {
            amLong = Long.valueOf(currency + "00");
        } else if (length - index >= 3) {
            amLong = Long.valueOf((currency.substring(0, index + 3)).replace(".", ""));
        } else if (length - index == 2) {
            amLong = Long.valueOf((currency.substring(0, index + 2)).replace(".", "") + 0);
        } else {
            amLong = Long.valueOf((currency.substring(0, index + 1)).replace(".", "") + "00");
        }
        return amLong.toString();
    }

    /**
     * 计算比率：部分数据/总数
     *
     * @param a 部分数据
     * @param b 总数
     * @return 比率
     */
    public static int ratio(long a, long b) {
        return (int) ((BigDecimal.valueOf((float) a / b).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) * 100);
    }

    /**
     * 获取本月第一天
     *
     * @return String
     */
    public static String getMonthStart() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date time = cal.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time) + " 00:00:00";
    }

    public static JSONObject getJsonObject(JSONObject jsonObject, String streetId, String communityId) {
        if (!StringUtils.isEmpty(streetId) && StringUtils.isEmpty(communityId)) {
//           街道用户默认显示该街道 可以该街道下社区老人
            jsonObject.put("homeStreetId", streetId);
        } else if (!StringUtils.isEmpty(streetId) && !StringUtils.isEmpty(communityId)) {
//           社区用户默认显示该社区 不可以查询其他老人
            jsonObject.put("homeStreetId", null);
            jsonObject.put("homeCommunityId", communityId);
        }
        return jsonObject;
    }

    /**
     * 获取真实ip地址，避免获取代理ip
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = null;
        String ipAddresses = request.getHeader("X-Forwarded-For");
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("X-Real-IP");
        }
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取登录方式
     *
     * @param request
     * @return
     */
    public static String getLoginType(HttpServletRequest request) {
        String requestHeader = request.getHeader("user-agent");
        /**
         * android : 所有android设备
         * mac os : iphone ipad
         * windows phone:Nokia等windows系统的手机
         */
        String[] deviceArray = new String[]{"android", "mac os", "windows phone"};
        if (requestHeader == null) {
            return LoginType.UNKNOWN.getValue();
        }

//        requestHeader = requestHeader.toLowerCase();
//        for (int i = 0; i < deviceArray.length; i++) {
//            if (requestHeader.indexOf(deviceArray[i]) > 0) {
//                return LoginType.MOBILE.getValue();
//            }
//        }
        return LoginType.PC.getValue();
    }

    /**
     * 两个日期之间的所有月份
     *
     * @param minDate 开始时间
     * @param maxDate 结束时间
     * @return java.util.List<java.lang.String>
     * @author Kikki  2020/6/26 14:53
     */
    public static List<String> getMonthBetween(String minDate, String maxDate) throws Exception {
        ArrayList<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(sdf.parse(minDate));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(sdf.parse(maxDate));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }
        return result;
    }

    /**
     * 两个日期之间的所有天数
     *
     * @param minDate 开始时间
     * @param maxDate 结束时间
     * @return java.util.List<java.lang.String>
     * @author Lin  2020/6/30 14:53
     */
    public static List<String> getDayBetween(String minDate, String maxDate) throws Exception {
        List<String> days = new ArrayList<String>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//格式化为年月

        Date start = dateFormat.parse(minDate);
        Date end = dateFormat.parse(maxDate);

        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);

        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
        while (tempStart.before(tempEnd)) {
            days.add(dateFormat.format(tempStart.getTime()));
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return days;
    }

    public static List<String> getWeekBetween(String minDate, String maxDate) throws Exception {
        ArrayList<String> result = new ArrayList<>();
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//格式化为年月

        Date start = dateFormat.parse(minDate);
        Date end = dateFormat.parse(maxDate);

        min.setTime(start);
        max.setTime(end);

        Calendar curr = min;
        while (curr.before(max)) {
            curr.setFirstDayOfWeek(Calendar.MONDAY);
            curr.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            result.add(dateFormat.format(curr.getTime()));
            curr.add(Calendar.WEEK_OF_YEAR, 1);
        }
        return result;
    }


    public static Calendar stringToCalendar(String datetime, String format) {
        Calendar calendar = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(datetime);
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            return null;
        }
    }

    public static int getWeekOfMonth(String date) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d = dateFormat.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);

        // 第几周
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    /**
     * 单次加密，双次解密
     */
    public static String convertMD5(String inStr) {

        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;
    }


}
