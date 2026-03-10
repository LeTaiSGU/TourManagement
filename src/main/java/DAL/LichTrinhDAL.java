package DAL;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DTO.LichTrinh;
import Exception.DaoException;

public class LichTrinhDAL {

    private final Connection conn;

    public LichTrinhDAL(Connection conn) {
        this.conn = conn;
    }

    // =========================================================
    // ĐỌC DỮ LIỆU
    // =========================================================

    /**
     * Lấy danh sách lịch trình, JOIN với TOUR/DIADIEM/PHUONGTIEN để lấy tên hiển
     * thị.
     * - tuKhoa rỗng/null → lấy toàn bộ.
     */
    public List<LichTrinh> getDanhSach(String tuKhoa) throws SQLException {
        List<LichTrinh> ds = new ArrayList<>();
        String pattern = "%" + (tuKhoa == null ? "" : tuKhoa.trim()) + "%";
        String sql = """
                SELECT lt.maLichTrinh, lt.maTour, t.tenTour,
                       lt.maDiaDiem, d.tenDiaDiem,
                       lt.maPT, p.tenPT,
                       lt.ngayThu, lt.noiDung, lt.trangThai
                FROM LICHTRINH lt
                LEFT JOIN TOUR t       ON lt.maTour    = t.maTour
                LEFT JOIN DIADIEM d    ON lt.maDiaDiem = d.maDiaDiem
                LEFT JOIN PHUONGTIEN p ON lt.maPT      = p.maPT
                WHERE t.tenTour    LIKE ?
                   OR d.tenDiaDiem LIKE ?
                   OR lt.maLichTrinh LIKE ?
                ORDER BY lt.maLichTrinh
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, pattern);
            ps.setNString(2, pattern);
            ps.setString(3, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LichTrinh lt = LichTrinh.builder()
                            .maLichTrinh(rs.getString("maLichTrinh"))
                            .maTour(rs.getString("maTour"))
                            .maDiaDiem(rs.getString("maDiaDiem"))
                            .maPT(rs.getString("maPT"))
                            .ngayThu(rs.getInt("ngayThu"))
                            .noiDung(rs.getString("noiDung"))
                            .trangThai(rs.getBoolean("trangThai"))
                            .tenTour(rs.getString("tenTour"))
                            .tenDiaDiem(rs.getString("tenDiaDiem"))
                            .tenPT(rs.getString("tenPT"))
                            .build();
                    ds.add(lt);
                }
            }
        }
        return ds;
    }

    /**
     * Lấy một lịch trình theo mã (để load form đầy đủ: noiDung, trangThai...).
     */
    public LichTrinh getById(String maLichTrinh) throws SQLException {
        String sql = """
                SELECT lt.maLichTrinh, lt.maTour, t.tenTour,
                       lt.maDiaDiem, d.tenDiaDiem,
                       lt.maPT, p.tenPT,
                       lt.ngayThu, lt.noiDung, lt.trangThai
                FROM LICHTRINH lt
                LEFT JOIN TOUR t       ON lt.maTour    = t.maTour
                LEFT JOIN DIADIEM d    ON lt.maDiaDiem = d.maDiaDiem
                LEFT JOIN PHUONGTIEN p ON lt.maPT      = p.maPT
                WHERE lt.maLichTrinh = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLichTrinh);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return LichTrinh.builder()
                            .maLichTrinh(rs.getString("maLichTrinh"))
                            .maTour(rs.getString("maTour"))
                            .maDiaDiem(rs.getString("maDiaDiem"))
                            .maPT(rs.getString("maPT"))
                            .ngayThu(rs.getInt("ngayThu"))
                            .noiDung(rs.getString("noiDung"))
                            .trangThai(rs.getBoolean("trangThai"))
                            .tenTour(rs.getString("tenTour"))
                            .tenDiaDiem(rs.getString("tenDiaDiem"))
                            .tenPT(rs.getString("tenPT"))
                            .build();
                }
            }
        }
        return null;
    }

    /** Kiểm tra mã lịch trình đã tồn tại chưa. */
    public boolean tonTai(String maLichTrinh) throws SQLException {
        String sql = "SELECT COUNT(1) FROM LICHTRINH WHERE maLichTrinh = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLichTrinh);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    /**
     * Sinh mã lịch trình mới tự động: LT001, LT002, ...
     */
    public String sinhMaMoi() throws SQLException {
        String sql = "SELECT MAX(maLichTrinh) FROM LICHTRINH";
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getString(1) != null) {
                int stt = Integer.parseInt(rs.getString(1).substring(2)) + 1;
                return String.format("LT%03d", stt);
            }
        }
        return "LT001";
    }

    // =========================================================
    // GHI DỮ LIỆU
    // =========================================================

    /** Thêm mới một lịch trình. */
    public int them(LichTrinh lt) throws SQLException {
        String sql = """
                INSERT INTO LICHTRINH (maLichTrinh, maTour, maDiaDiem, maPT, ngayThu, noiDung, trangThai)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lt.getMaLichTrinh());
            ps.setString(2, lt.getMaTour());
            ps.setString(3, lt.getMaDiaDiem());
            ps.setString(4, lt.getMaPT());
            ps.setInt(5, lt.getNgayThu());
            ps.setNString(6, lt.getNoiDung());
            ps.setBoolean(7, lt.getTrangThai() != null ? lt.getTrangThai() : true);
            return ps.executeUpdate();
        }
    }

    /** Cập nhật lịch trình theo maLichTrinh. */
    public int capNhat(LichTrinh lt) throws SQLException {
        String sql = """
                UPDATE LICHTRINH
                SET maTour = ?, maDiaDiem = ?, maPT = ?, ngayThu = ?, noiDung = ?, trangThai = ?
                WHERE maLichTrinh = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lt.getMaTour());
            ps.setString(2, lt.getMaDiaDiem());
            ps.setString(3, lt.getMaPT());
            ps.setInt(4, lt.getNgayThu());
            ps.setNString(5, lt.getNoiDung());
            ps.setBoolean(6, lt.getTrangThai() != null ? lt.getTrangThai() : true);
            ps.setString(7, lt.getMaLichTrinh());
            return ps.executeUpdate();
        }
    }

    /** Xóa lịch trình theo mã. */
    public int xoa(String maLichTrinh) throws SQLException {
        String sql = "DELETE FROM LICHTRINH WHERE maLichTrinh = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLichTrinh);
            return ps.executeUpdate();
        }
    }

    public Boolean checkMaLT(String maLT) throws DaoException {
        String sql = "{call checkMaLT(?)}";
        try (
                CallableStatement call = conn.prepareCall(sql);) {
            call.setString(1, maLT);
            ResultSet rs = call.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1);
            }
            return false;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("Lỗi: " + ex.getMessage());
        }
    }

    public String getLastMaLichTrinh() throws DaoException {
        String sql = "{call getLastMaLT}";
        try (
                CallableStatement call = conn.prepareCall(sql);) {
            ResultSet rs = call.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
            return null;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("Lỗi: " + ex.getMessage());
        }
    }

    public List<LichTrinh> getAllLichTrinh() throws DaoException {
        List<LichTrinh> dslt = new ArrayList();
        String sql = "Select * from LICHTRINH where trangThai = 1";
        try (
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LichTrinh lt = LichTrinh.builder()
                        .maLichTrinh(rs.getString("maLichTrinh"))
                        .maTour(rs.getString("maTour"))
                        .maDiaDiem(rs.getString("maDiaDiem"))
                        .maPT(rs.getString("maPT"))
                        .ngayThu(rs.getInt("ngayThu"))
                        .noiDung(rs.getString("noiDung"))
                        .trangThai(rs.getBoolean("trangThai"))
                        .build();
                dslt.add(lt);
            }
            return dslt;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("Lỗi: " + ex.getMessage());
        }
    }

    public List<LichTrinh> getLichTrinhByMaTour(String maTour) throws DaoException {
        List<LichTrinh> dslt = new ArrayList();
        String sql = "Select * from LICHTRINH where maTour = ? and trangThai = 1";
        try (
                PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setString(1, maTour);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LichTrinh lt = LichTrinh.builder()
                        .maLichTrinh(rs.getString("maLichTrinh"))
                        .maTour(rs.getString("maTour"))
                        .maDiaDiem(rs.getString("maDiaDiem"))
                        .maPT(rs.getString("maPT"))
                        .ngayThu(rs.getInt("ngayThu"))
                        .noiDung(rs.getString("noiDung"))
                        .trangThai(rs.getBoolean("trangThai"))
                        .build();
                dslt.add(lt);
            }
            return dslt;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("Lỗi: " + ex.getMessage());
        }
    }

    public Boolean insertLichTrinh(LichTrinh lichTrinh) throws DaoException {
        String sql = "{call insertLichTrinh(?,?,?,?,?,?,?)}";
        try (
                CallableStatement call = conn.prepareCall(sql);) {
            call.setString(1, lichTrinh.getMaLichTrinh());
            call.setString(2, lichTrinh.getMaTour());
            call.setString(3, lichTrinh.getMaDiaDiem());
            call.setString(4, lichTrinh.getMaPT());
            call.setInt(5, lichTrinh.getNgayThu());
            call.setString(6, lichTrinh.getNoiDung());
            call.setBoolean(7, true);
            int rows = call.executeUpdate();
            return rows > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("Lỗi: " + ex.getMessage());
        }
    }

    public Boolean editLichTrinh(LichTrinh lichTrinh) throws DaoException {
        String sql = "{call editLichTrinh(?,?,?,?,?,?,?)}";
        try (
                CallableStatement call = conn.prepareCall(sql);) {
            call.setString(1, lichTrinh.getMaLichTrinh());
            call.setString(2, lichTrinh.getMaTour());
            call.setString(3, lichTrinh.getMaDiaDiem());
            call.setString(4, lichTrinh.getMaPT());
            call.setInt(5, lichTrinh.getNgayThu());
            call.setString(6, lichTrinh.getNoiDung());
            call.setBoolean(7, lichTrinh.getTrangThai());
            int rows = call.executeUpdate();
            return rows > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("Lỗi: " + ex.getMessage());
        }
    }

    public Boolean dropLicTrinh(String maTour) throws DaoException {
        String sql = "{call dropLichtrinhTour(?)}";
        try (
                CallableStatement call = conn.prepareCall(sql);) {
            call.setString(1, maTour);
            int rows = call.executeUpdate();
            return rows > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("Lỗi: " + ex.getMessage());
        }
    }
}
