package BUS;

import DAL.BaoCaoDAL;
import DAL.ConnectionDAL;
import DTO.BaoCaoDoanhThuRowDTO;
import DTO.BaoCaoFilterDTO;
import DTO.BaoCaoKhachHangRowDTO;
import DTO.BaoCaoNhanVienRowDTO;
import DTO.BaoCaoTourRowDTO;
import DTO.BaoCaoVanHanhRowDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * BUS cho module Thống kê & Báo cáo.
 *
 * Ủy thác truy vấn xuống BaoCaoDAL và thực hiện các tính toán
 * nghiệp vụ bổ sung (tổng hợp, tỷ lệ, phần trăm...).
 *
 * Không chứa bất kỳ code Swing nào.
 */
public class BaoCaoBUS {

    private final BaoCaoDAL dal;

    /** Constructor mặc định — tự lấy kết nối từ ConnectionDAL */
    public BaoCaoBUS() {
        this.dal = new BaoCaoDAL(ConnectionDAL.getConnection());
    }

    // =========================================================
    // DOANH THU
    // =========================================================

    /**
     * Doanh thu 12 tháng trong năm được chọn.
     * Trả về đúng 12 phần tử (tháng thiếu = 0).
     */
    public List<BaoCaoDoanhThuRowDTO> getDoanhThuTheoThang(BaoCaoFilterDTO filter)
            throws SQLException {
        return dal.getDoanhThuTheoThang(filter);
    }

    /** Doanh thu theo từng tour, sắp xếp giảm dần. */
    public List<BaoCaoDoanhThuRowDTO> getDoanhThuTheoTour(BaoCaoFilterDTO filter)
            throws SQLException {
        return dal.getDoanhThuTheoTour(filter);
    }

    /** Doanh thu theo loại tour. */
    public List<BaoCaoDoanhThuRowDTO> getDoanhThuTheoLoaiTour(BaoCaoFilterDTO filter)
            throws SQLException {
        return dal.getDoanhThuTheoLoaiTour(filter);
    }

    /** Tổng doanh thu (sum tất cả tongTien theo filter). */
    public double tinhTongDoanhThu(BaoCaoFilterDTO filter) throws SQLException {
        return dal.getDoanhThuTheoThang(filter).stream()
                .mapToDouble(BaoCaoDoanhThuRowDTO::getTongDoanhThu)
                .sum();
    }

    // =========================================================
    // NHÂN VIÊN
    // =========================================================

    /** Hiệu quả bán hàng của từng nhân viên. */
    public List<BaoCaoNhanVienRowDTO> getDoanhThuTheoNhanVien(BaoCaoFilterDTO filter)
            throws SQLException {
        return dal.getDoanhThuTheoNhanVien(filter);
    }

    // =========================================================
    // TOUR
    // =========================================================

    /** Thống kê đầy đủ từng tour (lấp đầy, doanh thu, trạng thái). */
    public List<BaoCaoTourRowDTO> getThongKeTour(BaoCaoFilterDTO filter)
            throws SQLException {
        return dal.getThongKeTour(filter);
    }

    /**
     * Số tour theo từng trạng thái:
     * index 0 = Hoạt động, index 1 = Ngừng, index 2 = Đã khởi hành
     */
    public int[] demTourTheoTrangThai(BaoCaoFilterDTO filter) throws SQLException {
        List<BaoCaoTourRowDTO> ds = dal.getThongKeTour(filter);
        int hoatDong = 0, ngung = 0, khoiHanh = 0;
        for (BaoCaoTourRowDTO r : ds) {
            if (r.isTrangThai())
                hoatDong++;
            else
                ngung++;
            if (r.isKhoiHanh())
                khoiHanh++;
        }
        return new int[] { hoatDong, ngung, khoiHanh };
    }

    // =========================================================
    // KHÁCH HÀNG
    // =========================================================

    /** Phân loại khách hàng theo nhóm. */
    public List<BaoCaoKhachHangRowDTO> getKhachTheoLoai() throws SQLException {
        List<BaoCaoKhachHangRowDTO> ds = dal.getKhachTheoLoai();
        int tong = ds.stream().mapToInt(BaoCaoKhachHangRowDTO::getSoLuong).sum();
        if (tong > 0) {
            ds.forEach(r -> r.setTyLe(r.getSoLuong() * 100.0 / tong));
        }
        return ds;
    }

    /** Phân bổ theo giới tính. */
    public List<BaoCaoKhachHangRowDTO> getKhachTheoGioiTinh() throws SQLException {
        List<BaoCaoKhachHangRowDTO> ds = dal.getKhachTheoGioiTinh();
        int tong = ds.stream().mapToInt(BaoCaoKhachHangRowDTO::getSoLuong).sum();
        if (tong > 0) {
            ds.forEach(r -> r.setTyLe(r.getSoLuong() * 100.0 / tong));
        }
        return ds;
    }

    /** Top khách chi tiêu (mặc định top 10). */
    public List<BaoCaoKhachHangRowDTO> getTopKhachChiTieu(BaoCaoFilterDTO filter)
            throws SQLException {
        return dal.getTopKhachChiTieu(10, filter);
    }

    /** Toàn bộ khách hàng với chi tiết. */
    public List<BaoCaoKhachHangRowDTO> getAllKhachHangChiTiet(BaoCaoFilterDTO filter)
            throws SQLException {
        return dal.getAllKhachHangChiTiet(filter);
    }

    // =========================================================
    // VẬN HÀNH
    // =========================================================

    /** Số lịch trình theo phương tiện. */
    public List<BaoCaoVanHanhRowDTO> getLichTrinhTheoPhuongTien() throws SQLException {
        return dal.getLichTrinhTheoPhuongTien();
    }

    /** Số tour theo hướng dẫn viên. */
    public List<BaoCaoVanHanhRowDTO> getTourTheoHuongDanVien() throws SQLException {
        return dal.getTourTheoHuongDanVien();
    }

    // =========================================================
    // DỮ LIỆU PHỤ TRỢ CHO FILTER
    // =========================================================

    /** Danh sách năm có dữ liệu, dùng để điền ComboBox. */
    public List<Integer> getDanhSachNam() throws SQLException {
        return dal.getDanhSachNam();
    }

    /** Map mã→tên loại tour, dùng để điền ComboBox. */
    public Map<String, String> getDanhSachLoaiTour() throws SQLException {
        return dal.getDanhSachLoaiTour();
    }

    /** Map mã→tên nhân viên, dùng để điền ComboBox. */
    public Map<String, String> getDanhSachNhanVien() throws SQLException {
        return dal.getDanhSachNhanVien();
    }
}
