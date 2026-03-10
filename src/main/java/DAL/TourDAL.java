package DAL;

import DTO.Tour;
import Exception.DaoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TourDAL {
    private ConnectionDAL conn = new ConnectionDAL();

    public ArrayList<Tour> getAllTour() throws DaoException {
        ArrayList<Tour> dstour = new ArrayList<>();
        String sql = "Select * from TOUR";

        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Tour t = new Tour();
                t.setMaTour(rs.getString("maTour"));
                t.setMaLoaiTour(rs.getString("maLoaiTour"));
                t.setTenTour(rs.getString("tenTour"));
                t.setNoiKhoiHanh(rs.getString("noiKhoiHanh"));
                t.setTgKhoiHanh(rs.getDate("tgKhoiHanh").toLocalDate());
                t.setGiaTour(rs.getDouble("giaTour"));
                t.setSoLuongVe(rs.getInt("soLuongVe"));
                t.setSoLuongMin(rs.getInt("soLuongMin"));
                t.setTrangThai(rs.getBoolean("trangThai"));
                t.setKhoiHanh(rs.getBoolean("khoiHanh"));

                dstour.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Lỗi truy vấn tour");
        }
        return dstour;
    }

    public ArrayList<Tour> getAllTourWithSoChoCon() throws DaoException {
        ArrayList<Tour> dstour = new ArrayList<>();

        String sql = """
            SELECT
                t.*,
                t.soLuongVe - ISNULL(ct.daDat, 0) AS soChoCon
            FROM TOUR t
            LEFT JOIN (
                SELECT
                    ct.maTour,
                    SUM(ct.soLuongVe) AS daDat
                FROM CTHD ct
                WHERE (ct.trangThai = 'DA_DAT' OR ct.trangThai = 'HOAN_TAT')
                GROUP BY ct.maTour
            ) ct ON t.maTour = ct.maTour
            WHERE t.khoiHanh = 0 and t.trangThai = 1
            """;

        try (Connection con = conn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Tour t = new Tour();
                t.setMaTour(rs.getString("maTour"));
                t.setMaLoaiTour(rs.getString("maLoaiTour"));
                t.setTenTour(rs.getString("tenTour"));
                t.setNoiKhoiHanh(rs.getString("noiKhoiHanh"));
                t.setTgKhoiHanh(rs.getDate("tgKhoiHanh").toLocalDate());
                t.setGiaTour(rs.getDouble("giaTour"));
                t.setSoLuongVe(rs.getInt("soLuongVe"));
                t.setSoLuongMin(rs.getInt("soLuongMin"));
                t.setTrangThai(rs.getBoolean("trangThai"));
                t.setKhoiHanh(rs.getBoolean("khoiHanh"));
                t.setSoChoCon(rs.getInt("soChoCon"));

                dstour.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Lỗi truy vấn tour + số chỗ còn");
        }

        return dstour;
    }

}

