package d1.project.dcrun.center.webapi.common.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @Author chengh
 **/
@Configuration
public class SwaggerConfig extends WebMvcConfigurerAdapter {

    @Bean
    public Docket integration() {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/integration/**")).build().groupName("integration角色接口").pathMapping("/")
                .apiInfo(apiInfo("integration角色接口", "integration角色接口", "4.4"));
    }

    @Bean
    public Docket tenants() {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/tenants/**")).build().groupName("tenants角色接口").pathMapping("/")
                .apiInfo(apiInfo("tenants角色接口", "tenants角色接口", "4.4"));
    }

    @Bean
    public Docket service() {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/service/**")).build().groupName("service接口").pathMapping("/")
                .apiInfo(apiInfo("service接口", "service接口", "4.4"));
    }

    private ApiInfo apiInfo(String name, String description, String version) {
        ApiInfo apiInfo = new ApiInfoBuilder().title(name).description(description).version(version).build();
        return apiInfo;
    }
}
