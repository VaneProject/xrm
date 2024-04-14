package com.vane.xrm.controller.type;

import com.vane.xrm.exception.XrmInstanceException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Function;

public abstract class PrimitiveType<X> {
    // primitive list and converter
    private static final Map<Class<?>, Function<Number, Object>> primitiveNumbers = Map.of(
            byte.class, Number::byteValue,
            short.class, Number::shortValue,
            int.class, Number::intValue,
            long.class, Number::longValue,
            float.class, Number::floatValue,
            double.class, Number::doubleValue
    );

    final boolean isPrimitiveNumber(Class<?> type) {
        return primitiveNumbers.containsKey(type);
    }

    final boolean isNotNumber(Class<?> type) {
        return !isPrimitiveNumber(type) && !isWrapperNumber(type);
    }

    abstract boolean isWrapperNumber(Class<?> type);

    public abstract void setTypes(X x, Field field, Object value);

    PrimitiveType() {
    }

    /**
     * type check
     * @param field variable fields
     * @param value value
     */
    void checkType(Field field, Object value) {
        if (field.getType() != value.getClass())
            throw new XrmInstanceException(value.getClass() + " is not a " + field.getType());
    }

    /**
     * set primitive number
     * byte, short, int, long, float, double type only
     * @param x instance data
     * @param field field
     * @param value origin value
     * @throws IllegalAccessException if you do not access
     */
    final void setPrimitive(X x, @NotNull Field field, Object value) throws IllegalAccessException {
        Class<?> type = field.getType();
        if (type == boolean.class) {
            checkType(field, value);
            field.setBoolean(x, (boolean) value);
        } else if (type == char.class) {
            if (value instanceof Character c) field.setChar(x, c);
            else if (value instanceof String str && str.length() == 1)
                field.setChar(x, str.charAt(0));
            else throw new ClassCastException("Cannot cast " + value + " to char");
        } else if (isPrimitiveNumber(type)) {
            if (isNotNumber(value.getClass()))
                throw new ClassCastException("Cannot cast " + value + " to a Number");
            Number number = (Number) primitiveNumbers.get(type).apply((Number) value);
            setPrimitiveNumber(x, field, number);
        } else throw new ClassCastException("Cannot cast " + value + " to a primitive type");
    }

    /**
     * set primitive
     * byte, short, int, long, float, double type only
     * @param x instance data
     * @param field field
     * @param value origin value
     * @throws IllegalAccessException if you do not access
     */
    private void setPrimitiveNumber(X x, Field field, Number value) throws IllegalAccessException {
        Class<?> type = field.getType();
        if (type == byte.class) field.setByte(x, (byte) value);
        else if (type == short.class) field.setShort(x, (short) value);
        else if (type == int.class) field.setInt(x, (int) value);
        else if (type == long.class) field.setLong(x, (long) value);
        else if (type == float.class) field.setFloat(x, (float) value);
        else if (type == double.class) field.setDouble(x, (double) value);
        else throw new ClassCastException("Cannot cast " + value + " to a " + type);
    }
}
