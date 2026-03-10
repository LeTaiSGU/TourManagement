package BUS;

import java.util.List;

import DAL.LoaiTourDAL;
import DTO.LoaiTour;
import Exception.BusException;
import Exception.DaoException;

public class LoaiTourBUS {
    private LoaiTourDAL loaiTourDAL = new LoaiTourDAL();

    public List<LoaiTour> getAllLoaiTour() throws BusException {
        try {
            return loaiTourDAL.getAllLoaiTour();
        } catch (DaoException e) {
            throw new BusException("Lỗi khi tải danh sách loại tour");
        }
    }
}
