package d1.project.d1project.business.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author libin
 */
@Entity
@Table(name = "d1_data_unit_field")
@ApiModel(value = "DataUnitField", description = "元数据字段")
public class DataUnitField extends BaseEntity {

    @ApiModelProperty("字段名称")
    private String columnName;

    @ApiModelProperty("主键")
    private String columnKey;

    @ApiModelProperty("描述")
    private String columnComment;

    @ApiModelProperty("允许为空")
    private String isNullable;

    @ApiModelProperty("字段类型")
    private String columnType;

    @ApiModelProperty("元数据id")
    private String dataUnitId;

    @ApiModelProperty("数据源id")
    private String dataSourceId;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getDataUnitId() {
        return dataUnitId;
    }

    public void setDataUnitId(String dataUnitId) {
        this.dataUnitId = dataUnitId;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }
}
