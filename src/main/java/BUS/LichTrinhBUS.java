package BUS;

import java.util.List;

import DAL.LichTrinhDAL;
import DTO.LichTrinh;
import Exception.BusException;
import Exception.DaoException;

public class LichTrinhBUS {
    private LichTrinhDAL lichTrinhDAL = new LichTrinhDAL();

    public Boolean checkMaLT(String maLT) throws BusException {
        try {
            return lichTrinhDAL.checkMaLT(maLT);
        } catch (DaoException e) {
            throw new BusException("Lỗi khi kiểm tra mã Lịch trình");
        }
    }

    public String getLastMaLichTrinh() throws BusException {
        try {
            String lastMa = lichTrinhDAL.getLastMaLichTrinh();
            if (lastMa == null || lastMa.isEmpty())
                return "LT001";

            if (!lastMa.startsWith("LT") || lastMa.length() < 3) {
                throw new BusException("Mã lịch trình hiện tại không đúng định dạng: " + lastMa);
            }
            int soThuTu = Integer.parseInt(lastMa.substring(2)) + 1;
            return String.format("LT%03d", soThuTu);
        } catch (NumberFormatException e) {
            throw new BusException("Mã lịch trình hiện tại không hợp lệ.");
        } catch (DaoException e) {
            throw new BusException("Lỗi khi tải mã Lịch trình cuối cùng");
        }
    }

    public List<LichTrinh> getAllLichTrinh() throws BusException {
        try {
            return lichTrinhDAL.getAllLichTrinh();
        } catch (DaoException e) {
            throw new BusException("Lỗi khi tải danh sách Lịch trình");
        }
    }

    public List<LichTrinh> getLichTrinhByMaTour(String maTour) throws BusException {
        try {
            return lichTrinhDAL.getLichTrinhByMaTour(maTour);
        } catch (DaoException e) {
            throw new BusException("Lỗi khi tải danh sách Lịch trình có mã tour: " + maTour);
        }
    }

    public Boolean insertLichTrinh(LichTrinh lichTrinh) throws BusException {
        try {
            return lichTrinhDAL.insertLichTrinh(lichTrinh);
        } catch (DaoException e) {
            throw new BusException("Lỗi khi thêm Lịch trình");
        }
    }

    public Boolean editLichTrinh(LichTrinh lichTrinh) throws BusException {
        try {
            return lichTrinhDAL.editLichTrinh(lichTrinh);
        } catch (DaoException e) {
            throw new BusException("Lỗi khi sửa Lịch trình");
        }
    }

    public Boolean dropLichTrinh(String maTour) throws BusException {
        try {
            return lichTrinhDAL.dropLicTrinh(maTour);
        } catch (DaoException e) {
            throw new BusException("Lỗi khi xóa Lịch trình");
        }
    }

}
