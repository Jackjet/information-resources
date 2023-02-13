package d1.project.dcrun.center.webapi.common.util;

/**
 * 组件所属库的类型
 * @author chenghan
 */
public enum RepoType {
    OFFICIAL("official", "官方库"),
    OTHER("other", "其他");

    private String name;
    private String value;

    RepoType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }
}
