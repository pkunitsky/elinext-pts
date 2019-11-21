package com.elinext.pts.rest.core.bl.workflow;


import com.elinext.pts.rest.core.dal.repository.TimeReportRepository;
import com.elinext.pts.rest.model.entity.TimeReport;
import com.elinext.pts.rest.presentation.dto.TimeReportDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class TimeReportWorkflowTest {

    @Mock
    private TimeReportRepository timeReportRepository;

    private List<TimeReport> timeReports;
    @Before
    public void initProjects() {
        timeReports = new ArrayList<>();
        TimeReport firstTimeReport = new TimeReport();
        firstTimeReport.setId(1L);
        firstTimeReport.setHours(8.00);
        TimeReport secondTimeReport = new TimeReport();
        secondTimeReport.setId(2L);
        secondTimeReport.setPercentage(100);
        timeReports.add(firstTimeReport);
        timeReports.add(secondTimeReport);
    }

    @Before
    public void initProjectDto() {
        TimeReportDto timeReportDto = new TimeReportDto();
        timeReportDto.setId(1L);
        timeReportDto.setReportedDate(LocalDateTime.now());
    }

    @Test
    public void testAllProjectTest() {
        when(timeReportRepository.findAll()).thenReturn(timeReports);
        List<TimeReport> timeReports = timeReportRepository.findAll();
        assertEquals(2, timeReports.size());
    }
}
