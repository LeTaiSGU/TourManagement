package DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class DiaDiem {
    private String maDiaDiem;
    private String tenDiaDiem;
    private String anhDiaDiem;
    private String quocGia;
    private String moTa;
    private Boolean trangThai;
}
