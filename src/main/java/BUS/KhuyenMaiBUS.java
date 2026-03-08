package BUS;

import DAL.KhuyenMaiDAL;
import DTO.KhuyenMai;
import Exception.BusException;
import Exception.DaoException;
import java.util.ArrayList;

public class KhuyenMaiBUS {
    KhuyenMaiDAL kmdal = new KhuyenMaiDAL();
    
    public ArrayList<KhuyenMai> getGiaTriKhuyenMai() throws BusException {
        ArrayList<KhuyenMai> dskm = new ArrayList<>();
        
        try {
            return kmdal.getGiaTriKhuyenMai();
        }
        catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi lấy giá trị khuyến mãi");
        }
    }
    
    public double getGiaTriKMByMaKM(String makm) throws BusException {
        try {
            return kmdal.getGiaTriKMByMaKM(makm);
        }
        catch (DaoException e) {
            e.printStackTrace();
            throw new BusException("Lỗi khi lấy giá trị khuyến mãi theo mã khuyến mãi!");
        }
    }
}
