package GUI;

import BUS.KhachHangBUS;
import DTO.KhachHangDTO;
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
import java.util.ArrayList;

public class KhachHangPanel extends JPanel {

    private static final Color MAU_CHINH = new Color(41, 128, 185);
    private static final Color MAU_NEN = new Color(246, 247, 248);
    private static final Color MAU_TRANG = Color.WHITE;
    private static final Color MAU_HANG_XEN = new Color(240, 246, 252);
    private static final Color MAU_CHON = new Color(214, 234, 248);
    private static final Color MAU_CHU_TOI = new Color(44, 62, 80);
    private static final Color MAU_CHU_PHU = new Color(127, 140, 141);
    private static final Color MAU_VIEN = new Color(213, 219, 219);
    private static final String FONT = "Segoe UI";

    private final KhachHangBUS bus;
    private KhachHangDTO khDangChon = null;

    private JTextField txtTimKiem;
    private ActionButton btnTimKiem;
    private JLabel lblTongSo;

    private JTable bangDuLieu;
    private DefaultTableModel modelBang;

    private JTextField txtMaKH;
    private JTextField txtTenKH;
    private JComboBox<String> cboGioiTinh;
    private JTextField txtNamSinh;
    private JTextField txtDiaChi;
    private JTextField txtSoDienThoai;
    private JTextField txtEmail;
    private JComboBox<String> cboLoaiKH;

    private ActionButton btnThemMoi;
    private ActionButton btnCapNhat;
    private ActionButton btnXoa;
    private ActionButton btnLamMoi;

    public KhachHangPanel() {
        this.bus = new KhachHangBUS();
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
        tabs.addTab("Khách hàng", panelTab);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel xayDungThanhTimKiem() {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setBackground(MAU_NEN);
        panel.setBorder(new EmptyBorder(0, 0, 4, 0));
        JPanel panelTrai = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panelTrai.setBackground(MAU_NEN);
        JLabel lblTieuDe = new JLabel("Quản lý Khách hàng");
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
                new String[]{"STT", "Mã KH", "Tên khách hàng", "Giới tính", "Năm sinh",
                        "Địa chỉ", "Số ĐT", "Loại KH", "Email"}, 0) {
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
        bangDuLieu.getColumnModel().getColumn(2).setPreferredWidth(170);
        bangDuLieu.getColumnModel().getColumn(3).setPreferredWidth(75);
        bangDuLieu.getColumnModel().getColumn(4).setPreferredWidth(70);
        bangDuLieu.getColumnModel().getColumn(5).setPreferredWidth(160);
        bangDuLieu.getColumnModel().getColumn(6).setPreferredWidth(110);
        bangDuLieu.getColumnModel().getColumn(7).setPreferredWidth(80);
        bangDuLieu.getColumnModel().getColumn(8).setPreferredWidth(160);
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
                setHorizontalAlignment(col == 0 || col == 4 ? CENTER : LEFT);
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

        JLabel lblTieuDe = new JLabel("THÔNG TIN KHÁCH HÀNG");
        lblTieuDe.setFont(new Font(FONT, Font.BOLD, 13));
        lblTieuDe.setForeground(MAU_CHINH);
        lblTieuDe.setBorder(new MatteBorder(0, 0, 1, 0, MAU_VIEN));
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 2, 10, 2);
        panelNoiDung.add(lblTieuDe, gbc);
        gbc.insets = new Insets(4, 2, 4, 2); gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Mã khách hàng *");
        txtMaKH = new JTextField();
        txtMaKH.setFont(new Font(FONT, Font.PLAIN, 13));
        txtMaKH.setEditable(false);
        txtMaKH.setBackground(new Color(240, 243, 247));
        styleInput(txtMaKH);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        panelNoiDung.add(txtMaKH, gbc); gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Tên khách hàng *");
        txtTenKH = new JTextField();
        txtTenKH.setFont(new Font(FONT, Font.PLAIN, 13));
        styleInput(txtTenKH);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        panelNoiDung.add(txtTenKH, gbc); gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Giới tính");
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
        cboGioiTinh.setFont(new Font(FONT, Font.PLAIN, 13));
        cboGioiTinh.setPreferredSize(new Dimension(0, 32));
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        panelNoiDung.add(cboGioiTinh, gbc); gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Năm sinh *");
        txtNamSinh = new JTextField();
        txtNamSinh.setFont(new Font(FONT, Font.PLAIN, 13));
        styleInput(txtNamSinh);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        panelNoiDung.add(txtNamSinh, gbc); gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Địa chỉ *");
        txtDiaChi = new JTextField();
        txtDiaChi.setFont(new Font(FONT, Font.PLAIN, 13));
        styleInput(txtDiaChi);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        panelNoiDung.add(txtDiaChi, gbc); gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Số điện thoại *");
        txtSoDienThoai = new JTextField();
        txtSoDienThoai.setFont(new Font(FONT, Font.PLAIN, 13));
        styleInput(txtSoDienThoai);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        panelNoiDung.add(txtSoDienThoai, gbc); gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Email *");
        txtEmail = new JTextField();
        txtEmail.setFont(new Font(FONT, Font.PLAIN, 13));
        styleInput(txtEmail);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        panelNoiDung.add(txtEmail, gbc); gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Loại khách hàng");
        cboLoaiKH = new JComboBox<>(new String[]{"LKH01", "LKH02", "LKH03"});
        cboLoaiKH.setFont(new Font(FONT, Font.PLAIN, 13));
        cboLoaiKH.setPreferredSize(new Dimension(0, 32));
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        panelNoiDung.add(cboLoaiKH, gbc); gbc.gridwidth = 1;

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

    private void taiDuLieu(String tuKhoa) {
        lblTongSo.setText("Đang tải...");
        new SwingWorker<ArrayList<KhachHangDTO>, Void>() {
            @Override
            protected ArrayList<KhachHangDTO> doInBackground() throws Exception {
                return (tuKhoa == null || tuKhoa.isBlank())
                        ? bus.getAllKhachHang() : bus.searchKhachHang(tuKhoa);
            }
            @Override
            protected void done() {
                try {
                    ArrayList<KhachHangDTO> ds = get();
                    modelBang.setRowCount(0);
                    int stt = 1;
                    for (KhachHangDTO kh : ds) {
                        modelBang.addRow(new Object[]{
                            stt++, kh.getMaKhachHang(), kh.getTenKhachHang(),
                            kh.getGioiTinh(), kh.getNamSinh(),
                            kh.getDiaChi(), kh.getSoDienThoai(),
                            kh.getMaLoaiKH(), kh.getEmail()
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
        String maKH = (String) modelBang.getValueAt(row, 1);
        String tenKH = (String) modelBang.getValueAt(row, 2);
        String gioiTinh = (String) modelBang.getValueAt(row, 3);
        Object namSinh = modelBang.getValueAt(row, 4);
        String diaChi = (String) modelBang.getValueAt(row, 5);
        String sdt = (String) modelBang.getValueAt(row, 6);
        String loaiKH = (String) modelBang.getValueAt(row, 7);
        String email = (String) modelBang.getValueAt(row, 8);
        khDangChon = KhachHangDTO.builder()
                .maKhachHang(maKH).tenKhachHang(tenKH).gioiTinh(gioiTinh)
                .namSinh(namSinh instanceof Integer ? (Integer) namSinh : 0)
                .diaChi(diaChi).soDienThoai(sdt).maLoaiKH(loaiKH).email(email).build();
        txtMaKH.setText(maKH);
        txtTenKH.setText(tenKH);
        cboGioiTinh.setSelectedItem(gioiTinh != null ? gioiTinh : "Nam");
        txtNamSinh.setText(namSinh != null ? namSinh.toString() : "");
        txtDiaChi.setText(diaChi != null ? diaChi : "");
        txtSoDienThoai.setText(sdt != null ? sdt : "");
        txtEmail.setText(email != null ? email : "");
        cboLoaiKH.setSelectedItem(loaiKH);
    }

    private void resetFormThemMoi() {
        khDangChon = null;
        try { txtMaKH.setText(bus.sinhMaMoi()); } catch (Exception e) { txtMaKH.setText("KH---"); }
        txtTenKH.setText(""); cboGioiTinh.setSelectedIndex(0);
        txtNamSinh.setText(""); txtDiaChi.setText("");
        txtSoDienThoai.setText(""); txtEmail.setText("");
        cboLoaiKH.setSelectedIndex(0);
        bangDuLieu.clearSelection();
        txtTenKH.requestFocusInWindow();
    }

    private void xuLyThemMoi() {
        KhachHangDTO kh = docDuLieuForm();
        if (kh == null) return;
        try {
            bus.addKhachHang(kh);
            hienThiThongBao("Thêm khách hàng \"" + kh.getTenKhachHang() + "\" thành công.");
            taiDuLieu(null); resetFormThemMoi();
        } catch (BusException ex) { hienThiLoi("Thêm thất bại:\n" + ex.getMessage()); }
    }

    private void xuLyCapNhat() {
        if (khDangChon == null) { hienThiLoi("Vui lòng chọn một khách hàng để cập nhật."); return; }
        KhachHangDTO kh = docDuLieuForm();
        if (kh == null) return;
        try {
            bus.updateKhachHang(kh);
            hienThiThongBao("Cập nhật khách hàng \"" + kh.getTenKhachHang() + "\" thành công.");
            taiDuLieu(txtTimKiem.getText());
        } catch (BusException ex) { hienThiLoi("Cập nhật thất bại:\n" + ex.getMessage()); }
    }

    private void xuLyXoa() {
        if (khDangChon == null) { hienThiLoi("Vui lòng chọn một khách hàng để xóa."); return; }
        int xacNhan = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa khách hàng \"" + khDangChon.getTenKhachHang() + "\" không?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (xacNhan != JOptionPane.YES_OPTION) return;
        try {
            bus.deleteKhachHang(khDangChon.getMaKhachHang());
            hienThiThongBao("Đã xóa khách hàng \"" + khDangChon.getTenKhachHang() + "\" thành công.");
            taiDuLieu(null); resetFormThemMoi();
        } catch (BusException ex) { hienThiLoi("Xóa thất bại:\n" + ex.getMessage()); }
    }

    private void lamMoiToanBo() {
        txtTimKiem.setText(""); taiDuLieu(null); resetFormThemMoi();
    }

    private KhachHangDTO docDuLieuForm() {
        String maKH = txtMaKH.getText().trim();
        String tenKH = txtTenKH.getText().trim();
        String gioiTinh = (String) cboGioiTinh.getSelectedItem();
        String namSinhStr = txtNamSinh.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();
        String email = txtEmail.getText().trim();
        String loaiKH = (String) cboLoaiKH.getSelectedItem();
        if (tenKH.isEmpty()) { hienThiLoi("Tên khách hàng không được để trống."); txtTenKH.requestFocusInWindow(); return null; }
        int namSinh;
        try { namSinh = Integer.parseInt(namSinhStr); }
        catch (NumberFormatException e) { hienThiLoi("Năm sinh phải là số nguyên (VD: 1995)."); txtNamSinh.requestFocusInWindow(); return null; }
        if (diaChi.isEmpty()) { hienThiLoi("Địa chỉ không được để trống."); txtDiaChi.requestFocusInWindow(); return null; }
        if (sdt.isEmpty()) { hienThiLoi("Số điện thoại không được để trống."); txtSoDienThoai.requestFocusInWindow(); return null; }
        if (email.isEmpty()) { hienThiLoi("Email không được để trống."); txtEmail.requestFocusInWindow(); return null; }
        return KhachHangDTO.builder()
                .maKhachHang(maKH).tenKhachHang(tenKH).gioiTinh(gioiTinh)
                .namSinh(namSinh).diaChi(diaChi).soDienThoai(sdt)
                .email(email).maLoaiKH(loaiKH).build();
    }

    private void hienThiLoi(String thongBao) {
        JOptionPane.showMessageDialog(this, thongBao, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    private void hienThiThongBao(String thongBao) {
        JOptionPane.showMessageDialog(this, thongBao, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    // dummy constructor fragment placeholder — replaced below
    private void UNUSED() {
        initComponents();
        customTableHeader();
        setupTableResize();
        loadData ();
        khoiTaoTableSorter() ;

    }
    private void khoiTaoTableSorter() {
    DefaultTableModel model = (DefaultTableModel) tbKhachHang.getModel();
    sorter = new TableRowSorter<>(model);
    tbKhachHang.setRowSorter(sorter);
    }
    private void customTableHeader() {
        JTableHeader header = tbKhachHang.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    javax.swing.JTable t, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                setBackground(new Color(41, 128, 185));
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 16));
                setHorizontalAlignment(CENTER);
                setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(255, 255, 255, 60)));
                setOpaque(true);
                return this;
            }
        });
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);
    }
    private void setupTableResize() {
    tbKhachHang.setAutoResizeMode(tbKhachHang.AUTO_RESIZE_ALL_COLUMNS);
    tbKhachHang.setRowHeight(28);
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(CENTER);

    for (int i = 0; i < tbKhachHang.getColumnCount(); i++) {
        tbKhachHang.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
    TableColumnModel columnModel = tbKhachHang.getColumnModel();
    for (int i = 0; i < columnModel.getColumnCount(); i++) {
        columnModel.getColumn(i).setPreferredWidth(150);
    }
    }
    public void loadData() {
    DefaultTableModel model = (DefaultTableModel) tbKhachHang.getModel();
    model.setRowCount(0);

    try {
        ArrayList<KhachHangDTO> list = bus.getAllKhachHang();
        for(KhachHangDTO kh : list) {
            model.addRow(new Object[] {
                kh.getMaKhachHang(),
                kh.getTenKhachHang(),
                kh.getGioiTinh(),
                kh.getNamSinh(),
                kh.getDiaChi(),
                kh.getSoDienThoai(),
                kh.getMaLoaiKH(),
                kh.getEmail()
            });
        }

    } catch (BusException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách khách hàng", "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jpanel7 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        maKhachHang = new GUI.LoginForm.CustomTextField();
        lammoi = new GUI.Menu.ActionButton();
        timkiem = new GUI.Menu.ActionButton();
        gioiTinh = new GUI.LoginForm.CustomTextField();
        tenKhachHang = new GUI.LoginForm.CustomTextField();
        namSinh = new GUI.LoginForm.CustomTextField();
        jLabel45 = new javax.swing.JLabel();
        diaChi = new GUI.LoginForm.CustomTextField();
        jLabel46 = new javax.swing.JLabel();
        soDienThoai = new GUI.LoginForm.CustomTextField();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        email = new GUI.LoginForm.CustomTextField();
        jLabel49 = new javax.swing.JLabel();
        maLoaiKH = new GUI.Menu.CustomComboBox();
        them = new GUI.Menu.ActionButton();
        sua = new GUI.Menu.ActionButton();
        xoa = new GUI.Menu.ActionButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbKhachHang = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Tìm kiếm");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addComponent(jLabel1)
                .addContainerGap(103, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel5, java.awt.BorderLayout.PAGE_START);

        jpanel7.setBackground(new java.awt.Color(255, 255, 255));
        jpanel7.setPreferredSize(new java.awt.Dimension(300, 950));

        jLabel42.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel42.setText("Mã khách hàng");

        jLabel43.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel43.setText("Tên khách hàng");

        jLabel44.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel44.setText("Giới tính");

        maKhachHang.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        maKhachHang.setIconGap(1);

        lammoi.setBorder(null);
        lammoi.setForeground(new java.awt.Color(0, 0, 0));
        lammoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/reload.png"))); // NOI18N
        lammoi.setText("Làm mới");
        lammoi.setColorBottom(new java.awt.Color(204, 204, 204));
        lammoi.setColorTop(new java.awt.Color(255, 255, 255));
        lammoi.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lammoi.addActionListener(this::lammoiActionPerformed);

        timkiem.setBorder(null);
        timkiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/search2.png"))); // NOI18N
        timkiem.setText("Tìm kiếm");
        timkiem.setColorBottom(new java.awt.Color(0, 153, 255));
        timkiem.setColorTop(new java.awt.Color(102, 255, 255));
        timkiem.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        timkiem.addActionListener(this::timkiemActionPerformed);

        gioiTinh.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        tenKhachHang.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        namSinh.setFocusable(false);
        namSinh.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jLabel45.setFont(new java.awt.Font("sansserif", 1, 16)); // NOI18N
        jLabel45.setText("Năm sinh");

        diaChi.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jLabel46.setFont(new java.awt.Font("sansserif", 1, 16)); // NOI18N
        jLabel46.setText("Địa chỉ");

        soDienThoai.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jLabel47.setFont(new java.awt.Font("sansserif", 1, 16)); // NOI18N
        jLabel47.setText("Số điện thoại");

        jLabel48.setFont(new java.awt.Font("sansserif", 1, 16)); // NOI18N
        jLabel48.setText("Mã loại khách hàng");

        email.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jLabel49.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel49.setText("Email");

        maLoaiKH.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tất cả", "LKH01", "LKH02", "LKH03", " " }));
        maLoaiKH.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        maLoaiKH.addActionListener(this::maLoaiKHActionPerformed);

        them.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/plus.png"))); // NOI18N
        them.setText("Thêm");
        them.setColorBottom(new java.awt.Color(0, 153, 255));
        them.setColorTop(new java.awt.Color(102, 255, 255));
        them.addActionListener(this::themActionPerformed);

        sua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/pencil.png"))); // NOI18N
        sua.setText("Sửa");
        sua.setColorBottom(new java.awt.Color(51, 153, 255));
        sua.setColorTop(new java.awt.Color(102, 255, 255));
        sua.addActionListener(this::suaActionPerformed);

        xoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/delete.png"))); // NOI18N
        xoa.setText("Xóa");
        xoa.setColorBottom(new java.awt.Color(153, 0, 51));
        xoa.setColorTop(new java.awt.Color(255, 102, 102));
        xoa.addActionListener(this::xoaActionPerformed);

        javax.swing.GroupLayout jpanel7Layout = new javax.swing.GroupLayout(jpanel7);
        jpanel7.setLayout(jpanel7Layout);
        jpanel7Layout.setHorizontalGroup(
            jpanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpanel7Layout.createSequentialGroup()
                .addGroup(jpanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jpanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jpanel7Layout.createSequentialGroup()
                            .addGap(21, 21, 21)
                            .addComponent(soDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jpanel7Layout.createSequentialGroup()
                            .addGap(22, 22, 22)
                            .addGroup(jpanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jpanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(maKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(tenKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(gioiTinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(namSinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(diaChi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lammoi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jpanel7Layout.createSequentialGroup()
                                        .addGroup(jpanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(them, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(timkiem, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jpanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(sua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(xoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel43, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel44, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel45, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel46, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel47, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jpanel7Layout.createSequentialGroup()
                            .addGap(20, 20, 20)
                            .addGroup(jpanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(maLoaiKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jpanel7Layout.setVerticalGroup(
            jpanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpanel7Layout.createSequentialGroup()
                .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tenKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(namSinh, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(diaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(soDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel48, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maLoaiKH, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel49, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jpanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timkiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(them, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lammoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(195, Short.MAX_VALUE))
        );

        jPanel2.add(jpanel7, java.awt.BorderLayout.LINE_END);

        jPanel1.add(jPanel2, java.awt.BorderLayout.LINE_START);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jLabel50.setBackground(new java.awt.Color(255, 255, 255));
        jLabel50.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel50.setText("Danh sách khách hàng");
        jLabel50.setPreferredSize(new java.awt.Dimension(230, 80));
        jPanel3.add(jLabel50, java.awt.BorderLayout.PAGE_START);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        tbKhachHang.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        tbKhachHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã Khách Hàng", "Tên khách hàng", "Giới tính", "Năm sinh", "Địa chỉ", "Số điện thoại", "Mã loại khách hàng", "Email"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbKhachHang);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 973, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 519, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(361, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void lammoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lammoiActionPerformed
        maKhachHang.setText("");
        tenKhachHang.setText("");
        gioiTinh.setText("");
        namSinh.setText("");
        diaChi.setText("");
        soDienThoai.setText("");
        email.setText("");
    }//GEN-LAST:event_lammoiActionPerformed

    private void timkiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timkiemActionPerformed
        DefaultTableModel model = (DefaultTableModel) tbKhachHang.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        ArrayList<RowFilter<DefaultTableModel, Integer>> filters = new ArrayList<>();
        tbKhachHang.setRowSorter(sorter);
        if(!maKhachHang.getText().trim().isEmpty()){
        filters.add(RowFilter.regexFilter("(?i)" + maKhachHang.getText(), 0));
        }
            if(!tenKhachHang.getText().trim().isEmpty()){
                filters.add(RowFilter.regexFilter("(?i)" + tenKhachHang.getText(), 1));
            }
            if(!gioiTinh.getText().trim().isEmpty()){
                filters.add(RowFilter.regexFilter("(?i)" + gioiTinh.getText(), 2));
            }
            if(!namSinh.getText().trim().isEmpty()){
                filters.add(RowFilter.regexFilter(namSinh.getText(), 3));
            }
            if(!diaChi.getText().trim().isEmpty()){
                filters.add(RowFilter.regexFilter("(?i)" + diaChi.getText(), 4));
            }
            if(!soDienThoai.getText().trim().isEmpty()){
                filters.add(RowFilter.regexFilter(soDienThoai.getText(), 5));
            }
        String loai = maLoaiKH.getSelectedItem().toString();
            if(!loai.equals("Tất cả")){
                filters.add(RowFilter.regexFilter(loai, 6));
            }
            if(!email.getText().trim().isEmpty()){
                filters.add(RowFilter.regexFilter("(?i)" + email.getText(), 7));
            }
        sorter.setRowFilter(RowFilter.andFilter(filters));
    }//GEN-LAST:event_timkiemActionPerformed

    private void themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_themActionPerformed
        DefaultTableModel model = (DefaultTableModel) tbKhachHang.getModel();
        model.addRow(new Object[]{
        maKhachHang.getText(),
        tenKhachHang.getText(),
        gioiTinh.getText(),
        namSinh.getText(),
        diaChi.getText(),
        soDienThoai.getText(),
        email.getText()
    });
    }//GEN-LAST:event_themActionPerformed

    private void suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suaActionPerformed
        int i = tbKhachHang.getSelectedRow();
        if(i >= 0){
        DefaultTableModel model = (DefaultTableModel) tbKhachHang.getModel();
        model.setValueAt(maKhachHang.getText(), i, 0);
        model.setValueAt(tenKhachHang.getText(), i, 1);
        model.setValueAt(gioiTinh.getText(), i, 2);
        model.setValueAt(namSinh.getText(), i, 3);
        model.setValueAt(diaChi.getText(), i, 4);
        model.setValueAt(soDienThoai.getText(), i, 5);
        model.setValueAt(email.getText(), i, 6);
    }
    }//GEN-LAST:event_suaActionPerformed

    private void xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xoaActionPerformed
        int i = tbKhachHang.getSelectedRow();
        if(i >= 0){
        DefaultTableModel model = (DefaultTableModel) tbKhachHang.getModel();
        model.removeRow(i);
    }
    }//GEN-LAST:event_xoaActionPerformed

    private void maLoaiKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maLoaiKHActionPerformed
        
    }//GEN-LAST:event_maLoaiKHActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private GUI.LoginForm.CustomTextField diaChi;
    private GUI.LoginForm.CustomTextField email;
    private GUI.LoginForm.CustomTextField gioiTinh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel jpanel7;
    private GUI.Menu.ActionButton lammoi;
    private GUI.LoginForm.CustomTextField maKhachHang;
    private GUI.Menu.CustomComboBox maLoaiKH;
    private GUI.LoginForm.CustomTextField namSinh;
    private GUI.LoginForm.CustomTextField soDienThoai;
    private GUI.Menu.ActionButton sua;
    private javax.swing.JTable tbKhachHang;
    private GUI.LoginForm.CustomTextField tenKhachHang;
    private GUI.Menu.ActionButton them;
    private GUI.Menu.ActionButton timkiem;
    private GUI.Menu.ActionButton xoa;
    // End of variables declaration//GEN-END:variables
}
