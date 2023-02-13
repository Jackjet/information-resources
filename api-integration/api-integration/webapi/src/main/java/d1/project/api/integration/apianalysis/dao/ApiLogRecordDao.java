package d1.project.api.integration.apianalysis.dao;

import d1.project.api.integration.apianalysis.entity.ApiLogRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author baozh
 */
public interface ApiLogRecordDao extends JpaRepository<ApiLogRecord, String>, JpaSpecificationExecutor<ApiLogRecord> {

    /**
     * 根据API ID 按天分组统计数据
     *
     * @param apiId API ID
     * @param st    开始时间
     * @param et    结束时间
     * @return 查询结果
     */
    @Query(value = "SELECT to_char(request_time \\:\\: DATE, 'YYYY-MM-DD') AS date,COUNT(request_time) AS num " +
            "FROM d1_api_log_record WHERE api_id = ?1 AND request_time BETWEEN ?2 AND ?3 GROUP BY to_char(request_time \\:\\: DATE, 'YYYY-MM-DD')  ORDER BY date", nativeQuery = true)
    List<Map<String, Object>> sumByApiIdForDay(String apiId, Calendar st, Calendar et);

    /**
     * 根据API ID 按天分组统计数据
     *
     * @param apiId API ID
     * @param st    开始时间
     * @param et    结束时间
     * @return 查询结果
     */
    @Query(value = "SELECT to_char(request_time \\:\\:TIMESTAMP, 'YYYY-MM-DD HH24:00:00') AS date,COUNT(request_time) AS num " +
            "FROM d1_api_log_record WHERE api_id = ?1 AND request_time BETWEEN ?2 AND ?3 GROUP BY to_char(request_time \\:\\: TIMESTAMP, 'YYYY-MM-DD HH24:00:00')  ORDER BY date", nativeQuery = true)
    List<Map<String, Object>> sumByApiIdForHours(String apiId, Calendar st, Calendar et);


    /**
     * 根据APP ID 按天分组统计数据
     *
     * @param appId APP ID
     * @param st    开始时间
     * @param et    结束时间
     * @return 查询结果
     */
    @Query(value = "SELECT to_char(request_time \\:\\: DATE, 'YYYY-MM-DD') AS date,COUNT(request_time) AS num " +
            "FROM d1_api_log_record WHERE app_id = ?1 AND request_time BETWEEN ?2 AND ?3  GROUP BY to_char(request_time \\:\\: DATE, 'YYYY-MM-DD')  ORDER BY date ", nativeQuery = true)
    List<Map<String, Object>> sumByAppIdForDay(String appId, Calendar st, Calendar et);


    /**
     * 根据APP ID 按天分组统计数据
     *
     * @param appId APP ID
     * @param st    开始时间
     * @param et    结束时间
     * @return 查询结果
     */
    @Query(value = "SELECT to_char(request_time \\:\\: TIMESTAMP, 'YYYY-MM-DD HH24:00:00') AS date,COUNT(request_time) AS num " +
            "FROM d1_api_log_record WHERE app_id = ?1 AND request_time BETWEEN ?2 AND ?3  GROUP BY to_char(request_time \\:\\: TIMESTAMP, 'YYYY-MM-DD HH24:00:00')  ORDER BY date", nativeQuery = true)
    List<Map<String, Object>> sumByAppIdForHours(String appId, Calendar st, Calendar et);

    /**
     * 根据API ID 按天分组统计数据
     *
     * @param apiId API ID
     * @param appId app ID
     * @param st    开始时间
     * @param et    结束时间
     * @return 查询结果
     */
    @Query(value = "SELECT to_char(request_time \\:\\: DATE, 'YYYY-MM-DD') AS date,COUNT(request_time) AS num " +
            "FROM d1_api_log_record WHERE api_id = ?1 AND app_id = ?2 AND request_time BETWEEN ?3 AND ?4 GROUP BY to_char(request_time \\:\\: DATE, 'YYYY-MM-DD')  ORDER BY date", nativeQuery = true)
    List<Map<String, Object>> sumByApiIdAndAppIdForDay(String apiId, String appId, Calendar st, Calendar et);

    /**
     * 根据API ID 按天分组统计数据
     *
     * @param apiId API ID
     * @param appId app ID
     * @param st    开始时间
     * @param et    结束时间
     * @param code  状态码
     * @return 查询结果
     */
    @Query(value = "SELECT to_char(request_time \\:\\: DATE, 'YYYY-MM-DD') AS date,COUNT(request_time) AS num " +
            "FROM d1_api_log_record WHERE api_id = ?1 AND app_id = ?2 AND request_time BETWEEN ?3 AND ?4 AND response_code != ?5 GROUP BY to_char(request_time \\:\\: DATE, 'YYYY-MM-DD')  ORDER BY date DESC", nativeQuery = true)
    List<Map<String, Object>> sumFailByApiIdAndAppIdForDay(String apiId, String appId, Calendar st, Calendar et, String code);


    /**
     * 根据API ID 按天分组统计数据
     *
     * @param apiId API ID
     * @param appId app ID
     * @param st    开始时间
     * @param et    结束时间
     * @return 查询结果
     */
    @Query(value = "SELECT to_char(request_time \\:\\: TIMESTAMP, 'YYYY-MM-DD HH24:00:00') AS date,COUNT(request_time) AS num " +
            "FROM d1_api_log_record WHERE api_id = ?1 AND app_id = ?2 AND request_time BETWEEN ?3 AND ?4 GROUP BY to_char(request_time \\:\\: TIMESTAMP, 'YYYY-MM-DD HH24:00:00')  ORDER BY date DESC ", nativeQuery = true)
    List<Map<String, Object>> sumByApiIdAndAppIdForHours(String apiId, String appId, Calendar st, Calendar et);

    /**
     * 根据API ID 按天分组统计数据
     *
     * @param apiId API ID
     * @param appId app ID
     * @param st    开始时间
     * @param et    结束时间
     * @param code  结束时间
     * @return 查询结果
     */
    @Query(value = "SELECT to_char(request_time \\:\\: TIMESTAMP, 'YYYY-MM-DD HH24:00:00') AS date,COUNT(request_time) AS num " +
            "FROM d1_api_log_record WHERE api_id = ?1 AND app_id = ?2 AND request_time BETWEEN ?3 AND ?4 AND response_code != ?5 GROUP BY to_char(request_time \\:\\: TIMESTAMP, 'YYYY-MM-DD HH24:00:00')  ORDER BY date DESC ", nativeQuery = true)
    List<Map<String, Object>> sumFailByApiIdAndAppIdForHours(String apiId, String appId, Calendar st, Calendar et, String code);


    /**
     * 根据IP分组条件查询
     *
     * @param apiId API ID
     * @param appId app ID
     * @param st    开始时间
     * @param et    结束时间
     * @return 查询结果
     */
    @Query(value = "SELECT request_ip AS name,COUNT(request_ip) AS num FROM d1_api_log_record WHERE api_id = ?1 AND app_id = ?2 AND request_time BETWEEN ?3 AND ?4 GROUP BY request_ip ", nativeQuery = true)
    List<Map<String, Object>> sumIpByApiIdAndAppId(String apiId, String appId, Calendar st, Calendar et);

    /**
     * 根据CODE分组条件查询
     *
     * @param apiId API ID
     * @param appId app ID
     * @param st    开始时间
     * @param et    结束时间
     * @return 查询结果
     */
    @Query(value = "SELECT response_code AS name,COUNT(response_code) AS num FROM d1_api_log_record WHERE api_id = ?1 AND app_id = ?2 AND request_time BETWEEN ?3 AND ?4 GROUP BY response_code ", nativeQuery = true)
    List<Map<String, Object>> sumCodeByApiIdAndAppId(String apiId, String appId, Calendar st, Calendar et);

    /**
     * 根据响应时长分组条件查询
     *
     * @param apiId API ID
     * @param appId app ID
     * @param st    开始时间
     * @param et    结束时间
     * @return 查询结果
     */
    @Query(value = "SELECT duration_type AS name,COUNT(duration_type) AS num FROM d1_api_log_record WHERE api_id = ?1 AND app_id = ?2 AND request_time BETWEEN ?3 AND ?4 GROUP BY duration_type ORDER BY duration_type", nativeQuery = true)
    List<Map<String, Object>> sumDurationByApiIdAndAppId(String apiId, String appId, Calendar st, Calendar et);


    /**
     * 统计响应时长平均值，最大值，最小值
     *
     * @param apiId API ID
     * @param appId app ID
     * @param st    开始时间
     * @param et    结束时间
     * @return 查询结果
     */
    @Query(value = "SELECT CAST(AVG(duration) AS DECIMAL(10,2)) AS avgNum,MAX(duration) AS slowest,MIN(duration) AS fastest FROM d1_api_log_record WHERE api_id = ?1 AND app_id = ?2 AND request_time BETWEEN ?3 AND ?4", nativeQuery = true)
    List<Map<String, Object>> getAvgMaxMinByApiIdAndAppId(String apiId, String appId, Calendar st, Calendar et);

    /**
     * 根据时间统计访问量
     *
     * @param st 开始时间
     * @param et 结束时间
     * @return 结果
     */
    Long countAllByRequestTimeBetween(Calendar st, Calendar et);

    /**
     * 根据时间统计访问量
     *
     * @param st           开始时间
     * @param et           结束时间
     * @param responseCode 响应结果
     * @return 结果
     */
    Long countAllByRequestTimeBetweenAndResponseCode(Calendar st, Calendar et, String responseCode);

    /**
     * 统计查询访问失败TOP 10
     *
     * @param st   开始时间
     * @param et   结束时间
     * @param code 结束时间
     * @return 查询结果
     */
    @Query(value = "SELECT d1_api_base_info.name AS name,table1.num AS num  FROM  " +
            "(SELECT api_id AS id,COUNT(api_id) AS num FROM d1_api_log_record WHERE request_time BETWEEN ?1 AND ?2 AND response_code != ?3 GROUP BY api_id ) AS table1 " +
            "INNER JOIN d1_api_base_info ON table1.id = d1_api_base_info.id ORDER BY num DESC LIMIT 10 ", nativeQuery = true)
    List<Map<String, Object>> sumFailApi(Calendar st, Calendar et, String code);

    /**
     * 统计查询请求长耗时TOP10
     *
     * @param st 开始时间
     * @param et 结束时间
     * @return 查询结果
     */
    @Query(value = "SELECT d1_api_base_info.name AS name,table1.num AS num  FROM  " +
            "(SELECT api_id as id ,MAX(duration) AS num FROM d1_api_log_record WHERE request_time BETWEEN ?1 AND ?2 GROUP BY api_id ) AS table1 " +
            "INNER JOIN d1_api_base_info ON table1.id = d1_api_base_info.id ORDER BY num DESC LIMIT 10 ", nativeQuery = true)
    List<Map<String, Object>> sumDuration(Calendar st, Calendar et);


    List<ApiLogRecord> findAllByApiId(String apiId);

    @Query(value = "select count(d1_api_log_record.id) from d1_api_log_record",nativeQuery = true)
    int countAll();

    int countByStatus(Integer status);
}
