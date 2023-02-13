package d1.project.resourcesharingmgmt.resource.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 信息资源目录挂接申请表
 *
 * @author zhengyang
 */
@Entity
@Table(name = "d1_asset_ex")
@ApiModel(value = "AssetExEntity", description = "信息资源目录挂接申请表")
public class AssetExEntity extends BaseCreateEntity {
    @ApiModelProperty("资源名称")
    private String resourceName;

    @ApiModelProperty("信息资源ID")
    @Column(length = 32)
    private String uviewId;

    @ApiModelProperty("信息资源名称")
    @Column(length = 225)
    private String uviewNm;

    @ApiModelProperty("信息资源代码")
    @Column(length = 100)
    private String uviewNo;

    @ApiModelProperty("组织机构ID")
    @Column(length = 10)
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
    @Column(columnDefinition = "TEXT")
    private String uviewDesc;

    @ApiModelProperty("文件名称")
    @Column(length = 100)
    private String name;

    @ApiModelProperty("文件下载地址")
    @Column(columnDefinition = "TEXT")
    private String fileDownloadUri;

    @ApiModelProperty("文件类型")
    private String fileType;

    @ApiModelProperty("ip")
    private String ip;

    @ApiModelProperty("端口")
    private String port;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("数据库名")
    private String databaseName;

    @ApiModelProperty("表名")
    private String tableName;

    @ApiModelProperty("描述")
    @Column(columnDefinition = "TEXT")
    private String detail;

    @ApiModelProperty("类别，0文件，1数据库")
    private Integer type;

    @ApiModelProperty("审批状态，0草稿，1待审核，2审核通过，3审核驳回")
    private Integer status;

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
