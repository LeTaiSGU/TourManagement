package DAL;

import DTO.TaiKhoan;
import DTO.TaiKhoanDTO;
import Exception.DaoException;
import java.sql.CallableStatement;
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

    public Boolean editTaiKhoan(TaiKhoan tk) throws DaoException {
        String sql = "{call editTaiKhoan(?,?,?)}";

        try (Connection conn = con.getConnection();
                CallableStatement call = conn.prepareCall(sql)) {

            call.setString(1, tk.getMaNhanVien());
            call.setString(2, tk.getMatKhau());
            call.setString(3, tk.getMaNhomQuyen());

            int rows = call.executeUpdate();

            return rows > 0;

        } catch (SQLException ex) {
            throw new DaoException("Lỗi không lưu được tài khoản: " + ex.getMessage());
        }
    }

    public Boolean unblockAccount(TaiKhoan tk) throws DaoException {
        String sql = "{call setStatusAcount(?,?)}";

        try (Connection conn = con.getConnection();
                CallableStatement call = conn.prepareCall(sql)) {
            call.setString(1, tk.getMaNhanVien());
            call.setBoolean(2, tk.getTrangThai());
            int rows = call.executeUpdate();
            System.out.println("Rows affected: " + rows);
            return rows > 0;

        } catch (SQLException ex) {
            throw new DaoException("Lỗi không chuyển trạng thái tài khoản được: " + ex.getMessage());
        }
    }

    public List<TaiKhoan> searchTaiKhoan(String maNhanVien, String maNhomQuyen, Boolean trangThai) throws DaoException {
        List<TaiKhoan> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM TAIKHOAN WHERE 1=1");

        if (maNhanVien != null && !maNhanVien.trim().isEmpty())
            sql.append(" AND maNhanVien LIKE '%").append(maNhanVien.trim()).append("%'");
        if (maNhomQuyen != null && !maNhomQuyen.trim().isEmpty() && !maNhomQuyen.equals("..."))
            sql.append(" AND maNhomQuyen = '").append(maNhomQuyen.trim()).append("'");
        if (trangThai != null)
            sql.append(" AND trangThai = ").append(trangThai ? 1 : 0);

        try (Connection conn = con.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString());
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(TaiKhoan.builder()
                        .maNhanVien(rs.getString(1))
                        .matKhau(rs.getString(2))
                        .maNhomQuyen(rs.getString(3))
                        .trangThai(rs.getBoolean(4))
                        .build());
            }
        } catch (SQLException ex) {
            throw new DaoException("Lỗi khi tìm kiếm tài khoản: " + ex.getMessage());
        }

        return list;
    }
}
