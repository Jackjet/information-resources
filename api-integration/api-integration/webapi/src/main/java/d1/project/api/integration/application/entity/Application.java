package d1.project.api.integration.application.entity;

import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author libin
 * 源API
 */
@Entity
@Table(name = "d1_application")
public class Application extends BaseCreateEntity {
    /**
     * 应用名称
     */
    private String name;
    /**
     * 密钥
     */
    private String key;

    /**
     * 容器
     */
    private String container;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }
}
