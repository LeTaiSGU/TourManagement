package DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Một dòng dữ liệu trong báo cáo Doanh thu.
 *
 * Dùng chung cho nhiều loại báo cáo doanh thu:
 * - Theo tháng : nhan = "Tháng 1", "Tháng 2"...
 * - Theo tour : nhan = tên tour
 * - Theo loại tour : nhan = tên loại
 * - Theo nhân viên : nhan = tên nhân viên
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaoCaoDoanhThuRowDTO {

    /** Nhãn hiển thị (tháng / tên tour / tên loại / tên NV) */
    private String nhan;

    /** Khoá phụ để lọc/nhóm (maTour, maLoaiTour, maNV) */
    private String ma;

    /** Số hóa đơn */
    private int soHoaDon;

    /** Số vé đã bán */
    private int soVe;

    /** Tổng doanh thu (VND) */
    private double tongDoanhThu;

    /** Giá trung bình mỗi hóa đơn = tongDoanhThu / soHoaDon */
    public double getGiaTrungBinh() {
        return soHoaDon > 0 ? tongDoanhThu / soHoaDon : 0;
    }
}
