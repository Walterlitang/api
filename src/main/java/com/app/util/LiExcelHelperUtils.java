package com.app.util;

//import org.apache.poi.hssf.usermodel.HSSFDateUtil;

import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: wangfei
 * Date: 2019/12/24
 * FileName: AgrServiceDataService
 * Description: 读取Excel数据工具类
 */
public class LiExcelHelperUtils {

    public static List<String[]> readExcelToStringArray(MultipartFile mfile) throws Exception {
        // 读取Excel
        List<String[]> list = new ArrayList<>();
        Workbook wb = WorkbookFactory.create(mfile.getInputStream());
        Sheet sheet = wb.getSheetAt(0);
        Row row = null;
        String[] str = null;
        //取得有效的行数
        int lastRowNum = sheet.getLastRowNum();
        int lastCellNum = sheet.getRow(0).getLastCellNum();
        for (int i = 1; i <= lastRowNum; i++) {
            row = sheet.getRow(i);
            if (row != null) {
                // 获取每一列的值,将每一行数据存入数组中
                //sheet.getRow(1).getLastCellNum()
                str = new String[lastCellNum + 1];
                //row.getLastCellNum()
                for (int j = 0; j < lastCellNum; j++) {
                    Cell cell = row.getCell(j);
                    String value = LiExcelHelperUtils.getCellValue(cell);
                    value = value.trim();
                    value = value.replaceAll("\n", "");
                    if (!value.equals("")) {
                        str[j] = value;
                    } else {
                        str[j] = "";
                    }
                }
                str[lastCellNum] = i + "";
                list.add(str);
            }
        }
        return list;
    }

    public static List<String[]> readExcelToStringArray(MultipartFile mfile, int firstrow) throws Exception {
        // 读取Excel
        List<String[]> list = new ArrayList<>();
        Workbook wb = WorkbookFactory.create(mfile.getInputStream());
        Sheet sheet = wb.getSheetAt(0);
        Row row = null;
        String[] str = null;
        //取得有效的行数
        int lastRowNum = sheet.getLastRowNum();
        int truefirstrow = 0 == firstrow ? 0 : firstrow;
        int lastCellNum = sheet.getRow(truefirstrow).getLastCellNum();
        for (int i = firstrow; i <= lastRowNum; i++) {
            row = sheet.getRow(i);
            if (row != null) {
                // 获取每一列的值,将每一行数据存入数组中
                //sheet.getRow(1).getLastCellNum()
                str = new String[lastCellNum + 1];
                //row.getLastCellNum()
                for (int j = 0; j < lastCellNum; j++) {
                    Cell cell = row.getCell(j);
                    String value = LiExcelHelperUtils.getCellValue(cell);
                    value = value.trim();
                    value = value.replaceAll("\n", "");
                    if (!value.equals("")) {
                        str[j] = value;
                    } else {
                        str[j] = "";
                    }
                }
                str[lastCellNum] = i + "";
                list.add(str);
            }
        }
        return list;
    }

    /**
     * 判断字符串是否是整数
     */
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是浮点数
     */
    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            if (value.contains("."))
                return true;
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isnumber(String value) {
        return isDouble(value) || isInteger(value);
    }

    public static String getCellValue(Cell cell) {
        Object result = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case STRING:
                    String cellValue = cell.getStringCellValue();
                    if (cellValue.trim().equals("")
                            || cellValue.trim().length() <= 0) {
                        result = "";
                    } else {
                        result = cell.getStringCellValue().trim();
                    }
                    break;
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        double d = cell.getNumericCellValue();
                        Date date = DateUtil.getJavaDate(d);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        result = sdf.format(date);
                    } else {
                        DecimalFormat df = new DecimalFormat("0.000");
                        Double cellValue2 = cell.getNumericCellValue();
                        String cellvaluestr = df.format(cellValue2);
                        String xiaoshudian = cellvaluestr.split("\\.")[1];
                        if (xiaoshudian.equals("000")) {
                            return cellvaluestr.split("\\.")[0];
                        } else {
                            return cellvaluestr;
                        }
                    }

                    break;
                case BOOLEAN:
                    result = cell.getBooleanCellValue();
                    break;
                case FORMULA:
                    result = cell.getCellFormula();
                    break;
                case ERROR:
                    result = cell.getErrorCellValue();
                    break;
                case BLANK:
                    break;
                default:
                    break;
            }
        } else {
            return "";
        }
        return result.toString();
    }

}
