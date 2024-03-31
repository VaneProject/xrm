package com.vane.xrm.write;

import java.lang.reflect.Field;

record WriteUnit(
    Field field,
    String name,
    int order
) {}
