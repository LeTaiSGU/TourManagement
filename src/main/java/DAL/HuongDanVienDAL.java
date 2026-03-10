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
