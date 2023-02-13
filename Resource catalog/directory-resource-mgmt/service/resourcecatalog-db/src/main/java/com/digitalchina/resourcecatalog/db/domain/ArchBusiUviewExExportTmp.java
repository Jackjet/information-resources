package com.digitalchina.resourcecatalog.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 信息资源目录导入临时
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
public class ArchBusiUviewExExportTmp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    /**
     * 信息资源名称
     */
    @NotNull(message = "信息资源名称不能为空")
    @ApiModelProperty("信息资源名称")
    private String uviewNm;

    /**
     * 信息资源提供方部门ID,关联 表ID
     */
    @NotNull(message = "部门ID不能为空")

    private Integer deptId;

    /**
     * 信息资源提供方部门代码
     */
    @NotNull(message = "部门名称不能为空")
    private String deptName;


    @ApiModelProperty("市级目录编码")
    private String cityCataCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUviewNm() {
        return uviewNm;
    }

    public void setUviewNm(String uviewNm) {
        this.uviewNm = uviewNm;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getCityCataCode() {
        return cityCataCode;
    }

    public void setCityCataCode(String cityCataCode) {
        this.cityCataCode = cityCataCode;
    }

    @Override
    public String toString() {
        return "ArchBusiUviewExExportTmp{" +
                "id=" + id +
                ", uviewNm='" + uviewNm + '\'' +
                ", deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                ", cityCataCode='" + cityCataCode + '\'' +
                '}';
    }
}
