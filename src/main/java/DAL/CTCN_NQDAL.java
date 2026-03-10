package DAL;

import DTO.CTCN_NQ;
import Exception.DaoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CTCN_NQDAL {
    private ConnectionDAL conn = new ConnectionDAL();

    public List<CTCN_NQ> getAllCTNQByMa(String maNhomQuyen) throws DaoException {
        List<CTCN_NQ> list = new ArrayList();
        String sql = "select *from CTCNNQ where maNhomQuyen = ?";
        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setString(1, maNhomQuyen);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CTCN_NQ ctnqcn = CTCN_NQ.builder()
                        .maNhomQuyen(rs.getString("maNhomQuyen"))
                        .maCN(rs.getString("maCN"))
                        .chiTiet(rs.getString("chiTiet"))
                        .build();
                list.add(ctnqcn);
            }
            return list;
        } catch (SQLException ex) {
            throw new DaoException(ex.getMessage());
        }
    }

    public Boolean checkCTNQ(String maNhomQuyen, String maCN) throws DaoException {
        String sql = "select * from CTCNNQ where maNhomQuyen = ? and maCN = ?";
        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setString(1, maNhomQuyen);
            ps.setString(2, maCN);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException ex) {
            throw new DaoException(ex.getMessage());
        }
    }

    public void dropCTCNNQ(String maNhomQuyen) throws DaoException {
        String sql = "delete from CTCNNQ where maNhomQuyen = ?";
        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setString(1, maNhomQuyen);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DaoException(ex.getMessage());
        }
    }

    public Boolean insertCTCNNQ(String maNhomQuyen, String maCN, String chiTiet) throws DaoException {
        String sql = "insert into CTCNNQ (maNhomQuyen, maCN, chiTiet) values (?, ?, ?)";
        try (Connection con = conn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setString(1, maNhomQuyen);
            ps.setString(2, maCN);
            ps.setString(3, chiTiet);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException ex) {
            throw new DaoException(ex.getMessage());
        }
    }
}
