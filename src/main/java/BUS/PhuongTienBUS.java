package BUS;

import java.util.List;
import DAL.PhuongTienDAL;
import DTO.PhuongTien;
import Exception.BusException;
import Exception.DaoException;

public class PhuongTienBUS {
    private PhuongTienDAL phuongTienDAL = new PhuongTienDAL();

    public List<PhuongTien> getAllPhuongTien() throws BusException {
        try {
            return phuongTienDAL.getAllPhuongTien();
        } catch (DaoException e) {
            throw new BusException("Lỗi khi tải danh sách phương tiện");
        }
    }

    public String getTenPhuongTienByMa(String tenPhuongTien) throws BusException {
        try {
            return phuongTienDAL.getTenPhuongTienByMa(tenPhuongTien);
        } catch (DaoException e) {
            throw new BusException("Lỗi khi tải phương tiện theo tên");
        }
    }

    public String getMaPhuongTienByTen(String tenPhuongTien) throws BusException {
        try {
            if (tenPhuongTien == null || tenPhuongTien.isEmpty())
                throw new BusException("Tên phương tiện không được để trống");
            return phuongTienDAL.getMaPhuongTienByTen(tenPhuongTien);
        } catch (DaoException e) {
            throw new BusException("Lỗi khi tải mã phương tiện theo tên");
        }
    }
}
