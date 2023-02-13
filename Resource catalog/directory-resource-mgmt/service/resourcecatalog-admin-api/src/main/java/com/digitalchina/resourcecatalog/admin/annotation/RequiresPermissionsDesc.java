package com.digitalchina.resourcecatalog.admin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermissionsDesc {
    String[] menu();

    String button();
    
    int order() default 5;
    //二级排序
    int order2() default 10;
    //三级排序
    int order3() default 10;
}
