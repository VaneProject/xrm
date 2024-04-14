package com.vane.xrm.format;

public class VoidFormat extends XrmFormat<Void> {
    @Override
    public Void format(Object s) {
        return null;
    }
}
