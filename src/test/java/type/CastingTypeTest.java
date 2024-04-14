package type;

import com.vane.xrm.controller.type.WrapperType;

import java.lang.reflect.Field;

class CastingTypeTest<X> {
    private final WrapperType<X> type = new WrapperType<>();

    public void startTest(X x, Field field, Object value) {
        type.setTypes(x, field, value);
    }
}
