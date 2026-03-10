package GUI;

import DAL.ConnectionDAL;
import DAL.DiaDiemDAL;
import DAL.LichTrinhDAL;
import DAL.PhuongTienDAL;
import DAL.TourDAL;
import DTO.DiaDiem;
import DTO.LichTrinh;
import DTO.PhuongTien;
import DTO.Tour;
import GUI.Menu.ActionButton;
import GUI.Menu.PaintComponent;
import GUI.Menu.TabbedPaneCustom;
import GUI.ScrollPane.ScrollPaneWin11;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Panel Quản lý Lịch trình — CRUD đầy đủ.
 *
 * Bố cục (BorderLayout):
 * - NORTH : Thanh tìm kiếm (txtTimKiem + btnTimKiem + lblTongSo)
 * - CENTER : Bảng dữ liệu (trái) + Form nhập liệu (phải 310px)
 *
 * Form: Mã lịch trình (tự động), Tour, Địa điểm, Phương tiện,
 * Ngày thứ (spinner), Nội dung (textarea), Trạng thái (checkbox)
 * Nút: Thêm mới | Cập nhật | Xóa | Làm mới
 *
 * TODO: kết nối BUS khi sẵn sàng (xem comment "TODO" trong từng method)
 */
public class LichTrinhPanel extends JPanel {

    // =========================================================
    // HẰNG SỐ GIAO DIỆN
    // =========================================================

    private static final Color MAU_CHINH = new Color(41, 128, 185);
    private static final Color MAU_NEN = new Color(246, 247, 248);
    private static final Color MAU_TRANG = Color.WHITE;
    private static final Color MAU_HANG_XEN = new Color(240, 246, 252);
    private static final Color MAU_CHON = new Color(214, 234, 248);
    private static final Color MAU_CHU_TOI = new Color(44, 62, 80);
    private static final Color MAU_CHU_PHU = new Color(127, 140, 141);
    private static final Color MAU_VIEN = new Color(213, 219, 219);
    private static final Color MAU_XANH_LA = new Color(39, 174, 96);
    private static final Color MAU_DO = new Color(231, 76, 60);
    private static final String FONT = "Segoe UI";

    // =========================================================
    // TRẠNG THÁI
    // =========================================================

    /** Mã lịch trình đang được chọn (null = chế độ thêm mới) */
    private String maLichTrinhDangChon = null;

    // =========================================================
    // THÀNH PHẦN GIAO DIỆN
    // =========================================================

    // Thanh tìm kiếm
    private JTextField txtTimKiem;
    private ActionButton btnTimKiem;
    private JLabel lblTongSo;

    // Bảng
    private JTable bangDuLieu;
    private DefaultTableModel modelBang;

    // Form nhập liệu
    private JTextField txtMaLT; // Mã lịch trình — tự động, read-only
    private JComboBox<String> cboTour; // ComboBox chọn Tour
    private JComboBox<String> cboDiaDiem; // ComboBox chọn Địa điểm
    private JComboBox<String> cboPhuongTien; // ComboBox chọn Phương tiện
    private JSpinner spnNgayThu; // Số ngày thứ trong tour
    private JTextArea txtaNoiDung; // Nội dung hoạt động trong ngày
    private JCheckBox chkTrangThai; // Trạng thái hoạt động

    // Nút thao tác
    private ActionButton btnThemMoi;
    private ActionButton btnCapNhat;
    private ActionButton btnXoa;
    private ActionButton btnLamMoi;

    // =========================================================
    // DAL & DỮ LIỆU COMBO
    // =========================================================

    private LichTrinhDAL dal;
    private TourDAL tourDal;
    private DiaDiemDAL ddiaDiemDal;
    private PhuongTienDAL ptDal;

    /** tenTour → maTour */
    private final Map<String, String> mapTour = new LinkedHashMap<>();
    /** tenDiaDiem → maDiaDiem */
    private final Map<String, String> mapDiaDiem = new LinkedHashMap<>();
    /** tenPT → maPT */
    private final Map<String, String> mapPhuongTien = new LinkedHashMap<>();

    // =========================================================
    // KHỞI TẠO
    // =========================================================

    public LichTrinhPanel() {
        // Khởi tạo kết nối DB
        Connection conn = new ConnectionDAL().getConnection();
        dal = new LichTrinhDAL(conn);
        tourDal = new TourDAL();
        ddiaDiemDal = new DiaDiemDAL();
        ptDal = new PhuongTienDAL(conn);

        xayDungGiaoDien();
        khoiTaoCombos();
        taiDuLieu(null);
        resetFormThemMoi();
    }

    // =========================================================
    // KHỞI TẠO COMBO TỪ DB
    // =========================================================

    private void khoiTaoCombos() {
        try {
            // Tour
            cboTour.removeAllItems();
            mapTour.clear();
            for (Tour t : tourDal.getAllTour()) {
                mapTour.put(t.getTenTour(), t.getMaTour());
                cboTour.addItem(t.getTenTour());
            }
            // Địa điểm
            cboDiaDiem.removeAllItems();
            mapDiaDiem.clear();
            for (DiaDiem d : ddiaDiemDal.getAll()) {
                mapDiaDiem.put(d.getTenDiaDiem(), d.getMaDiaDiem());
                cboDiaDiem.addItem(d.getTenDiaDiem());
            }
            // Phương tiện
            cboPhuongTien.removeAllItems();
            mapPhuongTien.clear();
            for (PhuongTien p : ptDal.getAll()) {
                mapPhuongTien.put(p.getTenPT(), p.getMaPT());
                cboPhuongTien.addItem(p.getTenPT());
            }
        } catch (Exception e) {
            hienThiLoi("Lỗi tải dữ liệu combo: " + e.getMessage());
        }
    }

    // =========================================================
    // XÂY DỰNG GIAO DIỆN
    // =========================================================

    private void xayDungGiaoDien() {
        setBackground(MAU_NEN);
        setLayout(new BorderLayout());

        TabbedPaneCustom tabs = new TabbedPaneCustom();
        tabs.setFont(new Font(FONT, Font.BOLD, 13));
        tabs.setSelectedColor(MAU_CHINH);

        JPanel panelTab = new JPanel(new BorderLayout(0, 8));
        panelTab.setBackground(MAU_NEN);
        panelTab.setBorder(new EmptyBorder(10, 12, 10, 12));

        panelTab.add(xayDungThanhTimKiem(), BorderLayout.NORTH);
        panelTab.add(xayDungVungChinh(), BorderLayout.CENTER);

        tabs.addTab("Lịch trình", panelTab);
        add(tabs, BorderLayout.CENTER);
    }

    // ---------------------------------------------------------
    // THANH TÌM KIẾM
    // ---------------------------------------------------------

    private JPanel xayDungThanhTimKiem() {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setBackground(MAU_NEN);
        panel.setBorder(new EmptyBorder(0, 0, 4, 0));

        JPanel panelTrai = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panelTrai.setBackground(MAU_NEN);

        JLabel lblTieuDe = new JLabel("Quản lý Lịch trình");
        lblTieuDe.setFont(new Font(FONT, Font.BOLD, 18));
        lblTieuDe.setForeground(MAU_CHU_TOI);

        txtTimKiem = new JTextField(22);
        txtTimKiem.setFont(new Font(FONT, Font.PLAIN, 13));
        txtTimKiem.setPreferredSize(new Dimension(220, 32));
        txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MAU_VIEN, 1),
                new EmptyBorder(2, 8, 2, 8)));
        txtTimKiem.setToolTipText("Tìm theo tên tour hoặc địa điểm...");
        txtTimKiem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    taiDuLieu(txtTimKiem.getText());
            }
        });

        btnTimKiem = new ActionButton();
        btnTimKiem.setText("🔍  Tìm kiếm");
        btnTimKiem.setPreferredSize(new Dimension(120, 32));
        btnTimKiem.addActionListener(e -> taiDuLieu(txtTimKiem.getText()));

        panelTrai.add(lblTieuDe);
        panelTrai.add(Box.createHorizontalStrut(12));
        panelTrai.add(txtTimKiem);
        panelTrai.add(btnTimKiem);

        lblTongSo = new JLabel("Tổng: 0 bản ghi");
        lblTongSo.setFont(new Font(FONT, Font.ITALIC, 12));
        lblTongSo.setForeground(MAU_CHU_PHU);
        lblTongSo.setBorder(new EmptyBorder(0, 0, 0, 4));

        panel.add(panelTrai, BorderLayout.WEST);
        panel.add(lblTongSo, BorderLayout.EAST);
        return panel;
    }

    // ---------------------------------------------------------
    // VÙNG CHÍNH
    // ---------------------------------------------------------

    private JPanel xayDungVungChinh() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(MAU_NEN);
        panel.add(xayDungBang(), BorderLayout.CENTER);
        panel.add(xayDungFormNhap(), BorderLayout.EAST);
        return panel;
    }

    // ---------------------------------------------------------
    // BẢNG DỮ LIỆU
    // ---------------------------------------------------------

    private ScrollPaneWin11 xayDungBang() {
        modelBang = new DefaultTableModel(
                new String[] { "STT", "Mã LT", "Tour", "Địa điểm", "Phương tiện", "Ngày thứ" }, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        bangDuLieu = new JTable(modelBang);
        bangDuLieu.setFont(new Font(FONT, Font.PLAIN, 13));
        bangDuLieu.setRowHeight(36);
        bangDuLieu.setShowGrid(false);
        bangDuLieu.setIntercellSpacing(new Dimension(0, 0));
        bangDuLieu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bangDuLieu.setSelectionBackground(MAU_CHON);
        bangDuLieu.setSelectionForeground(MAU_CHU_TOI);
        bangDuLieu.setFillsViewportHeight(true);
        bangDuLieu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Độ rộng cột
        bangDuLieu.getColumnModel().getColumn(0).setMaxWidth(45);
        bangDuLieu.getColumnModel().getColumn(1).setPreferredWidth(90);
        bangDuLieu.getColumnModel().getColumn(2).setPreferredWidth(220);
        bangDuLieu.getColumnModel().getColumn(3).setPreferredWidth(180);
        bangDuLieu.getColumnModel().getColumn(4).setPreferredWidth(160);
        bangDuLieu.getColumnModel().getColumn(5).setPreferredWidth(90);
        bangDuLieu.getColumnModel().getColumn(5).setMinWidth(80);
        bangDuLieu.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Auto-sort: bản ghi mới nhất (STT lớn nhất) lên đầu
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelBang);
        sorter.setSortKeys(List.of(new RowSorter.SortKey(0, SortOrder.DESCENDING)));
        sorter.setSortsOnUpdates(true);
        bangDuLieu.setRowSorter(sorter);

        // Header gradient
        xayDungHeaderBang(bangDuLieu);

        // Renderer xen kẽ màu + căn giữa cột số/trạng thái
        bangDuLieu.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setFont(new Font(FONT, Font.PLAIN, 13));
                setForeground(MAU_CHU_TOI);
                setBorder(new EmptyBorder(0, 8, 0, 8));
                setOpaque(true);
                if (!sel)
                    setBackground(row % 2 == 0 ? MAU_TRANG : MAU_HANG_XEN);

                if (col == 0 || col == 5)
                    setHorizontalAlignment(CENTER);
                else
                    setHorizontalAlignment(LEFT);
                return this;
            }
        });

        // Click hàng → điền form
        bangDuLieu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = bangDuLieu.getSelectedRow();
                if (row >= 0)
                    dienFormTuHang(row);
            }
        });

        ScrollPaneWin11 scroll = new ScrollPaneWin11();
        scroll.setViewportView(bangDuLieu);
        scroll.getViewport().setBackground(MAU_TRANG);
        return scroll;
    }

    private void xayDungHeaderBang(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(0, 40));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                JPanel cell = new JPanel(new BorderLayout()) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setPaint(new GradientPaint(0, 0, MAU_CHINH,
                                getWidth(), 0, new Color(109, 213, 250)));
                        g2.fillRect(0, 0, getWidth(), getHeight());
                    }
                };
                JLabel lbl = new JLabel(v != null ? v.toString() : "");
                lbl.setFont(new Font(FONT, Font.BOLD, 13));
                lbl.setForeground(MAU_TRANG);
                lbl.setBorder(new EmptyBorder(0, 8, 0, 8));
                lbl.setHorizontalAlignment(
                        (col == 0 || col == 5) ? SwingConstants.CENTER : SwingConstants.LEFT);
                cell.add(lbl, BorderLayout.CENTER);
                cell.setBorder(BorderFactory.createMatteBorder(
                        0, 0, 0, 1, new Color(109, 213, 250, 80)));
                return cell;
            }
        });
    }

    // ---------------------------------------------------------
    // FORM NHẬP LIỆU
    // ---------------------------------------------------------

    private JPanel xayDungFormNhap() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setPreferredSize(new Dimension(310, 0));
        outer.setBackground(MAU_TRANG);
        outer.setBorder(BorderFactory.createLineBorder(MAU_VIEN, 1));

        // Thanh gradient trên cùng
        PaintComponent headerBar = new PaintComponent();
        headerBar.setPreferredSize(new Dimension(0, 6));
        outer.add(headerBar, BorderLayout.NORTH);

        // Nội dung form
        JPanel panelNoiDung = new JPanel(new GridBagLayout());
        panelNoiDung.setBackground(MAU_TRANG);
        panelNoiDung.setBorder(new EmptyBorder(12, 14, 12, 14));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 2, 4, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ── Mã lịch trình ─────────────────────────────────────
        addLabel(panelNoiDung, gbc, 0, "Mã lịch trình:");
        txtMaLT = new JTextField();
        txtMaLT.setFont(new Font(FONT, Font.PLAIN, 13));
        txtMaLT.setEditable(false);
        txtMaLT.setBackground(new Color(240, 243, 247));
        txtMaLT.setToolTipText("Mã tự động sinh: LT001, LT002...");
        styleField(txtMaLT);
        addField(panelNoiDung, gbc, 1, txtMaLT);

        // ── Tour ──────────────────────────────────────────────
        addLabel(panelNoiDung, gbc, 2, "Tour:");
        cboTour = new JComboBox<>();
        cboTour.setFont(new Font(FONT, Font.PLAIN, 13));
        cboTour.setBackground(MAU_TRANG);
        cboTour.setPreferredSize(new Dimension(0, 34));
        addField(panelNoiDung, gbc, 3, cboTour);

        // ── Địa điểm ──────────────────────────────────────────
        addLabel(panelNoiDung, gbc, 4, "Địa điểm:");
        cboDiaDiem = new JComboBox<>();
        cboDiaDiem.setFont(new Font(FONT, Font.PLAIN, 13));
        cboDiaDiem.setBackground(MAU_TRANG);
        cboDiaDiem.setPreferredSize(new Dimension(0, 34));
        addField(panelNoiDung, gbc, 5, cboDiaDiem);

        // ── Phương tiện ───────────────────────────────────────
        addLabel(panelNoiDung, gbc, 6, "Phương tiện:");
        cboPhuongTien = new JComboBox<>();
        cboPhuongTien.setFont(new Font(FONT, Font.PLAIN, 13));
        cboPhuongTien.setBackground(MAU_TRANG);
        cboPhuongTien.setPreferredSize(new Dimension(0, 34));
        addField(panelNoiDung, gbc, 7, cboPhuongTien);

        // ── Ngày thứ ──────────────────────────────────────────
        addLabel(panelNoiDung, gbc, 8, "Ngày thứ:");
        spnNgayThu = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        spnNgayThu.setFont(new Font(FONT, Font.PLAIN, 13));
        spnNgayThu.setPreferredSize(new Dimension(0, 34));
        ((JSpinner.DefaultEditor) spnNgayThu.getEditor())
                .getTextField().setFont(new Font(FONT, Font.PLAIN, 13));
        addField(panelNoiDung, gbc, 9, spnNgayThu);

        // ── Nội dung ──────────────────────────────────────────
        addLabel(panelNoiDung, gbc, 10, "Nội dung:");
        txtaNoiDung = new JTextArea(4, 0);
        txtaNoiDung.setFont(new Font(FONT, Font.PLAIN, 13));
        txtaNoiDung.setLineWrap(true);
        txtaNoiDung.setWrapStyleWord(true);
        txtaNoiDung.setBorder(new EmptyBorder(6, 8, 6, 8));
        JScrollPane scrollNoiDung = new JScrollPane(txtaNoiDung);
        scrollNoiDung.setBorder(BorderFactory.createLineBorder(MAU_VIEN, 1));
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.3;
        panelNoiDung.add(scrollNoiDung, gbc);
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ── Trạng thái ────────────────────────────────────────
        chkTrangThai = new JCheckBox("Hoạt động");
        chkTrangThai.setFont(new Font(FONT, Font.PLAIN, 13));
        chkTrangThai.setBackground(MAU_TRANG);
        chkTrangThai.setForeground(MAU_CHU_TOI);
        chkTrangThai.setSelected(true);
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        panelNoiDung.add(chkTrangThai, gbc);

        // Glue — đẩy nội dung lên trên
        gbc.gridy = 13;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panelNoiDung.add(Box.createVerticalGlue(), gbc);

        outer.add(panelNoiDung, BorderLayout.CENTER);
        outer.add(xayDungPanelNut(), BorderLayout.SOUTH);
        return outer;
    }

    /** Thêm label xám đậm vào form */
    private void addLabel(JPanel p, GridBagConstraints gbc, int row, String text) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font(FONT, Font.BOLD, 12));
        lbl.setForeground(MAU_CHU_PHU);
        p.add(lbl, gbc);
    }

    /** Thêm component field chiếm full width */
    private void addField(JPanel p, GridBagConstraints gbc, int row, JComponent comp) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(comp, gbc);
    }

    /** Style viền chuẩn cho JTextField */
    private void styleField(JTextField tf) {
        tf.setPreferredSize(new Dimension(0, 34));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MAU_VIEN, 1),
                new EmptyBorder(4, 8, 4, 8)));
    }

    // ---------------------------------------------------------
    // PANEL NÚT
    // ---------------------------------------------------------

    private JPanel xayDungPanelNut() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 6, 6));
        panel.setBackground(MAU_TRANG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 0, 0, 0, MAU_VIEN),
                new EmptyBorder(10, 14, 12, 14)));

        btnThemMoi = new ActionButton();
        btnThemMoi.setText("➕  Thêm mới");
        btnThemMoi.setPreferredSize(new Dimension(0, 36));
        btnThemMoi.addActionListener(e -> xuLyThemMoi());

        btnCapNhat = new ActionButton();
        btnCapNhat.setText("✏  Cập nhật");
        btnCapNhat.setColorTop(new Color(39, 174, 96));
        btnCapNhat.setColorBottom(new Color(27, 124, 66));
        btnCapNhat.setPreferredSize(new Dimension(0, 36));
        btnCapNhat.addActionListener(e -> xuLyCapNhat());

        btnXoa = new ActionButton();
        btnXoa.setText("🗑  Xóa");
        btnXoa.setColorTop(new Color(192, 57, 43));
        btnXoa.setColorBottom(new Color(146, 43, 33));
        btnXoa.setPreferredSize(new Dimension(0, 36));
        btnXoa.addActionListener(e -> xuLyXoa());

        btnLamMoi = new ActionButton();
        btnLamMoi.setText("⟳  Làm mới");
        btnLamMoi.setColorTop(new Color(127, 140, 141));
        btnLamMoi.setColorBottom(new Color(99, 110, 114));
        btnLamMoi.setPreferredSize(new Dimension(0, 36));
        btnLamMoi.addActionListener(e -> lamMoiToanBo());

        panel.add(btnThemMoi);
        panel.add(btnCapNhat);
        panel.add(btnXoa);
        panel.add(btnLamMoi);
        return panel;
    }

    // =========================================================
    // TẢI DỮ LIỆU
    // =========================================================

    /**
     * Tải danh sách lịch trình vào bảng.
     * TODO: kết nối LichTrinhBUS.getDanhSach(tuKhoa)
     */
    private void taiDuLieu(String tuKhoa) {
        modelBang.setRowCount(0);
        try {
            List<LichTrinh> ds = dal.getDanhSach(tuKhoa);
            int stt = 1;
            for (LichTrinh lt : ds) {
                modelBang.addRow(new Object[] {
                        stt++,
                        lt.getMaLichTrinh(),
                        lt.getTenTour(),
                        lt.getTenDiaDiem(),
                        lt.getTenPT(),
                        lt.getNgayThu()
                });
            }
        } catch (Exception e) {
            hienThiLoi("Lỗi tải danh sách lịch trình: " + e.getMessage());
        }
        lblTongSo.setText("Tổng: " + modelBang.getRowCount() + " bản ghi");
    }

    // =========================================================
    // ĐIỀN FORM TỪ HÀNG ĐƯỢC CHỌN
    // =========================================================

    private void dienFormTuHang(int row) {
        // Chuyển đổi view-row → model-row (do có sorter)
        int modelRow = bangDuLieu.convertRowIndexToModel(row);

        String maLT = (String) modelBang.getValueAt(modelRow, 1);
        String tenTour = (String) modelBang.getValueAt(modelRow, 2);
        String diaDiem = (String) modelBang.getValueAt(modelRow, 3);
        String pt = (String) modelBang.getValueAt(modelRow, 4);
        Object ngayThuObj = modelBang.getValueAt(modelRow, 5);

        maLichTrinhDangChon = maLT;
        txtMaLT.setText(maLT);
        txtMaLT.setBackground(new Color(240, 243, 247));

        selectCombo(cboTour, tenTour);
        selectCombo(cboDiaDiem, diaDiem);
        selectCombo(cboPhuongTien, pt);

        if (ngayThuObj instanceof Integer)
            spnNgayThu.setValue(ngayThuObj);

        // Load noiDung + trangThai từ DB (không có trong bảng)
        try {
            LichTrinh full = dal.getById(maLT);
            if (full != null) {
                txtaNoiDung.setText(full.getNoiDung() != null ? full.getNoiDung() : "");
                chkTrangThai.setSelected(Boolean.TRUE.equals(full.getTrangThai()));
            }
        } catch (SQLException e) {
            txtaNoiDung.setText("");
            chkTrangThai.setSelected(true);
        }
    }

    private void selectCombo(JComboBox<String> cbo, String value) {
        if (value == null)
            return;
        for (int i = 0; i < cbo.getItemCount(); i++) {
            if (value.equals(cbo.getItemAt(i))) {
                cbo.setSelectedIndex(i);
                return;
            }
        }
    }

    // =========================================================
    // RESET FORM
    // =========================================================

    private void resetFormThemMoi() {
        maLichTrinhDangChon = null;

        try {
            txtMaLT.setText(dal.sinhMaMoi());
        } catch (SQLException e) {
            txtMaLT.setText("LT001");
        }
        txtMaLT.setBackground(new Color(240, 243, 247));

        if (cboTour.getItemCount() > 0)
            cboTour.setSelectedIndex(0);
        if (cboDiaDiem.getItemCount() > 0)
            cboDiaDiem.setSelectedIndex(0);
        if (cboPhuongTien.getItemCount() > 0)
            cboPhuongTien.setSelectedIndex(0);

        spnNgayThu.setValue(1);
        txtaNoiDung.setText("");
        chkTrangThai.setSelected(true);
        bangDuLieu.clearSelection();
    }

    // =========================================================
    // XỬ LÝ SỰ KIỆN NÚT
    // =========================================================

    private void xuLyThemMoi() {
        String[] data = docVaValidateForm();
        if (data == null)
            return;

        int ok = JOptionPane.showConfirmDialog(this,
                "Xác nhận thêm lịch trình \"" + data[0] + "\"?\n"
                        + "Tour     : " + data[1] + "\n"
                        + "Địa điểm : " + data[2] + "\n"
                        + "Ngày thứ : " + data[4],
                "Xác nhận thêm mới",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (ok != JOptionPane.YES_OPTION)
            return;

        try {
            LichTrinh lt = LichTrinh.builder()
                    .maLichTrinh(data[0])
                    .maTour(mapTour.get(data[1]))
                    .maDiaDiem(mapDiaDiem.get(data[2]))
                    .maPT(mapPhuongTien.get(data[3]))
                    .ngayThu(Integer.parseInt(data[4]))
                    .noiDung(data[5])
                    .trangThai(chkTrangThai.isSelected())
                    .build();
            if (dal.them(lt) > 0) {
                hienThiThongBao("Thêm lịch trình \"" + data[0] + "\" thành công.");
                taiDuLieu(null);
                resetFormThemMoi();
            } else {
                hienThiLoi("Thêm lịch trình thất bại.");
            }
        } catch (Exception e) {
            hienThiLoi("Lỗi thêm lịch trình: " + e.getMessage());
        }
    }

    private void xuLyCapNhat() {
        if (maLichTrinhDangChon == null) {
            hienThiLoi("Vui lòng chọn một lịch trình trong bảng để cập nhật.");
            return;
        }
        String[] data = docVaValidateForm();
        if (data == null)
            return;

        try {
            LichTrinh lt = LichTrinh.builder()
                    .maLichTrinh(maLichTrinhDangChon)
                    .maTour(mapTour.get(data[1]))
                    .maDiaDiem(mapDiaDiem.get(data[2]))
                    .maPT(mapPhuongTien.get(data[3]))
                    .ngayThu(Integer.parseInt(data[4]))
                    .noiDung(data[5])
                    .trangThai(chkTrangThai.isSelected())
                    .build();
            if (dal.capNhat(lt) > 0) {
                hienThiThongBao("Cập nhật lịch trình \"" + maLichTrinhDangChon + "\" thành công.");
                taiDuLieu(txtTimKiem.getText());
            } else {
                hienThiLoi("Cập nhật thất bại — không tìm thấy bản ghi.");
            }
        } catch (Exception e) {
            hienThiLoi("Lỗi cập nhật lịch trình: " + e.getMessage());
        }
    }

    private void xuLyXoa() {
        if (maLichTrinhDangChon == null) {
            hienThiLoi("Vui lòng chọn một lịch trình trong bảng để xóa.");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa lịch trình \"" + maLichTrinhDangChon + "\" không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ok != JOptionPane.YES_OPTION)
            return;

        try {
            if (dal.xoa(maLichTrinhDangChon) > 0) {
                hienThiThongBao("Đã xóa lịch trình \"" + maLichTrinhDangChon + "\" thành công.");
                taiDuLieu(null);
                resetFormThemMoi();
            } else {
                hienThiLoi("Xóa thất bại — không tìm thấy bản ghi.");
            }
        } catch (Exception e) {
            hienThiLoi("Lỗi xóa lịch trình: " + e.getMessage());
        }
    }

    private void lamMoiToanBo() {
        txtTimKiem.setText("");
        khoiTaoCombos();
        taiDuLieu(null);
        resetFormThemMoi();
    }

    // =========================================================
    // ĐỌC & VALIDATE FORM
    // =========================================================

    /**
     * Đọc và validate cơ bản dữ liệu từ form.
     *
     * @return [maLT, tenTour, diaDiem, phuongTien, ngayThu, noiDung] hoặc null
     */
    private String[] docVaValidateForm() {
        String maLT = txtMaLT.getText().trim();

        String tenTour = (String) cboTour.getSelectedItem();
        if (tenTour == null || tenTour.isBlank()) {
            hienThiLoi("Vui lòng chọn Tour.");
            return null;
        }
        String diaDiem = (String) cboDiaDiem.getSelectedItem();
        if (diaDiem == null || diaDiem.isBlank()) {
            hienThiLoi("Vui lòng chọn Địa điểm.");
            return null;
        }
        String pt = (String) cboPhuongTien.getSelectedItem();
        if (pt == null || pt.isBlank()) {
            hienThiLoi("Vui lòng chọn Phương tiện.");
            return null;
        }

        int ngayThu = (int) spnNgayThu.getValue();
        String noiDung = txtaNoiDung.getText().trim();

        return new String[] { maLT, tenTour, diaDiem, pt, String.valueOf(ngayThu), noiDung };
    }

    // =========================================================
    // TIỆN ÍCH
    // =========================================================

    private void hienThiLoi(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private void hienThiThongBao(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
}
