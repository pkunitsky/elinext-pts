package com.elinext.pts.rest.model.reference;

import com.elinext.pts.rest.model.data.FilterType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterCriteria {

    private FilterType filterType;
    private List<String> filterValues;
}