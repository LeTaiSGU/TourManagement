package DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Một dòng dữ liệu trong báo cáo Tour.
 *
 * Cung cấp đánh giá hiệu quả hoạt động của từng tour:
 * số khách đã đặt vs sức chứa, tỷ lệ lấp đầy, doanh thu.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaoCaoTourRowDTO {

    /** Mã tour */
    private String maTour;

    /** Tên tour */
    private String tenTour;

    /** Tên loại tour */
    private String tenLoaiTour;

    /** Điểm khởi hành */
    private String noiKhoiHanh;

    /** Sức chứa tối đa (soLuongVe) */
    private int soLuongToiDa;

    /** Số vé đã bán (từ HOADON) */
    private int soVeDaBan;

    /** Tỷ lệ lấp đầy (%) = soVeDaBan * 100.0 / soLuongToiDa */
    private double tyLeLapDay;

    /** Tổng doanh thu từ tour này */
    private double doanhThu;

    /** Trạng thái tour: 1=hoạt động, 0=dừng */
    private boolean trangThai;

    /** Đã khởi hành chưa */
    private boolean khoiHanh;
}
