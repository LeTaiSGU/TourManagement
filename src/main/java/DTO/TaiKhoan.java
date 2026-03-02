package DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaiKhoan {
    private String maNhanVien;
    private String maNhomQuyen;
    private String matKhau;
    private Boolean trangThai;
}
