package com.vane.xrm.controller;

import com.vane.xrm.Csv;
import com.vane.xrm.CsvSheet;
import com.vane.xrm.exception.XrmSheetException;
import com.vane.xrm.format.XrmFormat;
import com.vane.xrm.items.XlsxHeader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.regex.Pattern;

public class CsvController<X> extends AnnotationController<X> {
    protected final Class<X> type;
    protected final String csvSeq;

    protected CsvController(Class<X> type) {
        this.type = type;
        CsvSheet csvSheet = type.getAnnotation(CsvSheet.class);
        if (csvSheet == null)
            throw new XrmSheetException("Do not find @" + CsvSheet.class.getSimpleName());
        this.csvSeq = Pattern.quote(csvSheet.seq());
    }

    protected XlsxHeader[] getHeader(String line) {
        String[] tokens = line.split(csvSeq);
        XlsxHeader[] headers = new XlsxHeader[tokens.length];
        for (int i = 0; i < tokens.length; i++)
            headers[i] = new XlsxHeader(i, tokens[i]);
        return headers;
    }

    /**
     * create instance
     * @param data field name &amp;&amp; value
     * @return instance value
     */
    @Override
    protected X createInstance(Map<String, Object> data) {
        try {
            X x = type.getDeclaredConstructor().newInstance();
            for (Field field : type.getDeclaredFields()) {
                Csv csv = field.getAnnotation(Csv.class);
                if (csv == null)
                    continue;
                String key = getCsvName(csv, field);
                // have key value
                if (data.containsKey(key)) {
                    field.setAccessible(true);
                    Object value;
                    Class<? extends XrmFormat<?>>[] formats = csv.format();
                    if (formats.length > 0) {
                        super.addConverter(formats);
                        value = super.getData(formats, field, (String) data.get(key));
                    } else
                        value = super.getData(field, (String) data.get(key));
                    setData(x, field, value);
                }
            }
            return x;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
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
