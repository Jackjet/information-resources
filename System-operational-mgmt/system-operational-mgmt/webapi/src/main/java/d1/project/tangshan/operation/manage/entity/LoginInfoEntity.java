package d1.project.tangshan.operation.manage.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import d1.framework.webapi.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Buter
 * @date 2020/3/20 14:40
 */
@Entity
@Table(name = "d1_login_info")
@ApiModel(value = "LoginInfoEntity", description = "用户登录信息")
public class LoginInfoEntity extends BaseEntity {
    @ApiModelProperty("用户名")
    @Excel(name = "用户", orderNum = "0", width = 15)
    private String name;

    @ApiModelProperty("用户ip")
    @Excel(name = "用户ip", orderNum = "1", width = 15)
    private String ip;

    @ApiModelProperty("登录方式")
    @Excel(name = "登录系统", orderNum = "3", width = 15)
    private String type;

    @ApiModelProperty("登录人id")
    private String loginUserId;

    @Temporal(TemporalType.TIMESTAMP)

    @ApiModelProperty(value = "登录时间", example = "2018-01-23 09:12:32")
    private Calendar loginTime;

    @Transient
    @Excel(name = "登录时间", orderNum = "2", width = 25,format = "yyyy-MM-dd HH:mm:ss")
    //导出表格使用
    private Date loginTimeTransient;

    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "首次登录时间", example = "2018-01-23 09:12:32")
    private Calendar firstLoginTime;


    public Date getLoginTimeTransient() {
        return loginTime.getTime();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(String loginUserId) {
        this.loginUserId = loginUserId;
    }

    public Calendar getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Calendar loginTime) {
        this.loginTime = loginTime;
    }

    public Calendar getFirstLoginTime() {
        return firstLoginTime;
    }

    public void setFirstLoginTime(Calendar firstLoginTime) {
        this.firstLoginTime = firstLoginTime;
    }

    public void setLoginTimeTransient(Date loginTimeTransient) {
        this.loginTimeTransient = loginTimeTransient;
    }

}
