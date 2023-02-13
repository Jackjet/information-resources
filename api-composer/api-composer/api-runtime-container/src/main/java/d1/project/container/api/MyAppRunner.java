package d1.project.container.api;

import d1.project.container.api.base.utils.Constants;
import d1.project.container.api.service.MainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author Buter
 * @date 2020/3/20 14:59
 */
@Component
public class MyAppRunner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(WebapiApplication.class);

    private final MainService mainService;

    public MyAppRunner(MainService mainService) {
        this.mainService = mainService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        init();
    }

    /**
     * 初始化环境
     */
    public void init() throws Exception {
        //初始化目录结构
        new File(Constants.TEMP_ROOT).mkdirs();
        new File(Constants.FILES_ROOT).mkdirs();
        new File(Constants.DATA_ROOT).mkdirs();

        //初始化
        mainService.init();
    }
}
