package d1.project.api.integration.common.configuraton;

import net.dcrun.component.http.HttpService;
import net.dcrun.component.http.IHttpService;
import net.dcrun.component.https.HttpsService;
import net.dcrun.component.https.IHttpsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfiguration {
    @Bean
    public IHttpService iHttpService() {
        IHttpService iHttpService = new HttpService();
        iHttpService.setTimeout(60 * 1000, 60 * 1000, 60 * 1000);
        return iHttpService;
    }

    @Bean
    public IHttpsService iHttpsService() {
        IHttpsService iHttpsService = new HttpsService();
        iHttpsService.setTimeout(60 * 1000, 60 * 1000, 60 * 1000);
        return iHttpsService;
    }
}
