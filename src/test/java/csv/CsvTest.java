package csv;

import com.vane.xrm.read.CsvRead;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CsvTest {
    @Test
    public void csv_print_test1() {
        String path = this.getClass().getClassLoader().getResource("example1.csv").getFile();
        CsvRead<CsvExample1> read = new CsvRead<>(CsvExample1.class, path);

        System.out.println("Print all");
        List<CsvExample1> all = read.getAll();
        for (CsvExample1 example : all)
            example.print();

        System.out.println("Print one");
        CsvExample1 example1 = read.get(0);
        example1.print();
    }

    @Test
    public void csv_print_test2() {
        String path = this.getClass().getClassLoader().getResource("example2.csv").getFile();
        CsvRead<CsvExample2> read = new CsvRead<>(CsvExample2.class, path);
        System.out.println("Print all");
        List<CsvExample2> all = read.getAll();
        for (CsvExample2 example : all)
            example.print();

        System.out.println("Print one");
        CsvExample2 example1 = read.get(0);
        example1.print();
    }

    @Test
    public void csv_print_test3() {
        String path = this.getClass().getClassLoader().getResource("example3.csv").getFile();
        CsvRead<CsvExample3> read = new CsvRead<>(CsvExample3.class, path);
        System.out.println("Print all");
        List<CsvExample3> all = read.getAll();
        for (CsvExample3 example : all)
            example.print();

        System.out.println("Print one");
        CsvExample3 example1 = read.get(0);
        example1.print();
    }
}
