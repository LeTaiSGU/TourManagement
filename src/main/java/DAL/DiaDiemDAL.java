package DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import DTO.DiaDiem;
import Exception.DaoException;

public class DiaDiemDAL {
    private ConnectionDAL conn = new ConnectionDAL();

    public List<DiaDiem> getAllDiaDiem() throws DaoException {
        List<DiaDiem> list = new ArrayList<>();
        String sql = "SELECT * FROM DIADIEM";
        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DiaDiem dd = new DiaDiem();
                dd.setMaDiaDiem(rs.getString("maDiaDiem"));
                dd.setTenDiaDiem(rs.getString("tenDiaDiem"));
                dd.setAnhDiaDiem(rs.getString("anhDiaDiem"));
                dd.setQuocGia(rs.getString("quocGia"));
                dd.setMoTa(rs.getString("moTa"));
                dd.setTrangThai(rs.getBoolean("trangThai"));
                list.add(dd);
            }
            return list;
        } catch (SQLException e) {
            throw new DaoException("Lỗi truy vấn địa điểm: " + e.getMessage());
        }
    }

    public String getTenDiaDiemByMa(String maDiaDiem) throws DaoException {
        String sql = "SELECT tenDiaDiem FROM DIADIEM WHERE maDiaDiem = ?";
        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setString(1, maDiaDiem);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("tenDiaDiem");
            }
            return null;
        } catch (SQLException e) {
            throw new DaoException("Lỗi truy vấn địa điểm: " + e.getMessage());
        }
    }

    public String getMaDiaDiemByTen(String tenDiaDiem) throws DaoException {
        String sql = "SELECT maDiaDiem FROM DIADIEM WHERE tenDiaDiem = ?";
        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setString(1, tenDiaDiem);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("maDiaDiem");
            }
            return null;
        } catch (SQLException e) {
            throw new DaoException("Lỗi truy vấn địa điểm: " + e.getMessage());
        }
    }
}
