package com.vane.xrm.exception;

import org.apache.poi.ss.usermodel.Cell;

class XrmException extends RuntimeException {
    XrmException(String message) {
        super(message);
    }

    XrmException(Cell cell, String message) {
        super(
            String.format("(Sheet: %s, Row: %d, Column: %d) ",
                cell.getSheet().getSheetName(),
                cell.getRowIndex(),
                cell.getColumnIndex()) + message
        );
    }
}
