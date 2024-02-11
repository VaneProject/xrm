import com.vane.xrm.ReadXrm;

import java.net.URL;
import java.util.List;

public class ExampleReadTest {
    public static void main(String[] args) {
        ReadXrm<Example> readXrm = new ReadXrm<>(Example.class);
        URL url = ExampleReadTest.class.getClassLoader().getResource("example.xlsx");
        URL url3 = ExampleReadTest.class.getClassLoader().getResource("example3.xlsx");
        if (url != null && url3 != null) {
//            List<Example> list = readXrm.readSimpleXlsx(url.getFile(), 0);
            List<Example> list = readXrm.readDetailXlsx(url3.getFile(), 0, 1,
                3, 5,
                2, 3);
            for (Example example : list)
                example.printExample();
        }
    }
}
