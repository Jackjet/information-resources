package d1.project.resourcesharingmgmt.resource.configuration;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FtpDownloadRoute extends RouteBuilder {
    private static Logger logger = LoggerFactory.getLogger(FtpDownloadRoute.class);

    @Value("${ftp.server.info}")
    private String ftpServer;
    @Value("${ftp.local.dir}")
    private String downloadLocation;

    private final LocationFileProcessor locationFileProcessor;

    public FtpDownloadRoute(LocationFileProcessor locationFileProcessor) {
        this.locationFileProcessor = locationFileProcessor;
    }

    @Override
    public void configure() throws Exception {
        from(ftpServer)
                .process(locationFileProcessor)
                .to(downloadLocation)
                .log(LoggingLevel.INFO, logger, "Downloaded file ${file:name} complete.");
    }
}
