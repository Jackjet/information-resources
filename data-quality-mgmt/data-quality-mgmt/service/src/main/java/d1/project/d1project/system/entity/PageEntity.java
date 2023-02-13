package d1.project.d1project.system.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "d1_page")
@ApiModel(value = "PageEntity", description = "页面")
public class PageEntity extends BaseCreateAndUpdateEntity {

    @ApiModelProperty("页面名称")
    private String name;

    @ApiModelProperty("路由地址")
    private String route;

    @ApiModelProperty("文件路径")
    private String path;

    @ApiModelProperty("页面扩展数据")
    private String metaData;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }
}
