package csv;

import com.vane.xrm.Csv;
import com.vane.xrm.CsvSheet;

@CsvSheet
public class CsvExample2 {
    @Csv("count")
    private int number;
    @Csv
    private String name;
    @Csv
    private String gender;

    public void print() {
        System.out.printf("count: %d, name: %s, gender: %s%n", number, name, gender);
    }
}
