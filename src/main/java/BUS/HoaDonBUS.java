package BUS;

import DAL.CTHDDAL;
import DAL.HoaDonDAL;
import DTO.CTHD;
import DTO.HoaDon;
import Exception.BusException;
import Exception.DaoException;
import Service.EmailService;
import Service.PDFService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.sql.SQLException;
import java.util.ArrayList;

public class HoaDonBUS {
    HoaDonDAL hddal = new HoaDonDAL();
    CTHDDAL cthddal = new CTHDDAL();

    public ArrayList<HoaDon> getAllHoaDon() throws BusException {
        try {
            return hddal.getAllHoaDon();
        } catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi tải danh sách Hóa đơn");
        }
    }

    public ArrayList<HoaDon> getHoaDonByTrangThaiTT(boolean tttt) throws BusException {
        try {
            return hddal.getHoaDonByTrangThaiTT(tttt);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi tải danh sách Hóa đơn có trạng thái hóa đơn: " + tttt);
        }
    }

    public HoaDon getHoaDonByMaHD(String mahd) throws BusException {
        try {
            return hddal.getHoaDonByMaHD(mahd);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi tải Hóa đơn có mã: " + mahd);
        }
    }

    public ArrayList<HoaDon> getHoaDonThanhToan() throws BusException {
        try {
            return hddal.getHoaDonThanhToan();
        } catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi tải danh sách Hóa đơn cần thanh toán!");
        }
    }

    public ArrayList<HoaDon> getHoaDonCoTheHuy() throws BusException {
        try {
            return hddal.getHoaDonCoTheHuy();
        } catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi tải danh sách Hóa đơn có thể hủy");
        }
    }

    public ArrayList<HoaDon> getHoaDonCanHoanTienDoTourBiHuy() throws BusException {
        try {
            return hddal.getHoaDonCanHoanTienDoTourBiHuy();
        } catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi tải danh sách Hóa đơn có tour bị hủy do công ty!");
        }
    }

    public String insertHoaDon(HoaDon hd) throws BusException {
        try {
            if (hddal.insertHoaDon(hd))
                return "Thêm hóa đơn thành công!";
            return "Thêm hóa đơn thất bại!";
        } catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi thêm hóa đơn!");
        }
    }

    public void sendEmail(HoaDon hd, ArrayList<CTHD> dscthd) throws BusException {
        try {
            String maHD = hd.getMaHoaDon();
            String filePath = PDFService.createHoaDonPDF(hd, dscthd);

            String noidung;
            if (!hd.isTrangThaiTT())
                noidung = "<h2>Xác nhận đặt tour</h2>"
                        + "<p>Mã hóa đơn: " + maHD + "</p>"
                        + "<p>Quý khách vui lòng thanh toán trong vòng 48h để hoàn tất đặt tour</p>";
            else
                noidung = "<h2>Thanh toán đặt tour thành công</h2>"
                        + "<p>Mã hóa đơn: " + maHD + "</p>"
                        + "<p>Quý khách vui lòng nhấn vào file đính kèm để xác nhận chi tiết nội dung thanh toán.</p>";

            CompletableFuture.runAsync(() -> {
                try {
                    EmailService.sendEmail(hd.getEmail(), "Xác nhận hóa đơn", noidung, filePath);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi gửi email!");
        }
    }

    public void sendEmailHoanTien(HoaDon hd, ArrayList<CTHD> dscthd, String tienhoan, String lydohuy)
            throws BusException {
        try {
            String maHD = hd.getMaHoaDon();
            String filePath = PDFService.createHoaDonHoanTienPDF(hd, dscthd, tienhoan, lydohuy);

            String content = "<h2>Xác nhận hủy tour</h2>"
                    + "<p>Mã hóa đơn: " + maHD + "</p>"
                    + "<p>Quý khách vui lòng nhấn vào file đính kèm để xem chi tiết nội dung hủy và hoàn tiền.</p>";

            CompletableFuture.runAsync(() -> {
                try {
                    EmailService.sendEmail(hd.getEmail(), "Xác nhận hủy tour", content, filePath);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi gửi email!");
        }
    }

    public String thanhToanHoaDon(String mahd, String httt) throws BusException {
        try {
            if (hddal.thanhToanHoaDon(mahd, httt))
                return "Thanh toán hóa đơn thành công!";
            else
                return "Thanh toán hóa đơn thất bại!";
        } catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi thanh toán hóa đơn!");
        }

    }

    public String updateTongTienHDSauHuy(String mahd) throws BusException {
        try {
            if (hddal.updateTongTienHDSauHuy(mahd))
                return "Cập nhật lại tổng tiền hóa đơn thành công!";
            else
                return "Cập nhật lại tổng tiền hóa đơn không thành công!";
        } catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi cập nhật lại tổng tiền hóa đơn!");
        }

    }

    public List<HoaDon> getHoaDonByMaTour(String maTour) throws BusException {
        try {
            return hddal.getHoaDonByMaTour(maTour);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi tải Hóa đơn có mã tour: " + maTour);
        }
    }
}
