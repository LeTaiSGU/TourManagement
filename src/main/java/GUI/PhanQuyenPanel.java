
package GUI;

import BUS.TaiKhoanBUS;
import DTO.TaiKhoan;
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
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

public class PhanQuyenPanel extends javax.swing.JPanel {

    private TaiKhoanBUS taiKhoanBUS = new TaiKhoanBUS();
   
    public PhanQuyenPanel() {
        initComponents();
        initGUI();
    }

    public void initGUI() {
        customTableHeader(tbTaiKhoan);
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        setupPanelThongTinTK();
        loadTableAcc();
    }

    public void loadTableAcc(){
        try { 
            List<TaiKhoan> listAcc = taiKhoanBUS.getAllTaiKhoan();
            DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Mã Nhân Viên", "Mật Khẩu", "Mã Quyền", "Trạng Thái"}, 0
            );
            
            for (TaiKhoan tk : listAcc){
                String maskedPassword = "●".repeat(tk.getMatKhau().length());
                model.addRow(new Object[]{
                    tk.getMaNhanVien(),
                    maskedPassword,
                    tk.getMaNhomQuyen(),
                    tk.getTrangThai() ? "Hoạt động" : "Đã khóa",
                });
            }
            tbTaiKhoan.setModel(model);
        } catch (BusException e){
            JOptionPane.showMessageDialog(null, "Lỗi: " + e);
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
        lblMaNV.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtTTMaNV = new CustomTextField();
        txtTTMaNV.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtTTMaNV.setEditable(false);
        txtTTMaNV.setFocusable(false);
        txtTTMaNV.setBackgroundColor(new Color(245, 245, 245));

        JLabel lblMatKhau = makeLabel("Mật khẩu");
        lblMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        pfTTMatKhau = new CustomPasswordField();
        pfTTMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        // Hàng 2: Nhóm quyền | Trạng thái
        JLabel lblNhomQuyen = makeLabel("Nhóm quyền");
        lblNhomQuyen.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        cbTTNhomQuyen = new CustomComboBox<>();
        cbTTNhomQuyen.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        JLabel lblTrangThai = makeLabel("Trạng thái");
        lblTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 18));
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
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
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
                setBackground(new Color(41,128,185));
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPaneCustom1 = new GUI.Menu.TabbedPaneCustom();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnTimkiem = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cbNhomquyen = new GUI.Menu.CustomComboBox();
        jLabel4 = new javax.swing.JLabel();
        txtManhanvien1 = new GUI.LoginForm.CustomTextField();
        cbTrangThai = new GUI.Menu.CustomComboBox();
        actionButton1 = new GUI.Menu.ActionButton();
        btnReloadSearch = new GUI.Menu.ActionButton();
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
                .addContainerGap(98, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel1)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel5, java.awt.BorderLayout.PAGE_START);

        btnTimkiem.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel2.setText("Mã nhân viên");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel3.setText("Nhóm quyền");

        cbNhomquyen.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4" }));
        cbNhomquyen.setSelectedIndex(1);
        cbNhomquyen.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel4.setText("Trạng thái");

        txtManhanvien1.setText("1");
        txtManhanvien1.setIconGap(1);

        cbTrangThai.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mở khóa", "Đã khóa" }));
        cbTrangThai.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        actionButton1.setBorder(null);
        actionButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/search2.png"))); // NOI18N
        actionButton1.setText("Tìm kiếm");
        actionButton1.setColorBottom(new java.awt.Color(0, 0, 0));
        actionButton1.setColorTop(new java.awt.Color(0, 0, 0));
        actionButton1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N

        btnReloadSearch.setBorder(null);
        btnReloadSearch.setForeground(new java.awt.Color(0, 0, 0));
        btnReloadSearch.setText("Làm mới");
        btnReloadSearch.setColorBottom(new java.awt.Color(204, 204, 204));
        btnReloadSearch.setColorTop(new java.awt.Color(204, 204, 204));
        btnReloadSearch.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N

        javax.swing.GroupLayout btnTimkiemLayout = new javax.swing.GroupLayout(btnTimkiem);
        btnTimkiem.setLayout(btnTimkiemLayout);
        btnTimkiemLayout.setHorizontalGroup(
            btnTimkiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnTimkiemLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(btnTimkiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnReloadSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(actionButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(btnTimkiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtManhanvien1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbNhomquyen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(btnTimkiemLayout.createSequentialGroup()
                            .addGap(12, 12, 12)
                            .addGroup(btnTimkiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addComponent(jLabel3)
                                .addComponent(jLabel4)))))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        btnTimkiemLayout.setVerticalGroup(
            btnTimkiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnTimkiemLayout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtManhanvien1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbNhomquyen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(cbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(actionButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnReloadSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 514, Short.MAX_VALUE))
        );

        jPanel3.add(btnTimkiem, java.awt.BorderLayout.LINE_END);

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
            .addGap(0, 1015, Short.MAX_VALUE)
        );
        panelThongTinLayout.setVerticalGroup(
            panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel8.add(panelThongTin, java.awt.BorderLayout.PAGE_START);

        tbTaiKhoan.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        tbTaiKhoan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", "", "", ""},
                {"", "", "", null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã Nhân Viên", "Mật khẩu", "Mã Quyền", "Trạng Thái"
            }
        ));
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
        btnChinhSua.setText("Chỉnh sửa");
        btnChinhSua.setColorBottom(new java.awt.Color(38, 198, 218));
        btnChinhSua.setColorTop(new java.awt.Color(0, 131, 143));
        btnChinhSua.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btnChinhSua.setRadius(50);

        btnMokhoa.setBorder(null);
        btnMokhoa.setText("Mở khóa");
        btnMokhoa.setColorBottom(new java.awt.Color(239, 83, 80));
        btnMokhoa.setColorTop(new java.awt.Color(198, 40, 40));
        btnMokhoa.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btnMokhoa.setRadius(50);

        btnReload.setBorder(null);
        btnReload.setText("Làm mới");
        btnReload.setColorBottom(new java.awt.Color(189, 189, 189));
        btnReload.setColorTop(new java.awt.Color(117, 117, 117));
        btnReload.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btnReload.setRadius(50);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(btnChinhSua, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnMokhoa, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnReload, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(335, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnChinhSua, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMokhoa, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReload, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(210, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel7, java.awt.BorderLayout.PAGE_END);

        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);

        tabbedPaneCustom1.addTab("Tài khoản", jPanel1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1315, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1013, Short.MAX_VALUE)
        );

        tabbedPaneCustom1.addTab("Phân quyền", jPanel2);

        add(tabbedPaneCustom1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void tbTaiKhoanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbTaiKhoanMouseClicked
        int row = tbTaiKhoan.getSelectedRow();
        if (row == -1 ) return;
        
        txtTTMaNV.setText(tbTaiKhoan.getValueAt(row, 0).toString());
        pfTTMatKhau.setText(tbTaiKhoan.getValueAt(row, 1).toString());
//        cbTTNhomQuyen.setSelectedItem();
        txtTTTrangThai.setText(tbTaiKhoan.getValueAt(row, 3).toString());
        
    }//GEN-LAST:event_tbTaiKhoanMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private GUI.Menu.ActionButton actionButton1;
    private GUI.Menu.ActionButton btnChinhSua;
    private GUI.Menu.ActionButton btnMokhoa;
    private GUI.Menu.ActionButton btnReload;
    private GUI.Menu.ActionButton btnReloadSearch;
    private javax.swing.JPanel btnTimkiem;
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
