package d1.project.dcrun.center.webapi.common.configuration;

import net.dcrun.component.compression.CompressService;
import net.dcrun.component.compression.ICompressService;
import net.dcrun.component.emq.EmqService;
import net.dcrun.component.emq.IEmqService;
import net.dcrun.component.file.server.FileServerService;
import net.dcrun.component.file.server.IFileServerService;
import net.dcrun.component.http.HttpService;
import net.dcrun.component.http.IHttpService;
import net.dcrun.component.mqtt.client.IMqttClientService;
import net.dcrun.component.mqtt.client.MqttClientService;
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
    IMqttClientService mqttClientService() {
        return new MqttClientService();
    }

    @Bean
    IHttpService httpService() {
        return new HttpService();
    }

    @Bean
    ICompressService compressService() {
        return new CompressService();
    }

    @Bean
    IFileServerService fileServerService() {
        return new FileServerService();
    }

    @Bean
    IEmqService emqService() {
        return new EmqService();
    }

    @Bean
    IHmacSignService hmacSignService() {
        return new HmacSignService();
    }
}
