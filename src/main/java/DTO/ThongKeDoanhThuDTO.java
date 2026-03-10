package DTO;

/**
 * DTO lưu trữ dữ liệu doanh thu theo từng tháng trong một năm.
 * Được dùng để vẽ biểu đồ cột doanh thu theo tháng và
 * hiển thị các chỉ số KPI tổng hợp trên panel thống kê.
 */
public class ThongKeDoanhThuDTO {

    /** Năm của bản ghi doanh thu */
    private int nam;

    /** Tháng của bản ghi doanh thu (1–12) */
    private int thang;

    /** Tổng tiền thu được trong tháng (đã áp khuyến mãi, đã tính phí) */
    private double tongDoanhThu;

    /** Số lượng hóa đơn được lập trong tháng */
    private int soHoaDon;

    /** Tổng số vé đã bán trong tháng */
    private int soVe;

    // ---- Constructors ----

    public ThongKeDoanhThuDTO() {
    }

    public ThongKeDoanhThuDTO(int nam, int thang, double tongDoanhThu, int soHoaDon, int soVe) {
        this.nam = nam;
        this.thang = thang;
        this.tongDoanhThu = tongDoanhThu;
        this.soHoaDon = soHoaDon;
        this.soVe = soVe;
    }

    // ---- Getters / Setters ----

    public int getNam() {
        return nam;
    }

    public void setNam(int nam) {
        this.nam = nam;
    }

    public int getThang() {
        return thang;
    }

    public void setThang(int thang) {
        this.thang = thang;
    }

    public double getTongDoanhThu() {
        return tongDoanhThu;
    }

    public void setTongDoanhThu(double tongDoanhThu) {
        this.tongDoanhThu = tongDoanhThu;
    }

    public int getSoHoaDon() {
        return soHoaDon;
    }

    public void setSoHoaDon(int soHoaDon) {
        this.soHoaDon = soHoaDon;
    }

    public int getSoVe() {
        return soVe;
    }

    public void setSoVe(int soVe) {
        this.soVe = soVe;
    }

    @Override
    public String toString() {
        return "ThongKeDoanhThuDTO{nam=" + nam + ", thang=" + thang
                + ", tongDoanhThu=" + tongDoanhThu
                + ", soHoaDon=" + soHoaDon + ", soVe=" + soVe + '}';
    }
}
