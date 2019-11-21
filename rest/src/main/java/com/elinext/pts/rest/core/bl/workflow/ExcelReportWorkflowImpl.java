package com.elinext.pts.rest.core.bl.workflow;

import com.elinext.pts.rest.core.bl.appservice.TimeReportConfigSpecs;
import com.elinext.pts.rest.core.dal.repository.TimeReportRepository;
import com.elinext.pts.rest.core.util.FilterUtil;
import com.elinext.pts.rest.core.util.xlsxwraper.CellWrapper;
import com.elinext.pts.rest.core.util.xlsxwraper.ExcelFormulaConstructor;
import com.elinext.pts.rest.core.util.xlsxwraper.ExcelMapWrapper;
import com.elinext.pts.rest.model.entity.Role;
import com.elinext.pts.rest.model.entity.TimeReport;
import com.elinext.pts.rest.model.reference.FilterCriteria;
import com.elinext.pts.rest.presentation.dto.TimeReportConfigDto;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class ExcelReportWorkflowImpl implements ExcelReportWorkflow {
    private static final String RESULT_FOOTER_NAME = "Total, hours";
    private static final String HEAD = "head";
    private static final List<String> DEFAULT_HEAD_LIST = Arrays.asList("Date", "Person", "Task", "Hours", "Comments");
    private static final String TOTAL = "total";
    private final TimeReportRepository timeReportRepository;

    public FileSystemResource fetchReports(TimeReportConfigDto timeReportConfigDto, Role role, String tableName) throws AccessDeniedException {
        if (isAdminOrManager(role)){
            List<FilterCriteria> filterCriteria = FilterUtil.getFilterCriteria(timeReportConfigDto);
            List<TimeReport> timeReports = timeReportRepository.findAll(TimeReportConfigSpecs.findByCriteria(filterCriteria));
            return createReport(timeReports, tableName);
        }else {
            throw new AccessDeniedException("Access is allowed only for a user with the role of the ADMIN or MANAGER");
        }
    }

    @Override
    public FileSystemResource fetchAllReports(Role role, String tableName) throws AccessDeniedException {
        if (isAdminOrManager(role)){
            List<TimeReport> allReports = timeReportRepository.findAll();
            return createReport(allReports, tableName);
        }else {
            throw new AccessDeniedException("Access is allowed only for a user with the role of the ADMIN or MANAGER");
        }
    }

    @Override
    public FileSystemResource createReport(List<TimeReport> timeReports, String tableHeadName) {
        ExcelMapWrapper workbookWrapper = (ExcelMapWrapper) ExcelMapWrapper.createTable(tableHeadName);
        fillHead(workbookWrapper, tableHeadName);
        int rowId = 1;
        for (TimeReport timeReport: timeReports){
            CellStyle cellStyle = bodyStyle(workbookWrapper.getWorkbook());
            String rowName = String.valueOf(rowId);
            workbookWrapper.addNextRow(rowName);
                workbookWrapper.getCell(rowName, "Date").ifPresent(cell -> cell.setValue(timeReport.getReportedDate().toString()).setStyle(cellStyle));
                workbookWrapper.getCell(rowName, "Person").ifPresent(cell -> cell.setValue(timeReport.getReporter().getEmail()).setStyle(cellStyle));
                workbookWrapper.getCell(rowName, "Task").ifPresent(cell -> cell.setValue(timeReport.getTask().getName()).setStyle(cellStyle));
                workbookWrapper.getCell(rowName, "Hours").ifPresent(cell -> cell.setValue(timeReport.getHours()).setStyle(hoursStyle(workbookWrapper.getWorkbook())));
                workbookWrapper.getCell(rowName, "Comments").ifPresent(cell -> cell.setValue(timeReport.getComment()).setStyle(cellStyle));
            rowId = rowId + 1;
        }
        fillTotalRow(timeReports, workbookWrapper);
        return new FileSystemResource(workbookWrapper.exportToXlsx(tableHeadName));
    }

    private void fillHead(ExcelMapWrapper workbookWrapper, String tableHeadName) {
        workbookWrapper.addNextRow(tableHeadName);
        DEFAULT_HEAD_LIST.forEach(workbookWrapper::addNextColumn);
        workbookWrapper.getCell(tableHeadName, "Date").orElseThrow().setValue(tableHeadName);
        DEFAULT_HEAD_LIST.forEach(value -> workbookWrapper.getCell(tableHeadName, value).ifPresent(cell -> cell
                .setStyle(headerStyle(workbookWrapper.getWorkbook()))));
        workbookWrapper.addNextRow(HEAD);
        DEFAULT_HEAD_LIST.forEach(value -> workbookWrapper.getCell(HEAD, value).ifPresent(cell -> cell
                .setValue(value)
                .setStyle(headerStyle(workbookWrapper.getWorkbook()))));
    }

    private void fillTotalRow(List<TimeReport> timeReports, ExcelMapWrapper workbookWrapper) {
        workbookWrapper.addNextRow(TOTAL);
        CellWrapper cellFromHours = workbookWrapper.getCell("1", "Hours").orElseThrow();
        CellWrapper cellToHours = workbookWrapper.getCell(String.valueOf(timeReports.size()), "Hours").orElseThrow();
        workbookWrapper.getCell(TOTAL, "Hours").ifPresent(cell -> cell.setValue(new ExcelFormulaConstructor().sum(cellFromHours, cellToHours), true));
        workbookWrapper.getCell(TOTAL, "Date").ifPresent(cellWrapper -> cellWrapper.setValue(RESULT_FOOTER_NAME));
        workbookWrapper.getRow(TOTAL).orElseThrow().values().forEach(cell -> cell.setStyle(headerStyle(workbookWrapper.getWorkbook())));
        workbookWrapper.getCell(TOTAL, "Date").orElseThrow().getStyle().setAlignment(HorizontalAlignment.LEFT);
    }

    private CellStyle headerStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFFont font = (XSSFFont) wb.createFont();
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(true);
        style.setFont(font);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle bodyStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        XSSFFont font = (XSSFFont) wb.createFont();
        font.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setWrapText(true);
        return style;
    }

    private CellStyle hoursStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        XSSFFont font = (XSSFFont) wb.createFont();
        font.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setWrapText(true);
        return style;
    }
}
