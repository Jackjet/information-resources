package d1.project.dcrun.center.webapi.task.dao;

import d1.project.dcrun.center.webapi.task.entity.Task;
import d1.project.dcrun.center.webapi.task.model.TaskInfoForContainerVm;
import d1.project.dcrun.center.webapi.task.model.TaskListForContainerVm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @author zhengyang
 */
public interface TaskDao extends JpaRepository<Task, String>, JpaSpecificationExecutor<Task> {
    /**
     * 根据数据集成信息ID查询是否存在
     *
     * @param groupId 组ID
     * @return 查询结果
     */
    boolean existsAllByGroupId(String groupId);

    @Query("SELECT new d1.project.dcrun.center.webapi.task.model.TaskInfoForContainerVm(count(id),COALESCE(sum(CASE WHEN mode=1 THEN 1 ELSE 0 END),0),COALESCE(sum(CASE WHEN mode=2 THEN 1 ELSE 0 END),0),COALESCE(sum(CASE WHEN status=1 THEN 1 ELSE 0 END),0),COALESCE(sum(CASE WHEN status=0 THEN 1 ELSE 0 END),0)) FROM Task WHERE container like CONCAT('%',?1,'%') and container like CONCAT('%',?2,'%')")
    TaskInfoForContainerVm findTaskInfoForContainer(String ip, String port);

    @Query("SELECT new d1.project.dcrun.center.webapi.task.model.TaskListForContainerVm(task.name,task.status,task.mode,count(logs.id),COALESCE(sum(CASE WHEN logs.status=0 THEN 1 ELSE 0 END),0),COALESCE(sum(CASE WHEN logs.status=1 THEN 1 ELSE 0 END),0)) FROM Task as task join TaskLogs as logs on task.id=logs.taskId WHERE task.name like CONCAT('%',?1,'%') and task.container like CONCAT('%',?2,'%') and task.container like CONCAT('%',?3,'%') group by task.id")
    Page<TaskListForContainerVm> findTaskListForContainer(String name, String ip, String port, Pageable pageable);

    /**
     * 根据mode查询任务数
     * @param mode
     * @return
     */
    long countByMode(int mode);

    /**
     * 根据数据容器ID查询是否存在
     *
     * @param containerId 容器ID
     * @return 查询结果
     */
    boolean existsAllByContainer(String containerId);

    /**
     * 根据任务名称和组id查询是否有相同数据
     * @param name
     * @param groupId
     * @return
     */
    boolean existsAllByNameAndGroupId(String name, String groupId);

    /**
     * 根据任务名称和组id排除ID查询是否有相同数据
     * @param name
     * @param groupId
     * @param id
     * @return
     */
    boolean existsAllByNameAndGroupIdAndIdNot(String name, String groupId, String id);
}
