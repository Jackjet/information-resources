package d1.project.dataintegration.common.configuration;

import com.alibaba.fastjson.JSONObject;
import d1.project.dataintegration.common.annotation.ApiAuth;
import d1.project.dataintegration.task.service.WebAdminUserService;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * 接口权限拦截器
 *
 * @author baozh
 */
public class ApiAuthInterceptor implements HandlerInterceptor {

    private final WebAdminUserService webAdminUserService;

    public ApiAuthInterceptor(WebAdminUserService webAdminUserService) {
        this.webAdminUserService = webAdminUserService;
    }


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        //在HTTP请求处理之前进行调用（Controller方法调用之前验证权限
        if (this.hasAuth(httpServletRequest, handler)) {
            return true;
        }
        httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "无权限");
        //如果false，停止流程，api被拦截
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {
        //请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
    }


    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        //在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
    }


    /**
     * 根据HTTP 请求的header里Authorization的值和Auth注解里的值比较来判断是否有权限
     *
     * @param httpServletRequest http请求
     * @param handler            方法或类
     * @return 是否通过
     */
    private boolean hasAuth(HttpServletRequest httpServletRequest, Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            // 获取方法上的注解
            ApiAuth auth = handlerMethod.getMethod().getAnnotation(ApiAuth.class);
            // 如果方法上的注解为空 则获取类的注解
            if (auth == null) {
                auth = handlerMethod.getBean().getClass().getAnnotation(ApiAuth.class);
            }

            // 如果标记了注解，则判断权限
            if (auth != null) {
                //允许@Auth("aaa,bbb")多个角色
                String[] authGroup = auth.value();
                //@Auth("noauth")表示不需要通过校验，直接通过
                if (Arrays.asList(authGroup).contains("noauth")) {
                    return true;
                }
                if (authGroup.length <= 0) {
                    return false;
                }
                String authorization = httpServletRequest.getHeader("Authorization");
                if (StringUtils.isEmpty(authorization)) {
                    //header如果没定义Authorization或空值都不让调用
                    return false;
                }
                //格式必须是"Authorization token xxxxxxxx"
                //token值必须在缓存里能找到
                //找到对应的用户对象必须包含type类型，并且必须和注解@Auth对应的值一致
                String[] authArray = authorization.split(" ");
                if (authArray.length < 2) {
                    return false;
                }
                if (!"token".equals(authArray[0])) {
                    return false;
                }

                try {
                    JSONObject entity = webAdminUserService.getUserInfo(httpServletRequest);
                    if (entity != null && entity.containsKey("type")) {
                        return Arrays.asList(authGroup).contains(entity.getString("type"));
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return true;
    }
}
