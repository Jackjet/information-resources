package d1.project.api.integration.kong.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.entity.BaseCreateEntity;
import d1.framework.webapi.entity.BaseEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import java.util.Calendar;

/**
 * kong日志文件地址
 */
@Entity
@Table(name = "d1_kong_log_dir")
@ApiModel(value = "KongLogDir", description = "kong日志文件地址")
public class KongLogDir extends BaseEntity {
    private String fileName;
    private String container;
    private String dir;
    private Calendar createTime;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }
}
