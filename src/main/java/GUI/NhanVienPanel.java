package GUI;

import BUS.NhanVienBUS;
import DTO.ChucVu;
import DTO.NhanVien;
import DTO.NhomQuyen;
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
import java.util.List;

/**
 * Panel Quản lý Nhân viên — CRUD đầy đủ.
 *
 * Tính năng đặc biệt:
 * - Chọn Chức vụ qua ComboBox (hiển thị tên chức vụ)
 * - Khi chọn Chức vụ, tự động tra & hiển thị Nhóm quyền tương ứng
 * - Khi Thêm mới, đồng thời tạo Tài khoản (username=maNV, pass=123123)
 * - Xóa: mềm nếu có hóa đơn, cứng nếu không
 */
public class NhanVienPanel extends JPanel {

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
    private static final String FONT = "Segoe UI";

    // =========================================================
    // NGHIỆP VỤ
    // =========================================================

    private final NhanVienBUS bus;

    /** Danh sách chức vụ đang giữ — để lấy maChucVu từ index combo */
    private List<ChucVu> danhSachChucVu = new ArrayList<>();

    /**
     * Cache toàn bộ danh sách NV đang hiển thị — để lấy diaChi, soDienThoai khi
     * click hàng
     */
    private List<NhanVien> danhSachNV = new ArrayList<>();

    /** Nhân viên đang được chọn (null = chế độ thêm mới) */
    private NhanVien nvDangChon = null;

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

    // Form
    private JTextField txtMaNV;
    private JTextField txtTenNV;
    private JComboBox<String> cboGioiTinh;
    private JTextField txtNamSinh;
    private JComboBox<String> cboChucVu; // hiển thị tenChucVu
    private JLabel lblNhomQuyen; // read-only — tự cập nhật khi đổi chức vụ
    private JCheckBox chkTrangThai;

    // Nút
    private ActionButton btnThemMoi;
    private ActionButton btnCapNhat;
    private ActionButton btnXoa;
    private ActionButton btnLamMoi;

    // =========================================================
    // KHỞI TẠO
    // =========================================================

    public NhanVienPanel() {
        this.bus = new NhanVienBUS();
        xayDungGiaoDien();
        taiDanhSachChucVu(); // Đổ combo trước
        taiDuLieu(null); // Rồi tải bảng
        resetFormThemMoi(); // Sinh mã NV tiếp theo vào form
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

        tabs.addTab("Nhân viên", panelTab);
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

        JLabel lblTieuDe = new JLabel("Quản lý Nhân viên");
        lblTieuDe.setFont(new Font(FONT, Font.BOLD, 18));
        lblTieuDe.setForeground(MAU_CHU_TOI);

        txtTimKiem = new JTextField(22);
        txtTimKiem.setFont(new Font(FONT, Font.PLAIN, 13));
        txtTimKiem.setPreferredSize(new Dimension(220, 32));
        txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MAU_VIEN, 1),
                new EmptyBorder(2, 8, 2, 8)));
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
                new String[] { "STT", "Mã NV", "Họ tên", "Giới tính", "Năm sinh", "Chức vụ", "Trạng thái" }, 0) {
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
        bangDuLieu.getColumnModel().getColumn(1).setPreferredWidth(65);
        bangDuLieu.getColumnModel().getColumn(1).setMaxWidth(75);
        bangDuLieu.getColumnModel().getColumn(2).setPreferredWidth(180);
        bangDuLieu.getColumnModel().getColumn(3).setPreferredWidth(80);
        bangDuLieu.getColumnModel().getColumn(4).setPreferredWidth(75);
        bangDuLieu.getColumnModel().getColumn(5).setPreferredWidth(160);
        bangDuLieu.getColumnModel().getColumn(6).setPreferredWidth(110);
        bangDuLieu.getColumnModel().getColumn(6).setMaxWidth(130);

        bangDuLieu.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setOpaque(true);
                setFont(new Font(FONT, Font.PLAIN, 13));
                setForeground(MAU_CHU_TOI);
                setBorder(new EmptyBorder(0, 8, 0, 8));
                if (!sel)
                    setBackground(row % 2 == 0 ? MAU_TRANG : MAU_HANG_XEN);
                setHorizontalAlignment(col == 0 || col == 4 ? CENTER : LEFT);
                if (col == 6 && v instanceof Boolean) {
                    setText((Boolean) v ? "✔ Hoạt động" : "✘ Ngừng");
                    setForeground((Boolean) v ? new Color(39, 174, 96) : new Color(192, 57, 43));
                }
                return this;
            }
        });

        apDungGradientHeader();

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

    private void apDungGradientHeader() {
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
                        Graphics2D g2 = (Graphics2D) g.create();
                        int totalWidth = table.getTableHeader().getWidth();
                        int xOffset = 0;
                        for (int i = 0; i < column; i++)
                            xOffset += table.getColumnModel().getColumn(i).getWidth();
                        g2.setPaint(new GradientPaint(-xOffset, 0, new Color(0x2980B9), totalWidth - xOffset, 0,
                                new Color(0x6DD5FA)));
                        g2.fillRect(0, 0, getWidth(), getHeight());
                        g2.dispose();
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
    }

    // ---------------------------------------------------------
    // FORM NHẬP LIỆU
    // ---------------------------------------------------------

    private JPanel xayDungFormNhap() {
        JPanel panelNgoai = new JPanel(new BorderLayout());
        panelNgoai.setPreferredSize(new Dimension(310, 0));
        panelNgoai.setBackground(MAU_TRANG);
        panelNgoai.setBorder(BorderFactory.createLineBorder(MAU_VIEN, 1));

        PaintComponent thanhGradient = new PaintComponent();
        thanhGradient.setPreferredSize(new Dimension(0, 6));

        // Bọc nội dung trong ScrollPane để form không bị cắt khi cửa sổ nhỏ
        JPanel panelNoiDung = new JPanel(new GridBagLayout());
        panelNoiDung.setBackground(MAU_TRANG);
        panelNoiDung.setBorder(new EmptyBorder(12, 14, 12, 14));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 2, 4, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        int row = 0;

        // Tiêu đề section
        JLabel lblTieuDe = new JLabel("THÔNG TIN NHÂN VIÊN");
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

        // Mã NV
        row = addLabel(panelNoiDung, gbc, row, "Mã NV *");
        txtMaNV = new JTextField();
        txtMaNV.setFont(new Font(FONT, Font.PLAIN, 13));
        txtMaNV.setEditable(false);
        txtMaNV.setBackground(new Color(240, 243, 247));
        styleInput(txtMaNV);
        gbc.gridx = 1;
        gbc.gridy = row - 1;
        panelNoiDung.add(txtMaNV, gbc);

        // Họ tên
        row = addLabel(panelNoiDung, gbc, row, "Họ tên *");
        txtTenNV = new JTextField();
        txtTenNV.setFont(new Font(FONT, Font.PLAIN, 13));
        styleInput(txtTenNV);
        gbc.gridx = 1;
        gbc.gridy = row - 1;
        panelNoiDung.add(txtTenNV, gbc);

        // Giới tính
        row = addLabel(panelNoiDung, gbc, row, "Giới tính");
        cboGioiTinh = new JComboBox<>(new String[] { "Nam", "Nữ", "Khác" });
        cboGioiTinh.setFont(new Font(FONT, Font.PLAIN, 13));
        cboGioiTinh.setPreferredSize(new Dimension(0, 32));
        gbc.gridx = 1;
        gbc.gridy = row - 1;
        panelNoiDung.add(cboGioiTinh, gbc);

        // Năm sinh
        row = addLabel(panelNoiDung, gbc, row, "Năm sinh *");
        txtNamSinh = new JTextField();
        txtNamSinh.setFont(new Font(FONT, Font.PLAIN, 13));
        styleInput(txtNamSinh);
        gbc.gridx = 1;
        gbc.gridy = row - 1;
        panelNoiDung.add(txtNamSinh, gbc);

        // Chức vụ
        row = addLabel(panelNoiDung, gbc, row, "Chức vụ *");
        cboChucVu = new JComboBox<>();
        cboChucVu.setFont(new Font(FONT, Font.PLAIN, 13));
        cboChucVu.setPreferredSize(new Dimension(0, 32));
        // Khi đổi chức vụ → tra nhóm quyền
        cboChucVu.addActionListener(e -> capNhatNhomQuyenLabel());
        gbc.gridx = 1;
        gbc.gridy = row - 1;
        panelNoiDung.add(cboChucVu, gbc);

        // Nhóm quyền (read-only) — label và value cạnh nhau tránh chồng chữ
        JLabel lblNhomQuyenText = new JLabel("Nhóm quyền");
        lblNhomQuyenText.setFont(new Font(FONT, Font.PLAIN, 12));
        lblNhomQuyenText.setForeground(MAU_CHU_PHU);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 2, 4, 4);
        panelNoiDung.add(lblNhomQuyenText, gbc);

        lblNhomQuyen = new JLabel("—");
        lblNhomQuyen.setFont(new Font(FONT, Font.BOLD, 12));
        lblNhomQuyen.setForeground(MAU_CHINH);
        lblNhomQuyen.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MAU_VIEN, 1),
                new EmptyBorder(4, 8, 4, 8)));
        lblNhomQuyen.setBackground(new Color(240, 243, 247));
        lblNhomQuyen.setOpaque(true);
        lblNhomQuyen.setPreferredSize(new Dimension(0, 32));
        gbc.gridx = 1;
        gbc.gridy = row++;
        gbc.insets = new Insets(8, 2, 4, 2);
        panelNoiDung.add(lblNhomQuyen, gbc);
        gbc.insets = new Insets(4, 2, 4, 2);

        // Trạng thái
        row = addLabel(panelNoiDung, gbc, row, "Trạng thái");
        chkTrangThai = new JCheckBox("Hoạt động");
        chkTrangThai.setFont(new Font(FONT, Font.PLAIN, 13));
        chkTrangThai.setForeground(MAU_CHU_TOI);
        chkTrangThai.setBackground(MAU_TRANG);
        chkTrangThai.setSelected(true);
        gbc.gridx = 1;
        gbc.gridy = row - 1;
        panelNoiDung.add(chkTrangThai, gbc);

        // Khoảng đệm trước nút
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(8, 2, 4, 2);
        panelNoiDung.add(new JLabel(""), gbc);
        gbc.insets = new Insets(4, 2, 4, 2);
        gbc.gridwidth = 1;

        // Bộ 4 nút
        JPanel panelNut = xayDungPanelNut();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        panelNoiDung.add(panelNut, gbc);

        panelNgoai.add(thanhGradient, BorderLayout.NORTH);
        panelNgoai.add(panelNoiDung, BorderLayout.CENTER);
        return panelNgoai;
    }

    /** Thêm dòng label (cột trái) vào form, trả về row tiếp theo. */
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
                BorderFactory.createLineBorder(MAU_VIEN, 1),
                new EmptyBorder(4, 8, 4, 8)));
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

    // =========================================================
    // TẢI DỮ LIỆU
    // =========================================================

    /** Tải danh sách chức vụ vào ComboBox — gọi 1 lần khi khởi tạo. */
    private void taiDanhSachChucVu() {
        new SwingWorker<List<ChucVu>, Void>() {
            @Override
            protected List<ChucVu> doInBackground() throws Exception {
                return bus.getDanhSachChucVu();
            }

            @Override
            protected void done() {
                try {
                    danhSachChucVu = get();
                    cboChucVu.removeAllItems();
                    for (ChucVu cv : danhSachChucVu)
                        cboChucVu.addItem(cv.getTenChucVu());
                    if (!danhSachChucVu.isEmpty())
                        capNhatNhomQuyenLabel();
                } catch (Exception ex) {
                    // Bảng CHUCVU có thể rỗng — không cần báo lỗi
                }
            }
        }.execute();
    }

    /** Tải bảng nhân viên. Chạy SwingWorker để không block EDT. */
    private void taiDuLieu(String tuKhoa) {
        lblTongSo.setText("Đang tải...");
        new SwingWorker<List<NhanVien>, Void>() {
            @Override
            protected List<NhanVien> doInBackground() throws Exception {
                return (tuKhoa == null || tuKhoa.isBlank())
                        ? bus.getAll()
                        : bus.timKiem(tuKhoa);
            }

            @Override
            protected void done() {
                try {
                    List<NhanVien> ds = get();
                    danhSachNV = ds;
                    modelBang.setRowCount(0);
                    int stt = 1;
                    for (NhanVien nv : ds) {
                        // Tìm tên chức vụ để hiển thị
                        String tenCV = timTenChucVu(nv.getMaChucVu());
                        modelBang.addRow(new Object[] {
                                stt++, nv.getMaNhanVien(), nv.getTenNhanVien(),
                                nv.getGioiTinh(), nv.getNamSinh(),
                                tenCV, nv.isTrangThai()
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

    /** Tìm tenChucVu từ maChucVu trong danh sách đã tải. */
    private String timTenChucVu(String maChucVu) {
        if (maChucVu == null)
            return "";
        for (ChucVu cv : danhSachChucVu)
            if (maChucVu.equals(cv.getMaChucVu()))
                return cv.getTenChucVu();
        return maChucVu;
    }

    /**
     * Khi ComboBox chức vụ thay đổi → tra nhóm quyền và cập nhật lblNhomQuyen.
     */
    private void capNhatNhomQuyenLabel() {
        int idx = cboChucVu.getSelectedIndex();
        if (idx < 0 || idx >= danhSachChucVu.size()) {
            lblNhomQuyen.setText("—");
            return;
        }
        String tenCV = danhSachChucVu.get(idx).getTenChucVu();
        new SwingWorker<NhomQuyen, Void>() {
            @Override
            protected NhomQuyen doInBackground() throws Exception {
                return bus.timNhomQuyenChoChucVu(tenCV);
            }

            @Override
            protected void done() {
                try {
                    NhomQuyen nq = get();
                    lblNhomQuyen.setText(nq != null ? nq.getTenNhomQuyen() : "Chưa có nhóm quyền");
                } catch (Exception ex) {
                    lblNhomQuyen.setText("—");
                }
            }
        }.execute();
    }

    // =========================================================
    // TƯƠNG TÁC BẢNG ↔ FORM
    // =========================================================

    private void dienFormTuHang(int row) {
        String maNV = (String) modelBang.getValueAt(row, 1);
        String tenNV = (String) modelBang.getValueAt(row, 2);
        String gioiTinh = (String) modelBang.getValueAt(row, 3);
        Object namSinh = modelBang.getValueAt(row, 4);
        String tenCV = (String) modelBang.getValueAt(row, 5);
        Boolean trangThai = (Boolean) modelBang.getValueAt(row, 6);

        // Tìm maChucVu từ tenChucVu
        String maCV = "";
        int idxCV = -1;
        for (int i = 0; i < danhSachChucVu.size(); i++) {
            if (danhSachChucVu.get(i).getTenChucVu().equals(tenCV)) {
                maCV = danhSachChucVu.get(i).getMaChucVu();
                idxCV = i;
                break;
            }
        }

        nvDangChon = NhanVien.builder()
                .maNhanVien(maNV).tenNhanVien(tenNV)
                .gioiTinh(gioiTinh)
                .namSinh(namSinh instanceof Integer ? (Integer) namSinh : 0)
                .maChucVu(maCV)
                .trangThai(Boolean.TRUE.equals(trangThai))
                .build();

        txtMaNV.setText(maNV);
        txtMaNV.setBackground(new Color(240, 243, 247));
        txtTenNV.setText(tenNV);
        cboGioiTinh.setSelectedItem(gioiTinh != null ? gioiTinh : "Nam");
        txtNamSinh.setText(namSinh != null ? namSinh.toString() : "");
        if (idxCV >= 0)
            cboChucVu.setSelectedIndex(idxCV);
        chkTrangThai.setSelected(Boolean.TRUE.equals(trangThai));
        // Chú ý: diaChi và soDienThoai không có trong bảng → để trống (user tự nhập khi
        // cập nhật)
    }

    private void resetFormThemMoi() {
        nvDangChon = null;
        try {
            txtMaNV.setText(bus.sinhMaMoi());
        } catch (Exception e) {
            txtMaNV.setText("NV---");
        }
        txtMaNV.setBackground(new Color(240, 243, 247));
        txtTenNV.setText("");
        cboGioiTinh.setSelectedIndex(0);
        txtNamSinh.setText("");
        if (cboChucVu.getItemCount() > 0)
            cboChucVu.setSelectedIndex(0);
        chkTrangThai.setSelected(true);
        bangDuLieu.clearSelection();
        txtTenNV.requestFocusInWindow();
    }

    // =========================================================
    // XỬ LÝ SỰ KIỆN NÚT
    // =========================================================

    private void xuLyThemMoi() {
        NhanVien nv = docDuLieuForm();
        if (nv == null)
            return;
        String tenCV = (String) cboChucVu.getSelectedItem();

        // Hỏi xác nhận trước khi tạo
        int xacNhan = JOptionPane.showConfirmDialog(
                this,
                "Bạn có muốn tạo nhân viên với mã \"" + nv.getMaNhanVien() + "\" không?\n"
                        + "Họ tên : " + nv.getTenNhanVien() + "\n"
                        + "Chức vụ: " + (tenCV != null ? tenCV : ""),
                "Xác nhận thêm nhân viên",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (xacNhan != JOptionPane.YES_OPTION)
            return;

        try {
            bus.them(nv, tenCV != null ? tenCV : "");
            hienThiThongBao("Thêm nhân viên \"" + nv.getTenNhanVien() + "\" thành công.\n"
                    + "Tài khoản: " + nv.getMaNhanVien() + " / Mật khẩu mặc định: 123123");
            taiDuLieu(null);
            resetFormThemMoi();
        } catch (IllegalArgumentException ex) {
            hienThiLoi("Dữ liệu không hợp lệ:\n" + ex.getMessage());
        } catch (Exception ex) {
            hienThiLoi("Thêm thất bại:\n" + ex.getMessage());
        }
    }

    private void xuLyCapNhat() {
        if (nvDangChon == null) {
            hienThiLoi("Vui lòng chọn một nhân viên trong bảng để cập nhật.");
            return;
        }
        NhanVien nv = docDuLieuForm();
        if (nv == null)
            return;
        String tenCV = (String) cboChucVu.getSelectedItem();
        try {
            bus.capNhat(nv, tenCV != null ? tenCV : "");
            hienThiThongBao("Cập nhật nhân viên \"" + nv.getTenNhanVien() + "\" thành công.");
            taiDuLieu(txtTimKiem.getText());
        } catch (IllegalArgumentException ex) {
            hienThiLoi("Dữ liệu không hợp lệ:\n" + ex.getMessage());
        } catch (Exception ex) {
            hienThiLoi("Cập nhật thất bại:\n" + ex.getMessage());
        }
    }

    private void xuLyXoa() {
        if (nvDangChon == null) {
            hienThiLoi("Vui lòng chọn một nhân viên trong bảng để xóa.");
            return;
        }
        int xacNhan = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa nhân viên \"" + nvDangChon.getTenNhanVien() + "\" không?\n"
                        + "(Nếu có hóa đơn liên quan, chỉ vô hiệu hóa.)",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (xacNhan != JOptionPane.YES_OPTION)
            return;
        try {
            String ketQua = bus.xoa(nvDangChon.getMaNhanVien());
            if ("XOA_MEM".equals(ketQua)) {
                hienThiThongBao("Nhân viên đang có hóa đơn liên quan.\nĐã vô hiệu hóa tài khoản thay vì xóa cứng.");
            } else {
                hienThiThongBao("Đã xóa nhân viên \"" + nvDangChon.getTenNhanVien() + "\" thành công.");
            }
            taiDuLieu(null);
            resetFormThemMoi();
        } catch (Exception ex) {
            hienThiLoi("Xóa thất bại:\n" + ex.getMessage());
        }
    }

    private void lamMoiToanBo() {
        txtTimKiem.setText("");
        taiDuLieu(null);
        resetFormThemMoi();
    }

    // =========================================================
    // ĐỌC DỮ LIỆU FORM
    // =========================================================

    private NhanVien docDuLieuForm() {
        String maNV = txtMaNV.getText().trim();
        String tenNV = txtTenNV.getText().trim();
        String gioiTinh = (String) cboGioiTinh.getSelectedItem();
        String namSinhStr = txtNamSinh.getText().trim();
        int idxCV = cboChucVu.getSelectedIndex();
        String maCV = (idxCV >= 0 && idxCV < danhSachChucVu.size())
                ? danhSachChucVu.get(idxCV).getMaChucVu()
                : "";

        if (tenNV.isEmpty()) {
            hienThiLoi("Tên nhân viên không được để trống.");
            txtTenNV.requestFocusInWindow();
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
        if (maCV.isEmpty()) {
            hienThiLoi("Vui lòng chọn chức vụ.");
            return null;
        }

        return NhanVien.builder()
                .maNhanVien(maNV)
                .tenNhanVien(tenNV)
                .gioiTinh(gioiTinh)
                .namSinh(namSinh)
                .maChucVu(maCV)
                .trangThai(chkTrangThai.isSelected())
                .build();
    }

    // =========================================================
    // TIỆN ÍCH
    // =========================================================

    private void hienThiLoi(String thongBao) {
        JOptionPane.showMessageDialog(this, thongBao, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private void hienThiThongBao(String thongBao) {
        JOptionPane.showMessageDialog(this, thongBao, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
}
