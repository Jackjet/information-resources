package d1.project.dataintegration.task.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author zhengyang
 */
@Entity
@Table(name = "d1_task")
@ApiModel(value = "Task", description = "数据集成任务")
public class Task extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("任务名称")
    @Column(length = 100)
    private String name;

    @ApiModelProperty("所属资源组")
    @Column(length = 32)
    private String groupId;

    @ApiModelProperty("运行状态：0未启动，1已启动")
    @Column(length = 1)
    private int status;

    @ApiModelProperty("集成模式：0未知，1定时，2实时")
    @Column(length = 1)
    private int mode;

    @ApiModelProperty("容器")
    @Column(columnDefinition = "TEXT")
    private String container;

    @ApiModelProperty("起始数据源")
    @Column(length = 32)
    private String startDataSource;

    @ApiModelProperty("目标数据源")
    @Column(length = 32)
    private String endDataSource;

    @ApiModelProperty("重复：Y是，N否")
    @Column(length = 1)
    private String repeats;

    @ApiModelProperty("调度周期：0不需要定时，1时间，2天，3周，4月")
    @Column(length = 2)
    private String schedulerType;

    @ApiModelProperty("以分计算时间间隔，当调度周期为1(时间)")
    @Column(length = 3)
    private String intervalMinutes;

    @ApiModelProperty("以秒计算时间间隔，当调度周期为1(时间)")
    @Column(length = 3)
    private String intervalSeconds;

    @ApiModelProperty("每天几点，当调度周期为2/3/4(天/周/月)")
    @Column(length = 3)
    private String dayOfHour;

    @ApiModelProperty("每天几分，当调度周期为2/3/4(天/周/月)")
    @Column(length = 3)
    private String dayOfMinutes;

    @ApiModelProperty("星期几，当调度周期为3(周)")
    @Column(length = 3)
    private String weekDay;

    @ApiModelProperty("几号，当调度周期为4(月)")
    @Column(length = 3)
    private String dayOfMonth;

    @ApiModelProperty("标签KEY列表")
    @Column(columnDefinition = "TEXT")
    private String metaKey;

    @ApiModelProperty("标签Value列表")
    @Column(columnDefinition = "TEXT")
    private String metaValue;

    @ApiModelProperty("描述")
    @Column(columnDefinition = "TEXT")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getStartDataSource() {
        return startDataSource;
    }

    public void setStartDataSource(String startDataSource) {
        this.startDataSource = startDataSource;
    }

    public String getEndDataSource() {
        return endDataSource;
    }

    public void setEndDataSource(String endDataSource) {
        this.endDataSource = endDataSource;
    }

    public String getRepeats() {
        return repeats;
    }

    public void setRepeats(String repeats) {
        this.repeats = repeats;
    }

    public String getSchedulerType() {
        return schedulerType;
    }

    public void setSchedulerType(String schedulerType) {
        this.schedulerType = schedulerType;
    }

    public String getIntervalMinutes() {
        return intervalMinutes;
    }

    public void setIntervalMinutes(String intervalMinutes) {
        this.intervalMinutes = intervalMinutes;
    }

    public String getIntervalSeconds() {
        return intervalSeconds;
    }

    public void setIntervalSeconds(String intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
    }

    public String getDayOfHour() {
        return dayOfHour;
    }

    public void setDayOfHour(String dayOfHour) {
        this.dayOfHour = dayOfHour;
    }

    public String getDayOfMinutes() {
        return dayOfMinutes;
    }

    public void setDayOfMinutes(String dayOfMinutes) {
        this.dayOfMinutes = dayOfMinutes;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getMetaKey() {
        return metaKey;
    }

    public void setMetaKey(String metaKey) {
        this.metaKey = metaKey;
    }

    public String getMetaValue() {
        return metaValue;
    }

    public void setMetaValue(String metaValue) {
        this.metaValue = metaValue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
