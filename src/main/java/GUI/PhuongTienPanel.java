package GUI;

import BUS.PhuongTienBUS;
import DTO.PhuongTien;
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
import java.util.List;

/**
 * Panel Quản lý Phương tiện — CRUD đầy đủ kết nối real-time với MSSQL.
 *
 * Bố cục (BorderLayout):
 * - NORTH: Thanh tìm kiếm (txtTimKiem + btnTimKiem + lblTongSo)
 * - CENTER: Chia đôi trái/phải
 * LEFT (CENTER): Bảng dữ liệu trong ScrollPaneWin11, gradient header
 * RIGHT (EAST) : Form nhập liệu (maPT, tenPT, moTa, trangThai) + 4 ActionButton
 *
 * Custom components từ dự án được sử dụng:
 * - TabbedPaneCustom : tab container đồng bộ màu sắc
 * - ScrollPaneWin11 : scroll bar kiểu Win11
 * - ActionButton : nút gradient với hover/pressed sẵn
 * - PaintComponent : thanh gradient #2980B9→#6DD5FA làm section header
 *
 * Màu sắc đồng bộ với toàn bộ ứng dụng:
 * - Màu chính : #2980B9 (topSide, sidebar)
 * - Gradient : #2980B9 → #6DD5FA
 * - Nền panel : #F6F7F8
 * - Font : Segoe UI
 */
public class PhuongTienPanel extends JPanel {

    // =========================================================
    // HẰNG SỐ MÀU SẮC & FONT — đồng bộ toàn dự án
    // =========================================================

    /** Màu chính xanh dương — khớp topSide #2980B9 */
    private static final Color MAU_CHINH = new Color(41, 128, 185);

    /** Nền panel tổng — khớp mainSide background #F6F7F8 */
    private static final Color MAU_NEN = new Color(246, 247, 248);

    /** Nền trắng cho form và bảng */
    private static final Color MAU_TRANG = Color.WHITE;

    /** Màu hàng xen kẽ (tạo sọc nhẹ cho bảng) */
    private static final Color MAU_HANG_XEN = new Color(240, 246, 252);

    /** Màu hàng đang chọn */
    private static final Color MAU_CHON = new Color(214, 234, 248);

    /** Màu chữ đậm tối */
    private static final Color MAU_CHU_TOI = new Color(44, 62, 80);

    /** Màu chữ phụ xám */
    private static final Color MAU_CHU_PHU = new Color(127, 140, 141);

    /** Màu viền nhạt */
    private static final Color MAU_VIEN = new Color(213, 219, 219);

    /** Font chữ chung toàn panel */
    private static final String FONT = "Segoe UI";

    // =========================================================
    // NGHIỆP VỤ
    // =========================================================

    /** BUS xử lý CRUD và validate phương tiện */
    private final PhuongTienBUS bus;

    /** Phương tiện đang chọn trong bảng (null khi đang ở chế độ thêm mới) */
    private PhuongTien ptDangChon = null;

    // =========================================================
    // THÀNH PHẦN GIAO DIỆN
    // =========================================================

    // --- Thanh tìm kiếm ---

    /** Ô nhập từ khoá tìm kiếm */
    private JTextField txtTimKiem;

    /** Nút thực hiện tìm kiếm */
    private ActionButton btnTimKiem;

    /** Nhãn hiển thị tổng số bản ghi đang hiển thị */
    private JLabel lblTongSo;

    // --- Bảng dữ liệu ---

    /** Bảng hiển thị danh sách phương tiện (không cho chỉnh sửa trực tiếp) */
    private JTable bangDuLieu;

    /** Model dữ liệu bảng — ghi đè isCellEditable để block edit trực tiếp */
    private DefaultTableModel modelBang;

    // --- Form nhập liệu ---

    /** Ô mã phương tiện — tự động sinh, chỉ đọc khi cập nhật */
    private JTextField txtMaPT;

    /** Ô tên phương tiện */
    private JTextField txtTenPT;

    /** Ô mô tả (đa dòng) */
    private JTextArea txtaMoTa;

    // --- Nút thao tác ---

    /** Nút Thêm mới — màu xanh dương mặc định của ActionButton */
    private ActionButton btnThemMoi;

    /** Nút Cập nhật — màu xanh lá */
    private ActionButton btnCapNhat;

    /** Nút Xóa — màu đỏ */
    private ActionButton btnXoa;

    /** Nút Làm mới (reset form + tải lại bảng) — màu xám */
    private ActionButton btnLamMoi;

    // =========================================================
    // KHỞI TẠO
    // =========================================================

    /**
     * Constructor mặc định — tự lấy kết nối DB qua ConnectionDAL.
     */
    public PhuongTienPanel() {
        this.bus = new PhuongTienBUS();
        xayDungGiaoDien();
        taiDuLieu(null); // Tải toàn bộ danh sách ban đầu
        resetFormThemMoi(); // Sinh mã PT tự động ngay khi mở panel
    }

    // =========================================================
    // XÂY DỰNG GIAO DIỆN
    // =========================================================

    /**
     * Lắp ráp toàn bộ layout của panel từ ngoài vào trong.
     * Dùng BorderLayout để responsive hoàn toàn.
     */
    private void xayDungGiaoDien() {
        setBackground(MAU_NEN);
        setLayout(new BorderLayout());

        // TabbedPaneCustom là container ngoài cùng để đồng bộ style với dự án.
        // Chỉ có 1 tab "Phương tiện" — dễ mở rộng thêm tab sau.
        TabbedPaneCustom tabs = new TabbedPaneCustom();
        tabs.setFont(new Font(FONT, Font.BOLD, 13));
        tabs.setSelectedColor(MAU_CHINH);

        // Panel chính bên trong tab
        JPanel panelTab = new JPanel(new BorderLayout(0, 8));
        panelTab.setBackground(MAU_NEN);
        panelTab.setBorder(new EmptyBorder(10, 12, 10, 12));

        // Lắp các vùng
        panelTab.add(xayDungThanhTimKiem(), BorderLayout.NORTH);
        panelTab.add(xayDungVungChinh(), BorderLayout.CENTER);

        tabs.addTab("Phương tiện", panelTab);
        add(tabs, BorderLayout.CENTER);
    }

    // ---------------------------------------------------------
    // THANH TÌM KIẾM
    // ---------------------------------------------------------

    /**
     * Vùng NORTH: ô tìm kiếm + nút "Tìm kiếm" + nhãn đếm bản ghi.
     *
     * @return Panel thanh tìm kiếm đã cấu hình
     */
    private JPanel xayDungThanhTimKiem() {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setBackground(MAU_NEN);
        panel.setBorder(new EmptyBorder(0, 0, 4, 0));

        // --- Bên trái: ô tìm kiếm + nút ---
        JPanel panelTrai = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panelTrai.setBackground(MAU_NEN);

        JLabel lblTieuDe = new JLabel("Quản lý Phương tiện");
        lblTieuDe.setFont(new Font(FONT, Font.BOLD, 18));
        lblTieuDe.setForeground(MAU_CHU_TOI);

        // Ô tìm kiếm — nhấn Enter tương đương nhấn nút Tìm kiếm
        txtTimKiem = new JTextField(22);
        txtTimKiem.setFont(new Font(FONT, Font.PLAIN, 13));
        txtTimKiem.setPreferredSize(new Dimension(220, 32));
        txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MAU_VIEN, 1),
                new EmptyBorder(2, 8, 2, 8)));
        txtTimKiem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Nhấn Enter trong ô tìm kiếm → tìm ngay
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    taiDuLieu(txtTimKiem.getText());
                }
            }
        });

        // Nút tìm kiếm — ActionButton chuẩn của dự án
        btnTimKiem = new ActionButton();
        btnTimKiem.setText("🔍  Tìm kiếm");
        btnTimKiem.setPreferredSize(new Dimension(120, 32));
        btnTimKiem.addActionListener(e -> taiDuLieu(txtTimKiem.getText()));

        panelTrai.add(lblTieuDe);
        panelTrai.add(Box.createHorizontalStrut(12));
        panelTrai.add(txtTimKiem);
        panelTrai.add(btnTimKiem);

        // --- Bên phải: nhãn tổng số bản ghi ---
        lblTongSo = new JLabel("Tổng: 0 bản ghi");
        lblTongSo.setFont(new Font(FONT, Font.ITALIC, 12));
        lblTongSo.setForeground(MAU_CHU_PHU);
        lblTongSo.setBorder(new EmptyBorder(0, 0, 0, 4));

        panel.add(panelTrai, BorderLayout.WEST);
        panel.add(lblTongSo, BorderLayout.EAST);
        return panel;
    }

    // ---------------------------------------------------------
    // VÙNG CHÍNH (bảng + form)
    // ---------------------------------------------------------

    /**
     * Vùng CENTER: bảng dữ liệu bên trái + form nhập liệu bên phải.
     * Bảng chiếm phần còn lại, form cố định 300px.
     *
     * @return Panel đã ghép hai vùng trái/phải
     */
    private JPanel xayDungVungChinh() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(MAU_NEN);

        panel.add(xayDungBang(), BorderLayout.CENTER); // Chiếm toàn bộ phần còn lại
        panel.add(xayDungFormNhap(), BorderLayout.EAST); // Cố định 300px

        return panel;
    }

    // ---------------------------------------------------------
    // BẢNG DỮ LIỆU
    // ---------------------------------------------------------

    /**
     * Xây dựng bảng dữ liệu với:
     * - Header gradient #2980B9 → #6DD5FA (giống LichTrinhPanel)
     * - Hàng xen kẽ màu trắng / xanh rất nhạt
     * - Hàng được chọn màu xanh nhạt
     * - Không cho chỉnh sửa trực tiếp trên bảng
     * - Click hàng → điền dữ liệu vào form bên phải
     *
     * @return ScrollPaneWin11 bọc bảng
     */
    private ScrollPaneWin11 xayDungBang() {
        // Model không cho chỉnh sửa cell
        modelBang = new DefaultTableModel(
                new String[] { "STT", "Mã PT", "Tên phương tiện", "Mô tả" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Bảng chỉ đọc — chỉnh sửa qua form bên phải
            }
        };

        bangDuLieu = new JTable(modelBang);
        bangDuLieu.setFont(new Font(FONT, Font.PLAIN, 13));
        bangDuLieu.setRowHeight(36); // Chiều cao hàng thoáng
        bangDuLieu.setShowGrid(false); // Bỏ lưới, dùng sọc màu thay thế
        bangDuLieu.setIntercellSpacing(new Dimension(0, 0));
        bangDuLieu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bangDuLieu.setSelectionBackground(MAU_CHON);
        bangDuLieu.setSelectionForeground(MAU_CHU_TOI);
        bangDuLieu.setFillsViewportHeight(true); // Bảng luôn lấp đầy chiều cao viewport
        bangDuLieu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Độ rộng từng cột
        bangDuLieu.getColumnModel().getColumn(0).setPreferredWidth(45); // STT
        bangDuLieu.getColumnModel().getColumn(0).setMaxWidth(50);
        bangDuLieu.getColumnModel().getColumn(1).setPreferredWidth(65); // Mã PT
        bangDuLieu.getColumnModel().getColumn(1).setMaxWidth(80);
        bangDuLieu.getColumnModel().getColumn(2).setPreferredWidth(160); // Tên
        bangDuLieu.getColumnModel().getColumn(3).setPreferredWidth(460); // Mô tả

        // Renderer xen kẽ màu hàng + căn giữa cột STT và Trạng thái
        bangDuLieu.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setOpaque(true);
                setFont(new Font(FONT, Font.PLAIN, 13));
                setForeground(MAU_CHU_TOI);
                setBorder(new EmptyBorder(0, 8, 0, 8));

                // Nền xen kẽ trắng / xanh rất nhạt
                if (!sel) {
                    setBackground(row % 2 == 0 ? MAU_TRANG : MAU_HANG_XEN);
                }

                // Căn giữa cột STT
                if (col == 0) {
                    setHorizontalAlignment(CENTER);
                } else {
                    setHorizontalAlignment(LEFT);
                }

                return this;
            }
        });

        // Header gradient — logic giống hệt LichTrinhPanel.customizeTableHeader()
        apDungGradientHeader();

        // Sự kiện click hàng → điền form
        bangDuLieu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = bangDuLieu.getSelectedRow();
                if (row >= 0) {
                    dienFormTuHang(row);
                }
            }
        });

        ScrollPaneWin11 scroll = new ScrollPaneWin11();
        scroll.setViewportView(bangDuLieu);
        scroll.setBorder(BorderFactory.createLineBorder(MAU_VIEN, 1));
        return scroll;
    }

    /**
     * Áp dụng gradient header cho bảng dữ liệu.
     * Logic gradient liền mạch giống LichTrinhPanel để đồng bộ giao diện.
     * Gradient chạy từ #2980B9 sang #6DD5FA theo chiều ngang.
     */
    private void apDungGradientHeader() {
        JTableHeader header = bangDuLieu.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 42));
        header.setFont(new Font(FONT, Font.BOLD, 13));
        header.setBorder(BorderFactory.createEmptyBorder());

        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                // JLabel tự vẽ gradient liền mạch qua toàn bộ header
                JLabel label = new JLabel(value != null ? value.toString() : "") {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        // Tính offset x tích lũy của cột này để gradient liền mạch
                        int totalWidth = table.getTableHeader().getWidth();
                        int xOffset = 0;
                        for (int i = 0; i < column; i++) {
                            xOffset += table.getColumnModel().getColumn(i).getWidth();
                        }
                        GradientPaint gp = new GradientPaint(
                                -xOffset, 0, new Color(0x2980B9),
                                totalWidth - xOffset, 0, new Color(0x6DD5FA));
                        g2.setPaint(gp);
                        g2.fillRect(0, 0, getWidth(), getHeight());
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                label.setFont(new Font(FONT, Font.BOLD, 13));
                label.setHorizontalAlignment(column == 0 ? JLabel.CENTER : JLabel.LEFT);
                label.setVerticalAlignment(JLabel.CENTER);
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

    /**
     * Xây dựng form bên phải với độ rộng cố định 300px.
     *
     * Cấu trúc form (BoxLayout Y_AXIS từ trên xuống):
     * 1. PaintComponent (6px) — thanh gradient đầu form, đồng bộ sidebar
     * 2. Label "THÔNG TIN PHƯƠNG TIỆN" — tiêu đề section
     * 3. Mã PT (txtMaPT — tự động sinh, read-only)
     * 4. Tên phương tiện (txtTenPT)
     * 5. Mô tả (txtaMoTa — đa dòng)
     * 6. Trạng thái (chkTrangThai)
     * 7. 4 ActionButton: Thêm mới, Cập nhật, Xóa, Làm mới
     *
     * @return JPanel form với chiều rộng cố định 300px
     */
    private JPanel xayDungFormNhap() {
        JPanel panelNgoai = new JPanel(new BorderLayout());
        panelNgoai.setPreferredSize(new Dimension(300, 0)); // Cố định rộng 300px
        panelNgoai.setBackground(MAU_TRANG);
        panelNgoai.setBorder(BorderFactory.createLineBorder(MAU_VIEN, 1));

        // --- Thanh gradient đầu form — dùng PaintComponent chuẩn của dự án ---
        // PaintComponent vẽ gradient #2980B9→#6DD5FA với bo góc, khớp sidebar/topbar
        PaintComponent thanhGradientDau = new PaintComponent();
        thanhGradientDau.setPreferredSize(new Dimension(0, 6));

        // --- Panel nội dung form (GridBagLayout để căn chỉnh label+field đẹp) ---
        JPanel panelNoiDung = new JPanel(new GridBagLayout());
        panelNoiDung.setBackground(MAU_TRANG);
        panelNoiDung.setBorder(new EmptyBorder(12, 14, 12, 14));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 2, 4, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        // --- Tiêu đề section ---
        JLabel lblTieuDeForm = new JLabel("THÔNG TIN PHƯƠNG TIỆN");
        lblTieuDeForm.setFont(new Font(FONT, Font.BOLD, 13));
        lblTieuDeForm.setForeground(MAU_CHINH);
        lblTieuDeForm.setBorder(new MatteBorder(0, 0, 1, 0, MAU_VIEN));
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 2, 10, 2);
        panelNoiDung.add(lblTieuDeForm, gbc);
        gbc.insets = new Insets(4, 2, 4, 2);
        gbc.gridwidth = 1;

        // --- Mã phương tiện ---
        row = themDongForm(panelNoiDung, gbc, row, "Mã PT *");
        txtMaPT = new JTextField();
        txtMaPT.setFont(new Font(FONT, Font.PLAIN, 13));
        txtMaPT.setBackground(new Color(240, 243, 247)); // Nền xám nhạt → đọc rõ read-only
        txtMaPT.setEditable(false); // Tự động sinh, không cho sửa
        txtMaPT.setToolTipText("Mã tự động sinh theo định dạng PT001, PT002...");
        aplicarEstiloInput(txtMaPT);
        gbc.gridx = 1;
        gbc.gridy = row - 1;
        panelNoiDung.add(txtMaPT, gbc);

        // --- Tên phương tiện ---
        row = themDongForm(panelNoiDung, gbc, row, "Tên PT *");
        txtTenPT = new JTextField();
        txtTenPT.setFont(new Font(FONT, Font.PLAIN, 13));
        aplicarEstiloInput(txtTenPT);
        gbc.gridx = 1;
        gbc.gridy = row - 1;
        panelNoiDung.add(txtTenPT, gbc);

        // --- Mô tả (đa dòng) ---
        row = themDongForm(panelNoiDung, gbc, row, "Mô tả");
        txtaMoTa = new JTextArea(3, 1);
        txtaMoTa.setFont(new Font(FONT, Font.PLAIN, 13));
        txtaMoTa.setLineWrap(true);
        txtaMoTa.setWrapStyleWord(true);
        txtaMoTa.setBorder(new EmptyBorder(4, 6, 4, 6));
        // Bọc JTextArea trong ScrollPaneWin11 chuẩn dự án
        ScrollPaneWin11 scrollMoTa = new ScrollPaneWin11();
        scrollMoTa.setViewportView(txtaMoTa);
        scrollMoTa.setBorder(BorderFactory.createLineBorder(MAU_VIEN, 1));
        scrollMoTa.setPreferredSize(new Dimension(0, 72));
        gbc.gridx = 1;
        gbc.gridy = row - 1;
        panelNoiDung.add(scrollMoTa, gbc);


        // --- Khoảng cách trước nút ---
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(8, 2, 4, 2);
        panelNoiDung.add(new JLabel(""), gbc);
        gbc.insets = new Insets(4, 2, 4, 2);
        gbc.gridwidth = 1;

        // --- Bộ 4 ActionButton ---
        JPanel panelNut = xayDungPanelNut();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        panelNoiDung.add(panelNut, gbc);

        panelNgoai.add(thanhGradientDau, BorderLayout.NORTH);
        panelNgoai.add(panelNoiDung, BorderLayout.CENTER);
        return panelNgoai;
    }

    /**
     * Thêm 1 dòng nhãn vào form (cột 0) và tăng row counter.
     * Trả về row mới sau khi đã thêm nhãn.
     *
     * @param panel Panel đích
     * @param gbc   GridBagConstraints dùng chung
     * @param row   Hàng hiện tại
     * @param ten   Nội dung nhãn (VD: "Mã PT *")
     * @return Row tiếp theo
     */
    private int themDongForm(JPanel panel, GridBagConstraints gbc, int row, String ten) {
        JLabel lbl = new JLabel(ten);
        lbl.setFont(new Font(FONT, Font.PLAIN, 12));
        lbl.setForeground(MAU_CHU_PHU);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(8, 2, 1, 6);
        panel.add(lbl, gbc);
        gbc.insets = new Insets(4, 2, 4, 2);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.gridwidth = 1;
        return row + 1;
    }

    /**
     * Thêm style viền và padding nhất quán cho tất cả JTextField trong form.
     *
     * @param tf JTextField cần style
     */
    private void aplicarEstiloInput(JTextField tf) {
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MAU_VIEN, 1),
                new EmptyBorder(4, 8, 4, 8)));
        tf.setPreferredSize(new Dimension(0, 32));
    }

    /**
     * Tạo panel chứa 4 ActionButton sắp xếp theo lưới 2×2.
     *
     * Màu sắc nút:
     * - Thêm mới : xanh dương mặc định ActionButton (#3498DB → #2980B9)
     * - Cập nhật : xanh lá (#27AE60 → #1E8449) — hành động tích cực
     * - Xóa : đỏ (#E74C3C → #C0392B) — hành động nguy hiểm
     * - Làm mới : xám (#7F8C8D → #636E72) — hành động trung tính
     *
     * @return Panel 2×2 chứa 4 nút
     */
    private JPanel xayDungPanelNut() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 6, 6));
        panel.setBackground(MAU_TRANG);

        // Nút Thêm mới — màu xanh dương (ActionButton default)
        btnThemMoi = new ActionButton();
        btnThemMoi.setText("＋  Thêm mới");
        // actionButton default đã là blue gradient — không cần set màu

        // Nút Cập nhật — màu xanh lá
        btnCapNhat = new ActionButton();
        btnCapNhat.setText("✎  Cập nhật");
        btnCapNhat.setColorTop(new Color(39, 174, 96));
        btnCapNhat.setColorBottom(new Color(27, 124, 66));

        // Nút Xóa — màu đỏ (cảnh báo hành động không thể hoàn tác)
        btnXoa = new ActionButton();
        btnXoa.setText("✖  Xóa");
        btnXoa.setColorTop(new Color(231, 76, 60));
        btnXoa.setColorBottom(new Color(192, 57, 43));

        // Nút Làm mới — màu xám trung tính
        btnLamMoi = new ActionButton();
        btnLamMoi.setText("⟳  Làm mới");
        btnLamMoi.setColorTop(new Color(127, 140, 141));
        btnLamMoi.setColorBottom(new Color(99, 110, 114));

        // Gắn sự kiện cho từng nút
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
    // TẢI & HIỂN THỊ DỮ LIỆU
    // =========================================================

    /**
     * Tải danh sách phương tiện từ DB và đổ vào bảng.
     * Chạy query trên SwingWorker để không block UI thread.
     *
     * @param tuKhoa Từ khóa tìm kiếm, null hoặc rỗng = lấy tất cả
     */
    private void taiDuLieu(String tuKhoa) {
        // Hiện trạng thái đang tải
        lblTongSo.setText("Đang tải...");

        new SwingWorker<List<PhuongTien>, Void>() {
            @Override
            protected List<PhuongTien> doInBackground() throws Exception {
                // Query chạy ở background thread, không block EDT
                if (tuKhoa == null || tuKhoa.isBlank()) {
                    return bus.getAll();
                }
                return bus.timKiem(tuKhoa);
            }

            @Override
            protected void done() {
                try {
                    List<PhuongTien> ds = get();
                    // Xóa dữ liệu cũ trong bảng
                    modelBang.setRowCount(0);

                    // Đổ dữ liệu vào từng hàng
                    int stt = 1;
                    for (PhuongTien pt : ds) {
                        modelBang.addRow(new Object[] {
                                stt++,
                                pt.getMaPT(),
                                pt.getTenPT(),
                                pt.getMoTa()
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

    // =========================================================
    // TƯƠNG TÁC BẢNG ↔ FORM
    // =========================================================

    /**
     * Điền dữ liệu vào form từ hàng đang chọn trong bảng.
     * Lưu đối tượng PhuongTien vào ptDangChon để dùng khi Cập nhật / Xóa.
     *
     * @param row Chỉ số hàng đang chọn (0-based)
     */
    private void dienFormTuHang(int row) {
        String maPT = (String) modelBang.getValueAt(row, 1);
        String tenPT = (String) modelBang.getValueAt(row, 2);
        String moTa = (String) modelBang.getValueAt(row, 3);

        // Lưu đối tượng hiện tại
        ptDangChon = PhuongTien.builder()
                .maPT(maPT)
                .tenPT(tenPT)
                .moTa(moTa)
                .trangThai(true)
                .build();

        // Điền form
        txtMaPT.setText(maPT);
        txtMaPT.setBackground(new Color(240, 243, 247)); // Giữ nền read-only khi edit
        txtTenPT.setText(tenPT);
        txtaMoTa.setText(moTa != null ? moTa : "");
    }

    /**
     * Reset form về trạng thái "Thêm mới":
     * - Sinh mã tự động mới
     * - Xóa các trường tenPT, moTa
     * - Đặt trạng thái = hoạt động
     * - Hủy chọn hàng trong bảng
     */
    private void resetFormThemMoi() {
        ptDangChon = null;
        try {
            txtMaPT.setText(bus.sinhMaMoi()); // Mã tự động
        } catch (Exception e) {
            txtMaPT.setText("PT---");
        }
        txtMaPT.setBackground(new Color(240, 243, 247));
        txtTenPT.setText("");
        txtaMoTa.setText("");
        bangDuLieu.clearSelection();
        txtTenPT.requestFocusInWindow(); // Focus vào ô đầu tiên cần nhập
    }

    // =========================================================
    // XỬ LÝ SỰ KIỆN NÚT
    // =========================================================

    /**
     * Xử lý Thêm mới:
     * 1. Đọc dữ liệu từ form
     * 2. Gọi bus.them() (có validate bên trong)
     * 3. Tải lại bảng sau khi thêm thành công
     */
    private void xuLyThemMoi() {
        PhuongTien pt = docDuLieuForm();
        if (pt == null)
            return; // Validate UI đã hiện lỗi
        try {
            bus.them(pt);
            hienThiThongBao("Thêm phương tiện \"" + pt.getTenPT() + "\" thành công.");
            taiDuLieu(null); // Tải lại toàn bộ bảng
            resetFormThemMoi(); // Reset form
        } catch (IllegalArgumentException ex) {
            hienThiLoi("Dữ liệu không hợp lệ:\n" + ex.getMessage());
        } catch (Exception ex) {
            hienThiLoi("Thêm thất bại:\n" + ex.getMessage());
        }
    }

    /**
     * Xử lý Cập nhật:
     * 1. Kiểm tra đã chọn hàng chưa
     * 2. Đọc dữ liệu từ form
     * 3. Gọi bus.capNhat()
     * 4. Tải lại bảng
     */
    private void xuLyCapNhat() {
        if (ptDangChon == null) {
            hienThiLoi("Vui lòng chọn một phương tiện trong bảng để cập nhật.");
            return;
        }
        PhuongTien pt = docDuLieuForm();
        if (pt == null)
            return;
        try {
            bus.capNhat(pt);
            hienThiThongBao("Cập nhật phương tiện \"" + pt.getTenPT() + "\" thành công.");
            taiDuLieu(txtTimKiem.getText()); // Tải lại dữ liệu theo từ khóa hiện tại
        } catch (IllegalArgumentException ex) {
            hienThiLoi("Dữ liệu không hợp lệ:\n" + ex.getMessage());
        } catch (Exception ex) {
            hienThiLoi("Cập nhật thất bại:\n" + ex.getMessage());
        }
    }

    /**
     * Xử lý Xóa:
     * 1. Kiểm tra đã chọn hàng chưa
     * 2. Xác nhận với người dùng (JOptionPane)
     * 3. Gọi bus.xoa() — tự xử lý xóa mềm/xóa cứng
     * 4. Hiển thị kết quả tương ứng
     */
    private void xuLyXoa() {
        if (ptDangChon == null) {
            hienThiLoi("Vui lòng chọn một phương tiện trong bảng để xóa.");
            return;
        }

        // Xác nhận trước khi xóa
        int xacNhan = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa phương tiện \"" + ptDangChon.getTenPT() + "\" không?\n"
                        + "(Nếu đang được dùng trong lịch trình, chỉ vô hiệu hóa.)",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (xacNhan != JOptionPane.YES_OPTION)
            return;

        try {
            String ketQua = bus.xoa(ptDangChon.getMaPT());
            hienThiThongBao("Đã xóa phương tiện \"" + ptDangChon.getTenPT() + "\" thành công.");
            taiDuLieu(null); // Tải lại bảng
            resetFormThemMoi(); // Reset form
        } catch (Exception ex) {
            hienThiLoi("Xóa thất bại:\n" + ex.getMessage());
        }
    }

    /**
     * Làm mới toàn bộ: reset form về Thêm mới và tải lại bảng.
     */
    private void lamMoiToanBo() {
        txtTimKiem.setText(""); // Xóa từ khóa tìm kiếm
        taiDuLieu(null); // Tải lại toàn bộ danh sách
        resetFormThemMoi(); // Reset form nhập liệu
    }

    // =========================================================
    // ĐỌC DỮ LIỆU TỪNG FORM
    // =========================================================

    /**
     * Đọc và validate cơ bản dữ liệu từ form.
     * Hiển thị lỗi ngay trên UI nếu có trường bắt buộc còn rỗng.
     *
     * @return Đối tượng PhuongTien đã điền đủ, hoặc null nếu validate thất bại
     */
    private PhuongTien docDuLieuForm() {
        String maPT = txtMaPT.getText().trim();
        String tenPT = txtTenPT.getText().trim();
        String moTa = txtaMoTa.getText().trim();

        // Validate UI tối giản — validate đầy đủ sẽ do BUS xử lý
        if (tenPT.isEmpty()) {
            hienThiLoi("Tên phương tiện không được để trống.");
            txtTenPT.requestFocusInWindow();
            return null;
        }

        return PhuongTien.builder()
                .maPT(maPT)
                .tenPT(tenPT)
                .moTa(moTa.isEmpty() ? null : moTa)
                .trangThai(true)
                .build();
    }

    // =========================================================
    // TIỆN ÍCH
    // =========================================================

    /**
     * Hiển thị hộp thoại lỗi thân thiện.
     *
     * @param thongBao Nội dung thông báo lỗi
     */
    private void hienThiLoi(String thongBao) {
        JOptionPane.showMessageDialog(this, thongBao, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Hiển thị hộp thoại thông báo thành công.
     *
     * @param thongBao Nội dung thông báo
     */
    private void hienThiThongBao(String thongBao) {
        JOptionPane.showMessageDialog(this, thongBao, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
}
