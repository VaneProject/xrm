package com.vane.xrm.controller;

import com.vane.xrm.Csv;
import com.vane.xrm.CsvSheet;
import com.vane.xrm.controller.type.WordType;
import com.vane.xrm.exception.XrmSheetException;
import com.vane.xrm.items.XrmHeader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public abstract class CsvController<X> extends AnnotationController<X, Csv> {
    protected final char comment, seq, quote;

    protected CsvController(Class<X> type) {
        super(type, new WordType<>());
        CsvSheet csvSheet = type.getAnnotation(CsvSheet.class);
        if (csvSheet == null)
            throw new XrmSheetException("Do not find @" + CsvSheet.class.getSimpleName());
        this.comment = csvSheet.comment();
        this.seq = csvSheet.seq();
        this.quote = csvSheet.quote();
    }

    protected XrmHeader[] getHeader(String line) {
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
        XrmHeader[] header = new XrmHeader[list.size()];
        for (int i = 0; i < header.length; i++)
            header[i] = new XrmHeader(i, list.get(i));
        return header;
    }

    /**
     * get csv field name
     * @param csv csv annotation
     * @param field class field status
     * @return header key name
     */
    private String getCsvName(@NotNull Csv csv, Field field) {
        String value = csv.value();
        // set default name
        if (value.isEmpty())
            return field.getName();
        return value;
    }
}
