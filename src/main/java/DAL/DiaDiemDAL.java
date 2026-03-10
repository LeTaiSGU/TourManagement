package DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import DTO.DiaDiem;
import Exception.DaoException;

public class DiaDiemDAL {
    private ConnectionDAL conn = new ConnectionDAL();

    public List<DiaDiem> getAllDiaDiem() throws DaoException {
        List<DiaDiem> list = new ArrayList<>();
        String sql = "SELECT * FROM DIADIEM";
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
}

    private ConnectionDAL connectionDAL = new ConnectionDAL();

    public ArrayList<DiaDiem> getAll() throws DaoException {
        ArrayList<DiaDiem> list = new ArrayList<>();
        String sql = "SELECT * FROM DiaDiem";

        try (Connection conn = connectionDAL.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                DiaDiem dd = new DiaDiem(
                        rs.getString("maDiaDiem"),
                        rs.getString("tenDiaDiem"),
                        rs.getString("anhDiaDiem"),
                        rs.getString("quocGia"),
                        rs.getString("moTa"));
                list.add(dd);
            }
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi truy vấn danh sách địa điểm.", e);
        }
        return list;
    }

    public void addDiaDiem(DiaDiem diaDiem) throws DaoException {
        String sql = "INSERT INTO DiaDiem (maDiaDiem, tenDiaDiem, anhDiaDiem, quocGia, moTa) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connectionDAL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

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

        try (Connection conn = connectionDAL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

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
        String sql = "DELETE FROM DiaDiem WHERE maDiaDiem = ?";

        try (Connection conn = connectionDAL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDiaDiem);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi xóa địa điểm.", e);
        }
    }

    public String getAnhDiaDiem(String maDiaDiem) throws DaoException {
        String sql = "SELECT anhDiaDiem FROM DiaDiem WHERE maDiaDiem = ?";
        String tenAnh = null;

        try (Connection conn = connectionDAL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

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