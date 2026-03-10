package BUS;

import DAL.NhomQuyenDAL;
import DTO.NhomQuyen;
import Exception.BusException;
import Exception.DaoException;
import java.util.List;

public class NhomQuyenBUS {
    private NhomQuyenDAL nhomQuyenDAL = new NhomQuyenDAL();
    
    public List<NhomQuyen> getAllNhomQuyen() throws BusException{
        try{
            return nhomQuyenDAL.getAllNhomQuyen();
        } catch (DaoException ex){
            throw new BusException(ex.getMessage());
        }
    }
    
    public NhomQuyen getQuyenByMa(String ma)throws BusException{
        try {
            return nhomQuyenDAL.getQuyenByMa(ma);
        } catch (DaoException ex){
            throw new BusException(ex.getMessage());
        }
    }
}
