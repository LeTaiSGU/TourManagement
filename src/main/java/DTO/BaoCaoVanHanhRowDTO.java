package DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Một dòng dữ liệu trong báo cáo Vận hành Tour.
 *
 * Dùng chung cho hai loại vận hành:
 * 1. Số lịch trình theo phương tiện (nhan = tên PT, soLuong = số lịch trình)
 * 2. Tour theo hướng dẫn viên (nhan = tên HDV, soLuong = số tour đã dẫn)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaoCaoVanHanhRowDTO {

    /** Mã phần tử (maPT / maHDV) */
    private String ma;

    /** Nhãn hiển thị (tên PT / tên HDV) */
    private String nhan;

    /** Số lượng (lịch trình / tour) */
    private int soLuong;

    /** Trường bổ sung (giới tính, chuyên môn...) */
    private String thongTinThem;
}
