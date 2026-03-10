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
        String sql = "SELECT * FROM KhuyenMai";

        try (Connection con = conn.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                KhuyenMaiDTO km = new KhuyenMaiDTO(
                        rs.getString("maKhuyenMai"),
                        rs.getString("tenKhuyenMai"),
                        rs.getString("phuongThucKM"),
                        rs.getString("moTa"));
                list.add(km);
            }
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi truy vấn khuyến mãi.", e);
        }
        return list;
    }

    public void addKhuyenMai(KhuyenMaiDTO km) throws DaoException {
        String sql = "INSERT INTO KhuyenMai(maKhuyenMai, tenKhuyenMai, phuongThucKM, moTa) VALUES (?, ?, ?, ?)";

        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, km.getMaKhuyenMai());
            ps.setString(2, km.getTenKhuyenMai());
            ps.setString(3, km.getPhuongThucKM());
            ps.setString(4, km.getMoTa());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi thêm khuyến mãi.", e);
        }
    }

    public void updateKhuyenMai(KhuyenMaiDTO km) throws DaoException {
        String sql = "UPDATE KhuyenMai SET tenKhuyenMai = ?, phuongThucKM = ?, moTa = ? WHERE maKhuyenMai = ?";

        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, km.getTenKhuyenMai());
            ps.setString(2, km.getPhuongThucKM());
            ps.setString(3, km.getMoTa());
            ps.setString(4, km.getMaKhuyenMai());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Lỗi khi cập nhật khuyến mãi.", e);
        }
    }

    public void deleteKhuyenMai(String maKhuyenMai) throws DaoException {
        String sql = "DELETE FROM KhuyenMai WHERE maKhuyenMai = ?";

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
        String sql = "SELECT maKhuyenMai, tenKhuyenMai, phuongThucKM "
                + "FROM KHUYENMAI";

        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                KhuyenMai km = new KhuyenMai();
                km.setMaKhuyenMai(rs.getString("maKhuyenMai"));
                km.setTenKhuyenMai(rs.getString("tenKhuyenMai"));
                km.setPhuongThucKM(rs.getDouble("phuongThucKM"));
                dskm.add(km);
            }
            return dskm;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Lỗi khi lấy giá trị khuyến mãi!");
        }
    }

    public double getGiaTriKMByMaKM(String makm) throws DaoException {
        String sql = "SELECT phuongThucKM "
                + "FROM KHUYENMAI "
                + "WHERE maKhuyenMai = ? ";

        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, makm);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getDouble("phuongThucKM");
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Lỗi khi lấy giá trị khuyến mãi theo mã khuyến mãi!");
        }
    }
}
