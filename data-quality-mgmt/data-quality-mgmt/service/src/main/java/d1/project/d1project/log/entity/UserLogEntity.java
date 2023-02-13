package d1.project.d1project.log.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户日志
 *
 * @author kikki
 */
@Entity
@Table(name = "d1_user_log")
@ApiModel(value = "UserLogEntity", description = "用户日志")
public class UserLogEntity extends BaseCreateEntity {

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
     * @mock @integer(0,1)
     */
    @ApiModelProperty("类型\tint\t0系统,1人为")
    @Column(length = 1)
    private int type;

    /**
     * @mock @cword(0,100)
     */
    @ApiModelProperty("内容\ttext\t添加的详情为json")
    @Column(columnDefinition = "TEXT")
    private String content;

    /**
     * @mock @cword(0,100)
     */
    @ApiModelProperty("内容描述\ttext\t内容描述为简易信息")
    @Column(columnDefinition = "TEXT")
    private String contentMsg;

    /**
     * @mock @string(0,100)
     */
    @ApiModelProperty("接口")
    @Column(length = 100)
    private String api;

    /**
     * @mock @ip
     */
    @ApiModelProperty("来源ip")
    @Column(length = 100)
    private String sourceIp;

    /**
     * @mock @integer(0,1)
     */
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
