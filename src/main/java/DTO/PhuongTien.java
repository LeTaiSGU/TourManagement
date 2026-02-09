package DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhuongTien {
    private String maPT;
    private String tenPT;
    private String moTa;
    private Boolean trangThai;
}
