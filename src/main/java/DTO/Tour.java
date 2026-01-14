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

public class Tour {
    private String maTour;
    private String maLoaiTour;
    private String tenTour;
    private String anhTour;
    private String noiKhoiHanh;
    private LocalDate tgKhoiHanh;
    private LocalDate tgKetThuc;
    private double giaTour;
    private int soLuongVe;
    private int soLuongMin;
    private Boolean trangThai;
    private Boolean khoiHanh;
    
}
