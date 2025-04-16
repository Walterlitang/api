package com.app.demo;


import cn.idev.excel.metadata.Head;
import cn.idev.excel.write.merge.AbstractMergeStrategy;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

public class CustomMergeStrategy extends AbstractMergeStrategy {
    private final List<Integer> mergeColumns; // 需要合并的列索引
    private final int mergeRowIndex; // 合并开始的行索引

    public CustomMergeStrategy(List<Integer> mergeColumns, int mergeRowIndex) {
        this.mergeColumns = mergeColumns;
        this.mergeRowIndex = mergeRowIndex;
    }

    @Override
    protected void merge(Sheet sheet, Cell cell, Head head, Integer relativeRowIndex) {
        int rowIndex = cell.getRowIndex();
        int colIndex = cell.getColumnIndex();

        // 仅处理需要合并的列，并从指定行开始
        if (mergeColumns.contains(colIndex) && rowIndex >= mergeRowIndex) {
            mergeColumn(sheet, rowIndex, colIndex);
        }
    }

    private void mergeColumn(Sheet sheet, int rowIndex, int colIndex) {
        String currentValue = sheet.getRow(rowIndex).getCell(colIndex).getStringCellValue();
        int startRow = rowIndex;

        // 向上查找相同值的单元格
        while (startRow > mergeRowIndex) {
            String aboveValue = sheet.getRow(startRow - 1).getCell(colIndex).getStringCellValue();
            if (!currentValue.equals(aboveValue)) {
                break;
            }
            startRow--;
        }

        // 如果有超过一个单元格需要合并
        if (startRow < rowIndex) {
            CellRangeAddress cellRangeAddress = new CellRangeAddress(startRow, rowIndex, colIndex, colIndex);
            sheet.addMergedRegion(cellRangeAddress);
        }
    }
}
