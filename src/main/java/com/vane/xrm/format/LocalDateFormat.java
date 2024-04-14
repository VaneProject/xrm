package com.vane.xrm.format;

import java.time.LocalDate;

public class LocalDateFormat extends XrmFormat<LocalDate> {
    @Override
    public LocalDate format(Object s) {
        return LocalDate.parse((String) s);
    }
}
