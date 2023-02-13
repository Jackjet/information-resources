package com.digitalchina.resourcecatalog.core.util;

import java.util.*;

/**
 * 自定义List分页工具
 *
 * @author wangmh
 */
public class PageUtil {

    /**
     * 开始分页
     *
     * @param list
     * @param pageNum  页码
     * @param pageSize 每页多少条数据
     * @return
     */
    public static Map<String, Object> startPage(List list, Integer pageNum,
                                                Integer pageSize) {
        if (list == null) {
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        List pageList = new ArrayList();
        Integer count = list.size(); // 记录总数
        Integer pageCount = 0; // 页数
        if (list.size() != 0) {
            if (count % pageSize == 0) {
                pageCount = count / pageSize;
            } else {
                pageCount = count / pageSize + 1;
            }
            int fromIndex = 0; // 开始索引
            int toIndex = 0; // 结束索引

            if (pageNum != pageCount) {
                fromIndex = (pageNum - 1) * pageSize;
                toIndex = fromIndex + pageSize;
            } else {
                fromIndex = (pageNum - 1) * pageSize;
                toIndex = count;
            }
            pageList = list.subList(fromIndex, toIndex);
        }
        result.put("records", pageList);
        result.put("total", count);
        result.put("size", pageSize);
        result.put("current", pageNum);
        result.put("pages", pageCount);
        return result;
    }
}
