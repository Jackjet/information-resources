package d1.project.container.api.base.bean;

import java.util.Map;

public class Node {

    /**
     * 节点id
     */
    private String id;

    /**
     * 节点名称
     */
    private String name;

    /**
     * 节点类型（组件类型）
     */
    private String type;

    /**
     * 下一个节点id
     */
    private String next;

    /**
     * 节点属性
     */
    private Map<String, Object> properties;

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

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
