package BUS;

import java.util.List;
import DAL.DiaDiemDAL;
import DTO.DiaDiem;
import Exception.BusException;
import Exception.DaoException;

public class DiaDiemBUS {

    private DiaDiemDAL diaDiemDAL = new DiaDiemDAL();

    public List<DiaDiem> getAllDiaDiem() throws BusException {
        try {
            return diaDiemDAL.getAllDiaDiem();
        } catch (DaoException e) {
            throw new BusException("Lỗi khi tải danh sách địa điểm");
        }
    }

    public String getTenDiaDiemByMa(String maDiaDiem) throws BusException {
        try {
            return diaDiemDAL.getTenDiaDiemByMa(maDiaDiem);
        } catch (DaoException e) {
            throw new BusException("Lỗi khi tải địa điểm theo mã");
        }
    }

    public String getMaDiaDiemByTen(String tenDiaDiem) throws BusException {
        try {
            if (tenDiaDiem == null || tenDiaDiem.isEmpty())
                throw new BusException("Tên địa điểm không được để trống");
            return diaDiemDAL.getMaDiaDiemByTen(tenDiaDiem);
        } catch (DaoException e) {
            throw new BusException("Lỗi khi tải mã địa điểm theo tên");
        }
    }
}
