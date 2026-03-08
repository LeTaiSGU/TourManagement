package DAL;

import DTO.DiaDiem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiaDiemDAL {

    private final Connection conn;

    public DiaDiemDAL(Connection conn) {
        this.conn = conn;
    }

    /** Lấy tất cả địa điểm đang hoạt động, sắp xếp theo tên. */
    public List<DiaDiem> getAll() throws SQLException {
        List<DiaDiem> ds = new ArrayList<>();
        String sql = "SELECT maDiaDiem, tenDiaDiem FROM DIADIEM WHERE trangThai = 1 ORDER BY tenDiaDiem";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                ds.add(DiaDiem.builder()
                        .maDiaDiem(rs.getString("maDiaDiem"))
                        .tenDiaDiem(rs.getString("tenDiaDiem"))
                        .build());
        }
        return ds;
    }
}
