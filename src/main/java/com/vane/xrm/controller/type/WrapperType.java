package com.vane.xrm.controller.type;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Function;

public final class WrapperType<X> extends PrimitiveType<X> {
    private static final Map<Class<?>, Function<Number, Object>> wrapperNumbers = Map.of(
            Byte.class, Number::byteValue,
            Short.class, Number::shortValue,
            Integer.class, Number::intValue,
            Long.class, Number::longValue,
            Float.class, Number::floatValue,
            Double.class, Number::doubleValue
    );

    @Override
    boolean isWrapperNumber(Class<?> type) {
        return wrapperNumbers.containsKey(type);
    }

    @Override
    public void setTypes(X x, @NotNull Field field, Object value) {
        Class<?> type = field.getType();
        try {
            if (type.isPrimitive())
                this.setPrimitive(x, field, value);
            else setWrapper(x, field, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * set wrapper
     * @param x instance data
     * @param field field
     * @param value origin value
     * @throws IllegalAccessException if you do not access
     */
    private void setWrapper(X x, Field field, Object value) throws IllegalAccessException {
        Class<?> type = field.getType();
        if (isWrapperNumber(type)) {
            // origin type check
            if (isNotNumber(value.getClass()))
                throw new ClassCastException("Cannot cast " + value + " to a Number");
            field.set(x, wrapperNumbers.get(type).apply((Number) value));
        } else if (type == Character.class) {
            // character casting
            if (value instanceof Character c) field.set(x, c);
            else if (value instanceof String str && str.length() == 1) field.set(x, str.charAt(0));
            else throw new ClassCastException("Cannot cast " + value + " to a Character");
        } else {
            super.checkType(field, value);
            field.set(x, value);
        }
    }
}
