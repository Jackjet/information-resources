package d1.project.resourcesharingmgmt.resource.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.*;
import java.util.Calendar;

/**
 * 资源使用
 *
 * @author zhengyang
 */
@Entity
@Table(name = "d1_resource_use_info")
@ApiModel(value = "ResourceUseInfoEntity", description = "资源使用表")
public class ResourceUseInfoEntity extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("资源名称")
    private String resourceName;

    @ApiModelProperty("信息资源提供方部门ID")
    @Column(length = 32)
    private String provOrgId;

    @ApiModelProperty("信息资源提供方部门代码")
    @Column(length = 50)
    private String provOrgCode;

    @ApiModelProperty("信息资源提供方部门代码")
    @Column(length = 215)
    private String provOrgName;

    @ApiModelProperty("信息资源id")
    @Column(length = 32)
    private String uviewId;

    @ApiModelProperty("信息资源名称")
    @Column(length = 100)
    private String uviewNm;

    @ApiModelProperty("信息资源代码")
    @Column(length = 100)
    private String uviewNo;

    @ApiModelProperty("更新周期")
    @Column(length = 100)
    private String updateCyc;

    @ApiModelProperty("信息资源摘要")
    @Column(columnDefinition = "TEXT")
    private String uviewDesc;

    @ApiModelProperty("共享类型,01无条件共享，02有条件共享，03不予共享")
    @Column(length = 100)
    private String shareLv;

    @ApiModelProperty("资源目录和云接口关联表ID")
    @Column(length = 32)
    private String uviewApiId;

    @ApiModelProperty("云接口名称")
    @Column(length = 50)
    private String sourceApiName;

    @ApiModelProperty("云接口描述")
    @Column(columnDefinition = "TEXT")
    private String sourceApiDesc;

    @ApiModelProperty("资源用途")
    @Column(length = 50)
    private String resourceUse;

    @ApiModelProperty("支持业务")
    @Column(length = 200)
    private String supportBusiness;

    @ApiModelProperty("使用期限开始时间")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar useStartTime;

    @ApiModelProperty("使用期限结束时间")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar useEndTime;

    @ApiModelProperty("说明")
    @Column(columnDefinition = "TEXT")
    private String instructions;

    @ApiModelProperty("联系人")
    @Column(length = 50)
    private String contacts;

    @ApiModelProperty("联系电话")
    @Column(length = 20)
    private String mobilePhone;

    @ApiModelProperty("电子邮件")
    @Column(length = 50)
    private String email;

    @ApiModelProperty("申请依据")
    @Column(columnDefinition = "TEXT")
    private String fileUrl;

    @ApiModelProperty("验证方式,默认Key-Auth,审批时")
    @Column(length = 50)
    private String verifyMethod;

    @ApiModelProperty("流量策略每分钟,可为空，审批时填写,默认：1000,可修改")
    private Integer perMinute;

    @ApiModelProperty("流量策略每小时,可为空，审批时填写,默认：6000,可修改")
    private Integer everyHour;

    @ApiModelProperty("流量策略每天,可为空，审批时填写,默认：10000,可修改")
    private Integer everyDay;

    @ApiModelProperty("流量策略每次,可为空，审批时填写,默认：1,可修改")
    private Integer singleSize;

    @ApiModelProperty("审核说明,可为空，审批时填写")
    @Column(columnDefinition = "TEXT")
    private String auditDesc;

    /**
     * 审核状态,0未审核1初审通过2已审核3已驳回4审核失败
     */
    @ApiModelProperty("审核状态,0未审核1初审通过2已审核3已驳回4审核失败")
    @Column(length = 4)
    private String status;

    /**
     * 驳回原因
     */
    @ApiModelProperty("驳回原因")
    private String rejectDetail;

    @ApiModelProperty("提交人部门id")
    @Column(length = 32)
    private String createDeptId;

    @ApiModelProperty("提交人部门名称")
    @Column(length = 100)
    private String createDeptName;

    @ApiModelProperty("审核错误信息,可为空")
    @Column(columnDefinition = "TEXT")
    private String msg;

    @ApiModelProperty("申请类型,1接口2文件3数据库")
    @Column(length = 4)
    private String type;

    /**
     * 名称
     */
    private String name;
    /**
     * Host
     */
    private String host;

    /**
     * Path
     */
    private String path;
    /**
     * 请求协议
     */
    private String protocol;
    /**
     * 请求方式
     */
    private String method;
    /**
     * 内容格式类型
     */
    private String formatType;
    /**
     * body
     */
    @Lob
    @Column(columnDefinition = "TEXT")
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    private String body;
    /**
     * 入参内容
     */
    @Lob
    @Column(columnDefinition = "TEXT")
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    private String params;
    /**
     * 返回值
     */
    @Lob
    @Column(columnDefinition = "TEXT")
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    private String response;
    /**
     * 常量
     */
    @Lob
    @Column(columnDefinition = "TEXT")
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    private String constants;
    /**
     * apiId
     */
    private String apiId;

    /**
     * 容器
     */
    private String container;

    /**
     * 路由信息
     */
    private String routeInfo;

    @ApiModelProperty("KONG_API_ID")
    private String kongApiId;

    /**
     * dataId
     */
    private String dataId;

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
    @ApiModelProperty("云数据描述")
    private String dataDetail;


    /**
     * fileId
     */
    private String fileId;

    /**
     * 文件名称
     */
    @ApiModelProperty("文件名称")
    private String fileName;

    /**
     * 文件下载地址
     */
    @ApiModelProperty("文件下载地址")
    @Column(columnDefinition = "TEXT")
    private String fileDownloadUri;

    /**
     * 文件类型
     */
    @ApiModelProperty("文件类型")
    private String fileType;

    /**
     * 云文件描述
     */
    @ApiModelProperty("云文件描述")
    private String fileDetail;

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getProvOrgId() {
        return provOrgId;
    }

    public void setProvOrgId(String provOrgId) {
        this.provOrgId = provOrgId;
    }

    public String getProvOrgCode() {
        return provOrgCode;
    }

    public void setProvOrgCode(String provOrgCode) {
        this.provOrgCode = provOrgCode;
    }

    public String getProvOrgName() {
        return provOrgName;
    }

    public void setProvOrgName(String provOrgName) {
        this.provOrgName = provOrgName;
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

    public String getUpdateCyc() {
        return updateCyc;
    }

    public void setUpdateCyc(String updateCyc) {
        this.updateCyc = updateCyc;
    }

    public String getUviewDesc() {
        return uviewDesc;
    }

    public void setUviewDesc(String uviewDesc) {
        this.uviewDesc = uviewDesc;
    }

    public String getShareLv() {
        return shareLv;
    }

    public void setShareLv(String shareLv) {
        this.shareLv = shareLv;
    }

    public String getUviewApiId() {
        return uviewApiId;
    }

    public void setUviewApiId(String uviewApiId) {
        this.uviewApiId = uviewApiId;
    }

    public String getSourceApiName() {
        return sourceApiName;
    }

    public void setSourceApiName(String sourceApiName) {
        this.sourceApiName = sourceApiName;
    }

    public String getSourceApiDesc() {
        return sourceApiDesc;
    }

    public void setSourceApiDesc(String sourceApiDesc) {
        this.sourceApiDesc = sourceApiDesc;
    }

    public String getResourceUse() {
        return resourceUse;
    }

    public void setResourceUse(String resourceUse) {
        this.resourceUse = resourceUse;
    }

    public String getSupportBusiness() {
        return supportBusiness;
    }

    public void setSupportBusiness(String supportBusiness) {
        this.supportBusiness = supportBusiness;
    }

    public Calendar getUseStartTime() {
        return useStartTime;
    }

    public void setUseStartTime(Calendar useStartTime) {
        this.useStartTime = useStartTime;
    }

    public Calendar getUseEndTime() {
        return useEndTime;
    }

    public void setUseEndTime(Calendar useEndTime) {
        this.useEndTime = useEndTime;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getVerifyMethod() {
        return verifyMethod;
    }

    public void setVerifyMethod(String verifyMethod) {
        this.verifyMethod = verifyMethod;
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

    public String getRejectDetail() {
        return rejectDetail;
    }

    public void setRejectDetail(String rejectDetail) {
        this.rejectDetail = rejectDetail;
    }

    public String getCreateDeptId() {
        return createDeptId;
    }

    public void setCreateDeptId(String createDeptId) {
        this.createDeptId = createDeptId;
    }

    public String getCreateDeptName() {
        return createDeptName;
    }

    public void setCreateDeptName(String createDeptName) {
        this.createDeptName = createDeptName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getFormatType() {
        return formatType;
    }

    public void setFormatType(String formatType) {
        this.formatType = formatType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getConstants() {
        return constants;
    }

    public void setConstants(String constants) {
        this.constants = constants;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getRouteInfo() {
        return routeInfo;
    }

    public void setRouteInfo(String routeInfo) {
        this.routeInfo = routeInfo;
    }

    public String getKongApiId() {
        return kongApiId;
    }

    public void setKongApiId(String kongApiId) {
        this.kongApiId = kongApiId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
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

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public String getFileDetail() {
        return fileDetail;
    }

    public void setFileDetail(String fileDetail) {
        this.fileDetail = fileDetail;
    }
}
