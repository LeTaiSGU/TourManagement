package DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import DTO.DiaDiem;
import Exception.DaoException;

public class DiaDiemDAL {
    private ConnectionDAL conn = new ConnectionDAL();

    public List<DiaDiem> getAllDiaDiem() throws DaoException {
        List<DiaDiem> list = new ArrayList<>();
        String sql = "SELECT * FROM DIADIEM WHERE trangThai = 1";
        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DiaDiem dd = new DiaDiem();
                dd.setMaDiaDiem(rs.getString("maDiaDiem"));
                dd.setTenDiaDiem(rs.getString("tenDiaDiem"));
                dd.setAnhDiaDiem(rs.getString("anhDiaDiem"));
                dd.setQuocGia(rs.getString("quocGia"));
                dd.setMoTa(rs.getString("moTa"));
                dd.setTrangThai(rs.getBoolean("trangThai"));
                list.add(dd);
            }
            return list;
        } catch (SQLException e) {
            throw new DaoException("Lỗi truy vấn địa điểm: " + e.getMessage());
        }
    }

    public String getTenDiaDiemByMa(String maDiaDiem) throws DaoException {
        String sql = "SELECT tenDiaDiem FROM DIADIEM WHERE maDiaDiem = ?";
        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setString(1, maDiaDiem);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("tenDiaDiem");
            }
            return null;
        } catch (SQLException e) {
            throw new DaoException("Lỗi truy vấn địa điểm: " + e.getMessage());
        }
    }

    public String getMaDiaDiemByTen(String tenDiaDiem) throws DaoException {
        String sql = "SELECT maDiaDiem FROM DIADIEM WHERE tenDiaDiem = ?";
        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setString(1, tenDiaDiem);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("maDiaDiem");
            }
            return null;
        } catch (SQLException e) {
            throw new DaoException("Lỗi truy vấn địa điểm: " + e.getMessage());
        }
    }

    public ArrayList<DiaDiem> getAll() throws DaoException {
        ArrayList<DiaDiem> list = new ArrayList<>();
        String sql = "SELECT * FROM DiaDiem WHERE trangThai = 1";

        try (Connection con = conn.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                DiaDiem dd = DiaDiem.builder()
                        .maDiaDiem(rs.getString("maDiaDiem"))
                        .tenDiaDiem(rs.getString("tenDiaDiem"))
                        .anhDiaDiem(rs.getString("anhDiaDiem"))
                        .quocGia(rs.getString("quocGia"))
                        .moTa(rs.getString("moTa"))
                        .trangThai(rs.getBoolean("trangThai"))
                        .build();
                list.add(dd);
            }
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi truy vấn danh sách địa điểm.", e);
        }
        return list;
    }

    public void addDiaDiem(DiaDiem diaDiem) throws DaoException {
        String sql = "INSERT INTO DiaDiem (maDiaDiem, tenDiaDiem, anhDiaDiem, quocGia, moTa) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, diaDiem.getMaDiaDiem());
            ps.setString(2, diaDiem.getTenDiaDiem());
            ps.setString(3, diaDiem.getAnhDiaDiem());
            ps.setString(4, diaDiem.getQuocGia());
            ps.setString(5, diaDiem.getMoTa());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi thêm địa điểm.", e);
        }
    }

    public void updateDiaDiem(DiaDiem diaDiem) throws DaoException {
        String sql = "UPDATE DiaDiem SET tenDiaDiem = ?, anhDiaDiem = ?, quocGia = ?, moTa = ? WHERE maDiaDiem = ?";

        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, diaDiem.getTenDiaDiem());
            ps.setString(2, diaDiem.getAnhDiaDiem());
            ps.setString(3, diaDiem.getQuocGia());
            ps.setString(4, diaDiem.getMoTa());
            ps.setString(5, diaDiem.getMaDiaDiem());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi sửa địa điểm.", e);
        }
    }

    public void deleteDiaDiem(String maDiaDiem) throws DaoException {
        String sql = "UPDATE DiaDiem SET trangThai = 0 WHERE maDiaDiem = ?";

        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maDiaDiem);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi xóa địa điểm.", e);
        }
    }

    public String sinhMaMoi() throws DaoException {
        String sql = "SELECT TOP 1 maDiaDiem FROM DiaDiem ORDER BY maDiaDiem DESC";
        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String lastMa = rs.getString("maDiaDiem");
                int num = Integer.parseInt(lastMa.replaceAll("[^0-9]", ""));
                return String.format("DD%03d", num + 1);
            }
            return "DD001";
        } catch (SQLException e) {
            throw new DaoException("Loi sinh ma dia diem.", e);
        }
    }

    public ArrayList<DiaDiem> searchDiaDiem(String keyword) throws DaoException {
        ArrayList<DiaDiem> list = new ArrayList<>();
        String sql = "SELECT * FROM DiaDiem WHERE trangThai = 1 AND (tenDiaDiem LIKE ? OR maDiaDiem LIKE ? OR quocGia LIKE ?)";
        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            String k = "%" + keyword + "%";
            ps.setString(1, k); ps.setString(2, k); ps.setString(3, k);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(DiaDiem.builder()
                        .maDiaDiem(rs.getString("maDiaDiem"))
                        .tenDiaDiem(rs.getString("tenDiaDiem"))
                        .anhDiaDiem(rs.getString("anhDiaDiem"))
                        .quocGia(rs.getString("quocGia"))
                        .moTa(rs.getString("moTa"))
                        .trangThai(rs.getBoolean("trangThai"))
                        .build());
            }
        } catch (SQLException e) {
            throw new DaoException("Loi tim kiem dia diem.", e);
        }
        return list;
    }

    public String getAnhDiaDiem(String maDiaDiem) throws DaoException {
        String sql = "SELECT anhDiaDiem FROM DiaDiem WHERE maDiaDiem = ?";
        String tenAnh = null;

        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maDiaDiem);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                tenAnh = rs.getString("anhDiaDiem");
            }

        } catch (SQLException e) {
            throw new DaoException("Lỗi khi lấy ảnh địa điểm.", e);
        }

        return tenAnh;
    }
}