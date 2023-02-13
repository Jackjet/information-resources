package d1.project.resourcesharingmgmt.resource.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.*;
import java.util.Calendar;

/**
 * 资源目录数据推送记录
 *
 * @author zhengyang
 */
@Entity
@Table(name = "d1_data_push_log")
@ApiModel(value = "DataPushLogEntity", description = "资源目录数据推送记录")
public class DataPushLogEntity extends BaseCreateEntity {
    @ApiModelProperty("信息资源提供方部门ID")
    @Column(length = 32)
    private String provOrgId;

    @ApiModelProperty("申请人部门id")
    @Column(length = 32)
    private String createDeptId;

    @ApiModelProperty("推送条数")
    private Integer num;

    public String getProvOrgId() {
        return provOrgId;
    }

    public void setProvOrgId(String provOrgId) {
        this.provOrgId = provOrgId;
    }

    public String getCreateDeptId() {
        return createDeptId;
    }

    public void setCreateDeptId(String createDeptId) {
        this.createDeptId = createDeptId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
