package d1.project.tangshan.operation.manage;

import java.io.File;

/**
 * 一些常量，包括枚举等
 *
 * @author Buter
 * @date 2020/3/18 16:09
 */
public class Constants {
    /**
     * 用户登录后token失效时间，暂定1天
     */
    public final static int TOKEN_EXPIRE_TIME = 1 * 24 * 60 * 60;
    public static final String SUCCESS = "成功";
    public static final String FAIL = "失败";
    public static final String SYNCHRONOUS_SUCCESS = "同步成功";
    public static final String ADD_SUCCESS = "添加成功";
    public static final String UPDATE_SUCCESS = "编辑成功";
    public static final String DELETE_SUCCESS = "删除成功";
    public static final String IMPORT_SUCCESS = "导入成功";

    public final static String FILES_ROOT = "." + File.separator + "files" + File.separator;

    public final static String FILES_STORAGE = FILES_ROOT + "storage" + File.separator;

    public final static String FILES_DEPLOYMENT = FILES_STORAGE + "deployment" + File.separator;
    public final static String FILES_UPGRADE = FILES_STORAGE + "upgrade" + File.separator;
    public final static String FILES_CONFIG_UPDATE = FILES_STORAGE + "configUpdate" + File.separator;
    public final static String FILES_ROLLBACK = FILES_STORAGE + "rollback" + File.separator;
}
