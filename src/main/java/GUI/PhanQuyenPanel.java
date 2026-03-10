
package GUI;

import BUS.NhomQuyenBUS;
import BUS.TaiKhoanBUS;
import DTO.NhomQuyen;
import DTO.TaiKhoan;
import DTO.TaiKhoanDTO;
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
import GUI.ScrollPane.ScrollPaneWin11;
import GUI.LoginForm.CustomTextField;
import GUI.LoginForm.CustomPasswordField;
import GUI.Menu.CustomComboBox;
import Exception.BusException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

public class PhanQuyenPanel extends javax.swing.JPanel {

    private TaiKhoanBUS taiKhoanBUS = new TaiKhoanBUS();
    private NhomQuyenBUS nhomQuyenBUS = new NhomQuyenBUS();
    private List<TaiKhoan> listAccCache = new ArrayList<>();

    public PhanQuyenPanel() {
        initComponents();
        initGUI();
    }

    public void initGUI() {
        customTableHeader(tbTaiKhoan);
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        setupPanelThongTinTK();
        loadTableAcc();
        loadCbTTNhomQuyen();
        loadCbNhomQuyen();
        cbTrangThai.setModel(
                new DefaultComboBoxModel(new String[] { "...", "Ho\u1EA1t \u0111\u1ED9ng", "\u0110\u00E3 kh\u00F3a" }));
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPaneCustom1 = new GUI.Menu.TabbedPaneCustom();
        jPanel1 = new javax.swing.JPanel();
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
        jPanel2 = new javax.swing.JPanel();

        setPreferredSize(new java.awt.Dimension(1320, 950));
        setLayout(new java.awt.BorderLayout());

        tabbedPaneCustom1.setBackground(new java.awt.Color(255, 255, 255));
        tabbedPaneCustom1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tabbedPaneCustom1.setSelectedColor(new java.awt.Color(51, 153, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.BorderLayout());

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

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel2.setText("Mã nhân viên");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel3.setText("Nhóm quyền");

        cbNhomquyen.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel4.setText("Trạng thái");

        txtManhanvien1.setIconGap(1);

        cbTrangThai.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Hoạt động", "Đã khóa" }));
        cbTrangThai.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

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
                                .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(btnTimkiem, javax.swing.GroupLayout.PREFERRED_SIZE, 250,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnReloadSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 250,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 250,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(panel2Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txtManhanvien1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(cbNhomquyen, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(panel2Layout.createSequentialGroup()
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
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtManhanvien1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbNhomquyen, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(cbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnTimkiem, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnReloadSearch, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 514, Short.MAX_VALUE)));

        jPanel3.add(panel2, java.awt.BorderLayout.LINE_END);

        jPanel1.add(jPanel3, java.awt.BorderLayout.LINE_START);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new java.awt.BorderLayout());

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
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
                        .addGap(0, 1015, Short.MAX_VALUE));
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

        jPanel7.setBackground(new java.awt.Color(255, 255, 102));
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
                                .addComponent(btnChinhSua, javax.swing.GroupLayout.PREFERRED_SIZE, 192,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(btnMokhoa, javax.swing.GroupLayout.PREFERRED_SIZE, 192,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(btnReload, javax.swing.GroupLayout.PREFERRED_SIZE, 192,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(335, Short.MAX_VALUE)));
        jPanel7Layout.setVerticalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnChinhSua, javax.swing.GroupLayout.PREFERRED_SIZE, 56,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnMokhoa, javax.swing.GroupLayout.PREFERRED_SIZE, 56,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnReload, javax.swing.GroupLayout.PREFERRED_SIZE, 56,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(210, Short.MAX_VALUE)));

        jPanel4.add(jPanel7, java.awt.BorderLayout.PAGE_END);

        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);

        tabbedPaneCustom1.addTab("Tài khoản", jPanel1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1315, Short.MAX_VALUE));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1013, Short.MAX_VALUE));

        tabbedPaneCustom1.addTab("Phân quyền", jPanel2);

        add(tabbedPaneCustom1, java.awt.BorderLayout.PAGE_START);
    }

    private void btnTimkiemActionPerformed(java.awt.event.ActionEvent evt) {
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

    }

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

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private GUI.Menu.ActionButton btnChinhSua;
    private GUI.Menu.ActionButton btnMokhoa;
    private GUI.Menu.ActionButton btnReload;
    private GUI.Menu.ActionButton btnReloadSearch;
    private GUI.Menu.ActionButton btnTimkiem;
    private GUI.Menu.CustomComboBox cbNhomquyen;
    private GUI.Menu.CustomComboBox cbTrangThai;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel panel2;
    private javax.swing.JPanel panelThongTin;
    private GUI.Menu.TabbedPaneCustom tabbedPaneCustom1;
    private javax.swing.JTable tbTaiKhoan;
    private GUI.LoginForm.CustomTextField txtManhanvien1;
    // End of variables declaration//GEN-END:variables

    private GUI.LoginForm.CustomTextField txtTTMaNV;
    private GUI.LoginForm.CustomPasswordField pfTTMatKhau;
    private GUI.Menu.CustomComboBox<String> cbTTNhomQuyen;
    private GUI.LoginForm.CustomTextField txtTTTrangThai;
}
