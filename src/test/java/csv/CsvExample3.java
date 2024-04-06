package csv;

import com.vane.xrm.Csv;
import com.vane.xrm.CsvSheet;
import com.vane.xrm.format.LocalDateFormat;
import com.vane.xrm.format.LocalDateTimeFormat;

import java.time.LocalDateTime;

@CsvSheet
public class CsvExample3 {
    @Csv("count")
    private int number;
    @Csv
    private String name;
    @Csv
    private String gender;
    @Csv(format = LocalDateTimeFormat.class)
    private LocalDateTime date;

    public void print() {
        System.out.printf("count: %d, name: %s, gender: %s, date: %s%n", number, name, gender, date);
    }
}
