package xlsx;

import com.vane.xrm.Xrm;
import com.vane.xrm.XrmSheet;

@XrmSheet(value = "sheet1", header = 2, data = 3)
public class XlsxExample1 {
    @Xrm
    private int number;
    @Xrm
    private String name;
    @Xrm
    private String gender;

    public void print() {
        System.out.printf("number: %d, name: %s, gender: %s%n", number, name, gender);
    }
}
