package com.vane.xrm;

import com.vane.xrm.format.VoidFormat;
import com.vane.xrm.format.XrmFormat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Csv {
    String value() default "";

    boolean notUsed() default false;

    Class<? extends XrmFormat<?>> format() default VoidFormat.class;
}
