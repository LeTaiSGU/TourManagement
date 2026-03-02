package BUS;

import DAL.TourDAL;
import DTO.Tour;
import Exception.BusException;
import Exception.DaoException;
import java.util.ArrayList;

public class TourBUS {
    TourDAL tourdal = new TourDAL();
    
    public ArrayList<Tour> getAllTour() throws BusException {
        try {
            return tourdal.getAllTour();
        }
        catch (DaoException e) {
            throw new BusException("Lỗi khi tải danh sách Tour");
        }
    }
    
    public ArrayList<Tour> getAllTourWithSoChoCon() throws BusException {
        try {
            return tourdal.getAllTourWithSoChoCon();
        }
        catch (DaoException e) {
            throw new BusException("Lỗi khi tải danh sách Tour để lấy số chỗ còn");
        }
    }
    
    
}
