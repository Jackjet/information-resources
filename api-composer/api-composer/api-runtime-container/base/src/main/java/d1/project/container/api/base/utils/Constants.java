package d1.project.container.api.base.utils;

import java.io.File;

/**
 * @author maorui
 */
public final class Constants {

    //////////////文件目录结构
    /**
     * 临时目录
     */
    public final static String TEMP_ROOT = "." + File.separator + "temp" + File.separator;

    /**
     * 文件存储目录
     */
    public final static String FILES_ROOT = "." + File.separator + "files" + File.separator;

    /**
     * 数据目录
     */
    public final static String DATA_ROOT = "." + File.separator + "data" + File.separator;

    /**
     * 日志目录
     */
    public final static String LOG_ROOT = "." + File.separator + "logs" + File.separator;


    /**
     * excel 下载目录
     */
    public final static String EXCEL = Constants.FILES_ROOT + "excel" + File.separator;
}
