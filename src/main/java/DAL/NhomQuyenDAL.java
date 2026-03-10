package DAL;

import DTO.NhomQuyen;
<<<<<<< HEAD

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAL cho bảng NHOMQUYEN.
 */
public class NhomQuyenDAL {

    private final Connection conn;

    public NhomQuyenDAL(Connection conn) {
        this.conn = conn;
    }

    private NhomQuyen map(ResultSet rs) throws SQLException {
        return NhomQuyen.builder()
                .maNhomQuyen(rs.getString("maNhomQuyen"))
                .tenNhomQuyen(rs.getString("tenNhomQuyen"))
                .moTa(rs.getString("moTa"))
                .build();
    }

    /** Lấy toàn bộ danh sách nhóm quyền. */
    public List<NhomQuyen> getAll() throws SQLException {
        List<NhomQuyen> ds = new ArrayList<>();
        String sql = "SELECT maNhomQuyen, tenNhomQuyen, moTa FROM NHOMQUYEN ORDER BY maNhomQuyen";
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                ds.add(map(rs));
        }
        return ds;
    }

    /**
     * Tìm nhóm quyền phù hợp nhất với tên chức vụ.
     * Ưu tiên: khớp chính xác → tên NQ chứa từ khóa → từ khóa nằm trong tên NQ →
     * đầu tiên trong DS.
     *
     * @param tenChucVu Tên chức vụ cần tra (VD: "Quản trị viên")
     * @return NhomQuyen phù hợp nhất, hoặc null nếu DB trống
     */
    public NhomQuyen timTheoTenChucVu(String tenChucVu) throws SQLException {
        List<NhomQuyen> daNQ = getAll();
        if (daNQ.isEmpty())
            return null;

        String key = tenChucVu == null ? "" : tenChucVu.trim().toLowerCase();

        // 1. Khớp chính xác (ignore case)
        for (NhomQuyen nq : daNQ) {
            if (nq.getTenNhomQuyen().trim().equalsIgnoreCase(key))
                return nq;
        }

        // 2. tenNhomQuyen chứa từ đầu tiên của tenChucVu
        String tuDau = key.contains(" ") ? key.split(" ")[0] : key;
        for (NhomQuyen nq : daNQ) {
            if (nq.getTenNhomQuyen().trim().toLowerCase().contains(tuDau))
                return nq;
        }

        // 3. tenChucVu chứa tenNhomQuyen (VD: NQ có tên "admin" nằm trong "Quản trị
        // admin")
        for (NhomQuyen nq : daNQ) {
            if (key.contains(nq.getTenNhomQuyen().trim().toLowerCase()))
                return nq;
        }

        // 4. Fallback — nhóm đầu tiên
        return daNQ.get(0);
=======
import Exception.DaoException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class NhomQuyenDAL{
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
    
    public NhomQuyen getQuyenByMa(String ma) throws DaoException{
        try (Connection con = conn.getConnection()) {
            CallableStatement call = con.prepareCall("{call getNQByMa(?)}");
            call.setString(1,ma);
            ResultSet rs = call.executeQuery();
             if (rs.next()) {   // 👈 BẮT BUỘC
                return NhomQuyen.builder()
                        .maNhomQuyen(rs.getString("maNhomQuyen"))
                        .tenNhomQuyen(rs.getString("tenNhomQuyen"))
                        .moTa(rs.getString("moTa"))
                        .build();
            } else {
                return null;   // Không tìm thấy
            }
        } catch(SQLException ex){
            throw new DaoException("Lỗi khi tìm dữ liệu: " + ex.getMessage());
        }
        
>>>>>>> 379838c5d2def71baad191839bfd9123657069d3
    }
}
