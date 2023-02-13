package com.digitalchina.resourcecatalog.db.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * MqMessage
 *
 * @author baokd
 **/
public class MqMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Long id;

    private String messageGuid;

    private String message;

    private String messageStatus;

    private int messageCode;

    private Date createTime;

    public Long getId() {
		return id;
	}

	public String getMessageGuid() {
		return messageGuid;
	}

	public String getMessage() {
		return message;
	}

	public String getMessageStatus() {
		return messageStatus;
	}

	public int getMessageCode() {
		return messageCode;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setMessageGuid(String messageGuid) {
		this.messageGuid = messageGuid;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMessageStatus(String messageStatus) {
		this.messageStatus = messageStatus;
	}

	public void setMessageCode(int messageCode) {
		this.messageCode = messageCode;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public MqMessage(String messageGuid, int messageCode, String messageStatus, String message) {
        this.messageGuid = messageGuid;
        this.messageCode = messageCode;
        this.messageStatus = messageStatus;
        this.message = message;

    }

}
