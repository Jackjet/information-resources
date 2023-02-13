package d1.project.api.integration.common.configuration;

import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.project.api.integration.common.ApiEnum;
import d1.project.api.integration.common.model.OperationLog;
import d1.project.api.integration.common.model.OperationLogAspect;
import d1.project.api.integration.common.model.OperationLogParametersAspect;
import d1.project.api.integration.common.service.IOperationLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
public class ControllerLogAspect {

    final
    IOperationLogService iOperationLogService;
    private static Logger log = LoggerFactory.getLogger(ControllerLogAspect.class);

    public ControllerLogAspect(IOperationLogService iOperationLogService) {
        this.iOperationLogService = iOperationLogService;
    }

    /**
     * 切入点描述 这个是controller包的切入点
     */
    @Pointcut("@annotation(d1.project.api.integration.common.model.OperationLogAspect)")
    public void annotationOptLog() {
    }//签名，可以理解成这个切入点的一个名称

    @Around("annotationOptLog()")
    public Object insertLogBeforeController(ProceedingJoinPoint joinPoint) throws Throwable {
        /*参数列表*/
        Object[] args = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        /*获取注释*/
        OperationLogAspect operationLogAspect = ((MethodSignature) signature).getMethod().getAnnotation(OperationLogAspect.class);
        String api = "";
        if (StringUtils.isEmpty(operationLogAspect.api())) {
            /*方法名称*/
            String methodName = joinPoint.getSignature().getName();
            /*从枚举里获取接口名称*/
            api = ApiEnum.getApiNameValue(methodName);
        } else {
            api = operationLogAspect.api();
        }
        Object result;
        List<String> collect = new ArrayList<>();
        //拼接参数
        JSONObject parse = (JSONObject) JSON.toJSON(args[0]);
        for (OperationLogParametersAspect operationLogParametersAspect : operationLogAspect.displayContent()) {
            String contentMsg = operationLogParametersAspect.contentMsg();
            if (!StringUtils.isEmpty(contentMsg)) {
                collect.add(contentMsg + parse.get(operationLogParametersAspect.contentParameters()));
            }
        }
        /*获取servlet请求属性*/
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        try {
            // 执行方法
            result = joinPoint.proceed();
            if (requestAttributes == null) {
                return result;
            }
            iOperationLogService.insert(new OperationLog(operationLogAspect.model(), api, String.join("\n", collect), JSON.toJSONString(args[0]), 1), requestAttributes.getRequest());
        } catch (Exception e) {
            if (requestAttributes == null) {
                throw e;
            }
            // 记录异常日志
            collect.add("异常问题 " + e.getMessage());
            OperationLog model = new OperationLog(operationLogAspect.model(), api, String.join("\n", collect), JSON.toJSONString(args[0]), 0);
            log.error(model.toString());
            iOperationLogService.insert(model, requestAttributes.getRequest());
            throw e;
        }
        return result;
    }
}
