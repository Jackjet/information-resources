package d1.project.dataintegration.task.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author zhengyang
 */
@Entity
@Table(name = "d1_task_kettle_files")
@ApiModel(value = "TaskKettleFiles", description = "数据集成任务")
public class TaskKettleFiles extends BaseEntity {
    @ApiModelProperty("任务ID")
    @Column(length = 32)
    private String taskId;

    @ApiModelProperty("文件名")
    @Column(length = 50)
    private String name;

    @ApiModelProperty("类型(ktr:转换,kjb:作业)")
    @Column(length = 10)
    private String type;

    @ApiModelProperty("xml地址")
    @Column(length = 200)
    private String xmlUrl;

    @ApiModelProperty("文件地址（ktr或者kjb文件路径）")
    @Column(length = 200)
    private String fileUrl;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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

    public String getXmlUrl() {
        return xmlUrl;
    }

    public void setXmlUrl(String xmlUrl) {
        this.xmlUrl = xmlUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
