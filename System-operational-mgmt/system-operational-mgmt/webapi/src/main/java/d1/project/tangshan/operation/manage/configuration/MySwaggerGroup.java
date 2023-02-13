package d1.project.tangshan.operation.manage.configuration;

import d1.framework.webapi.configuration.SwaggerGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author Buter
 * @date 2020/3/15 18:35
 */
@Configuration
public class MySwaggerGroup {
    private final SwaggerGroup swaggerGroup;

    @Autowired
    public MySwaggerGroup(SwaggerGroup swaggerGroup) {
        this.swaggerGroup = swaggerGroup;
    }

    /**
     * 定义“webadmin”的swagger组，所有路由以webadmin开头的都可以在这个组里找到
     * 这种api通常为web管理界面的用户调用
     *
     * @return swagger组
     */
    @Bean
    @ConditionalOnProperty(prefix = "d1.framework.webapi.swagger", name = "enable", havingValue = "true", matchIfMissing = false)
    public Docket createWebadminApi() {
        return swaggerGroup.build("webadmin");
    }

    /**
     * 定义“service”的swagger组，所有路由以service开头的都可以在这个组里找到
     * 这种api通常为其它开发者调用，通常使用hamc来签名
     *
     * @return swagger组
     */
    @Bean
    @ConditionalOnProperty(prefix = "d1.framework.webapi.swagger", name = "enable", havingValue = "true", matchIfMissing = false)
    public Docket createServiceApi() {
        return swaggerGroup.build("service");
    }
}
