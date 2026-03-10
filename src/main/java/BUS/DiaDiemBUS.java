package BUS;

import java.util.ArrayList;
import java.util.List;

import DAL.DiaDiemDAL;
import DTO.DiaDiem;
import Exception.BusException;
import Exception.DaoException;

public class DiaDiemBUS {

    private DiaDiemDAL diaDiemDAL = new DiaDiemDAL();

    public List<DiaDiem> getAllDiaDiemTai() throws BusException {
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

    public ArrayList<DiaDiem> getAllDiaDiem() throws BusException {
        try {
            return diaDiemDAL.getAll();
        } catch (DaoException e) {
            throw new BusException("Lỗi khi tải danh sách địa điểm.");
        }
    }

    public void addDiaDiem(DiaDiem diaDiem) throws BusException {
        try {
            validateDiaDiem(diaDiem);
            diaDiemDAL.addDiaDiem(diaDiem);
        } catch (DaoException e) {
            throw new BusException("Lỗi khi thêm địa điểm.");
        }
    }

    public void updateDiaDiem(DiaDiem diaDiem) throws BusException {
        try {
            validateDiaDiem(diaDiem);
            diaDiemDAL.updateDiaDiem(diaDiem);
        } catch (DaoException e) {
            throw new BusException("Lỗi khi sửa địa điểm.");
        }
    }

    public void deleteDiaDiem(String maDiaDiem) throws BusException {
        try {
            diaDiemDAL.deleteDiaDiem(maDiaDiem);
        } catch (DaoException e) {
            throw new BusException("Lỗi khi xóa địa điểm.");
        }
    }

    public String sinhMaMoi() throws BusException {
        try {
            return diaDiemDAL.sinhMaMoi();
        } catch (DaoException e) {
            throw new BusException("Loi sinh ma dia diem.");
        }
    }

    public ArrayList<DiaDiem> searchDiaDiem(String keyword) throws BusException {
        try {
            return diaDiemDAL.searchDiaDiem(keyword);
        } catch (DaoException e) {
            throw new BusException("Loi tim kiem dia diem.");
        }
    }

    private void validateDiaDiem(DiaDiem diaDiem) throws BusException {
        if (diaDiem == null)
            throw new BusException("Dữ liệu địa điểm không hợp lệ.");
        if (isBlank(diaDiem.getMaDiaDiem()))
            throw new BusException("Mã địa điểm không được để trống.");
        if (isBlank(diaDiem.getTenDiaDiem()))
            throw new BusException("Tên địa điểm không được để trống.");
        if (isBlank(diaDiem.getAnhDiaDiem()))
            throw new BusException("Anh địa điểm không được để trống.");
        if (isBlank(diaDiem.getQuocGia()))
            throw new BusException("Quốc gia không được để trống.");
        if (isBlank(diaDiem.getMoTa()))
            throw new BusException("Mô tả không được để trống.");
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
