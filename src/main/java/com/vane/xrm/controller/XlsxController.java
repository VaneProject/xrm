package com.vane.xrm.controller;

import com.vane.xrm.Xrm;
import com.vane.xrm.XrmSheet;
import com.vane.xrm.exception.XrmSheetException;
import com.vane.xrm.exception.XrmTypeException;
import com.vane.xrm.items.XlsxHeader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XlsxController<X> extends AnnotationController<X> {
    protected final Class<X> type;
    private final XrmSheet xrmSheet;

    protected XlsxController(Class<X> type) {
        this.type = type;
        this.xrmSheet = type.getAnnotation(XrmSheet.class);
        if (xrmSheet == null)
            throw new XrmSheetException("Do not find @" + XrmSheet.class.getSimpleName());
    }

    protected XlsxHeader[] getHeader(XSSFSheet sheet) {
        int headerIndex = this.getHeaderIndex();
        List<XlsxHeader> list = new ArrayList<>();
        XSSFRow row = sheet.getRow(headerIndex);
        int start = row.getFirstCellNum();
        int end = row.getLastCellNum();

        for (int i = start; i < end; i++) {
            final Cell cell = row.getCell(i);
            if (isHeaderType(cell))
                list.add(new XlsxHeader(i, getHeaderKey(cell)));
        }
        return list.toArray(XlsxHeader[]::new);
    }

    private boolean isHeaderType(Cell cell) {
        return switch (cell.getCellType()) {
            case BLANK, _NONE -> false;
            default -> true;
        };
    }

    private String getHeaderKey(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case FORMULA -> cell.getCellFormula();
            case BOOLEAN -> Boolean.toString(cell.getBooleanCellValue());
            case NUMERIC -> Double.toString(cell.getNumericCellValue());
            case BLANK, _NONE -> throw new XrmTypeException(cell, "Header type error " + cell.getCellType());
            case ERROR -> throw new XrmTypeException(cell, "Type Error: " + cell.getErrorCellValue());
        };
    }

    protected XSSFSheet getSheet(XSSFWorkbook workbook) {
        String sheetName = getXrmSheetName(this.xrmSheet);
        XSSFSheet sheet = workbook.getSheet(sheetName);
        if (sheet == null)
            throw new XrmSheetException("Do not find " + sheetName + " sheet");
        return sheet;
    }

    /**
     * header index
     * @return default = 1
     */
    protected int getHeaderIndex() {
        return this.xrmSheet.header() - 1;
    }

    /**
     * data index
     * @return default = 0
     */
    protected int getDataIndex() {
        return this.xrmSheet.data() - 1;
    }

    /**
     * get cell value
     * @param cell get value
     * @return values
     */
    protected final Object getCellValue(@NotNull Cell cell) {
        return switch (cell.getCellType()) {
            case NUMERIC -> DateUtil.isCellDateFormatted(cell)
                ? cell.getDateCellValue()
                : cell.getNumericCellValue();
            case STRING -> cell.getStringCellValue();
            case BOOLEAN -> cell.getBooleanCellValue();
            case FORMULA -> cell.getCellFormula();
            case BLANK -> "";
            case _NONE -> null;
            case ERROR -> throw new XrmTypeException(cell, "Type Error: " + cell.getErrorCellValue());
        };
    }

    /**
     * create instance
     * @param data field name && value
     * @return instance value
     */
    @Override
    protected X createInstance(Map<String, Object> data) {
        try {
            X x = type.getDeclaredConstructor().newInstance();
            for (Field field : type.getDeclaredFields()) {
                Xrm xrm = field.getAnnotation(Xrm.class);
                if (xrm == null)
                    continue;
                String key = getXrmName(xrm, field);
                // have key value
                if (data.containsKey(key)) {
                    field.setAccessible(true);
                    setData(x, field, data.get(key));
                }
            }
            return x;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * get xrm field name
     * @param xrm xrm annotation
     * @param field class field status
     * @return header key name
     */
    private String getXrmName(@NotNull Xrm xrm, Field field) {
        String value = xrm.value();
        // set default name
        if (value.isEmpty())
            return field.getName();
        return value;
    }

    /**
     * get xrm sheet name
     * @param xrmSheet sheet annotation
     * @return xrmSheet name
     */
    private String getXrmSheetName(@NotNull XrmSheet xrmSheet) {
        String sheetName = xrmSheet.value();
        if (sheetName.isEmpty())
            return type.getSimpleName();
        return sheetName;
    }
}
