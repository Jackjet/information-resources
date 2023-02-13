package d1.project.dataintegration.common.configuration;

import net.dcrun.component.file.server.FileServerService;
import net.dcrun.component.file.server.IFileServerService;
import net.dcrun.component.http.HttpService;
import net.dcrun.component.http.IHttpService;
import net.dcrun.component.security.HmacSignService;
import net.dcrun.component.security.IHmacSignService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Buter
 * @date 2020/3/10 10:37
 */
@Configuration
public class MyConfiguration {

    @Bean
    IHttpService httpService() {
        return new HttpService();
    }

    @Bean
    IFileServerService fileServerService() {
        return new FileServerService();
    }

    @Bean
    IHmacSignService hmacSignService() {
        return new HmacSignService();
    }
}
