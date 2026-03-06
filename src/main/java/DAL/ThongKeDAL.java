package DAL;

import DTO.ThongKeDTO;
import DTO.ThongKeDoanhThuDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp truy vấn dữ liệu thống kê từ SQL Server.
 *
 * Tất cả các câu truy vấn ở đây đều JOIN nhiều bảng và
 * tính toán tổng hợp (SUM, COUNT, GROUP BY) trực tiếp trên DB
 * để đảm bảo hiệu quả, thay vì kéo raw data về rồi tính ở Java.
 */
public class ThongKeDAL {

    /**
     * Kết nối CSDL được truyền vào từ BUS,
     * lấy từ ConnectionDAL.getConnection().
     */
    private final Connection conn;

    public ThongKeDAL(Connection conn) {
        this.conn = conn;
    }

    // =========================================================
    // 1. DOANH THU THEO THÁNG
    // =========================================================

    /**
     * Trả về danh sách doanh thu theo từng tháng trong năm được chỉ định.
     *
     * Chỉ tính hóa đơn có trangThaiHD = 1 (đã thanh toán/hợp lệ).
     * Kết quả được sắp xếp theo tháng tăng dần.
     *
     * @param nam Năm cần thống kê (VD: 2025)
     * @return Danh sách ThongKeDoanhThuDTO — mỗi phần tử = 1 tháng
     */
    public List<ThongKeDoanhThuDTO> getDoanhThuTheoThang(int nam) throws SQLException {
        List<ThongKeDoanhThuDTO> ketQua = new ArrayList<>();

        String sql = """
                SELECT
                    MONTH(ngayLapHD)     AS thang,
                    YEAR(ngayLapHD)      AS nam,
                    SUM(tongTien)        AS tongDoanhThu,
                    COUNT(maHoaDon)      AS soHoaDon,
                    SUM(soLuongVe)       AS soVe
                FROM HOADON
                WHERE trangThaiHD = 1
                  AND YEAR(ngayLapHD) = ?
                GROUP BY YEAR(ngayLapHD), MONTH(ngayLapHD)
                ORDER BY thang
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nam);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ketQua.add(new ThongKeDoanhThuDTO(
                            rs.getInt("nam"),
                            rs.getInt("thang"),
                            rs.getDouble("tongDoanhThu"),
                            rs.getInt("soHoaDon"),
                            rs.getInt("soVe")));
                }
            }
        }
        return ketQua;
    }

    // =========================================================
    // 2. DOANH THU THEO LOẠI TOUR (Biểu đồ tròn)
    // =========================================================

    /**
     * Trả về doanh thu tổng hợp theo từng loại tour.
     *
     * Đường JOIN: HOADON → CTHD → TOUR → LOAITOUR
     *
     * @return Danh sách ThongKeDTO — mỗi phần tử = 1 loại tour
     */
    public List<ThongKeDTO> getDoanhThuTheoLoaiTour() throws SQLException {
        List<ThongKeDTO> ketQua = new ArrayList<>();

        String sql = """
                SELECT
                    lt.tenLoai            AS nhan,
                    SUM(hd.tongTien)      AS giaTri,
                    SUM(hd.soLuongVe)     AS soLuong
                FROM HOADON hd
                JOIN CTHD     ct  ON hd.maHoaDon    = ct.maHoaDon
                JOIN TOUR     t   ON ct.maTour       = t.maTour
                JOIN LOAITOUR lt  ON t.maLoaiTour    = lt.maLoaiTour
                WHERE hd.trangThaiHD = 1
                GROUP BY lt.tenLoai
                ORDER BY giaTri DESC
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ketQua.add(new ThongKeDTO(
                        rs.getString("nhan"),
                        rs.getDouble("giaTri"),
                        rs.getInt("soLuong")));
            }
        }
        return ketQua;
    }

    // =========================================================
    // 3. TOP N TOUR BÁN CHẠY (Biểu đồ cột ngang)
    // =========================================================

    /**
     * Trả về N tour có số vé bán nhiều nhất.
     *
     * @param topN Số lượng tour muốn lấy (thường là 5 hoặc 10)
     * @return Danh sách ThongKeDTO sắp xếp theo soLuong giảm dần
     */
    public List<ThongKeDTO> getTopTourBanChay(int topN) throws SQLException {
        List<ThongKeDTO> ketQua = new ArrayList<>();

        // SQL Server dùng TOP(?), không dùng LIMIT
        String sql = """
                SELECT TOP (?)
                    t.tenTour            AS nhan,
                    SUM(hd.soLuongVe)    AS soLuong,
                    SUM(hd.tongTien)     AS giaTri
                FROM HOADON hd
                JOIN CTHD ct ON hd.maHoaDon = ct.maHoaDon
                JOIN TOUR  t  ON ct.maTour   = t.maTour
                WHERE hd.trangThaiHD = 1
                GROUP BY t.tenTour
                ORDER BY soLuong DESC
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, topN);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ketQua.add(new ThongKeDTO(
                            rs.getString("nhan"),
                            rs.getDouble("giaTri"),
                            rs.getInt("soLuong")));
                }
            }
        }
        return ketQua;
    }

    // =========================================================
    // 4. PHÂN LOẠI KHÁCH HÀNG (Biểu đồ tròn)
    // =========================================================

    /**
     * Trả về số lượng khách hàng theo từng loại (VIP, thường...).
     *
     * Đường JOIN: KHACHHANG → LOAIKHACHHANG
     *
     * @return Danh sách ThongKeDTO — mỗi phần tử = 1 loại KH
     */
    public List<ThongKeDTO> getKhachHangTheoLoai() throws SQLException {
        List<ThongKeDTO> ketQua = new ArrayList<>();

        String sql = """
                SELECT
                    lkh.tenLoaiKH              AS nhan,
                    COUNT(kh.maKhachHang)      AS soLuong,
                    0.0                        AS giaTri
                FROM KHACHHANG     kh
                JOIN LOAIKHACHHANG lkh ON kh.maLoaiKH = lkh.maLoaiKH
                WHERE kh.trangThai = 1
                GROUP BY lkh.tenLoaiKH
                ORDER BY soLuong DESC
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ketQua.add(new ThongKeDTO(
                        rs.getString("nhan"),
                        rs.getDouble("giaTri"),
                        rs.getInt("soLuong")));
            }
        }
        return ketQua;
    }

    // =========================================================
    // 5. SỐ TOUR ĐANG HOẠT ĐỘNG
    // =========================================================

    /**
     * Đếm số tour đang ở trạng thái hoạt động (trangThai = 1).
     * Dùng để hiển thị KPI card "Tour hoạt động".
     *
     * @return Số tour đang hoạt động
     */
    public int getSoTourHoatDong() throws SQLException {
        String sql = "SELECT COUNT(maTour) AS soTour FROM TOUR WHERE trangThai = 1";
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("soTour");
            }
        }
        return 0;
    }

    // =========================================================
    // 6. DANH SÁCH NĂM CÓ DỮ LIỆU (cho ComboBox chọn năm)
    // =========================================================

    /**
     * Trả về danh sách các năm đã có hóa đơn trong CSDL.
     * Dùng để điền vào ComboBox chọn năm thống kê.
     *
     * @return Danh sách năm (VD: [2023, 2024, 2025])
     */
    public List<Integer> getDanhSachNam() throws SQLException {
        List<Integer> danhSachNam = new ArrayList<>();

        String sql = """
                SELECT DISTINCT YEAR(ngayLapHD) AS nam
                FROM HOADON
                WHERE trangThaiHD = 1
                ORDER BY nam DESC
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                danhSachNam.add(rs.getInt("nam"));
            }
        }

        // Đảm bảo luôn có ít nhất năm hiện tại
        if (danhSachNam.isEmpty()) {
            danhSachNam.add(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR));
        }
        return danhSachNam;
    }
}
