/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.util.xlsxwraper;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The interface Workbook wrapper.
 *
 * @param <K> the type parameter
 * @param <V> the type parameter
 * @author Denis Senchenko
 */
public interface WorkbookWrapper<K, V extends CellWrapper> {

    /**
     * Gets row.
     *
     * @param rowName the row name
     * @return the row
     */
    @NotNull
    Optional<Map<K, V>> getRow(String rowName);

    /**
     * Gets column.
     *
     * @param columnName the column name
     * @return the column
     */
    @NotNull
    Optional<Map<K, V>> getColumn(String columnName);

    /**
     * Add next row workbook wrapper.
     *
     * @param rowName the row name
     * @return the workbook wrapper
     */
    @NotNull
    WorkbookWrapper addNextRow(String rowName);

    /**
     * Add next column workbook wrapper.
     *
     * @param columnName the column name
     * @return the workbook wrapper
     */
    @NotNull
    WorkbookWrapper addNextColumn(String columnName);

    /**
     * Gets cell.
     *
     * @param rowName    the row name
     * @param columnName the column name
     * @return the cell
     */
    @NotNull
    Optional<CellWrapper> getCell(String rowName, String columnName);

    /**
     * Sets row style.
     *
     * @param rowName   the row name
     * @param cellStyle the cell style
     * @return the row style
     */
    @NotNull
    WorkbookWrapper setRowStyle(K rowName, CellStyle cellStyle);

    /**
     * Sets rows style.
     *
     * @param rowNames  the row names
     * @param cellStyle the cell style
     * @return the rows style
     */
    @NotNull
    WorkbookWrapper setRowsStyle(List<K> rowNames, CellStyle cellStyle);

    /**
     * Sets column style.
     *
     * @param columnName the column name
     * @param cellStyle  the cell style
     * @return the column style
     */
    @NotNull
    WorkbookWrapper setColumnStyle(String columnName, CellStyle cellStyle);

    /**
     * Sets columns style.
     *
     * @param columnsNames the columns names
     * @param cellStyle    the cell style
     * @return the columns style
     */
    @NotNull
    WorkbookWrapper setColumnsStyle(List<String> columnsNames, CellStyle cellStyle);

    /**
     * Sets cell style.
     *
     * @param rowName    the row name
     * @param columnName the column name
     * @param cellStyle  the cell style
     * @return the cell style
     */
    @NotNull
    WorkbookWrapper setCellStyle(String rowName, String columnName, CellStyle cellStyle);

    /**
     * Export to xlsx.
     *
     * @param fileName the file name
     */
    String exportToXlsx(String fileName);

    /**
     * Gets workbook.
     *
     * @return the workbook
     */
    @NotNull
    Workbook getWorkbook();
}