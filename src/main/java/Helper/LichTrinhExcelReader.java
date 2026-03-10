package Helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class LichTrinhExcelReader {

    public static class ExcelLichTrinhRow {
        private final String tenDiaDiem;
        private final String tenPhuongTien;
        private final String noiDung;

        public ExcelLichTrinhRow(String tenDiaDiem, String tenPhuongTien, String noiDung) {
            this.tenDiaDiem = tenDiaDiem;
            this.tenPhuongTien = tenPhuongTien;
            this.noiDung = noiDung;
        }

        public String getTenDiaDiem() {
            return tenDiaDiem;
        }

        public String getTenPhuongTien() {
            return tenPhuongTien;
        }

        public String getNoiDung() {
            return noiDung;
        }
    }

    public static List<ExcelLichTrinhRow> read(File file) throws IOException {
        List<ExcelLichTrinhRow> rows = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        try (FileInputStream fis = new FileInputStream(file);
                Workbook workbook = WorkbookFactory.create(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            int first = sheet.getFirstRowNum();
            int last = sheet.getLastRowNum();

            // Mặc định bỏ qua dòng đầu tiên (header)
            for (int i = first + 1; i <= last; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                String tenDiaDiem = getCellString(row.getCell(0), formatter);
                String tenPhuongTien = getCellString(row.getCell(1), formatter);
                String noiDung = getCellString(row.getCell(2), formatter);

                if (isBlank(tenDiaDiem) && isBlank(tenPhuongTien) && isBlank(noiDung)) {
                    continue;
                }

                rows.add(new ExcelLichTrinhRow(tenDiaDiem, tenPhuongTien, noiDung));
            }
        }

        return rows;
    }

    private static String getCellString(Cell cell, DataFormatter formatter) {
        if (cell == null) {
            return null;
        }
        String value = formatter.formatCellValue(cell);
        return value != null ? value.trim() : null;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
