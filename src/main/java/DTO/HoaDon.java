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
    private String tenKhachHang;
    private String sdt;
    private String email;
    private LocalDate ngayLapHD;
    private double tongTien;    
    private String maKhuyenMai;
    private float thue;
    private String HTTT;
    private boolean trangThaiTT;
    
}
