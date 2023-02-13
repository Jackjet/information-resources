package d1.project.tangshan.operation.manage.utils;

/**
 * 告警方式
 *
 * @author chenghan
 */
public enum AlarmWay {
    SMS("sms", "短信"),
    EMAIL("email", "邮件"),
    ACCOUNT("account", "锁定账号");

    private String name;
    private String value;

    AlarmWay(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }
}
