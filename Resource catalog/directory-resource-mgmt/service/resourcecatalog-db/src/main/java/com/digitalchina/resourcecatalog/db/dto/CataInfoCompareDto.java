package com.digitalchina.resourcecatalog.db.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 信息资源目录历史信息记录
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
public class CataInfoCompareDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前版本对象
     */
    @ApiModelProperty("当前版本对象")
    private CataInfoHistoryDto cataInfoHistoryCurrent;

    /**
     * 对比版本对象
     */
    @ApiModelProperty("对比版本对象")
    private CataInfoHistoryDto cataInfoHistoryCompare;

    /**
     * 对比版本对象
     */
    @ApiModelProperty("信息项对比结果")
    private List<CataItemCompareDto> cataItemCompareList;

	public CataInfoHistoryDto getCataInfoHistoryCurrent() {
		return cataInfoHistoryCurrent;
	}

	public void setCataInfoHistoryCurrent(CataInfoHistoryDto cataInfoHistoryCurrent) {
		this.cataInfoHistoryCurrent = cataInfoHistoryCurrent;
	}

	public CataInfoHistoryDto getCataInfoHistoryCompare() {
		return cataInfoHistoryCompare;
	}

	public void setCataInfoHistoryCompare(CataInfoHistoryDto cataInfoHistoryCompare) {
		this.cataInfoHistoryCompare = cataInfoHistoryCompare;
	}

	public List<CataItemCompareDto> getCataItemCompareList() {
		return cataItemCompareList;
	}

	public void setCataItemCompareList(List<CataItemCompareDto> cataItemCompareList) {
		this.cataItemCompareList = cataItemCompareList;
	}

}
