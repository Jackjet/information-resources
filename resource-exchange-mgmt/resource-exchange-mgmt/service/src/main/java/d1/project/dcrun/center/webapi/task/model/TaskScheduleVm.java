package d1.project.dcrun.center.webapi.task.model;



import io.swagger.annotations.ApiModelProperty;


/**
 * API设计信息
 *
 * @author zhengyang
 */
public class TaskScheduleVm{
    @ApiModelProperty("集成模式：0未知，1定时，2实时")
    private int mode;

    @ApiModelProperty("重复：Y是，N否")
    private String repeats;

    @ApiModelProperty("调度周期：0不需要定时，1时间，2天，3周，4月")
    private String schedulerType;

    @ApiModelProperty("以秒计算时间间隔，当调度周期为1(时间)")
    private String intervalMinutes;

    @ApiModelProperty("以分计算时间间隔，当调度周期为1(时间)")
    private String intervalSeconds;

    @ApiModelProperty("每天几点，当调度周期为2、3、4(天/周/月)")
    private String dayOfHour;

    @ApiModelProperty("每天几分，当调度周期为2和3、4(天/周/月)")
    private String dayOfMinutes;

    @ApiModelProperty("星期几，当调度周期为2和3(天/周)")
    private String weekDay;

    @ApiModelProperty("几号，当调度周期为4(月)")
    private String dayOfMonth;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
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
}
