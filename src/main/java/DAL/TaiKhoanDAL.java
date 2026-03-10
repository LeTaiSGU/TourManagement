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

/**
 * Truy xuất dữ liệu bảng TAIKHOAN.
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

    public List<TaiKhoan> getAllAccount() throws DaoException {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = "SELECT * FROM TAIKHOAN";
        try (PreparedStatement ps = conn.prepareStatement(sql);
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
        try (CallableStatement call = conn.prepareCall(sql)) {
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
        try (CallableStatement call = conn.prepareCall(sql)) {
            call.setString(1, tk.getMaNhanVien());
            call.setBoolean(2, tk.getTrangThai());
            int rows = call.executeUpdate();
            return rows > 0;
        } catch (SQLException ex) {
            throw new DaoException("Lỗi không chuyển trạng thái tài khoản được: " + ex.getMessage());
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
=======
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
>>>>>>> 379838c5d2def71baad191839bfd9123657069d3
    }
}
