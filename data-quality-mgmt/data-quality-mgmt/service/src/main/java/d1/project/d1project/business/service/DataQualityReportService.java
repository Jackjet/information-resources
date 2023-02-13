package d1.project.d1project.business.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.d1project.business.dao.DataQualityReportDao;
import d1.project.d1project.business.dao.TaskLogDao;
import d1.project.d1project.business.dao.VerifyRuleDao;
import d1.project.d1project.business.dao.WorkOrderDao;
import d1.project.d1project.business.entity.DataQualityReport;
import d1.project.d1project.business.model.DataQualityReportFindAllVm;
import d1.project.d1project.business.model.DataQualityReportInsertVm;
import d1.project.d1project.business.model.DataQualityReportUpdateVm;
import d1.project.d1project.business.utils.Utils;
import d1.project.d1project.common.utils.BaseUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DataQualityReportService {

    private final DataQualityReportDao dataQualityReportDao;
    private final VerifyRuleDao verifyRuleDao;
    private final WorkOrderDao workOrderDao;
    private final TaskLogDao taskLogDao;


    public DataQualityReportService(DataQualityReportDao dataQualityReportDao, VerifyRuleDao verifyRuleDao, WorkOrderDao workOrderDao, TaskLogDao taskLogDao) {
        this.verifyRuleDao = verifyRuleDao;
        this.workOrderDao = workOrderDao;
        this.dataQualityReportDao = dataQualityReportDao;
        this.taskLogDao = taskLogDao;
    }

    /**
     * 分页查询所有
     *
     * @param model 查询参数
     */
    public Page<DataQualityReport> findAll(DataQualityReportFindAllVm model) throws Exception {
        SpecificationBuilder<DataQualityReport> builder = new SpecificationBuilder<>();
        Specification<DataQualityReport> specification = builder.init((JSONObject) JSON.toJSON(model))
                .sContain("name", "name")
                .sContain("createByName", "createByName")
                .tBetween("createTime","startTime","endTime","yyyy-MM-dd HH:mm:ss")
                .dOrder("createTime")
                .build();
        return dataQualityReportDao.findAll(specification, builder.getPageable());
    }

    public DataQualityReport find(String id) throws DoValidException {
        return dataQualityReportDao.findById(id).orElseThrow(() -> new DoValidException("数据质量报告不存在"));
    }

    public Map<String, Object> statistics(String startTime, String endTime, HttpServletRequest request) throws DoValidException, ParseException {
        Map<String, Object> result = new HashMap<>();

        //转换下时间，把yyyy-MM 转换成 yyyy-MM-dd 方便数据库查询
        LocalDateTime newStartTime = LocalDate.parse(formatTime(startTime)).atStartOfDay();
        LocalDateTime newEndTime = LocalDate.parse(formatTimeAndMonthAmount(endTime)).atStartOfDay();

        JSONObject count = new JSONObject();
        count.put("startTime", startTime); //开始时间
        count.put("endTime", endTime); //结束时间
        count.put("runTaskCount", getTaskCount(newStartTime, newEndTime)); //运行的任务数
        count.put("createRuleCount", getRuleCount(newStartTime, newEndTime)); //新建规则数
        count.put("createWorkOrderCount", getCreateWorkOrderCount(newStartTime, newEndTime)); //创建的工单数
        count.put("closeWorkOrderCount", getCloseWorkOrderCount(newStartTime, newEndTime)); //关闭的工单数

        Map<String, BigDecimal> validateTotalDataCountOrTotalErrorDataSize = taskLogDao.getValidateTotalDataCountOrTotalErrorDataSize(newStartTime, newEndTime);
        BigDecimal validateDataCount = validateTotalDataCountOrTotalErrorDataSize.get("total_data_size") == null ? BigDecimal.ZERO : validateTotalDataCountOrTotalErrorDataSize.get("total_data_size");
        BigDecimal errorDataCount = validateTotalDataCountOrTotalErrorDataSize.get("total_error_data_size") == null ? BigDecimal.ZERO : validateTotalDataCountOrTotalErrorDataSize.get("total_error_data_size");

        double errorRateCount;
        if (BigDecimal.ZERO.equals(validateDataCount)) {
            errorRateCount = 0.00;
        } else {
            errorRateCount = errorDataCount.divide(validateDataCount, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        count.put("errorDataCount", errorDataCount); //错误的数据条数
        count.put("validateDataCount", validateDataCount); //核检的数据条数
        count.put("errorRateCount", errorRateCount); //错误率

        result.put("count", count);
        result.put("workOrderLevel", workOrderDao.getCountGroupByLevel(newStartTime, newEndTime)); //按照工单级别分组
        result.put("workOrderCreateByName", workOrderDao.getCountGroupByCreateByName(newStartTime, newEndTime));//按照工单创建人分组
        result.put("workOrderHandlerName", workOrderDao.getCountGroupByHandlerName(newStartTime, newEndTime)); //按照工单解决人分组
        result.put("ruleCount", verifyRuleDao.getCountGroupByRuleTemplateName(newStartTime, newEndTime)); //按照模板分组查询创建的规则数量
        result.put("taskCount", getCountGroupByYearMonth(newStartTime, newEndTime, getMonths(startTime, endTime))); //按照月份计算每个月任务运行的数量
        result.put("validDataCount", getValidateTotalDataCountOrTotalErrorDataSizeGroupByYearMonth(newStartTime, newEndTime, getMonths(startTime, endTime))); //按照月份计算每个月检查数据的条数和检查数据错误的条数

        return result;
    }

    /**
     * 新增
     *
     * @param model 传输模型
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(DataQualityReportInsertVm model, HttpServletRequest request) throws DoValidException {
        DataQualityReport entity = new DataQualityReport();
        BeanUtils.copyProperties(model, entity);

        entity.setId(BaseUtils.generate32Id());
        entity.setCreateTime(Calendar.getInstance());
        entity.setCreateById(Utils.getCurrentUserId(request));
        entity.setCreateByName(Utils.getCurrentUserName(request));
        dataQualityReportDao.save(entity);
    }

    /**
     * 删除
     *
     * @param id id
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        dataQualityReportDao.deleteById(id);
    }

    /**
     * 完善报告
     *
     * @param model 传输模型
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(DataQualityReportUpdateVm model, HttpServletRequest request) throws DoValidException {
        DataQualityReport entity = dataQualityReportDao.findById(model.getId()).orElseThrow(() -> new DoValidException("数据质量报告不存在"));

        entity.setForeword(model.getForeword());
        entity.setClosing(model.getClosing());
        entity.setUpdateTime(Calendar.getInstance());
        entity.setUpdateById(Utils.getCurrentUserId(request));
        dataQualityReportDao.save(entity);
    }


    ///////////

    private Object getTaskCount(LocalDateTime startTime, LocalDateTime endTime) {
        return taskLogDao.getTaskCount(startTime, endTime);
    }

    private Object getRuleCount(LocalDateTime startTime, LocalDateTime endTime) {
        return verifyRuleDao.getRuleCount(startTime, endTime);
    }

    private Object getCreateWorkOrderCount(LocalDateTime startTime, LocalDateTime endTime) {
        return workOrderDao.getCreateWorkOrderCount(startTime, endTime);
    }

    private Object getCloseWorkOrderCount(LocalDateTime startTime, LocalDateTime endTime) {
        return workOrderDao.getCloseWorkOrderCount(startTime, endTime);
    }

    private Object getCountGroupByYearMonth(LocalDateTime newStartTime, LocalDateTime newEndTime, List<String> months) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> data = taskLogDao.getCountGroupByYearMonth(newStartTime, newEndTime);
        for (String month : months) {
            Map<String, Object> map = getCountByYearMonth(month, data);
            if (map == null) {
                map = new HashMap<>();
                map.put("year_month", month);
                map.put("count", 0);
            }
            result.add(map);
        }
        return result;
    }

    private Object getValidateTotalDataCountOrTotalErrorDataSizeGroupByYearMonth(LocalDateTime newStartTime, LocalDateTime newEndTime, List<String> months) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> data = taskLogDao.getValidateTotalDataCountOrTotalErrorDataSizeGroupByYearMonth(newStartTime, newEndTime);
        for (String month : months) {
            Map<String, Object> map = getCountByYearMonth(month, data);
            if (map == null) {
                map = new HashMap<>();
                map.put("year_month", month);
                map.put("total_error_data_size", 0);
                map.put("total_data_size", 0);
            }
            result.add(map);
        }
        return result;
    }


    private Map<String, Object> getCountByYearMonth(String month, List<Map<String, Object>> result) {
        for (Map<String, Object> map : result) {
            Object yearMonth = map.get("year_month");
            if (yearMonth == null) {
                continue;
            }
            if (yearMonth.toString().equals(month)) {
                return map;
            }
        }
        return null;
    }


    /**
     * 把 yyyy-MM 转换成 yyyy-MM-dd
     *
     * @param time yyyy-MM 时间格式字符串
     * @return yyyy-MM-dd 时间格式字符串
     * @throws ParseException 异常
     */
    private String formatTime(String time) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM").parse(time);
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    /**
     * 把 yyyy-MM 转换成 yyyy-MM-dd 并月份+1
     *
     * @param time yyyy-MM 时间格式字符串
     * @return yyyy-MM-dd 时间格式字符串
     * @throws ParseException 异常
     */
    private String formatTimeAndMonthAmount(String time) throws ParseException {
        //   yyyyMMdd转yyyy-MM-dd
        Date date = new SimpleDateFormat("yyyy-MM").parse(time);

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 1);

        return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    }

    //获取某个时间之间所有的月份
    private List<String> getMonths(String startTimeStr, String endTimeStr) throws ParseException {
        List<String> months = new ArrayList<>();

        Date startTime = new SimpleDateFormat("yyyy-MM").parse(startTimeStr);//定义起始日期
        Date endTime = new SimpleDateFormat("yyyy-MM").parse(endTimeStr);//定义结束日期

        //定义日期实例
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);//设置日期起始时间
        while (calendar.getTime().before(endTime)) {//判断是否到结束日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String str = sdf.format(calendar.getTime());
            months.add(str);
            calendar.add(Calendar.MONTH, 1);//进行当前日期月份加1
        }
        months.add(endTimeStr);
        return months;
    }


}
