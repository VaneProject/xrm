package type;

import com.vane.xrm.controller.type.WrapperType;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class PrimitiveNumberTest {
    private final CastingTypeTest<NumberTypeTest> wrapperType = new CastingTypeTest<>() {};

    private Field[] getField() {
        return NumberTypeTest.class.getFields();
    }

    @Test
    void short_test() {
        NumberTypeTest test = new NumberTypeTest();
        short s = 12;
        for (Field field : getField())
            wrapperType.startTest(test, field, s);
        System.out.println(test);
    }

    @Test
    void byte_test() {
        NumberTypeTest test = new NumberTypeTest();
        byte s = 12;
        for (Field field : getField())
            wrapperType.startTest(test, field, s);
        System.out.println(test);
    }

    @Test
    void int_test() {
        NumberTypeTest test = new NumberTypeTest();
        int s = 12;
        for (Field field : getField())
            wrapperType.startTest(test, field, s);
        System.out.println(test);
    }

    @Test
    void long_test() {
        NumberTypeTest test = new NumberTypeTest();
        long s = 12;
        for (Field field : getField())
            wrapperType.startTest(test, field, s);
        System.out.println(test);
    }

    @Test
    void float_test() {
        NumberTypeTest test = new NumberTypeTest();
        float s = 12;
        for (Field field : getField())
            wrapperType.startTest(test, field, s);
        System.out.println(test);
    }

    @Test
    void double_test() {
        NumberTypeTest test = new NumberTypeTest();
        float s = 12;
        for (Field field : getField())
            wrapperType.startTest(test, field, s);
        System.out.println(test);
    }
}
