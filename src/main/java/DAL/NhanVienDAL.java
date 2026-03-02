package DAL;

import DTO.NhanVien;
import DTO.TaiKhoan;
import Exception.DaoException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NhanVienDAL {
    private ConnectionDAL con = new ConnectionDAL();

    public NhanVien getNhanVienByMa(String ma) throws DaoException {
        NhanVien nv = new NhanVien();
        try {
            Connection conn = con.getConnection();
            CallableStatement call = conn.prepareCall("{call getNhanVienByMa(?)}");
            call.setString(1, ma);
            ResultSet rs = call.executeQuery();
            if (rs.next()) {
                nv = NhanVien.builder()
                        .maNhanVien(rs.getString("maNhanVien"))
                        .tenNhanVien(rs.getString("tenNhanVien"))
                        .gioiTinh(rs.getString("gioiTinh"))
                        .namSinh(rs.getInt("namSinh"))
                        .diaChi(rs.getString("diaChi"))
                        .soDienThoai(rs.getString("soDienThoai"))
                        .maChucVu(rs.getString("maChucVu"))
                        .trangThai(rs.getBoolean("trangThai"))
                        .build();
            } else {
                throw new DaoException("Không tìm thấy nhân viên: " + ma);
            }
        } catch (SQLException ex) {
            System.out.println("Lỗi" + ex.getMessage());
        }
        return nv;
    }
}
