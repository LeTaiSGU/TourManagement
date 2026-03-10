package BUS;

import DAL.HuongDanVienDAL;
import DTO.HuongDanVienDTO;
import Exception.BusException;
import java.util.ArrayList;

public class HuongDanVienBUS {
    private HuongDanVienDAL dal = new HuongDanVienDAL();
    public ArrayList<HuongDanVienDTO> getAllHDV() throws BusException {
        try {
            return dal.getAllHDV();
        } catch (Exception e) {
            throw new BusException("Lỗi khi tải danh sách hướng dẫn viên", e);
        }
    }
    public void addHuongDanVien(HuongDanVienDTO hdv) throws BusException {
        try {
            validateHuongDanVien(hdv);  
            dal.addHuongDanVien(hdv);
        } catch (Exception e) {
            throw new BusException("Lỗi khi thêm hướng dẫn viên", e);
        }
    }
    public void updateHuongDanVien(HuongDanVienDTO hdv) throws BusException {
        try {
            validateHuongDanVien(hdv);  
            dal.updateHuongDanVien(hdv);
        } catch (Exception e) {
            throw new BusException("Lỗi khi sửa hướng dẫn viên", e);
        }
    }
    public void deleteHuongDanVien(String maHDV) throws BusException {
        try {
            dal.deleteHuongDanVien(maHDV);
        } catch (Exception e) {
            throw new BusException("Lỗi khi xóa hướng dẫn viên", e);
        }
    }
    private void validateHuongDanVien(HuongDanVienDTO hdv) throws BusException {
        if (hdv == null) {
            throw new BusException("Dữ liệu hướng dẫn viên không hợp lệ.");
        }
        if (hdv.getMaHDV() == null || hdv.getMaHDV().trim().isEmpty()) {
            throw new BusException("Mã hướng dẫn viên không được để trống.");
        }
        if (hdv.getTenHDV() == null || hdv.getTenHDV().trim().isEmpty()) {
            throw new BusException("Tên hướng dẫn viên không được để trống.");
        }
        if (hdv.getGioiTinh() == null || hdv.getGioiTinh().trim().isEmpty()) {
            throw new BusException("Giới tính không được để trống.");
        }
        if (hdv.getNamSinh() <= 0) {
            throw new BusException("Năm sinh không hợp lệ.");
        }
        if (hdv.getChuyenMon() == null || hdv.getChuyenMon().trim().isEmpty()) {
            throw new BusException("Chuyên môn không được để trống.");
        }
        if (hdv.getSoDienThoai() == null || hdv.getSoDienThoai().trim().isEmpty()) {
            throw new BusException("Số điện thoại không được để trống.");
        }
    }
}