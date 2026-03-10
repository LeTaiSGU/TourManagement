package BUS;

import DAL.CTHDDAL;
import DAL.HoaDonDAL;
import DAL.KhachHangDAL;
import DAL.TourDAL;
import DTO.CTHD;
import DTO.HoaDon;
import DTO.KhachHang;
import Exception.BusException;
import Exception.DaoException;
import Service.EmailService;
import Service.PDFService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CTHDBUS {
    CTHDDAL cthddal = new CTHDDAL();
    KhachHangDAL khachHangDAL = new KhachHangDAL();
    HoaDonDAL hddal = new HoaDonDAL();
    HoaDonBUS hdbus = new HoaDonBUS();
    TourDAL tourDAL = new TourDAL();

    public ArrayList<CTHD> getDSCTHDTheoMaHD(String maHD) throws BusException {
        try {
            return cthddal.getDSCTHDTheoMaHD(maHD);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi tải danh sách Chi tiết hóa đơn!");
        }
    }

    public CTHD getCTHDTheoMaHDMaTour(String maHD, String maTour) throws BusException {
        try {
            return cthddal.getCTHDTheoMaHDMaTour(maHD, maTour);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi lấy Chi tiết hóa đơn!");
        }
    }

    public ArrayList<CTHD> getDSCTHDCoTheHuy(String maHD) throws BusException {
        try {
            return cthddal.getDSCTHDCoTheHuy(maHD);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi tải danh sách Chi tiết hóa đơn!");
        }
    }

    public String insertCTHD(CTHD cthd) throws BusException {
        try {
            if (cthddal.insertCTHD(cthd))
                return "Thêm chi tiết hóa đơn thành công!";
            else
                return "Thêm chi tiết hóa đơn thất bại!";
        } catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi thêm chi tiết hóa đơn!");
        }
    }

    public String huyDatVe(String mahd, String matour, String trangthai, String ghichu) throws BusException {
        try {
            if (cthddal.huyDatVe(mahd, matour, trangthai, ghichu))
                return "Hủy đặt vé thành công!";
            else
                return "Hủy đặt vé thất bại!";
        } catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi hủy đặt vé!");
        }
    }

    public int autoHuyDatVeQuaHan() throws BusException {
        try {
            return cthddal.autoHuyDatVeQuaHan();
        } catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi hủy đặt vé quá hạn!");
        }
    }

    public int autoHuyDatVeDoCongTyHuyTour() throws BusException {
        try {
            return cthddal.autoHuyDatVeDoCongTyHuyTour();
        } catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi tự động hủy đặt vé do công ty hủy tour!");
        }
    }

    public int demSoLuongCanHoanTien() throws BusException {
        try {
            return cthddal.demSoLuongCanHoanTien();
        } catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi đếm số lượng trường hợp cần hoàn tiền!");
        }
    }

    public boolean hoanTien(String mahd, String matour) throws BusException {
        try {
            return cthddal.hoanTien(mahd, matour);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi hoàn tiền!");
        }
    }

    public int tinhPhanTramHoanTien(CTHD cthd) {
        LocalDate ngayKhoiHanh = cthd.getNgayKhoiHanh();
        long soNgay = ChronoUnit.DAYS.between(LocalDate.now(), ngayKhoiHanh);

        int phantram = 0;
        if (soNgay <= 0)
            phantram = 0;
        else if (soNgay > 15)
            phantram = 100;
        else if (soNgay >= 8)
            phantram = 50;
        else
            phantram = 0;

        return phantram;
    }

    public int xuLyVeDaHoanTat() throws BusException {
        try {
            return cthddal.xuLyVeDaHoanTat();
        } catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi xử lứ những vé đã hoàn tất!");
        }
    }

    public Boolean setUpCTHD(String maTour, String lydohuy, Boolean hoanTien) throws BusException {
        try {
            return cthddal.setUpCTHD(maTour, lydohuy, hoanTien);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi thiết lập chi tiết hóa đơn!");
        }
    }

    public void thietLapHoanTien(String maTour, String lydohuy)
            throws BusException {

        try {
            double giaTour = tourDAL.getGiaTour(maTour);
            List<HoaDon> dshd = hdbus.getHoaDonByMaTour(maTour);
            if (dshd.isEmpty()) {
                return;
            }
            for (HoaDon hd : dshd) {
                ArrayList<CTHD> dscthd = getDSCTHDTheoMaHD(hd.getMaHoaDon());
                System.out.println("[DEBUG] maHD = " + hd.getMaHoaDon() + ", so CTHD = " + dscthd.size());
                System.out.println("[DEBUG] tenKhachHang = " + hd.getTenKhachHang());
                System.out.println("[DEBUG] sdt = " + hd.getSdt());
                CTHD cthdHoanTien = dscthd.stream()
                        .filter(ct -> ct.getMaTour().equals(maTour))
                        .findFirst()
                        .orElse(null);
                if (cthdHoanTien == null) {
                    System.out.println("[DEBUG] khong tim thay CTHD co maTour = " + maTour + " trong maHD = "
                            + hd.getMaHoaDon());
                } else {
                    double tongTien = tourDAL.getGiaTour(maTour) * cthdHoanTien.getSoLuongVe();
                    double thuePhanHoan = tongTien + (tongTien * 0.1);
                    // if (hd.getMaKhachHang() == null){

                    // } // tru gia khuyen mai ra la xong
                    String tienHoan = String.format("%,.0f", thuePhanHoan);
                    String maHD = hd.getMaHoaDon();
                    hddal.updateHoaDonHuyDoCongTy(maHD, tongTien);
                    String filePath = PDFService.createHoaDonHoanTienHuyTourPDF(hd, dscthd, tienHoan,
                            lydohuy);
                    String email = hddal.getEmailKhachHang(maHD);
                    String content = "<h2>Tổng công ty dịch vụ du lịch Alat xin chào quý khách!</h2>"
                            + "<p>Chúng tôi rất tiếc khi phải thông báo với quý khách rằng một số dịch vụ quý khách đã đặt đã bị hủy.</p>"
                            + "<p>Chúng tôi rất xin lỗi về trải nghiệm không tốt này. "
                            + "Để được hoàn tiền, hãy đem hóa đơn đến trụ sở chính hoặc liên hệ qua hotline: 0988.399.999 để được hỗ trợ.</p>"
                            + "<p>Mã hóa đơn: " + maHD + "</p>"
                            + "<p>Lý do hủy: " + lydohuy + "</p>"
                            + "<p>Số tiền hoàn: " + tienHoan + "</p>"
                            + "<p>Quý khách vui lòng nhấn vào file đính kèm để xem chi tiết nội dung hủy và hoàn tiền.</p>"
                            + "<p>Rất xin lỗi và cảm ơn quý khách đã lựa chọn dịch vụ của chúng tôi.</p>"
                            + "<p>Hẹn gặp lại.</p>";
                    CompletableFuture.runAsync(() -> {
                        try {
                            EmailService.sendEmail(email, "Xác nhận hủy tour", content, filePath);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi tạo hóa đơn!");
        }
    }
}
