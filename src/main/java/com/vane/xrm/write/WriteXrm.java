package com.vane.xrm.write;

import com.vane.xrm.Xrm;
import com.vane.xrm.XrmSheet;
import com.vane.xrm.exception.XrmReadException;
import com.vane.xrm.exception.XrmSheetException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.*;
import java.util.stream.Stream;

public class WriteXrm<X> implements Closeable {
    private final FileOutputStream fileOutputStream;
    private final XSSFWorkbook workbook;
    private final Class<X> klass;
    private final XSSFSheet sheet;

    /**
     * Constructor
     * @param fileName Create File Name or File Path
     * @param klass Xrm Type
     */
    public WriteXrm(@NotNull String fileName, @NotNull Class<X> klass) {
        this(new File(fileName), klass);
    }

    /**
     * Constructor
     * @param file Create File Name or File Path
     * @param klass Xrm Type
     */
    public WriteXrm(@NotNull File file, @NotNull Class<X> klass) {
        this.klass = klass;
        // if file not have: true
        XrmSheet xrmSheet = klass.getAnnotation(XrmSheet.class);
        if (xrmSheet == null)
            throw new XrmSheetException("Not have @WriteXrmSheet annotation");
        if (!file.canWrite())
            throw new XrmReadException("This file cannot be read");

        // set workbook
        if (! file.isFile())
            workbook = new XSSFWorkbook();
        else {
            try {
                workbook = new XSSFWorkbook(file);
            } catch (IOException | InvalidFormatException e) {
                throw new RuntimeException(e);
            }
        }

        // get sheet name
        String sheetName = xrmSheet.value();
        if (sheetName.isEmpty())
            sheetName = klass.getName();
        // create sheet
        XSSFSheet sheet = workbook.getSheet(sheetName);
        if (sheet == null)
            sheet = workbook.createSheet(sheetName);
        this.sheet = sheet;

        // create file stream
        try {
            this.fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private List<WriteUnit> getKlassField() {
        List<WriteUnit> list = new ArrayList<>();
        for (Field field : klass.getDeclaredFields()) {
            Xrm xrm = field.getAnnotation(Xrm.class);
            if (xrm == null)
                continue;
            String name = xrm.value();
            if (name.isEmpty())
                name = field.getName();
//            list.add(new WriteUnit(field, name, xrm.order()));
        }
        list.sort(Comparator.comparingInt(WriteUnit::order));
        return list;
    }

    @Override
    public void close() throws IOException {
        workbook.write(fileOutputStream);
        workbook.close();
    }
}
