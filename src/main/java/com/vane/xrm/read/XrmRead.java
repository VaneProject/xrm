package com.vane.xrm.read;

import java.util.List;

interface XrmRead<X> {
    X get(int i);
    List<X> getAll();
}
