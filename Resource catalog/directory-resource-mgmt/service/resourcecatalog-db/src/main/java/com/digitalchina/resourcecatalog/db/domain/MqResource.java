package com.digitalchina.resourcecatalog.db.domain;


import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

public class MqResource implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String TYPE = "type";

	public static final String CLIENTID = "client_id";

	@TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String description;

    private String clientId;

    private String userSyncKey;

    private Date createTime;

    private Date updateTime;
    /**
     * 类型 1-机构查询 2-机构新增 3-机构修改 4-机构删除 5-目录分类查询 6-分类新增 7-分类修改 8-分类删除 9-资源目录查询 10-资源目录新增 11-资源目录修改 12-资源目录删除
     */
    private Integer type;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getClientId() {
		return clientId;
	}

	public String getUserSyncKey() {
		return userSyncKey;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setUserSyncKey(String userSyncKey) {
		this.userSyncKey = userSyncKey;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "MqResource [id=" + id + ", name=" + name + ", description=" + description + ", clientId=" + clientId
				+ ", userSyncKey=" + userSyncKey + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}

}