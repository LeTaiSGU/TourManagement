package DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichTrinh {
    private String maLichTrinh;
    private String maTour;
    private String maDiaDiem;
    private String maPT;
    private int ngayThu;
    private String noiDung;
    private Boolean trangThai;
}
