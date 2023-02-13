package com.digitalchina.resourcecatalog.core.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: create by wangmh
 * @name: ListUtils.java
 * @description:
 * @date:2020/4/23
 **/
public class CollectUtils {

    public static boolean isEquals(List<Integer> list1, List<Integer> list2) {
        if (null != list1 && null != list2) {
            if (list1.containsAll(list2) && list2.containsAll(list1)) {
                return true;
            }
            return false;
        }
        return true;
    }


    public static <T> List<T> castList(Object obj, Class<T> clazz)
    {
        List<T> result = new ArrayList<T>();
        if(obj instanceof List<?>)
        {
            for (Object o : (List<?>) obj)
            {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }
}
