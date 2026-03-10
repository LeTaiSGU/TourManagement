package DAL;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DTO.LichTrinh;
import Exception.DaoException;

public class LichTrinhDAL {
    private ConnectionDAL conn = new ConnectionDAL();

    public Boolean checkMaLT(String maLT) throws DaoException {
        String sql = "{call checkMaLT(?)}";
        try (Connection con = conn.getConnection();
                CallableStatement call = con.prepareCall(sql);) {
            call.setString(1, maLT);
            ResultSet rs = call.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1);
            }
            return false;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("Lỗi: " + ex.getMessage());
        }
    }

    public String getLastMaLichTrinh() throws DaoException {
        String sql = "{call getLastMaLT}";
        try (Connection con = conn.getConnection();
                CallableStatement call = con.prepareCall(sql);) {
            ResultSet rs = call.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
            return null;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("Lỗi: " + ex.getMessage());
        }
    }

    public List<LichTrinh> getAllLichTrinh() throws DaoException {
        List<LichTrinh> dslt = new ArrayList();
        String sql = "Select * from LICHTRINH where trangThai = 1";
        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LichTrinh lt = LichTrinh.builder()
                        .maLichTrinh(rs.getString("maLichTrinh"))
                        .maTour(rs.getString("maTour"))
                        .maDiaDiem(rs.getString("maDiaDiem"))
                        .maPT(rs.getString("maPT"))
                        .ngayThu(rs.getInt("ngayThu"))
                        .noiDung(rs.getString("noiDung"))
                        .trangThai(rs.getBoolean("trangThai"))
                        .build();
                dslt.add(lt);
            }
            return dslt;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("Lỗi: " + ex.getMessage());
        }
    }

    public List<LichTrinh> getLichTrinhByMaTour(String maTour) throws DaoException {
        List<LichTrinh> dslt = new ArrayList();
        String sql = "Select * from LICHTRINH where maTour = ? and trangThai = 1";
        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setString(1, maTour);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LichTrinh lt = LichTrinh.builder()
                        .maLichTrinh(rs.getString("maLichTrinh"))
                        .maTour(rs.getString("maTour"))
                        .maDiaDiem(rs.getString("maDiaDiem"))
                        .maPT(rs.getString("maPT"))
                        .ngayThu(rs.getInt("ngayThu"))
                        .noiDung(rs.getString("noiDung"))
                        .trangThai(rs.getBoolean("trangThai"))
                        .build();
                dslt.add(lt);
            }
            return dslt;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("Lỗi: " + ex.getMessage());
        }
    }

    public Boolean insertLichTrinh(LichTrinh lichTrinh) throws DaoException {
        String sql = "{call insertLichTrinh(?,?,?,?,?,?,?)}";
        try (Connection con = conn.getConnection();
                CallableStatement call = con.prepareCall(sql);) {
            call.setString(1, lichTrinh.getMaLichTrinh());
            call.setString(2, lichTrinh.getMaTour());
            call.setString(3, lichTrinh.getMaDiaDiem());
            call.setString(4, lichTrinh.getMaPT());
            call.setInt(5, lichTrinh.getNgayThu());
            call.setString(6, lichTrinh.getNoiDung());
            call.setBoolean(7, true);
            int rows = call.executeUpdate();
            return rows > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("Lỗi: " + ex.getMessage());
        }
    }

    public Boolean editLichTrinh(LichTrinh lichTrinh) throws DaoException {
        String sql = "{call editLichTrinh(?,?,?,?,?,?,?)}";
        try (Connection con = conn.getConnection();
                CallableStatement call = con.prepareCall(sql);) {
            call.setString(1, lichTrinh.getMaLichTrinh());
            call.setString(2, lichTrinh.getMaTour());
            call.setString(3, lichTrinh.getMaDiaDiem());
            call.setString(4, lichTrinh.getMaPT());
            call.setInt(5, lichTrinh.getNgayThu());
            call.setString(6, lichTrinh.getNoiDung());
            call.setBoolean(7, lichTrinh.getTrangThai());
            int rows = call.executeUpdate();
            return rows > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("Lỗi: " + ex.getMessage());
        }
    }

    public Boolean dropLicTrinh(String maTour) throws DaoException {
        String sql = "{call dropLichtrinhTour(?)}";
        try (Connection con = conn.getConnection();
                CallableStatement call = con.prepareCall(sql);) {
            call.setString(1, maTour);
            int rows = call.executeUpdate();
            return rows > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("Lỗi: " + ex.getMessage());
        }
    }
}
