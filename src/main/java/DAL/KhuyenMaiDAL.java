package DAL;

import DTO.KhuyenMaiDTO;
import Exception.DaoException;
import java.sql.*;
import java.util.ArrayList;

public class KhuyenMaiDAL {
    public ArrayList<KhuyenMaiDTO> getAllKhuyenMai() throws DaoException {
        ArrayList<KhuyenMaiDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai";

        try (Connection con = getConnection(); 
             Statement st = con.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                KhuyenMaiDTO km = new KhuyenMaiDTO(
                        rs.getString("maKhuyenMai"),
                        rs.getString("tenKhuyenMai"),
                        rs.getString("phuongThucKM"),
                        rs.getString("moTa")
                );
                list.add(km);
            }
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi truy vấn khuyến mãi.", e);
        }
        return list;
    }
    public void addKhuyenMai(KhuyenMaiDTO km) throws DaoException {
        String sql = "INSERT INTO KhuyenMai(maKhuyenMai, tenKhuyenMai, phuongThucKM, moTa) VALUES (?, ?, ?, ?)";

        try (Connection con = getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, km.getMaKhuyenMai());
            ps.setString(2, km.getTenKhuyenMai());
            ps.setString(3, km.getPhuongThucKM());
            ps.setString(4, km.getMoTa());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi thêm khuyến mãi.", e);
        }
    }
    public void updateKhuyenMai(KhuyenMaiDTO km) throws DaoException {
        String sql = "UPDATE KhuyenMai SET tenKhuyenMai = ?, phuongThucKM = ?, moTa = ? WHERE maKhuyenMai = ?";

        try (Connection con = getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, km.getTenKhuyenMai());
            ps.setString(2, km.getPhuongThucKM());
            ps.setString(3, km.getMoTa());
            ps.setString(4, km.getMaKhuyenMai());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi cập nhật khuyến mãi.", e);
        }
    }
    public void deleteKhuyenMai(String maKhuyenMai) throws DaoException {
        String sql = "DELETE FROM KhuyenMai WHERE maKhuyenMai = ?";

        try (Connection con = getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKhuyenMai);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi xóa khuyến mãi.", e);
        }
    }
    private Connection getConnection() throws SQLException {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=tour_management;encrypt=false";
        String user = "sa";
        String password = "Mysqlserver02";
        return DriverManager.getConnection(url, user, password);
    }
}