package BUS;

import DAL.KhachHangDAL;
import DTO.KhachHang;
import Exception.BusException;
import Exception.DaoException;
import java.util.ArrayList;

public class KhachHangBUS {
    KhachHangDAL khdal = new KhachHangDAL();
    
    public ArrayList<KhachHang> getAllKhachHang() throws BusException {
        try {
            return khdal.getAllKhachHang();
        }
        catch (DaoException e) {
            throw new BusException("Lỗi khi tải danh sách Khách hàng");
        }
    }
}
