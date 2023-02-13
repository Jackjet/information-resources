package d1.project.dataintegration.common.model;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 17503
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface OperationLogParametersAspect {

    /**
     * contentParameters 操作内容展示字段
     */
    String contentParameters();

    /**
     * contentMsg 操作内容展示前缀
     */
    String contentMsg();

}
