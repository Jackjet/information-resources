package com.digitalchina.resourcecatalog.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 信息资源目录审核记录表
 * </p>
 *
 * @author baokd
 * @since 2020-05-15
 */
public class CataInfoApprove implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 信息资源ID
     */
    private Integer infoId;

    /**
     * 状态 0-审核不通过 1-审核通过 
     */
    private String status;

    /**
     * 审核意见
     */
    private String comment;

    /**
     * 审核人ID
     */
    private Integer checkById;

    /**
     * 审核人
     */
    private String checkByName;

    /**
     * 审核版本
     */
    private String version;

    /**
     * 审核时间
     */
    private LocalDateTime checkTime;
    /**
     * 操作类型 提交 修改（非草稿、非待初审） 初审 终审
     */
    private String optType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getInfoId() {
        return infoId;
    }

    public void setInfoId(Integer infoId) {
        this.infoId = infoId;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    public Integer getCheckById() {
        return checkById;
    }

    public void setCheckById(Integer checkById) {
        this.checkById = checkById;
    }
    public String getCheckByName() {
        return checkByName;
    }

    public void setCheckByName(String checkByName) {
        this.checkByName = checkByName;
    }
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    public LocalDateTime getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(LocalDateTime checkTime) {
        this.checkTime = checkTime;
    }

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    public static final String ID = "id";

    public static final String INFO_ID = "info_id";

    public static final String STATUS = "status";

    public static final String COMMENT = "comment";

    public static final String CHECK_BY_ID = "check_by_id";

    public static final String CHECK_BY_NAME = "check_by_name";

    public static final String VERSION = "version";

    public static final String CHECK_TIME = "check_time";

    public static final String OPT_TYPE = "opt_type";

    @Override
    public String toString() {
        return "CataInfoApprove{" +
                "id=" + id +
                ", infoId=" + infoId +
                ", status='" + status + '\'' +
                ", comment='" + comment + '\'' +
                ", checkById=" + checkById +
                ", checkByName='" + checkByName + '\'' +
                ", version='" + version + '\'' +
                ", checkTime=" + checkTime +
                ", optType='" + optType + '\'' +
                '}';
    }

    public static void addCataInfoApprove(String optType, List<CataInfoApprove> cataInfoApproveList, Integer uviewId, String status, Integer userId, String userName, String comment){
        LocalDateTime localDateTime = LocalDateTime.now();
        CataInfoApprove cataInfoApprove = new CataInfoApprove();
        cataInfoApprove.setCheckById(userId);
        cataInfoApprove.setCheckByName(userName);
        cataInfoApprove.setCheckTime(localDateTime);
        cataInfoApprove.setComment(comment);
        cataInfoApprove.setInfoId(uviewId);
        cataInfoApprove.setStatus(status);
        cataInfoApprove.setOptType(optType);
        cataInfoApproveList.add(cataInfoApprove);
    }
}
