package d1.project.tangshan.operation.manage.entity;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Buter
 * @date 2020/3/20 14:40
 */
@Entity
@Table(name = "d1_big_data_api_plan")
@ApiModel(value = "BigDataApiPlanEntity", description = "数据服务接口计划任务")
public class BigDataApiPlanEntity extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("计划名称")
    private String name;

    @ApiModelProperty("任务状态，已启动(started)，已暂停(paused)")
    private String status;

    @ApiModelProperty("接口id")
    private String apiId;

    @ApiModelProperty("接口名称")
    private String apiName;

    @ApiModelProperty("cron表达式")
    private String cron;

    @Column(columnDefinition = "TEXT")
    @ApiModelProperty("url参数")
    private String params;

    @Column(columnDefinition = "TEXT")
    @ApiModelProperty("请求body")
    private String body;

    @ApiModelProperty("备注")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
