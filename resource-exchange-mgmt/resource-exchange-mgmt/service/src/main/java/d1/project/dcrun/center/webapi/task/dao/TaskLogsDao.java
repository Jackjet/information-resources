package d1.project.dcrun.center.webapi.task.dao;

import d1.project.dcrun.center.webapi.task.entity.TaskLogs;
import d1.project.dcrun.center.webapi.task.model.TaskAnalysisVm;
import d1.project.dcrun.center.webapi.task.model.TaskLogsAnalysisVm;
import d1.project.dcrun.center.webapi.task.model.TaskLogsInfoVm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @author zhengyang
 */
public interface TaskLogsDao extends JpaRepository<TaskLogs, String>, JpaSpecificationExecutor<TaskLogs> {
    @Query("SELECT new d1.project.dcrun.center.webapi.task.model.TaskLogsInfoVm(count(id),COALESCE(sum(CASE WHEN status=0 THEN 1 ELSE 0 END),0),COALESCE(sum(CASE WHEN status=1 THEN 1 ELSE 0 END),0)) FROM TaskLogs WHERE taskId =?1 ")
    TaskLogsInfoVm findByTaskLogsInfo(String taskId);

    @Query("SELECT new d1.project.dcrun.center.webapi.task.model.TaskLogsAnalysisVm(count(id),COALESCE(sum(CASE WHEN status=0 THEN 1 ELSE 0 END),0),COALESCE(sum(dataSum),0)) FROM TaskLogs ")
    TaskLogsAnalysisVm findByTaskLogsAnalysis();

    @Query("SELECT new d1.project.dcrun.center.webapi.task.model.TaskLogsAnalysisVm(count(id),COALESCE(sum(CASE WHEN status=0 THEN 1 ELSE 0 END),0),COALESCE(sum(dataSum),0)) FROM TaskLogs WHERE mode =?1 ")
    TaskLogsAnalysisVm findByTaskLogsAnalysis(int mode);

    @Query("SELECT new d1.project.dcrun.center.webapi.task.model.TaskAnalysisVm(COALESCE(sum(CASE WHEN mode=1 THEN 1 ELSE 0 END),0),COALESCE(sum(CASE WHEN mode=1 THEN dataSum ELSE 0 END),0),COALESCE(sum(CASE WHEN mode=2 THEN dataSum ELSE 0 END),0),COALESCE(sum(CASE WHEN status=0 THEN 1 ELSE 0 END),0),COALESCE(sum(CASE WHEN status=1 THEN 1 ELSE 0 END),0)) FROM TaskLogs WHERE to_char(runTime,'YYYY-MM-DD') =?1 ")
    TaskAnalysisVm findByTaskAnalysisByDay(String day);

    @Query("SELECT new d1.project.dcrun.center.webapi.task.model.TaskAnalysisVm(COALESCE(sum(CASE WHEN mode=1 THEN 1 ELSE 0 END),0),COALESCE(sum(CASE WHEN mode=1 THEN dataSum ELSE 0 END),0),COALESCE(sum(CASE WHEN mode=2 THEN dataSum ELSE 0 END),0),COALESCE(sum(CASE WHEN status=0 THEN 1 ELSE 0 END),0),COALESCE(sum(CASE WHEN status=1 THEN 1 ELSE 0 END),0)) FROM TaskLogs WHERE to_char(runTime,'YYYY-MM') =?1 ")
    TaskAnalysisVm findByTaskAnalysisByMonth(String month);
}
