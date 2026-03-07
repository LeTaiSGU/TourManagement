package BUS;

import DAL.KhuyenMaiDAL;
import DTO.KhuyenMaiDTO;
import Exception.BusException;
import java.util.ArrayList;

public class KhuyenMaiBUS {
    private KhuyenMaiDAL kmDAL = new KhuyenMaiDAL();
    public ArrayList<KhuyenMaiDTO> getAll() throws BusException {
        try {
            return kmDAL.getAllKhuyenMai();  
        } catch (Exception e) {
            throw new BusException("Lỗi khi lấy danh sách khuyến mãi.", e);
        }
    }
    public void addKhuyenMai(KhuyenMaiDTO khuyenMai) throws BusException {
    validateKhuyenMai(khuyenMai);
    try {
        KhuyenMaiDAL dal = new KhuyenMaiDAL();
        dal.addKhuyenMai(khuyenMai);
    } catch (Exception e) {
        throw new BusException("Lỗi khi thêm khuyến mãi", e);
        }
    }
    public void updateKhuyenMai(KhuyenMaiDTO km) throws BusException {
        try {
            kmDAL.updateKhuyenMai(km);  
        } catch (Exception e) {
            throw new BusException("Lỗi khi sửa khuyến mãi.", e);
        }
    }
    public void deleteKhuyenMai(String maKhuyenMai) throws BusException {
        try {
            kmDAL.deleteKhuyenMai(maKhuyenMai);  
        } catch (Exception e) {
            throw new BusException("Lỗi khi xóa khuyến mãi.", e);
        }
    }
    public void validateKhuyenMai(KhuyenMaiDTO khuyenMai) throws BusException {
    if (khuyenMai == null) throw new BusException("Dữ liệu khuyến mãi không hợp lệ.");
    if (isBlank(khuyenMai.getMaKhuyenMai())) throw new BusException("Mã khuyến mãi không được để trống.");
    if (isBlank(khuyenMai.getTenKhuyenMai())) throw new BusException("Tên khuyến mãi không được để trống.");
    if (isBlank(khuyenMai.getPhuongThucKM())) throw new BusException("Phương thức khuyến mãi không được để trống.");
    if (isBlank(khuyenMai.getMoTa())) throw new BusException("Mô tả khuyến mãi không được để trống.");
    if (!isValidPhuongThucKM(khuyenMai.getPhuongThucKM())) throw new BusException("Phương thức khuyến mãi không hợp lệ.");
}

    private boolean isBlank(String str) {
    return str == null || str.trim().isEmpty();
}

    private boolean isValidPhuongThucKM(String phuongThucKM) {
    return phuongThucKM.equals("Giảm giá") || phuongThucKM.equals("Tặng quà");
    }
}