package GUI;

import BUS.HuongDanVienBUS;
import DTO.CTCN_NQ;
import DTO.HuongDanVienDTO;
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

public class HuongDanVienPanel extends JPanel {

    private static final Color MAU_CHINH = new Color(41, 128, 185);
    private static final Color MAU_NEN = new Color(246, 247, 248);
    private static final Color MAU_TRANG = Color.WHITE;
    private static final Color MAU_HANG_XEN = new Color(240, 246, 252);
    private static final Color MAU_CHON = new Color(214, 234, 248);
    private static final Color MAU_CHU_TOI = new Color(44, 62, 80);
    private static final Color MAU_CHU_PHU = new Color(127, 140, 141);
    private static final Color MAU_VIEN = new Color(213, 219, 219);
    private static final String FONT = "Segoe UI";

    private final HuongDanVienBUS bus;
    private HuongDanVienDTO hdvDangChon = null;

    private JTextField txtTimKiem;
    private ActionButton btnTimKiem;
    private JLabel lblTongSo;

    private JTable bangDuLieu;
    private DefaultTableModel modelBang;

    private JTextField txtMaHDV;
    private JTextField txtTenHDV;
    private JComboBox<String> cboGioiTinh;
    private JTextField txtNamSinh;
    private JTextField txtChuyenMon;
    private JTextField txtSoDienThoai;

    private ActionButton btnThemMoi;
    private ActionButton btnCapNhat;
    private ActionButton btnXoa;
    private ActionButton btnLamMoi;

    public HuongDanVienPanel(CTCN_NQ ctnq) {
        this.bus = new HuongDanVienBUS();
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
        tabs.addTab("Hướng dẫn viên", panelTab);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel xayDungThanhTimKiem() {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setBackground(MAU_NEN);
        panel.setBorder(new EmptyBorder(0, 0, 4, 0));
        JPanel panelTrai = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panelTrai.setBackground(MAU_NEN);
        JLabel lblTieuDe = new JLabel("Quản lý Hướng dẫn viên");
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
                new String[] { "STT", "Mã HDV", "Tên HDV", "Giới tính", "Năm sinh",
                        "Chuyên môn", "Số ĐT" },
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
        bangDuLieu.getColumnModel().getColumn(1).setPreferredWidth(80);
        bangDuLieu.getColumnModel().getColumn(2).setPreferredWidth(180);
        bangDuLieu.getColumnModel().getColumn(3).setPreferredWidth(80);
        bangDuLieu.getColumnModel().getColumn(4).setPreferredWidth(80);
        bangDuLieu.getColumnModel().getColumn(5).setPreferredWidth(180);
        bangDuLieu.getColumnModel().getColumn(6).setPreferredWidth(120);
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

        JLabel lblTieuDe = new JLabel("THÔNG TIN HƯỚNG DẪN VIÊN");
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

        row = addLabel(panelNoiDung, gbc, row, "Mã HDV *");
        txtMaHDV = new JTextField();
        txtMaHDV.setFont(new Font(FONT, Font.PLAIN, 13));
        txtMaHDV.setEditable(false);
        txtMaHDV.setBackground(new Color(240, 243, 247));
        styleInput(txtMaHDV);
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        panelNoiDung.add(txtMaHDV, gbc);
        gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Tên hướng dẫn viên *");
        txtTenHDV = new JTextField();
        txtTenHDV.setFont(new Font(FONT, Font.PLAIN, 13));
        styleInput(txtTenHDV);
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        panelNoiDung.add(txtTenHDV, gbc);
        gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Giới tính");
        cboGioiTinh = new JComboBox<>(new String[] { "Nam", "Nữ", "Khác" });
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

        row = addLabel(panelNoiDung, gbc, row, "Chuyên môn *");
        txtChuyenMon = new JTextField();
        txtChuyenMon.setFont(new Font(FONT, Font.PLAIN, 13));
        styleInput(txtChuyenMon);
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        panelNoiDung.add(txtChuyenMon, gbc);
        gbc.gridwidth = 1;

        row = addLabel(panelNoiDung, gbc, row, "Số điện thoại *");
        txtSoDienThoai = new JTextField();
        txtSoDienThoai.setFont(new Font(FONT, Font.PLAIN, 13));
        styleInput(txtSoDienThoai);
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        panelNoiDung.add(txtSoDienThoai, gbc);
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
        btnThemMoi.setText("＋  Thêm mới");
        btnCapNhat = new ActionButton();
        btnCapNhat.setText("✎  Cập nhật");
        btnCapNhat.setColorTop(new Color(39, 174, 96));
        btnCapNhat.setColorBottom(new Color(27, 124, 66));
        btnXoa = new ActionButton();
        btnXoa.setText("✖  Xóa");
        btnXoa.setColorTop(new Color(231, 76, 60));
        btnXoa.setColorBottom(new Color(192, 57, 43));
        btnLamMoi = new ActionButton();
        btnLamMoi.setText("⟳  Làm mới");
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
        lblTongSo.setText("Đang tải...");
        new SwingWorker<ArrayList<HuongDanVienDTO>, Void>() {
            @Override
            protected ArrayList<HuongDanVienDTO> doInBackground() throws Exception {
                return (tuKhoa == null || tuKhoa.isBlank())
                        ? bus.getAllHDV()
                        : bus.searchHuongDanVien(tuKhoa);
            }

            @Override
            protected void done() {
                try {
                    ArrayList<HuongDanVienDTO> ds = get();
                    modelBang.setRowCount(0);
                    int stt = 1;
                    for (HuongDanVienDTO hdv : ds) {
                        modelBang.addRow(new Object[] {
                                stt++, hdv.getMaHDV(), hdv.getTenHDV(),
                                hdv.getGioiTinh(), hdv.getNamSinh(),
                                hdv.getChuyenMon(), hdv.getSoDienThoai()
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
        String maHDV = (String) modelBang.getValueAt(row, 1);
        String tenHDV = (String) modelBang.getValueAt(row, 2);
        String gioiTinh = (String) modelBang.getValueAt(row, 3);
        Object namSinh = modelBang.getValueAt(row, 4);
        String chuyenMon = (String) modelBang.getValueAt(row, 5);
        String sdt = (String) modelBang.getValueAt(row, 6);
        hdvDangChon = HuongDanVienDTO.builder()
                .maHDV(maHDV).tenHDV(tenHDV).gioiTinh(gioiTinh)
                .namSinh(namSinh instanceof Integer ? (Integer) namSinh : 0)
                .chuyenMon(chuyenMon).soDienThoai(sdt).build();
        txtMaHDV.setText(maHDV);
        txtTenHDV.setText(tenHDV);
        cboGioiTinh.setSelectedItem(gioiTinh != null ? gioiTinh : "Nam");
        txtNamSinh.setText(namSinh != null ? namSinh.toString() : "");
        txtChuyenMon.setText(chuyenMon != null ? chuyenMon : "");
        txtSoDienThoai.setText(sdt != null ? sdt : "");
    }

    private void resetFormThemMoi() {
        hdvDangChon = null;
        try {
            txtMaHDV.setText(bus.sinhMaMoi());
        } catch (Exception e) {
            txtMaHDV.setText("HDV---");
        }
        txtTenHDV.setText("");
        cboGioiTinh.setSelectedIndex(0);
        txtNamSinh.setText("");
        txtChuyenMon.setText("");
        txtSoDienThoai.setText("");
        bangDuLieu.clearSelection();
        txtTenHDV.requestFocusInWindow();
    }

    private void xuLyThemMoi() {
        HuongDanVienDTO hdv = docDuLieuForm();
        if (hdv == null)
            return;
        try {
            bus.addHuongDanVien(hdv);
            hienThiThongBao("Thêm hướng dẫn viên \"" + hdv.getTenHDV() + "\" thành công.");
            taiDuLieu(null);
            resetFormThemMoi();
        } catch (BusException ex) {
            hienThiLoi("Thêm thất bại:\n" + ex.getMessage());
        }
    }

    private void xuLyCapNhat() {
        if (hdvDangChon == null) {
            hienThiLoi("Vui lòng chọn một hướng dẫn viên để cập nhật.");
            return;
        }
        HuongDanVienDTO hdv = docDuLieuForm();
        if (hdv == null)
            return;
        try {
            bus.updateHuongDanVien(hdv);
            hienThiThongBao("Cập nhật hướng dẫn viên \"" + hdv.getTenHDV() + "\" thành công.");
            taiDuLieu(txtTimKiem.getText());
        } catch (BusException ex) {
            hienThiLoi("Cập nhật thất bại:\n" + ex.getMessage());
        }
    }

    private void xuLyXoa() {
        if (hdvDangChon == null) {
            hienThiLoi("Vui lòng chọn một hướng dẫn viên để xóa.");
            return;
        }
        int xacNhan = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa hướng dẫn viên \"" + hdvDangChon.getTenHDV() + "\" không?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (xacNhan != JOptionPane.YES_OPTION)
            return;
        try {
            bus.deleteHuongDanVien(hdvDangChon.getMaHDV());
            hienThiThongBao("Đã xóa hướng dẫn viên \"" + hdvDangChon.getTenHDV() + "\" thành công.");
            taiDuLieu(null);
            resetFormThemMoi();
        } catch (BusException ex) {
            hienThiLoi("Xóa thất bại:\n" + ex.getMessage());
        }
    }

    private void lamMoiToanBo() {
        txtTimKiem.setText("");
        taiDuLieu(null);
        resetFormThemMoi();
    }

    private HuongDanVienDTO docDuLieuForm() {
        String maHDV = txtMaHDV.getText().trim();
        String tenHDV = txtTenHDV.getText().trim();
        String gioiTinh = (String) cboGioiTinh.getSelectedItem();
        String namSinhStr = txtNamSinh.getText().trim();
        String chuyenMon = txtChuyenMon.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();
        if (tenHDV.isEmpty()) {
            hienThiLoi("Tên hướng dẫn viên không được để trống.");
            txtTenHDV.requestFocusInWindow();
            return null;
        }
        int namSinh;
        try {
            namSinh = Integer.parseInt(namSinhStr);
        } catch (NumberFormatException e) {
            hienThiLoi("Năm sinh phải là số nguyên (VD: 1990).");
            txtNamSinh.requestFocusInWindow();
            return null;
        }
        if (chuyenMon.isEmpty()) {
            hienThiLoi("Chuyên môn không được để trống.");
            txtChuyenMon.requestFocusInWindow();
            return null;
        }
        if (sdt.isEmpty()) {
            hienThiLoi("Số điện thoại không được để trống.");
            txtSoDienThoai.requestFocusInWindow();
            return null;
        }
        return HuongDanVienDTO.builder()
                .maHDV(maHDV).tenHDV(tenHDV).gioiTinh(gioiTinh)
                .namSinh(namSinh).chuyenMon(chuyenMon).soDienThoai(sdt).build();
    }

    private void hienThiLoi(String thongBao) {
        JOptionPane.showMessageDialog(this, thongBao, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private void hienThiThongBao(String thongBao) {
        JOptionPane.showMessageDialog(this, thongBao, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
}
