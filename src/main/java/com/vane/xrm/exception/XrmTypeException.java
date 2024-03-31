package com.vane.xrm.exception;

import org.apache.poi.ss.usermodel.Cell;

public class XrmTypeException extends XrmException {
    public XrmTypeException(Cell cell, String message) {
        super(cell, message);
    }

    public XrmTypeException(String message) {
        super(message);
    }

    public XrmTypeException(Class<?> type, Object value) {
        super("Type match error " + type.getTypeName() + " to " + value.getClass().getTypeName());
    }
}
