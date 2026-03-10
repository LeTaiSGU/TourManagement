package GUI;

import BUS.DiaDiemBUS;
import DTO.DiaDiem;
import Exception.BusException;
import GUI.Menu.ActionButton;
import GUI.Menu.PaintComponent;
import GUI.Menu.TabbedPaneCustom;
import GUI.ScrollPane.ScrollPaneWin11;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

public class DiaDiemPanel extends JPanel {

    private static final Color MAU_CHINH = new Color(41, 128, 185);
    private static final Color MAU_NEN = new Color(246, 247, 248);
    private static final Color MAU_TRANG = Color.WHITE;
    private static final Color MAU_HANG_XEN = new Color(240, 246, 252);
    private static final Color MAU_CHON = new Color(214, 234, 248);
    private static final Color MAU_CHU_TOI = new Color(44, 62, 80);
    private static final Color MAU_CHU_PHU = new Color(127, 140, 141);
    private static final Color MAU_VIEN = new Color(213, 219, 219);
    private static final String FONT = "Segoe UI";

    private final DiaDiemBUS bus;
    private DiaDiem ddDangChon = null;

    private JTextField txtTimKiem;
    private ActionButton btnTimKiem;
    private JLabel lblTongSo;

    private JTable bangDuLieu;
    private DefaultTableModel modelBang;

    // Image panel
    private JPanel panelHinhAnh;
    private BufferedImage anhHienTai = null;
    private JLabel lblTenAnhOverlay;

    // Form fields
    private JTextField txtMaDiaDiem;
    private JTextField txtTenDiaDiem;
    private JTextField txtQuocGia;
    private JTextField txtAnhDiaDiem;
    private JTextField txtMoTa;

    // Buttons
    private ActionButton btnThemMoi;
    private ActionButton btnCapNhat;
    private ActionButton btnXoa;
    private ActionButton btnLamMoi;

    public DiaDiemPanel() {
        this.bus = new DiaDiemBUS();
        xayDungGiaoDien();
        taiDuLieu(null);
        resetFormThemMoi();
    }

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
        tabs.addTab("Địa điểm", panelTab);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel xayDungThanhTimKiem() {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setBackground(MAU_NEN);
        panel.setBorder(new EmptyBorder(0, 0, 4, 0));
        JPanel panelTrai = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panelTrai.setBackground(MAU_NEN);
        JLabel lblTieuDe = new JLabel("Quản lý Địa điểm");
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
        panel.add(xayDungVungBangVaAnh(), BorderLayout.CENTER);
        panel.add(xayDungFormNhap(), BorderLayout.EAST);
        return panel;
    }

    /** Left side: large image panel on top, table below */
    private JPanel xayDungVungBangVaAnh() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(MAU_NEN);
        panel.add(xayDungPanelHinhAnh(), BorderLayout.NORTH);
        panel.add(xayDungBang(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel xayDungPanelHinhAnh() {
        panelHinhAnh = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                if (anhHienTai != null) {
                    double scale = Math.max((double) w / anhHienTai.getWidth(),
                            (double) h / anhHienTai.getHeight());
                    int dw = (int) (anhHienTai.getWidth() * scale);
                    int dh = (int) (anhHienTai.getHeight() * scale);
                    g2.drawImage(anhHienTai, (w - dw) / 2, (h - dh) / 2, dw, dh, this);
                    // dark overlay strip at the bottom
                    g2.setColor(new Color(0, 0, 0, 110));
                    g2.fillRect(0, h - 34, w, 34);
                } else {
                    g2.setColor(new Color(228, 234, 238));
                    g2.fillRect(0, 0, w, h);
                    g2.setColor(MAU_CHU_PHU);
                    g2.setFont(new Font(FONT, Font.ITALIC, 14));
                    String txt = "Chọn địa điểm để xem ảnh";
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(txt, (w - fm.stringWidth(txt)) / 2, h / 2 + fm.getAscent() / 2);
                }
            }
        };
        panelHinhAnh.setPreferredSize(new Dimension(0, 230));
        panelHinhAnh.setOpaque(false);
        panelHinhAnh.setBorder(BorderFactory.createLineBorder(MAU_VIEN, 1));
        panelHinhAnh.setBackground(new Color(228, 234, 238));

        // Overlay label at bottom (name of place)
        lblTenAnhOverlay = new JLabel("", JLabel.CENTER);
        lblTenAnhOverlay.setFont(new Font(FONT, Font.BOLD, 14));
        lblTenAnhOverlay.setForeground(Color.WHITE);
        lblTenAnhOverlay.setPreferredSize(new Dimension(0, 34));
        lblTenAnhOverlay.setOpaque(false);
        panelHinhAnh.add(lblTenAnhOverlay, BorderLayout.SOUTH);
        return panelHinhAnh;
    }

    private ScrollPaneWin11 xayDungBang() {
        modelBang = new DefaultTableModel(
                new String[]{"STT", "Mã", "Tên địa điểm", "Quốc gia", "Ảnh", "Mô tả"}, 0) {
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
        bangDuLieu.getColumnModel().getColumn(2).setPreferredWidth(190);
        bangDuLieu.getColumnModel().getColumn(3).setPreferredWidth(100);
        bangDuLieu.getColumnModel().getColumn(4).setPreferredWidth(130);
        bangDuLieu.getColumnModel().getColumn(5).setPreferredWidth(300);
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

        JLabel lblTieuDe = new JLabel("THÔNG TIN ĐỊA ĐIỂM");
        lblTieuDe.setFont(new Font(FONT, Font.BOLD, 13));
        lblTieuDe.setForeground(MAU_CHINH);
        lblTieuDe.setBorder(new MatteBorder(0, 0, 1, 0, MAU_VIEN));
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 2, 10, 2);
        panelNoiDung.add(lblTieuDe, gbc);
        gbc.insets = new Insets(4, 2, 4, 2); gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Mã địa điểm *");
        txtMaDiaDiem = new JTextField();
        txtMaDiaDiem.setFont(new Font(FONT, Font.PLAIN, 13));
        txtMaDiaDiem.setEditable(false);
        txtMaDiaDiem.setBackground(new Color(240, 243, 247));
        styleInput(txtMaDiaDiem);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        panelNoiDung.add(txtMaDiaDiem, gbc); gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Tên địa điểm *");
        txtTenDiaDiem = new JTextField();
        txtTenDiaDiem.setFont(new Font(FONT, Font.PLAIN, 13));
        styleInput(txtTenDiaDiem);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        panelNoiDung.add(txtTenDiaDiem, gbc); gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Quốc gia *");
        txtQuocGia = new JTextField();
        txtQuocGia.setFont(new Font(FONT, Font.PLAIN, 13));
        styleInput(txtQuocGia);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        panelNoiDung.add(txtQuocGia, gbc); gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Mô tả *");
        txtMoTa = new JTextField();
        txtMoTa.setFont(new Font(FONT, Font.PLAIN, 13));
        styleInput(txtMoTa);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        panelNoiDung.add(txtMoTa, gbc); gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Ảnh địa điểm *");
        // txtAnhDiaDiem + Chon button in a nested panel
        JPanel panelChonAnh = new JPanel(new BorderLayout(4, 0));
        panelChonAnh.setBackground(MAU_TRANG);
        txtAnhDiaDiem = new JTextField();
        txtAnhDiaDiem.setFont(new Font(FONT, Font.PLAIN, 12));
        txtAnhDiaDiem.setEditable(false);
        txtAnhDiaDiem.setBackground(new Color(240, 243, 247));
        txtAnhDiaDiem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MAU_VIEN, 1), new EmptyBorder(4, 8, 4, 8)));
        txtAnhDiaDiem.setPreferredSize(new Dimension(0, 32));
        ActionButton btnChonAnh = new ActionButton();
        btnChonAnh.setText("📂 Chọn");
        btnChonAnh.setPreferredSize(new Dimension(82, 32));
        btnChonAnh.addActionListener(e -> chonAnhDiaDiem());
        panelChonAnh.add(txtAnhDiaDiem, BorderLayout.CENTER);
        panelChonAnh.add(btnChonAnh, BorderLayout.EAST);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        panelNoiDung.add(panelChonAnh, gbc); gbc.gridwidth = 1;

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
        lbl.setFont(new Font(FONT, Font.PLAIN, 12));
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
        new SwingWorker<ArrayList<DiaDiem>, Void>() {
            @Override
            protected ArrayList<DiaDiem> doInBackground() throws Exception {
                return (tuKhoa == null || tuKhoa.isBlank())
                        ? bus.getAllDiaDiem() : bus.searchDiaDiem(tuKhoa);
            }
            @Override
            protected void done() {
                try {
                    ArrayList<DiaDiem> ds = get();
                    modelBang.setRowCount(0);
                    int stt = 1;
                    for (DiaDiem dd : ds) {
                        modelBang.addRow(new Object[]{
                            stt++, dd.getMaDiaDiem(), dd.getTenDiaDiem(),
                            dd.getQuocGia(), dd.getAnhDiaDiem(),
                            dd.getMoTa()
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
        String quocGia = (String) modelBang.getValueAt(row, 3);
        String anh = (String) modelBang.getValueAt(row, 4);
        String moTa = (String) modelBang.getValueAt(row, 5);
        ddDangChon = DiaDiem.builder()
                .maDiaDiem(ma).tenDiaDiem(ten).quocGia(quocGia)
                .anhDiaDiem(anh).moTa(moTa).build();
        txtMaDiaDiem.setText(ma);
        txtTenDiaDiem.setText(ten != null ? ten : "");
        txtQuocGia.setText(quocGia != null ? quocGia : "");
        txtAnhDiaDiem.setText(anh != null ? anh : "");
        txtMoTa.setText(moTa != null ? moTa : "");
        // load image asynchronously
        taiAnhVaHienThi(anh, ten);
    }

    private void taiAnhVaHienThi(String tenAnh, String tenDiaDiem) {
        lblTenAnhOverlay.setText(tenDiaDiem != null ? tenDiaDiem : "");
        if (tenAnh == null || tenAnh.isBlank()) {
            anhHienTai = null;
            panelHinhAnh.repaint();
            return;
        }
        new SwingWorker<BufferedImage, Void>() {
            @Override
            protected BufferedImage doInBackground() throws Exception {
                // Try classpath first (works at runtime)
                URL url = getClass().getResource("/image/diadiem/" + tenAnh);
                if (url != null) return ImageIO.read(url);
                // Fallback: filesystem path for development
                File f = new File("src/main/resources/image/diadiem/" + tenAnh);
                if (f.exists()) return ImageIO.read(f);
                return null;
            }
            @Override
            protected void done() {
                try { anhHienTai = get(); } catch (Exception ignored) { anhHienTai = null; }
                panelHinhAnh.repaint();
            }
        }.execute();
    }

    private void resetFormThemMoi() {
        ddDangChon = null;
        try { txtMaDiaDiem.setText(bus.sinhMaMoi()); } catch (Exception e) { txtMaDiaDiem.setText("DD---"); }
        txtTenDiaDiem.setText(""); txtQuocGia.setText("");
        txtAnhDiaDiem.setText(""); txtMoTa.setText("");
        anhHienTai = null;
        lblTenAnhOverlay.setText("");
        panelHinhAnh.repaint();
        bangDuLieu.clearSelection();
        txtTenDiaDiem.requestFocusInWindow();
    }

    // ─── Image chooser ───────────────────────────────────────────────────────

    private void chonAnhDiaDiem() {
        JFileChooser chooser = new JFileChooser(getAnhFolder());
        chooser.setDialogTitle("Chọn ảnh địa điểm");
        chooser.setFileFilter(new FileNameExtensionFilter(
                "Ảnh (*.jpg, *.jpeg, *.png, *.gif)", "jpg", "jpeg", "png", "gif"));
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selected = chooser.getSelectedFile();
            txtAnhDiaDiem.setText(selected.getName());
            // preview immediately using selected file
            new SwingWorker<BufferedImage, Void>() {
                @Override
                protected BufferedImage doInBackground() throws Exception {
                    return ImageIO.read(selected);
                }
                @Override
                protected void done() {
                    try { anhHienTai = get(); } catch (Exception ignored) { anhHienTai = null; }
                    panelHinhAnh.repaint();
                }
            }.execute();
        }
    }

    private File getAnhFolder() {
        try {
            URL url = getClass().getResource("/image/diadiem");
            if (url != null && "file".equals(url.getProtocol())) {
                return new File(url.toURI());
            }
        } catch (Exception ignored) {}
        File f = new File("src/main/resources/image/diadiem");
        return f.exists() ? f : new File(".");
    }

    // ─── CRUD handlers ───────────────────────────────────────────────────────

    private void xuLyThemMoi() {
        DiaDiem dd = docDuLieuForm();
        if (dd == null) return;
        try {
            bus.addDiaDiem(dd);
            hienThiThongBao("Thêm địa điểm \"" + dd.getTenDiaDiem() + "\" thành công.");
            taiDuLieu(null); resetFormThemMoi();
        } catch (BusException ex) { hienThiLoi("Thêm thất bại:\n" + ex.getMessage()); }
    }

    private void xuLyCapNhat() {
        if (ddDangChon == null) { hienThiLoi("Vui lòng chọn một địa điểm để cập nhật."); return; }
        DiaDiem dd = docDuLieuForm();
        if (dd == null) return;
        try {
            bus.updateDiaDiem(dd);
            hienThiThongBao("Cập nhật địa điểm \"" + dd.getTenDiaDiem() + "\" thành công.");
            taiDuLieu(txtTimKiem.getText());
        } catch (BusException ex) { hienThiLoi("Cập nhật thất bại:\n" + ex.getMessage()); }
    }

    private void xuLyXoa() {
        if (ddDangChon == null) { hienThiLoi("Vui lòng chọn một địa điểm để xóa."); return; }
        int xacNhan = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa địa điểm \"" + ddDangChon.getTenDiaDiem() + "\" không?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (xacNhan != JOptionPane.YES_OPTION) return;
        try {
            bus.deleteDiaDiem(ddDangChon.getMaDiaDiem());
            hienThiThongBao("Đã xóa địa điểm \"" + ddDangChon.getTenDiaDiem() + "\" thành công.");
            taiDuLieu(null); resetFormThemMoi();
        } catch (BusException ex) { hienThiLoi("Xóa thất bại:\n" + ex.getMessage()); }
    }

    private void lamMoiToanBo() {
        txtTimKiem.setText(""); taiDuLieu(null); resetFormThemMoi();
    }

    private DiaDiem docDuLieuForm() {
        String ma = txtMaDiaDiem.getText().trim();
        String ten = txtTenDiaDiem.getText().trim();
        String quocGia = txtQuocGia.getText().trim();
        String anh = txtAnhDiaDiem.getText().trim();
        String moTa = txtMoTa.getText().trim();
        if (ten.isEmpty()) { hienThiLoi("Tên địa điểm không được để trống."); txtTenDiaDiem.requestFocusInWindow(); return null; }
        if (quocGia.isEmpty()) { hienThiLoi("Quốc gia không được để trống."); txtQuocGia.requestFocusInWindow(); return null; }
        if (anh.isEmpty()) { hienThiLoi("Vui lòng chọn ảnh địa điểm."); return null; }
        if (moTa.isEmpty()) { hienThiLoi("Mô tả không được để trống."); txtMoTa.requestFocusInWindow(); return null; }
        return DiaDiem.builder()
                .maDiaDiem(ma).tenDiaDiem(ten).quocGia(quocGia)
                .anhDiaDiem(anh).moTa(moTa).build();
    }

    private void hienThiLoi(String thongBao) {
        JOptionPane.showMessageDialog(this, thongBao, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private void hienThiThongBao(String thongBao) {
        JOptionPane.showMessageDialog(this, thongBao, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
}
