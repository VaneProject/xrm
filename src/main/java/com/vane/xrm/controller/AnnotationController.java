package com.vane.xrm.controller;

import com.vane.xrm.exception.XrmFormatException;
import com.vane.xrm.exception.XrmTypeException;
import com.vane.xrm.format.XrmFormat;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

abstract class AnnotationController<X> {
    private final Map<Class<? extends XrmFormat<?>>, XrmFormat<?>> formats = new HashMap<>();
    private final Map<Class<?>, Function<String, ?>> converter = new HashMap<>() {{
        put(String.class, v -> v);
        // numbers
        put(byte.class, Byte::parseByte);
        put(Byte.class, Byte::parseByte);
        put(short.class, Short::parseShort);
        put(Short.class, Short::parseShort);
        put(int.class, Integer::parseInt);
        put(Integer.class, Integer::parseInt);
        put(long.class, Long::parseLong);
        put(Long.class, Long::parseLong);
        put(float.class, Float::parseFloat);
        put(Float.class, Float::parseFloat);
        put(double.class, Double::parseDouble);
        put(Double.class, Double::parseDouble);
    }};

    /**
     * createInstance
     * @param data data
     * @return create type
     */
    protected abstract X createInstance(Map<String, Object> data);

    /**
     * type convert
     * @param field field
     * @param value origin value
     * @return convert value
     */
    protected final Object getData(@NotNull Field field, String value) {
        Class<?> type = field.getType();
        if (converter.containsKey(type))
            return converter.get(type).apply(value);
        throw new XrmTypeException("Do not convert " + type.getName() + " type");
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
     * @param formats format list
     */
    protected final void addConverter(Class<? extends XrmFormat<?>>[] formats) {
        try {
            for (Class<? extends XrmFormat<?>> format : formats) {
                if (!this.formats.containsKey(format)) {
                    Class<?> formatClass = this.getGenericType(format);
                    if (formatClass == null)
                        throw new XrmFormatException("Format class type error " + format);
                    XrmFormat<?> xrmFormat = format.getDeclaredConstructor().newInstance();
                    this.formats.put(format, xrmFormat);
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * return XrmFormat generic class
     * @param formatKlass XrmFormat class
     * @return generic type
     */
    private Class<?> getGenericType(Class<? extends XrmFormat<?>> formatKlass) {
        if (formatKlass.getGenericSuperclass() instanceof ParameterizedType type) {
            Type[] actualTypeArguments = type.getActualTypeArguments();
            if (actualTypeArguments.length > 0 && actualTypeArguments[0] instanceof Class<?> klass)
                return klass;
        }
        return null;
    }

    /**
     * set data
     * @param x object instance
     * @param field filed type
     * @param value field value
     */
    protected final void setData(X x, @NotNull Field field, Object value) throws IllegalAccessException {
        Class<?> type = field.getType();
        if (value instanceof Double number) {
            if (type == byte.class) field.setByte(x, number.byteValue());
            else if (type == short.class) field.setShort(x, number.shortValue());
            else if (type == int.class) field.setInt(x, number.intValue());
            else if (type == long.class) field.setLong(x, number.longValue());
            else if (type == float.class) field.setFloat(x, number.floatValue());
            else if (type == double.class) field.setDouble(x, number);
            // wrapper type
            else if (type == Byte.class) field.set(x, number.byteValue());
            else if (type == Short.class) field.set(x, number.shortValue());
            else if (type == Integer.class) field.set(x, number.intValue());
            else if (type == Long.class) field.set(x, number.longValue());
            else if (type == Float.class) field.set(x, number.floatValue());
            else if (type == Double.class) field.set(x, number);
            else throw new XrmTypeException(type, value);
        } else field.set(x, value);
    }
}
