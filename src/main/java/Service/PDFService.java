package Service;

import DTO.CTHD;
import DTO.HoaDon;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;

import java.awt.Desktop;
import java.io.File;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PDFService {
    static DeviceRgb grayBg    = new DeviceRgb(230, 230, 230);
    static DeviceRgb blue      = new DeviceRgb(25, 25, 112);
    static DeviceRgb lightGray = new DeviceRgb(245, 245, 245);
    static DeviceRgb red       = new DeviceRgb(128, 0, 0);
    static DeviceRgb white     = new DeviceRgb(255, 255, 255);

    static DecimalFormat df       = new DecimalFormat("#,###");
    static DateTimeFormatter dtf  = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    static DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public static String createHoaDonHoanTienPDF(HoaDon hd, ArrayList<CTHD> dsct, String tienhoan, String lydohuy) {
        String path = "HoaDonPDF/HOAN_TIEN/" + hd.getMaHoaDon() + ".pdf";

        try {
            File file = new File(path);
            if (!file.exists()) 
                taoHoaDonHoanTienPDF(hd, dsct, path, tienhoan, lydohuy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }
    public static String createHoaDonPDF(HoaDon hd, ArrayList<CTHD> dsct) {
        String folder;
        boolean thanhtoan;
        
        if (hd.isTrangThaiTT() == false) {
            folder = "HoaDonPDF/Chua_Thanh_Toan/";
            thanhtoan = false;
        }
        else {
            folder = "HoaDonPDF/Da_Thanh_Toan/";
            thanhtoan = true;
        }
        String path = folder + hd.getMaHoaDon() + ".pdf";
        File file = new File(path);

        try {
            if (!file.exists())
                taoPDF(hd, dsct, path, thanhtoan);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }
    
    private static void addHeader(Document document, String title, PdfFont boldFont) {
        try {
            float[] columnWidths = {100, 400}; // điều chỉnh theo kích thước logo và title
            Table headerTable = new Table(columnWidths).useAllAvailableWidth();

            ImageData logoData = ImageDataFactory.create(PDFService.class.getResource("/image/icon/logoHoaDonPDF.png"));
            Image logo = new Image(logoData);
            logo.scaleToFit(100, 100); 
            logo.setHorizontalAlignment(HorizontalAlignment.LEFT);

            Cell logoCell = new Cell()
                .add(logo)
                .setBorder(Border.NO_BORDER)
                .setPadding(10)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBackgroundColor(white);

            headerTable.addCell(logoCell);

            Paragraph titlePara = new Paragraph(title)
                .setFont(boldFont)
                .setFontSize(22)
                .setTextAlignment(TextAlignment.RIGHT); 

            Cell titleCell = new Cell()
                .add(titlePara)
                .setBorder(Border.NO_BORDER)
                .setPadding(15)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBackgroundColor(white);

            headerTable.addCell(titleCell);

            document.add(headerTable);

            Paragraph linePara = new Paragraph()
                .setBorderBottom(new SolidBorder(new DeviceRgb(150, 150, 150), 0.5f)) 
                .setMarginBottom(10f)
                .setWidth(UnitValue.createPercentValue(100));

            document.add(linePara);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addInfoTable(Document document, HoaDon hd, PdfFont boldFont) {
        Table info = new Table(2).useAllAvailableWidth();
        info.addCell(cellInfo("MÃ HÓA ĐƠN:", hd.getMaHoaDon(), boldFont));
        info.addCell(cellInfo("MÃ KHÁCH HÀNG:", hd.getMaKhachHang(), boldFont));
        info.addCell(cellInfo("MÃ NHÂN VIÊN:", hd.getMaNhanVien(), boldFont));
        info.addCell(cellInfo("TÊN KHÁCH HÀNG:", hd.getTenKhachHang(), boldFont));
        info.addCell(cellInfo("NGÀY:", hd.getNgayLapHD().format(dtf2), boldFont));
        info.addCell(cellInfo("SĐT:", hd.getSdt(), boldFont));
        document.add(info);
        document.add(new Paragraph("\n"));
    }

    private static double addDetailTable(Document document, ArrayList<CTHD> dsct, PdfFont boldFont) {
        float[] col = {80, 200, 100, 80, 120};
        Table table = new Table(col).useAllAvailableWidth();

        DeviceRgb blue = new DeviceRgb(25, 25, 112);
        table.addHeaderCell(headerCell("MÃ TOUR", boldFont, blue));
        table.addHeaderCell(headerCell("TÊN TOUR", boldFont, blue));
        table.addHeaderCell(headerCell("ĐƠN GIÁ", boldFont, blue));
        table.addHeaderCell(headerCell("SỐ LƯỢNG", boldFont, blue));
        table.addHeaderCell(headerCell("THÀNH TIỀN", boldFont, blue));

        double tong = 0;
        boolean alt = false;

        for (CTHD ct : dsct) {
            double gia = ct.getGiaTour();
            double tt = gia * ct.getSoLuongVe();
            tong += tt;
            DeviceRgb rowColor = alt ? lightGray : white;
            alt = !alt;

            table.addCell(cellData(ct.getMaTour(), rowColor));
            table.addCell(cellData(ct.getTenTour(), rowColor));
            table.addCell(cellData(df.format(gia), rowColor));
            table.addCell(cellData(String.valueOf(ct.getSoLuongVe()), rowColor));
            table.addCell(cellData(df.format(tt), rowColor));
        }

        document.add(table);
        document.add(new Paragraph("\n"));
        return tong;
    }

    private static void taoPDF(HoaDon hd, ArrayList<CTHD> dsct, String path, boolean daThanhToan) {
        try {
            PdfWriter writer = new PdfWriter(path);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            
            PdfFont font           = PdfFontFactory.createFont("font/arial_regular.ttf", PdfEncodings.IDENTITY_H);
            PdfFont boldFont       = PdfFontFactory.createFont("font/arial_bold.ttf", PdfEncodings.IDENTITY_H);
            PdfFont italicFont     = PdfFontFactory.createFont("font/arial_italic.ttf", PdfEncodings.IDENTITY_H);
            PdfFont boldItalicFont = PdfFontFactory.createFont("font/arial_bolditalic.ttf", PdfEncodings.IDENTITY_H);

            document.setFont(font);

            String title = daThanhToan ? "HÓA ĐƠN THANH TOÁN DỊCH VỤ" : "PHIẾU XÁC NHẬN ĐẶT TOUR";
            addHeader(document, title, boldFont);
            addInfoTable(document, hd, boldFont);
            double tong = addDetailTable(document, dsct, boldFont);

            double thue = tong * 0.1;
            double total = tong + thue;
            document.add(new Paragraph("TẠM TÍNH: " + df.format(tong)));
            document.add(new Paragraph("THUẾ (10%): " + df.format(thue)));
            document.add(
                new Paragraph("TỔNG TIỀN: " + df.format(total))
                    .setFont(boldFont)
                    .setFontSize(14)
            );

            if (!daThanhToan) {
                LocalDate han = hd.getNgayLapHD().plusDays(2);
                document.add(
                    new Paragraph("NGÀY ĐẾN HẠN THANH TOÁN: " + han.format(dtf2)).setFont(boldFont).setFontColor(red)
                );
            }
            else {
                document.add(new Paragraph("Đã thanh toán: " + df.format(total)).setFont(boldFont).setFontSize(14).setFontColor(red));
                document.add(new Paragraph("Thời gian thanh toán: " + dtf.format(LocalDateTime.now())).setFont(boldFont).setFontSize(14).setFontColor(red));
            }

            document.add(new Paragraph("----------------------------------------------------------------"));

            if (!daThanhToan) {
                document.add(new Paragraph("LƯU Ý:").setFont(boldFont));
                document.add(new Paragraph(
                        "QUÝ KHÁCH VUI LÒNG THANH TOÁN HÓA ĐƠN TRƯỚC NGÀY ĐẾN HẠN THANH TOÁN ĐỂ HOÀN THÀNH ĐẶT TOUR.\n" + 
                        "NẾU QUÁ HẠN THANH TOÁN THÌ HÓA ĐƠN SẼ TỰ ĐỘNG ĐƯỢC HỦY."
                ));
            }
            else {
                document.add(new Paragraph(
                    "CẢM ƠN QUÝ KHÁCH ĐÃ SỬ DỤNG DỊCH VỤ.\n" +
                    "CHÚC QUÝ KHÁCH CÓ MỘT CHUYẾN DU LỊCH THẬT VUI VẺ!"
                ));
            }
            
            document.close();
            Desktop.getDesktop().open(new File(path));

        } 
        catch (Exception e) {
            e.printStackTrace();
        }
}
    
    private static void taoHoaDonHoanTienPDF(HoaDon hd, ArrayList<CTHD> dsct, String path, String tienhoan, String lydohuy) {
        try {
            boolean thanhtoan = hd.isTrangThaiTT();
            PdfWriter writer = new PdfWriter(path);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            
            PdfFont font           = PdfFontFactory.createFont("font/arial_regular.ttf", PdfEncodings.IDENTITY_H);
            PdfFont boldFont       = PdfFontFactory.createFont("font/arial_bold.ttf", PdfEncodings.IDENTITY_H);
            PdfFont italicFont     = PdfFontFactory.createFont("font/arial_italic.ttf", PdfEncodings.IDENTITY_H);
            PdfFont boldItalicFont = PdfFontFactory.createFont("font/arial_bolditalic.ttf", PdfEncodings.IDENTITY_H);
            document.setFont(font);

            String title = thanhtoan ? "HÓA ĐƠN HOÀN TIỀN" : "XÁC NHẬN HỦY TOUR";
            addHeader(document, title, boldFont);
            addInfoTable(document, hd, boldFont);
            double tong = addDetailTable(document, dsct, boldFont);

            double thue = tong * 0.1;
            double total = tong + thue;
            document.add(new Paragraph("TẠM TÍNH: " + df.format(tong)));
            document.add(new Paragraph("THUẾ (10%): " + df.format(thue)));
            document.add(
                new Paragraph("TỔNG TIỀN: " + df.format(total))
                    .setFont(boldFont)
                    .setFontSize(14)
            );

            double dathanhtoan = 0;
            if (thanhtoan) dathanhtoan = total;
            else dathanhtoan = 0;
            document.add(new Paragraph("Đã thanh toán: " + df.format(dathanhtoan)).setFont(boldFont).setFontSize(14));
            document.add(new Paragraph("Số tiền hoàn trả: " + tienhoan).setFont(boldFont).setFontSize(14).setFontColor(red));
            document.add(new Paragraph("Thời gian hoàn tiền: " + dtf.format(LocalDateTime.now())).setFont(boldFont).setFontSize(14));
            document.add(new Paragraph("Lý do hủy: " + lydohuy).setFont(boldItalicFont).setFontSize(14));

            document.add(new Paragraph("----------------------------------------------------------------"));
            
            document.add(new Paragraph(
                "CẢM ƠN QUÝ KHÁCH ĐÃ SỬ DỤNG DỊCH VỤ.\n" +
                "MONG SỚM ĐƯỢC TIẾP TỤC PHỤC VỤ QUÝ KHÁCH TRONG TƯƠNG LAI!"
            ));
            
            document.close();
            Desktop.getDesktop().open(new File(path));

        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static Cell cellInfo(String label, String value, PdfFont boldFont) {
        Paragraph p = new Paragraph()
                .add(new Text(label).setFont(boldFont))
                .add(" " + value);

        return new Cell().add(p).setBorder(Border.NO_BORDER);
    }

    private static Cell headerCell(String text, PdfFont font, DeviceRgb bg) {
        return new Cell()
                .add(new Paragraph(text)
                        .setFont(font)
                        .setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(bg)
                .setTextAlignment(TextAlignment.CENTER);
    }

    private static Cell cellData(String text, DeviceRgb bg) {
        return new Cell()
                .add(new Paragraph(text))
                .setBackgroundColor(bg);
    }
}
