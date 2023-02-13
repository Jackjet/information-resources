package d1.project.tangshan.operation.manage.entity;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Buter
 * @date 2020/3/20 14:40
 */
@Entity
@Table(name = "d1_big_data_api")
@ApiModel(value = "BigDataApiEntity", description = "数据服务接口管理")
public class BigDataApiEntity extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("接口名称")
    private String name;

    @ApiModelProperty("接口类型，http/webservice/socket")
    private String type;

    @ApiModelProperty("url/Ip")
    private String urlOrIp;

    @ApiModelProperty("方法类型，get/post/put/delete，当接口类型为socket时，方法类型为空")
    private String methodType;

    @ApiModelProperty("备注")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrlOrIp() {
        return urlOrIp;
    }

    public void setUrlOrIp(String urlOrIp) {
        this.urlOrIp = urlOrIp;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
