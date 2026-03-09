package BUS;
import DAL.DiaDiemDAL;
import DTO.DiaDiem;
import Exception.BusException;
import Exception.DaoException;
import java.util.ArrayList;

public class DiaDiemBUS {
    private DiaDiemDAL diaDiemDAL = new DiaDiemDAL();

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

    private void validateDiaDiem(DiaDiem diaDiem) throws BusException {
        if (diaDiem == null) throw new BusException("Dữ liệu địa điểm không hợp lệ.");
        if (isBlank(diaDiem.getMaDiaDiem())) throw new BusException("Mã địa điểm không được để trống.");
        if (isBlank(diaDiem.getTenDiaDiem())) throw new BusException("Tên địa điểm không được để trống.");
        if (isBlank(diaDiem.getAnhDiaDiem())) throw new BusException("Anh địa điểm không được để trống.");
        if (isBlank(diaDiem.getQuocGia())) throw new BusException("Quốc gia không được để trống.");
        if (isBlank(diaDiem.getMoTa())) throw new BusException("Mô tả không được để trống.");
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}