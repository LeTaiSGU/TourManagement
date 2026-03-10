package DAL;

import DTO.ChucVu;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAL cho bảng CHUCVU.
 */
public class ChucVuDAL {

    private final Connection conn;

    public ChucVuDAL(Connection conn) {
        this.conn = conn;
    }

    private ChucVu map(ResultSet rs) throws SQLException {
        return ChucVu.builder()
                .maChucVu(rs.getString("maChucVu"))
                .tenChucVu(rs.getString("tenChucVu"))
                .moTa(rs.getString("moTa"))
                .build();
    }

    /** Lấy toàn bộ danh sách chức vụ. */
    public List<ChucVu> getAll() throws SQLException {
        List<ChucVu> ds = new ArrayList<>();
        String sql = "SELECT maChucVu, tenChucVu, moTa FROM CHUCVU ORDER BY maChucVu";
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                ds.add(map(rs));
        }
        return ds;
    }

    /** Tìm chức vụ theo mã. */
    public ChucVu getById(String maChucVu) throws SQLException {
        String sql = "SELECT maChucVu, tenChucVu, moTa FROM CHUCVU WHERE maChucVu = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maChucVu);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return map(rs);
            }
        }
        return null;
    }
}
