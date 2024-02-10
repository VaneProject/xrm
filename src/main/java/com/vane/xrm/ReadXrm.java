package com.vane.xrm;

import com.vane.xrm.exception.XrmIsNotStringException;
import com.vane.xrm.exception.XrmSheetException;
import com.vane.xrm.exception.XrmTypeException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadXrm<X> {
    private final Class<X> klass;
    public ReadXrm(Class<X> klass) {
        this.klass = klass;
    }
    // simple read method
    public List<X> readSimpleXlsx(String file, int sheetIndex) {
        return readSimpleXlsx(new File(file), sheetIndex);
    }

    public List<X> readSimpleXlsx(String file, String sheetName) {
        return readSimpleXlsx(new File(file), sheetName);
    }

    public List<X> readSimpleXlsx(File file, int sheetIndex) {
        try (FileInputStream fis = new FileInputStream(file);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
            if (sheet == null)
                throw new XrmSheetException("Do not find sheet index: " + sheetIndex);
            return readSimpleXlsxIntegrated(sheet);
        } catch (IOException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public List<X> readSimpleXlsx(File file, String sheetName) {
        try (FileInputStream fis = new FileInputStream(file);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            XSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null)
                throw new XrmSheetException("Do not find sheet: " + sheetName);
            return readSimpleXlsxIntegrated(sheet);
        } catch (IOException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private List<X> readSimpleXlsxIntegrated(XSSFSheet sheet)
        throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        int start = sheet.getFirstRowNum();
        int end = sheet.getLastRowNum();
        // row data
        Row tempRow = sheet.getRow(start);
        int first = tempRow.getFirstCellNum();
        int last = tempRow.getLastCellNum();
        return createItemList(sheet, start, start+1, end, first, last, true);
    }

    // detail xlsx
    public List<X> readDetailXlsx(String file, int sheetIndex, int column,
                                  int start, int end, int first, int last) {
        return readDetailXlsx(new File(file), sheetIndex, column, start, end, first, last);
    }

    public List<X> readDetailXlsx(String file, String sheetName, int column,
                                  int start, int end, int first, int last) {
        return readDetailXlsx(new File(file), sheetName, column, start, end, first, last);
    }

    public List<X> readDetailXlsx(File file, int sheetIndex, int column,
                                  int start, int end, int first, int last) {
        try (FileInputStream fis = new FileInputStream(file);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
            if (sheet == null)
                throw new XrmSheetException("Do not find sheet index: " + sheetIndex);
            return createItemList(sheet, column, start, end, first, last, false);
        } catch (IOException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public List<X> readDetailXlsx(File file, String sheetName, int column,
                                  int start, int end, int first, int last) {
        try (FileInputStream fis = new FileInputStream(file);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            XSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null)
                throw new XrmSheetException("Do not find sheet: " + sheetName);
            return createItemList(sheet, column, start, end, first, last, false);
        } catch (IOException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    // start xlsx
    public List<X> readStartXlsx(String file, int sheetIndex, int column, int start, int first) {
        return readStartXlsx(new File(file), sheetIndex, column, start, first);
    }

    public List<X> readStartXlsx(String file, String sheetName, int column, int start, int first) {
        return readStartXlsx(new File(file), sheetName, column, start, first);
    }

    // column skip
    public List<X> readStartXlsx(String file, int sheetIndex, int start, int first) {
        return readStartXlsx(new File(file), sheetIndex, start, first);
    }

    public List<X> readStartXlsx(String file, String sheetName, int start, int first) {
        return readStartXlsx(new File(file), sheetName, start, first);
    }

    public List<X> readStartXlsx(File file, int sheetIndex, int start, int first) {
        return readStartXlsx(file, sheetIndex, start, start+1, first);
    }

    public List<X> readStartXlsx(File file, String sheetName, int start, int first) {
        return readStartXlsx(file, sheetName, start, start+1, first);
    }

    public List<X> readStartXlsx(File file, int sheetIndex, int column, int start, int first) {
        try (FileInputStream fis = new FileInputStream(file);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
            if (sheet == null)
                throw new XrmSheetException("Do not find sheet index: " + sheetIndex);
            return readStartXlsxIntegrated(sheet, column, start, first);
        } catch (IOException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public List<X> readStartXlsx(File file, String sheetName, int column, int start, int first) {
        try (FileInputStream fis = new FileInputStream(file);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            XSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null)
                throw new XrmSheetException("Do not find sheet: " + sheetName);
            return readStartXlsxIntegrated(sheet, column, start, first);
        } catch (IOException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private List<X> readStartXlsxIntegrated(XSSFSheet sheet, int column, int start, int first)
        throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        int end = sheet.getLastRowNum();
        int last = sheet.getRow(start).getLastCellNum();
        return createItemList(sheet, column, start, end, first, last, true);
    }

    /**
     * create sheet to List Object
     * @param sheet xlsx sheet
     * @param column column index
     * @param start column start index
     * @param end column end index
     * @param first cell start index
     * @param last cell finish index
     * @param finishBlank if black meet make to finish
     * @return read all data
     * @throws InvocationTargetException if the underlying constructor throws an exception.
     * @throws InstantiationException if the class that declares the
     * underlying constructor represents an abstract class.
     * @throws IllegalAccessException if this {@code Constructor} object
     * is enforcing Java language access control and the underlying
     * @throws NoSuchMethodException if a matching method is not found.
     */
    private List<X> createItemList(XSSFSheet sheet, int column,
                                   int start, int end,
                                   int first, int last, boolean finishBlank
    ) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        List<X> list = new ArrayList<>();
        // create
        Row row = sheet.getRow(column);
        List<String> columnNameList = new ArrayList<>();
        for (int i = first; i < last; i++) {
            Cell cell = row.getCell(i);
            CellType type = cell.getCellType();
            if (type == CellType._NONE || (finishBlank && type == CellType.BLANK))
                break;
            if (type != CellType.STRING)
                throw new XrmIsNotStringException(cell, "Type: " + type.name());
            columnNameList.add(cell.getStringCellValue());
        }
        String[] columnName = columnNameList.toArray(String[]::new);
        Map<String, Object> columnValue = new HashMap<>(columnName.length);
        // read data
        for (int i = start; i < end; i++) {
            row = sheet.getRow(i);
            for (int j = first; j < columnName.length; j++) {
                Object cellValue = getCellValue(row.getCell(j));
                if (cellValue == null || cellValue instanceof String str && str.isEmpty())
                    columnValue.remove(columnName[j-first]);
                else
                    columnValue.put(columnName[j-first], cellValue);
            }
            if (columnValue.isEmpty())
                break;
            list.add(createInstance(columnValue));
        }
        return list;
    }

    private X createInstance(Map<String, Object> data)
        throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        X x = klass.getDeclaredConstructor().newInstance();
        for (Field field : klass.getDeclaredFields()) {
            Xrm xrm = field.getAnnotation(Xrm.class);
            if (xrm != null) {
                String value = xrm.value();
                // set default name
                if (value.isEmpty())
                    value = field.getName();
                // set data
                if (data.containsKey(value)) {
                    field.setAccessible(true);
                    setData(x, field, data.get(value));
                }
            }
        }
        return x;
    }

    private Object getCellValue(Cell cell) {
        return switch (cell.getCellType()) {
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell))
                    yield cell.getDateCellValue();
                else
                    yield cell.getNumericCellValue();
            }
            case STRING -> cell.getStringCellValue();
            case BOOLEAN -> cell.getBooleanCellValue();
            case BLANK -> "";
            case _NONE -> null;
            case FORMULA -> throw new XrmTypeException(cell, "Formula Type Error: " + cell.getCellFormula());
            case ERROR -> throw new XrmTypeException(cell, "Type Error: " + cell.getErrorCellValue());
        };
    }

    private void setData(X x, Field field, Object value) throws IllegalAccessException {
        Class<?> type = field.getType();
        if (value instanceof Double number) {
            if (type == byte.class)
                field.setByte(x, number.byteValue());
            else if (type == short.class)
                field.setShort(x, number.shortValue());
            else if (type == int.class)
                field.setInt(x, number.intValue());
            else if (type == long.class)
                field.setLong(x, number.longValue());
            else if (type == float.class)
                field.setFloat(x, number.floatValue());
            else if (type == double.class)
                field.setDouble(x, number);
            // wrapper type
            else if (type == Byte.class)
                field.set(x, number.byteValue());
            else if (type == Short.class)
                field.set(x, number.shortValue());
            else if (type == Integer.class)
                field.set(x, number.intValue());
            else if (type == Long.class)
                field.set(x, number.longValue());
            else if (type == Float.class)
                field.set(x, number.floatValue());
            else if (type == Double.class)
                field.set(x, number);
            else {
                String fieldTypeName = type.getTypeName();
                String valueTypeName = value.getClass().getTypeName();
                String message = String.format("(Field: %s) != (Value: %s)", fieldTypeName, valueTypeName);
                throw new XrmTypeException(message);
            }
        } else
            field.set(x, value);
    }
}
