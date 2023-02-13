package d1.project.tangshan.operation.manage.utils;

/**
 * 数据服务接口类型
 *
 * @author chenghan
 */
public enum BigDataServiceType {
    HTTP("http", "HTTP"),
    WEBSERVICE("webservice", "WEBSERVICE"),
    SOCKET("socket", "SOCKET");

    private String name;
    private String value;

    BigDataServiceType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }
}
