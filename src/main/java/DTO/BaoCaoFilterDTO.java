package DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Bộ lọc chung cho toàn bộ module Thống kê & Báo cáo.
 *
 * Tất cả các trường đều nullable — null = không áp dụng bộ lọc đó.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaoCaoFilterDTO {

    /** Lọc từ ngày (ngayLapHD >= tuNgay) */
    private LocalDate tuNgay;

    /** Lọc đến ngày (ngayLapHD <= denNgay) */
    private LocalDate denNgay;

    /** Lọc theo năm (ghi đè tuNgay/denNgay nếu set) */
    private Integer nam;

    /** Lọc theo mã loại tour (null = tất cả) */
    private String maLoaiTour;

    /** Lọc theo mã địa điểm trong LICHTRINH (null = tất cả) */
    private String maDiaDiem;

    /** Lọc theo mã nhân viên (null = tất cả) */
    private String maNhanVien;

    /**
     * Lọc theo trạng thái tour:
     * null = tất cả
     * true = đang hoạt động
     * false = không hoạt động
     */
    private Boolean trangThaiTour;

    /**
     * Kiểm tra filter có đang lọc theo khoảng thời gian không.
     * Ưu tiên năm, sau đó tu/denNgay.
     */
    public boolean hasTimeFilter() {
        return nam != null || tuNgay != null || denNgay != null;
    }
}
