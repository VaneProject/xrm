package csv;

import com.vane.xrm.Csv;
import com.vane.xrm.CsvSheet;

@CsvSheet
public class CsvExample1 {
    private int number;
    private String name;
    private String gender;

    @Csv(notUsed = true)
    private long nodata;

    public void print() {
        System.out.printf("number: %d, name: %s, gender: %s%n", number, name, gender);
    }
}
