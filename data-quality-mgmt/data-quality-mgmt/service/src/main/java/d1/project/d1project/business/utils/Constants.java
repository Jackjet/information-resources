package d1.project.d1project.business.utils;

import java.io.File;

/**
 * @author: maorui
 * @date: 2020/9/29
 */
public class Constants {

    /**
     * 用户登录后token失效时间，暂定1天
     */
    public final static int TOKEN_EXPIRE_TIME = 24 * 60 * 60;
    /**
     * 注册验证码过期时间，暂定10分钟
     */
    public final static int VER_CODE_EXPIRE_TIME = 10 * 60;


    ///文件目录结构

    public final static String TEMP_ROOT = "." + File.separator + "temp" + File.separator;
    public final static String FILES_ROOT = "." + File.separator + "files" + File.separator;

//    public final static String ACTIVITIES = FILES_ROOT + "activities" + File.separator;
//    public final static String USERS = FILES_ROOT + "users" + File.separator;


    //数据源状态
    public final static String DATASOURCE_STATUS_ENABLE = "1";
    public final static String DATASOURCE_STATUS_UNENABLE = "0";

    //任务状态
    public final static String TASK_STATUS_ENABLE = "1";
    public final static String TASK_STATUS_UNENABLE = "0";

    public final static String TASK_CYCLE_ONCE = "0";
    public final static String TASK_CYCLE_DAILY = "1";
    public final static String TASK_CYCLE_WEEKLY = "2";
    public final static String TASK_CYCLE_MONTHLY = "3";
}
