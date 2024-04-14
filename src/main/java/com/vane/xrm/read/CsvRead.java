package com.vane.xrm.read;

import com.vane.xrm.controller.CsvController;
import com.vane.xrm.exception.XrmFileException;
import com.vane.xrm.exception.XrmSyntaxException;
import com.vane.xrm.items.XrmHeader;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public final class CsvRead<X> extends CsvController<X> implements XrmRead<X> {
    private final XrmHeader[] headers;
    private final List<Object[]> content = new ArrayList<>();

    public CsvRead(Class<X> klass, String fileName) {
        this(klass, new File(fileName));
    }

    public CsvRead(Class<X> klass, File file) {
        super(klass);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StreamTokenizer tokenizer = new StreamTokenizer(new FileReader(file));
            tokenizer.eolIsSignificant(true);
            tokenizer.quoteChar(super.quote);
            tokenizer.commentChar(super.comment);
            tokenizer.ordinaryChars(super.seq, super.seq);

            String line;
            int headerIndex = 0, i = 0;
            while ((line = br.readLine()) != null) {
                headerIndex++;
                line = line.strip();
                if (!line.isEmpty() && line.charAt(0) == super.comment)
                    continue;
                break;
            }
            if (line == null)
                throw new XrmSyntaxException("No header found");
            this.headers = super.getHeader(line);

            Object[] values = new Object[headers.length];
            while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
                if (tokenizer.lineno() <= headerIndex)
                    continue;
                int ttype = tokenizer.ttype;
                if (ttype == super.quote || ttype == StreamTokenizer.TT_WORD)
                    values[i++] = tokenizer.sval;
                else if (ttype == StreamTokenizer.TT_NUMBER)
                    values[i++] = tokenizer.nval;
                else if (ttype == StreamTokenizer.TT_EOL) {
                    i = 0;
                    values = new Object[headers.length];
                    content.add(values);
                }
            }
        } catch (FileNotFoundException e) {
            throw new XrmFileException(e.getMessage());
        } catch (IOException e) {
            throw new XrmSyntaxException(e.getMessage());
        }
    }

    @Override
    protected XrmHeader[] getHeaders() {
        return this.headers;
    }

    @Override
    public X get(int i) {
        Object[] values = content.get(i);
        return createData(values);
    }

    @Override
    public List<X> getAll() {
        return content.stream()
            .map(this::createData)
            .toList();
    }

    private X createData(@NotNull Object[] values) {
        Map<String, Object> map = new HashMap<>(headers.length);
        for (XrmHeader header : headers)
            map.put(header.key(), values[header.index()]);
        return super.createInstance(map);
    }
}
