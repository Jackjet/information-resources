package d1.project.d1project.log.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author kikki
 */
@Entity
@Table(name = "d1_user_log")
@ApiModel(value = "UserLogEntity", description = "用户日志")
public class UserLogEntity extends BaseCreateEntity {

    @ApiModelProperty("创建人")
    @Column(length = 100)
    private String createByName;

    @ApiModelProperty("创建人账户")
    @Column(length = 100)
    private String createByAccount;

    @ApiModelProperty("创建人手机")
    @Column(length = 100)
    private String createByPhone;

    @ApiModelProperty("类型\tint\t0系统,1人为")
    @Column(length = 1)
    private int type;

    @ApiModelProperty("内容\ttext\t添加的详情为json")
    @Column(columnDefinition = "TEXT")
    private String content;

    @ApiModelProperty("内容描述\ttext\t内容描述为简易信息")
    @Column(columnDefinition = "TEXT")
    private String contentMsg;

    @ApiModelProperty("接口")
    @Column(length = 100)
    private String api;

    @ApiModelProperty("来源ip")
    @Column(length = 100)
    private String sourceIp;

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

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
