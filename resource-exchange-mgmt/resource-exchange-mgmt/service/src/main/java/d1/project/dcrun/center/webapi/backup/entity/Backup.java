package d1.project.dcrun.center.webapi.backup.entity;

import d1.framework.webapi.entity.BaseCreateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author xuaa
 */
@Entity
@Table(name = "d1_backup")
@ApiModel(value = "Backup", description = "备份管理")
public class Backup extends BaseCreateEntity {
    @ApiModelProperty(value = "备份名称")
    private String name;
    @ApiModelProperty(value = "备份url")
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
