package d1.project.tangshan.operation.manage.configuration;

import net.dcrun.component.ehcache.EhcacheService;
import net.dcrun.component.ehcache.IEhcacheService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IEhcacheConfiguration {
    @Bean
    public IEhcacheService cache() {
        return new EhcacheService();
    }
}
