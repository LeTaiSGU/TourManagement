package DAL;

import DTO.TaiKhoan;
import Exception.DaoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanDAL {
    private ConnectionDAL con = new ConnectionDAL();

    public List<TaiKhoan> getAllAccount() throws DaoException {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = "SELECT * FROM TAIKHOAN";
        try (Connection conn = con.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TaiKhoan tk = TaiKhoan.builder()
                        .maNhanVien(rs.getString(1))
                        .matKhau(rs.getString(2))
                        .maNhomQuyen(rs.getString(3))
                        .trangThai(rs.getBoolean(4))
                        .build();
                list.add(tk);
            }
        } catch (SQLException ex) {
            throw new DaoException("Lỗi khi lấy danh sách tài khoản: " + ex.getMessage());
        }
        return list;
    }
}
