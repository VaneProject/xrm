import com.vane.xrm.ReadXrm;

import java.net.URL;
import java.util.List;

public class ExampleReadTest {
    public static void main(String[] args) {
        ReadXrm<Example> readXrm = new ReadXrm<>(Example.class);
        URL url = ExampleReadTest.class.getClassLoader().getResource("example.xlsx");
        if (url != null) {
            List<Example> list = readXrm.readSimpleXlsx(url.getFile(), 0);
            for (Example example : list)
                example.printExample();
        }
    }
}
