package d1.project.dcrun.center.webapi.log.entity;

import d1.framework.webapi.entity.BaseCreateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "d1_operation_log")
@ApiModel(value = "OperationLogEntity", description = "操作日志")
public class OperationLogEntity extends BaseCreateEntity {

    @ApiModelProperty("创建人")
    @Column(length = 100)
    private String createByName;

    @ApiModelProperty("创建人账户")
    @Column(length = 100)
    private String createByAccount;

    @ApiModelProperty("创建人手机")
    @Column(length = 100)
    private String createByPhone;

    @ApiModelProperty("模块")
    @Column(length = 100)
    private String module;

    @ApiModelProperty("接口")
    @Column(length = 100)
    private String api;

    @ApiModelProperty("类型:0系统,1人为")
    @Column(length = 1)
    private int type;

    @ApiModelProperty("内容")
    @Column(columnDefinition = "TEXT")
    private String content;

    @ApiModelProperty("内容描述为简易信息")
    @Column(columnDefinition = "TEXT")
    private String contentMsg;

    @ApiModelProperty("开始时间")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar startTime;

    @ApiModelProperty("结束时间")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar endTime;

    @ApiModelProperty("耗时:选用")
    private long elapsedTime;

    @ApiModelProperty("结果")
    @Column(length = 1)
    private int result;

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getCreateByAccount() {
        return createByAccount;
    }

    public void setCreateByAccount(String createByAccount) {
        this.createByAccount = createByAccount;
    }

    public String getCreateByPhone() {
        return createByPhone;
    }

    public void setCreateByPhone(String createByPhone) {
        this.createByPhone = createByPhone;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentMsg() {
        return contentMsg;
    }

    public void setContentMsg(String contentMsg) {
        this.contentMsg = contentMsg;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
