package DAL;

import DTO.PhuongTien;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp truy xuất dữ liệu bảng PHUONGTIEN từ SQL Server.
 *
 * Cấu trúc bảng:
 * maPT VARCHAR(5) PRIMARY KEY
 * tenPT NVARCHAR(100) NOT NULL
 * moTa NVARCHAR(255)
 * trangThai BIT DEFAULT 1
 *
 * Tất cả phương thức đều dùng PreparedStatement để tránh SQL Injection.
 */
public class PhuongTienDAL {

    /** Kết nối CSDL, nhận từ constructor */
    private final Connection conn;

    public PhuongTienDAL(Connection conn) {
        this.conn = conn;
    }

    // =========================================================
    // ÁNH XẠ ResultSet → DTO
    // =========================================================

    /**
     * Ánh xạ một hàng kết quả SQL thành đối tượng PhuongTien.
     */
    private PhuongTien map(ResultSet rs) throws SQLException {
        return PhuongTien.builder()
                .maPT(rs.getString("maPT"))
                .tenPT(rs.getString("tenPT"))
                .moTa(rs.getString("moTa"))
                .trangThai(rs.getBoolean("trangThai"))
                .build();
    }

    // =========================================================
    // ĐỌC DỮ LIỆU
    // =========================================================

    /**
     * Lấy toàn bộ danh sách phương tiện (kể cả ngừng hoạt động).
     * Sắp xếp theo maPT tăng dần.
     */
    public List<PhuongTien> getAll() throws SQLException {
        List<PhuongTien> ds = new ArrayList<>();
        String sql = "SELECT maPT, tenPT, moTa, trangThai FROM PHUONGTIEN ORDER BY maPT";
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                ds.add(map(rs));
        }
        return ds;
    }

    /**
     * Tìm kiếm phương tiện theo từ khóa (khớp tên hoặc mã, không phân biệt
     * hoa/thường).
     *
     * @param tuKhoa Chuỗi tìm kiếm, có thể rỗng để lấy toàn bộ
     */
    public List<PhuongTien> timKiem(String tuKhoa) throws SQLException {
        List<PhuongTien> ds = new ArrayList<>();
        String sql = """
                SELECT maPT, tenPT, moTa, trangThai
                FROM PHUONGTIEN
                WHERE tenPT LIKE ? OR maPT LIKE ?
                ORDER BY maPT
                """;
        String pattern = "%" + (tuKhoa == null ? "" : tuKhoa.trim()) + "%";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, pattern);
            ps.setString(2, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    ds.add(map(rs));
            }
        }
        return ds;
    }

    /**
     * Tìm một phương tiện theo mã.
     *
     * @return Đối tượng PhuongTien nếu tìm thấy, null nếu không có
     */
    public PhuongTien getById(String maPT) throws SQLException {
        String sql = "SELECT maPT, tenPT, moTa, trangThai FROM PHUONGTIEN WHERE maPT = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPT);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return map(rs);
            }
        }
        return null;
    }

    /**
     * Kiểm tra mã phương tiện đã tồn tại chưa (dùng validate trước khi Thêm).
     */
    public boolean tonTai(String maPT) throws SQLException {
        String sql = "SELECT COUNT(1) FROM PHUONGTIEN WHERE maPT = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPT);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    /**
     * Sinh mã phương tiện tiếp theo theo định dạng PT001, PT002, ...
     * Tự động tăng dựa trên mã lớn nhất hiện có trong DB.
     */
    public String sinhMaMoi() throws SQLException {
        String sql = "SELECT MAX(maPT) FROM PHUONGTIEN";
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getString(1) != null) {
                int soTiepTheo = Integer.parseInt(rs.getString(1).substring(2)) + 1;
                return String.format("PT%03d", soTiepTheo);
            }
        }
        return "PT001";
    }

    // =========================================================
    // GHI DỮ LIỆU (CRUD)
    // =========================================================

    /**
     * Thêm một phương tiện mới vào CSDL (maPT phải chưa tồn tại).
     *
     * @return số dòng bị ảnh hưởng (1 = thành công)
     */
    public int them(PhuongTien pt) throws SQLException {
        String sql = "INSERT INTO PHUONGTIEN (maPT, tenPT, moTa, trangThai) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pt.getMaPT());
            ps.setNString(2, pt.getTenPT());
            ps.setNString(3, pt.getMoTa());
            ps.setBoolean(4, pt.getTrangThai() != null ? pt.getTrangThai() : true);
            return ps.executeUpdate();
        }
    }

    /**
     * Cập nhật thông tin phương tiện (dùng maPT làm khóa WHERE, không đổi maPT).
     *
     * @return số dòng bị ảnh hưởng (1 = thành công)
     */
    public int capNhat(PhuongTien pt) throws SQLException {
        String sql = "UPDATE PHUONGTIEN SET tenPT = ?, moTa = ?, trangThai = ? WHERE maPT = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, pt.getTenPT());
            ps.setNString(2, pt.getMoTa());
            ps.setBoolean(3, pt.getTrangThai() != null ? pt.getTrangThai() : true);
            ps.setString(4, pt.getMaPT());
            return ps.executeUpdate();
        }
    }

    /**
     * Xóa cứng một phương tiện.
     * Lưu ý: sẽ lỗi FK nếu đang được dùng trong LICHTRINH — nên gọi
     * dangDuocSuDung() để kiểm tra trước, hoặc dùng doiTrangThai() thay thế.
     *
     * @return số dòng bị ảnh hưởng (1 = thành công)
     */
    public int xoa(String maPT) throws SQLException {
        String sql = "DELETE FROM PHUONGTIEN WHERE maPT = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPT);
            return ps.executeUpdate();
        }
    }

    /**
     * Đổi trạng thái hoạt động / ngừng hoạt động (xóa mềm).
     *
     * @param trangThai true = hoạt động, false = ngừng hoạt động
     * @return số dòng bị ảnh hưởng
     */
    public int doiTrangThai(String maPT, boolean trangThai) throws SQLException {
        String sql = "UPDATE PHUONGTIEN SET trangThai = ? WHERE maPT = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, trangThai);
            ps.setString(2, maPT);
            return ps.executeUpdate();
        }
    }

    /**
     * Kiểm tra phương tiện có đang được tham chiếu trong LICHTRINH không.
     * Dùng để cảnh báo người dùng trước khi xóa cứng.
     *
     * @return true nếu đang được dùng trong ít nhất 1 lịch trình
     */
    public boolean dangDuocSuDung(String maPT) throws SQLException {
        String sql = "SELECT COUNT(1) FROM LICHTRINH WHERE maPT = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPT);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}
