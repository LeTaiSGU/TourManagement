package DAL;

import DTO.TaiKhoan;

import java.sql.*;

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
            ps.setString(1, tk.getTenDangNhap());
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
}
