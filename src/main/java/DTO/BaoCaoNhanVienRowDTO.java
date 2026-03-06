package DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Một dòng dữ liệu trong báo cáo Bán hàng theo Nhân viên.
 *
 * Giúp đánh giá hiệu quả bán tour của từng nhân viên:
 * số hóa đơn, số tour, số khách phục vụ và doanh thu tổng.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaoCaoNhanVienRowDTO {

    /** Mã nhân viên */
    private String maNhanVien;

    /** Tên nhân viên */
    private String tenNhanVien;

    /** Tên chức vụ */
    private String tenChucVu;

    /** Số hóa đơn đã lập */
    private int soHoaDon;

    /** Số tour đã bán (số CTHD) */
    private int soTourBan;

    /** Tổng số vé đã bán */
    private int soVe;

    /** Tổng doanh thu do nhân viên mang lại */
    private double tongDoanhThu;
}
