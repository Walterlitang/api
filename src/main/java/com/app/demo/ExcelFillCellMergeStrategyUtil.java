package com.app.demo;


import cn.idev.excel.metadata.Head;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.write.handler.CellWriteHandler;
import cn.idev.excel.write.metadata.holder.WriteSheetHolder;
import cn.idev.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

public class ExcelFillCellMergeStrategyUtil implements CellWriteHandler {

    private int[] mergeColumnIndex; // 需要合并的列索引数组
    private int mergeRowIndex; // 合并起始行索引

    public ExcelFillCellMergeStrategyUtil(int mergeRowIndex, int[] mergeColumnIndex) {
        this.mergeRowIndex = mergeRowIndex;
        this.mergeColumnIndex = mergeColumnIndex;
    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder,
                                 List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex,
                                 Boolean isHead) {
        int curRowIndex = cell.getRowIndex();
        int curColIndex = cell.getColumnIndex();
        Sheet sheet = writeSheetHolder.getSheet();

        if (curRowIndex > mergeRowIndex) {
            for (int colIndex : mergeColumnIndex) {
                if (curColIndex == colIndex) {
                    mergeWithPrevRow(sheet, cell, curRowIndex, curColIndex);
                    break;
                }
            }
        }
    }

    private void mergeWithPrevRow(Sheet sheet, Cell cell, int curRowIndex, int curColIndex) {
        Object curData = cell.getStringCellValue();
        Row preRow = sheet.getRow(curRowIndex - 1);
        if (preRow != null) {
            Cell preCell = preRow.getCell(curColIndex);
            if (preCell != null) {
                Object preData = preCell.getStringCellValue();
                if (curData.equals(preData)) {
                    List<CellRangeAddress> mergeRegions = sheet.getMergedRegions();
                    boolean merged = false;
                    for (int i = 0; i < mergeRegions.size() && !merged; i++) {
                        CellRangeAddress cellRangeAddr = mergeRegions.get(i);
                        if (cellRangeAddr.isInRange(curRowIndex - 1, curColIndex)) {
                            sheet.removeMergedRegion(i);
                            cellRangeAddr.setLastRow(curRowIndex);
                            sheet.addMergedRegion(cellRangeAddr);
                            merged = true;
                        }
                    }
                    if (!merged) {
                        CellRangeAddress cellRangeAddress = new CellRangeAddress(curRowIndex - 1, curRowIndex,
                                curColIndex, curColIndex);
                        sheet.addMergedRegion(cellRangeAddress);
                    }
                }
            }
        }
    }
}
