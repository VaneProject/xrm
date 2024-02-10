package com.vane.xrm.exception;

import org.apache.poi.ss.usermodel.Cell;

public class XrmIsNotStringException extends XrmException {
    public XrmIsNotStringException(Cell cell, String message) {
        super(cell, message);
    }
}
