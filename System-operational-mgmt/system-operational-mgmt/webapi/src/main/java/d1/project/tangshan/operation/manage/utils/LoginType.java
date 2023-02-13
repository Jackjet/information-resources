package d1.project.tangshan.operation.manage.utils;

/**
 * 登录方式
 *
 * @author chenghan
 */
public enum LoginType {
    UNKNOWN("unknown", "未知设备来源"),
    PC("pc", "电脑登录"),
    MOBILE("mobile", "移动端登录");

    private String name;
    private String value;

    LoginType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
