package com.app.demo;


import cn.idev.excel.FastExcel;
import cn.idev.excel.write.handler.WriteHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

@RestController
@RequestMapping("/export")
public class ExcelExportController {

    // 填充要写入的数据
    private List<DemoData> data() {
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }

    @GetMapping("/single-column")
    public void exportSingleColumn(HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("导出示例", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        FastExcel.write(response.getOutputStream(), DemoData.class)
                .sheet("sheet1")
                .doWrite(this.data());

    }


    public static List<ComplexHeadData> generateData() {
        List<ComplexHeadData> dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ComplexHeadData data = new ComplexHeadData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(i * 0.1);
            dataList.add(data);
        }
        return dataList;
    }

    @GetMapping("/multi-column")
    public void exportMultiColumn(HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("导出示例", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        FastExcel.write(response.getOutputStream(), ComplexHeadData.class)
                .sheet("模板")
                .doWrite(generateData());
    }

    @GetMapping("/dynamicHead")
    public void dynamicHead(HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("导出示例", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        List<List<String>> head = Arrays.asList(
                Collections.singletonList("动态字符串"),
                Collections.singletonList("动态数字"),
                Collections.singletonList("动态日期")
        );

        FastExcel.write(response.getOutputStream()).head(head)
                .sheet("模板")
                .doWrite(data());
    }

    @GetMapping("/column-merge")
    public void exportColumnMerge(HttpServletResponse response) throws IOException {
        String fileName = URLEncoder.encode("用户数据", "UTF-8").replaceAll("\\+", "%20");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 生成测试数据
        List<UserExcelDto> dataList = generateUserData();

        // 定义需要合并的列索引和起始行
        int[] mergeColumnIndex = {0, 1, 2, 3}; // 合并 "序号" 和 "用户名" 列
        int mergeRowIndex = 1; // 从第2行开始合并

        // 创建合并策略
        WriteHandler mergeStrategy = new ExcelFillCellMergeStrategyUtil(mergeRowIndex, mergeColumnIndex);

        // 写入数据并应用合并策略
        FastExcel.write(response.getOutputStream(), UserExcelDto.class)
                .registerWriteHandler(mergeStrategy)
                .sheet("用户数据")
                .doWrite(dataList);
    }

    private List<UserExcelDto> generateUserData() {
        List<UserExcelDto> dataList = new ArrayList<>();
        // 添加测试数据
        dataList.add(new UserExcelDto("1", "张三", "123456", "描述1", "100000"));
        dataList.add(new UserExcelDto("2", "李四", "abcdef", "描述2", "100001"));
        dataList.add(new UserExcelDto("3", "王五", "password", "描述3", "100002"));
        dataList.add(new UserExcelDto("3", "王五", "password", "描述3", "100002"));
        dataList.add(new UserExcelDto("3", "王五", "password", "描述3", "100002"));
        // 根据需要添加更多数据
        return dataList;

    }
}
