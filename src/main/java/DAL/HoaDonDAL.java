package DAL;

import DTO.HoaDon;
import Exception.DaoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAL {
    
    private HoaDon getHoaDonObj(ResultSet rs) throws SQLException {
        HoaDon hd = new HoaDon();
        hd.setMaHoaDon(rs.getString("maHoaDon"));
        hd.setMaNhanVien(rs.getString("maNhanVien"));
        hd.setMaKhachHang(rs.getString("maKhachHang"));
        hd.setTenKhachHang(rs.getString("tenKhachHang"));
        hd.setSdt(rs.getString("soDienThoai"));
        hd.setNgayLapHD(rs.getDate("ngayLapHD").toLocalDate());
        hd.setTongTien(rs.getDouble("tongTien"));
        hd.setMaKhuyenMai(rs.getString("maKhuyenMai"));
        hd.setThue(rs.getFloat("thue"));
        hd.setHTTT(rs.getString("HTTT"));
        hd.setTrangThaiTT(rs.getBoolean("trangthaiTT"));
        return hd;
    }
    public ArrayList<HoaDon> getAllHoaDon() throws DaoException {
        ArrayList<HoaDon> dshd = new ArrayList<>();
        String sql = "SELECT hd.*, kh.tenKhachHang, kh.soDienThoai "
                + "FROM HOADON hd "
                + "JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang";
        
        try (Connection con = ConnectionDAL.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                HoaDon hd = getHoaDonObj(rs);
                dshd.add(hd);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Lỗi truy vấn Hóa đơn!");
        }
        return dshd;
    }
    
    public ArrayList<HoaDon> getHoaDonByTrangThaiTT(boolean tttt) throws DaoException {
        ArrayList<HoaDon> dshd = new ArrayList<>();
        String sql = "SELECT hd.*, kh.tenKhachHang, kh.soDienThoai "
                + "FROM HOADON hd "
                + "JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang "
                + "WHERE hd.trangThaiTT = ? ";
        
        try (Connection con = ConnectionDAL.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, tttt);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                HoaDon hd = getHoaDonObj(rs);
                dshd.add(hd);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Lỗi truy vấn Hóa đơn có trạng thái hóa đơn: " + tttt);
        }
        return dshd;
    }
    
    public HoaDon getHoaDonByMaHD(String mahd) throws DaoException {
        String sql = "SELECT hd.*, kh.tenKhachHang, kh.soDienThoai "
                + "FROM HOADON hd "
                + "JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang "
                + "WHERE hd.maHoaDon = ? ";
        
        try (Connection con = ConnectionDAL.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, mahd);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return getHoaDonObj(rs);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Lỗi truy vấn Hóa đơn có mã: " + mahd);
        }
        return null;
    }
    
    public ArrayList<HoaDon> getHoaDonThanhToan() throws DaoException {
        ArrayList<HoaDon> dshd = new ArrayList<>();
        String sql = "SELECT DISTINCT hd.*, kh.tenKhachHang, kh.soDienThoai "
                + "FROM HOADON hd "
                + "JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang "
                + "JOIN CTHD cthd ON cthd.maHoaDon = hd.maHoaDon "
                + "WHERE hd.trangThaiTT = ? and cthd.trangThai = ? ";
        
        try (Connection con = ConnectionDAL.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, false);
            ps.setString(2, "DA_DAT");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                HoaDon hd = getHoaDonObj(rs);
                dshd.add(hd);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Lỗi truy vấn Hóa đơn cần hủy!");
        }
        return dshd;
    }
    
    public ArrayList<HoaDon> getHoaDonCoTheHuy() throws DaoException {
        ArrayList<HoaDon> dshd = new ArrayList<>();
        String sql = "SELECT DISTINCT hd.*, kh.tenKhachHang, kh.soDienThoai "
                + "FROM HOADON hd "
                + "JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang "
                + "JOIN CTHD cthd ON cthd.maHoaDon = hd.maHoaDon "
                + "WHERE cthd.trangThai = ? ";
        
        try (Connection con = ConnectionDAL.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "DA_DAT");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                HoaDon hd = getHoaDonObj(rs);
                dshd.add(hd);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Lỗi truy vấn Hóa đơn cần hủy!");
        }
        return dshd;
    }
    
    public ArrayList<HoaDon> getHoaDonCanHoanTienDoTourBiHuy() throws DaoException {
        ArrayList<HoaDon> dshd = new ArrayList<>();
        String sql = "SELECT DISTINCT hd.*, kh.tenKhachHang, kh.soDienThoai "
                   + "FROM HOADON hd "
                   + "JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang "
                   + "JOIN CTHD cthd ON cthd.maHoaDon = hd.maHoaDon "
                   + "WHERE cthd.trangThai = ? AND cthd.hoanTien = ? AND hd.trangThaiTT = ? ";

        try (Connection con = ConnectionDAL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "HUY_DO_CONG_TY");
            ps.setBoolean(2, false);
            ps.setBoolean(3, true);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                HoaDon hd = getHoaDonObj(rs);
                dshd.add(hd);
            }
        } catch (SQLException e) {
            throw new DaoException("Lỗi lấy hóa đơn có tour hủy do công ty!");
        }
        return dshd;
    }
    
    public boolean insertHoaDon(HoaDon hd) throws DaoException {
        boolean result = false;
        String sql = "INSERT INTO HOADON "
                + "(maHoaDon, maNhanVien, maKhachHang, ngayLapHD, tongTien, maKhuyenMai, thue, trangThaiTT) "
                + "VALUES (?,?,?,?,?,?,?,?)";
        
        String mahd = generateMaHD();
        hd.setMaHoaDon(mahd);
        
        try (Connection con = ConnectionDAL.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, hd.getMaHoaDon());
            ps.setString(2, hd.getMaNhanVien());
            ps.setString(3, hd.getMaKhachHang());
            ps.setDate(4, Date.valueOf(hd.getNgayLapHD()));
            ps.setDouble(5, hd.getTongTien());
            if (hd.getMaKhuyenMai().trim().equalsIgnoreCase("null") || hd.getMaKhuyenMai().trim().isEmpty()) {
                ps.setNull(6, java.sql.Types.VARCHAR);
            } else {
                ps.setString(6, hd.getMaKhuyenMai());
            }
            ps.setFloat(7, hd.getThue());
            ps.setBoolean(8, hd.isTrangThaiTT());
            
            if (ps.executeUpdate() >= 1) result = true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Lỗi thêm Hóa đơn");
        }
        return result;
    }
    private String generateMaHD() throws DaoException {
        int nextid = 1;
        String sql = "SELECT TOP 1 maHoaDon "
                        + "FROM HOADON "
                        + "ORDER BY CAST(REPLACE(maHoaDon, 'HD', '') AS INT) DESC";
        
        try (Connection con = ConnectionDAL.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery())
        {
            if (rs.next()) {
                String lastMaHD = rs.getString("maHoaDon");
                int lastid = Integer.parseInt(lastMaHD.substring(2));
                nextid = lastid + 1;
            }            
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Lỗi tạo mã hóa đơn");
        }
        return "HD" + String.format("%03d", nextid);
    } 
    
    public boolean thanhToanHoaDon(String mahd, String httt) throws DaoException {
        boolean result = false;
        
        String sql = "UPDATE HOADON "
                + "SET trangThaiTT = ?, HTTT = ? "
                + "WHERE maHoaDon = ?";
        
        try (Connection con = ConnectionDAL.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, true);
            ps.setString(2, httt);
            ps.setString(3, mahd);
            if (ps.executeUpdate() >= 1) result = true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Lỗi khi thanh toán hóa đơn!");
        }
        return result;
    }
   
    public boolean updateTongTienHDSauHuy(String mahd) throws DaoException {
        boolean result = false;

        String sql = "UPDATE HOADON "
                + "SET tongTien = ( "
                + "    SELECT ISNULL(SUM(cthd.soLuongVe * t.giaTour * 1.1), 0) "
                + "    FROM CTHD cthd "
                + "    JOIN TOUR t ON cthd.maTour = t.maTour "
                + "    WHERE cthd.maHoaDon = ? AND cthd.trangThai = 'DA_DAT' "
                + ") "
                + "WHERE maHoaDon = ? AND trangThaiTT = 0";

        try (Connection con = ConnectionDAL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, mahd);
            ps.setString(2, mahd);

            if (ps.executeUpdate() >= 1) result = true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Lỗi update tổng tiền!");
        }

        return result;
    }
}
