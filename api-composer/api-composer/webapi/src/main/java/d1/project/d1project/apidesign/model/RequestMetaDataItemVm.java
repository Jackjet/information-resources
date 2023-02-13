package d1.project.d1project.apidesign.model;

/**
 * http request 参数描述
 */
public class RequestMetaDataItemVm {

    /**
     * id
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 参数来源：Header、Parameter、Body
     */
    private String from = "Body";

    /**
     * 参数类型
     */
    private String type;

    /**
     * 参数描述
     */
    private String description;

    public RequestMetaDataItemVm() {
        this.from = "Body";
    }

    public RequestMetaDataItemVm(String name, String type, String from) {
        this.id = "";
        this.name = name;
        this.type = type;
        this.description = "";
        this.from = from;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
