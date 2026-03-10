package DAL;

import DTO.Tour;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TourDAL {

    private final Connection conn;

    public TourDAL(Connection conn) {
        this.conn = conn;
    }

    /** Lấy tất cả tour đang hoạt động, sắp xếp theo tên. */
    public List<Tour> getAll() throws SQLException {
        List<Tour> ds = new ArrayList<>();
        String sql = "SELECT maTour, tenTour FROM TOUR WHERE trangThai = 1 ORDER BY tenTour";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                ds.add(Tour.builder()
                        .maTour(rs.getString("maTour"))
                        .tenTour(rs.getString("tenTour"))
                        .build());
        }
        return ds;
    }
}
