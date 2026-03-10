package BUS;

import DAL.TourDAL;
import DTO.Tour;
import Exception.BusException;
import Exception.DaoException;
import GUI.Dialog.TourSearchDialog.SearchCriteria;

import java.util.ArrayList;
import java.util.List;

public class TourBUS {
    TourDAL tourdal = new TourDAL();

    public ArrayList<Tour> getAllTour() throws BusException {
        try {
            return tourdal.getAllTour();
        } catch (DaoException e) {
            throw new BusException("Lỗi khi tải danh sách Tour");
        }
    }

    public ArrayList<Tour> getAllTourWithSoChoCon() throws BusException {
        try {
            return tourdal.getAllTourWithSoChoCon();
        } catch (DaoException e) {
            throw new BusException("Lỗi khi tải danh sách Tour để lấy số chỗ còn");
        }
    }

    public List<Tour> getTourByMaTourChuaKhoiHanh() throws BusException {
        try {
            return tourdal.getTourByMaTourChuaKhoiHanh();
        } catch (DaoException e) {
            throw new BusException("Lỗi khi tải danh sách Tour chưa khởi hành");
        }
    }

    public String getLastMa() throws BusException {
        try {
            String lastMa = tourdal.getLastMa();
            if (lastMa == null || lastMa.isEmpty())
                return "T001";
            int soThuTu = Integer.parseInt(lastMa.substring(1)) + 1;
            return String.format("T%03d", soThuTu);
        } catch (DaoException ex) {
            throw new BusException(ex.getMessage());
        }
    }

    public Tour getTourByMaTour(String maTour) throws BusException {
        try {
            return tourdal.getTourByMaTour(maTour);
        } catch (DaoException e) {
            throw new BusException("Lỗi khi tải tour có mã: " + maTour);
        }
    }

    public ArrayList<Tour> searchTour(SearchCriteria criteria) throws BusException {
        try {
            return tourdal.searchTour(criteria);
        } catch (DaoException e) {
            throw new BusException("Lỗi khi tìm kiếm tour");
        }
    }

    public Boolean insertTour(Tour tour) throws BusException {
        validateTour(tour);
        try {
            return tourdal.insertTour(tour);
        } catch (DaoException e) {
            throw new BusException("Lỗi khi thêm tour: " + e.getMessage());
        }
    }

    public Boolean editTour(Tour tour) throws BusException {
        validateTour(tour);
        try {
            return tourdal.editTour(tour);
        } catch (DaoException e) {
            throw new BusException("Lỗi khi sửa tour: " + e.getMessage());
        }
    }

    public Boolean huyTourByAdmin(String maTour, String lyDoHuy) throws BusException {
        if (isBlank(lyDoHuy)) {
            throw new BusException("Vui lòng nhập lý do hủy tour.");
        }
        try {
            return tourdal.huyTourByAdmin(maTour.trim(), lyDoHuy.trim());
        } catch (DaoException e) {
            throw new BusException("Lỗi khi hủy tour: " + e.getMessage());
        }
    }

    private void validateTour(Tour tour) throws BusException {
        if (tour == null) {
            throw new BusException("Dữ liệu tour không hợp lệ.");
        }
        if (isBlank(tour.getMaLoaiTour())) {
            throw new BusException("Mã loại tour không được để trống.");
        }
        if (isBlank(tour.getMaHDV())) {
            throw new BusException("Mã hướng dẫn viên không được để trống.");
        }
        if (isBlank(tour.getTenTour())) {
            throw new BusException("Tên tour không được để trống.");
        }
        if (isBlank(tour.getNoiKhoiHanh())) {
            throw new BusException("Nơi khởi hành không được để trống.");
        }
        if (tour.getTgKhoiHanh() == null || tour.getTgKetThuc() == null) {
            throw new BusException("Thời gian khởi hành/kết thúc không được để trống.");
        }
        if (Double.isNaN(tour.getGiaTour()) || Double.isInfinite(tour.getGiaTour())) {
            throw new BusException("Giá tour không hợp lệ.");
        }
        if (tour.getGiaTour() < 0) {
            throw new BusException("Giá tour phải lớn hơn hoặc bằng 0.");
        }

        if (tour.getSoLuongVe() < 0) {
            throw new BusException("Số lượng vé phải lớn hơn hoặc bằng 0.");
        }
        if (tour.getSoLuongMin() < 0) {
            throw new BusException("Số lượng vé tối thiểu phải lớn hơn hoặc bằng 0.");
        }
        if (tour.getSoLuongMin() > tour.getSoLuongVe()) {
            throw new BusException("Số lượng vé tối thiểu không được lớn hơn tổng số lượng vé.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public int checkTourDuSoLuongKhoiHanh(String maTour) throws BusException {
        if (isBlank(maTour)) {
            throw new BusException("Mã tour không được để trống.");
        }
        try {
            return tourdal.checkTourDuSoLuongKhoiHanh(maTour.trim());
        } catch (DaoException e) {
            throw new BusException("Lỗi kiểm tra điều kiện khởi hành tour: " + e.getMessage());
        }
    }

    public int checkKhoiHanhHomNay() throws BusException {
        try {
            return tourdal.checkKhoiHanhHomNay();
        } catch (DaoException e) {
            throw new BusException("Lỗi kiểm tra khởi hành hôm nay: " + e.getMessage());
        }
    }

}
