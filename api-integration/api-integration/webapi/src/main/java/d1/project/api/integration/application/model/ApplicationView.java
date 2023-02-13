package d1.project.api.integration.application.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author libin
 */
@Entity
@Immutable
@Subselect("SELECT d1_application.id AS id," +
        "d1_application.name AS name," +
        "d1_application.key AS key," +
        "d1_application.container AS container," +
        "d1_application.create_time AS create_time," +
        "d1_application.create_by_id AS create_by_id," +
        "d1_user.name AS create_by_name " +
        "FROM d1_application " +
        "LEFT JOIN d1_web_admin_user AS d1_user ON d1_application.create_by_id = d1_user.id ")
public class ApplicationView {
    @Id
    @ExcelProperty("应用ID")
    private String id;
    /**
     * 应用名称
     */
    @ExcelProperty("应用名称")
    private String name;
    /**
     * kong key
     */
    @ExcelProperty("应用密钥")
    private String key;
    /**
     * 容器
     */
    @ExcelIgnore
    private String container;

    @ExcelProperty("创建时间")
    private Date createTime;
    @ExcelIgnore
    private String createById;
    @ExcelProperty("创建人")
    private String createByName;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateById() {
        return createById;
    }

    public void setCreateById(String createById) {
        this.createById = createById;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }
}
