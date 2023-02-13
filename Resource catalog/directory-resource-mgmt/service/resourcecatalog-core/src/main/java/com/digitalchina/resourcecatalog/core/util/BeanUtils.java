package com.digitalchina.resourcecatalog.core.util;

/**
 * @description:dto转换DO工具类 do转换DTO
 */
public class BeanUtils<Dto, Do> {

    /**
     * dot 转换为Do 工具类
     */
    public static <Do> Do dtoToDo(Object dtoEntity, Class<Do> doClass) {
        // 判断dto是否为空!
        if (dtoEntity == null) {
            return null;
        }
        // 判断DoClass 是否为空
        if (doClass == null) {
            return null;
        }
        try {
            Do newInstance = doClass.newInstance();
            org.springframework.beans.BeanUtils.copyProperties(dtoEntity, newInstance);
            // Dto转换Do
            return newInstance;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * do 转换为Dto 工具类
     */
    public static <Dto> Dto doToDto(Object doEntity, Class<Dto> dtoClass) {
        // 判断dto是否为空!
        if (doEntity == null) {
            return null;
        }
        // 判断DoClass 是否为空
        if (dtoClass == null) {
            return null;
        }
        try {
            Dto newInstance = dtoClass.newInstance();
            org.springframework.beans.BeanUtils.copyProperties(doEntity, newInstance);
            // Dto转换Do
            return newInstance;
        } catch (Exception e) {
            return null;
        }
    }
}
