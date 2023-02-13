package d1.project.d1project.log.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.*;
import java.util.Calendar;

/**
 * 操作日志
 */
@Entity
@Table(name = "d1_operation_log")
@ApiModel(value = "OperationLogEntity", description = "操作日志")
public class OperationLogEntity extends BaseCreateEntity {

    /**
     * @mock @cname(0,3)
     */
    @ApiModelProperty("创建人")
    @Column(length = 100)
    private String createByName;

    /**
     * @mock @cname(0,3)
     */
    @ApiModelProperty("创建人账户")
    @Column(length = 100)
    private String createByAccount;

    /**
     * @mock 1@pick(["34","35","36","37","38","39","50","5","52","58","59","57","82","87","88","70","47","30","3","32","55","56","85","86","33","53","80","89"])@string("number", 8)
     */
    @ApiModelProperty("创建人手机")
    @Column(length = 100)
    private String createByPhone;

    /**
     * @mock @cword(0,3)
     */
    @ApiModelProperty("模块")
    @Column(length = 100)
    private String module;

    /**
     * @mock @string(0,100)
     */
    @ApiModelProperty("接口")
    @Column(length = 100)
    private String api;

    @ApiModelProperty("类型:0系统,1人为")
    @Column(length = 1)
    private int type;

    /**
     * @mock @string(0,100)
     */
    @ApiModelProperty("内容")
    @Column(columnDefinition = "TEXT")
    private String content;

    /**
     * @mock @cword(0,100)
     */
    @ApiModelProperty("内容描述为简易信息")
    @Column(columnDefinition = "TEXT")
    private String contentMsg;

    /**
     * @mock @datetime
     */
    @ApiModelProperty("开始时间")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar startTime;

    /**
     * @mock @datetime
     */
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
