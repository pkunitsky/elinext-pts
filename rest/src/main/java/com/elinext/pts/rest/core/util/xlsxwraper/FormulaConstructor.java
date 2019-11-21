/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.util.xlsxwraper;

/**
 * The interface Formula Constructor. Performs the generation of table formulas.
 * The resulting string can be used to set the formula for the cell.
 *
 * @author Denis Senchenko
 */
public interface FormulaConstructor {
    /**
     * Sum string.
     *
     * @param fromCell the from cell
     * @param toCell   the to cell
     * @return the string
     */
    String sum(CellWrapper fromCell, CellWrapper toCell);

    /**
     * Average string.
     *
     * @param fromCell the from cell
     * @param toCell   the to cell
     * @return the string
     */
    String average(CellWrapper fromCell, CellWrapper toCell);

    /**
     * Count string.
     *
     * @param fromCell the from cell
     * @param toCell   the to cell
     * @return the string
     */
    String count(CellWrapper fromCell, CellWrapper toCell);

    /**
     * Counta string.
     *
     * @param fromCell the from cell
     * @param toCell   the to cell
     * @return the string
     */
    String counta(CellWrapper fromCell, CellWrapper toCell);

    /**
     * If condition string.
     *
     * @param arg1 the arg 1
     * @param arg2 the arg 2
     * @return the string
     */
    String ifCondition(CellWrapper arg1, CellWrapper arg2);

    /**
     * Sum string.
     *
     * @param cell the cell
     * @return the string
     */
    String sum(CellWrapper cell);

    /**
     * Max string.
     *
     * @param fromCell the from cell
     * @param toCell   the to cell
     * @return the string
     */
    String max(CellWrapper fromCell, CellWrapper toCell);

    /**
     * Min string.
     *
     * @param fromCell the from cell
     * @param toCell   the to cell
     * @return the string
     */
    String min(CellWrapper fromCell, CellWrapper toCell);
}
