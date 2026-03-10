package GUI;

import BUS.ThongKeBUS;
import DTO.ThongKeDTO;
import DTO.ThongKeDoanhThuDTO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import GUI.Menu.ActionButton; // Nút gradient chuẩn của dự án (hover/pressed tích hợp sẵn)
import GUI.Menu.PaintComponent; // Panel gradient #2980B9→#6DD5FA chuẩn của dự án

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Panel Thống Kê — hiển thị 4 KPI cards và 4 biểu đồ JFreeChart.
 *
 * Thiết kế responsive: toàn bộ layout dùng BorderLayout + GridLayout,
 * không sử dụng kích thước cố định, biểu đồ tự co giãn theo cửa sổ.
 *
 * Đồng bộ màu sắc với MainGUI:
 * - Màu chính: #2980B9 (xanh dương — khớp topSide và sidebar)
 * - Màu sáng: #6DD5FA (gradient sidebar)
 * - Nền panel: #F6F7F8 (khớp mainSide background)
 * - Font: Segoe UI
 */
public class ThongKePanel extends JPanel {

    // =========================================================
    // HẰNG SỐ MÀU SẮC VÀ FONT — đồng bộ toàn bộ ứng dụng
    // =========================================================

    /** Màu chính xanh dương — khớp với topSide và gradient sidebar */
    private static final Color MAU_CHINH = new Color(41, 128, 185);

    /** Màu sáng cuối gradient sidebar */
    private static final Color MAU_CHINH_SANG = new Color(109, 213, 250);

    /** Màu nền panel tổng — khớp với jPanel2 background #F6F7F8 */
    private static final Color MAU_NEN = new Color(246, 247, 248);

    /** Màu nền cho thẻ KPI và khung biểu đồ */
    private static final Color MAU_THE = Color.WHITE;

    /** Màu chữ tiêu đề đậm — dark blue */
    private static final Color MAU_CHU_TOI = new Color(44, 62, 80);

    /** Màu chữ phụ — xám trung */
    private static final Color MAU_CHU_PHU = new Color(127, 140, 141);

    /** Màu viền card — xám nhạt */
    private static final Color MAU_VIEN = new Color(220, 230, 240);

    /** Tên font toàn bộ panel */
    private static final String FONT_CHINH = "Segoe UI";

    /** Bảng màu cho các lát/cột biểu đồ khi có nhiều nhóm */
    private static final Color[] BANG_MAU_BIEU_DO = {
            new Color(41, 128, 185), // xanh dương chính
            new Color(39, 174, 96), // xanh lá
            new Color(231, 76, 60), // đỏ san hô
            new Color(241, 196, 15), // vàng
            new Color(155, 89, 182), // tím
            new Color(230, 126, 34), // cam
    };

    // =========================================================
    // THÀNH PHẦN GIAO DIỆN (đặt tên tiếng Việt theo yêu cầu)
    // =========================================================

    /** BUS xử lý nghiệp vụ thống kê, tính toán và cung cấp dữ liệu */
    private final ThongKeBUS thuongKeBUS;

    /** Panel đầu trang: chứa thanh tiêu đề + các thẻ KPI */
    private JPanel panelDauTrang;

    /** Thanh tiêu đề: chứa tên màn hình, combo chọn năm, nút làm mới */
    private JPanel panelThanhTieuDe;

    /** ComboBox để người dùng chọn năm cần xem thống kê */
    private JComboBox<Integer> cboNamThongKe;

    /**
     * Nút làm mới — sử dụng ActionButton (custom component của dự án).
     * Đã tích hợp sẵn: gradient xanh dương, hover brighter, pressed darker,
     * cursor HAND, font Segoe UI Bold 14, foreground trắng.
     */
    private ActionButton btnLamMoi;

    /** Panel chứa 4 thẻ KPI nằm ngang */
    private JPanel panelTheKPI;

    /** Nhãn hiển thị giá trị Tổng doanh thu */
    private JLabel lblGiaTriDoanhThu;

    /** Nhãn hiển thị giá trị Số hóa đơn */
    private JLabel lblGiaTriHoaDon;

    /** Nhãn hiển thị giá trị Số vé bán */
    private JLabel lblGiaTriSoVe;

    /** Nhãn hiển thị giá trị Số tour hoạt động */
    private JLabel lblGiaTriTourHoatDong;

    /** Panel trung tâm chứa lưới 2×2 biểu đồ */
    private JPanel panelLuoiBieuDo;

    /** ChartPanel biểu đồ cột — Doanh thu theo tháng */
    private ChartPanel chartPanelDoanhThuThang;

    /** ChartPanel biểu đồ tròn — Doanh thu theo loại tour */
    private ChartPanel chartPanelLoaiTour;

    /** ChartPanel biểu đồ cột ngang — Top 5 tour bán chạy */
    private ChartPanel chartPanelTopTour;

    /** ChartPanel biểu đồ tròn — Phân loại khách hàng */
    private ChartPanel chartPanelLoaiKhach;

    // =========================================================
    // KHỞI TẠO
    // =========================================================

    /**
     * Constructor mặc định — tự lấy kết nối DB qua ConnectionDAL.
     */
    public ThongKePanel() {
        this.thuongKeBUS = new ThongKeBUS();
        xayDungGiaoDien();
        taiDuLieuLanDau();
    }

    /**
     * Constructor có Connection — dùng khi muốn share kết nối.
     *
     * @param conn Kết nối CSDL đã mở sẵn
     */
    public ThongKePanel(java.sql.Connection conn) {
        this.thuongKeBUS = new ThongKeBUS(conn);
        xayDungGiaoDien();
        taiDuLieuLanDau();
    }

    // =========================================================
    // XÂY DỰNG GIAO DIỆN — bước từ ngoài vào trong
    // =========================================================

    /**
     * Xây dựng toàn bộ layout chính của panel.
     *
     * Sử dụng BorderLayout để responsive hoàn toàn:
     * - NORTH → panelDauTrang (thanh tiêu đề + 4 KPI cards)
     * - CENTER → panelLuoiBieuDo (2×2 grid biểu đồ)
     */
    private void xayDungGiaoDien() {
        // Nền panel tổng khớp với mainSide background
        setBackground(MAU_NEN);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(12, 12, 12, 12));

        // Xây dựng từng khu vực
        xayDungPanelDauTrang(); // NORTH
        xayDungPanelBieuDo(); // CENTER

        add(panelDauTrang, BorderLayout.NORTH);
        add(panelLuoiBieuDo, BorderLayout.CENTER);

        // Lắng nghe sự kiện resize cửa sổ để cập nhật lại biểu đồ
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // GridLayout tự co giãn — chỉ cần repaint
                repaint();
            }
        });
    }

    /**
     * Xây dựng panel đầu trang gồm 2 tầng:
     * - Tầng 1 (NORTH): Thanh tiêu đề + filter năm + nút làm mới
     * - Tầng 2 (CENTER): 4 thẻ KPI ngang nhau
     */
    private void xayDungPanelDauTrang() {
        panelDauTrang = new JPanel(new BorderLayout(0, 8));
        panelDauTrang.setBackground(MAU_NEN);
        panelDauTrang.setBorder(new EmptyBorder(0, 0, 10, 0));

        // --- Tầng 1: Thanh tiêu đề ---
        panelThanhTieuDe = xayDungThanhTieuDe();

        // --- Tầng 2: KPI cards ---
        panelTheKPI = xayDungPanelKPI();

        panelDauTrang.add(panelThanhTieuDe, BorderLayout.NORTH);
        panelDauTrang.add(panelTheKPI, BorderLayout.CENTER);
    }

    /**
     * Xây dựng thanh tiêu đề chứa:
     * - Label tiêu đề màn hình bên trái
     * - ComboBox chọn năm + nút làm mới bên phải
     *
     * @return Panel thanh tiêu đề đã được cấu hình
     */
    private JPanel xayDungThanhTieuDe() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(MAU_NEN);
        panel.setBorder(new EmptyBorder(0, 4, 4, 4));

        JLabel lblTieuDe = new JLabel("TRANG CHỦ");
        lblTieuDe.setFont(new Font(FONT_CHINH, Font.BOLD, 20));
        lblTieuDe.setForeground(MAU_CHU_TOI);

        panel.add(lblTieuDe, BorderLayout.WEST);
        return panel;
    }

    /**
     * Xây dựng panel chứa 4 thẻ KPI nằm ngang nhau với tỉ lệ bằng nhau.
     *
     * GridLayout(1, 4) đảm bảo 4 thẻ luôn dàn đều theo chiều ngang,
     * tự co giãn khi cửa sổ thay đổi kích thước.
     *
     * @return Panel KPI đã được cấu hình với 4 thẻ rỗng
     */
    private JPanel xayDungPanelKPI() {
        // GridLayout 1 hàng, 4 cột, khoảng cách 10px
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 0));
        panel.setBackground(MAU_NEN);

        // Tạo 4 thẻ KPI theo thứ tự: Doanh thu, Hóa đơn, Vé, Tour
        // Mỗi thẻ trả về label giá trị để cập nhật sau khi load data

        // Tạo 4 thẻ KPI — đồng bộ hoàn toàn màu sắc dự án (dùng PaintComponent bên
        // trong)
        lblGiaTriDoanhThu = taoTheKPI(panel, "Tổng doanh thu", "0 ₫");
        lblGiaTriHoaDon = taoTheKPI(panel, "Số hóa đơn", "0");
        lblGiaTriSoVe = taoTheKPI(panel, "Số vé đã bán", "0");
        lblGiaTriTourHoatDong = taoTheKPI(panel, "Tour hoạt động", "0");

        return panel;
    }

    /**
     * Tạo một thẻ KPI và thêm vào panel cha.
     *
     * Cấu trúc thẻ (BorderLayout):
     * - NORTH: PaintComponent — panel gradient chuẩn của dự án (#2980B9→#6DD5FA)
     * - CENTER: Nội dung — tiêu đề xám nhỏ + giá trị đậm lớn
     *
     * Các component sử dụng:
     * - PaintComponent (GUI.Menu): thanh accent gradient đầu thẻ, đồng bộ
     * sidebar/topbar
     *
     * @param parent       Panel cha để add thẻ vào
     * @param tieuDe       Tên chỉ số (VD: "Tổng doanh thu")
     * @param giaTriBanDau Giá trị ban đầu trước khi load xong
     * @return Label giá trị lớn — để cập nhật sau khi load data
     */
    private JLabel taoTheKPI(JPanel parent, String tieuDe, String giaTriBanDau) {
        // --- Panel thẻ ngoài cùng: nền trắng, bo góc, viền nhạt ---
        JPanel theKPI = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Nền trắng bo góc nhẹ — giữ sạch, để PaintComponent ở NORTH tạo điểm nhấn
                g2.setColor(MAU_THE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                // Viền mỏng nhạt
                g2.setColor(MAU_VIEN);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        theKPI.setOpaque(false);
        theKPI.setBorder(new EmptyBorder(0, 0, 0, 0));

        // --- Thanh accent đầu thẻ: dùng PaintComponent chuẩn của dự án ---
        // PaintComponent vẽ gradient #2980B9→#6DD5FA với bo góc, khớp với
        // màu topSide, sidebar và toàn bộ design language của ứng dụng.
        PaintComponent thanhGradientDau = new PaintComponent();
        thanhGradientDau.setPreferredSize(new Dimension(0, 6)); // Cao 6px dạng thanh mỏng

        // --- Khu vực nội dung: tiêu đề + giá trị ---
        JPanel panelNoiDung = new JPanel();
        panelNoiDung.setLayout(new BoxLayout(panelNoiDung, BoxLayout.Y_AXIS));
        panelNoiDung.setOpaque(false);
        panelNoiDung.setBorder(new EmptyBorder(10, 16, 10, 16));

        // Nhãn tiêu đề chỉ số — chữ xám nhỏ, in thường
        JLabel lblTieuDeKPI = new JLabel(tieuDe);
        lblTieuDeKPI.setFont(new Font(FONT_CHINH, Font.PLAIN, 11));
        lblTieuDeKPI.setForeground(MAU_CHU_PHU);
        lblTieuDeKPI.setAlignmentX(LEFT_ALIGNMENT);

        // Khoảng đệm dọc giữa tiêu đề và giá trị
        Component khoangDem = Box.createVerticalStrut(4);

        // Label giá trị lớn — cập nhật sau khi load data
        JLabel lblGiaTri = new JLabel(giaTriBanDau);
        lblGiaTri.setFont(new Font(FONT_CHINH, Font.BOLD, 22));
        lblGiaTri.setForeground(MAU_CHU_TOI);
        lblGiaTri.setAlignmentX(LEFT_ALIGNMENT);

        panelNoiDung.add(lblTieuDeKPI);
        panelNoiDung.add(khoangDem);
        panelNoiDung.add(lblGiaTri);

        // Ghép các khu vực vào thẻ
        theKPI.add(thanhGradientDau, BorderLayout.NORTH);
        theKPI.add(panelNoiDung, BorderLayout.CENTER);

        parent.add(theKPI);
        return lblGiaTri; // Trả về để cập nhật giá trị sau
    }

    /**
     * Xây dựng panel lưới 2×2 chứa 4 biểu đồ.
     *
     * GridLayout(2, 2) đảm bảo 4 biểu đồ luôn chiếm đều diện tích còn lại,
     * tự co giãn hoàn toàn khi cửa sổ resize.
     *
     * Biểu đồ được khởi tạo với dữ liệu rỗng, sẽ được fill khi loadData().
     */
    private void xayDungPanelBieuDo() {
        // GridLayout 2 hàng 2 cột, khoảng cách 10px giữa các ô
        panelLuoiBieuDo = new JPanel(new GridLayout(2, 2, 10, 10));
        panelLuoiBieuDo.setBackground(MAU_NEN);

        // Khởi tạo 4 ChartPanel với biểu đồ rỗng placeholder
        chartPanelDoanhThuThang = taoChartPanelRong("Doanh thu theo tháng");
        chartPanelLoaiTour = taoChartPanelRong("Doanh thu theo loại tour");
        chartPanelTopTour = taoChartPanelRong("Top 5 tour bán chạy");
        chartPanelLoaiKhach = taoChartPanelRong("Phân loại khách hàng");

        panelLuoiBieuDo.add(chartPanelDoanhThuThang);
        panelLuoiBieuDo.add(chartPanelLoaiTour);
        panelLuoiBieuDo.add(chartPanelTopTour);
        panelLuoiBieuDo.add(chartPanelLoaiKhach);
    }

    /**
     * Tạo một ChartPanel rỗng với tiêu đề placeholder.
     * Dùng trong giai đoạn khởi tạo trước khi dữ liệu được load.
     *
     * @param tieuDe Tiêu đề hiển thị trên biểu đồ placeholder
     * @return ChartPanel với biểu đồ trống đã cấu hình responsive
     */
    private ChartPanel taoChartPanelRong(String tieuDe) {
        // Biểu đồ rỗng để placeholder
        DefaultCategoryDataset tapDuLieuRong = new DefaultCategoryDataset();
        JFreeChart bieuDoRong = ChartFactory.createBarChart(
                tieuDe, null, null,
                tapDuLieuRong, PlotOrientation.VERTICAL, false, false, false);

        apDungPhongCachBieuDo(bieuDoRong);

        ChartPanel cp = new ChartPanel(bieuDoRong);
        apDungPhongCachChartPanel(cp);
        return cp;
    }

    // =========================================================
    // TẢI VÀ CẬP NHẬT DỮ LIỆU
    // =========================================================

    /**
     * Tải dữ liệu lần đầu khi panel được tạo:
     * 1. Lấy danh sách năm từ DB → điền ComboBox
     * 2. Chọn năm hiện tại (hoặc năm gần nhất có data)
     * 3. Tải dữ liệu theo năm đó
     */
    private void taiDuLieuLanDau() {
        SwingUtilities.invokeLater(() -> {
            int namHienTai = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
            taiVaHienThiDuLieu(namHienTai);
        });
    }

    /**
     * Tải và hiển thị toàn bộ dữ liệu thống kê cho năm được chọn.
     * Phương thức này chạy trên Event Dispatch Thread.
     *
     * Thứ tự thực hiện:
     * 1. Cập nhật 4 thẻ KPI
     * 2. Vẽ lại 4 biểu đồ
     *
     * @param nam Năm cần hiển thị thống kê
     */
    public void taiVaHienThiDuLieu(int nam) {
        // Chạy query trên background thread để không block UI
        SwingWorker<Void, Void> congViecTai = new SwingWorker<>() {

            // Kết quả trung gian từ DB
            List<ThongKeDoanhThuDTO> dsDoanhThuThang;
            List<ThongKeDTO> dsLoaiTour;
            List<ThongKeDTO> dsTopTour;
            List<ThongKeDTO> dsLoaiKhach;
            int soTourHoatDong;

            @Override
            protected Void doInBackground() throws Exception {
                // Tất cả query chạy song song trên background thread
                dsDoanhThuThang = thuongKeBUS.getDoanhThuTheoThang(nam);
                dsLoaiTour = thuongKeBUS.getDoanhThuTheoLoaiTour();
                dsTopTour = thuongKeBUS.getTopTourBanChay(5);
                dsLoaiKhach = thuongKeBUS.getKhachHangTheoLoai();
                soTourHoatDong = thuongKeBUS.getSoTourHoatDong();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get(); // Propagate exception nếu có

                    // 1. Điền đủ 12 tháng (tháng không có data → 0)
                    List<ThongKeDoanhThuDTO> ds12Thang = thuongKeBUS.dien12Thang(dsDoanhThuThang, nam);

                    // 2. Tính toán KPI
                    double tongDoanhThu = thuongKeBUS.tinhTongDoanhThu(ds12Thang);
                    int tongHoaDon = thuongKeBUS.tinhTongHoaDon(ds12Thang);
                    int tongSoVe = thuongKeBUS.tinhTongSoVe(ds12Thang);

                    // 3. Cập nhật nhãn KPI (chạy trên EDT)
                    capNhatKPI(tongDoanhThu, tongHoaDon, tongSoVe, soTourHoatDong);

                    // 4. Vẽ lại 4 biểu đồ
                    capNhatBieuDoDoanhThuThang(ds12Thang, nam);
                    capNhatBieuDoLoaiTour(dsLoaiTour);
                    capNhatBieuDoTopTour(dsTopTour);
                    capNhatBieuDoLoaiKhach(dsLoaiKhach);

                } catch (Exception e) {
                    hienThiLoi("Lỗi khi tải dữ liệu thống kê:\n" + e.getMessage());
                }
            }
        };

        congViecTai.execute();
    }

    // =========================================================
    // CẬP NHẬT KPI
    // =========================================================

    /**
     * Cập nhật các nhãn giá trị trên 4 thẻ KPI.
     * Định dạng tiền tệ VND và số nguyên.
     *
     * @param tongDoanhThu   Tổng doanh thu (VND)
     * @param tongHoaDon     Tổng số hóa đơn
     * @param tongSoVe       Tổng số vé bán được
     * @param soTourHoatDong Số tour đang hoạt động
     */
    private void capNhatKPI(double tongDoanhThu, int tongHoaDon,
            int tongSoVe, int soTourHoatDong) {
        // Định dạng tiền tệ kiểu Việt (dấu phẩy nghìn)
        NumberFormat dinhDangTien = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        dinhDangTien.setMaximumFractionDigits(0);

        lblGiaTriDoanhThu.setText(dinhDangTien.format(tongDoanhThu) + " ₫");
        lblGiaTriHoaDon.setText(String.valueOf(tongHoaDon));
        lblGiaTriSoVe.setText(String.valueOf(tongSoVe));
        lblGiaTriTourHoatDong.setText(String.valueOf(soTourHoatDong));
    }

    // =========================================================
    // CẬP NHẬT BIỂU ĐỒ
    // =========================================================

    /**
     * Vẽ lại biểu đồ cột Doanh thu theo tháng.
     *
     * Mỗi tháng là 1 cột, đơn vị trục Y là Triệu VNĐ.
     * Màu gradient từ MAU_CHINH để khớp với thiết kế tổng thể.
     *
     * @param danhSach Danh sách 12 tháng (đã điền đủ 0 cho tháng thiếu)
     * @param nam      Năm hiển thị trên tiêu đề biểu đồ
     */
    private void capNhatBieuDoDoanhThuThang(List<ThongKeDoanhThuDTO> danhSach, int nam) {
        DefaultCategoryDataset tapDuLieu = new DefaultCategoryDataset();
        String chuoiSeries = "Doanh thu (triệu ₫)";

        for (ThongKeDoanhThuDTO dto : danhSach) {
            // Chuyển VND → Triệu để trục Y gọn hơn
            double doanhThuTrieu = dto.getTongDoanhThu() / 1_000_000.0;
            tapDuLieu.addValue(doanhThuTrieu, chuoiSeries, "T" + dto.getThang());
        }

        JFreeChart bieuDo = ChartFactory.createBarChart(
                "Doanh thu năm " + nam, // Tiêu đề
                "Tháng", // Nhãn trục X
                "Triệu VNĐ", // Nhãn trục Y
                tapDuLieu,
                PlotOrientation.VERTICAL,
                false, // Không hiện legend (chỉ 1 series)
                true, // Bật tooltips
                false);

        apDungPhongCachBieuDo(bieuDo);

        // Tùy chỉnh màu cột
        CategoryPlot plot = bieuDo.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBarPainter(new StandardBarPainter()); // Không gradient trên cột
        renderer.setSeriesPaint(0, MAU_CHINH); // Màu chính xanh dương
        renderer.setMaximumBarWidth(0.06); // Độ rộng cột tối đa 6%

        // Số trên đầu cột
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultItemLabelFont(new Font(FONT_CHINH, Font.PLAIN, 9));
        renderer.setDefaultItemLabelPaint(MAU_CHU_TOI);

        capNhatChartPanel(chartPanelDoanhThuThang, bieuDo);
    }

    /**
     * Vẽ lại biểu đồ tròn Doanh thu theo loại tour.
     *
     * Mỗi lát tương ứng 1 loại tour, dùng bảng màu BANG_MAU_BIEU_DO.
     *
     * @param danhSach Danh sách {tên loại tour, doanh thu}
     */
    private void capNhatBieuDoLoaiTour(List<ThongKeDTO> danhSach) {
        DefaultPieDataset<String> tapDuLieu = new DefaultPieDataset<>();

        // Thêm dữ liệu vào biểu đồ tròn
        for (ThongKeDTO dto : danhSach) {
            tapDuLieu.setValue(dto.getNhan(), dto.getGiaTri());
        }

        // Nếu không có data → hiển thị 1 lát "Chưa có dữ liệu"
        if (danhSach.isEmpty()) {
            tapDuLieu.setValue("Chưa có dữ liệu", 1);
        }

        JFreeChart bieuDo = ChartFactory.createPieChart(
                "Doanh thu theo loại tour",
                tapDuLieu,
                true, // Hiện legend
                true, // Tooltips
                false);

        apDungPhongCachBieuDo(bieuDo);
        apDungMauBieuDoTron(bieuDo, tapDuLieu);

        capNhatChartPanel(chartPanelLoaiTour, bieuDo);
    }

    /**
     * Vẽ lại biểu đồ cột ngang Top 5 tour bán chạy nhất.
     *
     * Hiển thị số vé theo chiều ngang để tên tour dài không bị cắt.
     *
     * @param danhSach Danh sách top N tour sắp xếp giảm dần
     */
    private void capNhatBieuDoTopTour(List<ThongKeDTO> danhSach) {
        DefaultCategoryDataset tapDuLieu = new DefaultCategoryDataset();
        String chuoiSeries = "Số vé";

        for (ThongKeDTO dto : danhSach) {
            // Rút gọn tên tour nếu quá dài để trục Y gọn hơn
            String tenRutGon = rutGonTen(dto.getNhan(), 25);
            tapDuLieu.addValue(dto.getSoLuong(), chuoiSeries, tenRutGon);
        }

        JFreeChart bieuDo = ChartFactory.createBarChart(
                "Top 5 tour bán chạy",
                null, // Không cần nhãn trục Y (tên tour)
                "Số vé bán", // Nhãn trục X (giá trị)
                tapDuLieu,
                PlotOrientation.HORIZONTAL, // Cột ngang
                false,
                true,
                false);

        apDungPhongCachBieuDo(bieuDo);

        // Màu cột theo thứ tự từ bảng màu
        CategoryPlot plot = bieuDo.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBarPainter(new StandardBarPainter());

        // Tô màu gradient cho các tour khác nhau
        for (int i = 0; i < Math.min(danhSach.size(), BANG_MAU_BIEU_DO.length); i++) {
            renderer.setSeriesPaint(0, BANG_MAU_BIEU_DO[0]);
        }
        // Dùng màu đơn nhất quán cho top tour
        renderer.setSeriesPaint(0, new Color(39, 174, 96));
        renderer.setMaximumBarWidth(0.5);

        capNhatChartPanel(chartPanelTopTour, bieuDo);
    }

    /**
     * Vẽ lại biểu đồ tròn Phân loại khách hàng.
     *
     * @param danhSach Danh sách {tên loại KH, số lượng}
     */
    private void capNhatBieuDoLoaiKhach(List<ThongKeDTO> danhSach) {
        DefaultPieDataset<String> tapDuLieu = new DefaultPieDataset<>();

        for (ThongKeDTO dto : danhSach) {
            tapDuLieu.setValue(dto.getNhan(), dto.getSoLuong());
        }

        if (danhSach.isEmpty()) {
            tapDuLieu.setValue("Chưa có dữ liệu", 1);
        }

        JFreeChart bieuDo = ChartFactory.createPieChart(
                "Phân loại khách hàng",
                tapDuLieu,
                true,
                true,
                false);

        apDungPhongCachBieuDo(bieuDo);
        apDungMauBieuDoTron(bieuDo, tapDuLieu);

        capNhatChartPanel(chartPanelLoaiKhach, bieuDo);
    }

    // =========================================================
    // PHONG CÁCH BIỂU ĐỒ — áp dụng thống nhất cho tất cả chart
    // =========================================================

    /**
     * Áp dụng phong cách đồng bộ cho một JFreeChart:
     * - Font "Segoe UI" cho tiêu đề, chú thích, trục
     * - Nền trắng, không viền
     * - Màu sắc khớp với thiết kế tổng thể
     *
     * @param bieuDo Biểu đồ cần áp dụng phong cách
     */
    private void apDungPhongCachBieuDo(JFreeChart bieuDo) {
        // Nền chart trắng, không viền
        bieuDo.setBackgroundPaint(MAU_THE);
        bieuDo.setBorderVisible(false);

        // Font tiêu đề biểu đồ
        bieuDo.getTitle().setFont(new Font(FONT_CHINH, Font.BOLD, 14));
        bieuDo.getTitle().setPaint(MAU_CHU_TOI);

        // Font chú thích legend (nếu có)
        if (bieuDo.getLegend() != null) {
            bieuDo.getLegend().setItemFont(new Font(FONT_CHINH, Font.PLAIN, 11));
            bieuDo.getLegend().setBackgroundPaint(MAU_THE);
        }

        // Tùy chỉnh plot (phần vẽ biểu đồ)
        if (bieuDo.getPlot() instanceof CategoryPlot) {
            CategoryPlot plot = (CategoryPlot) bieuDo.getPlot();
            apDungPhongCachCategoryPlot(plot);
        } else if (bieuDo.getPlot() instanceof PiePlot) {
            apDungPhongCachPiePlot((PiePlot<?>) bieuDo.getPlot());
        }
    }

    /**
     * Áp dụng phong cách cho CategoryPlot (biểu đồ cột/đường).
     *
     * @param plot CategoryPlot của biểu đồ cần tùy chỉnh
     */
    private void apDungPhongCachCategoryPlot(CategoryPlot plot) {
        // Nền vùng vẽ
        plot.setBackgroundPaint(new Color(250, 252, 255));

        // Đường lưới ngang nhạt
        plot.setRangeGridlinePaint(new Color(220, 220, 220));
        plot.setDomainGridlinePaint(new Color(220, 220, 220));
        plot.setRangeGridlinesVisible(true);
        plot.setOutlinePaint(null); // Bỏ viền plot

        // Font trục X (CategoryAxis)
        CategoryAxis trucX = plot.getDomainAxis();
        trucX.setTickLabelFont(new Font(FONT_CHINH, Font.PLAIN, 10));
        trucX.setLabelFont(new Font(FONT_CHINH, Font.PLAIN, 11));
        trucX.setLabelPaint(MAU_CHU_TOI);
        trucX.setTickLabelPaint(MAU_CHU_PHU);
        trucX.setAxisLinePaint(MAU_VIEN);

        // Font trục Y (NumberAxis)
        NumberAxis trucY = (NumberAxis) plot.getRangeAxis();
        trucY.setTickLabelFont(new Font(FONT_CHINH, Font.PLAIN, 10));
        trucY.setLabelFont(new Font(FONT_CHINH, Font.PLAIN, 11));
        trucY.setLabelPaint(MAU_CHU_TOI);
        trucY.setTickLabelPaint(MAU_CHU_PHU);
        trucY.setAxisLinePaint(MAU_VIEN);
        trucY.setAutoRangeIncludesZero(true); // Luôn bắt đầu từ 0
    }

    /**
     * Áp dụng phong cách cho PiePlot (biểu đồ tròn).
     *
     * @param plot PiePlot của biểu đồ cần tùy chỉnh
     */
    private void apDungPhongCachPiePlot(PiePlot<?> plot) {
        // Nền vùng vẽ trong suốt để hòa với nền chart
        plot.setBackgroundPaint(MAU_THE);
        plot.setOutlinePaint(null);

        // Màu đường viền mặc định cho tất cả các lát (JFreeChart 1.5.x dùng
        // setDefault*)
        plot.setDefaultSectionOutlinePaint(Color.WHITE);
        plot.setDefaultSectionOutlineStroke(new BasicStroke(2f));

        // Font nhãn trên lát
        plot.setLabelFont(new Font(FONT_CHINH, Font.PLAIN, 10));
        plot.setLabelPaint(MAU_CHU_TOI);
        plot.setLabelBackgroundPaint(new Color(255, 255, 255, 200));

        // Hiển thị % trên từng lát — format: "Tên: 35%"
        plot.setLabelGenerator(new org.jfree.chart.labels.StandardPieSectionLabelGenerator(
                "{0}: {2}", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance()));
    }

    /**
     * Áp dụng bảng màu cho biểu đồ tròn theo thứ tự BANG_MAU_BIEU_DO.
     *
     * @param bieuDo    JFreeChart kiểu pie
     * @param tapDuLieu Dataset của biểu đồ tròn
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void apDungMauBieuDoTron(JFreeChart bieuDo, DefaultPieDataset<?> tapDuLieu) {
        // Dùng raw PiePlot để tránh vấn đề wildcard type với generics JFreeChart 1.5.x
        PiePlot plotRaw = (PiePlot) bieuDo.getPlot();
        int soLuongMau = BANG_MAU_BIEU_DO.length;

        java.util.List<?> cacKhoa = tapDuLieu.getKeys();
        for (int i = 0; i < cacKhoa.size(); i++) {
            // Lặp vòng bảng màu khi số nhóm vượt 6
            Color mau = BANG_MAU_BIEU_DO[i % soLuongMau];
            // Cast sang Comparable vì PiePlot raw type yêu cầu — @SuppressWarnings bao che
            // cảnh báo
            plotRaw.setSectionPaint((Comparable) cacKhoa.get(i), mau);
        }
    }

    /**
     * Cấu hình ChartPanel để hoàn toàn responsive.
     *
     * Bỏ minimum draw size của JFreeChart để chart vẽ lại thực sự
     * theo kích thước container, thay vì scale hình ảnh.
     *
     * @param cp ChartPanel cần cấu hình
     */
    private void apDungPhongCachChartPanel(ChartPanel cp) {
        // Xóa kích thước cố định — cho phép GridLayout co giãn
        cp.setMinimumDrawWidth(0);
        cp.setMinimumDrawHeight(0);
        cp.setMaximumDrawWidth(Integer.MAX_VALUE);
        cp.setMaximumDrawHeight(Integer.MAX_VALUE);

        // Viền nhẹ quanh chart
        cp.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MAU_VIEN, 1),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        cp.setBackground(MAU_THE);
    }

    /**
     * Thay thế nội dung biểu đồ trong ChartPanel đã có sẵn trên giao diện.
     * Áp dụng phong cách ChartPanel và repaint.
     *
     * @param chartPanel ChartPanel đang hiển thị (đã add vào panelLuoiBieuDo)
     * @param bieuDoMoi  JFreeChart mới để thay thế
     */
    private void capNhatChartPanel(ChartPanel chartPanel, JFreeChart bieuDoMoi) {
        chartPanel.setChart(bieuDoMoi);
        apDungPhongCachChartPanel(chartPanel);
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    // =========================================================
    // TIỆN ÍCH
    // =========================================================

    /**
     * Rút gọn tên dài bằng cách cắt và thêm "..." nếu vượt quá giới hạn.
     *
     * @param ten      Tên cần rút gọn
     * @param gioidHan Số ký tự tối đa
     * @return Tên đã rút gọn
     */
    private String rutGonTen(String ten, int gioidHan) {
        if (ten == null)
            return "";
        if (ten.length() <= gioidHan)
            return ten;
        return ten.substring(0, gioidHan - 3) + "...";
    }

    /**
     * Hiển thị hộp thoại thông báo lỗi thân thiện với người dùng.
     *
     * @param thongBao Nội dung thông báo lỗi
     */
    private void hienThiLoi(String thongBao) {
        JOptionPane.showMessageDialog(
                this,
                thongBao,
                "Lỗi thống kê",
                JOptionPane.WARNING_MESSAGE);
    }
}
