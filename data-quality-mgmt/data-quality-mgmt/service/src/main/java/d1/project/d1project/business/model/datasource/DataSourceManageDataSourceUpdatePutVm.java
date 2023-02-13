package d1.project.d1project.business.model.datasource;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "DataSourceManageDataSourceUpdatePutVm", description = "修改数据源")
public class DataSourceManageDataSourceUpdatePutVm {
    @NotBlank(message = "id不能为空")
    @ApiModelProperty("id")
    private String id;
    @NotBlank(message = "数据源不能为空")
    @ApiModelProperty("名称")
    private String name;
    @NotBlank(message = "请选择数据源类型")
    @ApiModelProperty("类型")
    private String type;
    @NotBlank(message = "数据库ip不能为空")
    @ApiModelProperty("ip")
    private String ip;
    @NotBlank(message = "数据库端口号不能为空")
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
    @ApiModelProperty("分组")
    private String groupId;
    @ApiModelProperty("描述")
    private String description;
    @NotBlank(message = "请选择部门")
    @ApiModelProperty("部门")
    private String deptId;
    @ApiModelProperty("ServiceName,仅Oracle用")
    private String serviceName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
