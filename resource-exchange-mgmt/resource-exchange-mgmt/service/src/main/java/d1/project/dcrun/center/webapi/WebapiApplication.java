package d1.project.dcrun.center.webapi;

import d1.framework.webapi.BaseApplication;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.util.regex.Pattern;

/**
 * @author chengh
 */
@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
public class WebapiApplication extends BaseApplication {

    private static Logger logger = LoggerFactory.getLogger(WebapiApplication.class);

    public static void main(String[] args) {

        try {
            for (File f : new File(".").listFiles()) {
                if (Pattern.matches(".{32}-tcp[0-9]{14}", f.getName())) {
                    FileUtils.deleteDirectory(f);
                }
            }
        } catch (Exception e) {
            logger.info("管理中心启动前，删除mqtt的clientId文件失败");
        }

        SpringApplication.run(WebapiApplication.class, args);
    }

}
