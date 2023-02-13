package d1.framework.permission.configuration;

import com.alibaba.fastjson.JSONObject;
import d1.framework.permission.annotation.Permission;
import d1.framework.permission.service.MyServices;
import d1.framework.webapi.http.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * @author all
 */
public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private MyServices myServices;

    /**
     * admin用户不许添加、编辑、删除功能树
     */
    private final static String[] MENU_TREE_OPT = {"/webadmin/permission/MenuTree/insert", "/webadmin/permission/MenuTree/update", "/webadmin/permission/MenuTree/deleteById"};

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        // 验证权限
        if (this.hasAuth(httpServletRequest, handler)) {
            return true;
        }

        this.setWebApiError(httpServletResponse);
        return false;
    }

    private boolean hasAuth(HttpServletRequest httpServletRequest, Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            // 获取方法上的注解（方法上可能是 nopermission
            Permission permissionAnno = handlerMethod.getMethodAnnotation(Permission.class);
            // 如果方法上的注解为空 则获取类的注解
            if (permissionAnno == null) {
                permissionAnno = handlerMethod.getBean().getClass().getAnnotation(Permission.class);
            }

            // 如果标记了注解，则判断数据权限
            if (permissionAnno != null) {

                String[] permissionGroup = permissionAnno.value();
                if (Arrays.asList(permissionGroup).contains("nopermission")) {
                    return true;
                }

                //进行权限校验，一定要求登入
                String auth = httpServletRequest.getHeader("Authorization");
                if (StringUtils.isEmpty(auth)) {
                    return false;
                }
                String path = httpServletRequest.getServletPath();
                String userId = myServices.getMyUserId(httpServletRequest);
                if (!"root".equals(userId) && !"admin".equals(userId)) {
                    List<String> routes = myServices.getMyMenuTreeRoutes(httpServletRequest);
                    if (routes == null || !routes.contains(path)) {
                        return false;
                    }
                } else {
                    if ("admin".equals(userId) && Arrays.asList(MENU_TREE_OPT).contains(path)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void setWebApiError(HttpServletResponse httpServletResponse) throws Exception {
        Result<Object> obj = new Result<>();
        obj.setCode(0);
        obj.setMsg("无访问权限");
        obj.setData("");

        String dataString = JSONObject.toJSONString(obj);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");

        PrintWriter writer = httpServletResponse.getWriter();
        writer.print(dataString);
        writer.flush();
        writer.close();
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
