package d1.project.dcrun.center.webapi.task.dao;

import d1.project.dcrun.center.webapi.task.entity.TaskEntityLog;
import d1.project.dcrun.center.webapi.task.model.TaskLogsVm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @author baozh
 */
public interface TaskEntityLogDao extends JpaRepository<TaskEntityLog, String>, JpaSpecificationExecutor<TaskEntityLog> {
    @Query("SELECT new d1.project.dcrun.center.webapi.task.model.TaskLogsVm(taskId,status,MIN(createTime),MIN(createTime),MAX(createTime),COALESCE(SUM(writeNum),0),COALESCE(SUM(inputNum),0),COALESCE(SUM(outputNum),0),COALESCE(SUM(readNum),0),COALESCE(SUM(writeNum),0),COALESCE(SUM(updateNum),0),COALESCE(SUM(errorNum),0)) FROM TaskEntityLog WHERE taskId = ?1 AND channelId = ?2 AND groupId = ?3 group by taskId,status")
    TaskLogsVm findByTaskIdAndChannelIdAndGroupId(String taskId, String channelId, String groupId);
}
