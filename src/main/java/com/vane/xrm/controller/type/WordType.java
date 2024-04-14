package com.vane.xrm.controller.type;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Function;

public final class WordType<X> extends PrimitiveType<X> {
    private static final Map<Class<?>, Function<String, Object>> wrapperNumbers = Map.of(
            Byte.class, Byte::parseByte,
            Short.class, Short::parseShort,
            Integer.class, Integer::parseInt,
            Long.class, Long::parseLong,
            Float.class, Float::parseFloat,
            Double.class, Double::parseDouble
    );

    private static final Map<Class<?>, Function<String, Object>> primitiveNumbers = Map.of(
            byte.class, Byte::parseByte,
            short.class, Short::parseShort,
            int.class, Integer::parseInt,
            long.class, Long::parseLong,
            float.class, Float::parseFloat,
            double.class, Double::parseDouble
    );

    @Override
    boolean isWrapperNumber(Class<?> type) {
        return wrapperNumbers.containsKey(type);
    }

    @Override
    public void setTypes(X x, Field field, Object value) {
        Class<?> type = field.getType();
        try {
            if (type.isPrimitive()) {
                if (value instanceof String str) setPrimitive(x, field, str);
                else setPrimitive(x, field, value);
            } else if (value instanceof String str) setWrapper(x, field, str);
            else field.set(x, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void setWrapper(X x, Field field, String value) throws IllegalAccessException {
        Class<?> type = field.getType();
        if (isWrapperNumber(type)) {
            if (isNotNumber(type))
                throw new ClassCastException("Cannot cast " + value + " to a Number");
            field.set(x, wrapperNumbers.get(type).apply(value));
        } else if (type == Character.class) {
            // character casting
            if (value.length() == 1) field.set(x, value.charAt(0));
            else throw new ClassCastException("Cannot cast " + value + " to a Character");
        } else if (type == Boolean.class)
            field.set(x, Boolean.parseBoolean(value));
        else if (type == String.class)
            field.set(x, value);
        else throw new ClassCastException("Cannot cast " + value + " to a " + type);
    }

    /**
     * set primitive values
     * @param x class type
     * @param field field
     * @param value origin value
     * @throws IllegalAccessException if you can not access
     */
    private void setPrimitive(X x, Field field, String value) throws IllegalAccessException {
        Class<?> type = field.getType();
        if (type == boolean.class)
            field.setBoolean(x, Boolean.parseBoolean(value));
        else if (type == char.class) {
            if (value.length() == 1) field.setChar(x, value.charAt(0));
            else throw new ClassCastException("Cannot cast " + value + " to " + type);
        } else if (isPrimitiveNumber(type)) {
            Object number = primitiveNumbers.get(type).apply(value);
            super.setPrimitive(x, field, number);
        } else throw new ClassCastException("Cannot cast " + value + " to " + type);
    }
}
