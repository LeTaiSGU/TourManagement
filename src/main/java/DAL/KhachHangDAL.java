package DAL;

import DTO.KhachHang;
import Exception.DaoException;
import java.sql.*;
import java.util.ArrayList;

public class KhachHangDAL {

    // tai
    private ConnectionDAL conn = new ConnectionDAL();

    public ArrayList<KhachHang> getAllKhachHang() throws DaoException {
        ArrayList<KhachHang> dskh = new ArrayList<>();
        // Truy vấn tất cả khách hàng
        String sql = "SELECT * FROM KHACHHANG";

        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                KhachHang kh = new KhachHang();

                // Gán dữ liệu từ ResultSet vào đối tượng KhachHang
                kh.setMaKhachHang(rs.getString("maKhachHang"));
                kh.setMaLoaiKH(rs.getString("maLoaiKH"));
                kh.setTenKhachHang(rs.getString("tenKhachHang"));
                kh.setGioiTinh(rs.getString("gioiTinh"));
                kh.setNamSinh(rs.getInt("namSinh"));
                kh.setDiaChi(rs.getString("diaChi"));
                kh.setSoDienThoai(rs.getString("soDienThoai"));
                kh.setEmail(rs.getString("email"));
                kh.setTrangThai(rs.getBoolean("trangThai"));

                dskh.add(kh);
            }
        } catch (SQLException e) {
            // Log lỗi chi tiết ra console để dễ debug nếu cần
            e.printStackTrace();
            throw new DaoException("Lỗi truy vấn danh sách khách hàng");
        }
        return dskh;
    }

    // tai

}
