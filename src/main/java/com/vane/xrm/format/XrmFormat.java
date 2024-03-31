package com.vane.xrm.format;

public abstract class XrmFormat<TYPE> {
    public XrmFormat() {
    }

    public abstract TYPE format(String s);
}
