package DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class HuongDanVien {
    private String maHDV;
    private String tenHDV;
    private String gioiTinh;
    private int namSinh;
    private String chuyenMon;
    private String soDienThoai;
    private Boolean trangThai;
}
