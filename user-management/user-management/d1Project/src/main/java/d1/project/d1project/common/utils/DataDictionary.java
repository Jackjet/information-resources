package d1.project.d1project.common.utils;


import java.util.HashMap;


/**
 * 数据字典
 *
 * @author baozh
 * @date 2021/03/29
 */
public class DataDictionary {

    public final static HashMap<Integer, String> SYS_NAME = new HashMap<Integer, String>() {
        {
            put(0, "用户管理系统");
            put(1, "资源管理系统");
            put(2, "API编排系统");
            put(3, "API管理服务系统");
            put(4, "数据集成服务系统");
            put(5, "消息集成服务系统");
        }
    };
}
