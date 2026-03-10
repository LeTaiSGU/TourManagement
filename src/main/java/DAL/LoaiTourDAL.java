package DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DTO.LoaiTour;
import Exception.DaoException;

public class LoaiTourDAL {
    private ConnectionDAL conn = new ConnectionDAL();

    public List<LoaiTour> getAllLoaiTour() throws DaoException {
        List<LoaiTour> list = new ArrayList();
        String sql = "SELECT * FROM LOAITOUR";
        try (Connection con = conn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LoaiTour loaiTour = new LoaiTour();
                loaiTour.setMaLoaiTour(rs.getString("maLoaiTour"));
                loaiTour.setTenLoai(rs.getString("tenLoai"));
                loaiTour.setMoTa(rs.getString("moTa"));
                list.add(loaiTour);
            }
        } catch (SQLException e) {
            throw new DaoException("Lỗi truy vấn loại tour: " + e.getMessage());
        }
        return list;
    }
}
