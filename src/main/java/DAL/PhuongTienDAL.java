package DAL;

import DTO.PhuongTien;
import Exception.DaoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhuongTienDAL {
    private final ConnectionDAL connDal = new ConnectionDAL();
    private final Connection sharedConnection;

    public PhuongTienDAL() {
        this.sharedConnection = null;
    }

    public PhuongTienDAL(Connection connection) {
        this.sharedConnection = connection;
    }

    private interface SqlWork<T> {
        T run(Connection con) throws SQLException;
    }

    private <T> T withConnection(SqlWork<T> work) throws SQLException {
        if (sharedConnection != null) {
            return work.run(sharedConnection);
        }
        Connection con = connDal.getConnection();
        if (con == null) {
            throw new SQLException("Khong the tao ket noi CSDL.");
        }
        try (Connection autoCloseCon = con) {
            return work.run(autoCloseCon);
        }
    }

    private PhuongTien map(ResultSet rs) throws SQLException {
        return PhuongTien.builder()
                .maPT(rs.getString("maPT"))
                .tenPT(rs.getString("tenPT"))
                .moTa(rs.getString("moTa"))
                .trangThai(rs.getBoolean("trangThai"))
                .build();
    }

    public List<PhuongTien> getAllPhuongTien() throws DaoException {
        String sql = "SELECT maPT, tenPT, moTa, trangThai FROM PHUONGTIEN WHERE trangThai = 1";
        try {
            return withConnection(con -> {
                List<PhuongTien> list = new ArrayList<>();
                try (PreparedStatement ps = con.prepareStatement(sql);
                        ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(map(rs));
                    }
                }
                return list;
            });
        } catch (SQLException e) {
            throw new DaoException("Loi truy van phuong tien: " + e.getMessage());
        }
    }

    public String getTenPhuongTienByMa(String maPhuongTien) throws DaoException {
        String sql = "SELECT tenPT FROM PHUONGTIEN WHERE maPT = ?";
        try {
            return withConnection(con -> {
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, maPhuongTien);
                    try (ResultSet rs = ps.executeQuery()) {
                        return rs.next() ? rs.getString("tenPT") : null;
                    }
                }
            });
        } catch (SQLException e) {
            throw new DaoException("Loi truy van phuong tien: " + e.getMessage());
        }
    }

    public String getMaPhuongTienByTen(String tenPhuongTien) throws DaoException {
        String sql = "SELECT maPT FROM PHUONGTIEN WHERE tenPT = ?";
        try {
            return withConnection(con -> {
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, tenPhuongTien);
                    try (ResultSet rs = ps.executeQuery()) {
                        return rs.next() ? rs.getString("maPT") : null;
                    }
                }
            });
        } catch (SQLException e) {
            throw new DaoException("Loi truy van phuong tien: " + e.getMessage());
        }
    }

    public List<PhuongTien> getAll() throws SQLException {
        String sql = "SELECT maPT, tenPT, moTa, trangThai FROM PHUONGTIEN WHERE trangThai = 1 ORDER BY maPT";
        return withConnection(con -> {
            List<PhuongTien> ds = new ArrayList<>();
            try (PreparedStatement ps = con.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(map(rs));
                }
            }
            return ds;
        });
    }

    public List<PhuongTien> timKiem(String tuKhoa) throws SQLException {
        String sql = "SELECT maPT, tenPT, moTa, trangThai FROM PHUONGTIEN WHERE trangThai = 1 AND (tenPT LIKE ? OR maPT LIKE ?) ORDER BY maPT";
        String pattern = "%" + (tuKhoa == null ? "" : tuKhoa.trim()) + "%";
        return withConnection(con -> {
            List<PhuongTien> ds = new ArrayList<>();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setNString(1, pattern);
                ps.setString(2, pattern);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        ds.add(map(rs));
                    }
                }
            }
            return ds;
        });
    }

    public PhuongTien getById(String maPT) throws SQLException {
        String sql = "SELECT maPT, tenPT, moTa, trangThai FROM PHUONGTIEN WHERE maPT = ?";
        return withConnection(con -> {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maPT);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? map(rs) : null;
                }
            }
        });
    }

    public boolean tonTai(String maPT) throws SQLException {
        String sql = "SELECT COUNT(1) FROM PHUONGTIEN WHERE maPT = ?";
        return withConnection(con -> {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maPT);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() && rs.getInt(1) > 0;
                }
            }
        });
    }

    public String sinhMaMoi() throws SQLException {
        String sql = "SELECT MAX(maPT) FROM PHUONGTIEN";
        return withConnection(con -> {
            try (PreparedStatement ps = con.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getString(1) != null) {
                    int soTiepTheo = Integer.parseInt(rs.getString(1).substring(2)) + 1;
                    return String.format("PT%03d", soTiepTheo);
                }
            }
            return "PT001";
        });
    }

    public int them(PhuongTien pt) throws SQLException {
        String sql = "INSERT INTO PHUONGTIEN (maPT, tenPT, moTa, trangThai) VALUES (?, ?, ?, ?)";
        return withConnection(con -> {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, pt.getMaPT());
                ps.setNString(2, pt.getTenPT());
                ps.setNString(3, pt.getMoTa());
                ps.setBoolean(4, pt.getTrangThai() != null ? pt.getTrangThai() : true);
                return ps.executeUpdate();
            }
        });
    }

    public int capNhat(PhuongTien pt) throws SQLException {
        String sql = "UPDATE PHUONGTIEN SET tenPT = ?, moTa = ?, trangThai = ? WHERE maPT = ?";
        return withConnection(con -> {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setNString(1, pt.getTenPT());
                ps.setNString(2, pt.getMoTa());
                ps.setBoolean(3, pt.getTrangThai() != null ? pt.getTrangThai() : true);
                ps.setString(4, pt.getMaPT());
                return ps.executeUpdate();
            }
        });
    }

    public int xoa(String maPT) throws SQLException {
        String sql = "DELETE FROM PHUONGTIEN WHERE maPT = ?";
        return withConnection(con -> {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maPT);
                return ps.executeUpdate();
            }
        });
    }

    public int doiTrangThai(String maPT, boolean trangThai) throws SQLException {
        String sql = "UPDATE PHUONGTIEN SET trangThai = ? WHERE maPT = ?";
        return withConnection(con -> {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setBoolean(1, trangThai);
                ps.setString(2, maPT);
                return ps.executeUpdate();
            }
        });
    }

    public boolean dangDuocSuDung(String maPT) throws SQLException {
        String sql = "SELECT COUNT(1) FROM LICHTRINH WHERE maPT = ?";
        return withConnection(con -> {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maPT);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() && rs.getInt(1) > 0;
                }
            }
        });
    }
}
