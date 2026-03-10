package DAL;

import DTO.TaiKhoan;
import Exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Truy xuất dữ liệu bảng TAIKHOAN.
 *
 * Cấu trúc bảng:
 * maNhanVien VARCHAR(5) PK & FK → NHANVIEN
 * matKhau NVARCHAR(255) NOT NULL
 * maNhomQuyen VARCHAR(5) FK → NHOMQUYEN
 * trangThai BIT DEFAULT 1
 */
public class TaiKhoanDAL {

    private final Connection conn;

    public TaiKhoanDAL() {
        this.conn = new ConnectionDAL().getConnection();
    }

    public TaiKhoanDAL(Connection conn) {
        this.conn = conn;
    }

    /** Kiểm tra tài khoản đã tồn tại. */
    public boolean tonTai(String maNhanVien) throws SQLException {
        String sql = "SELECT COUNT(1) FROM TAIKHOAN WHERE maNhanVien = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNhanVien);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    /** Thêm mới tài khoản. */
    public int them(TaiKhoan tk) throws SQLException {
        String sql = "INSERT INTO TAIKHOAN (maNhanVien, matKhau, maNhomQuyen, trangThai) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tk.getMaNhanVien());
            ps.setNString(2, tk.getMatKhau());
            if (tk.getMaNhomQuyen() != null) {
                ps.setString(3, tk.getMaNhomQuyen());
            } else {
                ps.setNull(3, java.sql.Types.VARCHAR);
            }
            ps.setBoolean(4, Boolean.TRUE.equals(tk.getTrangThai()));
            return ps.executeUpdate();
        }
    }

    /** Xóa tài khoản theo mã nhân viên. */
    public int xoa(String maNhanVien) throws SQLException {
        String sql = "DELETE FROM TAIKHOAN WHERE maNhanVien = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNhanVien);
            return ps.executeUpdate();
        }
    }

    /** Cập nhật nhóm quyền cho tài khoản (khi chức vụ thay đổi). */
    public int capNhatNhomQuyen(String maNhanVien, String maNhomQuyen) throws SQLException {
        String sql = "UPDATE TAIKHOAN SET maNhomQuyen=? WHERE maNhanVien=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (maNhomQuyen != null) {
                ps.setString(1, maNhomQuyen);
            } else {
                ps.setNull(1, java.sql.Types.VARCHAR);
            }
            ps.setString(2, maNhanVien);
            return ps.executeUpdate();
        }
    }

    public List<TaiKhoan> getAllAccount() throws DaoException {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = "SELECT * FROM TAIKHOAN";
        try (PreparedStatement ps = conn.prepareStatement(sql);
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
            throw new DaoException("Lỗi khi lấy danh sách tài khoản: " + ex.getMessage());
        }
        return list;
    }

    public Boolean editTaiKhoan(TaiKhoan tk) throws DaoException {
        String sql = "{call editTaiKhoan(?,?,?)}";
        try (CallableStatement call = conn.prepareCall(sql)) {
            call.setString(1, tk.getMaNhanVien());
            call.setString(2, tk.getMatKhau());
            call.setString(3, tk.getMaNhomQuyen());
            return call.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new DaoException("Lỗi khi lưu tài khoản: " + ex.getMessage());
        }
    }

    public Boolean unblockAccount(TaiKhoan tk) throws DaoException {
        String sql = "{call setStatusAcount(?,?)}";
        try (CallableStatement call = conn.prepareCall(sql)) {
            call.setString(1, tk.getMaNhanVien());
            call.setBoolean(2, tk.getTrangThai());
            return call.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new DaoException("Lỗi khi đổi trạng thái tài khoản: " + ex.getMessage());
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
        try (PreparedStatement ps = conn.prepareStatement(sql.toString());
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
