package d1.project.d1project.business.dao;

import d1.project.d1project.business.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

public interface TaskDao extends JpaRepository<Task, String>, JpaSpecificationExecutor<Task> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, String id);

    int countByCreateTimeBetween(Calendar startTime, Calendar endTime);


    /**
     * 修改最后执行时间
     *
     * @param lastTime 最后执行时间
     * @param id       task id
     */
    @Transactional
    @Modifying
    @Query(value = "update d1_task set last_time =?1 where id =?2 ", nativeQuery = true)
    void updateLastTime(Calendar lastTime, String id);

    boolean existsByGroupId(String groupId);
}
