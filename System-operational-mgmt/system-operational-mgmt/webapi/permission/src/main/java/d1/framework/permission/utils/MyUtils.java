package d1.framework.permission.utils;

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


    public static Pageable getPageable(Integer page, Integer size, Sort sort) {
        page = page != null && page >= 1 ? page - 1 : 0;
        if (size == null || size < 1) {
            size = 10;
        }
        return sort != null ? PageRequest.of(page, size, sort) : PageRequest.of(page, size);
    }



    public static <T> T model2Entity(Object object, Class<T> entityClass) {
        String jsonObject = JSON.toJSON(object).toString();
        return JSON.parseObject(jsonObject, entityClass);
    }


    public static void throwMsg(boolean b, String msg) throws Exception {
        if (b) {
            throw new ValidException(msg);
        }
    }

}
