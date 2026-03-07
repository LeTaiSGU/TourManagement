package BUS;

import DAL.KhachHangDAL;
import DTO.KhachHangDTO;
import Exception.BusException;
import java.util.ArrayList;

public class KhachHangBUS {
    private KhachHangDAL khachHangDAL = new KhachHangDAL();
    public ArrayList<KhachHangDTO> getAllKhachHang() throws BusException {
        try {
            return khachHangDAL.getAllKhachHang();
        } catch (Exception e) {
            throw new BusException("Lỗi khi lấy danh sách khách hàng.", e);
        }
    }
    public void addKhachHang(KhachHangDTO kh) throws BusException {
    validateKhachHang(kh);  
    try {
        khachHangDAL.addKhachHang(kh);
    } catch (Exception e) {
        throw new BusException("Lỗi khi thêm khách hàng.", e);
    }
}

    public void updateKhachHang(KhachHangDTO kh) throws BusException {
    validateKhachHang(kh);  
    try {
        khachHangDAL.updateKhachHang(kh);
    } catch (Exception e) {
        throw new BusException("Lỗi khi sửa thông tin khách hàng.", e);
    }
}
    public void deleteKhachHang(String maKhachHang) throws BusException {
        try {
            khachHangDAL.deleteKhachHang(maKhachHang);
        } catch (Exception e) {
            throw new BusException("Lỗi khi xóa khách hàng.", e);
        }
    }
    public ArrayList<KhachHangDTO> searchKhachHang(String keyword) throws BusException {
        try {
            return khachHangDAL.searchKhachHang(keyword);
        } catch (Exception e) {
            throw new BusException("Lỗi khi tìm kiếm khách hàng.", e);
        }
    }
    public void validateKhachHang(KhachHangDTO kh) throws BusException {
    if (kh == null) throw new BusException("Dữ liệu khách hàng không hợp lệ.");
    if (isBlank(kh.getMaKhachHang())) throw new BusException("Mã khách hàng không được để trống.");
    if (isBlank(kh.getTenKhachHang())) throw new BusException("Tên khách hàng không được để trống.");
    if (isBlank(kh.getGioiTinh())) throw new BusException("Giới tính không được để trống.");
    if (kh.getNamSinh() <= 0) throw new BusException("Năm sinh không hợp lệ.");
    if (isBlank(kh.getDiaChi())) throw new BusException("Địa chỉ không được để trống.");
    if (isBlank(kh.getSoDienThoai())) throw new BusException("Số điện thoại không được để trống.");
    if (isBlank(kh.getEmail())) throw new BusException("Email không được để trống.");
    if (!isValidEmail(kh.getEmail())) throw new BusException("Email không hợp lệ.");
}
    private boolean isBlank(String str) {
    return str == null || str.trim().isEmpty();
}
    private boolean isValidEmail(String email) {
    String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
    return email.matches(regex);
}
}