package GUI;

import BUS.KhuyenMaiBUS;
import DTO.KhuyenMaiDTO;
import Exception.BusException;
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
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

public class KhuyenMaiPanel extends JPanel {

    private static final Color MAU_CHINH = new Color(41, 128, 185);
    private static final Color MAU_NEN = new Color(246, 247, 248);
    private static final Color MAU_TRANG = Color.WHITE;
    private static final Color MAU_HANG_XEN = new Color(240, 246, 252);
    private static final Color MAU_CHON = new Color(214, 234, 248);
    private static final Color MAU_CHU_TOI = new Color(44, 62, 80);
    private static final Color MAU_CHU_PHU = new Color(127, 140, 141);
    private static final Color MAU_VIEN = new Color(213, 219, 219);
    private static final String FONT = "Segoe UI";

    private static final DecimalFormat FMT_TIEN;
    static {
        DecimalFormatSymbols sym = new DecimalFormatSymbols(Locale.getDefault());
        sym.setGroupingSeparator(',');
        FMT_TIEN = new DecimalFormat("#,##0", sym);
    }

    private final KhuyenMaiBUS bus;
    private KhuyenMaiDTO kmDangChon = null;

    private JTextField txtTimKiem;
    private ActionButton btnTimKiem;
    private JLabel lblTongSo;

    private JTable bangDuLieu;
    private DefaultTableModel modelBang;

    // Form fields
    private JTextField txtMaKM;
    private JTextField txtTenKM;
    private JTextField txtGiaTri;
    private JLabel lblXemTruoc;
    private JTextField txtMoTa;

    // Buttons
    private ActionButton btnThemMoi;
    private ActionButton btnCapNhat;
    private ActionButton btnXoa;
    private ActionButton btnLamMoi;

    public KhuyenMaiPanel() {
        this.bus = new KhuyenMaiBUS();
        xayDungGiaoDien();
        taiDuLieu(null);
        resetFormThemMoi();
    }

    // ─── Discount formatting ─────────────────────────────────────────────────

    /**
     * phuongThucKM stores a raw number string:
     *   < 1  → percentage  (e.g. "0.1" → "10%")
     *   >= 1 → fixed deduct (e.g. "500000" → "-500,000đ")
     */
    static String formatGiaTri(String raw) {
        if (raw == null || raw.isBlank()) return "";
        try {
            double v = Double.parseDouble(raw);
            if (v <= 0) return raw;
            if (v < 1) {
                double pct = v * 100;
                return (pct == Math.floor(pct)) ? (int) pct + "%" : String.format("%.1f%%", pct);
            } else {
                return "-" + FMT_TIEN.format((long) v) + "đ";
            }
        } catch (NumberFormatException e) {
            return raw;
        }
    }

    private void capNhatXemTruoc() {
        String text = txtGiaTri.getText().trim();
        if (text.isEmpty()) { lblXemTruoc.setText(" "); return; }
        try {
            double v = Double.parseDouble(text);
            if (v <= 0) {
                lblXemTruoc.setText("⚠ Phải > 0");
                lblXemTruoc.setForeground(new Color(192, 57, 43));
            } else {
                lblXemTruoc.setText("→ " + formatGiaTri(text));
                lblXemTruoc.setForeground(v < 1 ? new Color(39, 174, 96) : new Color(192, 57, 43));
            }
        } catch (NumberFormatException ex) {
            lblXemTruoc.setText("⚠ Nhập số hợp lệ");
            lblXemTruoc.setForeground(new Color(192, 57, 43));
        }
    }

    // ─── UI Construction ─────────────────────────────────────────────────────

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
        tabs.addTab("Khuyến mãi", panelTab);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel xayDungThanhTimKiem() {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setBackground(MAU_NEN);
        panel.setBorder(new EmptyBorder(0, 0, 4, 0));
        JPanel panelTrai = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panelTrai.setBackground(MAU_NEN);
        JLabel lblTieuDe = new JLabel("Quản lý Khuyến mãi");
        lblTieuDe.setFont(new Font(FONT, Font.BOLD, 18));
        lblTieuDe.setForeground(MAU_CHU_TOI);
        txtTimKiem = new JTextField(22);
        txtTimKiem.setFont(new Font(FONT, Font.PLAIN, 13));
        txtTimKiem.setPreferredSize(new Dimension(220, 32));
        txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MAU_VIEN, 1), new EmptyBorder(2, 8, 2, 8)));
        txtTimKiem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) taiDuLieu(txtTimKiem.getText());
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

    private JPanel xayDungVungChinh() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(MAU_NEN);
        panel.add(xayDungBang(), BorderLayout.CENTER);
        panel.add(xayDungFormNhap(), BorderLayout.EAST);
        return panel;
    }

    private ScrollPaneWin11 xayDungBang() {
        modelBang = new DefaultTableModel(
                new String[]{"STT", "Mã KM", "Tên khuyến mãi", "Giá trị", "Mô tả"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
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
        bangDuLieu.getColumnModel().getColumn(0).setMaxWidth(50);
        bangDuLieu.getColumnModel().getColumn(1).setPreferredWidth(70);
        bangDuLieu.getColumnModel().getColumn(2).setPreferredWidth(220);
        bangDuLieu.getColumnModel().getColumn(3).setPreferredWidth(110);
        bangDuLieu.getColumnModel().getColumn(4).setPreferredWidth(370);
        bangDuLieu.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel,
                    boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setOpaque(true);
                setFont(new Font(FONT, Font.PLAIN, 13));
                setForeground(MAU_CHU_TOI);
                setBorder(new EmptyBorder(0, 8, 0, 8));
                if (!sel) setBackground(row % 2 == 0 ? MAU_TRANG : MAU_HANG_XEN);
                setHorizontalAlignment(col == 0 ? CENTER : LEFT);
                if (col == 3 && v != null) {
                    String s = v.toString();
                    if (s.contains("%")) setForeground(new Color(39, 174, 96));
                    else if (s.startsWith("-")) setForeground(new Color(192, 57, 43));
                }
                return this;
            }
        });
        JTableHeader header = bangDuLieu.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 42));
        header.setFont(new Font(FONT, Font.BOLD, 13));
        header.setBorder(BorderFactory.createEmptyBorder());
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value != null ? value.toString() : "") {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setPaint(new GradientPaint(0, 0, MAU_CHINH, 0, getHeight(), MAU_CHINH.darker()));
                        g2.fillRect(0, 0, getWidth(), getHeight());
                        super.paintComponent(g);
                    }
                };
                label.setFont(new Font(FONT, Font.BOLD, 13));
                label.setHorizontalAlignment(column == 0 ? JLabel.CENTER : JLabel.LEFT);
                label.setForeground(Color.WHITE);
                label.setOpaque(false);
                label.setBorder(new EmptyBorder(0, 8, 0, 8));
                return label;
            }
        });
        bangDuLieu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = bangDuLieu.getSelectedRow();
                if (row >= 0) dienFormTuHang(row);
            }
        });
        ScrollPaneWin11 scroll = new ScrollPaneWin11();
        scroll.setViewportView(bangDuLieu);
        scroll.setBorder(BorderFactory.createLineBorder(MAU_VIEN, 1));
        return scroll;
    }

    private JPanel xayDungFormNhap() {
        JPanel panelNgoai = new JPanel(new BorderLayout());
        panelNgoai.setPreferredSize(new Dimension(310, 0));
        panelNgoai.setBackground(MAU_TRANG);
        panelNgoai.setBorder(BorderFactory.createLineBorder(MAU_VIEN, 1));
        PaintComponent thanhGradient = new PaintComponent();
        thanhGradient.setPreferredSize(new Dimension(0, 6));
        JPanel panelNoiDung = new JPanel(new GridBagLayout());
        panelNoiDung.setBackground(MAU_TRANG);
        panelNoiDung.setBorder(new EmptyBorder(12, 14, 12, 14));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 2, 4, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        int row = 0;

        JLabel lblTieuDe = new JLabel("THÔNG TIN KHUYẾN MÃI");
        lblTieuDe.setFont(new Font(FONT, Font.BOLD, 13));
        lblTieuDe.setForeground(MAU_CHINH);
        lblTieuDe.setBorder(new MatteBorder(0, 0, 1, 0, MAU_VIEN));
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 2, 10, 2);
        panelNoiDung.add(lblTieuDe, gbc);
        gbc.insets = new Insets(4, 2, 4, 2); gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Mã khuyến mãi *");
        txtMaKM = new JTextField();
        txtMaKM.setFont(new Font(FONT, Font.PLAIN, 13));
        txtMaKM.setEditable(false);
        txtMaKM.setBackground(new Color(240, 243, 247));
        styleInput(txtMaKM);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        panelNoiDung.add(txtMaKM, gbc); gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Tên khuyến mãi *");
        txtTenKM = new JTextField();
        txtTenKM.setFont(new Font(FONT, Font.PLAIN, 13));
        styleInput(txtTenKM);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        panelNoiDung.add(txtTenKM, gbc); gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Giá trị KM * (VD: 0.1 = 10%  /  500000 = -500,000đ)");
        txtGiaTri = new JTextField();
        txtGiaTri.setFont(new Font(FONT, Font.PLAIN, 13));
        styleInput(txtGiaTri);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        panelNoiDung.add(txtGiaTri, gbc); gbc.gridwidth = 1;
        txtGiaTri.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { capNhatXemTruoc(); }
        });

        // Live preview
        lblXemTruoc = new JLabel(" ");
        lblXemTruoc.setFont(new Font(FONT, Font.ITALIC, 12));
        lblXemTruoc.setForeground(new Color(39, 174, 96));
        lblXemTruoc.setBorder(new EmptyBorder(0, 4, 2, 0));
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 2, 4, 2);
        panelNoiDung.add(lblXemTruoc, gbc);
        gbc.insets = new Insets(4, 2, 4, 2); gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Mô tả *");
        txtMoTa = new JTextField();
        txtMoTa.setFont(new Font(FONT, Font.PLAIN, 13));
        styleInput(txtMoTa);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        panelNoiDung.add(txtMoTa, gbc); gbc.gridwidth = 1;

        JPanel panelNut = xayDungPanelNut();
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 0, 0, 0);
        panelNoiDung.add(panelNut, gbc);

        panelNgoai.add(thanhGradient, BorderLayout.NORTH);
        panelNgoai.add(panelNoiDung, BorderLayout.CENTER);
        return panelNgoai;
    }

    private int addLabel(JPanel panel, GridBagConstraints gbc, int row, String ten) {
        JLabel lbl = new JLabel(ten);
        lbl.setFont(new Font(FONT, Font.PLAIN, 11));
        lbl.setForeground(MAU_CHU_PHU);
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.insets = new Insets(8, 2, 1, 2);
        panel.add(lbl, gbc);
        gbc.insets = new Insets(4, 2, 4, 2); gbc.gridwidth = 1;
        return row + 1;
    }

    private void styleInput(JTextField tf) {
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MAU_VIEN, 1), new EmptyBorder(4, 8, 4, 8)));
        tf.setPreferredSize(new Dimension(0, 32));
    }

    private JPanel xayDungPanelNut() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 6, 6));
        panel.setBackground(MAU_TRANG);
        btnThemMoi = new ActionButton(); btnThemMoi.setText("＋  Thêm mới");
        btnCapNhat = new ActionButton(); btnCapNhat.setText("✎  Cập nhật");
        btnCapNhat.setColorTop(new Color(39, 174, 96)); btnCapNhat.setColorBottom(new Color(27, 124, 66));
        btnXoa = new ActionButton(); btnXoa.setText("✖  Xóa");
        btnXoa.setColorTop(new Color(231, 76, 60)); btnXoa.setColorBottom(new Color(192, 57, 43));
        btnLamMoi = new ActionButton(); btnLamMoi.setText("⟳  Làm mới");
        btnLamMoi.setColorTop(new Color(127, 140, 141)); btnLamMoi.setColorBottom(new Color(99, 110, 114));
        btnThemMoi.addActionListener(e -> xuLyThemMoi());
        btnCapNhat.addActionListener(e -> xuLyCapNhat());
        btnXoa.addActionListener(e -> xuLyXoa());
        btnLamMoi.addActionListener(e -> lamMoiToanBo());
        panel.add(btnThemMoi); panel.add(btnCapNhat);
        panel.add(btnXoa); panel.add(btnLamMoi);
        return panel;
    }

    // ─── Data Loading ────────────────────────────────────────────────────────

    private void taiDuLieu(String tuKhoa) {
        lblTongSo.setText("Đang tải...");
        new SwingWorker<ArrayList<KhuyenMaiDTO>, Void>() {
            @Override
            protected ArrayList<KhuyenMaiDTO> doInBackground() throws Exception {
                return (tuKhoa == null || tuKhoa.isBlank())
                        ? bus.getAll() : bus.searchKhuyenMai(tuKhoa);
            }
            @Override
            protected void done() {
                try {
                    ArrayList<KhuyenMaiDTO> ds = get();
                    modelBang.setRowCount(0);
                    int stt = 1;
                    for (KhuyenMaiDTO km : ds) {
                        modelBang.addRow(new Object[]{
                            stt++,
                            km.getMaKhuyenMai(),
                            km.getTenKhuyenMai(),
                            formatGiaTri(km.getPhuongThucKM()),
                            km.getMoTa()
                        });
                    }
                    lblTongSo.setText("Tổng: " + ds.size() + " bản ghi");
                } catch (Exception ex) {
                    hienThiLoi("Không thể tải dữ liệu:\n" + ex.getMessage());
                    lblTongSo.setText("Lỗi tải dữ liệu");
                }
            }
        }.execute();
    }

    private void dienFormTuHang(int row) {
        String ma = (String) modelBang.getValueAt(row, 1);
        String ten = (String) modelBang.getValueAt(row, 2);
        String moTa = (String) modelBang.getValueAt(row, 4);

        // phuongThucKM (raw numeric) is not in the table model display (it's formatted).
        // Re-fetch via BUS using the mã.
        kmDangChon = KhuyenMaiDTO.builder()
                .maKhuyenMai(ma).tenKhuyenMai(ten)
                .moTa(moTa).trangThaiKM(true).build();

        txtMaKM.setText(ma);
        txtTenKM.setText(ten != null ? ten : "");
        txtMoTa.setText(moTa != null ? moTa : "");

        new SwingWorker<Double, Void>() {
            @Override protected Double doInBackground() throws Exception {
                return bus.getGiaTriKMByMaKM(ma);
            }
            @Override protected void done() {
                try {
                    double v = get();
                    String raw = v > 0 ? String.valueOf(v) : "";
                    kmDangChon.setPhuongThucKM(raw);
                    txtGiaTri.setText(raw);
                    capNhatXemTruoc();
                } catch (Exception ignored) { txtGiaTri.setText(""); }
            }
        }.execute();
    }

    private void resetFormThemMoi() {
        kmDangChon = null;
        try { txtMaKM.setText(bus.sinhMaMoi()); } catch (Exception e) { txtMaKM.setText("KM---"); }
        txtTenKM.setText(""); txtGiaTri.setText(""); txtMoTa.setText("");
        lblXemTruoc.setText(" ");
        bangDuLieu.clearSelection();
        txtTenKM.requestFocusInWindow();
    }

    // ─── CRUD handlers ───────────────────────────────────────────────────────

    private void xuLyThemMoi() {
        KhuyenMaiDTO km = docDuLieuForm();
        if (km == null) return;
        try {
            bus.addKhuyenMai(km);
            hienThiThongBao("Thêm khuyến mãi \"" + km.getTenKhuyenMai() + "\" thành công.");
            taiDuLieu(null); resetFormThemMoi();
        } catch (BusException ex) { hienThiLoi("Thêm thất bại:\n" + ex.getMessage()); }
    }

    private void xuLyCapNhat() {
        if (kmDangChon == null) { hienThiLoi("Vui lòng chọn một khuyến mãi để cập nhật."); return; }
        KhuyenMaiDTO km = docDuLieuForm();
        if (km == null) return;
        try {
            bus.updateKhuyenMai(km);
            hienThiThongBao("Cập nhật khuyến mãi \"" + km.getTenKhuyenMai() + "\" thành công.");
            taiDuLieu(txtTimKiem.getText());
        } catch (BusException ex) { hienThiLoi("Cập nhật thất bại:\n" + ex.getMessage()); }
    }

    private void xuLyXoa() {
        if (kmDangChon == null) { hienThiLoi("Vui lòng chọn một khuyến mãi để xóa."); return; }
        int xacNhan = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa khuyến mãi \"" + kmDangChon.getTenKhuyenMai() + "\" không?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (xacNhan != JOptionPane.YES_OPTION) return;
        try {
            bus.deleteKhuyenMai(kmDangChon.getMaKhuyenMai());
            hienThiThongBao("Đã xóa khuyến mãi \"" + kmDangChon.getTenKhuyenMai() + "\" thành công.");
            taiDuLieu(null); resetFormThemMoi();
        } catch (BusException ex) { hienThiLoi("Xóa thất bại:\n" + ex.getMessage()); }
    }

    private void lamMoiToanBo() {
        txtTimKiem.setText(""); taiDuLieu(null); resetFormThemMoi();
    }

    private KhuyenMaiDTO docDuLieuForm() {
        String ma = txtMaKM.getText().trim();
        String ten = txtTenKM.getText().trim();
        String giaTriStr = txtGiaTri.getText().trim();
        String moTa = txtMoTa.getText().trim();

        if (ten.isEmpty()) { hienThiLoi("Tên khuyến mãi không được để trống."); txtTenKM.requestFocusInWindow(); return null; }
        if (giaTriStr.isEmpty()) { hienThiLoi("Giá trị khuyến mãi không được để trống.\nVD: 0.1 = 10%  /  500000 = -500,000đ"); txtGiaTri.requestFocusInWindow(); return null; }
        try {
            double v = Double.parseDouble(giaTriStr);
            if (v <= 0) { hienThiLoi("Giá trị khuyến mãi phải lớn hơn 0."); txtGiaTri.requestFocusInWindow(); return null; }
        } catch (NumberFormatException e) {
            hienThiLoi("Giá trị khuyến mãi phải là số.\nVD: 0.1 = 10%  /  500000 = -500,000đ");
            txtGiaTri.requestFocusInWindow(); return null;
        }
        if (moTa.isEmpty()) { hienThiLoi("Mô tả không được để trống."); txtMoTa.requestFocusInWindow(); return null; }

        return KhuyenMaiDTO.builder()
                .maKhuyenMai(ma).tenKhuyenMai(ten)
                .phuongThucKM(giaTriStr)   // raw number stored in phuongThucKM column
                .moTa(moTa).trangThaiKM(true).build();
    }

    private void hienThiLoi(String thongBao) {
        JOptionPane.showMessageDialog(this, thongBao, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private void hienThiThongBao(String thongBao) {
        JOptionPane.showMessageDialog(this, thongBao, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
}
