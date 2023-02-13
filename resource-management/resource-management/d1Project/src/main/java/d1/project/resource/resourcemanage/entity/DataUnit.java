package d1.project.resource.resourcemanage.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author libin
 */
@Entity
@Table(name = "d1_data_unit")
@ApiModel(value = "DataUnit", description = "元数据")
public class DataUnit extends BaseCreateEntity {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("数据源id")
    private String dataSourceId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }
}
