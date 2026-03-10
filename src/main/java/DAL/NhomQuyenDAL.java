package DAL;

import DTO.NhomQuyen;
import Exception.DaoException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NhomQuyenDAL {
    private ConnectionDAL conn = new ConnectionDAL();

    public List<NhomQuyen> getAllNhomQuyen() throws DaoException {
        List<NhomQuyen> list = new ArrayList<>();
        String sql = "select * from NHOMQUYEN";

        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                NhomQuyen nq = NhomQuyen.builder()
                        .maNhomQuyen(rs.getString(1))
                        .tenNhomQuyen(rs.getString(2))
                        .moTa(rs.getString(3))
                        .build();
                list.add(nq);
            }

        } catch (SQLException ex) {
            throw new DaoException("Lỗi khi lấy dữ liệu: " + ex.getMessage());
        }

        return list;
    }

    public NhomQuyen getQuyenByMa(String ma) throws DaoException {
        try (Connection con = conn.getConnection()) {
            CallableStatement call = con.prepareCall("{call getNQByMa(?)}");
            call.setString(1, ma);
            ResultSet rs = call.executeQuery();
            if (rs.next()) { // 👈 BẮT BUỘC
                return NhomQuyen.builder()
                        .maNhomQuyen(rs.getString("maNhomQuyen"))
                        .tenNhomQuyen(rs.getString("tenNhomQuyen"))
                        .moTa(rs.getString("moTa"))
                        .build();
            } else {
                return null; // Không tìm thấy
            }
        } catch (SQLException ex) {
            throw new DaoException("Lỗi khi tìm dữ liệu: " + ex.getMessage());
        }

    }
}
