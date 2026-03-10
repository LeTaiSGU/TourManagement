package GUI;

import BUS.KhachHangBUS;
import DTO.CTCN_NQ;
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

    public KhachHangPanel(CTCN_NQ ctnq) {
        this.bus = new KhachHangBUS();
        xayDungGiaoDien();
        taiDuLieu(null);
        resetFormThemMoi();

        String chiTiet = (ctnq != null && ctnq.getChiTiet() != null) ? ctnq.getChiTiet() : "";
        boolean coQuyenThem = chiTiet.contains("Thêm");
        boolean coQuyenSua = chiTiet.contains("Sửa");
        boolean coQuyenXoa = chiTiet.contains("Xóa");

        btnThemMoi.setVisible(coQuyenThem);
        btnCapNhat.setVisible(coQuyenSua);
        btnXoa.setVisible(coQuyenXoa);
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
        JLabel lblTieuDe = new JLabel("Quản lý khách hàng");
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
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    taiDuLieu(txtTimKiem.getText());
            }
        });
        btnTimKiem = new ActionButton();
        btnTimKiem.setText("Tìm kiếm");
        btnTimKiem.setPreferredSize(new Dimension(120, 32));
        btnTimKiem.addActionListener(e -> taiDuLieu(txtTimKiem.getText()));
        panelTrai.add(lblTieuDe);
        panelTrai.add(Box.createHorizontalStrut(12));
        panelTrai.add(txtTimKiem);
        panelTrai.add(btnTimKiem);
        lblTongSo = new JLabel("Tổng 0 bản ghi");
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
                new String[] { "STT", "Mã KH", "Tên khách hàng", "Giới tính", "Năm sinh",
                        "Đỉa chỉ", "SDT", "Loại KH", "Email" },
                0) {
            @Override
            public boolean isCellEditable(int r, int c) {
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
                if (!sel)
                    setBackground(row % 2 == 0 ? MAU_TRANG : MAU_HANG_XEN);
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
                if (row >= 0)
                    dienFormTuHang(row);
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
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 2, 10, 2);
        panelNoiDung.add(lblTieuDe, gbc);
        gbc.insets = new Insets(4, 2, 4, 2);
        gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Mã khách hàng *");
        txtMaKH = new JTextField();
        txtMaKH.setFont(new Font(FONT, Font.PLAIN, 13));
        txtMaKH.setEditable(false);
        txtMaKH.setBackground(new Color(240, 243, 247));
        styleInput(txtMaKH);
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        panelNoiDung.add(txtMaKH, gbc);
        gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Tên khách hàng");
        txtTenKH = new JTextField();
        txtTenKH.setFont(new Font(FONT, Font.PLAIN, 13));
        styleInput(txtTenKH);
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        panelNoiDung.add(txtTenKH, gbc);
        gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Giới tính");
        cboGioiTinh = new JComboBox<>(new String[] { "Nam", "Nữ", "Khác" });
        cboGioiTinh.setFont(new Font(FONT, Font.PLAIN, 13));
        cboGioiTinh.setPreferredSize(new Dimension(0, 32));
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        panelNoiDung.add(cboGioiTinh, gbc);
        gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Năm sinh *");
        txtNamSinh = new JTextField();
        txtNamSinh.setFont(new Font(FONT, Font.PLAIN, 13));
        styleInput(txtNamSinh);
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        panelNoiDung.add(txtNamSinh, gbc);
        gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Địa chỉ *");
        txtDiaChi = new JTextField();
        txtDiaChi.setFont(new Font(FONT, Font.PLAIN, 13));
        styleInput(txtDiaChi);
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        panelNoiDung.add(txtDiaChi, gbc);
        gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Số điện thoại *");
        txtSoDienThoai = new JTextField();
        txtSoDienThoai.setFont(new Font(FONT, Font.PLAIN, 13));
        styleInput(txtSoDienThoai);
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        panelNoiDung.add(txtSoDienThoai, gbc);
        gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Email *");
        txtEmail = new JTextField();
        txtEmail.setFont(new Font(FONT, Font.PLAIN, 13));
        styleInput(txtEmail);
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        panelNoiDung.add(txtEmail, gbc);
        gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Loại khách hàng");
        cboLoaiKH = new JComboBox<>(new String[] { "LKH01", "LKH02", "LKH03" });
        cboLoaiKH.setFont(new Font(FONT, Font.PLAIN, 13));
        cboLoaiKH.setPreferredSize(new Dimension(0, 32));
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        panelNoiDung.add(cboLoaiKH, gbc);
        gbc.gridwidth = 1;

        JPanel panelNut = xayDungPanelNut();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
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
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(8, 2, 1, 2);
        panel.add(lbl, gbc);
        gbc.insets = new Insets(4, 2, 4, 2);
        gbc.gridwidth = 1;
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
        btnThemMoi = new ActionButton();
        btnThemMoi.setText("Thêm mới");
        btnCapNhat = new ActionButton();
        btnCapNhat.setText("Cập nhật");
        btnCapNhat.setColorTop(new Color(39, 174, 96));
        btnCapNhat.setColorBottom(new Color(27, 124, 66));
        btnXoa = new ActionButton();
        btnXoa.setText("Xóa");
        btnXoa.setColorTop(new Color(231, 76, 60));
        btnXoa.setColorBottom(new Color(192, 57, 43));
        btnLamMoi = new ActionButton();
        btnLamMoi.setText("Làm mới");
        btnLamMoi.setColorTop(new Color(127, 140, 141));
        btnLamMoi.setColorBottom(new Color(99, 110, 114));
        btnThemMoi.addActionListener(e -> xuLyThemMoi());
        btnCapNhat.addActionListener(e -> xuLyCapNhat());
        btnXoa.addActionListener(e -> xuLyXoa());
        btnLamMoi.addActionListener(e -> lamMoiToanBo());
        panel.add(btnThemMoi);
        panel.add(btnCapNhat);
        panel.add(btnXoa);
        panel.add(btnLamMoi);
        return panel;
    }

    private void taiDuLieu(String tuKhoa) {
        lblTongSo.setText("Đang tải...");
        new SwingWorker<ArrayList<KhachHangDTO>, Void>() {
            @Override
            protected ArrayList<KhachHangDTO> doInBackground() throws Exception {
                return (tuKhoa == null || tuKhoa.isBlank())
                        ? bus.getAllKhachHang()
                        : bus.searchKhachHang(tuKhoa);
            }

            @Override
            protected void done() {
                try {
                    ArrayList<KhachHangDTO> ds = get();
                    modelBang.setRowCount(0);
                    int stt = 1;
                    for (KhachHangDTO kh : ds) {
                        modelBang.addRow(new Object[] {
                                stt++, kh.getMaKhachHang(), kh.getTenKhachHang(),
                                kh.getGioiTinh(), kh.getNamSinh(),
                                kh.getDiaChi(), kh.getSoDienThoai(),
                                kh.getMaLoaiKH(), kh.getEmail()
                        });
                    }
                    lblTongSo.setText("Tổng: " + ds.size() + " bản ghi");
                } catch (Exception ex) {
                    hienThiLoi("Không thể tải dữ liệu:\n" + layThongDiepLoiGoc(ex));
                    lblTongSo.setText("Lỗi tải dữ liệu");
                }
            }
        }.execute();
    }

    private String layThongDiepLoiGoc(Throwable ex) {
        Throwable root = ex;
        while (root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }
        String msg = root.getMessage();
        return (msg == null || msg.trim().isEmpty()) ? root.toString() : msg;
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
        try {
            txtMaKH.setText(bus.sinhMaMoi());
        } catch (Exception e) {
            txtMaKH.setText("KH---");
        }
        txtTenKH.setText("");
        cboGioiTinh.setSelectedIndex(0);
        txtNamSinh.setText("");
        txtDiaChi.setText("");
        txtSoDienThoai.setText("");
        txtEmail.setText("");
        cboLoaiKH.setSelectedIndex(0);
        bangDuLieu.clearSelection();
        txtTenKH.requestFocusInWindow();
    }

    private void xuLyThemMoi() {
        KhachHangDTO kh = docDuLieuForm();
        if (kh == null)
            return;
        try {
            bus.addKhachHang(kh);
            hienThiThongBao("Thêm khách hàng \"" + kh.getTenKhachHang() + "\" thành công.");
            taiDuLieu(null);
            resetFormThemMoi();
        } catch (BusException ex) {
            hienThiLoi("Thêm thất bại:\n" + ex.getMessage());
        }
    }

    private void xuLyCapNhat() {
        if (khDangChon == null) {
            hienThiLoi("Vui lòng chọn một khách hàng để cập nhật.");
            return;
        }
        KhachHangDTO kh = docDuLieuForm();
        if (kh == null)
            return;
        try {
            bus.updateKhachHang(kh);
            hienThiThongBao("Cập nhật khách hàng \"" + kh.getTenKhachHang() + "\" thành công.");
            taiDuLieu(txtTimKiem.getText());
        } catch (BusException ex) {
            hienThiLoi("Cập nhật thất bại:\n" + ex.getMessage());
        }
    }

    private void xuLyXoa() {
        if (khDangChon == null) {
            hienThiLoi("Vui lòng chọn một khách hàng để xóa.");
            return;
        }
        int xacNhan = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa khách hàng \"" + khDangChon.getTenKhachHang() + "\" không?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (xacNhan != JOptionPane.YES_OPTION)
            return;
        try {
            bus.deleteKhachHang(khDangChon.getMaKhachHang());
            hienThiThongBao("Đã xóa khách hàng \"" + khDangChon.getTenKhachHang() + "\" thành công.");
            taiDuLieu(null);
            resetFormThemMoi();
        } catch (BusException ex) {
            hienThiLoi("Xóa thất bại:\n" + ex.getMessage());
        }
    }

    private void lamMoiToanBo() {
        txtTimKiem.setText("");
        taiDuLieu(null);
        resetFormThemMoi();
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
        if (tenKH.isEmpty()) {
            hienThiLoi("Tên khách hàng không được để trống.");
            txtTenKH.requestFocusInWindow();
            return null;
        }
        int namSinh;
        try {
            namSinh = Integer.parseInt(namSinhStr);
        } catch (NumberFormatException e) {
            hienThiLoi("Năm sinh phải là số nguyên (VD: 1995).");
            txtNamSinh.requestFocusInWindow();
            return null;
        }
        if (diaChi.isEmpty()) {
            hienThiLoi("Địa chỉ không được để trống.");
            txtDiaChi.requestFocusInWindow();
            return null;
        }
        if (sdt.isEmpty()) {
            hienThiLoi("Số điện thoại không được để trống.");
            txtSoDienThoai.requestFocusInWindow();
            return null;
        }
        if (email.isEmpty()) {
            hienThiLoi("Email không được để trống.");
            txtEmail.requestFocusInWindow();
            return null;
        }
        return KhachHangDTO.builder()
                .maKhachHang(maKH).tenKhachHang(tenKH).gioiTinh(gioiTinh)
                .namSinh(namSinh).diaChi(diaChi).soDienThoai(sdt)
                .email(email).maLoaiKH(loaiKH).build();
    }

    private void hienThiLoi(String thongBao) {
        JOptionPane.showMessageDialog(this, thongBao, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private void hienThiThongBao(String thongBao) {
        JOptionPane.showMessageDialog(this, thongBao, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

}
