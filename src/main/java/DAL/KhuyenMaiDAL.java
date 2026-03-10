package DAL;

import DTO.KhuyenMai;
import DTO.KhuyenMaiDTO;
import Exception.DaoException;
import java.sql.*;
import java.util.ArrayList;

public class KhuyenMaiDAL {
    private ConnectionDAL conn = new ConnectionDAL();

    public ArrayList<KhuyenMaiDTO> getAllKhuyenMai() throws DaoException {
        ArrayList<KhuyenMaiDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai WHERE trangThaiKM = 1";

        try (Connection con = conn.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                KhuyenMaiDTO km = KhuyenMaiDTO.builder()
                        .maKhuyenMai(rs.getString("maKhuyenMai"))
                        .tenKhuyenMai(rs.getString("tenKhuyenMai"))
                        .phuongThucKM(rs.getString("phuongThucKM"))
                        .moTa(rs.getString("moTa"))
                        .trangThaiKM(rs.getBoolean("trangThaiKM"))
                        .build();
                list.add(km);
            }
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi truy vấn khuyến mãi.", e);
        }
        return list;
    }

    public ArrayList<KhuyenMaiDTO> searchKhuyenMai(String keyword) throws DaoException {
        ArrayList<KhuyenMaiDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai WHERE trangThaiKM = 1 AND (tenKhuyenMai LIKE ? OR maKhuyenMai LIKE ?)";
        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                KhuyenMaiDTO km = KhuyenMaiDTO.builder()
                        .maKhuyenMai(rs.getString("maKhuyenMai"))
                        .tenKhuyenMai(rs.getString("tenKhuyenMai"))
                        .phuongThucKM(rs.getString("phuongThucKM"))
                        .moTa(rs.getString("moTa"))
                        .trangThaiKM(rs.getBoolean("trangThaiKM"))
                        .build();
                list.add(km);
            }
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi tìm kiếm khuyến mãi.", e);
        }
        return list;
    }

    public String sinhMaMoi() throws DaoException {
        String sql = "SELECT TOP 1 maKhuyenMai FROM KhuyenMai ORDER BY maKhuyenMai DESC";
        try (Connection con = conn.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                String ma = rs.getString(1);
                int so = Integer.parseInt(ma.replaceAll("[^0-9]", "")) + 1;
                return String.format("KM%03d", so);
            }
            return "KM001";
        } catch (SQLException e) {
            throw new DaoException("Lỗi sinh mã khuyến mãi.", e);
        }
    }

    public void addKhuyenMai(KhuyenMaiDTO km) throws DaoException {
        String sql = "INSERT INTO KhuyenMai(maKhuyenMai, tenKhuyenMai, phuongThucKM, moTa, trangThaiKM) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, km.getMaKhuyenMai());
            ps.setString(2, km.getTenKhuyenMai());
            ps.setString(3, km.getPhuongThucKM());
            ps.setString(4, km.getMoTa());
            ps.setBoolean(5, km.getTrangThaiKM() != null ? km.getTrangThaiKM() : true);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi thêm khuyến mãi.", e);
        }
    }

    public void updateKhuyenMai(KhuyenMaiDTO km) throws DaoException {
        String sql = "UPDATE KhuyenMai SET tenKhuyenMai = ?, phuongThucKM = ?, moTa = ?, trangThaiKM = ? WHERE maKhuyenMai = ?";

        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, km.getTenKhuyenMai());
            ps.setString(2, km.getPhuongThucKM());
            ps.setString(3, km.getMoTa());
            ps.setBoolean(4, km.getTrangThaiKM() != null ? km.getTrangThaiKM() : true);
            ps.setString(5, km.getMaKhuyenMai());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi cập nhật khuyến mãi.", e);
        }
    }

    public void deleteKhuyenMai(String maKhuyenMai) throws DaoException {
        String sql = "UPDATE KhuyenMai SET trangThaiKM = 0 WHERE maKhuyenMai = ?";

        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKhuyenMai);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi xóa khuyến mãi.", e);
        }
    }

    public ArrayList<KhuyenMai> getGiaTriKhuyenMai() throws DaoException {
        ArrayList<KhuyenMai> dskm = new ArrayList<>();
        String sql = "SELECT maKhuyenMai, tenKhuyenMai, phuongThucKM FROM KHUYENMAI";

        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                KhuyenMai km = new KhuyenMai();
                km.setMaKhuyenMai(rs.getString("maKhuyenMai"));
                km.setTenKhuyenMai(rs.getString("tenKhuyenMai"));
                km.setPhuongThucKM(rs.getString("phuongThucKM"));
                try {
                    String val = rs.getString("phuongThucKM");
                    km.setGiaTriKM(val != null ? Double.parseDouble(val) : 0);
                } catch (NumberFormatException ignored) {
                    km.setGiaTriKM(0);
                }
                dskm.add(km);
            }
            return dskm;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Lỗi khi lấy giá trị khuyến mãi!");
        }
    }

    public double getGiaTriKMByMaKM(String makm) throws DaoException {
        String sql = "SELECT phuongThucKM FROM KHUYENMAI WHERE maKhuyenMai = ?";

        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, makm);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String val = rs.getString("phuongThucKM");
                try {
                    return val != null ? Double.parseDouble(val) : 0;
                } catch (NumberFormatException ignored) {
                    return 0;
                }
            }
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Lỗi khi lấy giá trị khuyến mãi theo mã khuyến mãi!");
        }
    }
}
