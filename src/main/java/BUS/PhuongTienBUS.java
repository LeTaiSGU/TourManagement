package BUS;

import DAL.ConnectionDAL;
import DAL.PhuongTienDAL;
import DTO.PhuongTien;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Lớp xử lý nghiệp vụ quản lý Phương tiện.
 *
 * Trách nhiệm:
 * - Ủy thác truy vấn dữ liệu xuống PhuongTienDAL
 * - Validate dữ liệu đầu vào (maPT, tenPT không rỗng, độ dài hợp lệ...)
 * - Áp logic nghiệp vụ: kiểm tra trùng mã, kiểm tra FK trước khi xóa
 * - Sinh mã tự động khi tạo mới
 *
 * KHÔNG chứa bất kỳ logic giao diện (Swing) nào.
 */
public class PhuongTienBUS {

    /** DAL thực hiện truy vấn SQL */
    private final PhuongTienDAL dal;

    /** Constructor mặc định: tự lấy kết nối từ ConnectionDAL */
    public PhuongTienBUS() {
        this.dal = new PhuongTienDAL(ConnectionDAL.getConnection());
    }

    /** Constructor nhận Connection từ bên ngoài (dùng khi share kết nối) */
    public PhuongTienBUS(Connection conn) {
        this.dal = new PhuongTienDAL(conn);
    }

    // =========================================================
    // LẤY DỮ LIỆU
    // =========================================================

    /**
     * Lấy toàn bộ danh sách phương tiện, sắp xếp theo maPT tăng dần.
     */
    public List<PhuongTien> getAll() throws SQLException {
        return dal.getAll();
    }

    /**
     * Tìm kiếm phương tiện theo từ khóa (khớp tên hoặc mã).
     * Trả về toàn bộ danh sách nếu từ khóa rỗng.
     *
     * @param tuKhoa Từ khoá tìm kiếm
     */
    public List<PhuongTien> timKiem(String tuKhoa) throws SQLException {
        return dal.timKiem(tuKhoa);
    }

    /**
     * Sinh mã phương tiện mới tự động (PT001, PT002, ...).
     */
    public String sinhMaMoi() throws SQLException {
        return dal.sinhMaMoi();
    }

    // =========================================================
    // NGHIỆP VỤ THÊM MỚI
    // =========================================================

    /**
     * Thêm mới một phương tiện sau khi validate đầy đủ.
     *
     * Validate:
     * - maPT không rỗng, tối đa 5 ký tự
     * - maPT chưa tồn tại trong DB
     * - tenPT không rỗng, tối đa 100 ký tự
     *
     * @param pt Đối tượng PhuongTien cần thêm
     * @throws IllegalArgumentException nếu vi phạm validate
     */
    public void them(PhuongTien pt) throws SQLException {
        // --- Validate ---
        validateCommon(pt);
        if (dal.tonTai(pt.getMaPT())) {
            throw new IllegalArgumentException("Mã phương tiện \"" + pt.getMaPT() + "\" đã tồn tại.");
        }
        // --- Thực thi ---
        int result = dal.them(pt);
        if (result == 0) {
            throw new SQLException("Thêm phương tiện thất bại, không có dòng nào bị ảnh hưởng.");
        }
    }

    // =========================================================
    // NGHIỆP VỤ CẬP NHẬT
    // =========================================================

    /**
     * Cập nhật thông tin phương tiện sau khi validate.
     *
     * Validate:
     * - maPT phải đã tồn tại trong DB
     * - tenPT không rỗng, tối đa 100 ký tự
     *
     * @param pt Đối tượng PhuongTien với dữ liệu mới
     * @throws IllegalArgumentException nếu vi phạm validate
     */
    public void capNhat(PhuongTien pt) throws SQLException {
        // --- Validate ---
        validateCommon(pt);
        if (!dal.tonTai(pt.getMaPT())) {
            throw new IllegalArgumentException("Mã phương tiện \"" + pt.getMaPT() + "\" không tồn tại.");
        }
        // --- Thực thi ---
        int result = dal.capNhat(pt);
        if (result == 0) {
            throw new SQLException("Cập nhật phương tiện thất bại.");
        }
    }

    // =========================================================
    // NGHIỆP VỤ XÓA
    // =========================================================

    /**
     * Xóa một phương tiện sau khi kiểm tra ràng buộc FK.
     *
     * Nếu phương tiện đang được dùng trong LICHTRINH, sẽ đặt
     * trangThai = false (xóa mềm) thay vì xóa cứng, để tránh lỗi FK.
     *
     * @param maPT Mã phương tiện cần xóa
     * @return "XOA_CUNG" nếu đã xóa thật sự,
     *         "XOA_MEM" nếu chỉ vô hiệu hóa (đang dùng trong LICHTRINH)
     */
    public String xoa(String maPT) throws SQLException {
        if (maPT == null || maPT.isBlank()) {
            throw new IllegalArgumentException("Mã phương tiện không được để trống.");
        }
        if (dal.dangDuocSuDung(maPT)) {
            // Không thể xóa cứng → xóa mềm (ẩn khỏi hoạt động)
            dal.doiTrangThai(maPT, false);
            return "XOA_MEM";
        }
        dal.xoa(maPT);
        return "XOA_CUNG";
    }

    // =========================================================
    // VALIDATE CHUNG
    // =========================================================

    /**
     * Kiểm tra các ràng buộc chung cho cả thêm và cập nhật.
     *
     * @throws IllegalArgumentException mô tả lỗi để hiển thị trong UI
     */
    private void validateCommon(PhuongTien pt) {
        if (pt == null) {
            throw new IllegalArgumentException("Dữ liệu phương tiện không được null.");
        }
        if (pt.getMaPT() == null || pt.getMaPT().isBlank()) {
            throw new IllegalArgumentException("Mã phương tiện không được để trống.");
        }
        if (pt.getMaPT().length() > 5) {
            throw new IllegalArgumentException("Mã phương tiện tối đa 5 ký tự.");
        }
        if (pt.getTenPT() == null || pt.getTenPT().isBlank()) {
            throw new IllegalArgumentException("Tên phương tiện không được để trống.");
        }
        if (pt.getTenPT().length() > 100) {
            throw new IllegalArgumentException("Tên phương tiện tối đa 100 ký tự.");
        }
        if (pt.getMoTa() != null && pt.getMoTa().length() > 255) {
            throw new IllegalArgumentException("Mô tả tối đa 255 ký tự.");
        }
    }
}
