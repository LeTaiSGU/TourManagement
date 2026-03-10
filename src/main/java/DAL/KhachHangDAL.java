package DAL;

import DTO.KhachHangDTO;
import Exception.DaoException;
import java.sql.*;
import java.util.ArrayList;

public class KhachHangDAL {
    // tai
    private ConnectionDAL conn = new ConnectionDAL();

    public ArrayList<KhachHangDTO> getAllKhachHang() throws DaoException {
        ArrayList<KhachHangDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE trangThai = 1";
        try (Connection con = conn.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                KhachHangDTO kh = new KhachHangDTO();
                kh.setMaKhachHang(rs.getString("maKhachHang"));
                kh.setTenKhachHang(rs.getString("tenKhachHang"));
                kh.setGioiTinh(rs.getString("gioiTinh"));
                kh.setNamSinh(rs.getInt("namSinh"));
                kh.setDiaChi(rs.getString("diaChi"));
                kh.setSoDienThoai(rs.getString("soDienThoai"));
                kh.setEmail(rs.getString("email"));
                kh.setMaLoaiKH(rs.getString("maLoaiKH"));
                list.add(kh);
            }
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi truy vấn danh sách khách hàng", e);
        }
        return list;
    }

    // tai

    public void addKhachHang(KhachHangDTO kh) throws DaoException {
        String sql = "INSERT INTO KhachHang(maKhachHang, tenKhachHang, gioiTinh, namSinh, diaChi, soDienThoai, email, maLoaiKH) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = conn.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kh.getMaKhachHang());
            ps.setString(2, kh.getTenKhachHang());
            ps.setString(3, kh.getGioiTinh());
            ps.setInt(4, kh.getNamSinh());
            ps.setString(5, kh.getDiaChi());
            ps.setString(6, kh.getSoDienThoai());
            ps.setString(7, kh.getEmail());
            ps.setString(8, kh.getMaLoaiKH());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi thêm khách hàng.", e);
        }
    }

    public void updateKhachHang(KhachHangDTO kh) throws DaoException {
        String sql = "UPDATE KhachHang SET tenKhachHang = ?, gioiTinh = ?, namSinh = ?, diaChi = ?, soDienThoai = ?, email = ?, maLoaiKH = ? WHERE maKhachHang = ?";
        try (Connection con = conn.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kh.getTenKhachHang());
            ps.setString(2, kh.getGioiTinh());
            ps.setInt(3, kh.getNamSinh());
            ps.setString(4, kh.getDiaChi());
            ps.setString(5, kh.getSoDienThoai());
            ps.setString(6, kh.getEmail());
            ps.setString(7, kh.getMaLoaiKH());
            ps.setString(8, kh.getMaKhachHang());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi cập nhật thông tin khách hàng.", e);
        }
    }

    public void deleteKhachHang(String maKhachHang) throws DaoException {
        String sql = "UPDATE KhachHang SET trangThai = 0 WHERE maKhachHang = ?";
        try (Connection con = conn.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKhachHang);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi xóa khách hàng.", e);
        }
    }

    public String sinhMaMoi() throws DaoException {
        String sql = "SELECT TOP 1 maKhachHang FROM KhachHang ORDER BY maKhachHang DESC";
        try (Connection con = conn.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String ma = rs.getString(1);
                int so = Integer.parseInt(ma.replaceAll("[^0-9]", "")) + 1;
                return String.format("KH%03d", so);
            }
            return "KH001";
        } catch (SQLException e) {
            throw new DaoException("Lỗi sinh mã khách hàng.", e);
        }
    }

    public ArrayList<KhachHangDTO> searchKhachHang(String keyword) throws DaoException {
        ArrayList<KhachHangDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE trangThai = 1 AND (tenKhachHang LIKE ? OR maKhachHang LIKE ?)";
        try (Connection con = conn.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                KhachHangDTO kh = new KhachHangDTO();
                kh.setMaKhachHang(rs.getString("maKhachHang"));
                kh.setTenKhachHang(rs.getString("tenKhachHang"));
                kh.setGioiTinh(rs.getString("gioiTinh"));
                kh.setNamSinh(rs.getInt("namSinh"));
                kh.setDiaChi(rs.getString("diaChi"));
                kh.setSoDienThoai(rs.getString("soDienThoai"));
                kh.setEmail(rs.getString("email"));
                kh.setMaLoaiKH(rs.getString("maLoaiKH"));
                list.add(kh);
            }
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi tìm kiếm khách hàng.", e);
        }
        return list;
    }
}
