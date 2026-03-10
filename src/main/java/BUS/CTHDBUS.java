package BUS;

import DAL.CTHDDAL;
import DTO.CTHD;
import Exception.BusException;
import Exception.DaoException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class CTHDBUS {
    CTHDDAL cthddal = new CTHDDAL();
    
    public ArrayList<CTHD> getDSCTHDTheoMaHD(String maHD) throws BusException {
        try {
            return cthddal.getDSCTHDTheoMaHD(maHD);
        }
        catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi tải danh sách Chi tiết hóa đơn!");
        }
    }
    public CTHD getCTHDTheoMaHDMaTour(String maHD, String maTour) throws BusException {
        try {
            return cthddal.getCTHDTheoMaHDMaTour(maHD, maTour);
        }
        catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi lấy Chi tiết hóa đơn!");
        }
    }

    public String insertCTHD(CTHD cthd) throws BusException {
        try {
            if (cthddal.insertCTHD(cthd))
                return "Thêm chi tiết hóa đơn thành công!";
            else 
                return "Thêm chi tiết hóa đơn thất bại!";
        }
        catch (DaoException e) {
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
        }
        catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi hủy đặt vé!");
        }
    }
    
    public int autoHuyDatVeQuaHan() throws BusException {
        try {
            return cthddal.autoHuyDatVeQuaHan();
        }
        catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi hủy đặt vé quá hạn!");
        }
    }
    
    public int autoHuyDatVeDoCongTyHuyTour() throws BusException {
        try {
            return cthddal.autoHuyDatVeDoCongTyHuyTour();
        }
        catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi tự động hủy đặt vé do công ty hủy tour!");
        }
    }
    
    public int demSoLuongCanHoanTien() throws BusException {
        try {
            return cthddal.demSoLuongCanHoanTien();
        }
        catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi đếm số lượng trường hợp cần hoàn tiền!");
        }
    }
    
    public boolean hoanTien(String mahd, String matour) throws BusException {
        try {
            return cthddal.hoanTien(mahd, matour);
        }
        catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi hoàn tiền!");
        }
    }
    
    public int tinhPhanTramHoanTien(CTHD cthd) {
        LocalDate ngayKhoiHanh = cthd.getNgayKhoiHanh();
        long soNgay = ChronoUnit.DAYS.between(LocalDate.now(), ngayKhoiHanh);

        int phantram = 0;
        if (soNgay <= 0) phantram = 0;
        else if (soNgay > 15) phantram = 100;
        else if (soNgay >= 8) phantram = 50;
        else phantram = 0;
        
        return phantram;
    }
    
    public int xuLyVeDaHoanTat() throws BusException {
        try {
            return cthddal.xuLyVeDaHoanTat();
        }
        catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi xử lứ những vé đã hoàn tất!");
        }
    }
}
