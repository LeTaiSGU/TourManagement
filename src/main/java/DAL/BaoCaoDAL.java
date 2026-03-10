package DAL;

import DTO.BaoCaoDoanhThuRowDTO;
import DTO.BaoCaoFilterDTO;
import DTO.BaoCaoKhachHangRowDTO;
import DTO.BaoCaoNhanVienRowDTO;
import DTO.BaoCaoTourRowDTO;
import DTO.BaoCaoVanHanhRowDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * DAL cho toàn bộ module Thống kê & Báo cáo.
 *
 * Mỗi phương thức nhận {@link BaoCaoFilterDTO} để xây dựng câu SQL động.
 * PreparedStatement được dùng xuyên suốt để tránh SQL Injection.
 *
 * Schema liên quan:
 * HOADON → maHoaDon, maNhanVien, maKhachHang, ngayLapHD, soLuongVe, tongTien,
 * trangThaiHD
 * CTHD → maHoaDon, maTour
 * TOUR → maTour, maLoaiTour, maHDV, tenTour, giaTour, soLuongVe, soLuongMin,
 * trangThai, khoiHanh, noiKhoiHanh
 * LOAITOUR → maLoaiTour, tenLoai
 * NHANVIEN → maNhanVien, tenNhanVien, maChucVu
 * CHUCVU → maChucVu, tenChucVu
 * KHACHHANG → maKhachHang, tenKhachHang, maLoaiKH, gioiTinh, namSinh, diaChi
 * LOAIKHACHHANG → maLoaiKH, tenLoaiKH
 * LICHTRINH → maLichTrinh, maTour, maDiaDiem, maPT
 * PHUONGTIEN → maPT, tenPT
 * HUONGDANVIEN → maHDV, tenHDV, gioiTinh, chuyenMon
 * DIADIEM → maDiaDiem, tenDiaDiem
 */
public class BaoCaoDAL {

    private final Connection conn;

    public BaoCaoDAL(Connection conn) {
        this.conn = conn;
    }

    // =========================================================
    // TIỆN ÍCH NỘI BỘ — xây dựng điều kiện WHERE động
    // =========================================================

    /**
     * Thêm điều kiện thời gian vào query HOADON dựa theo filter.
     * Ưu tiên: nam > (tuNgay & denNgay)
     */
    private void apDungFilterThoiGian(StringBuilder sql, BaoCaoFilterDTO f,
            List<Object> params, String alias) {
        String col = (alias != null ? alias + "." : "") + "ngayLapHD";
        if (f.getNam() != null) {
            sql.append(" AND YEAR(").append(col).append(") = ?");
            params.add(f.getNam());
        } else {
            if (f.getTuNgay() != null) {
                sql.append(" AND ").append(col).append(" >= ?");
                params.add(Date.valueOf(f.getTuNgay()));
            }
            if (f.getDenNgay() != null) {
                sql.append(" AND ").append(col).append(" <= ?");
                params.add(Date.valueOf(f.getDenNgay()));
            }
        }
    }

    /**
     * Điền tham số vào PreparedStatement từ danh sách params.
     */
    private PreparedStatement buildPs(String sql, List<Object> params) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < params.size(); i++) {
            Object p = params.get(i);
            if (p instanceof Integer)
                ps.setInt(i + 1, (Integer) p);
            else if (p instanceof Long)
                ps.setLong(i + 1, (Long) p);
            else if (p instanceof Double)
                ps.setDouble(i + 1, (Double) p);
            else if (p instanceof Boolean)
                ps.setBoolean(i + 1, (Boolean) p);
            else if (p instanceof Date)
                ps.setDate(i + 1, (Date) p);
            else
                ps.setString(i + 1, p == null ? null : p.toString());
        }
        return ps;
    }

    // =========================================================
    // DOANH THU THEO THÁNG
    // SELECT tháng, tổng doanh thu, số hóa đơn, số vé
    // trong năm/khoảng thời gian được chọn
    // =========================================================

    /**
     * Doanh thu theo từng tháng. Luôn trả về đủ 12 tháng (tháng thiếu = 0).
     */
    public List<BaoCaoDoanhThuRowDTO> getDoanhThuTheoThang(BaoCaoFilterDTO filter)
            throws SQLException {

        // Xác định năm muốn xem
        int nam = filter.getNam() != null ? filter.getNam() : LocalDate.now().getYear();

        String sql = """
                SELECT MONTH(HD.ngayLapHD)        AS thang,
                       SUM(HD.tongTien)            AS tongDoanhThu,
                       COUNT(DISTINCT HD.maHoaDon) AS soHoaDon,
                       COALESCE(SUM(ct_agg.soVe), 0) AS soVe
                FROM   HOADON HD
                LEFT JOIN (SELECT maHoaDon, SUM(soLuongVe) AS soVe FROM CTHD GROUP BY maHoaDon) ct_agg
                       ON HD.maHoaDon = ct_agg.maHoaDon
                WHERE  HD.trangThaiTT = 1
                  AND  YEAR(HD.ngayLapHD) = ?
                GROUP  BY MONTH(HD.ngayLapHD)
                ORDER  BY thang
                """;

        Map<Integer, BaoCaoDoanhThuRowDTO> map = new LinkedHashMap<>();
        for (int i = 1; i <= 12; i++) {
            map.put(i, BaoCaoDoanhThuRowDTO.builder()
                    .nhan("Tháng " + i).ma(String.valueOf(i))
                    .build());
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nam);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int thang = rs.getInt("thang");
                    map.put(thang, BaoCaoDoanhThuRowDTO.builder()
                            .nhan("Tháng " + thang)
                            .ma(String.valueOf(thang))
                            .tongDoanhThu(rs.getDouble("tongDoanhThu"))
                            .soHoaDon(rs.getInt("soHoaDon"))
                            .soVe(rs.getInt("soVe"))
                            .build());
                }
            }
        }
        return new ArrayList<>(map.values());
    }

    // =========================================================
    // DOANH THU THEO TOUR
    // =========================================================

    public List<BaoCaoDoanhThuRowDTO> getDoanhThuTheoTour(BaoCaoFilterDTO filter)
            throws SQLException {

        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                SELECT T.maTour, T.tenTour,
                       COUNT(DISTINCT HD.maHoaDon) AS soHoaDon,
                       COALESCE(SUM(C.soLuongVe), 0)  AS soVe,
                       COALESCE(SUM(HD.tongTien),  0)  AS tongDoanhThu
                FROM   TOUR T
                LEFT   JOIN CTHD C ON T.maTour = C.maTour
                LEFT   JOIN HOADON HD ON C.maHoaDon = HD.maHoaDon
                       AND HD.trangThaiTT = 1
                WHERE  1 = 1
                """);

        if (filter.getMaLoaiTour() != null && !filter.getMaLoaiTour().isBlank()) {
            sql.append(" AND T.maLoaiTour = ?");
            params.add(filter.getMaLoaiTour());
        }
        if (filter.getTrangThaiTour() != null) {
            sql.append(" AND T.trangThai = ?");
            params.add(filter.getTrangThaiTour());
        }
        apDungFilterThoiGian(sql, filter, params, "HD");

        sql.append(" GROUP BY T.maTour, T.tenTour ORDER BY tongDoanhThu DESC");

        List<BaoCaoDoanhThuRowDTO> result = new ArrayList<>();
        try (PreparedStatement ps = buildPs(sql.toString(), params);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(BaoCaoDoanhThuRowDTO.builder()
                        .ma(rs.getString("maTour"))
                        .nhan(rs.getString("tenTour"))
                        .soHoaDon(rs.getInt("soHoaDon"))
                        .soVe(rs.getInt("soVe"))
                        .tongDoanhThu(rs.getDouble("tongDoanhThu"))
                        .build());
            }
        }
        return result;
    }

    // =========================================================
    // DOANH THU THEO LOẠI TOUR
    // =========================================================

    public List<BaoCaoDoanhThuRowDTO> getDoanhThuTheoLoaiTour(BaoCaoFilterDTO filter)
            throws SQLException {

        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                SELECT LT.maLoaiTour, LT.tenLoai AS tenLoaiTour,
                       COUNT(DISTINCT HD.maHoaDon) AS soHoaDon,
                       COALESCE(SUM(C.soLuongVe), 0) AS soVe,
                       COALESCE(SUM(HD.tongTien),  0) AS tongDoanhThu
                FROM   LOAITOUR LT
                LEFT   JOIN TOUR T ON LT.maLoaiTour = T.maLoaiTour
                LEFT   JOIN CTHD C ON T.maTour = C.maTour
                LEFT   JOIN HOADON HD ON C.maHoaDon = HD.maHoaDon
                       AND HD.trangThaiTT = 1
                WHERE  1 = 1
                """);

        apDungFilterThoiGian(sql, filter, params, "HD");

        sql.append(" GROUP BY LT.maLoaiTour, LT.tenLoai ORDER BY tongDoanhThu DESC");

        List<BaoCaoDoanhThuRowDTO> result = new ArrayList<>();
        try (PreparedStatement ps = buildPs(sql.toString(), params);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(BaoCaoDoanhThuRowDTO.builder()
                        .ma(rs.getString("maLoaiTour"))
                        .nhan(rs.getString("tenLoaiTour"))
                        .soHoaDon(rs.getInt("soHoaDon"))
                        .soVe(rs.getInt("soVe"))
                        .tongDoanhThu(rs.getDouble("tongDoanhThu"))
                        .build());
            }
        }
        return result;
    }

    // =========================================================
    // DOANH THU THEO NHÂN VIÊN
    // =========================================================

    public List<BaoCaoNhanVienRowDTO> getDoanhThuTheoNhanVien(BaoCaoFilterDTO filter)
            throws SQLException {

        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                SELECT NV.maNhanVien, NV.tenNhanVien,
                       COALESCE(CV.tenChucVu, N'Chưa phân công') AS tenChucVu,
                       COUNT(DISTINCT HD.maHoaDon)                AS soHoaDon,
                       COUNT(DISTINCT C.maTour)                   AS soTourBan,
                       COALESCE(SUM(C.soLuongVe), 0)             AS soVe,
                       COALESCE(SUM(HD.tongTien),  0)             AS tongDoanhThu
                FROM   NHANVIEN NV
                LEFT   JOIN CHUCVU CV  ON NV.maChucVu  = CV.maChucVu
                LEFT   JOIN HOADON HD  ON NV.maNhanVien = HD.maNhanVien
                       AND HD.trangThaiTT = 1
                LEFT   JOIN CTHD C     ON HD.maHoaDon   = C.maHoaDon
                WHERE  1 = 1
                """);

        if (filter.getMaNhanVien() != null && !filter.getMaNhanVien().isBlank()) {
            sql.append(" AND NV.maNhanVien = ?");
            params.add(filter.getMaNhanVien());
        }
        apDungFilterThoiGian(sql, filter, params, "HD");

        sql.append("""
                 GROUP BY NV.maNhanVien, NV.tenNhanVien, CV.tenChucVu
                 ORDER BY tongDoanhThu DESC
                """);

        List<BaoCaoNhanVienRowDTO> result = new ArrayList<>();
        try (PreparedStatement ps = buildPs(sql.toString(), params);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(BaoCaoNhanVienRowDTO.builder()
                        .maNhanVien(rs.getString("maNhanVien"))
                        .tenNhanVien(rs.getString("tenNhanVien"))
                        .tenChucVu(rs.getString("tenChucVu"))
                        .soHoaDon(rs.getInt("soHoaDon"))
                        .soTourBan(rs.getInt("soTourBan"))
                        .soVe(rs.getInt("soVe"))
                        .tongDoanhThu(rs.getDouble("tongDoanhThu"))
                        .build());
            }
        }
        return result;
    }

    // =========================================================
    // BÁO CÁO TOUR — hiệu quả hoạt động từng tour
    // =========================================================

    public List<BaoCaoTourRowDTO> getThongKeTour(BaoCaoFilterDTO filter)
            throws SQLException {

        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                SELECT T.maTour, T.tenTour, T.noiKhoiHanh,
                       COALESCE(LT.tenLoai, '') AS tenLoaiTour,
                       T.soLuongVe              AS soLuongToiDa,
                       T.trangThai, T.khoiHanh,
                       COALESCE(SUM(C.soLuongVe), 0) AS soVeDaBan,
                       COALESCE(SUM(HD.tongTien),  0) AS doanhThu
                FROM   TOUR T
                LEFT   JOIN LOAITOUR LT ON T.maLoaiTour = LT.maLoaiTour
                LEFT   JOIN CTHD C ON T.maTour = C.maTour
                LEFT   JOIN HOADON HD ON C.maHoaDon = HD.maHoaDon
                       AND HD.trangThaiTT = 1
                WHERE  1 = 1
                """);

        if (filter.getMaLoaiTour() != null && !filter.getMaLoaiTour().isBlank()) {
            sql.append(" AND T.maLoaiTour = ?");
            params.add(filter.getMaLoaiTour());
        }
        if (filter.getTrangThaiTour() != null) {
            sql.append(" AND T.trangThai = ?");
            params.add(filter.getTrangThaiTour());
        }
        apDungFilterThoiGian(sql, filter, params, "HD");

        sql.append(
                " GROUP BY T.maTour, T.tenTour, T.noiKhoiHanh, LT.tenLoai, T.soLuongVe, T.trangThai, T.khoiHanh ORDER BY doanhThu DESC");

        List<BaoCaoTourRowDTO> result = new ArrayList<>();
        try (PreparedStatement ps = buildPs(sql.toString(), params);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int toiDa = rs.getInt("soLuongToiDa");
                int daBan = rs.getInt("soVeDaBan");
                double tyLe = toiDa > 0 ? daBan * 100.0 / toiDa : 0;
                result.add(BaoCaoTourRowDTO.builder()
                        .maTour(rs.getString("maTour"))
                        .tenTour(rs.getString("tenTour"))
                        .tenLoaiTour(rs.getString("tenLoaiTour"))
                        .noiKhoiHanh(rs.getString("noiKhoiHanh"))
                        .soLuongToiDa(toiDa)
                        .soVeDaBan(daBan)
                        .tyLeLapDay(tyLe)
                        .doanhThu(rs.getDouble("doanhThu"))
                        .trangThai(rs.getBoolean("trangThai"))
                        .khoiHanh(rs.getBoolean("khoiHanh"))
                        .build());
            }
        }
        return result;
    }

    // =========================================================
    // BÁO CÁO KHÁCH HÀNG — phân loại
    // =========================================================

    /** Phân loại khách hàng theo nhóm (LOAIKHACHHANG) */
    public List<BaoCaoKhachHangRowDTO> getKhachTheoLoai() throws SQLException {
        String sql = """
                SELECT LKH.maLoaiKH, LKH.tenLoaiKH,
                       COUNT(KH.maKhachHang) AS soLuong
                FROM   LOAIKHACHHANG LKH
                LEFT   JOIN KHACHHANG KH ON LKH.maLoaiKH = KH.maLoaiKH
                GROUP  BY LKH.maLoaiKH, LKH.tenLoaiKH
                ORDER  BY soLuong DESC
                """;
        List<BaoCaoKhachHangRowDTO> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(BaoCaoKhachHangRowDTO.builder()
                        .maKhachHang(rs.getString("maLoaiKH"))
                        .nhan(rs.getString("tenLoaiKH"))
                        .soLuong(rs.getInt("soLuong"))
                        .build());
            }
        }
        return result;
    }

    /** Phân bổ khách hàng theo giới tính */
    public List<BaoCaoKhachHangRowDTO> getKhachTheoGioiTinh() throws SQLException {
        String sql = """
                SELECT COALESCE(gioiTinh, N'Không rõ') AS nhan,
                       COUNT(*) AS soLuong
                FROM   KHACHHANG
                GROUP  BY gioiTinh
                """;
        List<BaoCaoKhachHangRowDTO> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(BaoCaoKhachHangRowDTO.builder()
                        .nhan(rs.getString("nhan"))
                        .soLuong(rs.getInt("soLuong"))
                        .build());
            }
        }
        return result;
    }

    /** Top khách hàng chi tiêu cao nhất */
    public List<BaoCaoKhachHangRowDTO> getTopKhachChiTieu(int topN, BaoCaoFilterDTO filter)
            throws SQLException {

        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                SELECT TOP(?) KH.maKhachHang, KH.tenKhachHang AS nhan,
                       COUNT(DISTINCT HD.maHoaDon) AS soHoaDon,
                       COALESCE(SUM(ct_agg.soVe), 0) AS soLuong,
                       SUM(HD.tongTien)            AS tongChiTieu
                FROM   KHACHHANG KH
                JOIN   HOADON HD ON KH.maKhachHang = HD.maKhachHang
                       AND HD.trangThaiTT = 1
                LEFT JOIN (SELECT maHoaDon, SUM(soLuongVe) AS soVe FROM CTHD GROUP BY maHoaDon) ct_agg
                       ON HD.maHoaDon = ct_agg.maHoaDon
                WHERE  1 = 1
                """);;
        params.add(topN);
        apDungFilterThoiGian(sql, filter, params, "HD");

        sql.append(" GROUP BY KH.maKhachHang, KH.tenKhachHang ORDER BY tongChiTieu DESC");

        List<BaoCaoKhachHangRowDTO> result = new ArrayList<>();
        try (PreparedStatement ps = buildPs(sql.toString(), params);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(BaoCaoKhachHangRowDTO.builder()
                        .maKhachHang(rs.getString("maKhachHang"))
                        .nhan(rs.getString("nhan"))
                        .soHoaDon(rs.getInt("soHoaDon"))
                        .soLuong(rs.getInt("soLuong"))
                        .tongChiTieu(rs.getDouble("tongChiTieu"))
                        .build());
            }
        }
        return result;
    }

    /** Tất cả khách hàng với số lần đặt tour và chi tiêu */
    public List<BaoCaoKhachHangRowDTO> getAllKhachHangChiTiet(BaoCaoFilterDTO filter)
            throws SQLException {

        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                SELECT KH.maKhachHang,
                       KH.tenKhachHang AS nhan,
                       COALESCE(LKH.tenLoaiKH, N'Chưa phân loại') AS tenLoaiKH,
                       COUNT(DISTINCT HD.maHoaDon) AS soHoaDon,
                       COALESCE(SUM(ct_agg.soVe), 0) AS soLuong,
                       COALESCE(SUM(HD.tongTien),  0) AS tongChiTieu
                FROM   KHACHHANG KH
                LEFT   JOIN LOAIKHACHHANG LKH ON KH.maLoaiKH = LKH.maLoaiKH
                LEFT   JOIN HOADON HD ON KH.maKhachHang = HD.maKhachHang
                       AND HD.trangThaiTT = 1
                LEFT JOIN (SELECT maHoaDon, SUM(soLuongVe) AS soVe FROM CTHD GROUP BY maHoaDon) ct_agg
                       ON HD.maHoaDon = ct_agg.maHoaDon
                WHERE  1 = 1
                """);

        apDungFilterThoiGian(sql, filter, params, "HD");
        sql.append("""
                 GROUP BY KH.maKhachHang, KH.tenKhachHang, LKH.tenLoaiKH
                 ORDER BY tongChiTieu DESC
                """);

        List<BaoCaoKhachHangRowDTO> result = new ArrayList<>();
        try (PreparedStatement ps = buildPs(sql.toString(), params);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(BaoCaoKhachHangRowDTO.builder()
                        .maKhachHang(rs.getString("maKhachHang"))
                        .nhan(rs.getString("nhan") + " (" + rs.getString("tenLoaiKH") + ")")
                        .soHoaDon(rs.getInt("soHoaDon"))
                        .soLuong(rs.getInt("soLuong"))
                        .tongChiTieu(rs.getDouble("tongChiTieu"))
                        .build());
            }
        }
        return result;
    }

    // =========================================================
    // BÁO CÁO VẬN HÀNH — phương tiện & hướng dẫn viên
    // =========================================================

    /** Số lịch trình sử dụng mỗi phương tiện */
    public List<BaoCaoVanHanhRowDTO> getLichTrinhTheoPhuongTien() throws SQLException {
        String sql = """
                SELECT PT.maPT, PT.tenPT AS nhan,
                       COUNT(LT.maLichTrinh) AS soLuong
                FROM   PHUONGTIEN PT
                LEFT   JOIN LICHTRINH LT ON PT.maPT = LT.maPT
                GROUP  BY PT.maPT, PT.tenPT
                ORDER  BY soLuong DESC
                """;
        List<BaoCaoVanHanhRowDTO> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(BaoCaoVanHanhRowDTO.builder()
                        .ma(rs.getString("maPT"))
                        .nhan(rs.getString("nhan"))
                        .soLuong(rs.getInt("soLuong"))
                        .build());
            }
        }
        return result;
    }

    /** Số tour mỗi hướng dẫn viên đã dẫn (khoiHanh = 1) */
    public List<BaoCaoVanHanhRowDTO> getTourTheoHuongDanVien() throws SQLException {
        String sql = """
                SELECT HDV.maHDV, HDV.tenHDV AS nhan,
                       HDV.chuyenMon         AS thongTinThem,
                       COUNT(T.maTour)        AS soLuong
                FROM   HUONGDANVIEN HDV
                LEFT   JOIN TOUR T ON HDV.maHDV = T.maHDV
                GROUP  BY HDV.maHDV, HDV.tenHDV, HDV.chuyenMon
                ORDER  BY soLuong DESC
                """;
        List<BaoCaoVanHanhRowDTO> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(BaoCaoVanHanhRowDTO.builder()
                        .ma(rs.getString("maHDV"))
                        .nhan(rs.getString("nhan"))
                        .thongTinThem(rs.getString("thongTinThem"))
                        .soLuong(rs.getInt("soLuong"))
                        .build());
            }
        }
        return result;
    }

    // =========================================================
    // THỐNG KÊ TỔNG HỢP cho filter
    // =========================================================

    /** Lấy danh sách năm có dữ liệu từ HOADON */
    public List<Integer> getDanhSachNam() throws SQLException {
        List<Integer> result = new ArrayList<>();
        String sql = "SELECT DISTINCT YEAR(ngayLapHD) AS nam FROM HOADON ORDER BY nam DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                result.add(rs.getInt("nam"));
        }
        if (result.isEmpty())
            result.add(LocalDate.now().getYear());
        return result;
    }

    /** Lấy danh sách loại tour cho ComboBox lọc */
    public Map<String, String> getDanhSachLoaiTour() throws SQLException {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("", "-- Tất cả loại tour --");
        String sql = "SELECT maLoaiTour, tenLoai FROM LOAITOUR ORDER BY tenLoai";
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                result.put(rs.getString("maLoaiTour"), rs.getString("tenLoai"));
        }
        return result;
    }

    /** Lấy danh sách nhân viên cho ComboBox lọc */
    public Map<String, String> getDanhSachNhanVien() throws SQLException {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("", "-- Tất cả nhân viên --");
        String sql = "SELECT maNhanVien, tenNhanVien FROM NHANVIEN WHERE trangThai = 1 ORDER BY tenNhanVien";
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                result.put(rs.getString("maNhanVien"), rs.getString("tenNhanVien"));
        }
        return result;
    }
}
