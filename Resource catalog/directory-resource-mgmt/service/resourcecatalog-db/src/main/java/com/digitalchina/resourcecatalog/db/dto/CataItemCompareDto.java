package com.digitalchina.resourcecatalog.db.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class CataItemCompareDto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * 序号
     */
    @ApiModelProperty("序号")
    private Integer number;
    
    /**
     * 信息项标识
     */
    @ApiModelProperty("信息项标识")
    private String code;
    
    /**
     * 信息项类型
     */
    @ApiModelProperty("信息项类型")
    private String type;
    
    /**
     * 当前版本
     */
    @ApiModelProperty("当前版本")
    private String currentVersion;
    
    /**
     * 比较版本
     */
    @ApiModelProperty("比较版本")
    private String compareVersion;

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}

	public String getCompareVersion() {
		return compareVersion;
	}

	public void setCompareVersion(String compareVersion) {
		this.compareVersion = compareVersion;
	}
    
}
