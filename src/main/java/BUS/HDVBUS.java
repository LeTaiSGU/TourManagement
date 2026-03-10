package BUS;

import java.util.List;

import DAL.HuongDanVienDAL;
import DTO.HuongDanVien;
import Exception.BusException;
import Exception.DaoException;

public class HDVBUS {
    private HuongDanVienDAL hdvdal = new HuongDanVienDAL();

    public List<HuongDanVien> getAllHuongDanVien() throws BusException {
        try {
            return hdvdal.getAllHuongDanVien();
        } catch (DaoException e) {
            throw new BusException("Lỗi khi tải danh sách huớng dẫn viên");
        }
    }
}
