package d1.project.dcrun.center.webapi.task.model.vm;

/**
 * @author zhengyang
 */
public class TaskEntityLogInsertVm {
    private String taskId;
    private String channelId;
    private String groupId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
