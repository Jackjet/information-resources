package d1.project.container.api.configuration;

import net.dcrun.component.file.server.FileServerService;
import net.dcrun.component.file.server.IFileServerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 开启websocket支持
 */
@Configuration
public class WebConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
    @Bean
    IFileServerService fileServerService() {
        return new FileServerService();
    }
}
