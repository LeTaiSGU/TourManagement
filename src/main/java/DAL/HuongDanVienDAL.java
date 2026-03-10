package DAL;

import DTO.HuongDanVienDTO;
import Exception.DaoException;
import java.sql.*;
import java.util.ArrayList;

public class HuongDanVienDAL {
    private ConnectionDAL connectionDAL = new ConnectionDAL();
    public ArrayList<HuongDanVienDTO> getAllHDV() throws DaoException {
        ArrayList<HuongDanVienDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM HuongDanVien";
        
        try (Connection conn = connectionDAL.getConnection(); 
             Statement st = conn.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                HuongDanVienDTO hdv = new HuongDanVienDTO(
                    rs.getString("maHDV"),
                    rs.getString("tenHDV"),
                    rs.getString("gioiTinh"),
                    rs.getInt("namSinh"),
                    rs.getString("chuyenMon"),
                    rs.getString("soDienThoai")
                );
                list.add(hdv);
            }
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi truy vấn danh sách hướng dẫn viên", e);
        }
        return list;
    }
    public void addHuongDanVien(HuongDanVienDTO hdv) throws DaoException {
        String sql = "INSERT INTO HuongDanVien (maHDV, tenHDV, gioiTinh, namSinh, chuyenMon, soDienThoai) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = connectionDAL.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hdv.getMaHDV());
            ps.setString(2, hdv.getTenHDV());
            ps.setString(3, hdv.getGioiTinh());
            ps.setInt(4, hdv.getNamSinh());
            ps.setString(5, hdv.getChuyenMon());
            ps.setString(6, hdv.getSoDienThoai());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi thêm hướng dẫn viên", e);
        }
    }
    public void updateHuongDanVien(HuongDanVienDTO hdv) throws DaoException {
        String sql = "UPDATE HuongDanVien SET tenHDV = ?, gioiTinh = ?, namSinh = ?, chuyenMon = ?, soDienThoai = ? "
                + "WHERE maHDV = ?";
        
        try (Connection conn = connectionDAL.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hdv.getTenHDV());
            ps.setString(2, hdv.getGioiTinh());
            ps.setInt(3, hdv.getNamSinh());
            ps.setString(4, hdv.getChuyenMon());
            ps.setString(5, hdv.getSoDienThoai());
            ps.setString(6, hdv.getMaHDV());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi sửa thông tin hướng dẫn viên", e);
        }
    }
    public void deleteHuongDanVien(String maHDV) throws DaoException {
        String sql = "DELETE FROM HuongDanVien WHERE maHDV = ?";
        
        try (Connection conn = connectionDAL.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHDV);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi xóa hướng dẫn viên", e);
        }
    }
}