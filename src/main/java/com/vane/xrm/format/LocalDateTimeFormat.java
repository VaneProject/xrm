package com.vane.xrm.format;

import java.time.LocalDateTime;

public class LocalDateTimeFormat extends XrmFormat<LocalDateTime> {
    @Override
    public LocalDateTime format(String s) {
        return LocalDateTime.parse(s);
    }
}
