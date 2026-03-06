package DAL;

import DTO.NhomQuyen;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAL cho bảng NHOMQUYEN.
 */
public class NhomQuyenDAL {

    private final Connection conn;

    public NhomQuyenDAL(Connection conn) {
        this.conn = conn;
    }

    private NhomQuyen map(ResultSet rs) throws SQLException {
        return NhomQuyen.builder()
                .maNhomQuyen(rs.getString("maNhomQuyen"))
                .tenNhomQuyen(rs.getString("tenNhomQuyen"))
                .moTa(rs.getString("moTa"))
                .build();
    }

    /** Lấy toàn bộ danh sách nhóm quyền. */
    public List<NhomQuyen> getAll() throws SQLException {
        List<NhomQuyen> ds = new ArrayList<>();
        String sql = "SELECT maNhomQuyen, tenNhomQuyen, moTa FROM NHOMQUYEN ORDER BY maNhomQuyen";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) ds.add(map(rs));
        }
        return ds;
    }

    /**
     * Tìm nhóm quyền phù hợp nhất với tên chức vụ.
     * Ưu tiên: khớp chính xác → tên NQ chứa từ khóa → từ khóa nằm trong tên NQ → đầu tiên trong DS.
     *
     * @param tenChucVu Tên chức vụ cần tra (VD: "Quản trị viên")
     * @return NhomQuyen phù hợp nhất, hoặc null nếu DB trống
     */
    public NhomQuyen timTheoTenChucVu(String tenChucVu) throws SQLException {
        List<NhomQuyen> daNQ = getAll();
        if (daNQ.isEmpty()) return null;

        String key = tenChucVu == null ? "" : tenChucVu.trim().toLowerCase();

        // 1. Khớp chính xác (ignore case)
        for (NhomQuyen nq : daNQ) {
            if (nq.getTenNhomQuyen().trim().equalsIgnoreCase(key)) return nq;
        }

        // 2. tenNhomQuyen chứa từ đầu tiên của tenChucVu
        String tuDau = key.contains(" ") ? key.split(" ")[0] : key;
        for (NhomQuyen nq : daNQ) {
            if (nq.getTenNhomQuyen().trim().toLowerCase().contains(tuDau)) return nq;
        }

        // 3. tenChucVu chứa tenNhomQuyen (VD: NQ có tên "admin" nằm trong "Quản trị admin")
        for (NhomQuyen nq : daNQ) {
            if (key.contains(nq.getTenNhomQuyen().trim().toLowerCase())) return nq;
        }

        // 4. Fallback — nhóm đầu tiên
        return daNQ.get(0);
    }
}
