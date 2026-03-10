package DAL;

import DTO.Tour;
import Exception.DaoException;
import GUI.Dialog.TourSearchDialog.SearchCriteria;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TourDAL {
    private ConnectionDAL conn = new ConnectionDAL();

    public ArrayList<Tour> getAllTour() throws DaoException {
        ArrayList<Tour> dstour = new ArrayList<>();
        String sql = "Select * from TOUR order by maTour desc";

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

    public String getLastMa() throws DaoException {
        String sql = "{call getLatestTourId}";

        try (Connection con = conn.getConnection();
                CallableStatement call = con.prepareCall(sql);
                ResultSet rs = call.executeQuery()) {

            String ma = null;

            if (rs.next()) {
                ma = rs.getString(1);
            }

            return ma;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("Lỗi: " + ex.getMessage());
        }
    }

    public List<Tour> getTourByMaTourChuaKhoiHanh() throws DaoException {
        List<Tour> listTour = new ArrayList<>();
        String sql = "select * from TOUR where khoiHanh = 0 and trangThai = 1";
        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Tour t = Tour.builder()
                        .maTour(rs.getString("maTour"))
                        .maLoaiTour(rs.getString("maLoaiTour"))
                        .maHDV(rs.getString("maHDV"))
                        .tenTour(rs.getString("tenTour"))
                        .anhTour(rs.getString("anhTour"))
                        .noiKhoiHanh(rs.getString("noiKhoiHanh"))
                        .tgKhoiHanh(rs.getDate("tgKhoiHanh").toLocalDate())
                        .tgKetThuc(rs.getDate("tgKetThuc").toLocalDate())
                        .giaTour(rs.getDouble("giaTour"))
                        .soLuongVe(rs.getInt("soLuongVe"))
                        .soLuongMin(rs.getInt("soLuongMin"))
                        .trangThai(rs.getBoolean("trangThai"))
                        .khoiHanh(rs.getBoolean("khoiHanh"))
                        .build();
                listTour.add(t);
            }
            return listTour;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("Lỗi: " + ex.getMessage());
        }
    }

    public Tour getTourByMaTour(String maTour) throws DaoException {
        String sql = "{call getTourByMa(?)}";
        try (Connection con = conn.getConnection();
                CallableStatement call = con.prepareCall(sql);) {
            call.setString(1, maTour);
            ResultSet rs = call.executeQuery();
            if (rs.next()) {
                return Tour.builder()
                        .maTour(rs.getString("maTour"))
                        .maLoaiTour(rs.getString("maLoaiTour"))
                        .maHDV(rs.getString("maHDV"))
                        .tenTour(rs.getString("tenTour"))
                        .anhTour(rs.getString("anhTour"))
                        .noiKhoiHanh(rs.getString("noiKhoiHanh"))
                        .tgKhoiHanh(rs.getDate("tgKhoiHanh").toLocalDate())
                        .tgKetThuc(rs.getDate("tgKetThuc").toLocalDate())
                        .giaTour(rs.getDouble("giaTour"))
                        .soLuongVe(rs.getInt("soLuongVe"))
                        .soLuongMin(rs.getInt("soLuongMin"))
                        .trangThai(rs.getBoolean("trangThai"))
                        .khoiHanh(rs.getBoolean("khoiHanh"))
                        .build();
            } else {
                throw new DaoException("Không tìm thấy tour: " + maTour);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("Lỗi: " + ex.getMessage());
        }
    }

    public Boolean editTour(Tour tr) throws DaoException {
        String sql = "{call updateTour(?,?,?,?,?,?,?,?,?,?,?)}";
        try (Connection con = conn.getConnection();
                CallableStatement call = con.prepareCall(sql);) {
            call.setString(1, tr.getMaTour());
            call.setString(2, tr.getMaLoaiTour());
            call.setString(3, tr.getMaHDV());
            call.setString(4, tr.getTenTour());
            call.setString(5, tr.getAnhTour());
            call.setString(6, tr.getNoiKhoiHanh());
            call.setDate(7, Date.valueOf(tr.getTgKhoiHanh()));
            call.setDate(8, Date.valueOf(tr.getTgKetThuc()));
            call.setDouble(9, tr.getGiaTour());
            call.setInt(10, tr.getSoLuongVe());
            call.setInt(11, tr.getSoLuongMin());
            int rows = call.executeUpdate();
            return rows > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("Lỗi: " + ex.getMessage());
        }
    }

    public ArrayList<Tour> searchTour(SearchCriteria criteria) throws DaoException {
        ArrayList<Tour> dstour = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM TOUR WHERE 1=1");

        if (criteria.getMaTour() != null) {
            sql.append(" AND maTour = ?");
        }
        if (criteria.getTenTour() != null) {
            sql.append(" AND tenTour LIKE ?");
        }
        if (criteria.getMaLoaiTour() != null) {
            sql.append(" AND maLoaiTour = ?");
        }
        if (criteria.getTgKhoiHanh() != null) {
            sql.append(" AND tgKhoiHanh = ?");
        }
        if (criteria.getNoiKhoiHanh() != null) {
            sql.append(" AND noiKhoiHanh LIKE ?");
        }
        if (criteria.getGiaMin() != null) {
            sql.append(" AND giaTour >= ?");
        }
        if (criteria.getGiaMax() != null) {
            sql.append(" AND giaTour <= ?");
        }
        if (criteria.getDaKhoiHanh() != null) {
            sql.append(" AND khoiHanh = ?");
        }

        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql.toString())) {
            int idx = 1;
            if (criteria.getMaTour() != null) {
                ps.setString(idx++, criteria.getMaTour());
            }
            if (criteria.getTenTour() != null) {
                ps.setString(idx++, "%" + criteria.getTenTour() + "%");
            }
            if (criteria.getMaLoaiTour() != null) {
                ps.setString(idx++, criteria.getMaLoaiTour());
            }
            if (criteria.getTgKhoiHanh() != null) {
                ps.setDate(idx++, Date.valueOf(criteria.getTgKhoiHanh()));
            }
            if (criteria.getNoiKhoiHanh() != null) {
                ps.setString(idx++, "%" + criteria.getNoiKhoiHanh() + "%");
            }
            if (criteria.getGiaMin() != null) {
                ps.setDouble(idx++, criteria.getGiaMin());
            }
            if (criteria.getGiaMax() != null) {
                ps.setDouble(idx++, criteria.getGiaMax());
            }
            if (criteria.getDaKhoiHanh() != null) {
                ps.setBoolean(idx++, criteria.getDaKhoiHanh());
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Tour t = new Tour();
                    t.setMaTour(rs.getString("maTour"));
                    t.setMaLoaiTour(rs.getString("maLoaiTour"));
                    t.setMaHDV(rs.getString("maHDV"));
                    t.setTenTour(rs.getString("tenTour"));
                    t.setAnhTour(rs.getString("anhTour"));
                    t.setNoiKhoiHanh(rs.getString("noiKhoiHanh"));

                    Date tgKhoiHanh = rs.getDate("tgKhoiHanh");
                    Date tgKetThuc = rs.getDate("tgKetThuc");
                    if (tgKhoiHanh != null) {
                        t.setTgKhoiHanh(tgKhoiHanh.toLocalDate());
                    }
                    if (tgKetThuc != null) {
                        t.setTgKetThuc(tgKetThuc.toLocalDate());
                    }

                    t.setGiaTour(rs.getDouble("giaTour"));
                    t.setSoLuongVe(rs.getInt("soLuongVe"));
                    t.setSoLuongMin(rs.getInt("soLuongMin"));
                    t.setTrangThai(rs.getBoolean("trangThai"));
                    t.setKhoiHanh(rs.getBoolean("khoiHanh"));
                    dstour.add(t);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("Lỗi tìm kiếm tour: " + ex.getMessage());
        }
        return dstour;
    }

    public Boolean insertTour(Tour tr) throws DaoException {
        String sql = "{call insertTour(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try (Connection con = conn.getConnection();
                CallableStatement call = con.prepareCall(sql);) {
            call.setString(1, tr.getMaTour());
            call.setString(2, tr.getMaLoaiTour());
            call.setString(3, tr.getMaHDV());
            call.setString(4, tr.getTenTour());
            call.setString(5, tr.getAnhTour());
            call.setString(6, tr.getNoiKhoiHanh());
            call.setDate(7, Date.valueOf(tr.getTgKhoiHanh()));
            call.setDate(8, Date.valueOf(tr.getTgKetThuc()));
            call.setDouble(9, tr.getGiaTour());
            call.setInt(10, tr.getSoLuongVe());
            call.setInt(11, tr.getSoLuongMin());
            call.setBoolean(12, tr.getKhoiHanh());
            call.setBoolean(13, tr.getTrangThai());

            int rows = call.executeUpdate();
            return rows > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("Lỗi: " + ex.getMessage());
        }
    }

    public Boolean huyTourByAdmin(String maTour, String lyDoHuy) throws DaoException {
        String sql = "{call deleteTourByAdmin(?,?)}";
        try (Connection con = conn.getConnection();
                CallableStatement call = con.prepareCall(sql);) {
            String maTourValue = maTour == null ? null : maTour.trim();
            String lyDoHuyValue = lyDoHuy == null ? "" : lyDoHuy.trim();
            call.setString(1, maTourValue);
            call.setNString(2, lyDoHuyValue);
            int rows = call.executeUpdate();
            return rows > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("Lỗi: " + ex.getMessage());
        }
    }

    public double getGiaTour(String maTour) throws DaoException {
        String sql = "{call getGiaTour(?)}";
        try (Connection con = conn.getConnection();
                CallableStatement call = con.prepareCall(sql);) {
            call.setString(1, maTour);
            ResultSet rs = call.executeQuery();
            if (rs.next()) {
                return rs.getDouble("giaTour");
            }
            return 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("Lỗi: " + ex.getMessage());
        }
    }

    public int checkTourDuSoLuongKhoiHanh(String maTour) throws DaoException {
        String sql = "{call checkTourDuSoLuongKhoiHanh(?)}";
        try (Connection con = conn.getConnection();
                CallableStatement call = con.prepareCall(sql)) {

            call.setString(1, maTour);
            ResultSet rs = call.executeQuery();

            if (rs.next()) {
                // cột daCapNhat bạn đang SELECT ra là 0/1
                return rs.getInt("daCapNhat");
            }
            return 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("Lỗi gọi checkTourDuSoLuongKhoiHanh: " + ex.getMessage());
        }
    }

    public int checkKhoiHanhHomNay() throws DaoException {
        String sql = "{call checkKhoiHanhHomNay}";
        try (Connection con = conn.getConnection();
                CallableStatement call = con.prepareCall(sql);
                ResultSet rs = call.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("soTourDaKhoiHanh");
            }
            return 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("Lỗi gọi checkKhoiHanhHomNay: " + ex.getMessage());
        }
    }
}
