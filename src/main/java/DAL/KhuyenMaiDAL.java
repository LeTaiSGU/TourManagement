package DAL;

import DTO.KhuyenMai;
import Exception.DaoException;
import java.sql.*;
import java.util.ArrayList;

public class KhuyenMaiDAL {
    private ConnectionDAL conn = new ConnectionDAL();
    
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
        }
        catch (SQLException e) {
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
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Lỗi khi lấy giá trị khuyến mãi theo mã khuyến mãi!");
        }
    }
}
