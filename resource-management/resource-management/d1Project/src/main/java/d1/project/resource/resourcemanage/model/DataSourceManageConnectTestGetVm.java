package d1.project.resource.resourcemanage.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "DataSourceManageConnectTestGetVm", description = "数据源连接测试")
public class DataSourceManageConnectTestGetVm {
    @NotBlank(message = "数据库类型不能为空")
    @ApiModelProperty("类型")
    private String type;
    @NotBlank(message = "IP不能为空")
    @ApiModelProperty("ip")
    private String ip;
    @NotBlank(message = "端口不能为空")
    @ApiModelProperty("端口")
    private String port;
    @NotBlank(message = "数据库名称不能为空")
    @ApiModelProperty("数据库名称")
    private String dataBaseName;
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty("用户名")
    private String username;
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("ServiceName,仅Oracle用")
    private String serviceName;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDataBaseName() {
        return dataBaseName;
    }

    public void setDataBaseName(String dataBaseName) {
        this.dataBaseName = dataBaseName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
