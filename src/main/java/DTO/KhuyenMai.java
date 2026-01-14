package DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class KhuyenMai {
    private String maKhuyenMai;
    private String tenKhuyenMai;
    private String moTa;
    private String phuongThucKM;
    private Boolean trangThaiKM; 
}
