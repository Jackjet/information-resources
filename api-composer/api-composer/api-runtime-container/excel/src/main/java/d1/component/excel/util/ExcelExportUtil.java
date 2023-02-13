package d1.component.excel.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.project.container.api.base.utils.Utils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.util.*;

public class ExcelExportUtil {
    /**
     * 导出Excel
     *
     * @param array 数据
     * @throws Exception 生成Excel时的报错信息
     */
    public HSSFWorkbook exportExcel(JSONArray array) throws Exception {
        //指定2003版本 .xls文件
        HSSFWorkbook wb = new HSSFWorkbook();
        setSheet(wb, "Sheet1", array);
        return wb;
    }

    private void setSheet(HSSFWorkbook wb, String sheetName, JSONArray array) {
        HSSFSheet sheet = wb.createSheet(sheetName);
        sheet.setDefaultColumnWidth(6);

        //遍历数据获取header
        Set<String> headers = getHeaders(array);
        if (headers.size() == 0) {
            return;
        }

        //设置字段
        setHeader(sheet, wb, headers, 0);

        //遍历数据获取所有的value
        setContent(sheet, wb, 1, getValues(array, headers));
    }

    private Set<String> getHeaders(JSONArray array) {
        //遍历得到所有key
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < array.size(); i++) {
            Object o = array.get(i);
            if (!(o instanceof JSONObject)) {
                continue;
            }

            Set<String> keys = ((JSONObject) o).keySet();
            for (String key : keys) {
                map.put(key, null);
            }
        }
        return map.keySet();
    }


    private List<List<String>> getValues(JSONArray array, Set<String> headers) {
        List<List<String>> values = new ArrayList<>();
        //填充所有缺失数据
        for (int i = 0; i < array.size(); i++) {
            Object o = array.get(i);
            List<String> row = new ArrayList<>();
            if (o instanceof JSONObject) {
                for (String header : headers) {
                    row.add(Utils.getJsonValue((JSONObject) o, header, ""));
                }
            } else {
                row.add(o.toString());
            }
            values.add(row);
        }

        return values;
    }

    private void setHeader(Sheet sheet, Workbook wb, Set<String> headers, int rowIndex) {
        Row row = sheet.createRow(rowIndex);
        row.setHeightInPoints(20);
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setBold(true);//粗体显示
        font.setFontHeightInPoints((short) 12);//设置字体大小

        Iterator<String> iterator = headers.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            String value = iterator.next();
            Cell cell = row.createCell(i);
            cellStyle.setFont(font);
            cell.setCellValue(value);
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(i);
            i++;
        }

    }

    private void setContent(Sheet sheet, Workbook wb, int rowIndex, List<List<String>> list) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        Font cellFont = wb.createFont();
        cellFont.setFontName("宋体");
        cellFont.setFontHeightInPoints((short) 11);//设置字体大小
        cellStyle.setFont(cellFont);

        cellStyle.setWrapText(true);

        for (int i = 0; i < list.size(); i++) {
            Row row = sheet.createRow(rowIndex + i);
            List<String> rowValues = list.get(i);

            for (int j = 0; j < rowValues.size(); j++) {
                Cell cell = row.createCell(j);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(rowValues.get(j));
            }
        }
    }
}
