package DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaiKhoan {
    private String tenDangNhap;
    private String maNhomQuyen;
    private String matKhau;
    private Boolean trangThai;
}
