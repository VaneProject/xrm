package csv;

import com.vane.xrm.Csv;
import com.vane.xrm.CsvSheet;

@CsvSheet
class CsvExample2 {
    @Csv("count")
    private int number;
    private String name;
    private String gender;

    public void print() {
        System.out.printf("count: %d, name: %s, gender: %s%n", number, name, gender);
    }
}
