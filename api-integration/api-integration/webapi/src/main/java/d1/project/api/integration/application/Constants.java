package d1.project.api.integration.application;

import java.io.File;

/**
 * 一些常量，包括枚举等
 *
 * @author Buter
 * @date 2020/3/18 16:09
 */
public class Constants {
    /**
     * 文件根路径
     */
    public final static String FILE_ROOT = System.getProperty("user.dir") + File.separator + "file" + File.separator;
    /**
     * 应用文件导出根路径
     */
    public final static String APPLICATION_FILE_ROOT = FILE_ROOT + "application" + File.separator;
}
