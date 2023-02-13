package d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo;

import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author maoyuying
 */
public class ResourceUseInfoUpdateVm {
    @ApiModelProperty("id")
    @NotBlank(message = "id不可为空")
    private String id;

    @ApiModelProperty("支持业务")
    private String supportBusiness;

    @ApiModelProperty("流量策略每分钟,可为空，审批时填写,默认：1000,可修改")
    private Integer perMinute;

    @ApiModelProperty("每小时,可为空，审批时填写,默认：6000,可修改")
    private Integer everyHour;

    @ApiModelProperty("每天,可为空，审批时填写,默认：10000,可修改")
    private Integer everyDay;

    @ApiModelProperty("每次,可为空，审批时填写,默认：1,可修改")
    private Integer singleSize;

    @ApiModelProperty("审核说明,可为空，审批时填写")
    private String auditDesc;

    @ApiModelProperty("审核状态,1已审核2已驳回")
    @NotBlank(message = "审核状态不可为空")
    private String status;

    /**
     * ip
     */
    @ApiModelProperty("ip")
    private String ip;

    /**
     * 端口
     */
    @ApiModelProperty("端口")
    private String port;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * 数据库名
     */
    @ApiModelProperty("数据库名")
    private String databaseName;

    /**
     * 表名
     */
    @ApiModelProperty("表名")
    private String tableName;

    /**
     * 云数据描述
     */
    @ApiModelProperty("描述")
    private String dataDetail;

    /**
     * 驳回原因
     */
    @ApiModelProperty("驳回原因")
    private String rejectDetail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSupportBusiness() {
        return supportBusiness;
    }

    public void setSupportBusiness(String supportBusiness) {
        this.supportBusiness = supportBusiness;
    }

    public Integer getPerMinute() {
        return perMinute;
    }

    public void setPerMinute(Integer perMinute) {
        this.perMinute = perMinute;
    }

    public Integer getEveryHour() {
        return everyHour;
    }

    public void setEveryHour(Integer everyHour) {
        this.everyHour = everyHour;
    }

    public Integer getEveryDay() {
        return everyDay;
    }

    public void setEveryDay(Integer everyDay) {
        this.everyDay = everyDay;
    }

    public Integer getSingleSize() {
        return singleSize;
    }

    public void setSingleSize(Integer singleSize) {
        this.singleSize = singleSize;
    }

    public String getAuditDesc() {
        return auditDesc;
    }

    public void setAuditDesc(String auditDesc) {
        this.auditDesc = auditDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDataDetail() {
        return dataDetail;
    }

    public void setDataDetail(String dataDetail) {
        this.dataDetail = dataDetail;
    }

    public String getRejectDetail() {
        return rejectDetail;
    }

    public void setRejectDetail(String rejectDetail) {
        this.rejectDetail = rejectDetail;
    }
}
