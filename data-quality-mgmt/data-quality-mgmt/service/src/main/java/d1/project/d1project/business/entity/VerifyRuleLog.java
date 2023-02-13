package d1.project.d1project.business.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "d1_verify_rule_logs")
@ApiModel(value = "VerifyRuleLog", description = "质量检查规则变更日志")
public class VerifyRuleLog extends BaseEntity {


    @ApiModelProperty("质量检查规则变更id")
    private String id;

    @ApiModelProperty("质量检查规则id")
    private String verifyruleid;

    @ApiModelProperty("修改时间")
    private Date updatetime;

    @ApiModelProperty("变更日志")
    private String updatalog;

    public String getVerifyruleid() {
        return verifyruleid;
    }

    public void setVerifyruleid(String verifyruleid) {
        this.verifyruleid = verifyruleid;
    }

    public Date getUpdateime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getUpdatalog() {
        return updatalog;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void setUpdatalog(String updatalog) {
        this.updatalog = updatalog;
    }
}
