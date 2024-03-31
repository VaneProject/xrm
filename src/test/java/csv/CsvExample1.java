package csv;

import com.vane.xrm.Csv;
import com.vane.xrm.CsvSheet;

@CsvSheet
public class CsvExample1 {
    @Csv
    private int number;
    @Csv
    private String name;
    @Csv
    private String gender;

    public void print() {
        System.out.printf("number: %d, name: %s, gender: %s%n", number, name, gender);
    }
}
