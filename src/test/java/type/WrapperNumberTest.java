package type;

import com.vane.xrm.controller.type.WrapperType;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class WrapperNumberTest {
    private final CastingTypeTest<NumberTypeTest> wrapperType = new CastingTypeTest<>() {};

    private Field[] getField() {
        return NumberTypeTest.class.getFields();
    }

    @Test
    void short_test() {
        NumberTypeTest test = new NumberTypeTest();
        Short s = 12;
        for (Field field : getField())
            wrapperType.startTest(test, field, s);
        System.out.println(test);
    }

    @Test
    void byte_test() {
        NumberTypeTest test = new NumberTypeTest();
        Byte s = 12;
        for (Field field : getField())
            wrapperType.startTest(test, field, s);
        System.out.println(test);
    }

    @Test
    void int_test() {
        NumberTypeTest test = new NumberTypeTest();
        Integer s = 12;
        for (Field field : getField())
            wrapperType.startTest(test, field, s);
        System.out.println(test);
    }

    @Test
    void long_test() {
        NumberTypeTest test = new NumberTypeTest();
        Long s = 12L;
        for (Field field : getField())
            wrapperType.startTest(test, field, s);
        System.out.println(test);
    }

    @Test
    void float_test() {
        NumberTypeTest test = new NumberTypeTest();
        Float s = 12f;
        for (Field field : getField())
            wrapperType.startTest(test, field, s);
        System.out.println(test);
    }

    @Test
    void double_test() {
        NumberTypeTest test = new NumberTypeTest();
        Double s = 12d;
        for (Field field : getField())
            wrapperType.startTest(test, field, s);
        System.out.println(test);
    }
}
