package com.digitalchina.resourcecatalog.db.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.digitalchina.resourcecatalog.db.domain.CataInfoItemTempRel;
import org.springframework.format.annotation.DateTimeFormat;

import com.digitalchina.resourcecatalog.db.domain.CataInfoTempTypeRel;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 信息资源目录报告导出实体
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
public class CataInfoReportDto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String typNm;
    
    private List<CataInfoDto> infoList;

	public String getTypNm() {
		return typNm;
	}

	public List<CataInfoDto> getInfoList() {
		return infoList;
	}

	public void setTypNm(String typNm) {
		this.typNm = typNm;
	}

	public void setInfoList(List<CataInfoDto> infoList) {
		this.infoList = infoList;
	}
    
}
