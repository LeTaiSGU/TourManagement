package DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhuyenMaiDTO {
    private String maKhuyenMai;
    private String tenKhuyenMai;
    private String phuongThucKM;
    private String moTa;
}