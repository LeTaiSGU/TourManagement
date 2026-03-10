package DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhachHangDTO {

    private String maKhachHang;
    private String tenKhachHang;
    private String gioiTinh;
    private int namSinh;
    private String diaChi;
    private String soDienThoai;
    private String maLoaiKH;
    private String email;
    //private Boolean trangThai;
}
