package BUS;

import DAL.NhanVienDAL;
import DTO.NhanVien;
import Exception.BusException;
import Exception.DaoException;

public class NhanVienBUS {
    private NhanVienDAL nhanVienDAL = new NhanVienDAL();

    public NhanVien getNvByMa(String ma) throws BusException {
        if (ma == null || ma.trim().isEmpty()) {
            throw new BusException("Mã nhân viên không được để trống");
        }
        try {
            NhanVien nv = nhanVienDAL.getNhanVienByMa(ma);
            if (nv == null) {
                throw new BusException("Không tìm thấy nhân viên với mã: " + ma);
            }
            return nv;
        } catch (DaoException e) {
            throw new BusException("Lỗi dữ liệu nhân viên: " + e.getMessage());
        }
    }
}
