package DTO;

/**
 * DTO dùng chung cho các kết quả thống kê đơn giản dạng (nhãn, số lượng, giá
 * trị).
 * Được tái sử dụng cho nhiều loại biểu đồ khác nhau:
 * - Doanh thu theo loại tour
 * - Top tour bán chạy
 * - Phân loại khách hàng
 * - Tỉ lệ lấp đầy tour
 */
public class ThongKeDTO {

    /** Nhãn hiển thị trên trục / lát biểu đồ (tên loại tour, tên địa điểm...) */
    private String nhan;

    /** Giá trị tiền tệ hoặc phần trăm (doanh thu, tỉ lệ...) */
    private double giaTri;

    /** Số lượng đếm được (số vé, số hóa đơn, số khách...) */
    private int soLuong;

    // ---- Constructors ----

    public ThongKeDTO() {
    }

    public ThongKeDTO(String nhan, double giaTri, int soLuong) {
        this.nhan = nhan;
        this.giaTri = giaTri;
        this.soLuong = soLuong;
    }

    // ---- Builder style factory ----

    /**
     * Tạo nhanh một bản ghi chỉ có nhãn + giá trị tiền (dùng cho biểu đồ doanh thu)
     */
    public static ThongKeDTO ofGiaTri(String nhan, double giaTri) {
        return new ThongKeDTO(nhan, giaTri, 0);
    }

    /** Tạo nhanh một bản ghi chỉ có nhãn + số lượng (dùng cho biểu đồ đếm) */
    public static ThongKeDTO ofSoLuong(String nhan, int soLuong) {
        return new ThongKeDTO(nhan, 0, soLuong);
    }

    // ---- Getters / Setters ----

    public String getNhan() {
        return nhan;
    }

    public void setNhan(String nhan) {
        this.nhan = nhan;
    }

    public double getGiaTri() {
        return giaTri;
    }

    public void setGiaTri(double giaTri) {
        this.giaTri = giaTri;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    @Override
    public String toString() {
        return "ThongKeDTO{nhan='" + nhan + "', giaTri=" + giaTri + ", soLuong=" + soLuong + '}';
    }
}
