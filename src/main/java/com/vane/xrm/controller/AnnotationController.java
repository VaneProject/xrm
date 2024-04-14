package com.vane.xrm.controller;

import com.vane.xrm.controller.type.PrimitiveType;
import com.vane.xrm.exception.XrmFormatException;
import com.vane.xrm.exception.XrmInstanceException;
import com.vane.xrm.exception.XrmTypeException;
import com.vane.xrm.format.VoidFormat;
import com.vane.xrm.format.XrmFormat;
import com.vane.xrm.items.XrmHeader;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * controller center class
 * @param <X> instance type
 * @param <A> annotation type
 */
abstract class AnnotationController<X, A extends Annotation> {
    // format class repository
    private final Map<Class<? extends XrmFormat<?>>, XrmFormat<?>> formats = new HashMap<>();
    // X type field information
    private final Map<String, Field> fields = new HashMap<>();
    // class type, annotation type
    protected final Class<X> type;
    protected final Class<A> annotationType;
    private final PrimitiveType<X> primitiveType;

    @SuppressWarnings("unchecked")
    AnnotationController(Class<X> type, PrimitiveType<X> primitiveType) {
        this.primitiveType = primitiveType;
        this.type = type;
        this.annotationType = (Class<A>) this.getGeneric(getClass().getSuperclass(), 1);
        if (annotationType == null)
            throw new RuntimeException("Development error");
        // read type all fields
        for (Field field : type.getDeclaredFields()) {
            A a = field.getAnnotation(this.annotationType);
            String name;
            if (a == null) name = field.getName();
            else if (isNotUsed(a)) continue;
            else name = getFieldName(a);
            // default value
            if (name.isEmpty())
                name = field.getName();
            // check same name
            if (fields.containsKey(name))
                throw new XrmInstanceException("Duplicate field name: " + name);
            fields.put(name, field);
        }
    }

    /**
     * @return get headers
     */
    protected abstract XrmHeader[] getHeaders();

    /**
     * Annotation Function <br>
     * notUsed method <br>
     * default value: false
     * @param a annotation type
     * @return if is not used return false
     */
    private boolean isNotUsed(@NotNull A a) {
        try {
            return (boolean) annotationType.getMethod("notUsed").invoke(a);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return false;
        }
    }

    /**
     * Annotation Function <br>
     * get default or field name
     * @param a annotation
     * @return field names
     */
    private @NotNull String getFieldName(@NotNull A a) {
        try {
            return (String) annotationType.getMethod("value").invoke(a);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Development error");
        }
    }

    /**
     * find method by name
     * @return method or null
     */
    private Method findMethodFormat() {
        for (Method method : annotationType.getMethods()) {
            if (Objects.equals("format", method.getName()))
                return method;
        }
        return null;
    }

    /**
     * createInstance
     * @param data data
     * @return create type
     */
    @SuppressWarnings("unchecked")
    protected final X createInstance(final Map<String, Object> data) {
        try {
            final X x = type.getDeclaredConstructor().newInstance();
            final Method formatMethod = findMethodFormat();
            for (XrmHeader header : getHeaders()) {
                final String key = header.key();
                final Field field = fields.get(key);
                if (field == null || !data.containsKey(key))
                    continue;

                Object value = data.get(key);
                A a = field.getAnnotation(annotationType);
                field.setAccessible(true);
                if (a != null && formatMethod != null) {
                    Class<? extends XrmFormat<?>> formatKlass = (Class<? extends XrmFormat<?>>) formatMethod.invoke(a);
                    if (VoidFormat.class != formatKlass) {
                        XrmFormat<?> format = this.addConverter(formatKlass);
                        this.primitiveType.setTypes(x, field, format.format(value));
                        continue;
                    }
                }
                this.primitiveType.setTypes(x, field, value);
            }
            return x;
        } catch (InstantiationException e) {
            throw new XrmInstanceException("Instance failed. " + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new XrmInstanceException("Cannot access the corresponding creator.");
        } catch (NoSuchMethodException e) {
            throw new XrmInstanceException("Default constructor does not exist.");
        } catch (InvocationTargetException e) {
            throw new XrmInstanceException("An exception occurred in the called constructor.");
        }
    }

    /**
     * custom type convert
     * @param formats custom type
     * @param field field
     * @param value origin value
     * @return convert value
     */
    protected final Object getData(Class<? extends XrmFormat<?>>[] formats, @NotNull Field field, String value) {
        Class<?> type = field.getType();
        for (Class<? extends XrmFormat<?>> format : formats) {
            if (this.formats.containsKey(format))
                return this.formats.get(format).format(value);
        }
        throw new XrmTypeException("Do not convert " + type.getName() + " type");
    }

    /**
     * add format class
     * @param format format list
     */
    private XrmFormat<?> addConverter(Class<? extends XrmFormat<?>> format) {
        try {
            if (! this.formats.containsKey(format)) {
                Class<?> formatClass = getGeneric(format, 0);
                if (formatClass == null)
                    throw new XrmFormatException("Format class type error " + format);
                XrmFormat<?> xrmFormat = format.getDeclaredConstructor().newInstance();
                this.formats.put(format, xrmFormat);
                return xrmFormat;
            }
            return this.formats.get(format);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * return XrmFormat generic class
     * @param type XrmFormat class
     * @param i index value
     * @return generic type or null
     */
    private Class<?> getGeneric(Class<?> type, int i) {
        if (type.getGenericSuperclass() instanceof ParameterizedType parameterizedType) {
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length > i && actualTypeArguments[i] instanceof Class<?> clazz)
                return clazz;
        }
        return null;
    }
}
