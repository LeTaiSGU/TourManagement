package BUS;

import DAL.ConnectionDAL;
import DAL.ThongKeDAL;
import DTO.ThongKeDTO;
import DTO.ThongKeDoanhThuDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Lớp xử lý nghiệp vụ thống kê.
 *
 * Trách nhiệm:
 * - Gọi ThongKeDAL để lấy raw data từ DB
 * - Tính toán các chỉ số tổng hợp (tổng, trung bình, tăng trưởng, max...)
 * - Cung cấp dữ liệu sạch cho ThongKePanel (GUI) sử dụng trực tiếp
 *
 * KHÔNG chứa bất kỳ logic giao diện nào.
 */
public class ThongKeBUS {

    /**
     * Đối tượng DAL thực hiện truy vấn SQL.
     * Được khởi tạo trong constructor với kết nối DB từ ConnectionDAL.
     */
    private final ThongKeDAL dal;

    /**
     * Constructor mặc định: tự lấy kết nối từ ConnectionDAL.
     * Dùng khi GUI khởi tạo ThongKeBUS() không cần truyền Connection.
     */
    public ThongKeBUS() {
        Connection conn = ConnectionDAL.getConnection();
        this.dal = new ThongKeDAL(conn);
    }

    /**
     * Constructor có tham số: nhận Connection từ bên ngoài.
     * Dùng khi muốn chia sẻ kết nối chung trong ứng dụng.
     *
     * @param conn Kết nối CSDL đã được mở sẵn
     */
    public ThongKeBUS(Connection conn) {
        this.dal = new ThongKeDAL(conn);
    }

    // =========================================================
    // CÁC PHƯƠNG THỨC LẤY DỮ LIỆU (ủy thác xuống DAL)
    // =========================================================

    /**
     * Lấy danh sách doanh thu từng tháng trong năm.
     *
     * @param nam Năm cần thống kê
     * @return Danh sách doanh thu theo tháng (tối đa 12 phần tử)
     */
    public List<ThongKeDoanhThuDTO> getDoanhThuTheoThang(int nam) throws SQLException {
        return dal.getDoanhThuTheoThang(nam);
    }

    /**
     * Lấy doanh thu tổng hợp theo loại tour (cho biểu đồ tròn).
     *
     * @return Danh sách doanh thu theo loại tour
     */
    public List<ThongKeDTO> getDoanhThuTheoLoaiTour() throws SQLException {
        return dal.getDoanhThuTheoLoaiTour();
    }

    /**
     * Lấy top N tour bán chạy nhất theo số vé đã bán.
     *
     * @param topN Số lượng tour cần lấy
     * @return Danh sách tour sắp xếp giảm dần theo số vé
     */
    public List<ThongKeDTO> getTopTourBanChay(int topN) throws SQLException {
        return dal.getTopTourBanChay(topN);
    }

    /**
     * Lấy số lượng khách hàng theo từng loại (VD: VIP, thường...).
     *
     * @return Danh sách phân loại khách hàng
     */
    public List<ThongKeDTO> getKhachHangTheoLoai() throws SQLException {
        return dal.getKhachHangTheoLoai();
    }

    /**
     * Lấy số tour đang hoạt động.
     *
     * @return Số tour đang hoạt động
     */
    public int getSoTourHoatDong() throws SQLException {
        return dal.getSoTourHoatDong();
    }

    /**
     * Lấy danh sách các năm có dữ liệu để điền vào ComboBox.
     *
     * @return Danh sách năm giảm dần
     */
    public List<Integer> getDanhSachNam() throws SQLException {
        return dal.getDanhSachNam();
    }

    // =========================================================
    // CÁC PHƯƠNG THỨC TÍNH TOÁN (logic thuần Java)
    // =========================================================

    /**
     * Tính tổng doanh thu của toàn bộ danh sách.
     *
     * @param danhSach Danh sách doanh thu theo tháng
     * @return Tổng doanh thu (VND)
     */
    public double tinhTongDoanhThu(List<ThongKeDoanhThuDTO> danhSach) {
        return danhSach.stream()
                .mapToDouble(ThongKeDoanhThuDTO::getTongDoanhThu)
                .sum();
    }

    /**
     * Tính tổng số hóa đơn trong danh sách.
     *
     * @param danhSach Danh sách doanh thu theo tháng
     * @return Tổng số hóa đơn
     */
    public int tinhTongHoaDon(List<ThongKeDoanhThuDTO> danhSach) {
        return danhSach.stream()
                .mapToInt(ThongKeDoanhThuDTO::getSoHoaDon)
                .sum();
    }

    /**
     * Tính tổng số vé đã bán trong danh sách.
     *
     * @param danhSach Danh sách doanh thu theo tháng
     * @return Tổng số vé bán được
     */
    public int tinhTongSoVe(List<ThongKeDoanhThuDTO> danhSach) {
        return danhSach.stream()
                .mapToInt(ThongKeDoanhThuDTO::getSoVe)
                .sum();
    }

    /**
     * Tìm tháng có doanh thu cao nhất trong năm.
     *
     * @param danhSach Danh sách doanh thu theo tháng
     * @return Đối tượng ThongKeDoanhThuDTO của tháng đỉnh cao nhất,
     *         hoặc null nếu danh sách rỗng
     */
    public ThongKeDoanhThuDTO getThangDoanhThuCaoNhat(List<ThongKeDoanhThuDTO> danhSach) {
        return danhSach.stream()
                .max((a, b) -> Double.compare(a.getTongDoanhThu(), b.getTongDoanhThu()))
                .orElse(null);
    }

    /**
     * Tính tỉ lệ tăng trưởng doanh thu so với kỳ trước (%).
     *
     * Công thức: ((thangNay - thangTruoc) / thangTruoc) * 100
     *
     * @param thangTruoc Doanh thu kỳ trước (VD: tháng 11)
     * @param thangNay   Doanh thu kỳ này (VD: tháng 12)
     * @return % tăng trưởng (số âm = giảm, số dương = tăng)
     */
    public double tinhTangTruong(double thangTruoc, double thangNay) {
        if (thangTruoc == 0) {
            // Tránh chia cho 0: nếu kỳ trước = 0 mà kỳ này có doanh thu → tăng vô hạn
            return thangNay > 0 ? 100.0 : 0.0;
        }
        return ((thangNay - thangTruoc) / thangTruoc) * 100.0;
    }

    /**
     * Tính doanh thu trung bình theo tháng trong năm.
     *
     * @param danhSach Danh sách doanh thu theo tháng
     * @return Doanh thu trung bình (VND), hoặc 0 nếu danh sách rỗng
     */
    public double tinhDoanhThuTrungBinhThang(List<ThongKeDoanhThuDTO> danhSach) {
        if (danhSach == null || danhSach.isEmpty()) {
            return 0;
        }
        return tinhTongDoanhThu(danhSach) / danhSach.size();
    }

    /**
     * Điền các tháng bị thiếu với giá trị 0 để biểu đồ hiển thị đủ 12 cột.
     *
     * Khi một tháng không có hóa đơn, DB sẽ không trả về row đó.
     * Phương thức này đảm bảo kết quả luôn có đủ 12 điểm dữ liệu.
     *
     * @param danhSach Danh sách nhận được từ DB (có thể thiếu tháng)
     * @param nam      Năm cần điền đủ
     * @return Danh sách 12 phần tử, tháng thiếu có giá trị 0
     */
    public List<ThongKeDoanhThuDTO> dien12Thang(List<ThongKeDoanhThuDTO> danhSach, int nam) {
        List<ThongKeDoanhThuDTO> du12Thang = new java.util.ArrayList<>();

        for (int thang = 1; thang <= 12; thang++) {
            final int t = thang;
            // Tìm xem tháng này đã có trong DB chưa
            ThongKeDoanhThuDTO timThay = danhSach.stream()
                    .filter(d -> d.getThang() == t)
                    .findFirst()
                    .orElse(null);

            if (timThay != null) {
                du12Thang.add(timThay);
            } else {
                // Tháng không có dữ liệu → thêm bản ghi rỗng cho trục X đầy đủ
                du12Thang.add(new ThongKeDoanhThuDTO(nam, thang, 0, 0, 0));
            }
        }
        return du12Thang;
    }
}
