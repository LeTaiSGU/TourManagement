package DTO;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HoaDon {
    private String maHoaDon;
    private String maNhanVien;
    private String maKhachHang;
    private LocalDate ngayLapHD;
    private int soLuongVe;
    private double tongTien;    
    private String maKhuyenMai;
    private static double thue;
    private String HTTT;
    private int trangThaiTT;
    private int trangThaiHD;
    
}
