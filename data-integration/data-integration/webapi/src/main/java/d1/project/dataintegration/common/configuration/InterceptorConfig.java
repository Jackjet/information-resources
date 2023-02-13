package d1.project.dataintegration.common.configuration;

import d1.project.dataintegration.task.service.WebAdminUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 自定义web controller拦截器，通过header的Authorization的token值（或sign值），以及当前用户的类型来判断是否有权限访问
 *
 * @author baozh
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    /**
     * 缺省启动拦截器，在一些特殊情况，比如SSO上可能需要关闭自定义的拦截器
     */
    @Value("${d1.project.apiAuth.interceptor.enable}")
    private boolean enableInterceptor;

    private final WebAdminUserService webAdminUserService;

    public InterceptorConfig(WebAdminUserService webAdminUserService) {
        this.webAdminUserService = webAdminUserService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (enableInterceptor) {
            // 注册拦截器
            InterceptorRegistration ir = registry.addInterceptor(getApiAuthInterceptor());
            // 配置拦截的路径
            ir.addPathPatterns("/**");
            // 配置不拦截的路径
            ir.excludePathPatterns("/**.html");
            ir.excludePathPatterns("/error");
        }
    }

    @Bean
    public ApiAuthInterceptor getApiAuthInterceptor() {
        return new ApiAuthInterceptor(webAdminUserService);
    }

}
