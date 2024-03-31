package com.vane.xrm.read;

import com.vane.xrm.controller.XlsxController;
import com.vane.xrm.exception.XrmFileException;
import com.vane.xrm.items.XlsxHeader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class XlsxRead<X> extends XlsxController<X> implements XrmRead<X> {
    private final XSSFSheet sheet;
    private final XlsxHeader[] headers;

    public XlsxRead(Class<X> klass, String fileName) {
        this(klass, new File(fileName));
    }

    public XlsxRead(Class<X> klass, File file) {
        super(klass);
        try (XSSFWorkbook workbook = new XSSFWorkbook(file)) {
            this.sheet = super.getSheet(workbook);
            this.headers = super.getHeader(sheet);
        } catch (IOException | InvalidFormatException e) {
            throw new XrmFileException(e.getMessage());
        }
    }

    @Override
    public X get(int i) {
        Row row = sheet.getRow(super.getDataIndex()+i);
        return createData(row);
    }

    @Override
    public List<X> getAll() {
        List<X> list = new ArrayList<>();
        int first = sheet.getFirstRowNum();
        int last = sheet.getLastRowNum();
        int headerIndex = super.getHeaderIndex();
        for (int i = first; i < last; i++) {
            Row row = sheet.getRow(i);
            if (row != null && haveDataCell(row) && headerIndex != i)
                list.add(createData(row));
        }
        return list;
    }

    private X createData(@NotNull Row row) {
        Map<String, Object> data = new HashMap<>();
        for (XlsxHeader header : headers) {
            Cell cell = row.getCell(header.index());
            Object value = super.getCellValue(cell);
            data.put(header.key(), value);
        }
        return super.createInstance(data);
    }

    private boolean haveDataCell(Row row) {
        for (XlsxHeader header : headers) {
            Cell cell = row.getCell(header.index());
            switch (cell.getCellType()) {
                case BLANK, _NONE: continue;
                default: return true;
            }
        }
        return false;
    }
}
