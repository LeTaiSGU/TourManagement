package DAL;

import DTO.NhanVien;
import DTO.TaiKhoan;
import Exception.DaoException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Truy xuất dữ liệu bảng NHANVIEN.
 */
public class NhanVienDAL {

    private final Connection conn;

    public NhanVienDAL(Connection conn) {
        this.conn = conn;
    }

    // =========================================================
    // ÁNH XẠ ResultSet → DTO
    // =========================================================

    private NhanVien map(ResultSet rs) throws SQLException {
        return NhanVien.builder()
                .maNhanVien(rs.getString("maNhanVien"))
                .tenNhanVien(rs.getString("tenNhanVien"))
                .gioiTinh(rs.getString("gioiTinh"))
                .namSinh(rs.getInt("namSinh"))
                .diaChi(rs.getString("diaChi"))
                .soDienThoai(rs.getString("soDienThoai"))
                .maChucVu(rs.getString("maChucVu"))
                .trangThai(rs.getBoolean("trangThai"))
                .build();
    }

    // =========================================================
    // ĐỌC DỮ LIỆU
    // =========================================================

    /** Lấy toàn bộ danh sách nhân viên, sắp xếp theo mã tăng dần. */
    public List<NhanVien> getAll() throws SQLException {
        List<NhanVien> ds = new ArrayList<>();
        String sql = "SELECT maNhanVien, tenNhanVien, gioiTinh, namSinh, diaChi, "
                + "soDienThoai, maChucVu, trangThai FROM NHANVIEN ORDER BY maNhanVien";
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                ds.add(map(rs));
        }
        return ds;
    }

    /** Tìm kiếm theo từ khóa (tên hoặc mã nhân viên). */
    public List<NhanVien> timKiem(String tuKhoa) throws SQLException {
        List<NhanVien> ds = new ArrayList<>();
        String sql = "SELECT maNhanVien, tenNhanVien, gioiTinh, namSinh, diaChi, "
                + "soDienThoai, maChucVu, trangThai FROM NHANVIEN "
                + "WHERE tenNhanVien LIKE ? OR maNhanVien LIKE ? ORDER BY maNhanVien";
        String pat = "%" + (tuKhoa == null ? "" : tuKhoa.trim()) + "%";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, pat);
            ps.setString(2, pat);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    ds.add(map(rs));
            }
        }
        return ds;
    }

    /** Kiểm tra mã nhân viên đã tồn tại chưa. */
    public boolean tonTai(String maNhanVien) throws SQLException {
        String sql = "SELECT COUNT(1) FROM NHANVIEN WHERE maNhanVien = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNhanVien);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    /** Sinh mã nhân viên mới tự động (NV001, NV002, …). */
    public String sinhMaMoi() throws SQLException {
        String sql = "SELECT MAX(CAST(SUBSTRING(maNhanVien, 3, LEN(maNhanVien)) AS INT)) "
                + "FROM NHANVIEN WHERE maNhanVien LIKE 'NV%'";
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            int max = rs.next() ? rs.getInt(1) : 0;
            return String.format("NV%03d", max + 1);
        }
    }

    /** Kiểm tra nhân viên có đang được dùng trong HOÀĐƠN không. */
    public boolean dangDuocSuDung(String maNhanVien) throws SQLException {
        String sql = "SELECT COUNT(1) FROM HOADON WHERE maNhanVien = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNhanVien);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    // =========================================================
    // GHI DỮ LIỆU
    // =========================================================

    /** Thêm mới nhân viên. */
    public int them(NhanVien nv) throws SQLException {
        String sql = "INSERT INTO NHANVIEN (maNhanVien, tenNhanVien, gioiTinh, namSinh, "
                + "diaChi, soDienThoai, maChucVu, trangThai) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nv.getMaNhanVien());
            ps.setNString(2, nv.getTenNhanVien());
            ps.setNString(3, nv.getGioiTinh());
            ps.setInt(4, nv.getNamSinh());
            ps.setNString(5, nv.getDiaChi());
            ps.setString(6, nv.getSoDienThoai());
            ps.setString(7, nv.getMaChucVu());
            ps.setBoolean(8, nv.isTrangThai());
            return ps.executeUpdate();
        }
    }

    /** Cập nhật thông tin nhân viên. */
    public int capNhat(NhanVien nv) throws SQLException {
        String sql = "UPDATE NHANVIEN SET tenNhanVien=?, gioiTinh=?, namSinh=?, "
                + "diaChi=?, soDienThoai=?, maChucVu=?, trangThai=? WHERE maNhanVien=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, nv.getTenNhanVien());
            ps.setNString(2, nv.getGioiTinh());
            ps.setInt(3, nv.getNamSinh());
            ps.setNString(4, nv.getDiaChi());
            ps.setString(5, nv.getSoDienThoai());
            ps.setString(6, nv.getMaChucVu());
            ps.setBoolean(7, nv.isTrangThai());
            ps.setString(8, nv.getMaNhanVien());
            return ps.executeUpdate();
        }
    }

    /** Xóa cứng nhân viên (chỉ dùng khi không có FK). */
    public int xoa(String maNhanVien) throws SQLException {
        String sql = "DELETE FROM NHANVIEN WHERE maNhanVien = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNhanVien);
            return ps.executeUpdate();
        }
    }

    /** Xóa mềm: chuyển trangThai = 0. */
    public int doiTrangThai(String maNhanVien, boolean trangThai) throws SQLException {
        String sql = "UPDATE NHANVIEN SET trangThai=? WHERE maNhanVien=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, trangThai);
            ps.setString(2, maNhanVien);
            return ps.executeUpdate();
        }
    }

    /** Lấy nhân viên theo mã (dùng stored procedure). */
    public NhanVien getNhanVienByMa(String ma) throws SQLException {
        try {
            CallableStatement call = conn.prepareCall("{call getNhanVienByMa(?)}");
            call.setString(1, ma);
            ResultSet rs = call.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
        } catch (SQLException ex) {
            System.out.println("Lỗi getNhanVienByMa: " + ex.getMessage());
        }
        return null;
    }
}


/**
 * Truy xuất dữ liệu bảng NHANVIEN.
 *
 * Cấu trúc bảng:
 * maNhanVien VARCHAR(5) PK
 * tenNhanVien NVARCHAR(100) NOT NULL
 * gioiTinh NVARCHAR(10)
 * namSinh INT
 * diaChi NVARCHAR(255)
 * soDienThoai VARCHAR(10)
 * maChucVu VARCHAR(5) FK → CHUCVU
 * trangThai BIT DEFAULT 1
 */
public class NhanVienDAL {

    private final Connection conn;

    public NhanVienDAL(Connection conn) {
        this.conn = conn;
    }

    // =========================================================
    // ÁNH XẠ ResultSet → DTO
    // =========================================================

    private NhanVien map(ResultSet rs) throws SQLException {
        return NhanVien.builder()
                .maNhanVien(rs.getString("maNhanVien"))
                .tenNhanVien(rs.getString("tenNhanVien"))
                .gioiTinh(rs.getString("gioiTinh"))
                .namSinh(rs.getInt("namSinh"))
                .diaChi(rs.getString("diaChi"))
                .soDienThoai(rs.getString("soDienThoai"))
                .maChucVu(rs.getString("maChucVu"))
                .trangThai(rs.getBoolean("trangThai"))
                .build();
    }

    // =========================================================
    // ĐỌC DỮ LIỆU
    // =========================================================

    /** Lấy toàn bộ danh sách nhân viên, sắp xếp theo mã tăng dần. */
    public List<NhanVien> getAll() throws SQLException {
        List<NhanVien> ds = new ArrayList<>();
        String sql = "SELECT maNhanVien, tenNhanVien, gioiTinh, namSinh, diaChi, "
                + "soDienThoai, maChucVu, trangThai FROM NHANVIEN ORDER BY maNhanVien";
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                ds.add(map(rs));
        }
        return ds;
    }

    /** Tìm kiếm theo từ khóa (tên hoặc mã nhân viên). */
    public List<NhanVien> timKiem(String tuKhoa) throws SQLException {
        List<NhanVien> ds = new ArrayList<>();
        String sql = "SELECT maNhanVien, tenNhanVien, gioiTinh, namSinh, diaChi, "
                + "soDienThoai, maChucVu, trangThai FROM NHANVIEN "
                + "WHERE tenNhanVien LIKE ? OR maNhanVien LIKE ? ORDER BY maNhanVien";
        String pat = "%" + (tuKhoa == null ? "" : tuKhoa.trim()) + "%";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, pat);
            ps.setString(2, pat);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    ds.add(map(rs));
            }
        }
        return ds;
    }

    /** Kiểm tra mã nhân viên đã tồn tại chưa. */
    public boolean tonTai(String maNhanVien) throws SQLException {
        String sql = "SELECT COUNT(1) FROM NHANVIEN WHERE maNhanVien = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNhanVien);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    /**
     * Sinh mã nhân viên mới tự động (NV001, NV002, …).
     * Lấy số lớn nhất hiện tại rồi + 1.
     */
    public String sinhMaMoi() throws SQLException {
        String sql = "SELECT MAX(CAST(SUBSTRING(maNhanVien, 3, LEN(maNhanVien)) AS INT)) "
                + "FROM NHANVIEN WHERE maNhanVien LIKE 'NV%'";
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            int max = rs.next() ? rs.getInt(1) : 0;
            return String.format("NV%03d", max + 1);
        }
    }

    /** Kiểm tra nhân viên có đang được dùng trong HOÀĐƠN không. */
    public boolean dangDuocSuDung(String maNhanVien) throws SQLException {
        String sql = "SELECT COUNT(1) FROM HOADON WHERE maNhanVien = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNhanVien);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    // =========================================================
    // GHI DỮ LIỆU
    // =========================================================

    /** Thêm mới nhân viên. */
    public int them(NhanVien nv) throws SQLException {
        String sql = "INSERT INTO NHANVIEN (maNhanVien, tenNhanVien, gioiTinh, namSinh, "
                + "diaChi, soDienThoai, maChucVu, trangThai) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nv.getMaNhanVien());
            ps.setNString(2, nv.getTenNhanVien());
            ps.setNString(3, nv.getGioiTinh());
            ps.setInt(4, nv.getNamSinh());
            ps.setNString(5, nv.getDiaChi());
            ps.setString(6, nv.getSoDienThoai());
            ps.setString(7, nv.getMaChucVu());
            ps.setBoolean(8, nv.isTrangThai());
            return ps.executeUpdate();
        }
    }

    /** Cập nhật thông tin nhân viên. */
    public int capNhat(NhanVien nv) throws SQLException {
        String sql = "UPDATE NHANVIEN SET tenNhanVien=?, gioiTinh=?, namSinh=?, "
                + "diaChi=?, soDienThoai=?, maChucVu=?, trangThai=? WHERE maNhanVien=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, nv.getTenNhanVien());
            ps.setNString(2, nv.getGioiTinh());
            ps.setInt(3, nv.getNamSinh());
            ps.setNString(4, nv.getDiaChi());
            ps.setString(5, nv.getSoDienThoai());
            ps.setString(6, nv.getMaChucVu());
            ps.setBoolean(7, nv.isTrangThai());
            ps.setString(8, nv.getMaNhanVien());
            return ps.executeUpdate();
        }
    }

    /** Xóa cứng nhân viên (chỉ dùng khi không có FK). */
    public int xoa(String maNhanVien) throws SQLException {
        String sql = "DELETE FROM NHANVIEN WHERE maNhanVien = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNhanVien);
            return ps.executeUpdate();
        }
    }

    /** Xóa mềm: chuyển trangThai = 0. */
    public int doiTrangThai(String maNhanVien, boolean trangThai) throws SQLException {
        String sql = "UPDATE NHANVIEN SET trangThai=? WHERE maNhanVien=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, trangThai);
            ps.setString(2, maNhanVien);
            return ps.executeUpdate();
        }
=======
import DTO.TaiKhoan;
import Exception.DaoException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NhanVienDAL {
    private ConnectionDAL con = new ConnectionDAL();

    public NhanVien getNhanVienByMa(String ma) throws DaoException {
        NhanVien nv = new NhanVien();
        try {
            Connection conn = con.getConnection();
            CallableStatement call = conn.prepareCall("{call getNhanVienByMa(?)}");
            call.setString(1, ma);
            ResultSet rs = call.executeQuery();
            if (rs.next()) {
                nv = NhanVien.builder()
                        .maNhanVien(rs.getString("maNhanVien"))
                        .tenNhanVien(rs.getString("tenNhanVien"))
                        .gioiTinh(rs.getString("gioiTinh"))
                        .namSinh(rs.getInt("namSinh"))
                        .diaChi(rs.getString("diaChi"))
                        .soDienThoai(rs.getString("soDienThoai"))
                        .maChucVu(rs.getString("maChucVu"))
                        .trangThai(rs.getBoolean("trangThai"))
                        .build();
            } else {
                throw new DaoException("Không tìm thấy nhân viên: " + ma);
            }
        } catch (SQLException ex) {
            System.out.println("Lỗi" + ex.getMessage());
        }
        return nv;
>>>>>>> 379838c5d2def71baad191839bfd9123657069d3
    }
}
