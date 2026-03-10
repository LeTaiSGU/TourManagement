package BUS;

import DAL.CTCN_NQDAL;
import DTO.CTCN_NQ;
import Exception.BusException;
import Exception.DaoException;
import java.util.List;

public class CTCN_NQBUS {
    private CTCN_NQDAL ctcnnqDal = new CTCN_NQDAL();

    public List<CTCN_NQ> getCTCN_NQbyMa(String maNhomQuyen) throws BusException {
        try {
            return ctcnnqDal.getAllCTNQByMa(maNhomQuyen);
        } catch (DaoException ex) {
            throw new BusException(ex.getMessage());
        }
    }

    public Boolean checkCTNQ(String maNhomQuyen, String maCN) throws BusException {
        try {
            return ctcnnqDal.checkCTNQ(maNhomQuyen, maCN);
        } catch (DaoException ex) {
            throw new BusException(ex.getMessage());
        }
    }

    public void dropCTCNNQ(String maNhomQuyen) throws BusException {
        try {
            ctcnnqDal.dropCTCNNQ(maNhomQuyen);
        } catch (DaoException ex) {
            throw new BusException(ex.getMessage());
        }
    }

    public Boolean insertCTCNNQ(String maNhomQuyen, String maCN, String chiTiet) throws BusException {
        try {
            return ctcnnqDal.insertCTCNNQ(maNhomQuyen, maCN, chiTiet);
        } catch (DaoException ex) {
            throw new BusException(ex.getMessage());
        }
    }
}
