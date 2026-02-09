package DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhomQuyen {
    private String maNhomQuyen;
    private String tenNhomQuyen;
    private String moTa;
}
