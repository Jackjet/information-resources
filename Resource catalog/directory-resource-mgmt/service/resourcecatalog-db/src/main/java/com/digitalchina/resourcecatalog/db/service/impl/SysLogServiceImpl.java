package com.digitalchina.resourcecatalog.db.service.impl;

import com.digitalchina.resourcecatalog.db.domain.SysLog;
import com.digitalchina.resourcecatalog.db.dao.SysLogMapper;
import com.digitalchina.resourcecatalog.db.service.SysLogService;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志表 服务实现类
 * </p>
 *
 * @author wangmh
 * @since 2020-04-30
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

	@Override
    public Workbook export(String name,String startDate,String endDate) {
        // 创建工作薄
        XSSFWorkbook wb = new XSSFWorkbook();
        getSheet1(wb,name,startDate,endDate);
        return wb;
    }

    private void getSheet1(XSSFWorkbook wb,String name,String startDate,String endDate){
        QueryWrapper<SysLog> qw = new QueryWrapper<SysLog>();
        qw.eq(SysLog.TYPE,0);
        qw.eq(SysLog.DELETED,0);
        if(!StringUtils.isEmpty(name)){
            qw.and(w -> w.like(SysLog.ADMIN,name).or().like(SysLog.ACCOUNT,name));
        }
        //TODO 转换
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(!StringUtils.isEmpty(startDate)){
            startDate = startDate + " 00:00:00";
            qw.ge(SysLog.ADD_TIME, LocalDateTime.parse(startDate,dtf));
        }
        if(!StringUtils.isEmpty(endDate)){
            endDate = endDate + " 23:59:59";
            qw.le(SysLog.ADD_TIME,LocalDateTime.parse(endDate,dtf));
        }
        qw.orderByDesc(SysLog.ADD_TIME);
        List<SysLog> sysLogList = this.baseMapper.selectList(qw);
        XSSFSheet sheet2 = wb.createSheet("操作日志");
        sheet2.setDefaultColumnWidth((short)20);// 默认列宽

        XSSFRow row2 = sheet2.createRow((short) 0);
        cteateCell(wb, row2, (short) 0, "序号",true);
        cteateCell(wb, row2, (short) 1, "操作人账号",true);
        cteateCell(wb, row2, (short) 2, "操作人",true);
        cteateCell(wb, row2, (short) 3, "操作时间",true);
        cteateCell(wb, row2, (short) 4, "功能模块",true);
        cteateCell(wb, row2, (short) 5, "操作内容",true);
//        CellRangeAddress cra = new CellRangeAddress(0,0,0,5);
//        sheet2.addMergedRegion(cra);
//        setBorder(0, cra, sheet2);

        for (int i = 0; i < sysLogList.size(); i++) {
            XSSFRow row = sheet2.createRow((short) i+1);
            cteateCell(wb, row, (short) 0, String.valueOf(i+1),false);
            cteateCell(wb, row, (short) 1, sysLogList.get(i).getAccount() == null? "" : sysLogList.get(i).getAccount(),false);
            cteateCell(wb, row, (short) 2, sysLogList.get(i).getAdmin() == null? "" : sysLogList.get(i).getAdmin(),false);
            cteateCell(wb, row, (short) 3, sysLogList.get(i).getAddTime() == null? "" : dtf.format(sysLogList.get(i).getAddTime()),false);
            cteateCell(wb, row, (short) 4, sysLogList.get(i).getAction() == null? "" : sysLogList.get(i).getAction(),false);
            cteateCell(wb, row, (short) 5, sysLogList.get(i).getResult() == null? "" : sysLogList.get(i).getResult(),false);

        }

    }
    
    private void cteateCell(XSSFWorkbook wb, XSSFRow row, short col,String val,boolean isTitle) {
        //设置行高
        //row.setHeight((short) 480);
        XSSFCell cell = row.createCell(col);
        cell.setCellValue(val);
        XSSFCellStyle cellstyle = wb.createCellStyle();
        XSSFFont font = wb.createFont();
        if(isTitle){
            font.setBold(true);
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 11);
        }else{
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 10);
        }
        cellstyle.setFont(font);
        cellstyle.setWrapText(true);
        cellstyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);
        cellstyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellstyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
        cellstyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
        cellstyle.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
        cellstyle.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
        cell.setCellStyle(cellstyle);
    }

     private void setBorder(int border, CellRangeAddress cra, Sheet sheet){
         // 使用RegionUtil类为合并后的单元格添加边框
         RegionUtil.setBorderBottom(border, cra, sheet); // 下边框
         RegionUtil.setBorderLeft(border, cra, sheet); // 左边框
         RegionUtil.setBorderRight(border, cra, sheet); // 有边框
         RegionUtil.setBorderTop(border, cra, sheet); // 上边框
     }

}
