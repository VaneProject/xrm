package xlsx;

import com.vane.xrm.read.CsvRead;
import com.vane.xrm.read.XlsxRead;
import csv.CsvExample1;
import org.junit.jupiter.api.Test;

import java.util.List;

public class XlsxTest {
    @Test
    void xlsx_print_test1() {
        String path = this.getClass().getClassLoader().getResource("example1.xlsx").getFile();
        XlsxRead<XlsxExample1> read = new XlsxRead<>(XlsxExample1.class, path);

        System.out.println("Print all");
        List<XlsxExample1> all = read.getAll();
        for (XlsxExample1 example : all)
            example.print();

        System.out.println("Print one");
        XlsxExample1 example1 = read.get(0);
        example1.print();
    }

    @Test
    void xlsx_print_test2() {
        String path = this.getClass().getClassLoader().getResource("example2.xlsx").getFile();
        XlsxRead<XlsxExample2> read = new XlsxRead<>(XlsxExample2.class, path);

        System.out.println("Print all");
        List<XlsxExample2> all = read.getAll();
        for (XlsxExample2 example : all)
            example.print();

        System.out.println("Print one");
        XlsxExample2 example1 = read.get(0);
        example1.print();
    }
}
