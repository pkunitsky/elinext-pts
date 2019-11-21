/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.util.xlsxwraper;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * The type Excel map wrapper.
 *
 * @author Denis Senchenko
 */
public class ExcelMapWrapper implements WorkbookWrapper<String, CellWrapper> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String EXCEL_REPORT_FOLDER_NAME = "reports";
    private static final String SLSX = "%s.xlsx";
    private Map<String, Map<String, CellWrapper>> mapTable;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    @NotNull
    public XSSFWorkbook getWorkbook() {
        return workbook;
    }

    private ExcelMapWrapper(String sheetName) {
        this.workbook = new XSSFWorkbook();
        this.mapTable = new LinkedHashMap<>();
        this.sheet = workbook.createSheet(sheetName);
    }

    /**
     * Create table workbook wrapper.
     *
     * @param sheetName the sheet name
     * @return the workbook wrapper
     */
    @NotNull
    public static WorkbookWrapper createTable(@NotNull String sheetName) {
        return new ExcelMapWrapper(sheetName);
    }

    @Override
    @NotNull
    public Optional<Map<String, CellWrapper>> getRow(@NotNull String rowName) {
        Map<String, CellWrapper> stringCellWrapperMap = mapTable.get(rowName);
        stringCellWrapperMap.entrySet().stream().map(t -> t.getValue().setAddress(getExcelBasedAddress(rowName, t.getKey())));
        return Optional.ofNullable(stringCellWrapperMap);
    }

    @Override
    @NotNull
    public Optional<Map<String, CellWrapper>> getColumn(@NotNull String columnName) {
        Map<String, CellWrapper> map = new LinkedHashMap<>();
        mapTable.forEach((key1, value1) -> map.put(key1, value1.get(columnName)));
        map.forEach((key, value) -> value.setAddress(getExcelBasedAddress(key, columnName)));
        return Optional.of(map);
    }

    @Override
    @NotNull
    public WorkbookWrapper addNextRow(@NotNull String rowName) {
        LinkedHashMap<String, CellWrapper> value = new LinkedHashMap<>();
        if (!mapTable.isEmpty()) {
            Map<String, CellWrapper> stringCellWrapperMap = mapTable.values().stream().findAny().orElseThrow(UnsupportedOperationException::new);
            stringCellWrapperMap.keySet().forEach(column -> value.put(column, new ExcelCellWrapper()));
        }
        mapTable.put(rowName, value);
        return this;
    }

    @Override
    @NotNull
    public WorkbookWrapper addNextColumn(@NotNull String columnName) {
        mapTable.values().forEach(row -> row.put(columnName, new ExcelCellWrapper()));
        return this;
    }

    @Override
    @NotNull
    public Optional<CellWrapper> getCell(@NotNull String rowName, @NotNull String columnName) {
        CellWrapper cellWrapper = mapTable.get(rowName) != null ? mapTable.get(rowName).get(columnName) : null;
        if (cellWrapper != null) {
            cellWrapper.setAddress(getExcelBasedAddress(rowName, columnName));
        }
        return Optional.ofNullable(cellWrapper);
    }

    private String getExcelBasedAddress(@NotNull String rowName, @NotNull String columnName) {
        int col = new ArrayList<>(mapTable.get(rowName).keySet()).indexOf(columnName);
        int row = new ArrayList<>(mapTable.keySet()).indexOf(rowName);
        return CellReference.convertNumToColString(col) + (row+1);
    }

    @Override
    @NotNull
    public WorkbookWrapper setRowStyle(@NotNull String rowName, @NotNull CellStyle cellStyle) {
        Map<String, CellWrapper> cellWrapperMap = mapTable.get(rowName);
        if (cellWrapperMap != null){
            cellWrapperMap.values().forEach(row -> row.setStyle(cellStyle));
        }
        return this;
    }

    @Override
    @NotNull
    public WorkbookWrapper setRowsStyle(@NotNull List<String> rowNames, @NotNull CellStyle cellStyle) {
        rowNames.forEach(row -> setRowStyle(row, cellStyle));
        return this;
    }

    @Override
    @NotNull
    public WorkbookWrapper setColumnStyle(@NotNull String columnName, @NotNull CellStyle cellStyle) {
        Objects.requireNonNull(getColumn(columnName)).ifPresent(cellWrapperMap -> cellWrapperMap.values().forEach(column -> column.setStyle(cellStyle)));
        return this;
    }

    @Override
    @NotNull
    public WorkbookWrapper setColumnsStyle(@NotNull List<String> columnsNames, @NotNull CellStyle cellStyle) {
        columnsNames.forEach(column -> setColumnStyle(column, cellStyle));
        return this;
    }


    @Override
    @NotNull
    public WorkbookWrapper setCellStyle(@NotNull String rowName, @NotNull String columnName, @NotNull CellStyle cellStyle) {
        CellWrapper cellWrapper = mapTable.get(rowName).get(columnName);
        if (cellWrapper != null){
            cellWrapper.setStyle(cellStyle);
        }
        return this;
    }

    @Override
    public String exportToXlsx(@NotNull String fileName) {
        int rowNum = 0;
        for (Map<String, CellWrapper> next : mapTable.values()) {
            XSSFRow row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (CellWrapper cellWrapper : next.values()) {
                sheet.autoSizeColumn(colNum);
                Object value = cellWrapper.getValue();
                CellStyle style = cellWrapper.getStyle();
                XSSFCell cell = row.createCell(colNum++);

                if (cellWrapper.isFormula()) {
                    cell.setCellFormula(value.toString());
                } else if (value instanceof String) {
                    cell.setCellValue((String) value);
                } else if (value instanceof Integer) {
                    cell.setCellValue((Integer) value);
                } else if (value instanceof Double) {
                    cell.setCellValue((Double) value);
                }
                cell.setCellStyle(style);
            }
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0 , mapTable.values().stream().findFirst().orElseThrow().size() -1 ));
        sheet.addMergedRegion(new CellRangeAddress(mapTable.size() - 1, mapTable.size() - 1, 0 , 2));
        return writeToFile(fileName);
    }

    private String writeToFile(@NotNull String filePath) {
        filePath = String.format(SLSX, EXCEL_REPORT_FOLDER_NAME + "/" + filePath);
        try (FileOutputStream outputStream = FileUtils.openOutputStream(new File(filePath))){
            workbook.write(outputStream);
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.ERROR, "File not found", e);
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, "I/O exception", e);
        }
        return filePath;
    }
}