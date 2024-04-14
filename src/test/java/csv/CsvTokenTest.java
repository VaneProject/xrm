package csv;

import com.vane.xrm.exception.XrmFileException;
import com.vane.xrm.exception.XrmSyntaxException;
import com.vane.xrm.read.CsvRead;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class CsvTokenTest {
    @Test
    void token() throws IOException {
        String filePath = getClass().getClassLoader().getResource("example1.csv").getFile();
        StreamTokenizer tokenizer = new StreamTokenizer(new FileReader(filePath));
        tokenizer.eolIsSignificant(true);
        tokenizer.quoteChar('"');
        tokenizer.commentChar('#');
        tokenizer.ordinaryChars(',', ',');

//        List<String> headers = new ArrayList<>();
//        while (tokenizer.nextToken() != StreamTokenizer.TT_EOL || headers.isEmpty()) {
//            switch (tokenizer.ttype) {
//                case '"':
//                case StreamTokenizer.TT_WORD:
//                    headers.add(tokenizer.sval);
//                    break;
//                case StreamTokenizer.TT_NUMBER:
//                    System.out.println(tokenizer.nval);
//                    break;
//            }
//        }
//
//        System.out.println(headers);
        while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
            switch (tokenizer.ttype) {
                case '"':
                case StreamTokenizer.TT_WORD:
                    System.out.println(tokenizer.sval);
                    break;
                case StreamTokenizer.TT_NUMBER:
                    System.out.println(tokenizer.nval);
                    break;
                case StreamTokenizer.TT_EOL:
                    System.out.println();
                    break;
                default:
                    break;
            }
        }


//        try (FileReader reader = new FileReader(filePath);
//             BufferedReader br = new BufferedReader(reader)) {
//            tokenizer = new StreamTokenizer(reader);
//            tokenizer.eolIsSignificant(true);
//            tokenizer.quoteChar('"');
//            tokenizer.commentChar('#');
//            tokenizer.ordinaryChars(',', ',');
//
//
//            int headerIndex = 0;
//            String line;
//            while ((line = br.readLine()) != null) {
//                headerIndex++;
//                line = line.strip();
//                if (!line.isEmpty() && line.strip().charAt(0) == '#')
//                    continue;
//                break;
//            }
//            if (line == null)
//                throw new XrmSyntaxException("No header found");
//            System.out.println(Arrays.toString(getHeaderToken(line)));
//            System.out.println(headerIndex);
//
//            List<String> headers = new ArrayList<>();
//            while (tokenizer.nextToken() != StreamTokenizer.TT_EOL || headers.isEmpty()) {
//                switch (tokenizer.ttype) {
//                    case '"':
//                    case StreamTokenizer.TT_WORD:
//                        headers.add(tokenizer.sval);
//                        break;
//                    case StreamTokenizer.TT_NUMBER:
//                        System.out.println(tokenizer.nval);
//                        break;
//                }
//            }
//
//        } catch (FileNotFoundException e) {
//            throw new XrmFileException(e.getMessage());
//        } catch (IOException e) {
//            throw new XrmSyntaxException(e.getMessage());
//        }

        CsvRead<CsvExample1> read = new CsvRead<>(CsvExample1.class, filePath);
        read.getAll().forEach(v -> v.print()
        );
    }

    private String[] getHeaderToken(String line) {
        StringBuilder builder = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        List<String> list = new ArrayList<>();
        for (byte b : line.getBytes()) {
            switch (b) {
                case ',' -> {
                    if (stack.isEmpty()) {
                        list.add(builder.toString());
                        builder.setLength(0);
                    } else builder.append(',');
                }
                case '"' -> {
                    if (stack.isEmpty()) stack.add('"');
                    else stack.pop();
                }
                default -> builder.append((char) b);
            }
        }
        list.add(builder.toString());
        return list.toArray(new String[0]);
    }
}
