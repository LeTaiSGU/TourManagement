package GUI;

import DTO.BaoCaoDoanhThuRowDTO;
import DTO.BaoCaoFilterDTO;
import DTO.BaoCaoKhachHangRowDTO;
import DTO.BaoCaoNhanVienRowDTO;
import DTO.BaoCaoTourRowDTO;
import DTO.BaoCaoVanHanhRowDTO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.awt.Color;
import java.io.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Tiện ích xuất báo cáo Excel (.xlsx) và PDF (.pdf).
 *
 * Sử dụng:
 * - Apache POI (poi-ooxml) cho Excel
 * - Apache PDFBox (pdfbox) cho PDF
 *
 * Tất cả phương thức là static — không cần tạo instance.
 * Tên file tự sinh theo quy tắc: TenBaoCao_NamThang.xlsx / .pdf
 */
public class BaoCaoExportUtil {

    /** Định dạng tiền tệ VND */
    private static final NumberFormat FMT_TIEN = NumberFormat.getInstance(new Locale("vi", "VN"));

    static {
        FMT_TIEN.setMaximumFractionDigits(0);
        FMT_TIEN.setMinimumFractionDigits(0);
    }

    /** Định dạng tên file: không dấu, không khoảng trắng */
    private static String tenFile(String loaiBaoCao) {
        String ngay = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return loaiBaoCao + "_" + ngay;
    }

    // =========================================================
    // XUẤT EXCEL — Doanh thu theo tháng
    // =========================================================

    public static File xuatExcelDoanhThuThang(List<BaoCaoDoanhThuRowDTO> data,
            BaoCaoFilterDTO filter) throws Exception {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            XSSFSheet sheet = wb.createSheet("Doanh thu theo tháng");

            // Styles
            CellStyle styleHeader = taoStyleHeader(wb);
            CellStyle styleTitle = taoStyleTitle(wb);
            CellStyle styleTien = taoStyleTien(wb);
            CellStyle styleNormal = taoStyleNormal(wb);

            int r = 0;
            // Tiêu đề
            Row rowTitle = sheet.createRow(r++);
            rowTitle.createCell(0).setCellValue("BÁO CÁO DOANH THU THEO THÁNG");
            rowTitle.getCell(0).setCellStyle(styleTitle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

            // Thông tin lọc
            Row rowFilter = sheet.createRow(r++);
            String namStr = filter.getNam() != null ? "Năm: " + filter.getNam()
                    : buildKhoangThoiGian(filter);
            rowFilter.createCell(0).setCellValue(namStr);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));
            r++; // Dòng trống

            // Header bảng
            String[] headers = { "Tháng", "Số hóa đơn", "Số vé bán", "Tổng doanh thu (VND)", "Doanh thu TB/HĐ" };
            Row rowHead = sheet.createRow(r++);
            for (int i = 0; i < headers.length; i++) {
                Cell c = rowHead.createCell(i);
                c.setCellValue(headers[i]);
                c.setCellStyle(styleHeader);
            }

            // Dữ liệu
            double tongCong = 0;
            for (BaoCaoDoanhThuRowDTO row : data) {
                Row dr = sheet.createRow(r++);
                dr.createCell(0).setCellValue(row.getNhan());
                dr.createCell(1).setCellValue(row.getSoHoaDon());
                dr.createCell(2).setCellValue(row.getSoVe());
                Cell cTien = dr.createCell(3);
                cTien.setCellValue(row.getTongDoanhThu());
                cTien.setCellStyle(styleTien);
                Cell cTb = dr.createCell(4);
                cTb.setCellValue(row.getGiaTrungBinh());
                cTb.setCellStyle(styleTien);
                tongCong += row.getTongDoanhThu();
                dr.getCell(0).setCellStyle(styleNormal);
                dr.getCell(1).setCellStyle(styleNormal);
                dr.getCell(2).setCellStyle(styleNormal);
            }

            // Dòng tổng
            Row rowTong = sheet.createRow(r++);
            Cell cTongNhan = rowTong.createCell(0);
            cTongNhan.setCellValue("TỔNG CỘNG");
            cTongNhan.setCellStyle(styleHeader);
            sheet.addMergedRegion(new CellRangeAddress(r - 1, r - 1, 0, 2));
            Cell cTongTien = rowTong.createCell(3);
            cTongTien.setCellValue(tongCong);
            cTongTien.setCellStyle(styleHeader);

            // Auto width
            for (int i = 0; i < 5; i++)
                sheet.autoSizeColumn(i);

            File f = File.createTempFile(tenFile("BaoCaoDoanhThuThang"), ".xlsx");
            try (FileOutputStream fos = new FileOutputStream(f)) {
                wb.write(fos);
            }
            return f;
        }
    }

    // =========================================================
    // XUẤT EXCEL — Tour
    // =========================================================

    public static File xuatExcelTour(List<BaoCaoTourRowDTO> data) throws Exception {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            XSSFSheet sheet = wb.createSheet("Báo cáo Tour");
            CellStyle styleHeader = taoStyleHeader(wb);
            CellStyle styleTitle = taoStyleTitle(wb);
            CellStyle styleTien = taoStyleTien(wb);
            CellStyle styleNormal = taoStyleNormal(wb);

            int r = 0;
            Row rowTitle = sheet.createRow(r++);
            rowTitle.createCell(0).setCellValue("BÁO CÁO HIỆU QUẢ HOẠT ĐỘNG TOUR");
            rowTitle.getCell(0).setCellStyle(styleTitle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
            r++;

            String[] headers = { "Tên tour", "Loại tour", "Điểm KH",
                    "Sức chứa", "Đã đăng ký", "Lấp đầy (%)", "Doanh thu (VND)", "Trạng thái" };
            Row rowHead = sheet.createRow(r++);
            for (int i = 0; i < headers.length; i++) {
                Cell c = rowHead.createCell(i);
                c.setCellValue(headers[i]);
                c.setCellStyle(styleHeader);
            }

            for (BaoCaoTourRowDTO row : data) {
                Row dr = sheet.createRow(r++);
                dr.createCell(0).setCellValue(row.getTenTour());
                dr.createCell(1).setCellValue(row.getTenLoaiTour());
                dr.createCell(2).setCellValue(row.getNoiKhoiHanh());
                dr.createCell(3).setCellValue(row.getSoLuongToiDa());
                dr.createCell(4).setCellValue(row.getSoVeDaBan());
                dr.createCell(5).setCellValue(String.format("%.1f%%", row.getTyLeLapDay()));
                Cell cTien = dr.createCell(6);
                cTien.setCellValue(row.getDoanhThu());
                cTien.setCellStyle(styleTien);
                dr.createCell(7).setCellValue(row.isTrangThai() ? "Hoạt động" : "Ngừng");
                for (int ci : new int[] { 0, 1, 2, 3, 4, 5, 7 })
                    dr.getCell(ci).setCellStyle(styleNormal);
            }

            for (int i = 0; i < 8; i++)
                sheet.autoSizeColumn(i);
            File f = File.createTempFile(tenFile("BaoCaoTour"), ".xlsx");
            try (FileOutputStream fos = new FileOutputStream(f)) {
                wb.write(fos);
            }
            return f;
        }
    }

    // =========================================================
    // XUẤT EXCEL — Nhân viên
    // =========================================================

    public static File xuatExcelNhanVien(List<BaoCaoNhanVienRowDTO> data) throws Exception {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            XSSFSheet sheet = wb.createSheet("Báo cáo Nhân viên");
            CellStyle styleHeader = taoStyleHeader(wb);
            CellStyle styleTitle = taoStyleTitle(wb);
            CellStyle styleTien = taoStyleTien(wb);
            CellStyle styleNormal = taoStyleNormal(wb);

            int r = 0;
            Row rowTitle = sheet.createRow(r++);
            rowTitle.createCell(0).setCellValue("BÁO CÁO BÁN HÀNG THEO NHÂN VIÊN");
            rowTitle.getCell(0).setCellStyle(styleTitle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
            r++;

            String[] headers = { "Tên nhân viên", "Chức vụ", "Số hóa đơn", "Số tour bán", "Số vé", "Doanh thu (VND)" };
            Row rowHead = sheet.createRow(r++);
            for (int i = 0; i < headers.length; i++) {
                Cell c = rowHead.createCell(i);
                c.setCellValue(headers[i]);
                c.setCellStyle(styleHeader);
            }

            for (BaoCaoNhanVienRowDTO row : data) {
                Row dr = sheet.createRow(r++);
                dr.createCell(0).setCellValue(row.getTenNhanVien());
                dr.createCell(1).setCellValue(row.getTenChucVu());
                dr.createCell(2).setCellValue(row.getSoHoaDon());
                dr.createCell(3).setCellValue(row.getSoTourBan());
                dr.createCell(4).setCellValue(row.getSoVe());
                Cell cTien = dr.createCell(5);
                cTien.setCellValue(row.getTongDoanhThu());
                cTien.setCellStyle(styleTien);
                for (int ci : new int[] { 0, 1, 2, 3, 4 })
                    dr.getCell(ci).setCellStyle(styleNormal);
            }

            for (int i = 0; i < 6; i++)
                sheet.autoSizeColumn(i);
            File f = File.createTempFile(tenFile("BaoCaoNhanVien"), ".xlsx");
            try (FileOutputStream fos = new FileOutputStream(f)) {
                wb.write(fos);
            }
            return f;
        }
    }

    // =========================================================
    // XUẤT EXCEL — Khách hàng
    // =========================================================

    public static File xuatExcelKhachHang(List<BaoCaoKhachHangRowDTO> data) throws Exception {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            XSSFSheet sheet = wb.createSheet("Báo cáo Khách hàng");
            CellStyle styleHeader = taoStyleHeader(wb);
            CellStyle styleTitle = taoStyleTitle(wb);
            CellStyle styleTien = taoStyleTien(wb);
            CellStyle styleNormal = taoStyleNormal(wb);

            int r = 0;
            Row rowTitle = sheet.createRow(r++);
            rowTitle.createCell(0).setCellValue("BÁO CÁO TOP KHÁCH HÀNG CHI TIÊU");
            rowTitle.getCell(0).setCellStyle(styleTitle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
            r++;

            String[] headers = { "Tên khách hàng", "Số hóa đơn", "Số vé", "Tổng chi tiêu (VND)" };
            Row rowHead = sheet.createRow(r++);
            for (int i = 0; i < headers.length; i++) {
                Cell c = rowHead.createCell(i);
                c.setCellValue(headers[i]);
                c.setCellStyle(styleHeader);
            }

            for (BaoCaoKhachHangRowDTO row : data) {
                Row dr = sheet.createRow(r++);
                dr.createCell(0).setCellValue(row.getNhan());
                dr.createCell(1).setCellValue(row.getSoHoaDon());
                dr.createCell(2).setCellValue(row.getSoLuong());
                Cell cTien = dr.createCell(3);
                cTien.setCellValue(row.getTongChiTieu());
                cTien.setCellStyle(styleTien);
                for (int ci : new int[] { 0, 1, 2 })
                    dr.getCell(ci).setCellStyle(styleNormal);
            }

            for (int i = 0; i < 4; i++)
                sheet.autoSizeColumn(i);
            File f = File.createTempFile(tenFile("BaoCaoKhachHang"), ".xlsx");
            try (FileOutputStream fos = new FileOutputStream(f)) {
                wb.write(fos);
            }
            return f;
        }
    }

    // =========================================================
    // XUẤT PDF — Doanh thu theo tháng (đơn giản dùng PDFBox)
    // =========================================================

    public static File xuatPdfDoanhThuThang(List<BaoCaoDoanhThuRowDTO> data,
            BaoCaoFilterDTO filter) throws Exception {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                float y = page.getMediaBox().getHeight() - 50;
                float marginLeft = 40;
                float colW = (page.getMediaBox().getWidth() - 80) / 4f;

                // Tiêu đề
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 14);
                cs.newLineAtOffset(marginLeft, y);
                cs.showText("BAO CAO DOANH THU THEO THANG");
                cs.endText();
                y -= 20;

                String namStr = filter.getNam() != null
                        ? "Nam: " + filter.getNam()
                        : buildKhoangThoiGian(filter);
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 10);
                cs.newLineAtOffset(marginLeft, y);
                cs.showText(namStr);
                cs.endText();
                y -= 30;

                // Header bảng
                String[] headers = { "Thang", "So HD", "So Ve", "Doanh thu (VND)" };
                cs.setNonStrokingColor(new java.awt.Color(41, 128, 185));
                cs.addRect(marginLeft, y - 4, page.getMediaBox().getWidth() - 80, 18);
                cs.fill();
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 10);
                cs.setNonStrokingColor(java.awt.Color.WHITE);
                cs.newLineAtOffset(marginLeft + 4, y);
                for (int i = 0; i < headers.length; i++) {
                    cs.showText(headers[i]);
                    cs.newLineAtOffset(colW, 0);
                }
                cs.endText();
                y -= 20;

                // Dữ liệu
                boolean alt = false;
                for (BaoCaoDoanhThuRowDTO row : data) {
                    if (y < 60) {
                        cs.close();
                        PDPage newPage = new PDPage(PDRectangle.A4);
                        doc.addPage(newPage);
                        y = newPage.getMediaBox().getHeight() - 50;
                    }
                    if (alt) {
                        cs.setNonStrokingColor(new java.awt.Color(240, 246, 252));
                        cs.addRect(marginLeft, y - 4, page.getMediaBox().getWidth() - 80, 16);
                        cs.fill();
                    }
                    alt = !alt;
                    cs.beginText();
                    cs.setFont(PDType1Font.HELVETICA, 9);
                    cs.setNonStrokingColor(java.awt.Color.BLACK);
                    cs.newLineAtOffset(marginLeft + 4, y);
                    cs.showText(row.getNhan());
                    cs.newLineAtOffset(colW, 0);
                    cs.showText(String.valueOf(row.getSoHoaDon()));
                    cs.newLineAtOffset(colW, 0);
                    cs.showText(String.valueOf(row.getSoVe()));
                    cs.newLineAtOffset(colW, 0);
                    cs.showText(FMT_TIEN.format(row.getTongDoanhThu()));
                    cs.endText();
                    y -= 16;
                }
            }

            File f = File.createTempFile(tenFile("BaoCaoDoanhThuThang"), ".pdf");
            doc.save(f);
            return f;
        }
    }

    // =========================================================
    // STYLE HELPERS cho Apache POI
    // =========================================================

    private static CellStyle taoStyleHeader(XSSFWorkbook wb) {
        XSSFCellStyle style = wb.createCellStyle();
        XSSFFont font = wb.createFont();
        font.setBold(true);
        font.setColor(new XSSFColor(new byte[] { (byte) 255, (byte) 255, (byte) 255 }, null));
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);
        style.setFillForegroundColor(new XSSFColor(new byte[] { 41, (byte) 128, (byte) 185 }, null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }

    private static CellStyle taoStyleTitle(XSSFWorkbook wb) {
        XSSFCellStyle style = wb.createCellStyle();
        XSSFFont font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private static CellStyle taoStyleTien(XSSFWorkbook wb) {
        XSSFCellStyle style = wb.createCellStyle();
        XSSFDataFormat df = wb.createDataFormat();
        style.setDataFormat(df.getFormat("#,##0"));
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private static CellStyle taoStyleNormal(XSSFWorkbook wb) {
        XSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private static String buildKhoangThoiGian(BaoCaoFilterDTO f) {
        String from = f.getTuNgay() != null ? f.getTuNgay().toString() : "Đầu";
        String to = f.getDenNgay() != null ? f.getDenNgay().toString() : "cuối";
        return "Từ " + from + " đến " + to;
    }
}
