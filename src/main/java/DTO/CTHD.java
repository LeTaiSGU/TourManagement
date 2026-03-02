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

public class CTHD {
    private String maHoaDon;
    private String maTour;
    private String tenTour;
    private double giaTour;
    private int soLuongVe;
    private double thanhTien;
    private String trangThai;
    private String ghiChu;
    private LocalDate ngayKhoiHanh;
    private boolean hoanTien;
}
