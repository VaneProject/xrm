import org.apache.poi.xssf.usermodel.XSSFPivotTable;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class TypeTest {
    @Test
    public void test() throws NoSuchFieldException, IllegalAccessException {
//        TestNumber testNumber = new TestNumber();
//        PrimitiveType<TestNumber> type = new PrimitiveType<TestNumber>();
//        Field a = TestNumber.class.getDeclaredField("a");
//        Field b = TestNumber.class.getDeclaredField("b");
//        Field c = TestNumber.class.getDeclaredField("c");
//        Field d = TestNumber.class.getDeclaredField("d");
//        Field a1 = TestNumber.class.getDeclaredField("a1");
//        Field b1 = TestNumber.class.getDeclaredField("b1");
//        Field c1 = TestNumber.class.getDeclaredField("c1");
//
//
//        Double value = 10.0;
//        String character = "1";
//
//        type.setPrimitiveAndWrapperString(testNumber, a, "10");
//        type.setPrimitiveAndWrapperString(testNumber, b, "10");
//        type.setPrimitiveAndWrapperString(testNumber, c, "1");
//        type.setPrimitiveAndWrapperString(testNumber, d, "2");
//        type.setPrimitiveAndWrapperString(testNumber, a1, "true");
//        type.setPrimitiveAndWrapperString(testNumber, b1, "true");
//        type.setPrimitiveAndWrapperString(testNumber, c1, "true1");

//        System.out.println(testNumber);


        long a = 10L;
        Number b = a;

        System.out.println(b.getClass().isPrimitive());
        System.out.println(b.getClass());

    }

    public class TestNumber {
        public int a;
        public Integer b;
        public char c;
        public Character d;
        public boolean a1;
        public Boolean b1;
        public String c1;

        @Override
        public String toString() {
            return "TestNumber{" +
                    "a=" + a +
                    ", b=" + b +
                    ", c=" + c +
                    ", d=" + d +
                    ", a1=" + a1 +
                    ", b1=" + b1 +
                    ", c1=" + c1 +
                    '}';
        }
    }
}
