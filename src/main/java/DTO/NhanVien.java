package DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhanVien {
    private String maNhanVien;
    private String tenNhanVien;
    private String gioiTinh;
    private int namSinh;
    private String diaChi;
    private String soDienThoai;
    private String maChucVu;
    private boolean trangThai;
}
