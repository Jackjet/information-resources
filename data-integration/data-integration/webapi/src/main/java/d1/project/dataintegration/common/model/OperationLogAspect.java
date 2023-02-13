package d1.project.dataintegration.common.model;

import java.lang.annotation.*;

/**
 * @author 17503
 */
@Target(ElementType.METHOD) //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented
public @interface OperationLogAspect {
    /**
     * 操作模块
     * 必填
     */
    String model() default "";

    /**
     * 接口
     * 缺省根据api枚举类：获取接口名
     * 非必填
     *
     * @see d1.project.dataintegration.common.ApiEnum
     */
    String api() default "";

    /**
     * 展示参数
     * 非必填
     * contentMsg 操作内容展示前缀
     * contentParameters 操作内容展示字段
     */
    OperationLogParametersAspect[] displayContent() default {@OperationLogParametersAspect(contentMsg = "", contentParameters = "")};
}
