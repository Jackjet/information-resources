package d1.project.dcrun.center.webapi.task.dao;

import d1.project.dcrun.center.webapi.task.entity.TaskKettleFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author zhengyang
 */
public interface TaskKettleFilesDao extends JpaRepository<TaskKettleFiles, String>, JpaSpecificationExecutor<TaskKettleFiles> {
    List<TaskKettleFiles> findByTaskId(String taskId);

    List<TaskKettleFiles> findAllByTaskId(String taskId);

    void deleteAllByTaskId(String taskId);

    /**
     * 根据任务ID和类型查找文件
     * @param taskId
     * @param type
     * @return
     */
    TaskKettleFiles findByTaskIdAndType(String taskId, String type);

    /**
     * 根据任务ID和名称查找文件
     * @param taskId
     * @param name
     * @return
     */
    TaskKettleFiles findByTaskIdAndName(String taskId, String name);

    /**
     * 根据任务ID查询文件数量
     * @param taskId
     * @return
     */
    int countAllByTaskId(String taskId);
}
