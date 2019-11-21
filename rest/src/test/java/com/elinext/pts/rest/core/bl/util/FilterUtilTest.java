package com.elinext.pts.rest.core.bl.util;

import com.elinext.pts.rest.core.util.FilterUtil;
import com.elinext.pts.rest.model.data.FilterType;
import com.elinext.pts.rest.model.entity.Status;
import com.elinext.pts.rest.model.reference.FilterCriteria;
import com.elinext.pts.rest.presentation.dto.TimeReportConfigDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class FilterUtilTest {

    private TimeReportConfigDto timeReportConfigDto;
    private TimeReportConfigDto timeReportConfigDtoWithNull;

    @Before
    public void initTimeReportConfigDto() {
        timeReportConfigDto = new TimeReportConfigDto();
        LinkedHashMap<FilterType, List<String>> filter = new LinkedHashMap<>();
        filter.put(FilterType.PROJECT_STATUS, List.of(Status.ACTIVE.name(), Status.COMPLETED.name()));
        timeReportConfigDto.setFilters(filter);
    }

    @Before
    public void initTimeReportConfigDtoWithNull() {
        timeReportConfigDtoWithNull = new TimeReportConfigDto();
        timeReportConfigDtoWithNull.setFilters(null);
    }

    @Test
    public void testFilterCriteria() {
        List<FilterCriteria> filterCriteria = FilterUtil.getFilterCriteria(timeReportConfigDto);
        assertEquals(FilterType.PROJECT_STATUS, filterCriteria.get(0).getFilterType());
        assertEquals(filterCriteria.get(0).getFilterValues().size(), 2);
    }

    @Test
    public void testFilterCriteriaWithNull() {
        List<FilterCriteria> filterCriteria = FilterUtil.getFilterCriteria(timeReportConfigDtoWithNull);
        assertEquals(filterCriteria.size(), 0);
    }
}
