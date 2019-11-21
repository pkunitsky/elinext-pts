/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.util.xlsxwraper;

import org.apache.poi.ss.usermodel.CellStyle;

/**
 * The interface Cell wrapper.
 *
 * @param <V> the type parameter
 * @param <S> the type parameter
 * @author Denis Senchenko
 */
public interface CellWrapper<V, S extends CellStyle> {

    /**
     * Gets value.
     *
     * @return the value
     */
    V getValue();

    /**
     * Gets style.
     *
     * @return the style
     */
    S getStyle();

    /**
     * Gets address.
     *
     * @return the address
     */
    String getAddress();

    /**
     * Sets value.
     *
     * @param value the value
     * @return the value
     */
    CellWrapper setValue(Object value);

    /**
     * Sets value.
     *
     * @param value     the value
     * @param isFormula the is formula
     * @return the value
     */
    CellWrapper setValue(Object value, boolean isFormula);

    /**
     * Sets style.
     *
     * @param style the style
     * @return the style
     */
    CellWrapper setStyle(CellStyle style);

    /**
     * Sets value and style.
     *
     * @param value the value
     * @param style the style
     * @return the value and style
     */
    CellWrapper setValueAndStyle(V value, S style);

    /**
     * Sets address.
     *
     * @param address the address
     * @return the address
     */
    CellWrapper setAddress(String address);

    /**
     * Is formula boolean.
     *
     * @return the boolean
     */
    boolean isFormula();
}