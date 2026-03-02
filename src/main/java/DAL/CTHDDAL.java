package DAL;

import DTO.CTHD;
import Exception.DaoException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CTHDDAL {
    private CTHD getCTHDObj(ResultSet rs) throws SQLException {
        CTHD cthd = new CTHD();
        cthd.setMaHoaDon(rs.getString("maHoaDon"));
        cthd.setMaTour(rs.getString("maTour"));
        cthd.setTenTour(rs.getString("tenTour"));
        cthd.setGiaTour(rs.getDouble("giaTour"));
        cthd.setSoLuongVe(rs.getInt("soLuongVe"));
        cthd.setTrangThai(rs.getString("trangThai"));
        cthd.setGhiChu(rs.getString("ghiChu"));
        cthd.setNgayKhoiHanh(rs.getDate("tgKhoiHanh").toLocalDate());
        cthd.setHoanTien(rs.getBoolean("hoanTien"));
        return cthd;
    }
    
    public ArrayList<CTHD> getDSCTHDTheoMaHD(String maHD) throws DaoException {
        ArrayList<CTHD> dscthd = new ArrayList<>();
        String sql = "SELECT cthd.*, t.tenTour, t.giaTour, t.tgKhoiHanh "
                + "FROM CTHD cthd "
                + "JOIN TOUR t ON cthd.maTour = t.maTour "
                + "WHERE cthd.maHoaDon = ? ";
        
        try (Connection con = ConnectionDAL.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                CTHD cthd = getCTHDObj(rs);
                dscthd.add(cthd);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Lỗi truy vấn Chi tiết hóa đơn!");
        }
        return dscthd;
    }
    
    public CTHD getCTHDTheoMaHDMaTour(String maHD, String maTour) throws DaoException {
        String sql = "SELECT cthd.*, t.tenTour, t.giaTour, t.tgKhoiHanh "
                + "FROM CTHD cthd "
                + "JOIN TOUR t ON cthd.maTour = t.maTour "
                + "WHERE cthd.maHoaDon = ? and cthd.maTour = ? ";
        
        try (Connection con = ConnectionDAL.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHD);
            ps.setString(2, maTour);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return getCTHDObj(rs);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Lỗi truy vấn Chi tiết hóa đơn");
        }
        return null;
    }
    
    public ArrayList<CTHD> getDSCTHDCoTheHuy(String maHD) throws DaoException {
        ArrayList<CTHD> dscthd = new ArrayList<>();
        String sql = "SELECT cthd.*, t.tenTour, t.giaTour, t.tgKhoiHanh "
                + "FROM CTHD cthd "
                + "JOIN TOUR t ON cthd.maTour = t.maTour "
                + "WHERE cthd.maHoaDon = ? and cthd.trangThai = ? ";
        
        try (Connection con = ConnectionDAL.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHD);
            ps.setString(2, "DA_DAT");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                CTHD cthd = getCTHDObj(rs);
                dscthd.add(cthd);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Lỗi lấy danh sách Chi tiết hóa đơn có thể hủy!");
        }
        return dscthd;
    }
    
    public boolean insertCTHD(CTHD cthd) throws DaoException {
        boolean result = false;
        String sql = "INSERT INTO CTHD "
                + "(maHoaDon, maTour, soLuongVe, trangThai) "
                + "VALUES (?,?,?,?)";
        
        try (Connection con = ConnectionDAL.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cthd.getMaHoaDon());
            ps.setString(2, cthd.getMaTour());
            ps.setInt(3, cthd.getSoLuongVe());
            ps.setString(4, cthd.getTrangThai());
            
            if (ps.executeUpdate() >= 1) result = true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Lỗi thêm Chi tiết hóa đơn!");
        }
        return result;
    }
    
    public boolean huyDatVe(String mahd, String matour, String trangthai, String ghichu) throws DaoException {
        boolean result = false;
        
        String sql = "UPDATE CTHD "
                + "SET trangThai = ?, ghiChu = ? "
                + "WHERE maHoaDon = ? AND maTour = ? ";
        
        try (Connection con = ConnectionDAL.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, trangthai);
            ps.setString(2, ghichu);
            ps.setString(3, mahd);
            ps.setString(4, matour);
            
            if (ps.executeUpdate() >= 1) result = true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Lỗi khi hủy đặt vé!");
        }
        return result;
    }
    
    public int autoHuyDatVeQuaHan() throws DaoException {
        String sql = "UPDATE cthd "
                + "SET trangThai = ?, hoanTien = ? "
                + "FROM CTHD cthd "
                + "JOIN HOADON hd ON hd.maHoaDon = cthd.maHoaDon "
                + "WHERE hd.trangThaiTT = ? AND DATEADD(DAY, 3, hd.ngayLapHD) < GETDATE() AND cthd.trangThai <> ? ";
        
        try (Connection con = ConnectionDAL.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "HUY_QUA_HAN");
            ps.setBoolean(2, false);
            ps.setBoolean(3, false);
            ps.setString(4, "HUY_QUA_HAN");
            
            return ps.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Lỗi tự động hủy hóa đơn quá hạn!");
        }
    }
    
    public int autoHuyDatVeDoCongTyHuyTour() throws DaoException {
        String sql = "UPDATE cthd "
                + "SET trangThai = ?, hoanTien = ?, ghiChu = ? "
                + "FROM CTHD cthd "
                + "JOIN TOUR t ON t.maTour = cthd.maTour "
                + "WHERE t.trangThai = ? AND cthd.trangThai = ? AND cthd.hoanTien = ? ";
        
        try (Connection con = ConnectionDAL.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "HUY_DO_CONG_TY");
            ps.setBoolean(2, false);
            ps.setString(3, "Tour bị hủy bởi công ty.");
            ps.setBoolean(4, false);
            ps.setString(5, "DA_DAT");
            ps.setBoolean(6, false);
            
            return ps.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Lỗi tự động hủy đặt vé do công ty hủy tour!");
        }
    }
    
    public boolean hoanTien(String mahd, String matour) throws DaoException {
        String sql = "UPDATE cthd "
                + "SET hoanTien = ? "
                + "FROM CTHD cthd "
                + "JOIN HOADON hd ON hd.maHoaDon = cthd.maHoaDon "
                + "WHERE cthd.maHoaDon = ? AND cthd.maTour = ? AND cthd.trangThai = ?  "
                + "AND cthd.hoanTien = ? AND hd.trangThaiTT = ?";
        
        try (Connection con = ConnectionDAL.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, true);
            ps.setString(2, mahd);
            ps.setString(3, matour);
            ps.setString(4, "HUY_DO_CONG_TY");
            ps.setBoolean(5, false);
            ps.setBoolean(6, true);
            
            return (ps.executeUpdate() >= 1);
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Lỗi khi hoàn tiền!");
        }
    }
    
    public int demSoLuongCanHoanTien() throws DaoException {
        String sql = "SELECT COUNT(*) "
               + "FROM CTHD cthd "
               + "JOIN HOADON hd ON hd.maHoaDon = cthd.maHoaDon "
               + "WHERE cthd.trangThai = ? AND cthd.hoanTien = ? AND hd.trangThaiTT = ?";
        
        try (Connection con = ConnectionDAL.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "HUY_DO_CONG_TY");
            ps.setBoolean(2, false);
            ps.setBoolean(3, true);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
            
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Lỗi tự động hủy đặt vé do công ty hủy tour!");
        }
    }
}
