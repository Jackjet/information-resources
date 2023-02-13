package d1.project.resource.common;

import java.io.File;
import java.util.Arrays;
import java.util.List;

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
    public final static int TOKEN_EXPIRE_TIME = 24 * 60 * 60;
    /**
     * 不可删除角色
     */
    public final static List<String> CANNOT_DELETED_ROLE = Arrays.asList("admin", "default");
    /**
     * 不可删除菜单
     */
    public final static List<String> CANNOT_DELETED_MENU_TREE = Arrays.asList("02d0aa6c0e644e3f90aea6cd7cdd3d59", "045bc95a27624d44b94575b610749269",
            "1046cf27f46549609da95cc719277eb1", "142f0f4993dc4e9d97bbc249e3091926", "180889784e7041e88fc1983571ff2dfb", "1b31f8af13044cdbbad8816d355d147b",
            "21984b06e86c4667940869dea5ac8c20", "2c3aa4735b484699af812281bb3f78a8", "4c16960f95974299a5fd610df6a1ed54", "4ddee9efffc84344a3d6c08726686792",
            "4e0a5bb8061340aaba2032fb5ce7c6f8", "5306c986ed644d7f93f5370c38b10a50", "58c067850c0c4e7e88722a0ac43668a3", "68f58248669b406984663e6b7539dc4f",
            "6ef3337c0eba4df2ab099811cff61674", "81271ed483d54ddb814b8c72de9e7b04", "8fe6ba6f57954cd4a69bcb3881cd0d94", "951c7103f1394631931a8badd48b5975",
            "96050fae0d674f3998ee81a3556b3164", "9a6d1f16e306467bb96166de6cc80159", "9f911a785e2d49c682dc22d0379e0455", "af1c183e6b9e4ee3970aecb62670ebf8",
            "b52942bcd7d54a378dc4b61661ba1a4b", "b6b8d3c1a18c4165b5070fdf91684815", "b71ead0d2db841e4b3b925d57e2b638c", "c700dcd3cab441148c81c07ce3c1ef69",
            "cb38c7fad3eb45bfbf9422cc24b62178", "d130850df56e44329bd24814bf2f8902", "d219c621546c4468901f38ab3b8ce317", "dbabc974a6344a90b6da0c7f88c9e424",
            "df88a57d5ab84bc58899a3ebcdbf3c4e", "f11f28ac9f744eb69b05be651d10f5ff", "f72f39e2b35c4e4ba26dd02d9603f7a2", "f7facbd19fcb4ae79c021f1355d03cb9",
            "f887432193e9422c90ea1ad25247b877", "f9582a6d01834919a6cd20315ae6e883", "ffbfa1599ff34cb8bbdbcdec6fe46b61"
    );
    /**
     * 不可删除部门
     */
    public final static String CANNOT_DELETED_ORGANIZATION = "headquarters";
    public final static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 用户删除事件
     */
    public final static String USER_DELETE_EVENT = "userDelete";


    ///文件目录结构

    public final static String TEMP_ROOT = "." + File.separator + "temp" + File.separator;
    public final static String FILES_ROOT = "." + File.separator + "files" + File.separator;

    public final static String USERS = FILES_ROOT + "users" + File.separator;

    public final static String DATA_NULL = "当前信息不存在";
    public final static String DATA_SOURCE_NAME_REPEAT = "数据源名称已存在";
    public final static String DATA_SOURCE_JDBC_REPEAT = "数据源数据链接已存在";
    public final static String CONTAINER_NAME_REPEAT = "容器名称已存在";
    public final static String CONTAINER_URL_REPEAT = "容器地址已存在";
    public final static String USER_IS_NULL = "用户不存在";
    public final static String REQUEST_METHOD_ERROR = "请求方法不存在";
    public final static String DATA_TYPE_ERROR = "数据库类型错误";
    public final static String NET_ERROR = "网络异常";
    public final static String DATA_INFO_ERROR = "数据库信息错误无法里连接";
    public final static String TAG_REPEAT_ERROR = "该标签已存在,请勿重复添加";
    public final static String DATA_NAME_REPEAT = "该名称已存在";
    public final static String GROUP_HAS_CHILD = "分组下有子类,不可删除";
    public final static String GROUP_TYPE_ERROR = "分组类型错误";
    public final static String GROUP_HAS_RESOURCE = "分组下有信息,不可删除";
    public final static String API_NAME_REPEAT = "接口名称已存在";
    public final static String API_IMPORT_ERROR = "部分数据无名称无法导入";

    /**
     * 文件根路径
     */
    public final static String FILE_ROOT = System.getProperty("user.dir") + File.separator + "file" + File.separator;

    public final static String TEST_CASE_DOWNLOAD = FILE_ROOT + "test_case_download" + File.separator;


    /**
     * 源API文件导出根路径
     */
    public final static String SOURCE_API_FILE_ROOT = FILE_ROOT + "sourceApi" + File.separator;

    public final static String FILE_CREATE_ERROR = "文件创建失败";

    public final static String DIR_CREATE_ERROR = "文件夹创建失败";

}
