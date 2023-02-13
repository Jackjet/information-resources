package d1.project.dcrun.center.webapi.system.entity;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author libin
 */
@Entity
@Table(name = "d1_sys_install_pack")
@ApiModel(value = "SysInstallPack", description = "系统安装包表")
public class SysInstallPack extends BaseCreateAndUpdateEntity {
    @ApiModelProperty(value = "安装包名称")
    private String name;

    @ApiModelProperty(value = "安装包类型")
    private String type;

    @ApiModelProperty(value = "安装包版本")
    private Long version;

    @ApiModelProperty(value = "安装包文件名")
    private String filename;

    @ApiModelProperty(value = "备注")
    private String remark;

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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
