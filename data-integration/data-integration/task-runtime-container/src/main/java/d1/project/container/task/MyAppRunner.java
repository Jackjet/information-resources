package d1.project.container.task;

import d1.project.container.task.utils.Constants;
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
    private static final Logger logger = LoggerFactory.getLogger(MyAppRunner.class);


    @Override
    public void run(ApplicationArguments args) throws Exception {
        init();
    }

    /**
     * 初始化环境
     */
    public void init() throws Exception {
        //初始化目录结构
//        new File(Constants.TEMP_ROOT).mkdirs();
        new File(Constants.FILES_ROOT).mkdirs();
//        new File(Constants.DATA_ROOT).mkdirs();

    }
}
