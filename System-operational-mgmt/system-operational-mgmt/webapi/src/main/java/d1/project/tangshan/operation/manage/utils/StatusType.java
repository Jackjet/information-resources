package d1.project.tangshan.operation.manage.utils;

public enum StatusType {
    NORMAL("正常", "正常"),
    ABNORMAL("异常", "异常"),
    STOP("停止", "停止");

    private String name;
    private String value;

    StatusType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }
}
