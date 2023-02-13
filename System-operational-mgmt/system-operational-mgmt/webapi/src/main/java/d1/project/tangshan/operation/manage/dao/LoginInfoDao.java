package d1.project.tangshan.operation.manage.dao;

import d1.project.tangshan.operation.manage.entity.LoginInfoEntity;
import d1.project.tangshan.operation.manage.model.DateAndNumber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author Buter
 */
public interface LoginInfoDao extends JpaRepository<LoginInfoEntity, String>, JpaSpecificationExecutor<LoginInfoEntity> {
    /**
     * 获取首次登陆时间
     *
     * @param loginUserId
     * @return
     */
    LoginInfoEntity findFirstByLoginUserIdOrderByFirstLoginTimeAsc(String loginUserId);

    @Query(value = "select new d1.project.tangshan.operation.manage.model.DateAndNumber(to_char(loginTime, 'YYYY-MM-DD') , count(id)) from LoginInfoEntity where loginTime between ?1 and ?2 group by to_char(loginTime, 'YYYY-MM-DD')")
    List<DateAndNumber> countByDay(Calendar startTime, Calendar endTime);

    @Query(value = "select to_char(log.login_time, 'YYYY-MM-DD') as date , count(log.id) as number from (select login_time,id from d1_login_info where (CASE WHEN ?1 =''  THEN 1=1 ELSE name like ('%'|| ?1 || '%') END )  and login_time >= ?2 and login_time<= ?3 ) as log group by to_char(log.login_time, 'YYYY-MM-DD')", nativeQuery = true)
    List<Map<String, Object>> findByDay(String name, Calendar startTime, Calendar endTime);

    @Query(value = "select to_char(log.login_time, 'YYYY-MM') as date , count(log.id) as number from (select login_time,id from d1_login_info where (CASE WHEN ?1 =''  THEN 1=1 ELSE name like ('%'|| ?1 || '%') END )  and login_time >= ?2 and login_time<= ?3 ) as log group by to_char(log.login_time, 'YYYY-MM')", nativeQuery = true)
    List<Map<String, Object>> findByMonth(String name, Calendar startTime, Calendar endTime);

    @Query(value = "select to_char(date_trunc('week', login_time),'YYYY-MM-DD') as date , count(log.id) as number from (select login_time,id from d1_login_info where (CASE WHEN ?1 =''  THEN 1=1 ELSE name like ('%'|| ?1 || '%') END )  and login_time >= ?2 and login_time<= ?3 ) as log group by to_char(date_trunc('week', login_time),'YYYY-MM-DD')", nativeQuery = true)
    List<Map<String, Object>> findByWeek(String name, Calendar startTime, Calendar endTime);

    @Query(value = "select count(distinct name)  from  d1_login_info where  login_time >= ?1 and login_time<= ?2 ", nativeQuery = true)
    Long countUserNumByLoginTime(Calendar startTime, Calendar endTime);

    @Query(value = "select to_char(log.login_time, 'YYYY-MM-DD') as date , count(log.id) as number from (select distinct on(name) * from d1_login_info where login_time >= ?1 and login_time<= ?2 ) as log group by to_char(log.login_time, 'YYYY-MM-DD')", nativeQuery = true)
    List<Map<String, Object>> countLoginNumByDay(Calendar startTime, Calendar endTime);

    @Query(value = "select * from(select * ,row_number() over (partition by name order by login_time desc nulls last) as number from d1_login_info where login_time >= ?1)as temp where number< 2", countQuery = "select count(distinct name)  from d1_login_info where login_time >= ?1 ", nativeQuery = true)
    Page<LoginInfoEntity> findAllDistinctName(Calendar time, Pageable pageable);



}
