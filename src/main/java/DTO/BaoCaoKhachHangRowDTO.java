package DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Một dòng dữ liệu trong báo cáo Khách hàng.
 *
 * Dùng cho:
 * - Phân loại khách hàng theo nhóm (loai = tên nhóm)
 * - Top khách chi tiêu (tenKhachHang + tongChiTieu)
 * - Phân bổ theo giới tính / khu vực
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaoCaoKhachHangRowDTO {

    /** Mã khách hàng (nếu là thống kê từng khách) */
    private String maKhachHang;

    /** Tên hoặc nhóm (tên KH / tên loại KH / giới tính / khu vực) */
    private String nhan;

    /** Số lượng khách hoặc hóa đơn */
    private int soLuong;

    /** Số lần đặt tour */
    private int soHoaDon;

    /** Tổng chi tiêu (VND) */
    private double tongChiTieu;

    /** Tỷ lệ % so với tổng (tính ngoài, không lưu trong DB) */
    private double tyLe;
}
