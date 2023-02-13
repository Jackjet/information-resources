package d1.project.tangshan.operation.manage.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.dao.LoginInfoDao;
import d1.project.tangshan.operation.manage.entity.LoginInfoEntity;
import d1.project.tangshan.operation.manage.entity.ResourceMonitorEntity;
import d1.project.tangshan.operation.manage.entity.operations.module.Node;
import d1.project.tangshan.operation.manage.model.DateAndNumber;
import d1.project.tangshan.operation.manage.service.operations.log.OperationLogService;
import d1.project.tangshan.operation.manage.utils.ExcelUtils;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import net.dcrun.component.pdf.PdfDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

/**
 * @author Buter
 * @date 2020/3/16 6:50
 */
@Service
public class LoginInfoService {
    private final LoginInfoDao loginInfoDao;
    private final TokenService tokenService;
    private final OperationLogService operationLogService;

    @Autowired
    public LoginInfoService(LoginInfoDao loginInfoDao, TokenService tokenService, OperationLogService operationLogService) {
        this.loginInfoDao = loginInfoDao;
        this.tokenService = tokenService;
        this.operationLogService = operationLogService;
    }


    public Page<LoginInfoEntity> findAll(JSONObject params, HttpServletRequest request) throws Exception {
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        params.put("startTime", params.getString("startTime") + " 00:00:00");
        params.put("endTime", params.getString("endTime") + " 23:59:59");
        SpecificationBuilder<LoginInfoEntity> builder = new SpecificationBuilder<>();
        Specification specification = builder.init(params)
                .tBetween("loginTime", "startTime", "endTime", dateFormat)
                .dOrder("loginTime").build();

        return loginInfoDao.findAll(specification, builder.getPageable());
    }

    public void insert(JSONObject params) throws Exception {
        LoginInfoEntity entity = JSON.toJavaObject(params, LoginInfoEntity.class);
        entity.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        this.loginInfoDao.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void excelExport(JSONObject params, HttpServletResponse response, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");

        try {
            String dateFormat = "yyyy-MM-dd HH:mm:ss";
            SpecificationBuilder<LoginInfoEntity> builder = new SpecificationBuilder<>();
            Specification specification = builder.init(params)
                    .tBetween("loginTime", "startTime", "endTime", dateFormat)
                    .dOrder("loginTime").build();

            List<LoginInfoEntity> all = loginInfoDao.findAll(specification);
            if (all.size() == 0) {
                throw new ValidException("数据为空");
            }
            operationLogService.setLog("用户登录信息监控—导出Excel", userName, "用户行为监控-用户登录信息监控", "导出了用户登录信息", "1", request);
            ExcelUtils.exportExcel(all, "用户登录信息", "用户登录信息", LoginInfoEntity.class, "用户登录信息", response);


        } catch (Exception e) {
            operationLogService.setLog("用户登录信息监控—导出Excel", userName, "用户行为监控-用户登录信息监控", "导出了用户登录信息", "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void pdfExport(JSONObject params, HttpServletResponse response, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        try {
            // 设置response参数，可以打开下载页面
//        response.reset();
            response.setCharacterEncoding("UTF-8");
            // 定义输出类型
            response.setContentType("application/PDF;application/json;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + "LoginInfo.pdf");

            BaseFont bfChinese = BaseFont.createFont(this.getFontPath(""), "Identity-H", false);
            Font titleCN = new Font(bfChinese, 17, Font.NORMAL);
            Font contentCN = new Font(bfChinese, 12, Font.NORMAL);

            String dateFormat = "yyyy-MM-dd HH:mm:ss";
            SpecificationBuilder<LoginInfoEntity> builder = new SpecificationBuilder<>();
            Specification<LoginInfoEntity> specification = builder.init(params)
                    .tBetween("loginTime", "startTime", "endTime", dateFormat)
                    .dOrder("loginTime").build();

            List<LoginInfoEntity> all = this.loginInfoDao.findAll(specification);

            if (all.size() == 0) {
                throw new ValidException("数据为空");
            }

            operationLogService.setLog("用户登录信息监控—导出PDF", userName, "用户行为监控-用户登录信息监控", "导出了用户登录信息", "1", request);
            Document doc = new Document();
            PdfWriter writer = PdfWriter.getInstance(doc, response.getOutputStream());
            PdfPTable table = new PdfPTable(4); //指定为2列
            table.setWidthPercentage(100); // 自定义宽度 100%
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            doc.open();
            PdfPCell cell = createCell("用户登录信息", titleCN);
            cell.setRowspan(1);
            cell.setColspan(4);
            table.addCell(cell);
            table.addCell(createCell("用户", titleCN));
            table.addCell(createCell("用户ip", titleCN));
            table.addCell(createCell("登录时间", titleCN));
            table.addCell(createCell("登录系统", titleCN));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (LoginInfoEntity loginInfoEntity : all) {
                table.addCell(createCell(loginInfoEntity.getName(), contentCN));
                table.addCell(createCell(loginInfoEntity.getIp(), contentCN));
                table.addCell(createCell(simpleDateFormat.format(loginInfoEntity.getLoginTimeTransient()), contentCN));
                table.addCell(createCell(loginInfoEntity.getType(), contentCN));
            }

            doc.add(table);
            doc.close();
            writer.close();


        } catch (Exception e) {
            operationLogService.setLog("用户登录信息监控—导出PDF", userName, "用户行为监控-用户登录信息监控", "导出了用户登录信息", "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    //生成单元格
    public static PdfPCell createCell(Object value, Font font) {
        if (value == null) {
            value = "";
        }
        PdfPCell cell = new PdfPCell(new Paragraph(value.toString(), font));
        cell.setBorderColor(BaseColor.BLACK);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    public String getFontPath(String fontPath) {
        if (fontPath != null && !"".equals(fontPath)) {
            return fontPath;
        } else {
            String filePath = PdfDocument.class.getClassLoader().getResource("simsun.ttc").toString() + ",0";
            return filePath;
        }
    }


    public void insert(LoginInfoEntity entity) {
        loginInfoDao.save(entity);
    }

    /**
     * 根据用户id，获取该用户的首次登陆时间
     *
     * @param loginUserId
     * @return
     */
    public Calendar getFirstLoginTime(String loginUserId) {
        LoginInfoEntity entity = loginInfoDao.findFirstByLoginUserIdOrderByFirstLoginTimeAsc(loginUserId);
        LoginInfoEntity entity1 = new LoginInfoEntity();
        if (entity == null) {
            entity1.setFirstLoginTime(Calendar.getInstance());
        } else {
            entity1.setFirstLoginTime(entity.getFirstLoginTime());
        }

        return entity1.getFirstLoginTime();
    }

    public void save(LoginInfoEntity entity) {
        loginInfoDao.save(entity);
    }

    public List<DateAndNumber> accessTrend(JSONObject params) throws Exception {
        String startTime = params.getString("startTime") + " 00:00:00";
        String endTime = params.getString("endTime") + " 23:59:59";

        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        Calendar start = MyUtils.stringToCalendar(startTime, dateFormat);
        Calendar end = MyUtils.stringToCalendar(endTime, dateFormat);


        List<DateAndNumber> list = loginInfoDao.countByDay(start, end);
        List<String> allDays = MyUtils.getDayBetween(startTime, endTime);
        List<DateAndNumber> result = new ArrayList<>();
        polishing(allDays, result, list);

        return result;
    }

    public Page<LoginInfoEntity> online(JSONObject params) throws Exception {

        // 1小时之前
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        Pageable pageable = MyUtils.getPageable(params.getInteger("page"),params.getInteger("size"),null);
        return loginInfoDao.findAllDistinctName(calendar,pageable);
    }

    public List<DateAndNumber> activity(JSONObject params) throws Exception {
        // 统计单位
        String unit = params.getString("unit");
        String name = params.getString("name");
        String startTime = params.getString("startTime") + " 00:00:00";
        String endTime = params.getString("endTime") + " 23:59:59";

        Calendar start = MyUtils.stringToCalendar(startTime, "yyyy-MM-dd HH:mm:ss");
        Calendar end = MyUtils.stringToCalendar(endTime, "yyyy-MM-dd HH:mm:ss");
        List<Map<String, Object>> list = new ArrayList<>();

        switch (unit) {
            case "day":
                list = loginInfoDao.findByDay(name, start, end);
                break;
            case "month":
                list = loginInfoDao.findByMonth(name, start, end);

                break;
            case "week":
                list = loginInfoDao.findByWeek(name, start, end);
                break;
            default:
                throw new ValidException("统计单位错误");
        }
        String json = JSON.toJSON(list).toString();
        List<DateAndNumber> list1 = JSON.parseArray(json, DateAndNumber.class);
        if ("week".equals(unit)) {
            for (DateAndNumber dateAndNumber : list1) {
                String date = dateAndNumber.getDate();
                dateAndNumber.setDate(date.substring(0, 7) + "第" + MyUtils.getWeekOfMonth(date) + "周");
            }
        }
        List<DateAndNumber> result = new ArrayList<>();

        List<String> allDays = new ArrayList<>();

        switch (unit) {
            case "day":
                allDays = MyUtils.getDayBetween(startTime, endTime);
                break;
            case "month":
                allDays = MyUtils.getMonthBetween(startTime, endTime);
                break;
            case "week":
                allDays = MyUtils.getWeekBetween(startTime, endTime);
                List<String> newWeeks = new ArrayList<>();
                for (String week : allDays) {
                    newWeeks.add(week.substring(0, 7) + "第" + MyUtils.getWeekOfMonth(week) + "周");
                }
                allDays = newWeeks;
                break;
            default:
                throw new ValidException("统计单位错误");
        }
        polishing(allDays, result, list1);

        return result;
    }

    private JSONObject getParamsByUnit(String unit, Date beginTime, Date endTime, JSONObject params) throws Exception {
        params.put("time1", beginTime);
        if (beginTime.after(endTime)) {
            throw new ValidException("开始时间晚于结束时间");
        }
        JSONObject result = new JSONObject();
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(beginTime);
        switch (unit) {
            case "day":
                beginCalendar.add(Calendar.DAY_OF_MONTH, 1);
                break;
            case "month":
                beginCalendar.add(Calendar.MONTH, 1);
                break;
            case "year":
                beginCalendar.add(Calendar.YEAR, 1);
                break;
            default:
                throw new ValidException("未知的日志类型");
        }

        if (beginCalendar.getTime().after(endTime)) {
            beginCalendar.setTime(endTime);
        }
        params.put("time2", beginCalendar.getTime());
        return result;
    }


    private JSONObject getParams(Integer days) {
        if (days == null) {
            days = 7;
        }
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -days + 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        JSONObject params = new JSONObject();
        params.put("beginTime", format.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        params.put("endTime", format.format(calendar.getTime()));
        return params;
    }

    private void polishing(List<String> allDays, List<DateAndNumber> result, List<DateAndNumber> list) {
        for (String day : allDays) {
            DateAndNumber dateAndNumber = new DateAndNumber();
            dateAndNumber.setDate(day);
            dateAndNumber.setNumber(0L);
            result.add(dateAndNumber);
        }
        for (DateAndNumber dateAndNumber : result) {
            for (DateAndNumber dateAndNumber1 : list) {
                if (dateAndNumber.getDate().equals(dateAndNumber1.getDate())) {
                    dateAndNumber.setNumber(dateAndNumber1.getNumber());
                }
            }
        }
    }

    public Long countUserNumByLoginTime(Calendar startTime,Calendar endTime){
        return loginInfoDao.countUserNumByLoginTime(startTime,endTime);
    }

    public List<Map<String,Object>> countLoginNumByDay(Calendar startTime, Calendar endTime){
        return loginInfoDao.countLoginNumByDay(startTime,endTime);
    }
}
