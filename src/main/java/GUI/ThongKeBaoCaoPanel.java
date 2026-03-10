package GUI;

import BUS.BaoCaoBUS;
import DTO.*;
import GUI.Menu.ActionButton;
import GUI.Menu.PaintComponent;
import GUI.Menu.TabbedPaneCustom;
import GUI.ScrollPane.ScrollPaneWin11;

import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.*;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Module Thống kê & Báo cáo — panel chính.
 *
 * Cấu trúc:
 * NORTH : Tiêu đề + bộ lọc (thời gian, loại tour, nhân viên...) + nút Lọc/Làm
 * mới/Xuất
 * CENTER : TabbedPaneCustom với 6 tab:
 * [0] Dashboard — biểu đồ KPI tổng quan (ThongKePanel)
 * [1] Doanh thu — biểu đồ cột + bảng chi tiết
 * [2] Tour — biểu đồ lấp đầy + bảng
 * [3] Khách hàng — biểu đồ tròn phân loại + top KH chi tiêu
 * [4] Nhân viên — biểu đồ cột so sánh + bảng
 * [5] Vận hành — biểu đồ PT & HDV + bảng
 *
 * Thiết kế UI đồng bộ với dự án:
 * - ActionButton, PaintComponent, TabbedPaneCustom, ScrollPaneWin11
 * - Font: Segoe UI | Màu chính: #2980B9→#6DD5FA | Nền: #F6F7F8
 * - SwingWorker cho tất cả truy vấn DB, không block EDT
 */
public class ThongKeBaoCaoPanel extends JPanel {

    // ─────────────────────────────────────────────────────────
    // HẰNG SỐ MÀU & FONT
    // ─────────────────────────────────────────────────────────

    private static final Color C_CHINH = new Color(41, 128, 185);
    private static final Color C_SANG = new Color(109, 213, 250);
    private static final Color C_NEN = new Color(246, 247, 248);
    private static final Color C_TRANG = Color.WHITE;
    private static final Color C_CHU_TOI = new Color(44, 62, 80);
    private static final Color C_CHU_PHU = new Color(127, 140, 141);
    private static final Color C_VIEN = new Color(213, 219, 219);
    private static final Color C_HANG_XEN = new Color(240, 246, 252);
    private static final Color C_CHON = new Color(214, 234, 248);
    private static final Color C_XANH_LA = new Color(39, 174, 96);
    private static final Color C_DO = new Color(231, 76, 60);
    private static final Color C_CAM = new Color(230, 126, 34);
    private static final Color C_VANG = new Color(241, 196, 15);
    private static final Color[] BANG_MAU = { C_CHINH, C_XANH_LA, C_DO, C_CAM, C_VANG,
            new Color(155, 89, 182), new Color(26, 188, 156), new Color(52, 73, 94) };
    private static final String FONT = "Segoe UI";
    private static final NumberFormat FMT = NumberFormat.getInstance(new Locale("vi", "VN"));

    static {
        FMT.setMaximumFractionDigits(0);
    }

    // ─────────────────────────────────────────────────────────
    // NGHIỆP VỤ
    // ─────────────────────────────────────────────────────────

    private final BaoCaoBUS bus;

    /** Filter đang áp dụng hiện tại */
    private BaoCaoFilterDTO filterHienTai;

    // ─────────────────────────────────────────────────────────
    // THÀNH PHẦN FILTER
    // ─────────────────────────────────────────────────────────

    private JSpinner spinTuNgay;
    private JSpinner spinDenNgay;
    /** Mapping mã loại tour → hiển thị trong cboLoaiTour */
    private JComboBox<String> cboLoaiTour;
    private JComboBox<String> cboTrangThaiTour;
    private ActionButton btnLoc;
    private ActionButton btnLamMoi;
    private ActionButton btnXuatExcel;
    private ActionButton btnXuatPdf;

    /** Map hiển thị → mã thực để tra lại khi lọc */
    private Map<String, String> mapLoaiTour;

    // ─────────────────────────────────────────────────────────
    // CÁC TAB
    // ─────────────────────────────────────────────────────────

    private TabbedPaneCustom tabs;

    // Tab Doanh thu
    private ChartPanel chartDoanhThuThang;
    private ChartPanel chartDoanhThuLoai;
    private DefaultTableModel modelDoanhThu;
    private JTable bangDoanhThu;

    // Tab Tour
    private ChartPanel chartTourLapDay;
    private ChartPanel chartTourTrangThai;
    private DefaultTableModel modelTour;
    private JTable bangTour;

    // Tab Khách hàng
    private ChartPanel chartKhachLoai;
    private ChartPanel chartKhachGioiTinh;
    private DefaultTableModel modelKhach;
    private JTable bangKhach;

    // Tab Nhân viên
    private ChartPanel chartNhanVien;
    private ChartPanel chartNhanVienVe;
    private DefaultTableModel modelNhanVien;
    private JTable bangNhanVien;

    // ─────────────────────────────────────────────────────────
    // KHỞI TẠO
    // ─────────────────────────────────────────────────────────

    public ThongKeBaoCaoPanel() {
        this.bus = new BaoCaoBUS();
        LocalDate today = LocalDate.now();
        this.filterHienTai = BaoCaoFilterDTO.builder()
                .tuNgay(LocalDate.of(today.getYear(), 1, 1))
                .denNgay(today)
                .build();
        xayDungGiaoDien();
        khoiTaoFilter();
        taiDuLieuTatCa();
    }

    // ─────────────────────────────────────────────────────────
    // XÂY DỰNG GIAO DIỆN
    // ─────────────────────────────────────────────────────────

    private void xayDungGiaoDien() {
        setLayout(new BorderLayout(0, 0));
        setBackground(C_NEN);

        // PaintComponent thanh gradient trên cùng (dày 6px)
        PaintComponent thanhGradient = new PaintComponent();
        thanhGradient.setPreferredSize(new Dimension(0, 6));

        // Panel tổng NORTH = gradient + tiêu đề + filter
        JPanel panelNorth = new JPanel(new BorderLayout(0, 0));
        panelNorth.setBackground(C_NEN);
        panelNorth.add(thanhGradient, BorderLayout.NORTH);
        panelNorth.add(xayDungTieuDe(), BorderLayout.CENTER);
        panelNorth.add(xayDungFilterBar(), BorderLayout.SOUTH);

        add(panelNorth, BorderLayout.NORTH);

        // Tabs trung tâm
        tabs = new TabbedPaneCustom();
        tabs.setFont(new Font(FONT, Font.BOLD, 12));
        tabs.setSelectedColor(C_CHINH);
        tabs.setBackground(C_NEN);

        tabs.addTab("💰  Doanh thu", xayDungTabDoanhThu());
        tabs.addTab("🗺  Tour", xayDungTabTour());
        tabs.addTab("👥  Khách hàng", xayDungTabKhachHang());
        tabs.addTab("👔  Nhân viên", xayDungTabNhanVien());
        JPanel panelCenter = new JPanel(new BorderLayout());
        panelCenter.setBackground(C_NEN);
        panelCenter.setBorder(new EmptyBorder(0, 10, 10, 10));
        panelCenter.add(tabs, BorderLayout.CENTER);
        add(panelCenter, BorderLayout.CENTER);
    }

    // ─── Tiêu đề ─────────────────────────────────────────────

    private JPanel xayDungTieuDe() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        p.setBackground(C_NEN);
        JLabel lbl = new JLabel("Thống kê & Báo cáo");
        lbl.setFont(new Font(FONT, Font.BOLD, 20));
        lbl.setForeground(C_CHU_TOI);
        p.add(lbl);
        return p;
    }

    // ─── Filter bar ──────────────────────────────────────────

    private JPanel xayDungFilterBar() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(C_TRANG);
        outer.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 0, 1, 0, C_VIEN),
                new EmptyBorder(8, 12, 8, 12)));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
        row1.setBackground(C_TRANG);

        // Từ ngày
        row1.add(labelFilter("Từ ngày:"));
        spinTuNgay = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorTu = new JSpinner.DateEditor(spinTuNgay, "dd/MM/yyyy");
        spinTuNgay.setEditor(editorTu);
        spinTuNgay.setFont(new Font(FONT, Font.PLAIN, 12));
        spinTuNgay.setPreferredSize(new Dimension(110, 28));
        // Init to 1st Jan of current year
        LocalDate today0 = LocalDate.now();
        spinTuNgay.setValue(Date.from(LocalDate.of(today0.getYear(), 1, 1)
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));
        row1.add(spinTuNgay);

        // Đến ngày
        row1.add(labelFilter("Đến ngày:"));
        spinDenNgay = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorDen = new JSpinner.DateEditor(spinDenNgay, "dd/MM/yyyy");
        spinDenNgay.setEditor(editorDen);
        spinDenNgay.setFont(new Font(FONT, Font.PLAIN, 12));
        spinDenNgay.setPreferredSize(new Dimension(110, 28));
        spinDenNgay.setValue(Date.from(today0.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        row1.add(spinDenNgay);

        row1.add(Sep());

        // Loại tour
        row1.add(labelFilter("Loại tour:"));
        cboLoaiTour = new JComboBox<>();
        cboLoaiTour.setFont(new Font(FONT, Font.PLAIN, 12));
        cboLoaiTour.setPreferredSize(new Dimension(155, 28));
        row1.add(cboLoaiTour);

        row1.add(Sep());

        // Trạng thái tour
        row1.add(labelFilter("Trạng thái:"));
        cboTrangThaiTour = new JComboBox<>(new String[] { "Tất cả", "Hoạt động", "Ngừng" });
        cboTrangThaiTour.setFont(new Font(FONT, Font.PLAIN, 12));
        cboTrangThaiTour.setPreferredSize(new Dimension(110, 28));
        row1.add(cboTrangThaiTour);

        row1.add(Box.createHorizontalStrut(16));

        // Nút Lọc
        btnLoc = new ActionButton();
        btnLoc.setText("Lọc");
        btnLoc.setPreferredSize(new Dimension(95, 28));
        btnLoc.addActionListener(e -> apDungFilter());
        row1.add(btnLoc);

        // Nút Làm mới
        btnLamMoi = new ActionButton();
        btnLamMoi.setText("Làm mới");
        btnLamMoi.setColorTop(new Color(127, 140, 141));
        btnLamMoi.setColorBottom(new Color(99, 110, 114));
        btnLamMoi.setPreferredSize(new Dimension(105, 28));
        btnLamMoi.addActionListener(e -> datLaiFilter());
        row1.add(btnLamMoi);

        row1.add(Box.createHorizontalStrut(16));

        // Nút Xuất Excel
        btnXuatExcel = new ActionButton();
        btnXuatExcel.setText("Xuất Excel");
        btnXuatExcel.setColorTop(new Color(39, 174, 96));
        btnXuatExcel.setColorBottom(new Color(27, 124, 66));
        btnXuatExcel.setPreferredSize(new Dimension(125, 28));
        btnXuatExcel.addActionListener(e -> xuatExcel());
        row1.add(btnXuatExcel);

        // Nút Xuất PDF
        btnXuatPdf = new ActionButton();
        btnXuatPdf.setText("Xuất PDF");
        btnXuatPdf.setColorTop(new Color(192, 57, 43));
        btnXuatPdf.setColorBottom(new Color(146, 43, 33));
        btnXuatPdf.setPreferredSize(new Dimension(115, 28));
        btnXuatPdf.addActionListener(e -> xuatPdf());
        row1.add(btnXuatPdf);

        outer.add(row1, BorderLayout.CENTER);
        return outer;
    }

    /** Label nhỏ xám cho filter */
    private JLabel labelFilter(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font(FONT, Font.PLAIN, 12));
        l.setForeground(C_CHU_PHU);
        return l;
    }

    /** Separator mảnh giữa các nhóm filter */
    private JSeparator Sep() {
        JSeparator s = new JSeparator(JSeparator.VERTICAL);
        s.setPreferredSize(new Dimension(1, 22));
        s.setForeground(C_VIEN);
        return s;
    }

    // ─────────────────────────────────────────────────────────
    // TAB 0 — DOANH THU
    // ─────────────────────────────────────────────────────────

    private JPanel xayDungTabDoanhThu() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(C_NEN);
        p.setBorder(new EmptyBorder(8, 0, 0, 0));

        // Phần trên: 2 biểu đồ cạnh nhau
        chartDoanhThuThang = taoChartPanelRong();
        chartDoanhThuLoai = taoChartPanelRong();

        JPanel panelBieuDo = new JPanel(new GridLayout(1, 2, 8, 0));
        panelBieuDo.setBackground(C_NEN);
        panelBieuDo.setPreferredSize(new Dimension(0, 280));
        panelBieuDo.add(baoChartPanel(chartDoanhThuThang, "Doanh thu theo tháng"));
        panelBieuDo.add(baoChartPanel(chartDoanhThuLoai, "Doanh thu theo loại tour"));

        // Phần dưới: bảng chi tiết
        modelDoanhThu = taoModelKhongSua(
                "STT", "Tên tour", "Số hóa đơn", "Số vé", "Doanh thu (VND)", "TB / HĐ (VND)");
        bangDoanhThu = taoBang(modelDoanhThu);
        bangDoanhThu.getColumnModel().getColumn(0).setMaxWidth(45);
        bangDoanhThu.getColumnModel().getColumn(1).setPreferredWidth(220);
        bangDoanhThu.getColumnModel().getColumn(4).setPreferredWidth(130);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                panelBieuDo, baoScroll(bangDoanhThu, "Chi tiết doanh thu theo tour"));
        split.setDividerLocation(290);
        split.setDividerSize(4);
        split.setResizeWeight(0.45);
        split.setBorder(null);
        split.setBackground(C_NEN);

        p.add(split, BorderLayout.CENTER);
        return p;
    }

    // ─────────────────────────────────────────────────────────
    // TAB 2 — TOUR
    // ─────────────────────────────────────────────────────────

    private JPanel xayDungTabTour() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(C_NEN);
        p.setBorder(new EmptyBorder(8, 0, 0, 0));

        chartTourLapDay = taoChartPanelRong();
        chartTourTrangThai = taoChartPanelRong();

        JPanel panelBieuDo = new JPanel(new GridLayout(1, 2, 8, 0));
        panelBieuDo.setBackground(C_NEN);
        panelBieuDo.setPreferredSize(new Dimension(0, 280));
        panelBieuDo.add(baoChartPanel(chartTourLapDay, "Tỷ lệ lấp đầy top 10 tour"));
        panelBieuDo.add(baoChartPanel(chartTourTrangThai, "Phân bổ trạng thái tour"));

        modelTour = taoModelKhongSua(
                "STT", "Tên tour", "Loại", "Điểm KH", "Sức chứa", "Đã đăng ký", "Lấp đầy", "Doanh thu", "Trạng thái");
        bangTour = taoBang(modelTour);
        bangTour.getColumnModel().getColumn(0).setMaxWidth(45);
        bangTour.getColumnModel().getColumn(1).setPreferredWidth(200);
        bangTour.getColumnModel().getColumn(7).setPreferredWidth(130);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                panelBieuDo, baoScroll(bangTour, "Chi tiết hiệu quả tour"));
        split.setDividerLocation(290);
        split.setDividerSize(4);
        split.setResizeWeight(0.45);
        split.setBorder(null);
        split.setBackground(C_NEN);

        p.add(split, BorderLayout.CENTER);
        return p;
    }

    // ─────────────────────────────────────────────────────────
    // TAB 3 — KHÁCH HÀNG
    // ─────────────────────────────────────────────────────────

    private JPanel xayDungTabKhachHang() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(C_NEN);
        p.setBorder(new EmptyBorder(8, 0, 0, 0));

        chartKhachLoai = taoChartPanelRong();
        chartKhachGioiTinh = taoChartPanelRong();

        JPanel panelBieuDo = new JPanel(new GridLayout(1, 2, 8, 0));
        panelBieuDo.setBackground(C_NEN);
        panelBieuDo.setPreferredSize(new Dimension(0, 280));
        panelBieuDo.add(baoChartPanel(chartKhachLoai, "Phân loại khách hàng"));
        panelBieuDo.add(baoChartPanel(chartKhachGioiTinh, "Phân bổ theo giới tính"));

        modelKhach = taoModelKhongSua(
                "STT", "Tên khách hàng", "Số hóa đơn", "Số vé", "Tổng chi tiêu (VND)");
        bangKhach = taoBang(modelKhach);
        bangKhach.getColumnModel().getColumn(0).setMaxWidth(45);
        bangKhach.getColumnModel().getColumn(1).setPreferredWidth(200);
        bangKhach.getColumnModel().getColumn(4).setPreferredWidth(140);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                panelBieuDo, baoScroll(bangKhach, "Top khách hàng chi tiêu cao nhất"));
        split.setDividerLocation(290);
        split.setDividerSize(4);
        split.setResizeWeight(0.45);
        split.setBorder(null);
        split.setBackground(C_NEN);

        p.add(split, BorderLayout.CENTER);
        return p;
    }

    // ─────────────────────────────────────────────────────────
    // TAB 4 — NHÂN VIÊN
    // ─────────────────────────────────────────────────────────

    private JPanel xayDungTabNhanVien() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(C_NEN);
        p.setBorder(new EmptyBorder(8, 0, 0, 0));

        chartNhanVien = taoChartPanelRong();
        chartNhanVienVe = taoChartPanelRong();

        JPanel panelBieuDo = new JPanel(new GridLayout(1, 2, 8, 0));
        panelBieuDo.setBackground(C_NEN);
        panelBieuDo.setPreferredSize(new Dimension(0, 280));
        panelBieuDo.add(baoChartPanel(chartNhanVien, "Doanh thu theo nhân viên (triệu VND)"));
        panelBieuDo.add(baoChartPanel(chartNhanVienVe, "Số vé bán theo nhân viên"));

        modelNhanVien = taoModelKhongSua(
                "STT", "Tên nhân viên", "Chức vụ", "Số hóa đơn", "Số tour bán", "Số vé", "Doanh thu (VND)");
        bangNhanVien = taoBang(modelNhanVien);
        bangNhanVien.getColumnModel().getColumn(0).setMaxWidth(45);
        bangNhanVien.getColumnModel().getColumn(1).setPreferredWidth(170);
        bangNhanVien.getColumnModel().getColumn(6).setPreferredWidth(130);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                panelBieuDo, baoScroll(bangNhanVien, "Chi tiết bán hàng theo nhân viên"));
        split.setDividerLocation(290);
        split.setDividerSize(4);
        split.setResizeWeight(0.45);
        split.setBorder(null);
        split.setBackground(C_NEN);

        p.add(split, BorderLayout.CENTER);
        return p;
    }

    // ─────────────────────────────────────────────────────────
    // KHỞI TẠO FILTER — điền ComboBox từ DB
    // ─────────────────────────────────────────────────────────

    private void khoiTaoFilter() {
        new SwingWorker<Void, Void>() {
            private Map<String, String> dsLoaiTour;

            @Override
            protected Void doInBackground() throws Exception {
                dsLoaiTour = bus.getDanhSachLoaiTour();
                mapLoaiTour = dsLoaiTour;
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    // Điền loại tour
                    cboLoaiTour.removeAllItems();
                    for (String ten : dsLoaiTour.values())
                        cboLoaiTour.addItem(ten);
                } catch (Exception ex) {
                    // Filter vẫn hoạt động với dữ liệu mặc định
                }
            }
        }.execute();
    }

    // ─────────────────────────────────────────────────────────
    // TẢI DỮ LIỆU
    // ─────────────────────────────────────────────────────────

    /** Tải & cập nhật tất cả các tab dựa theo filter hiện tại */
    private void taiDuLieuTatCa() {
        taiTabDoanhThu();
        taiTabTour();
        taiTabKhachHang();
        taiTabNhanVien();
    }

    // ── Doanh thu ─────────────────────────────────────────────

    private void taiTabDoanhThu() {
        new SwingWorker<Void, Void>() {
            List<BaoCaoDoanhThuRowDTO> dsThang;
            List<BaoCaoDoanhThuRowDTO> dsLoai;
            List<BaoCaoDoanhThuRowDTO> dsTour;

            @Override
            protected Void doInBackground() throws Exception {
                dsThang = bus.getDoanhThuTheoThang(filterHienTai);
                dsLoai = bus.getDoanhThuTheoLoaiTour(filterHienTai);
                dsTour = bus.getDoanhThuTheoTour(filterHienTai);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();

                    // Biểu đồ cột — doanh thu theo tháng
                    DefaultCategoryDataset dsThangChart = new DefaultCategoryDataset();
                    for (BaoCaoDoanhThuRowDTO r : dsThang) {
                        dsThangChart.addValue(r.getTongDoanhThu() / 1_000_000.0,
                                "Doanh thu (triệu VND)", r.getNhan());
                    }
                    JFreeChart chartThang = ChartFactory.createBarChart(
                            null, null, "Triệu VND",
                            dsThangChart, PlotOrientation.VERTICAL, false, true, false);
                    apDungStyleCot(chartThang, C_CHINH);
                    chartDoanhThuThang.setChart(chartThang);

                    // Biểu đồ tròn — doanh thu theo loại tour
                    DefaultPieDataset<String> dsLoaiChart = new DefaultPieDataset<>();
                    for (BaoCaoDoanhThuRowDTO r : dsLoai) {
                        if (r.getTongDoanhThu() > 0)
                            dsLoaiChart.setValue(r.getNhan(), r.getTongDoanhThu());
                    }
                    JFreeChart chartLoai = ChartFactory.createPieChart(null, dsLoaiChart, true, true, false);
                    apDungStylePie(chartLoai);
                    chartDoanhThuLoai.setChart(chartLoai);

                    // Bảng chi tiết tour
                    modelDoanhThu.setRowCount(0);
                    int stt = 1;
                    for (BaoCaoDoanhThuRowDTO r : dsTour) {
                        modelDoanhThu.addRow(new Object[] {
                                stt++, r.getNhan(), r.getSoHoaDon(), r.getSoVe(),
                                FMT.format(r.getTongDoanhThu()) + " ₫",
                                FMT.format(r.getGiaTrungBinh()) + " ₫"
                        });
                    }
                } catch (Exception ex) {
                    hienThiLoi("Không tải được dữ liệu doanh thu: " + ex.getMessage());
                }
            }
        }.execute();
    }

    // ── Tour ──────────────────────────────────────────────────

    private void taiTabTour() {
        new SwingWorker<Void, Void>() {
            List<BaoCaoTourRowDTO> ds;

            @Override
            protected Void doInBackground() throws Exception {
                ds = bus.getThongKeTour(filterHienTai);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();

                    // Biểu đồ ngang — top 10 tour tỷ lệ lấp đầy
                    DefaultCategoryDataset dsLapDay = new DefaultCategoryDataset();
                    int cnt = 0;
                    for (BaoCaoTourRowDTO r : ds) {
                        if (cnt++ >= 10)
                            break;
                        // Rút ngắn tên tour cho biểu đồ
                        String ten = r.getTenTour().length() > 25
                                ? r.getTenTour().substring(0, 23) + "…"
                                : r.getTenTour();
                        dsLapDay.addValue(r.getTyLeLapDay(), "Lấp đầy (%)", ten);
                    }
                    JFreeChart chartLap = ChartFactory.createBarChart(
                            null, null, "%",
                            dsLapDay, PlotOrientation.HORIZONTAL, false, true, false);
                    apDungStyleCot(chartLap, C_XANH_LA);
                    chartTourLapDay.setChart(chartLap);

                    // Biểu đồ tròn — trạng thái
                    int[] dem = bus.demTourTheoTrangThai(filterHienTai);
                    DefaultPieDataset<String> dsTrangThai = new DefaultPieDataset<>();
                    dsTrangThai.setValue("Hoạt động", dem[0]);
                    dsTrangThai.setValue("Ngừng", dem[1]);
                    dsTrangThai.setValue("Đã khởi hành", dem[2]);
                    JFreeChart chartTT = ChartFactory.createPieChart(null, dsTrangThai, true, true, false);
                    apDungStylePie(chartTT);
                    @SuppressWarnings({ "unchecked", "rawtypes" })
                    PiePlot pptt = (PiePlot) chartTT.getPlot();
                    pptt.setSectionPaint("Hoạt động", C_CHINH);
                    pptt.setSectionPaint("Ngừng", C_DO);
                    pptt.setSectionPaint("Đã khởi hành", C_XANH_LA);
                    chartTourTrangThai.setChart(chartTT);

                    // Bảng
                    modelTour.setRowCount(0);
                    int stt = 1;
                    for (BaoCaoTourRowDTO r : ds) {
                        modelTour.addRow(new Object[] {
                                stt++, r.getTenTour(), r.getTenLoaiTour(), r.getNoiKhoiHanh(),
                                r.getSoLuongToiDa(), r.getSoVeDaBan(),
                                String.format("%.1f%%", r.getTyLeLapDay()),
                                FMT.format(r.getDoanhThu()) + " ₫",
                                r.isTrangThai() ? "Hoạt động" : "Ngừng"
                        });
                    }
                } catch (Exception ex) {
                    hienThiLoi("Không tải được dữ liệu tour: " + ex.getMessage());
                }
            }
        }.execute();
    }

    // ── Khách hàng ────────────────────────────────────────────

    private void taiTabKhachHang() {
        new SwingWorker<Void, Void>() {
            List<BaoCaoKhachHangRowDTO> dsLoai;
            List<BaoCaoKhachHangRowDTO> dsGioiTinh;
            List<BaoCaoKhachHangRowDTO> dsTop;

            @Override
            protected Void doInBackground() throws Exception {
                dsLoai = bus.getKhachTheoLoai();
                dsGioiTinh = bus.getKhachTheoGioiTinh();
                dsTop = bus.getTopKhachChiTieu(filterHienTai);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();

                    // Biểu đồ tròn — phân loại KH
                    DefaultPieDataset<String> dsLoaiChart = new DefaultPieDataset<>();
                    for (BaoCaoKhachHangRowDTO r : dsLoai) {
                        if (r.getSoLuong() > 0)
                            dsLoaiChart.setValue(r.getNhan(), r.getSoLuong());
                    }
                    JFreeChart chartLoai = ChartFactory.createPieChart(null, dsLoaiChart, true, true, false);
                    apDungStylePie(chartLoai);
                    chartKhachLoai.setChart(chartLoai);

                    // Biểu đồ tròn — giới tính
                    DefaultPieDataset<String> dsGTChart = new DefaultPieDataset<>();
                    for (BaoCaoKhachHangRowDTO r : dsGioiTinh) {
                        if (r.getSoLuong() > 0)
                            dsGTChart.setValue(r.getNhan(), r.getSoLuong());
                    }
                    JFreeChart chartGT = ChartFactory.createPieChart(null, dsGTChart, true, true, false);
                    apDungStylePie(chartGT);
                    @SuppressWarnings({ "unchecked", "rawtypes" })
                    PiePlot ppgt = (PiePlot) chartGT.getPlot();
                    ppgt.setSectionPaint("Nam", C_CHINH);
                    ppgt.setSectionPaint("Nữ", C_DO);
                    ppgt.setSectionPaint("Không rõ", C_CHU_PHU);
                    chartKhachGioiTinh.setChart(chartGT);

                    // Bảng top khách
                    modelKhach.setRowCount(0);
                    int stt = 1;
                    for (BaoCaoKhachHangRowDTO r : dsTop) {
                        modelKhach.addRow(new Object[] {
                                stt++, r.getNhan(), r.getSoHoaDon(), r.getSoLuong(),
                                FMT.format(r.getTongChiTieu()) + " ₫"
                        });
                    }
                } catch (Exception ex) {
                    hienThiLoi("Không tải được dữ liệu khách hàng: " + ex.getMessage());
                }
            }
        }.execute();
    }

    // ── Nhân viên ─────────────────────────────────────────────

    private void taiTabNhanVien() {
        new SwingWorker<Void, Void>() {
            List<BaoCaoNhanVienRowDTO> ds;

            @Override
            protected Void doInBackground() throws Exception {
                ds = bus.getDoanhThuTheoNhanVien(filterHienTai);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();

                    // Biểu đồ 1: Doanh thu theo NV
                    DefaultCategoryDataset dsDoanhThu = new DefaultCategoryDataset();
                    // Biểu đồ 2: Số vé theo NV
                    DefaultCategoryDataset dsSoVe = new DefaultCategoryDataset();
                    for (BaoCaoNhanVienRowDTO r : ds) {
                        String ten = r.getTenNhanVien().length() > 18
                                ? r.getTenNhanVien().substring(0, 16) + "…"
                                : r.getTenNhanVien();
                        dsDoanhThu.addValue(r.getTongDoanhThu() / 1_000_000.0, "Doanh thu (triệu VND)", ten);
                        dsSoVe.addValue(r.getSoVe(), "Số vé", ten);
                    }

                    JFreeChart chartDT = ChartFactory.createBarChart(
                            null, null, "Triệu VND",
                            dsDoanhThu, PlotOrientation.VERTICAL, false, true, false);
                    apDungStyleCot(chartDT, C_CHINH);
                    chartNhanVien.setChart(chartDT);

                    JFreeChart chartVe = ChartFactory.createBarChart(
                            null, null, "Số vé",
                            dsSoVe, PlotOrientation.VERTICAL, false, true, false);
                    apDungStyleCot(chartVe, C_XANH_LA);
                    chartNhanVienVe.setChart(chartVe);

                    // Bảng
                    modelNhanVien.setRowCount(0);
                    int stt = 1;
                    for (BaoCaoNhanVienRowDTO r : ds) {
                        modelNhanVien.addRow(new Object[] {
                                stt++, r.getTenNhanVien(), r.getTenChucVu(),
                                r.getSoHoaDon(), r.getSoTourBan(), r.getSoVe(),
                                FMT.format(r.getTongDoanhThu()) + " ₫"
                        });
                    }
                } catch (Exception ex) {
                    hienThiLoi("Không tải được dữ liệu nhân viên: " + ex.getMessage());
                }
            }
        }.execute();
    }

    // ─────────────────────────────────────────────────────────
    // XỬ LÝ FILTER
    // ─────────────────────────────────────────────────────────

    /** Đọc từ spinner ngày, xây BaoCaoFilterDTO, reload tất cả tab */
    private void apDungFilter() {
        // Đọc ngày từ spinner
        LocalDate tuNgay = ((Date) spinTuNgay.getValue()).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate denNgay = ((Date) spinDenNgay.getValue()).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();

        // Validate: ngày đầu phải <= ngày cuối
        if (tuNgay.isAfter(denNgay)) {
            JOptionPane.showMessageDialog(this,
                    "Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc!",
                    "Lỗi khoảng ngày", JOptionPane.WARNING_MESSAGE);
            return;
        }

        BaoCaoFilterDTO.BaoCaoFilterDTOBuilder b = BaoCaoFilterDTO.builder()
                .tuNgay(tuNgay)
                .denNgay(denNgay);

        // Loại tour — tra ngược trong map để lấy mã
        if (mapLoaiTour != null) {
            String tenChon = (String) cboLoaiTour.getSelectedItem();
            mapLoaiTour.forEach((ma, ten) -> {
                if (ten.equals(tenChon) && !ma.isBlank())
                    b.maLoaiTour(ma);
            });
        }

        // Trạng thái tour
        int idxTT = cboTrangThaiTour.getSelectedIndex();
        if (idxTT == 1)
            b.trangThaiTour(true);
        else if (idxTT == 2)
            b.trangThaiTour(false);

        filterHienTai = b.build();
        taiDuLieuTatCa();
    }

    /** Đặt lại filter về mặc định (đầu năm hiện tại → hôm nay, tất cả) */
    private void datLaiFilter() {
        LocalDate today = LocalDate.now();
        LocalDate dauNam = LocalDate.of(today.getYear(), 1, 1);
        spinTuNgay.setValue(Date.from(dauNam.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        spinDenNgay.setValue(Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        if (cboLoaiTour.getItemCount() > 0)
            cboLoaiTour.setSelectedIndex(0);
        cboTrangThaiTour.setSelectedIndex(0);
        filterHienTai = BaoCaoFilterDTO.builder()
                .tuNgay(dauNam)
                .denNgay(today)
                .build();
        taiDuLieuTatCa();
    }

    // ─────────────────────────────────────────────────────────
    // XUẤT BÁO CÁO
    // ─────────────────────────────────────────────────────────

    private void xuatExcel() {
        // Xuất dựa theo tab đang chọn
        int tabIdx = tabs.getSelectedIndex();
        new SwingWorker<File, Void>() {
            @Override
            protected File doInBackground() throws Exception {
                switch (tabIdx) {
                    case 1:
                        return BaoCaoExportUtil.xuatExcelDoanhThuThang(
                                bus.getDoanhThuTheoThang(filterHienTai), filterHienTai);
                    case 2:
                        return BaoCaoExportUtil.xuatExcelTour(
                                bus.getThongKeTour(filterHienTai));
                    case 3:
                        return BaoCaoExportUtil.xuatExcelKhachHang(
                                bus.getAllKhachHangChiTiet(filterHienTai));
                    case 4:
                        return BaoCaoExportUtil.xuatExcelNhanVien(
                                bus.getDoanhThuTheoNhanVien(filterHienTai));
                    default:
                        return BaoCaoExportUtil.xuatExcelDoanhThuThang(
                                bus.getDoanhThuTheoThang(filterHienTai), filterHienTai);
                }
            }

            @Override
            protected void done() {
                try {
                    File f = get();
                    // Mở hộp thoại lưu file để người dùng chọn nơi lưu
                    JFileChooser fc = new JFileChooser();
                    fc.setSelectedFile(new File(f.getName()));
                    fc.setDialogTitle("Lưu báo cáo Excel");
                    if (fc.showSaveDialog(ThongKeBaoCaoPanel.this) == JFileChooser.APPROVE_OPTION) {
                        java.nio.file.Files.copy(f.toPath(),
                                fc.getSelectedFile().toPath(),
                                java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        JOptionPane.showMessageDialog(ThongKeBaoCaoPanel.this,
                                "Xuất Excel thành công:\n" + fc.getSelectedFile().getAbsolutePath(),
                                "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    }
                    f.delete(); // Xóa temp file
                } catch (Exception ex) {
                    hienThiLoi("Xuất Excel thất bại:\n" + ex.getMessage());
                }
            }
        }.execute();
    }

    private void xuatPdf() {
        new SwingWorker<File, Void>() {
            @Override
            protected File doInBackground() throws Exception {
                return BaoCaoExportUtil.xuatPdfDoanhThuThang(
                        bus.getDoanhThuTheoThang(filterHienTai), filterHienTai);
            }

            @Override
            protected void done() {
                try {
                    File f = get();
                    JFileChooser fc = new JFileChooser();
                    fc.setSelectedFile(new File(f.getName()));
                    fc.setDialogTitle("Lưu báo cáo PDF");
                    if (fc.showSaveDialog(ThongKeBaoCaoPanel.this) == JFileChooser.APPROVE_OPTION) {
                        java.nio.file.Files.copy(f.toPath(),
                                fc.getSelectedFile().toPath(),
                                java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        JOptionPane.showMessageDialog(ThongKeBaoCaoPanel.this,
                                "Xuất PDF thành công:\n" + fc.getSelectedFile().getAbsolutePath(),
                                "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    }
                    f.delete();
                } catch (Exception ex) {
                    hienThiLoi("Xuất PDF thất bại:\n" + ex.getMessage());
                }
            }
        }.execute();
    }

    // ─────────────────────────────────────────────────────────
    // HELPER — biểu đồ
    // ─────────────────────────────────────────────────────────

    /** Tạo ChartPanel trắng rỗng, responsive, không border */
    private ChartPanel taoChartPanelRong() {
        ChartPanel cp = new ChartPanel(null);
        cp.setMinimumDrawWidth(0);
        cp.setMinimumDrawHeight(0);
        cp.setMaximumDrawWidth(Integer.MAX_VALUE);
        cp.setMaximumDrawHeight(Integer.MAX_VALUE);
        cp.setBackground(C_TRANG);
        return cp;
    }

    /** Bọc ChartPanel trong panel có tiêu đề section + viền */
    private JPanel baoChartPanel(ChartPanel cp, String tieuDe) {
        JPanel wrapper = new JPanel(new BorderLayout(0, 4));
        wrapper.setBackground(C_TRANG);
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_VIEN, 1),
                new EmptyBorder(6, 6, 6, 6)));

        // Thanh gradient 4px đầu section
        PaintComponent pc = new PaintComponent();
        pc.setPreferredSize(new Dimension(0, 4));

        JLabel lbl = new JLabel(tieuDe);
        lbl.setFont(new Font(FONT, Font.BOLD, 12));
        lbl.setForeground(C_CHU_TOI);
        lbl.setBorder(new EmptyBorder(0, 0, 4, 0));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(C_TRANG);
        headerPanel.add(pc, BorderLayout.NORTH);
        headerPanel.add(lbl, BorderLayout.SOUTH);

        wrapper.add(headerPanel, BorderLayout.NORTH);
        wrapper.add(cp, BorderLayout.CENTER);
        return wrapper;
    }

    /** Bọc JTable trong ScrollPaneWin11 có tiêu đề */
    private JPanel baoScroll(JTable table, String tieuDe) {
        JPanel wrapper = new JPanel(new BorderLayout(0, 4));
        wrapper.setBackground(C_NEN);

        JLabel lbl = new JLabel(tieuDe);
        lbl.setFont(new Font(FONT, Font.BOLD, 12));
        lbl.setForeground(C_CHU_TOI);
        lbl.setBorder(new EmptyBorder(6, 2, 4, 0));

        ScrollPaneWin11 scroll = new ScrollPaneWin11();
        scroll.setViewportView(table);
        scroll.setBorder(BorderFactory.createLineBorder(C_VIEN, 1));

        wrapper.add(lbl, BorderLayout.NORTH);
        wrapper.add(scroll, BorderLayout.CENTER);
        return wrapper;
    }

    /** Áp dụng style đồng bộ cho biểu đồ cột */
    private void apDungStyleCot(JFreeChart chart, Color mauCot) {
        chart.setBackgroundPaint(C_TRANG);
        if (chart.getLegend() != null)
            chart.getLegend().setBackgroundPaint(C_TRANG);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(C_TRANG);
        plot.setOutlineVisible(false);
        plot.setRangeGridlinePaint(new Color(220, 220, 220));
        plot.setDomainGridlinesVisible(false);

        BarRenderer rdr = (BarRenderer) plot.getRenderer();
        rdr.setDefaultPaint(mauCot);
        rdr.setSeriesPaint(0, mauCot);
        rdr.setBarPainter(new StandardBarPainter());
        rdr.setShadowVisible(false);
        rdr.setDefaultItemLabelsVisible(false);
        rdr.setItemMargin(0.05);
        rdr.setMaximumBarWidth(0.12);

        CategoryAxis da = plot.getDomainAxis();
        da.setTickLabelFont(new Font(FONT, Font.PLAIN, 10));
        da.setLabelFont(new Font(FONT, Font.PLAIN, 11));
        da.setTickLabelPaint(C_CHU_TOI);

        NumberAxis na = (NumberAxis) plot.getRangeAxis();
        na.setTickLabelFont(new Font(FONT, Font.PLAIN, 10));
        na.setLabelFont(new Font(FONT, Font.PLAIN, 11));
    }

    /** Áp dụng style đồng bộ cho biểu đồ tròn */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void apDungStylePie(JFreeChart chart) {
        chart.setBackgroundPaint(C_TRANG);
        if (chart.getLegend() != null)
            chart.getLegend().setBackgroundPaint(C_TRANG);

        PiePlot pp = (PiePlot) chart.getPlot();
        pp.setBackgroundPaint(C_TRANG);
        pp.setOutlineVisible(false);
        pp.setDefaultSectionOutlinePaint(new Color(0, 0, 0, 0));
        pp.setLabelFont(new Font(FONT, Font.PLAIN, 11));
        pp.setLabelBackgroundPaint(new Color(255, 255, 255, 200));
        pp.setLabelOutlinePaint(null);
        pp.setLabelShadowPaint(null);
        pp.setShadowPaint(null);

        // Tô màu đồng bộ theo BANG_MAU
        for (int i = 0; i < BANG_MAU.length; i++) {
            int idx = i;
            // Không thể trực tiếp set theo index → sẽ dùng key từ dataset nếu cần
        }
    }

    // ─────────────────────────────────────────────────────────
    // HELPER — bảng
    // ─────────────────────────────────────────────────────────

    /** Tạo DefaultTableModel không cho sửa cell */
    private DefaultTableModel taoModelKhongSua(String... cols) {
        return new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
    }

    /** Tạo JTable có sort + style chuẩn */
    private JTable taoBang(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(new Font(FONT, Font.PLAIN, 12));
        table.setRowHeight(32);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(C_CHON);
        table.setSelectionForeground(C_CHU_TOI);
        table.setFillsViewportHeight(true);

        // Sắp xếp theo cột
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        // Gradient header
        apDungGradientHeader(table);

        // Renderer xen kẽ màu + căn phải cho cột số tiền
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setFont(new Font(FONT, Font.PLAIN, 12));
                setForeground(C_CHU_TOI);
                setBorder(new EmptyBorder(0, 8, 0, 8));
                setOpaque(true);
                if (!sel)
                    setBackground(row % 2 == 0 ? C_TRANG : C_HANG_XEN);

                // Cột STT căn giữa
                if (col == 0)
                    setHorizontalAlignment(CENTER);
                // Cột chứa "₫" căn giữa
                else if (v instanceof String && v.toString().contains("₫"))
                    setHorizontalAlignment(CENTER);
                else
                    setHorizontalAlignment(LEFT);

                // Màu trạng thái
                if (v instanceof String) {
                    String s = v.toString();
                    if ("Hoạt động".equals(s))
                        setForeground(C_XANH_LA);
                    else if ("Ngừng".equals(s))
                        setForeground(C_DO);
                }
                return this;
            }
        });
        return table;
    }

    /** Gradient header giống LichTrinhPanel */
    private void apDungGradientHeader(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 38));
        header.setFont(new Font(FONT, Font.BOLD, 12));
        header.setBorder(BorderFactory.createEmptyBorder());
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSel, boolean hasFoc, int row, int col) {
                JLabel label = new JLabel(value != null ? value.toString() : "") {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        int totalW = t.getTableHeader().getWidth();
                        int xOff = 0;
                        for (int i = 0; i < col; i++)
                            xOff += t.getColumnModel().getColumn(i).getWidth();
                        GradientPaint gp = new GradientPaint(
                                -xOff, 0, new Color(0x2980B9),
                                totalW - xOff, 0, new Color(0x6DD5FA));
                        g2.setPaint(gp);
                        g2.fillRect(0, 0, getWidth(), getHeight());
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                label.setFont(new Font(FONT, Font.BOLD, 12));
                label.setForeground(Color.WHITE);
                label.setOpaque(false);
                label.setHorizontalAlignment(col == 0 ? JLabel.CENTER : JLabel.LEFT);
                label.setVerticalAlignment(JLabel.CENTER);
                label.setBorder(new EmptyBorder(0, 8, 0, 8));
                return label;
            }
        });
    }

    // ─────────────────────────────────────────────────────────
    // THÔNG BÁO
    // ─────────────────────────────────────────────────────────

    private void hienThiLoi(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
