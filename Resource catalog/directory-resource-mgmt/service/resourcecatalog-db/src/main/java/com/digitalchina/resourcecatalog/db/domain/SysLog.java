package com.digitalchina.resourcecatalog.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 操作日志表
 * </p>
 *
 * @author wangmh
 * @since 2020-04-30
 */
public class SysLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 管理员账号
     */
    private String admin;

    /**
     * 管理员名称
     */
    private String account;

    /**
     * 管理员地址
     */
    private String ip;

    /**
     * 操作分类
     */
    private Integer type;

    /**
     * 操作动作
     */
    private String action;

    /**
     * 操作状态
     */
    private Boolean status;

    /**
     * 操作结果，或者成功消息，或者失败消息
     */
    private String result;

    /**
     * 补充信息
     */
    private String comment;

    /**
     * 创建时间
     */
    private LocalDateTime addTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    private Integer deleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getAccount(){
        return account;
    }

    public void setAccount(String account){
        this.account = account;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    public LocalDateTime getAddTime() {
        return addTime;
    }

    public void setAddTime(LocalDateTime addTime) {
        this.addTime = addTime;
    }
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public static final String ID = "id";

    public static final String ADMIN = "admin";

    public static final String IP = "ip";

    public static final String TYPE = "type";

    public static final String ACTION = "action";

    public static final String STATUS = "status";

    public static final String RESULT = "result";

    public static final String COMMENT = "comment";

    public static final String ADD_TIME = "add_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String DELETED = "deleted";

    public static final String ACCOUNT = "account";

    @Override
    public String toString() {
        return "SysLog{" +
        "id=" + id +
        ", admin=" + admin +
        ", ip=" + ip +
        ", type=" + type +
        ", action=" + action +
        ", status=" + status +
        ", result=" + result +
        ", comment=" + comment +
        ", addTime=" + addTime +
        ", updateTime=" + updateTime +
        ", deleted=" + deleted +
        ", account=" + account +
        "}";
    }
}
