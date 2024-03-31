package com.vane.xrm.format;

import java.time.LocalDate;

public class LocalDateFormat extends XrmFormat<LocalDate> {
    @Override
    public LocalDate format(String s) {
        return LocalDate.parse(s);
    }
}
