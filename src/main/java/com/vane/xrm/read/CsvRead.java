package com.vane.xrm.read;

import com.vane.xrm.controller.CsvController;
import com.vane.xrm.controller.XlsxController;
import com.vane.xrm.exception.XrmFileException;
import com.vane.xrm.items.XlsxHeader;
import org.apache.commons.io.input.ReaderInputStream;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Pattern;

public class CsvRead<X> extends CsvController<X> implements XrmRead<X> {
    private final XlsxHeader[] headers;
    private final List<String> content = new ArrayList<>();

    public CsvRead(Class<X> klass, String fileName) {
        this(klass, new File(fileName));
    }

    public CsvRead(Class<X> klass, File file) {
        super(klass);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            this.headers = super.getHeader(reader.readLine());
            String line;
            while ((line = reader.readLine()) != null)
                content.add(line);
        } catch (IOException e) {
            throw new XrmFileException(e.getMessage());
        }
    }

    @Override
    public X get(int i) {
        String line = content.get(i);
        return createData(line);
    }

    public X get(int i, String seq) {
        String line = content.get(i);
        return createData(line, Pattern.quote(seq));
    }

    @Override
    public List<X> getAll() {
        return content.stream()
            .map(this::createData)
            .toList();
    }

    private X createData(@NotNull String line) {
        return createData(line, super.csvSeq);
    }

    private X createData(@NotNull String line, String seq) {
        String[] tokens = line.split(seq);
        Map<String, Object> data = new HashMap<>();
        for (XlsxHeader header : headers)
            data.put(header.key(), tokens[header.index()]);
        return super.createInstance(data);
    }
}
