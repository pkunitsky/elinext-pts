/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.util.xlsxwraper;

/**
 * The type Excel Formula constructor.
 *
 * @author Denis Senchenko
 */
public class ExcelFormulaConstructor implements FormulaConstructor {

    @Override
    public String sum(CellWrapper fromCell, CellWrapper toCell) {
        return String.format("SUM(%s:%s)", fromCell.getAddress(), toCell.getAddress());
    }

    @Override
    public String average(CellWrapper fromCell, CellWrapper toCell) {
        return String.format("AVERAGE(%s:%s)", fromCell.getAddress(), toCell.getAddress());
    }

    @Override
    public String count(CellWrapper fromCell, CellWrapper toCell) {
        return String.format("COUNT(%s:%s)", fromCell.getAddress(), toCell.getAddress());
    }

    @Override
    public String counta(CellWrapper fromCell, CellWrapper toCell) {
        return String.format("COUNTA(%s:%s)", fromCell.getAddress(), toCell.getAddress());
    }

    @Override
    public String ifCondition(CellWrapper arg1, CellWrapper arg2) {
        return String.format("IF(%s<%s, ‘TRUE,’ ‘FALSE’)", arg1.getAddress(), arg2.getAddress());
    }

    @Override
    public String sum(CellWrapper cell) {
        return String.format("TRIM(%s)", cell.getAddress());
    }

    @Override
    public String max(CellWrapper fromCell, CellWrapper toCell) {
        return String.format("MAX(%s:%s)", fromCell.getAddress(), toCell.getAddress());
    }

    @Override
    public String min(CellWrapper fromCell, CellWrapper toCell) {
        return String.format("MIN(%s:%s)", fromCell.getAddress(), toCell.getAddress());
    }
}