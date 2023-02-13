package d1.project.resourcesharingmgmt.resource.model.AssetApiEx;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author zhengyang
 */
@ApiModel(value = "AssetExUpdateVm", description = "编辑挂接申请")
public class AssetExUpdateVm {
    /**
     * id
     */
    @ApiModelProperty("id")
    @NotBlank(message = "ID不可为空")
    private String id;

    /**
     * 信息资源ID
     */
    @ApiModelProperty("信息资源ID")
    @NotBlank(message = "信息资源ID不可为空")
    private String uviewId;

    /**
     * 信息资源名称
     */
    @ApiModelProperty("信息资源名称")
    @NotBlank(message = "信息资源名称不可为空")
    private String uviewNm;

    /**
     * 信息资源代码
     */
    @ApiModelProperty("信息资源代码")
    @NotBlank(message = "信息资源代码不可为空")
    private String uviewNo;

    /**
     * 组织机构ID
     */
    @ApiModelProperty("组织机构ID")
    private Integer orgId;

    /**
     * 组织机构ID
     */
    @ApiModelProperty("组织机构名称")
    private String orgName;

    /**
     * 信息资源摘要
     */
    @ApiModelProperty("信息资源摘要")
    private String uviewDesc;

    /**
     * 云文件名称
     */
    @ApiModelProperty("云文件名称")
    private String name;

    /**
     * 云文件下载地址
     */
    @ApiModelProperty("云文件下载地址")
    private String fileDownloadUri;

    /**
     * 云文件类型
     */
    @ApiModelProperty("云文件类型")
    private String fileType;

    /**
     * 云数据ip
     */
    @ApiModelProperty("云数据ip")
    private String ip;

    /**
     * 云数据端口
     */
    @ApiModelProperty("云数据端口")
    private String port;

    /**
     * 云数据用户名
     */
    @ApiModelProperty("云数据用户名")
    private String username;

    /**
     * 云数据密码
     */
    @ApiModelProperty("云数据密码")
    private String password;

    /**
     * 云数据数据库名
     */
    @ApiModelProperty("云数据数据库名")
    private String databaseName;

    /**
     * 云数据表名
     */
    @ApiModelProperty("云数据表名")
    private String tableName;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String detail;

    /**
     * 类别，0文件，1数据库
     */
    @ApiModelProperty("类别，0文件，1数据库")
    private Integer type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUviewId() {
        return uviewId;
    }

    public void setUviewId(String uviewId) {
        this.uviewId = uviewId;
    }

    public String getUviewNm() {
        return uviewNm;
    }

    public void setUviewNm(String uviewNm) {
        this.uviewNm = uviewNm;
    }

    public String getUviewNo() {
        return uviewNo;
    }

    public void setUviewNo(String uviewNo) {
        this.uviewNo = uviewNo;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getUviewDesc() {
        return uviewDesc;
    }

    public void setUviewDesc(String uviewDesc) {
        this.uviewDesc = uviewDesc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
