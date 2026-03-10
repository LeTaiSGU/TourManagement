package DAL;

import java.util.List;

import DTO.HuongDanVien;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Exception.DaoException;

public class HuongDanVienDAL {
    private ConnectionDAL conn = new ConnectionDAL();

    public List<HuongDanVien> getAllHuongDanVien() throws DaoException {
        List<HuongDanVien> list = new ArrayList();
        String sql = "SELECT * FROM HUONGDANVIEN";
        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                HuongDanVien hdv = HuongDanVien.builder()
                        .maHDV(rs.getString("maHDV"))
                        .tenHDV(rs.getString("tenHDV"))
                        .gioiTinh(rs.getString("gioiTinh"))
                        .namSinh(rs.getInt("namSinh"))
                        .chuyenMon(rs.getString("chuyenMon"))
                        .soDienThoai(rs.getString("soDienThoai"))
                        .trangThai(rs.getBoolean("trangThai"))
                        .build();
                list.add(hdv);
            }
            return list;
        } catch (SQLException e) {
            throw new DaoException("Lỗi truy vấn huớng dẫn viên: " + e.getMessage());
        }
    }
}

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