package com.APIFramework.utilExcel;

import org.apache.poi.ss.usermodel.*;
import java.io.FileInputStream;
import java.io.IOException;

public class UtilExcel {

    public static String SHEET_PATH = System.getProperty("user.dir") + "/src/test/resources/TestData.xlsx";
    static Workbook book;
    public static Sheet sheet;

    public static Object[][] getTestDataFromExcel(String sheetName) {
        Object[][] data = null;
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(SHEET_PATH);
            book = WorkbookFactory.create(fileInputStream);
            sheet = book.getSheet(sheetName);

            if (sheet == null) {
                return new Object[0][0];
            }

            int headerRowIndex = 1; // Row 2 (Index 1) contains the actual headers
            Row headerRow = sheet.getRow(headerRowIndex);
            if (headerRow == null) {
                return new Object[0][0];
            }

            int totalCols = headerRow.getLastCellNum();
            int lastRow = sheet.getLastRowNum();
            int totalRows = lastRow - headerRowIndex;

            if (totalRows <= 0 || totalCols <= 0) {
                return new Object[0][0];
            }

            data = new Object[totalRows][totalCols];
            DataFormatter formatter = new DataFormatter();

            for (int i = 0; i < totalRows; i++) {
                Row row = sheet.getRow(i + headerRowIndex + 1); // Row 3 (Index 2) is the start of test data
                for (int j = 0; j < totalCols; j++) {
                    if (row == null) {
                        data[i][j] = "";
                    } else {
                        Cell cell = row.getCell(j);
                        String cellValue = formatter.formatCellValue(cell);
                        data[i][j] = (cellValue == null) ? "" : cellValue.trim();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Excel file read error: " + e.getMessage());
            e.printStackTrace();
            return new Object[0][0];
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (book != null) {
                    book.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
}