package com.app.demo;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
public class ComplexHeadData {
    @ExcelProperty({"主标题1", "子标题1", "字符串标题"})
    private String string;

    @ExcelProperty({"主标题1", "子标题2", "日期标题"})
    private Date date;

    @ExcelProperty({"主标题2", "子标题3", "数字标题"})
    private Double doubleData;
}
