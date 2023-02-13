package d1.project.resourcesharingmgmt.system.utils;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.util.StyleUtil;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导出Excel给列加下拉框
 *
 * @author zhengyang
 */
public class ExcelTitleHandler implements CellWriteHandler {
    /**
     * 操作列
     */
    private List<Integer> columnIndexs;

    /**
     * 颜色
     */
    private Short colorIndex;

    /**
     * 批注<列的下标,批注内容>
     */
    private HashMap<Integer, String> annotationsMap;

    /**
     * 下拉框值
     */
    private HashMap<Integer, String[]> dropDownMap;

    /**
     * 导出数据的list大小
     */
    private int dataSize;

    public ExcelTitleHandler(List<Integer> columnIndexs, Short colorIndex, HashMap<Integer, String> annotationsMap, int dataSize) {
        this.columnIndexs = columnIndexs;
        this.colorIndex = colorIndex;
        this.annotationsMap = annotationsMap;
        this.dataSize = dataSize;
    }

    public ExcelTitleHandler(List<Integer> columnIndexs, Short colorIndex, int dataSize) {
        this.columnIndexs = columnIndexs;
        this.colorIndex = colorIndex;
        this.dataSize = dataSize;
    }

    public ExcelTitleHandler(HashMap<Integer, String[]> dropDownMap, int dataSize) {
        this.dropDownMap = dropDownMap;
        this.dataSize = dataSize;
    }

    public ExcelTitleHandler(List<Integer> columnIndexs, Short colorIndex, HashMap<Integer, String> annotationsMap, HashMap<Integer, String[]> dropDownMap, int dataSize) {
        this.columnIndexs = columnIndexs;
        this.colorIndex = colorIndex;
        this.annotationsMap = annotationsMap;
        this.dropDownMap = dropDownMap;
        this.dataSize = dataSize;
    }

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {
    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
    }

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        if (isHead) {
            // 设置列宽
            Sheet sheet = writeSheetHolder.getSheet();
            sheet.setColumnWidth(cell.getColumnIndex(), 14 * 256);
            writeSheetHolder.getSheet().getRow(0).setHeight((short) (1.8 * 256));
            Workbook workbook = writeSheetHolder.getSheet().getWorkbook();
            Drawing<?> drawing = sheet.createDrawingPatriarch();

            // 设置标题字体样式
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            WriteFont headWriteFont = new WriteFont();
            headWriteFont.setFontName("宋体");
            headWriteFont.setFontHeightInPoints((short) 14);
            headWriteFont.setBold(true);
            if (CollectionUtils.isNotEmpty(columnIndexs) &&
                    colorIndex != null &&
                    columnIndexs.contains(cell.getColumnIndex())) {
                // 设置字体颜色
                headWriteFont.setColor(colorIndex);
            }
            headWriteCellStyle.setWriteFont(headWriteFont);
            headWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            CellStyle cellStyle = StyleUtil.buildHeadCellStyle(workbook, headWriteCellStyle);
            cell.setCellStyle(cellStyle);

            if (null != annotationsMap && annotationsMap.containsKey(cell.getColumnIndex())) {
                // 批注内容
                String context = annotationsMap.get(cell.getColumnIndex());
                // 创建绘图对象
                Comment comment = drawing.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short) cell.getColumnIndex(), 0, (short) 5, 5));
                comment.setString(new XSSFRichTextString(context));
                cell.setCellComment(comment);
            }

            if (null != dropDownMap && !dropDownMap.isEmpty() && dropDownMap.containsKey(cell.getColumnIndex())) {
                DataValidationHelper dvHelper = sheet.getDataValidationHelper();
                for (Map.Entry<Integer, String[]> entry : dropDownMap.entrySet()) {
                    int key = entry.getKey();
                    if(key == head.getColumnIndex()) {
                        // 解决下拉框内容过多不显示的问题，创建一个隐藏的sheet
                        //定义sheet的名称
                        String hiddenName = "hidden" + key;
                        Sheet hidden = null;

                        if (workbook.getSheetIndex(hidden) < 0) {
                            hidden = workbook.createSheet(hiddenName);
                        } else {
                            hidden = workbook.getSheet(hiddenName);
                        }

                        //下拉框的起始行,结束行,起始列,结束列
                        CellRangeAddressList addressList = new CellRangeAddressList(1, dataSize + 500, entry.getKey(), entry.getKey());
                        //获取excel列名
                        String excelLine = getExcelLine(entry.getKey());

                        //2.循环赋值
                        String[] values = entry.getValue();
                        for (int i = 0, length = values.length; i < length; i++) {
                            // 3:表示你开始的行数  3表示 你开始的列数
                            Row row = hidden.getRow(i);
                            if (row == null) {
                                row = hidden.createRow(i);
                            }
                            row.createCell(entry.getKey()).setCellValue(values[i]);
                        }

                        //4.  =hidden!$H:$1:$H$50  sheet为hidden的 H1列开始H50行数据获取下拉数组
                        String refers = "=" + hiddenName + "!$" + excelLine +
                                "$1:$" + excelLine + "$" + (values.length);
                        //5 将刚才设置的sheet引用到你的下拉列表中
                        DataValidationConstraint constraint = dvHelper.createFormulaListConstraint(refers);
                        DataValidation dataValidation = dvHelper.createValidation(constraint, addressList);
                        writeSheetHolder.getSheet().addValidationData(dataValidation);

                        //设置列为隐藏
                        int hiddenIndex = workbook.getSheetIndex(hidden);
                        if (!workbook.isSheetHidden(hiddenIndex)) {
                            workbook.setSheetHidden(hiddenIndex, true);
                        }
                    }
                }
            }
        }
    }

    /**
     * @param num 列数
     * @return java.lang.String
     * @Description 返回excel列标A-Z-AA-ZZ
     * @Author chou
     * @Date 2020/9/8
     */
    public static String getExcelLine(int num) {
        String line = "";
        int first = num / 26;
        int second = num % 26;
        if (first > 0) {
            line = (char) ('A' + first - 1) + "";
        }
        line += (char) ('A' + second) + "";
        return line;
    }
}
