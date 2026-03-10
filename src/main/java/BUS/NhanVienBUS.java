package BUS;

import DAL.ChucVuDAL;
import DAL.ConnectionDAL;
import DAL.NhanVienDAL;
import DAL.NhomQuyenDAL;
import DAL.TaiKhoanDAL;
import DTO.ChucVu;
import DTO.NhanVien;
import DTO.NhomQuyen;
import DTO.TaiKhoan;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Xử lý nghiệp vụ quản lý Nhân viên.
 *
 * Điểm đặc biệt:
 * - Khi THÊM mới nhân viên, đồng thời tạo TAIKHOAN với:
 * tenDangNhap = maNhanVien
 * matKhau = "123123" (mặc định)
 * maNhomQuyen = tra tự động từ tên chức vụ
 * - Cả hai thao tác NHANVIEN + TAIKHOAN được thực hiện trong 1 transaction.
 * - Khi CẬP NHẬT, nếu chức vụ đổi thì cập nhật maNhomQuyen trong TAIKHOAN.
 * - Khi XÓA: nếu NV đang có trong HOADON → xóa mềm; ngược lại xóa cứng (xóa TK
 * trước).
 */
public class NhanVienBUS {

    private static final String MAT_KHAU_MAC_DINH = "123123";

    // =========================================================
    // KHỞI TẠO
    // =========================================================

    public NhanVienBUS() {
        // Kết nối lấy theo yêu cầu trong từng phương thức
    }

    // =========================================================
    // LẤY DỮ LIỆU
    // =========================================================

    /** Toàn bộ danh sách nhân viên. */
    public List<NhanVien> getAll() throws SQLException {
        Connection conn = new ConnectionDAL().getConnection();
        return new NhanVienDAL(conn).getAll();
    }

    /** Tìm kiếm theo từ khoá. */
    public List<NhanVien> timKiem(String tuKhoa) throws SQLException {
        Connection conn = new ConnectionDAL().getConnection();
        return new NhanVienDAL(conn).timKiem(tuKhoa);
    }

    /** Danh sách chức vụ (dùng để đổ ComboBox trên Panel). */
    public List<ChucVu> getDanhSachChucVu() throws SQLException {
        Connection conn = new ConnectionDAL().getConnection();
        return new ChucVuDAL(conn).getAll();
    }

    /**
     * Tìm NhomQuyen phù hợp nhất với tên chức vụ bằng cách query DB.
     * So sánh tenChucVu với tenNhomQuyen (không phân biệt hoa thường, bỏ dấu).
     * Nếu không tìm thấy, trả về nhóm quyền cuối cùng trong danh sách.
     */
    public NhomQuyen timNhomQuyenChoChucVu(String tenChucVu) throws SQLException {
        try {
            java.util.List<NhomQuyen> danhSach = new NhomQuyenDAL().getAllNhomQuyen();
            if (danhSach == null || danhSach.isEmpty()) return null;
            if (tenChucVu == null) return danhSach.get(danhSach.size() - 1);
            String normalized = removeAccents(tenChucVu.toLowerCase());
            // Ưu tiên so khớp chứa tên
            for (NhomQuyen nq : danhSach) {
                String nqNorm = removeAccents(nq.getTenNhomQuyen().toLowerCase());
                if (normalized.contains(nqNorm) || nqNorm.contains(normalized)) {
                    return nq;
                }
            }
            // Fallback: nhóm cuối (ít quyền nhất)
            return danhSach.get(danhSach.size() - 1);
        } catch (Exception e) {
            return null;
        }
    }

    /** Bỏ dấu tiếng Việt để so sánh tên không phân biệt dấu. */
    private String removeAccents(String s) {
        String result = java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD);
        return result.replaceAll("\\p{M}", "");
    }

    /** Sinh mã nhân viên mới tự động (NV001, NV002, …). */
    public String sinhMaMoi() throws SQLException {
        Connection conn = new ConnectionDAL().getConnection();
        return new NhanVienDAL(conn).sinhMaMoi();
    }

    // =========================================================
    // NGHIỆP VỤ THÊM MỚI
    // =========================================================

    /**
     * Thêm nhân viên + tạo tài khoản trong 1 transaction.
     *
     * @param nv        Thông tin nhân viên
     * @param tenChucVu Tên chức vụ đã chọn (để tra nhóm quyền)
     * @throws IllegalArgumentException nếu validate thất bại
     * @throws SQLException             nếu lỗi DB
     */
    public void them(NhanVien nv, String tenChucVu) throws SQLException {
        validateCommon(nv);

        Connection conn = new ConnectionDAL().getConnection();
        conn.setAutoCommit(false);
        try {
            NhanVienDAL nvDAL = new NhanVienDAL(conn);
            TaiKhoanDAL tkDAL = new TaiKhoanDAL(conn);

            // Kiểm tra trùng mã
            if (nvDAL.tonTai(nv.getMaNhanVien())) {
                throw new IllegalArgumentException("Mã nhân viên \"" + nv.getMaNhanVien() + "\" đã tồn tại.");
            }

            // 1. Thêm nhân viên
            int r = nvDAL.them(nv);
            if (r == 0)
                throw new SQLException("Thêm nhân viên thất bại.");

            // 2. Xác định nhóm quyền từ tên chức vụ (gán cố định)
            NhomQuyen nq = timNhomQuyenChoChucVu(tenChucVu);
            String maNhomQuyen = nq.getMaNhomQuyen();

            // 3. Tạo tài khoản
            TaiKhoan tk = TaiKhoan.builder()
                    .maNhanVien(nv.getMaNhanVien())
                    .matKhau(MAT_KHAU_MAC_DINH)
                    .maNhomQuyen(maNhomQuyen)
                    .trangThai(true)
                    .build();
            tkDAL.them(tk);

            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw (e instanceof SQLException) ? (SQLException) e
                    : new SQLException("Lỗi khi thêm nhân viên: " + e.getMessage(), e);
        } finally {
            conn.setAutoCommit(true);
        }
    }

    // =========================================================
    // NGHIỆP VỤ CẬP NHẬT
    // =========================================================

    /**
     * Cập nhật thông tin nhân viên và đồng bộ nhóm quyền tài khoản
     * nếu chức vụ thay đổi.
     *
     * @param nv        Thông tin nhân viên (đã có mã)
     * @param tenChucVu Tên chức vụ mới (để cập nhật nhóm quyền)
     */
    public void capNhat(NhanVien nv, String tenChucVu) throws SQLException {
        validateCommon(nv);

        Connection conn = new ConnectionDAL().getConnection();
        conn.setAutoCommit(false);
        try {
            NhanVienDAL nvDAL = new NhanVienDAL(conn);
            TaiKhoanDAL tkDAL = new TaiKhoanDAL(conn);

            if (!nvDAL.tonTai(nv.getMaNhanVien())) {
                throw new IllegalArgumentException("Mã nhân viên \"" + nv.getMaNhanVien() + "\" không tồn tại.");
            }

            // 1. Cập nhật nhân viên
            int r = nvDAL.capNhat(nv);
            if (r == 0)
                throw new SQLException("Cập nhật nhân viên thất bại.");

            // 2. Cập nhật nhóm quyền trong tài khoản (nếu TK tồn tại)
            if (tkDAL.tonTai(nv.getMaNhanVien())) {
                NhomQuyen nq = timNhomQuyenChoChucVu(tenChucVu);
                tkDAL.capNhatNhomQuyen(nv.getMaNhanVien(), nq.getMaNhomQuyen());
            }

            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw (e instanceof SQLException) ? (SQLException) e
                    : new SQLException("Lỗi khi cập nhật nhân viên: " + e.getMessage(), e);
        } finally {
            conn.setAutoCommit(true);
        }
    }

    // =========================================================
    // NGHIỆP VỤ XÓA
    // =========================================================

    /**
     * Xóa nhân viên:
     * - Nếu đang có hóa đơn → xóa mềm (trangThai = 0), trả về "XOA_MEM"
     * - Ngược lại → xóa cứng TK trước rồi xóa NV, trả về "XOA_CUNG"
     *
     * @param maNhanVien Mã nhân viên cần xóa
     * @return "XOA_MEM" hoặc "XOA_CUNG"
     */
    public String xoa(String maNhanVien) throws SQLException {
        Connection conn = new ConnectionDAL().getConnection();
        conn.setAutoCommit(false);
        try {
            NhanVienDAL nvDAL = new NhanVienDAL(conn);
            TaiKhoanDAL tkDAL = new TaiKhoanDAL(conn);

            if (!nvDAL.tonTai(maNhanVien)) {
                throw new IllegalArgumentException("Nhân viên \"" + maNhanVien + "\" không tồn tại.");
            }

            String ketQua;
            nvDAL.doiTrangThai(maNhanVien, false);
            ketQua = "XOA_MEM";

            conn.commit();
            return ketQua;
        } catch (Exception e) {
            conn.rollback();
            throw (e instanceof SQLException) ? (SQLException) e
                    : new SQLException("Lỗi khi xóa nhân viên: " + e.getMessage(), e);
        } finally {
            conn.setAutoCommit(true);
        }
    }

    // =========================================================
    // VALIDATE
    // =========================================================

    private void validateCommon(NhanVien nv) {
        if (nv.getMaNhanVien() == null || nv.getMaNhanVien().isBlank())
            throw new IllegalArgumentException("Mã nhân viên không được để trống.");
        if (nv.getMaNhanVien().length() > 5)
            throw new IllegalArgumentException("Mã nhân viên tối đa 5 ký tự.");
        if (nv.getTenNhanVien() == null || nv.getTenNhanVien().isBlank())
            throw new IllegalArgumentException("Tên nhân viên không được để trống.");
        if (nv.getTenNhanVien().length() > 100)
            throw new IllegalArgumentException("Tên nhân viên tối đa 100 ký tự.");
        if (nv.getMaChucVu() == null || nv.getMaChucVu().isBlank())
            throw new IllegalArgumentException("Chức vụ không được để trống.");
        if (nv.getNamSinh() < 1900 || nv.getNamSinh() > 2100)
            throw new IllegalArgumentException("Năm sinh không hợp lệ.");
        if (nv.getSoDienThoai() != null && nv.getSoDienThoai().length() > 10)
            throw new IllegalArgumentException("Số điện thoại tối đa 10 ký tự.");
    }
}
