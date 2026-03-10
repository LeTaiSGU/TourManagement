
package GUI;

import BUS.CTCN_NQBUS;
import BUS.NhomQuyenBUS;
import BUS.TaiKhoanBUS;
import DTO.CTCN_NQ;
import DTO.NhomQuyen;
import DTO.TaiKhoan;
import Exception.BusException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import GUI.LoginForm.CustomTextField;
import GUI.LoginForm.CustomPasswordField;
import GUI.Menu.CustomComboBox;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

public class PhanQuyenPanel extends javax.swing.JPanel {

        private TaiKhoanBUS taiKhoanBUS = new TaiKhoanBUS();
        private NhomQuyenBUS nhomQuyenBUS = new NhomQuyenBUS();
        private CTCN_NQBUS ctcn_nqbus = new CTCN_NQBUS();
        private List<TaiKhoan> listAccCache = new ArrayList<>();
        private List<NhomQuyen> listNQ = new ArrayList<>();

        public PhanQuyenPanel() {
                initComponents();
                initGUI();
        }

        public void initGUI() {
                customTableHeader(tbTaiKhoan);
                jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                setupPanelThongTinTK();
                bindPermissionParentCheckboxes();
                resetPermissionCheckboxes();
                loadTableAcc();
                loadCbTTNhomQuyen();
                loadCbNhomQuyen();
                cbTrangThai.setModel(
                                new DefaultComboBoxModel(new String[] { "...", "Ho\u1EA1t \u0111\u1ED9ng",
                                                "\u0110\u00E3 kh\u00F3a" }));
                try {
                        listNQ = nhomQuyenBUS.getAllNhomQuyen();
                } catch (BusException ex) {
                        JOptionPane.showMessageDialog(null, "Lỗi: " + ex.getMessage());
                }

                loadTableNhomQuyen();
        }

        public void loadCbTTNhomQuyen() {
                List<NhomQuyen> listNQ = new ArrayList<>();
                try {
                        listNQ = nhomQuyenBUS.getAllNhomQuyen();
                        DefaultComboBoxModel model1 = new DefaultComboBoxModel();
                        model1.addElement("...");
                        for (NhomQuyen nq : listNQ) {
                                model1.addElement(nq.getTenNhomQuyen());
                        }
                        cbTTNhomQuyen.setModel(model1);
                } catch (BusException ex) {
                        JOptionPane.showMessageDialog(null, ex);
                }
        }

        public void loadCbNhomQuyen() {
                List<NhomQuyen> listNQ = new ArrayList<>();
                try {
                        listNQ = nhomQuyenBUS.getAllNhomQuyen();
                        DefaultComboBoxModel model2 = new DefaultComboBoxModel();
                        model2.addElement("...");
                        for (NhomQuyen nq : listNQ) {
                                model2.addElement(nq.getTenNhomQuyen());
                        }
                        cbNhomquyen.setModel(model2);
                } catch (BusException ex) {
                        JOptionPane.showMessageDialog(null, ex);
                }
        }

        public void loadTableAcc() {
                try {
                        List<TaiKhoan> listAcc = taiKhoanBUS.getAllTaiKhoan();
                        listAccCache = listAcc;
                        DefaultTableModel model = new DefaultTableModel(
                                        new Object[] { "Mã Nhân Viên", "Mật Khẩu", "Mã Quyền", "Trạng Thái" }, 0);

                        for (TaiKhoan tk : listAcc) {
                                String maskedPassword = "●".repeat(tk.getMatKhau().length());

                                model.addRow(new Object[] {
                                                tk.getMaNhanVien(),
                                                maskedPassword,
                                                tk.getMaNhomQuyen(),
                                                tk.getTrangThai() ? "Hoạt động" : "Đã khóa",
                                });
                        }
                        tbTaiKhoan.setModel(model);
                } catch (BusException e) {
                        JOptionPane.showMessageDialog(null, e);
                }
        }

        private void setupPanelThongTinTK() {
                panelThongTin.setBackground(Color.WHITE);
                panelThongTin.setLayout(new MigLayout(
                                "insets 20 30 20 30, gap 10 14",
                                "[120px, right][grow, fill][40px][120px, right][grow, fill]",
                                "[][]"));

                // Hàng 1: Mã nhân viên | Mật khẩu
                JLabel lblMaNV = makeLabel("Mã nhân viên");
                txtTTMaNV = new CustomTextField();
                txtTTMaNV.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                txtTTMaNV.setEditable(false);
                txtTTMaNV.setFocusable(false);
                txtTTMaNV.setBackgroundColor(new Color(245, 245, 245));

                JLabel lblMatKhau = makeLabel("Mật khẩu");
                pfTTMatKhau = new CustomPasswordField();
                pfTTMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 18));

                // Hàng 2: Nhóm quyền | Trạng thái
                JLabel lblNhomQuyen = makeLabel("Nhóm quyền");
                cbTTNhomQuyen = new CustomComboBox<>();
                cbTTNhomQuyen.setFont(new Font("Segoe UI", Font.PLAIN, 18));

                JLabel lblTrangThai = makeLabel("Trạng thái");
                txtTTTrangThai = new CustomTextField();
                txtTTTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                txtTTTrangThai.setEditable(false);
                txtTTTrangThai.setFocusable(false);
                txtTTTrangThai.setBackgroundColor(new Color(245, 245, 245));

                // Hàng 1
                panelThongTin.add(lblMaNV, "cell 0 0");
                panelThongTin.add(txtTTMaNV, "cell 1 0, growx");
                panelThongTin.add(lblMatKhau, "cell 3 0");
                panelThongTin.add(pfTTMatKhau, "cell 4 0, growx");

                // Hàng 2
                panelThongTin.add(lblNhomQuyen, "cell 0 1");
                panelThongTin.add(cbTTNhomQuyen, "cell 1 1, growx");
                panelThongTin.add(lblTrangThai, "cell 3 1");
                panelThongTin.add(txtTTTrangThai, "cell 4 1, growx");

                panelThongTin.revalidate();
                panelThongTin.repaint();
        }

        private JLabel makeLabel(String text) {
                JLabel lbl = new JLabel(text);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
                lbl.setForeground(new Color(60, 60, 60));
                lbl.setHorizontalAlignment(SwingConstants.RIGHT);
                return lbl;
        }

        private void customTableHeader(javax.swing.JTable table) {
                JTableHeader header = table.getTableHeader();
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

        // phan quyen

        public void loadTableNhomQuyen() {
                DefaultTableModel model = new DefaultTableModel(
                                new Object[] { "Mã Nhóm quyền", "Tên nhóm quyền", "Mô tả" }, 0);
                for (NhomQuyen nq : listNQ) {
                        model.addRow(new Object[] {
                                        nq.getMaNhomQuyen(),
                                        nq.getTenNhomQuyen(),
                                        nq.getMoTa()
                        });
                }
                customTableHeader(tbNhomQuyen);
                tbNhomQuyen.setModel(model);
        }

        private void bindPermissionParentCheckboxes() {
                bindParentCheckbox(cboxTour, cboxThemTour, cboxSuaTour, cboxXoaTour);
                bindParentCheckbox(cboxLichTrinh, cboxSuaLT, cboxXoaLT);
                bindParentCheckbox(cboxDiadiem, cboxThemDd, cboxSuaDd, cboxXoaDd);
                bindParentCheckbox(cboxLKH, cboxThemLKH, cboxSuaLKH, cboxXoaLKH);
                bindParentCheckbox(cboxKhachhang, cboxThemKH, cboxSuaKH, cboxXoaKH);
                bindParentCheckbox(cboxHDV, cboxThemHDV, cboxSuaHDV, cboxXoaHDV);
                bindParentCheckbox(cboxKhuyenmai, cboxThemKm, cboxSuaKm, cboxXoaKm);
                bindParentCheckbox(cboxNhanvien, cboxThemNv, cboxSuaNv, cboxXoaNv);
                bindParentCheckbox(cboxPT, cboxThemPT, cboxSuaPT, cboxXoaPT);
                bindParentCheckbox(cboxTaikhoan, cboxSuaTk, cboxKhoaTk);
                bindParentCheckbox(cboxPhanQuyen, cboxSuaQ);
                bindParentCheckbox(cboxHoadon, cboxDatve, cboxTTHuy);
                bindParentCheckbox(cboxThongke);
        }

        private void bindParentCheckbox(javax.swing.JCheckBox parent, javax.swing.JCheckBox... children) {
                if (parent == null) {
                        return;
                }
                parent.addActionListener(e -> setParentWithChildrenState(parent, parent.isSelected(), children));
        }

        private void setParentWithChildrenState(javax.swing.JCheckBox parent, boolean checked,
                        javax.swing.JCheckBox... children) {
                if (parent != null) {
                        parent.setSelected(checked);
                }
                if (children == null) {
                        return;
                }
                for (javax.swing.JCheckBox child : children) {
                        if (child == null) {
                                continue;
                        }
                        child.setEnabled(checked);
                        if (!checked) {
                                child.setSelected(false);
                        }
                }
        }

        private void resetPermissionCheckboxes() {
                setParentWithChildrenState(cboxTour, false, cboxThemTour, cboxSuaTour, cboxXoaTour);
                setParentWithChildrenState(cboxLichTrinh, false, cboxSuaLT, cboxXoaLT);
                setParentWithChildrenState(cboxDiadiem, false, cboxThemDd, cboxSuaDd, cboxXoaDd);
                setParentWithChildrenState(cboxLKH, false, cboxThemLKH, cboxSuaLKH, cboxXoaLKH);
                setParentWithChildrenState(cboxKhachhang, false, cboxThemKH, cboxSuaKH, cboxXoaKH);
                setParentWithChildrenState(cboxHDV, false, cboxThemHDV, cboxSuaHDV, cboxXoaHDV);
                setParentWithChildrenState(cboxKhuyenmai, false, cboxThemKm, cboxSuaKm, cboxXoaKm);
                setParentWithChildrenState(cboxNhanvien, false, cboxThemNv, cboxSuaNv, cboxXoaNv);
                setParentWithChildrenState(cboxPT, false, cboxThemPT, cboxSuaPT, cboxXoaPT);
                setParentWithChildrenState(cboxTaikhoan, false, cboxSuaTk, cboxKhoaTk);
                setParentWithChildrenState(cboxPhanQuyen, false, cboxSuaQ);
                setParentWithChildrenState(cboxHoadon, false, cboxDatve, cboxTTHuy);
                setParentWithChildrenState(cboxThongke, false);
        }

        private String normalizeText(String input) {
                if (input == null) {
                        return "";
                }
                String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
                                .replaceAll("\\p{M}+", "")
                                .toLowerCase()
                                .trim();
                return normalized;
        }

        private void applyActionDetail(String chiTiet, javax.swing.JCheckBox them, javax.swing.JCheckBox sua,
                        javax.swing.JCheckBox xoa, javax.swing.JCheckBox khoaMo, javax.swing.JCheckBox thanhToanHuy) {
                String detail = normalizeText(chiTiet);
                if (detail.isEmpty()) {
                        return;
                }

                if (them != null && detail.contains("them")) {
                        them.setSelected(true);
                }
                if (sua != null && detail.contains("sua")) {
                        sua.setSelected(true);
                }
                if (xoa != null && detail.contains("xoa")) {
                        xoa.setSelected(true);
                }
                if (khoaMo != null && (detail.contains("khoa") || detail.contains("mo khoa"))) {
                        khoaMo.setSelected(true);
                }
                if (thanhToanHuy != null && (detail.contains("thanh toan") || detail.contains("huy"))) {
                        thanhToanHuy.setSelected(true);
                }
                if (detail.contains("dat ve")) {
                        cboxDatve.setSelected(true);
                }
        }

        private void applyPermissionCheckbox(CTCN_NQ ct) {
                if (ct == null || ct.getMaCN() == null) {
                        return;
                }
                String maCN = ct.getMaCN().trim().toUpperCase();
                String chiTiet = ct.getChiTiet();

                switch (maCN) {
                        case "CN001":
                                setParentWithChildrenState(cboxTour, true, cboxThemTour, cboxSuaTour, cboxXoaTour);
                                applyActionDetail(chiTiet, cboxThemTour, cboxSuaTour, cboxXoaTour, null, null);
                                break;
                        case "CN002":
                                setParentWithChildrenState(cboxLichTrinh, true, cboxSuaLT, cboxXoaLT);
                                applyActionDetail(chiTiet, null, cboxSuaLT, cboxXoaLT, null, null);
                                break;
                        case "CN003":
                                setParentWithChildrenState(cboxDiadiem, true, cboxThemDd, cboxSuaDd, cboxXoaDd);
                                applyActionDetail(chiTiet, cboxThemDd, cboxSuaDd, cboxXoaDd, null, null);
                                break;
                        case "CN004":
                                setParentWithChildrenState(cboxKhachhang, true, cboxThemKH, cboxSuaKH, cboxXoaKH);
                                applyActionDetail(chiTiet, cboxThemKH, cboxSuaKH, cboxXoaKH, null, null);
                                break;
                        case "CN005":
                                setParentWithChildrenState(cboxLKH, true, cboxThemLKH, cboxSuaLKH, cboxXoaLKH);
                                applyActionDetail(chiTiet, cboxThemLKH, cboxSuaLKH, cboxXoaLKH, null, null);
                                break;
                        case "CN006":
                                setParentWithChildrenState(cboxHDV, true, cboxThemHDV, cboxSuaHDV, cboxXoaHDV);
                                applyActionDetail(chiTiet, cboxThemHDV, cboxSuaHDV, cboxXoaHDV, null, null);
                                break;
                        case "CN007":
                                setParentWithChildrenState(cboxKhuyenmai, true, cboxThemKm, cboxSuaKm, cboxXoaKm);
                                applyActionDetail(chiTiet, cboxThemKm, cboxSuaKm, cboxXoaKm, null, null);
                                break;
                        case "CN008":
                                setParentWithChildrenState(cboxNhanvien, true, cboxThemNv, cboxSuaNv, cboxXoaNv);
                                applyActionDetail(chiTiet, cboxThemNv, cboxSuaNv, cboxXoaNv, null, null);
                                break;
                        case "CN009":
                                setParentWithChildrenState(cboxPT, true, cboxThemPT, cboxSuaPT, cboxXoaPT);
                                applyActionDetail(chiTiet, cboxThemPT, cboxSuaPT, cboxXoaPT, null, null);
                                break;
                        case "CN010":
                                setParentWithChildrenState(cboxPhanQuyen, true, cboxSuaQ);
                                applyActionDetail(chiTiet, null, cboxSuaQ, null, null, null);
                                break;
                        case "CN011": {
                                setParentWithChildrenState(cboxHoadon, true, cboxDatve, cboxTTHuy);
                                String chiTietHoaDon = normalizeText(chiTiet);
                                if (chiTietHoaDon.contains("dat ve")) {
                                        cboxDatve.setSelected(true);
                                }
                                if (chiTietHoaDon.contains("thanh toan") || chiTietHoaDon.contains("huy")) {
                                        cboxTTHuy.setSelected(true);
                                }
                                break;
                        }
                        case "CN012":
                                setParentWithChildrenState(cboxTaikhoan, true, cboxSuaTk, cboxKhoaTk);
                                applyActionDetail(chiTiet, null, cboxSuaTk, null, cboxKhoaTk, null);
                                break;
                        case "CN013":
                                setParentWithChildrenState(cboxThongke, true);
                                break;
                        default:
                                // Một số chức năng không có trên panel (ví dụ CN009) thì bỏ qua an toàn.
                                break;
                }
        }

        private String buildChiTietString(String... actions) {
                StringBuilder sb = new StringBuilder();
                for (String action : actions) {
                        if (action == null || action.trim().isEmpty()) {
                                continue;
                        }
                        if (sb.length() > 0) {
                                sb.append(" ");
                        }
                        sb.append(action.trim());
                }
                return sb.length() == 0 ? null : sb.toString();
        }

        private List<CTCN_NQ> buildSelectedCTCNNQ(String maNhomQuyen) {
                List<CTCN_NQ> selected = new ArrayList<>();
                if (maNhomQuyen == null || maNhomQuyen.trim().isEmpty()) {
                        return selected;
                }
                String maNQ = maNhomQuyen.trim();

                if (cboxTour.isSelected()) {
                        String chiTiet = buildChiTietString(
                                        cboxThemTour.isSelected() ? "Thêm" : null,
                                        cboxSuaTour.isSelected() ? "Sửa" : null,
                                        cboxXoaTour.isSelected() ? "Xóa" : null);
                        selected.add(CTCN_NQ.builder().maNhomQuyen(maNQ).maCN("CN001").chiTiet(chiTiet).build());
                }
                if (cboxLichTrinh.isSelected()) {
                        String chiTiet = buildChiTietString(
                                        cboxSuaLT.isSelected() ? "Sửa" : null,
                                        cboxXoaLT.isSelected() ? "Xóa" : null);
                        selected.add(CTCN_NQ.builder().maNhomQuyen(maNQ).maCN("CN002").chiTiet(chiTiet).build());
                }
                if (cboxDiadiem.isSelected()) {
                        String chiTiet = buildChiTietString(
                                        cboxThemDd.isSelected() ? "Thêm" : null,
                                        cboxSuaDd.isSelected() ? "Sửa" : null,
                                        cboxXoaDd.isSelected() ? "Xóa" : null);
                        selected.add(CTCN_NQ.builder().maNhomQuyen(maNQ).maCN("CN003").chiTiet(chiTiet).build());
                }
                if (cboxKhachhang.isSelected()) {
                        String chiTiet = buildChiTietString(
                                        cboxThemKH.isSelected() ? "Thêm" : null,
                                        cboxSuaKH.isSelected() ? "Sửa" : null,
                                        cboxXoaKH.isSelected() ? "Xóa" : null);
                        selected.add(CTCN_NQ.builder().maNhomQuyen(maNQ).maCN("CN004").chiTiet(chiTiet).build());
                }
                // CN005 không còn trong bảng CHUCNANG hiện tại nên không lưu quyền này
                // để tránh lỗi FK khi insert CTCNNQ.
                if (cboxHDV.isSelected()) {
                        String chiTiet = buildChiTietString(
                                        cboxThemHDV.isSelected() ? "Thêm" : null,
                                        cboxSuaHDV.isSelected() ? "Sửa" : null,
                                        cboxXoaHDV.isSelected() ? "Xóa" : null);
                        selected.add(CTCN_NQ.builder().maNhomQuyen(maNQ).maCN("CN006").chiTiet(chiTiet).build());
                }
                if (cboxKhuyenmai.isSelected()) {
                        String chiTiet = buildChiTietString(
                                        cboxThemKm.isSelected() ? "Thêm" : null,
                                        cboxSuaKm.isSelected() ? "Sửa" : null,
                                        cboxXoaKm.isSelected() ? "Xóa" : null);
                        selected.add(CTCN_NQ.builder().maNhomQuyen(maNQ).maCN("CN007").chiTiet(chiTiet).build());
                }
                if (cboxNhanvien.isSelected()) {
                        String chiTiet = buildChiTietString(
                                        cboxThemNv.isSelected() ? "Thêm" : null,
                                        cboxSuaNv.isSelected() ? "Sửa" : null,
                                        cboxXoaNv.isSelected() ? "Xóa" : null);
                        selected.add(CTCN_NQ.builder().maNhomQuyen(maNQ).maCN("CN008").chiTiet(chiTiet).build());
                }
                if (cboxPT.isSelected()) {
                        String chiTiet = buildChiTietString(
                                        cboxThemPT.isSelected() ? "Thêm" : null,
                                        cboxSuaPT.isSelected() ? "Sửa" : null,
                                        cboxXoaPT.isSelected() ? "Xóa" : null);
                        selected.add(CTCN_NQ.builder().maNhomQuyen(maNQ).maCN("CN009").chiTiet(chiTiet).build());
                }
                if (cboxTaikhoan.isSelected()) {
                        String chiTiet = buildChiTietString(
                                        cboxSuaTk.isSelected() ? "Sửa" : null,
                                        cboxKhoaTk.isSelected() ? "Khóa/Mở khóa" : null);
                        selected.add(CTCN_NQ.builder().maNhomQuyen(maNQ).maCN("CN012").chiTiet(chiTiet).build());
                }
                if (cboxPhanQuyen.isSelected()) {
                        String chiTiet = buildChiTietString(cboxSuaQ.isSelected() ? "Sửa" : null);
                        selected.add(CTCN_NQ.builder().maNhomQuyen(maNQ).maCN("CN010").chiTiet(chiTiet).build());
                }
                if (cboxHoadon.isSelected()) {
                        String chiTietHoaDon = buildChiTietString(
                                        cboxDatve.isSelected() ? "Đặt vé" : null,
                                        cboxTTHuy.isSelected() ? "Thanh toán + hủy" : null);
                        selected.add(CTCN_NQ.builder()
                                        .maNhomQuyen(maNQ)
                                        .maCN("CN011")
                                        .chiTiet(chiTietHoaDon)
                                        .build());
                }
                if (cboxThongke.isSelected()) {
                        selected.add(CTCN_NQ.builder().maNhomQuyen(maNQ).maCN("CN013").chiTiet(null).build());
                }
                return selected;
        }

        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated
        // Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                tabbedPaneCustom1 = new GUI.Menu.TabbedPaneCustom();
                accPanel = new javax.swing.JPanel();
                jPanel3 = new javax.swing.JPanel();
                jPanel5 = new javax.swing.JPanel();
                jLabel1 = new javax.swing.JLabel();
                panel2 = new javax.swing.JPanel();
                jLabel2 = new javax.swing.JLabel();
                jLabel3 = new javax.swing.JLabel();
                cbNhomquyen = new GUI.Menu.CustomComboBox();
                jLabel4 = new javax.swing.JLabel();
                txtManhanvien1 = new GUI.LoginForm.CustomTextField();
                cbTrangThai = new GUI.Menu.CustomComboBox();
                btnReloadSearch = new GUI.Menu.ActionButton();
                btnTimkiem = new GUI.Menu.ActionButton();
                jPanel4 = new javax.swing.JPanel();
                jPanel6 = new javax.swing.JPanel();
                jLabel5 = new javax.swing.JLabel();
                jPanel8 = new javax.swing.JPanel();
                panelThongTin = new javax.swing.JPanel();
                jScrollPane2 = new javax.swing.JScrollPane();
                tbTaiKhoan = new javax.swing.JTable();
                jPanel7 = new javax.swing.JPanel();
                btnChinhSua = new GUI.Menu.ActionButton();
                btnMokhoa = new GUI.Menu.ActionButton();
                btnReload = new GUI.Menu.ActionButton();
                phanquyenPanel = new javax.swing.JPanel();
                jPanel1 = new javax.swing.JPanel();
                jPanel2 = new javax.swing.JPanel();
                jLabel6 = new javax.swing.JLabel();
                btnSuaNhomQuyen = new GUI.Menu.ActionButton();
                jLabel7 = new javax.swing.JLabel();
                txtTenNhomQuyen = new GUI.LoginForm.CustomTextField();
                jScrollPane1 = new javax.swing.JScrollPane();
                tbNhomQuyen = new javax.swing.JTable();
                jPanel9 = new javax.swing.JPanel();
                jPanel14 = new javax.swing.JPanel();
                jPanel11 = new javax.swing.JPanel();
                cboxTour = new javax.swing.JCheckBox();
                cboxThemTour = new javax.swing.JCheckBox();
                cboxSuaTour = new javax.swing.JCheckBox();
                cboxXoaTour = new javax.swing.JCheckBox();
                jPanel13 = new javax.swing.JPanel();
                cboxLichTrinh = new javax.swing.JCheckBox();
                cboxSuaLT = new javax.swing.JCheckBox();
                cboxXoaLT = new javax.swing.JCheckBox();
                jPanel12 = new javax.swing.JPanel();
                cboxDiadiem = new javax.swing.JCheckBox();
                cboxThemDd = new javax.swing.JCheckBox();
                cboxSuaDd = new javax.swing.JCheckBox();
                cboxXoaDd = new javax.swing.JCheckBox();
                jPanel16 = new javax.swing.JPanel();
                cboxLKH = new javax.swing.JCheckBox();
                cboxThemLKH = new javax.swing.JCheckBox();
                cboxSuaLKH = new javax.swing.JCheckBox();
                cboxXoaLKH = new javax.swing.JCheckBox();
                jPanel15 = new javax.swing.JPanel();
                cboxKhachhang = new javax.swing.JCheckBox();
                cboxThemKH = new javax.swing.JCheckBox();
                cboxSuaKH = new javax.swing.JCheckBox();
                cboxXoaKH = new javax.swing.JCheckBox();
                jPanel17 = new javax.swing.JPanel();
                cboxHDV = new javax.swing.JCheckBox();
                cboxThemHDV = new javax.swing.JCheckBox();
                cboxSuaHDV = new javax.swing.JCheckBox();
                cboxXoaHDV = new javax.swing.JCheckBox();
                jPanel18 = new javax.swing.JPanel();
                cboxKhuyenmai = new javax.swing.JCheckBox();
                cboxThemKm = new javax.swing.JCheckBox();
                cboxSuaKm = new javax.swing.JCheckBox();
                cboxXoaKm = new javax.swing.JCheckBox();
                jPanel19 = new javax.swing.JPanel();
                cboxNhanvien = new javax.swing.JCheckBox();
                cboxThemNv = new javax.swing.JCheckBox();
                cboxSuaNv = new javax.swing.JCheckBox();
                cboxXoaNv = new javax.swing.JCheckBox();
                jPanel20 = new javax.swing.JPanel();
                cboxTaikhoan = new javax.swing.JCheckBox();
                cboxSuaTk = new javax.swing.JCheckBox();
                cboxKhoaTk = new javax.swing.JCheckBox();
                jPanel21 = new javax.swing.JPanel();
                cboxPhanQuyen = new javax.swing.JCheckBox();
                cboxSuaQ = new javax.swing.JCheckBox();
                jPanel22 = new javax.swing.JPanel();
                cboxDatve = new javax.swing.JCheckBox();
                cboxTTHuy = new javax.swing.JCheckBox();
                cboxHoadon = new javax.swing.JCheckBox();
                jPanel23 = new javax.swing.JPanel();
                cboxThongke = new javax.swing.JCheckBox();
                jPanel24 = new javax.swing.JPanel();
                cboxPT = new javax.swing.JCheckBox();
                cboxThemPT = new javax.swing.JCheckBox();
                cboxSuaPT = new javax.swing.JCheckBox();
                cboxXoaPT = new javax.swing.JCheckBox();

                setPreferredSize(new java.awt.Dimension(1320, 950));
                setLayout(new java.awt.BorderLayout());

                tabbedPaneCustom1.setBackground(new java.awt.Color(255, 255, 255));
                tabbedPaneCustom1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
                tabbedPaneCustom1.setSelectedColor(new java.awt.Color(51, 153, 255));

                accPanel.setBackground(new java.awt.Color(255, 255, 255));
                accPanel.setLayout(new java.awt.BorderLayout());

                jPanel3.setBackground(new java.awt.Color(255, 255, 255));
                jPanel3.setPreferredSize(new java.awt.Dimension(300, 913));
                jPanel3.setLayout(new java.awt.BorderLayout());

                jPanel5.setBackground(new java.awt.Color(255, 255, 255));

                jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
                jLabel1.setText("Tìm kiếm");

                javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
                jPanel5.setLayout(jPanel5Layout);
                jPanel5Layout.setHorizontalGroup(
                                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addGap(97, 97, 97)
                                                                .addComponent(jLabel1)
                                                                .addContainerGap(98, Short.MAX_VALUE)));
                jPanel5Layout.setVerticalGroup(
                                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addGap(39, 39, 39)
                                                                .addComponent(jLabel1)
                                                                .addContainerGap(45, Short.MAX_VALUE)));

                jPanel3.add(jPanel5, java.awt.BorderLayout.PAGE_START);

                panel2.setBackground(new java.awt.Color(255, 255, 255));

                jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
                jLabel2.setText("Mã nhân viên");

                jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
                jLabel3.setText("Nhóm quyền");

                cbNhomquyen.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

                jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
                jLabel4.setText("Trạng thái");

                txtManhanvien1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                txtManhanvien1.setIconGap(1);

                cbTrangThai.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Hoạt động", "Đã khóa" }));
                cbTrangThai.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

                btnReloadSearch.setBorder(null);
                btnReloadSearch.setForeground(new java.awt.Color(0, 0, 0));
                btnReloadSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/reload.png"))); // NOI18N
                btnReloadSearch.setText("Làm mới");
                btnReloadSearch.setColorBottom(new java.awt.Color(204, 204, 204));
                btnReloadSearch.setColorTop(new java.awt.Color(204, 204, 204));
                btnReloadSearch.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
                btnReloadSearch.addActionListener(this::btnReloadSearchActionPerformed);

                btnTimkiem.setBorder(null);
                btnTimkiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/search2.png"))); // NOI18N
                btnTimkiem.setText("Tìm kiếm");
                btnTimkiem.setColorBottom(new java.awt.Color(0, 0, 0));
                btnTimkiem.setColorTop(new java.awt.Color(0, 0, 0));
                btnTimkiem.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
                btnTimkiem.addActionListener(this::btnTimkiemActionPerformed);

                javax.swing.GroupLayout panel2Layout = new javax.swing.GroupLayout(panel2);
                panel2.setLayout(panel2Layout);
                panel2Layout.setHorizontalGroup(
                                panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(panel2Layout.createSequentialGroup()
                                                                .addGap(22, 22, 22)
                                                                .addGroup(panel2Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                .addComponent(btnTimkiem,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                250,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(btnReloadSearch,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                250,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(cbTrangThai,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                250,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGroup(panel2Layout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                .addComponent(txtManhanvien1,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addComponent(cbNhomquyen,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGroup(panel2Layout
                                                                                                                .createSequentialGroup()
                                                                                                                .addGap(12, 12, 12)
                                                                                                                .addGroup(panel2Layout
                                                                                                                                .createParallelGroup(
                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                .addComponent(jLabel2)
                                                                                                                                .addComponent(jLabel3)
                                                                                                                                .addComponent(jLabel4)))))
                                                                .addContainerGap(28, Short.MAX_VALUE)));
                panel2Layout.setVerticalGroup(
                                panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(panel2Layout.createSequentialGroup()
                                                                .addComponent(jLabel2)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(txtManhanvien1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jLabel3)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cbNhomquyen,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jLabel4)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(cbTrangThai,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(btnTimkiem,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(btnReloadSearch,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 442, Short.MAX_VALUE)));

                jPanel3.add(panel2, java.awt.BorderLayout.LINE_END);

                accPanel.add(jPanel3, java.awt.BorderLayout.LINE_START);

                jPanel4.setBackground(new java.awt.Color(255, 255, 255));
                jPanel4.setLayout(new java.awt.BorderLayout());

                jPanel6.setBackground(new java.awt.Color(255, 255, 255));
                jPanel6.setLayout(new java.awt.BorderLayout());

                jLabel5.setBackground(new java.awt.Color(255, 255, 255));
                jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
                jLabel5.setText("Danh sách tài khoản");
                jLabel5.setPreferredSize(new java.awt.Dimension(230, 80));
                jPanel6.add(jLabel5, java.awt.BorderLayout.CENTER);

                jPanel4.add(jPanel6, java.awt.BorderLayout.PAGE_START);

                jPanel8.setPreferredSize(new java.awt.Dimension(1015, 500));
                jPanel8.setLayout(new java.awt.BorderLayout());

                javax.swing.GroupLayout panelThongTinLayout = new javax.swing.GroupLayout(panelThongTin);
                panelThongTin.setLayout(panelThongTinLayout);
                panelThongTinLayout.setHorizontalGroup(
                                panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 1097, Short.MAX_VALUE));
                panelThongTinLayout.setVerticalGroup(
                                panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 100, Short.MAX_VALUE));

                jPanel8.add(panelThongTin, java.awt.BorderLayout.PAGE_START);

                tbTaiKhoan.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                tbTaiKhoan.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {
                                                { "", "", "", "" },
                                                { "", "", "", null },
                                                { null, null, null, null },
                                                { null, null, null, null }
                                },
                                new String[] {
                                                "Mã Nhân Viên", "Mật khẩu", "Mã Quyền", "Trạng Thái"
                                }));
                tbTaiKhoan.setRowHeight(25);
                tbTaiKhoan.setShowGrid(false);
                tbTaiKhoan.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                tbTaiKhoanMouseClicked(evt);
                        }
                });
                jScrollPane2.setViewportView(tbTaiKhoan);

                jPanel8.add(jScrollPane2, java.awt.BorderLayout.CENTER);

                jPanel4.add(jPanel8, java.awt.BorderLayout.CENTER);

                jPanel7.setBackground(new java.awt.Color(255, 255, 255));
                jPanel7.setPreferredSize(new java.awt.Dimension(1015, 300));

                btnChinhSua.setBorder(null);
                btnChinhSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/save.png"))); // NOI18N
                btnChinhSua.setText("Chỉnh sửa");
                btnChinhSua.setColorBottom(new java.awt.Color(38, 198, 218));
                btnChinhSua.setColorTop(new java.awt.Color(0, 131, 143));
                btnChinhSua.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
                btnChinhSua.setIconGap(10);
                btnChinhSua.setIconTextGap(5);
                btnChinhSua.setRadius(50);
                btnChinhSua.addActionListener(this::btnChinhSuaActionPerformed);

                btnMokhoa.setBorder(null);
                btnMokhoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/padlock.png"))); // NOI18N
                btnMokhoa.setText("Mở khóa");
                btnMokhoa.setToolTipText("");
                btnMokhoa.setColorBottom(new java.awt.Color(239, 83, 80));
                btnMokhoa.setColorTop(new java.awt.Color(198, 40, 40));
                btnMokhoa.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
                btnMokhoa.setIconGap(10);
                btnMokhoa.setIconTextGap(8);
                btnMokhoa.setRadius(50);
                btnMokhoa.addActionListener(this::btnMokhoaActionPerformed);

                btnReload.setBorder(null);
                btnReload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/reload2.png"))); // NOI18N
                btnReload.setText("Làm mới");
                btnReload.setColorBottom(new java.awt.Color(189, 189, 189));
                btnReload.setColorTop(new java.awt.Color(117, 117, 117));
                btnReload.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
                btnReload.setRadius(50);
                btnReload.addActionListener(this::btnReloadActionPerformed);

                javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
                jPanel7.setLayout(jPanel7Layout);
                jPanel7Layout.setHorizontalGroup(
                                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel7Layout.createSequentialGroup()
                                                                .addGap(44, 44, 44)
                                                                .addComponent(btnChinhSua,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                192,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(30, 30, 30)
                                                                .addComponent(btnMokhoa,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                192,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(30, 30, 30)
                                                                .addComponent(btnReload,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                192,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(417, Short.MAX_VALUE)));
                jPanel7Layout.setVerticalGroup(
                                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel7Layout.createSequentialGroup()
                                                                .addGap(34, 34, 34)
                                                                .addGroup(jPanel7Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(btnChinhSua,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                56,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(btnMokhoa,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                56,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(btnReload,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                56,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addContainerGap(210, Short.MAX_VALUE)));

                jPanel4.add(jPanel7, java.awt.BorderLayout.PAGE_END);

                accPanel.add(jPanel4, java.awt.BorderLayout.CENTER);

                tabbedPaneCustom1.addTab("Tài khoản", accPanel);

                phanquyenPanel.setBackground(new java.awt.Color(255, 255, 255));

                jPanel1.setBackground(new java.awt.Color(255, 153, 153));
                jPanel1.setPreferredSize(new java.awt.Dimension(500, 913));
                jPanel1.setLayout(new java.awt.BorderLayout());

                jPanel2.setBackground(new java.awt.Color(255, 255, 255));
                jPanel2.setPreferredSize(new java.awt.Dimension(400, 200));

                jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

                btnSuaNhomQuyen.setBorder(null);
                btnSuaNhomQuyen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/save.png"))); // NOI18N
                btnSuaNhomQuyen.setText("Sửa");
                btnSuaNhomQuyen.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
                btnSuaNhomQuyen.addActionListener(this::btnSuaNhomQuyenActionPerformed);

                jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                jLabel7.setText("Tên nhóm quyền");

                txtTenNhomQuyen.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

                javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
                jPanel2.setLayout(jPanel2Layout);
                jPanel2Layout.setHorizontalGroup(
                                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addGroup(jPanel2Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel2Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(18, 18, 18)
                                                                                                .addGroup(jPanel2Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                                .addComponent(jLabel7)
                                                                                                                .addGroup(jPanel2Layout
                                                                                                                                .createSequentialGroup()
                                                                                                                                .addComponent(jLabel6)
                                                                                                                                .addGap(108, 108,
                                                                                                                                                108)))
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(txtTenNhomQuyen,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                336,
                                                                                                                Short.MAX_VALUE))
                                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                jPanel2Layout.createSequentialGroup()
                                                                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                                                                .addComponent(btnSuaNhomQuyen,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addContainerGap()));
                jPanel2Layout.setVerticalGroup(
                                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addGap(46, 46, 46)
                                                                .addComponent(jLabel6)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(jPanel2Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel7)
                                                                                .addComponent(txtTenNhomQuyen,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18)
                                                                .addComponent(btnSuaNhomQuyen,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(39, Short.MAX_VALUE)));

                jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

                tbNhomQuyen.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                tbNhomQuyen.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {
                                                { null, null, null },
                                                { null, null, null },
                                                { null, null, null },
                                                { null, null, null }
                                },
                                new String[] {
                                                "Mã Nhóm Quyền", "Tên Nhóm Quyền", "Mô tả"
                                }));
                tbNhomQuyen.setRowHeight(25);
                tbNhomQuyen.setShowGrid(false);
                tbNhomQuyen.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                tbNhomQuyenMouseClicked(evt);
                        }
                });
                jScrollPane1.setViewportView(tbNhomQuyen);

                jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

                jPanel9.setBackground(new java.awt.Color(255, 255, 255));

                javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
                jPanel9.setLayout(jPanel9Layout);
                jPanel9Layout.setHorizontalGroup(
                                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 500, Short.MAX_VALUE));
                jPanel9Layout.setVerticalGroup(
                                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 100, Short.MAX_VALUE));

                jPanel1.add(jPanel9, java.awt.BorderLayout.PAGE_END);

                jPanel14.setBackground(new java.awt.Color(255, 255, 255));
                jPanel14.setLayout(new java.awt.GridLayout(4, 3));

                jPanel11.setBackground(new java.awt.Color(255, 255, 255));
                jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(
                                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Tour",
                                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                                new java.awt.Font("Segoe UI", 0, 18))); // NOI18N

                cboxTour.setBackground(new java.awt.Color(255, 255, 255));
                cboxTour.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxTour.setText("Tour");

                cboxThemTour.setBackground(new java.awt.Color(255, 255, 255));
                cboxThemTour.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxThemTour.setText("Thêm");
                cboxThemTour.setEnabled(false);
                cboxThemTour.addActionListener(this::cboxThemTourActionPerformed);

                cboxSuaTour.setBackground(new java.awt.Color(255, 255, 255));
                cboxSuaTour.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxSuaTour.setText("Sửa");
                cboxSuaTour.setEnabled(false);

                cboxXoaTour.setBackground(new java.awt.Color(255, 255, 255));
                cboxXoaTour.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxXoaTour.setText("Xóa");
                cboxXoaTour.setEnabled(false);

                javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
                jPanel11.setLayout(jPanel11Layout);
                jPanel11Layout.setHorizontalGroup(
                                jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel11Layout.createSequentialGroup()
                                                                .addGap(27, 27, 27)
                                                                .addGroup(jPanel11Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel11Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(21, 21, 21)
                                                                                                .addComponent(cboxThemTour,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                92,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addComponent(cboxTour,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                92,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(cboxSuaTour,
                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                92,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(cboxXoaTour,
                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                92,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addContainerGap(56, Short.MAX_VALUE)));
                jPanel11Layout.setVerticalGroup(
                                jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel11Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(cboxTour)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxThemTour)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxSuaTour)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxXoaTour)
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));

                jPanel14.add(jPanel11);

                jPanel13.setBackground(new java.awt.Color(255, 255, 255));
                jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(
                                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Lịch Trình",
                                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                                new java.awt.Font("Segoe UI", 0, 18))); // NOI18N

                cboxLichTrinh.setBackground(new java.awt.Color(255, 255, 255));
                cboxLichTrinh.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxLichTrinh.setText("Lịch trình");

                cboxSuaLT.setBackground(new java.awt.Color(255, 255, 255));
                cboxSuaLT.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxSuaLT.setText("Sửa");
                cboxSuaLT.setEnabled(false);
                cboxSuaLT.addActionListener(this::cboxSuaLTActionPerformed);

                cboxXoaLT.setBackground(new java.awt.Color(255, 255, 255));
                cboxXoaLT.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxXoaLT.setText("Xóa");
                cboxXoaLT.setEnabled(false);

                javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
                jPanel13.setLayout(jPanel13Layout);
                jPanel13Layout.setHorizontalGroup(
                                jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel13Layout.createSequentialGroup()
                                                                .addGap(27, 27, 27)
                                                                .addGroup(jPanel13Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel13Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(21, 21, 21)
                                                                                                .addComponent(cboxSuaLT,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                92,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addComponent(cboxLichTrinh,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                92,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(cboxXoaLT,
                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                92,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addContainerGap(56, Short.MAX_VALUE)));
                jPanel13Layout.setVerticalGroup(
                                jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel13Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(cboxLichTrinh)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxSuaLT)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxXoaLT)
                                                                .addContainerGap(69, Short.MAX_VALUE)));

                jPanel14.add(jPanel13);

                jPanel12.setBackground(new java.awt.Color(255, 255, 255));
                jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(
                                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Địa điểm",
                                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                                new java.awt.Font("Segoe UI", 0, 18))); // NOI18N

                cboxDiadiem.setBackground(new java.awt.Color(255, 255, 255));
                cboxDiadiem.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxDiadiem.setText("Địa điểm");

                cboxThemDd.setBackground(new java.awt.Color(255, 255, 255));
                cboxThemDd.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxThemDd.setText("Thêm");
                cboxThemDd.setEnabled(false);
                cboxThemDd.addActionListener(this::cboxThemDdActionPerformed);

                cboxSuaDd.setBackground(new java.awt.Color(255, 255, 255));
                cboxSuaDd.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxSuaDd.setText("Sửa");
                cboxSuaDd.setEnabled(false);

                cboxXoaDd.setBackground(new java.awt.Color(255, 255, 255));
                cboxXoaDd.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxXoaDd.setText("Xóa");
                cboxXoaDd.setEnabled(false);
                cboxXoaDd.addActionListener(this::cboxXoaDdActionPerformed);

                javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
                jPanel12.setLayout(jPanel12Layout);
                jPanel12Layout.setHorizontalGroup(
                                jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel12Layout.createSequentialGroup()
                                                                .addGap(27, 27, 27)
                                                                .addGroup(jPanel12Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                .addComponent(cboxXoaDd,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                92,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGroup(jPanel12Layout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                .addGroup(jPanel12Layout
                                                                                                                .createSequentialGroup()
                                                                                                                .addGap(21, 21, 21)
                                                                                                                .addComponent(cboxThemDd,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                92,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                .addComponent(cboxDiadiem,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                92,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addComponent(cboxSuaDd,
                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                92,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addContainerGap(56, Short.MAX_VALUE)));
                jPanel12Layout.setVerticalGroup(
                                jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel12Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(cboxDiadiem)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxThemDd)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxSuaDd)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxXoaDd)
                                                                .addContainerGap(34, Short.MAX_VALUE)));

                jPanel14.add(jPanel12);

                jPanel16.setBackground(new java.awt.Color(255, 255, 255));
                jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(
                                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)),
                                "Loại khách hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                                new java.awt.Font("Segoe UI", 0, 18))); // NOI18N

                cboxLKH.setBackground(new java.awt.Color(255, 255, 255));
                cboxLKH.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxLKH.setText("Loại khách hàng");

                cboxThemLKH.setBackground(new java.awt.Color(255, 255, 255));
                cboxThemLKH.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxThemLKH.setText("Thêm");
                cboxThemLKH.setEnabled(false);
                cboxThemLKH.addActionListener(this::cboxThemLKHActionPerformed);

                cboxSuaLKH.setBackground(new java.awt.Color(255, 255, 255));
                cboxSuaLKH.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxSuaLKH.setText("Sửa");
                cboxSuaLKH.setEnabled(false);

                cboxXoaLKH.setBackground(new java.awt.Color(255, 255, 255));
                cboxXoaLKH.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxXoaLKH.setText("Xóa");
                cboxXoaLKH.setEnabled(false);

                javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
                jPanel16.setLayout(jPanel16Layout);
                jPanel16Layout.setHorizontalGroup(
                                jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout
                                                                .createSequentialGroup()
                                                                .addContainerGap(24, Short.MAX_VALUE)
                                                                .addGroup(jPanel16Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel16Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGroup(jPanel16Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                                .addGroup(jPanel16Layout
                                                                                                                                .createParallelGroup(
                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                .addGroup(jPanel16Layout
                                                                                                                                                .createSequentialGroup()
                                                                                                                                                .addGap(48, 48, 48)
                                                                                                                                                .addComponent(cboxThemLKH,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                92,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                                                .addComponent(cboxSuaLKH,
                                                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                92,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                                .addGroup(jPanel16Layout
                                                                                                                                .createSequentialGroup()
                                                                                                                                .addGap(27, 27, 27)
                                                                                                                                .addComponent(cboxXoaLKH,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                92,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                                                11,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addComponent(cboxLKH,
                                                                                                javax.swing.GroupLayout.Alignment.TRAILING))
                                                                .addGap(21, 21, 21)));
                jPanel16Layout.setVerticalGroup(
                                jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel16Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(cboxLKH)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxThemLKH)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxSuaLKH)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxXoaLKH)
                                                                .addContainerGap(34, Short.MAX_VALUE)));

                jPanel14.add(jPanel16);

                jPanel15.setBackground(new java.awt.Color(255, 255, 255));
                jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(
                                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Khách hàng",
                                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                                new java.awt.Font("Segoe UI", 0, 18))); // NOI18N

                cboxKhachhang.setBackground(new java.awt.Color(255, 255, 255));
                cboxKhachhang.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxKhachhang.setText("Khách hàng");

                cboxThemKH.setBackground(new java.awt.Color(255, 255, 255));
                cboxThemKH.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxThemKH.setText("Thêm");
                cboxThemKH.setEnabled(false);
                cboxThemKH.addActionListener(this::cboxThemKHActionPerformed);

                cboxSuaKH.setBackground(new java.awt.Color(255, 255, 255));
                cboxSuaKH.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxSuaKH.setText("Sửa");
                cboxSuaKH.setEnabled(false);

                cboxXoaKH.setBackground(new java.awt.Color(255, 255, 255));
                cboxXoaKH.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxXoaKH.setText("Xóa");
                cboxXoaKH.setEnabled(false);

                javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
                jPanel15.setLayout(jPanel15Layout);
                jPanel15Layout.setHorizontalGroup(
                                jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel15Layout.createSequentialGroup()
                                                                .addGroup(jPanel15Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                .addGroup(jPanel15Layout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                .addGroup(jPanel15Layout
                                                                                                                .createSequentialGroup()
                                                                                                                .addGap(48, 48, 48)
                                                                                                                .addComponent(cboxThemKH,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                92,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                .addComponent(cboxSuaKH,
                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                92,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGroup(jPanel15Layout
                                                                                                .createSequentialGroup()
                                                                                                .addContainerGap()
                                                                                                .addGroup(jPanel15Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                                .addComponent(cboxXoaKH,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                92,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(cboxKhachhang,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                113,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                                .addContainerGap(56, Short.MAX_VALUE)));
                jPanel15Layout.setVerticalGroup(
                                jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel15Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(cboxKhachhang)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxThemKH)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxSuaKH)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxXoaKH)
                                                                .addContainerGap(34, Short.MAX_VALUE)));

                jPanel14.add(jPanel15);

                jPanel17.setBackground(new java.awt.Color(255, 255, 255));
                jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(
                                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)),
                                "Hướng dẫn viên", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                                new java.awt.Font("Segoe UI", 0, 18))); // NOI18N

                cboxHDV.setBackground(new java.awt.Color(255, 255, 255));
                cboxHDV.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxHDV.setText("Hướng dẫn viên");

                cboxThemHDV.setBackground(new java.awt.Color(255, 255, 255));
                cboxThemHDV.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxThemHDV.setText("Thêm");
                cboxThemHDV.setEnabled(false);
                cboxThemHDV.addActionListener(this::cboxThemHDVActionPerformed);

                cboxSuaHDV.setBackground(new java.awt.Color(255, 255, 255));
                cboxSuaHDV.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxSuaHDV.setText("Sửa");
                cboxSuaHDV.setEnabled(false);

                cboxXoaHDV.setBackground(new java.awt.Color(255, 255, 255));
                cboxXoaHDV.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxXoaHDV.setText("Xóa");
                cboxXoaHDV.setEnabled(false);

                javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
                jPanel17.setLayout(jPanel17Layout);
                jPanel17Layout.setHorizontalGroup(
                                jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel17Layout.createSequentialGroup()
                                                                .addGroup(jPanel17Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                .addGroup(jPanel17Layout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                .addGroup(jPanel17Layout
                                                                                                                .createSequentialGroup()
                                                                                                                .addGap(48, 48, 48)
                                                                                                                .addComponent(cboxThemHDV,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                92,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                .addComponent(cboxSuaHDV,
                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                92,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGroup(jPanel17Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(27, 27, 27)
                                                                                                .addComponent(cboxXoaHDV,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                92,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                jPanel17Layout.createSequentialGroup()
                                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                                .addComponent(cboxHDV)
                                                                                .addContainerGap(45, Short.MAX_VALUE)));
                jPanel17Layout.setVerticalGroup(
                                jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel17Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(cboxHDV)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxThemHDV)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxSuaHDV)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxXoaHDV)
                                                                .addContainerGap(34, Short.MAX_VALUE)));

                jPanel14.add(jPanel17);

                jPanel18.setBackground(new java.awt.Color(255, 255, 255));
                jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(
                                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Khuyến mãi",
                                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                                new java.awt.Font("Segoe UI", 0, 18))); // NOI18N

                cboxKhuyenmai.setBackground(new java.awt.Color(255, 255, 255));
                cboxKhuyenmai.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxKhuyenmai.setText("Khuyến mãi");

                cboxThemKm.setBackground(new java.awt.Color(255, 255, 255));
                cboxThemKm.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxThemKm.setText("Thêm");
                cboxThemKm.setEnabled(false);
                cboxThemKm.addActionListener(this::cboxThemKmActionPerformed);

                cboxSuaKm.setBackground(new java.awt.Color(255, 255, 255));
                cboxSuaKm.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxSuaKm.setText("Sửa");
                cboxSuaKm.setEnabled(false);

                cboxXoaKm.setBackground(new java.awt.Color(255, 255, 255));
                cboxXoaKm.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxXoaKm.setText("Xóa");
                cboxXoaKm.setEnabled(false);

                javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
                jPanel18.setLayout(jPanel18Layout);
                jPanel18Layout.setHorizontalGroup(
                                jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel18Layout.createSequentialGroup()
                                                                .addGroup(jPanel18Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                .addGroup(jPanel18Layout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                .addGroup(jPanel18Layout
                                                                                                                .createSequentialGroup()
                                                                                                                .addGap(48, 48, 48)
                                                                                                                .addComponent(cboxThemKm,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                92,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                .addComponent(cboxSuaKm,
                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                92,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGroup(jPanel18Layout
                                                                                                .createSequentialGroup()
                                                                                                .addContainerGap()
                                                                                                .addGroup(jPanel18Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                                .addComponent(cboxXoaKm,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                92,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(cboxKhuyenmai))))
                                                                .addContainerGap(56, Short.MAX_VALUE)));
                jPanel18Layout.setVerticalGroup(
                                jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel18Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(cboxKhuyenmai)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxThemKm)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxSuaKm)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxXoaKm)
                                                                .addContainerGap(34, Short.MAX_VALUE)));

                jPanel14.add(jPanel18);

                jPanel19.setBackground(new java.awt.Color(255, 255, 255));
                jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder(
                                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Nhân viên",
                                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                                new java.awt.Font("Segoe UI", 0, 18))); // NOI18N

                cboxNhanvien.setBackground(new java.awt.Color(255, 255, 255));
                cboxNhanvien.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxNhanvien.setText("Nhân viên");

                cboxThemNv.setBackground(new java.awt.Color(255, 255, 255));
                cboxThemNv.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxThemNv.setText("Thêm");
                cboxThemNv.setEnabled(false);
                cboxThemNv.addActionListener(this::cboxThemNvActionPerformed);

                cboxSuaNv.setBackground(new java.awt.Color(255, 255, 255));
                cboxSuaNv.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxSuaNv.setText("Sửa");
                cboxSuaNv.setEnabled(false);

                cboxXoaNv.setBackground(new java.awt.Color(255, 255, 255));
                cboxXoaNv.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxXoaNv.setText("Xóa");
                cboxXoaNv.setEnabled(false);

                javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
                jPanel19.setLayout(jPanel19Layout);
                jPanel19Layout.setHorizontalGroup(
                                jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel19Layout.createSequentialGroup()
                                                                .addGroup(jPanel19Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel19Layout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                .addGroup(jPanel19Layout
                                                                                                                .createSequentialGroup()
                                                                                                                .addGap(48, 48, 48)
                                                                                                                .addComponent(cboxThemNv,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                92,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                .addComponent(cboxSuaNv,
                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                92,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addComponent(cboxXoaNv,
                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                92,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGroup(jPanel19Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(22, 22, 22)
                                                                                                .addComponent(cboxNhanvien)))
                                                                .addContainerGap(56, Short.MAX_VALUE)));
                jPanel19Layout.setVerticalGroup(
                                jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel19Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(cboxNhanvien)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxThemNv)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxSuaNv)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxXoaNv)
                                                                .addContainerGap(34, Short.MAX_VALUE)));

                jPanel14.add(jPanel19);

                jPanel20.setBackground(new java.awt.Color(255, 255, 255));
                jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder(
                                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Tài khoản",
                                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                                new java.awt.Font("Segoe UI", 0, 18))); // NOI18N

                cboxTaikhoan.setBackground(new java.awt.Color(255, 255, 255));
                cboxTaikhoan.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxTaikhoan.setText("Tài khoản");
                cboxTaikhoan.addActionListener(this::cboxTaikhoanActionPerformed);

                cboxSuaTk.setBackground(new java.awt.Color(255, 255, 255));
                cboxSuaTk.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxSuaTk.setText("Sửa");
                cboxSuaTk.setEnabled(false);
                cboxSuaTk.addActionListener(this::cboxSuaTkActionPerformed);

                cboxKhoaTk.setBackground(new java.awt.Color(255, 255, 255));
                cboxKhoaTk.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxKhoaTk.setText("Khóa/Mở khóa");
                cboxKhoaTk.setEnabled(false);

                javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
                jPanel20.setLayout(jPanel20Layout);
                jPanel20Layout.setHorizontalGroup(
                                jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel20Layout.createSequentialGroup()
                                                                .addGroup(jPanel20Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel20Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGroup(jPanel20Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addGroup(jPanel20Layout
                                                                                                                                .createSequentialGroup()
                                                                                                                                .addGap(48, 48, 48)
                                                                                                                                .addComponent(cboxSuaTk,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                92,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                                .addGroup(jPanel20Layout
                                                                                                                                .createSequentialGroup()
                                                                                                                                .addGap(29, 29, 29)
                                                                                                                                .addComponent(cboxTaikhoan)))
                                                                                                .addGap(0, 50, Short.MAX_VALUE))
                                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                jPanel20Layout.createSequentialGroup()
                                                                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                                                                .addComponent(cboxKhoaTk)))
                                                                .addContainerGap()));
                jPanel20Layout.setVerticalGroup(
                                jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel20Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(cboxTaikhoan)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxSuaTk)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxKhoaTk)
                                                                .addContainerGap(69, Short.MAX_VALUE)));

                jPanel14.add(jPanel20);

                jPanel21.setBackground(new java.awt.Color(255, 255, 255));
                jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder(
                                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Khuyến mãi",
                                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                                new java.awt.Font("Segoe UI", 0, 18))); // NOI18N

                cboxPhanQuyen.setBackground(new java.awt.Color(255, 255, 255));
                cboxPhanQuyen.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxPhanQuyen.setText("Phân quyền");

                cboxSuaQ.setBackground(new java.awt.Color(255, 255, 255));
                cboxSuaQ.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxSuaQ.setText("Sửa");
                cboxSuaQ.setEnabled(false);
                cboxSuaQ.addActionListener(this::cboxSuaQActionPerformed);

                javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
                jPanel21.setLayout(jPanel21Layout);
                jPanel21Layout.setHorizontalGroup(
                                jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel21Layout.createSequentialGroup()
                                                                .addGap(24, 24, 24)
                                                                .addGroup(jPanel21Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                .addComponent(cboxPhanQuyen)
                                                                                .addComponent(cboxSuaQ,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                92,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addContainerGap(56, Short.MAX_VALUE)));
                jPanel21Layout.setVerticalGroup(
                                jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel21Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(cboxPhanQuyen)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxSuaQ)
                                                                .addContainerGap(104, Short.MAX_VALUE)));

                jPanel14.add(jPanel21);

                jPanel22.setBackground(new java.awt.Color(255, 255, 255));
                jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder(
                                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Hóa đơn",
                                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                                new java.awt.Font("Segoe UI", 0, 18))); // NOI18N

                cboxDatve.setBackground(new java.awt.Color(255, 255, 255));
                cboxDatve.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxDatve.setText("Đặt vé");
                cboxDatve.addActionListener(this::cboxDatveActionPerformed);

                cboxTTHuy.setBackground(new java.awt.Color(255, 255, 255));
                cboxTTHuy.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxTTHuy.setText("Thanh toán + hủy");
                cboxTTHuy.setEnabled(false);
                cboxTTHuy.addActionListener(this::cboxTTHuyActionPerformed);

                cboxHoadon.setBackground(new java.awt.Color(255, 255, 255));
                cboxHoadon.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxHoadon.setText("Hóa đơn");

                javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
                jPanel22.setLayout(jPanel22Layout);
                jPanel22Layout.setHorizontalGroup(
                                jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel22Layout.createSequentialGroup()
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addGroup(jPanel22Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(cboxHoadon)
                                                                                .addGroup(jPanel22Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(21, 21, 21)
                                                                                                .addGroup(jPanel22Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(cboxTTHuy)
                                                                                                                .addComponent(cboxDatve))))
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));
                jPanel22Layout.setVerticalGroup(
                                jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel22Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(cboxHoadon)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxDatve)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxTTHuy)
                                                                .addContainerGap(69, Short.MAX_VALUE)));

                jPanel14.add(jPanel22);

                jPanel23.setBackground(new java.awt.Color(255, 255, 255));
                jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder(
                                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Thống kê",
                                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                                new java.awt.Font("Segoe UI", 0, 18))); // NOI18N

                cboxThongke.setBackground(new java.awt.Color(255, 255, 255));
                cboxThongke.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxThongke.setText("Thống kê");

                javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
                jPanel23.setLayout(jPanel23Layout);
                jPanel23Layout.setHorizontalGroup(
                                jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel23Layout.createSequentialGroup()
                                                                .addGap(20, 20, 20)
                                                                .addComponent(cboxThongke)
                                                                .addContainerGap(79, Short.MAX_VALUE)));
                jPanel23Layout.setVerticalGroup(
                                jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel23Layout.createSequentialGroup()
                                                                .addComponent(cboxThongke)
                                                                .addGap(0, 145, Short.MAX_VALUE)));

                jPanel14.add(jPanel23);

                jPanel24.setBackground(new java.awt.Color(255, 255, 255));
                jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder(
                                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Phương tiện",
                                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                                new java.awt.Font("Segoe UI", 0, 18))); // NOI18N

                cboxPT.setBackground(new java.awt.Color(255, 255, 255));
                cboxPT.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxPT.setText("Phương tiện");

                cboxThemPT.setBackground(new java.awt.Color(255, 255, 255));
                cboxThemPT.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxThemPT.setText("Thêm");
                cboxThemPT.setEnabled(false);
                cboxThemPT.addActionListener(this::cboxThemPTActionPerformed);

                cboxSuaPT.setBackground(new java.awt.Color(255, 255, 255));
                cboxSuaPT.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxSuaPT.setText("Sửa");
                cboxSuaPT.setEnabled(false);

                cboxXoaPT.setBackground(new java.awt.Color(255, 255, 255));
                cboxXoaPT.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                cboxXoaPT.setText("Xóa");
                cboxXoaPT.setEnabled(false);

                javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
                jPanel24.setLayout(jPanel24Layout);
                jPanel24Layout.setHorizontalGroup(
                                jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel24Layout.createSequentialGroup()
                                                                .addGroup(jPanel24Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                .addGroup(jPanel24Layout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                .addGroup(jPanel24Layout
                                                                                                                .createSequentialGroup()
                                                                                                                .addGap(48, 48, 48)
                                                                                                                .addComponent(cboxThemPT,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                92,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                .addComponent(cboxSuaPT,
                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                92,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGroup(jPanel24Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(27, 27, 27)
                                                                                                .addComponent(cboxXoaPT,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                92,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addContainerGap(56, Short.MAX_VALUE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout
                                                                .createSequentialGroup()
                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                .addComponent(cboxPT,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                131,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));
                jPanel24Layout.setVerticalGroup(
                                jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel24Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(cboxPT)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxThemPT)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxSuaPT)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cboxXoaPT)
                                                                .addContainerGap(34, Short.MAX_VALUE)));

                jPanel14.add(jPanel24);

                javax.swing.GroupLayout phanquyenPanelLayout = new javax.swing.GroupLayout(phanquyenPanel);
                phanquyenPanel.setLayout(phanquyenPanelLayout);
                phanquyenPanelLayout.setHorizontalGroup(
                                phanquyenPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(phanquyenPanelLayout.createSequentialGroup()
                                                                .addComponent(jPanel1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jPanel14,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addGap(61, 61, 61)));
                phanquyenPanelLayout.setVerticalGroup(
                                phanquyenPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(phanquyenPanelLayout.createSequentialGroup()
                                                                .addComponent(jPanel1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                909,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 41, Short.MAX_VALUE))
                                                .addGroup(phanquyenPanelLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(jPanel14,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                824,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));

                tabbedPaneCustom1.addTab("Phân quyền", phanquyenPanel);

                add(tabbedPaneCustom1, java.awt.BorderLayout.PAGE_START);
        }// </editor-fold>//GEN-END:initComponents

        private void cboxXoaDdActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cboxXoaDdActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_cboxXoaDdActionPerformed

        private void cboxThemPTActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cboxThemPTActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_cboxThemPTActionPerformed

        private void cboxDatveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cboxDatveActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_cboxDatveActionPerformed

        private void cboxTTHuyActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cboxTTHuyActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_cboxTTHuyActionPerformed

        private void cboxThemTourActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cboxThemTourActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_cboxThemTourActionPerformed

        private void cboxThemDdActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cboxThemDdActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_cboxThemDdActionPerformed

        private void cboxSuaLTActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cboxSuaLTActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_cboxSuaLTActionPerformed

        private void cboxThemKHActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cboxThemKHActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_cboxThemKHActionPerformed

        private void cboxThemLKHActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cboxThemLKHActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_cboxThemLKHActionPerformed

        private void cboxThemHDVActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cboxThemHDVActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_cboxThemHDVActionPerformed

        private void cboxThemKmActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cboxThemKmActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_cboxThemKmActionPerformed

        private void cboxThemNvActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cboxThemNvActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_cboxThemNvActionPerformed

        private void cboxSuaTkActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cboxSuaTkActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_cboxSuaTkActionPerformed

        private void cboxTaikhoanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cboxTaikhoanActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_cboxTaikhoanActionPerformed

        private void cboxSuaQActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cboxSuaQActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_cboxSuaQActionPerformed

        private void cboxXoaHDV5ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cboxXoaHDV5ActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_cboxXoaHDV5ActionPerformed

        private void tbNhomQuyenMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tbNhomQuyenMouseClicked
                int viewRow = tbNhomQuyen.getSelectedRow();
                if (viewRow == -1)
                        return;
                try {
                        resetPermissionCheckboxes();
                        int modelRow = tbNhomQuyen.convertRowIndexToModel(viewRow);
                        String maNhomQuyen = tbNhomQuyen.getModel().getValueAt(modelRow, 0).toString();
                        String tenNhomQuyen = tbNhomQuyen.getModel().getValueAt(modelRow, 1).toString();
                        txtTenNhomQuyen.setText(tenNhomQuyen);

                        List<CTCN_NQ> listNQCN = ctcn_nqbus.getCTCN_NQbyMa(maNhomQuyen);
                        if (listNQCN == null || listNQCN.isEmpty())
                                return;
                        for (CTCN_NQ ct : listNQCN) {
                                applyPermissionCheckbox(ct);
                        }
                } catch (BusException ex) {
                        JOptionPane.showMessageDialog(null, ex);
                }
        }// GEN-LAST:event_tbNhomQuyenMouseClicked

        private void btnSuaNhomQuyenActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSuaNhomQuyenActionPerformed
                int viewRow = tbNhomQuyen.getSelectedRow();
                if (viewRow == -1) {
                        JOptionPane.showMessageDialog(this, "Vui lòng chọn nhóm quyền để kiểm tra.");
                        return;
                }
                int modelRow = tbNhomQuyen.convertRowIndexToModel(viewRow);
                String maNhomQuyen = tbNhomQuyen.getModel().getValueAt(modelRow, 0).toString();

                List<CTCN_NQ> selected = buildSelectedCTCNNQ(maNhomQuyen);
                if (selected.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Hiện chưa có chức năng nào được chọn.");
                        return;
                }
                try {
                        ctcn_nqbus.dropCTCNNQ(maNhomQuyen);
                } catch (BusException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage());
                }
                for (CTCN_NQ ct : selected) {
                        try {
                                ctcn_nqbus.insertCTCNNQ(maNhomQuyen, ct.getMaCN(), ct.getChiTiet());
                        } catch (BusException e) {
                                JOptionPane.showMessageDialog(null, e.getMessage());
                                return;
                        }
                }
                JOptionPane.showMessageDialog(this, "Cập nhật thành công");
        }// GEN-LAST:event_btnSuaNhomQuyenActionPerformed

        private void btnTimkiemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTimkiemActionPerformed
                try {
                        List<NhomQuyen> listNQ = nhomQuyenBUS.getAllNhomQuyen();
                        String maNhanVien = txtManhanvien1.getText();
                        String maNhomQuyen = null;
                        for (NhomQuyen nq : listNQ) {
                                if (nq.getTenNhomQuyen().equals(cbNhomquyen.getSelectedItem().toString())) {
                                        maNhomQuyen = nq.getMaNhomQuyen();
                                        break;
                                }
                        }

                        Boolean trangThai = null;
                        int trangThaiIdx = cbTrangThai.getSelectedIndex();
                        if (trangThaiIdx == 1)
                                trangThai = true;
                        else if (trangThaiIdx == 2)
                                trangThai = false;

                        List<TaiKhoan> list = taiKhoanBUS.searchTaiKhoan(maNhanVien, maNhomQuyen, trangThai);
                        listAccCache = list;

                        DefaultTableModel model = new DefaultTableModel(
                                        new Object[] { "Mã Nhân Viên", "Mật Khẩu", "Mã Quyền", "Trạng Thái" }, 0);
                        for (TaiKhoan tk : list) {
                                model.addRow(new Object[] {
                                                tk.getMaNhanVien(),
                                                "●".repeat(tk.getMatKhau().length()),
                                                tk.getMaNhomQuyen(),
                                                tk.getTrangThai() ? "Hoạt động" : "Đã khóa"
                                });
                        }
                        tbTaiKhoan.setModel(model);
                        customTableHeader(tbTaiKhoan);
                } catch (BusException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage());
                }
        }// GEN-LAST:event_btnTimkiemActionPerformed

        private void btnReloadActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnReloadActionPerformed
                reloadAllTT();
        }// GEN-LAST:event_btnReloadActionPerformed

        public void reloadAllTT() {
                txtTTMaNV.setText("");
                txtTTTrangThai.setText("");
                pfTTMatKhau.setText("");
                cbTTNhomQuyen.setSelectedItem("...");
        }

        private void btnReloadSearchActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnReloadSearchActionPerformed
                txtManhanvien1.setText("");
                cbNhomquyen.setSelectedItem("...");
                cbTrangThai.setSelectedItem("Hoạt động");
        }// GEN-LAST:event_btnReloadSearchActionPerformed

        private void btnMokhoaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnMokhoaActionPerformed
                if (txtTTMaNV.getText().equals(""))
                        JOptionPane.showMessageDialog(null, "Vui lòng chọn tài khoản để chỉnh sửa");
                boolean trangThai = txtTTTrangThai.getText()
                                .equalsIgnoreCase("Hoạt Động");

                // JOptionPane.showMessageDialog(null, tk.getMaNhanVien() + " " +
                // tk.getTrangThai());
                try {
                        TaiKhoan tk = TaiKhoan.builder()
                                        .maNhanVien(txtTTMaNV.getText())
                                        .trangThai(trangThai)
                                        .build();
                        if (taiKhoanBUS.changeStatus(tk)) {
                                if (tk.getTrangThai()) {
                                        JOptionPane.showMessageDialog(null, "Mở khóa tài khoản thành công");
                                        loadTableAcc();
                                        reloadAllTT();
                                } else {
                                        JOptionPane.showMessageDialog(null, "Khóa tài khoản thành công");
                                        loadTableAcc();
                                        reloadAllTT();
                                }
                        }

                } catch (BusException ex) {
                        JOptionPane.showMessageDialog(null, ex);
                }

        }// GEN-LAST:event_btnMokhoaActionPerformed

        private void tbTaiKhoanMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tbTaiKhoanMouseClicked
                int row = tbTaiKhoan.getSelectedRow();
                if (row == -1 || row >= listAccCache.size())
                        return;
                try {
                        TaiKhoan tk = listAccCache.get(row);
                        NhomQuyen nq = nhomQuyenBUS.getQuyenByMa(tk.getMaNhomQuyen());

                        if (tk.getTrangThai()) {
                                btnMokhoa.setText("Khóa");
                        } else {
                                btnMokhoa.setText("Mở khóa");
                        }

                        txtTTMaNV.setText(tk.getMaNhanVien());
                        pfTTMatKhau.setText(tk.getMatKhau());
                        cbTTNhomQuyen.setSelectedItem(nq.getTenNhomQuyen());
                        txtTTTrangThai.setText(tbTaiKhoan.getValueAt(row, 3).toString());
                } catch (BusException ex) {
                        JOptionPane.showMessageDialog(null, ex);
                }

        }// GEN-LAST:event_tbTaiKhoanMouseClicked

        private void btnChinhSuaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnChinhSuaActionPerformed
                if (txtTTMaNV.getText().equals(""))
                        JOptionPane.showMessageDialog(null, "Vui lòng chọn tài khoản để chỉnh sửa");
                boolean trangThai = txtTTTrangThai.getText()
                                .equalsIgnoreCase("Hoạt Động");

                try {
                        List<NhomQuyen> listNQ = nhomQuyenBUS.getAllNhomQuyen();
                        String tenNhomQuyenSelected = cbTTNhomQuyen.getSelectedItem().toString();
                        String maNhomQuyen = null;

                        // Tìm maNhomQuyen theo tên
                        for (NhomQuyen nq : listNQ) {
                                if (nq.getTenNhomQuyen().equals(tenNhomQuyenSelected)) {
                                        maNhomQuyen = nq.getMaNhomQuyen();
                                        break;
                                }
                        }

                        if (maNhomQuyen == null) {
                                JOptionPane.showMessageDialog(null, "Không tìm thấy mã nhóm quyền!");
                                return;
                        }

                        TaiKhoan tk = TaiKhoan.builder()
                                        .maNhanVien(txtTTMaNV.getText())
                                        .matKhau(pfTTMatKhau.getText())
                                        .maNhomQuyen(maNhomQuyen)
                                        .trangThai(trangThai)
                                        .build();

                        if (taiKhoanBUS.editTaiKhoan(tk)) {
                                JOptionPane.showMessageDialog(null, "Chỉnh sửa thành công");
                                loadCbTTNhomQuyen();
                                loadTableAcc();
                                reloadAllTT();
                        } else {
                                JOptionPane.showMessageDialog(null, "Chỉnh sửa thất bại");
                        }

                } catch (BusException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                }

        }// GEN-LAST:event_btnChinhSuaActionPerformed

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JPanel accPanel;
        private GUI.Menu.ActionButton btnChinhSua;
        private GUI.Menu.ActionButton btnMokhoa;
        private GUI.Menu.ActionButton btnReload;
        private GUI.Menu.ActionButton btnReloadSearch;
        private GUI.Menu.ActionButton btnSuaNhomQuyen;
        private GUI.Menu.ActionButton btnTimkiem;
        private GUI.Menu.CustomComboBox cbNhomquyen;
        private GUI.Menu.CustomComboBox cbTrangThai;
        private javax.swing.JCheckBox cboxDatve;
        private javax.swing.JCheckBox cboxDiadiem;
        private javax.swing.JCheckBox cboxHDV;
        private javax.swing.JCheckBox cboxHoadon;
        private javax.swing.JCheckBox cboxKhachhang;
        private javax.swing.JCheckBox cboxKhoaTk;
        private javax.swing.JCheckBox cboxKhuyenmai;
        private javax.swing.JCheckBox cboxLKH;
        private javax.swing.JCheckBox cboxLichTrinh;
        private javax.swing.JCheckBox cboxNhanvien;
        private javax.swing.JCheckBox cboxPT;
        private javax.swing.JCheckBox cboxPhanQuyen;
        private javax.swing.JCheckBox cboxSuaDd;
        private javax.swing.JCheckBox cboxSuaHDV;
        private javax.swing.JCheckBox cboxSuaKH;
        private javax.swing.JCheckBox cboxSuaKm;
        private javax.swing.JCheckBox cboxSuaLKH;
        private javax.swing.JCheckBox cboxSuaLT;
        private javax.swing.JCheckBox cboxSuaNv;
        private javax.swing.JCheckBox cboxSuaPT;
        private javax.swing.JCheckBox cboxSuaQ;
        private javax.swing.JCheckBox cboxSuaTk;
        private javax.swing.JCheckBox cboxSuaTour;
        private javax.swing.JCheckBox cboxTTHuy;
        private javax.swing.JCheckBox cboxTaikhoan;
        private javax.swing.JCheckBox cboxThemDd;
        private javax.swing.JCheckBox cboxThemHDV;
        private javax.swing.JCheckBox cboxThemKH;
        private javax.swing.JCheckBox cboxThemKm;
        private javax.swing.JCheckBox cboxThemLKH;
        private javax.swing.JCheckBox cboxThemNv;
        private javax.swing.JCheckBox cboxThemPT;
        private javax.swing.JCheckBox cboxThemTour;
        private javax.swing.JCheckBox cboxThongke;
        private javax.swing.JCheckBox cboxTour;
        private javax.swing.JCheckBox cboxXoaDd;
        private javax.swing.JCheckBox cboxXoaHDV;
        private javax.swing.JCheckBox cboxXoaKH;
        private javax.swing.JCheckBox cboxXoaKm;
        private javax.swing.JCheckBox cboxXoaLKH;
        private javax.swing.JCheckBox cboxXoaLT;
        private javax.swing.JCheckBox cboxXoaNv;
        private javax.swing.JCheckBox cboxXoaPT;
        private javax.swing.JCheckBox cboxXoaTour;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JLabel jLabel5;
        private javax.swing.JLabel jLabel6;
        private javax.swing.JLabel jLabel7;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel11;
        private javax.swing.JPanel jPanel12;
        private javax.swing.JPanel jPanel13;
        private javax.swing.JPanel jPanel14;
        private javax.swing.JPanel jPanel15;
        private javax.swing.JPanel jPanel16;
        private javax.swing.JPanel jPanel17;
        private javax.swing.JPanel jPanel18;
        private javax.swing.JPanel jPanel19;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JPanel jPanel20;
        private javax.swing.JPanel jPanel21;
        private javax.swing.JPanel jPanel22;
        private javax.swing.JPanel jPanel23;
        private javax.swing.JPanel jPanel24;
        private javax.swing.JPanel jPanel3;
        private javax.swing.JPanel jPanel4;
        private javax.swing.JPanel jPanel5;
        private javax.swing.JPanel jPanel6;
        private javax.swing.JPanel jPanel7;
        private javax.swing.JPanel jPanel8;
        private javax.swing.JPanel jPanel9;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane2;
        private javax.swing.JPanel panel2;
        private javax.swing.JPanel panelThongTin;
        private javax.swing.JPanel phanquyenPanel;
        private GUI.Menu.TabbedPaneCustom tabbedPaneCustom1;
        private javax.swing.JTable tbNhomQuyen;
        private javax.swing.JTable tbTaiKhoan;
        private GUI.LoginForm.CustomTextField txtManhanvien1;
        private GUI.LoginForm.CustomTextField txtTenNhomQuyen;
        // End of variables declaration//GEN-END:variables

        private GUI.LoginForm.CustomTextField txtTTMaNV;
        private GUI.LoginForm.CustomPasswordField pfTTMatKhau;
        private GUI.Menu.CustomComboBox<String> cbTTNhomQuyen;
        private GUI.LoginForm.CustomTextField txtTTTrangThai;
}
