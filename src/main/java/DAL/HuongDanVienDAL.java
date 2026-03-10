package DAL;

import java.util.List;

import DTO.HuongDanVien;
import DTO.HuongDanVienDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Exception.DaoException;

public class HuongDanVienDAL {
    private ConnectionDAL conn = new ConnectionDAL();

    public List<HuongDanVien> getAllHuongDanVien() throws DaoException {
        List<HuongDanVien> list = new ArrayList();
        String sql = "SELECT * FROM HUONGDANVIEN WHERE trangThai = 1";
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

    public ArrayList<HuongDanVienDTO> getAllHDV() throws DaoException {
        ArrayList<HuongDanVienDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM HuongDanVien WHERE trangThai = 1";

        try (Connection con = conn.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                HuongDanVienDTO hdv = new HuongDanVienDTO(
                        rs.getString("maHDV"),
                        rs.getString("tenHDV"),
                        rs.getString("gioiTinh"),
                        rs.getInt("namSinh"),
                        rs.getString("chuyenMon"),
                        rs.getString("soDienThoai"));
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

        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
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

        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
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

    public String sinhMaMoi() throws DaoException {
        String sql = "SELECT TOP 1 maHDV FROM HuongDanVien ORDER BY maHDV DESC";
        try (Connection con = conn.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                String ma = rs.getString(1);
                int so = Integer.parseInt(ma.replaceAll("[^0-9]", "")) + 1;
                return String.format("HDV%03d", so);
            }
            return "HDV001";
        } catch (SQLException e) {
            throw new DaoException("Lỗi sinh mã hướng dẫn viên.", e);
        }
    }

    public ArrayList<HuongDanVienDTO> searchHuongDanVien(String keyword) throws DaoException {
        ArrayList<HuongDanVienDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM HuongDanVien WHERE trangThai = 1 AND (tenHDV LIKE ? OR maHDV LIKE ?)";
        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new HuongDanVienDTO(
                        rs.getString("maHDV"), rs.getString("tenHDV"),
                        rs.getString("gioiTinh"), rs.getInt("namSinh"),
                        rs.getString("chuyenMon"), rs.getString("soDienThoai")));
            }
        } catch (SQLException e) {
            throw new DaoException("Lỗi tìm kiếm hướng dẫn viên.", e);
        }
        return list;
    }

    public void deleteHuongDanVien(String maHDV) throws DaoException {
        String sql = "UPDATE HuongDanVien SET trangThai = 0 WHERE maHDV = ?";

        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHDV);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi xóa hướng dẫn viên", e);
        }
    }
}