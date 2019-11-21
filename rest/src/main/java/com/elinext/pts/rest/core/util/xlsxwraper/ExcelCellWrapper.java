/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.util.xlsxwraper;

import org.apache.poi.ss.usermodel.CellStyle;

/**
 * The type Excel cell wrapper.
 *
 * @author Denis Senchenko
 */
public class ExcelCellWrapper implements CellWrapper<Object, CellStyle> {
    private Object value;
    private CellStyle style;
    private String address;
    private boolean isFormula;

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public CellStyle getStyle() {
        return style;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public CellWrapper setValue(Object value) {
        this.value = value;
        return this;
    }

    @Override
    public CellWrapper setValue(Object value, boolean isFormula) {
        this.value = value;
        this.isFormula = isFormula;
        return null;
    }

    @Override
    public CellWrapper setStyle(CellStyle style) {
        this.style = style;
        return this;
    }

    @Override
    public CellWrapper setValueAndStyle(Object value, CellStyle style) {
        this.value = value;
        this.style = style;
        return this;
    }

    @Override
    public CellWrapper setAddress(String address) {
        this.address = address;
        return this;
    }

    @Override
    public boolean isFormula() {
        return isFormula;
    }
}