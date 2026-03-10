package DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import DTO.PhuongTien;
import Exception.DaoException;

public class PhuongTienDAL {
    private ConnectionDAL conn = new ConnectionDAL();

    public List<PhuongTien> getAllPhuongTien() throws DaoException {
        List<PhuongTien> list = new ArrayList<>();
        String sql = "SELECT * FROM PHUONGTIEN";
        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                PhuongTien pt = new PhuongTien();
                pt.setMaPT(rs.getString("maPT"));
                pt.setTenPT(rs.getString("tenPT"));
                pt.setMoTa(rs.getString("moTa"));
                pt.setTrangThai(rs.getBoolean("trangThai"));
                list.add(pt);
            }
            return list;
        } catch (SQLException e) {
            throw new DaoException("Lỗi truy vấn phương tiện: " + e.getMessage());
        }
    }

    public String getTenPhuongTienByMa(String maPhuongTien) throws DaoException {
        String sql = "SELECT tenPT FROM PHUONGTIEN WHERE maPT = ?";
        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setString(1, maPhuongTien);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("tenPT");
            }
            return null;
        } catch (SQLException e) {
            throw new DaoException("Lỗi truy vấn phương tiện: " + e.getMessage());
        }
    }

    public String getMaPhuongTienByTen(String tenPhuongTien) throws DaoException {
        String sql = "SELECT maPT FROM PHUONGTIEN WHERE tenPT = ?";
        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setString(1, tenPhuongTien);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("maPT");
            }
            return null;
        } catch (SQLException e) {
            throw new DaoException("Lỗi truy vấn phương tiện: " + e.getMessage());
        }
    }
}