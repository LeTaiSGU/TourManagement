package GUI;
import BUS.KhuyenMaiBUS;
import DTO.KhuyenMaiDTO;
import Exception.BusException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.*;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class KhuyenMaiPanel extends javax.swing.JPanel {
    KhuyenMaiBUS bus = new KhuyenMaiBUS();
    public KhuyenMaiPanel() {
        initComponents();
        customTableHeader();
        setupTableResize();
        loadData();
    }
    private void customTableHeader() {
        JTableHeader header = tbKhuyenMai.getTableHeader();
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
    tbKhuyenMai.setAutoResizeMode(tbKhuyenMai.AUTO_RESIZE_ALL_COLUMNS);
    tbKhuyenMai.setRowHeight(28);
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(CENTER);

    for (int i = 0; i < tbKhuyenMai.getColumnCount(); i++) {
        tbKhuyenMai.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
    TableColumnModel columnModel = tbKhuyenMai.getColumnModel();
    for (int i = 0; i < columnModel.getColumnCount(); i++) {
        columnModel.getColumn(i).setPreferredWidth(150);
    }
    }
    public void loadData() {
    DefaultTableModel model = (DefaultTableModel) tbKhuyenMai.getModel();
    model.setRowCount(0);  
    try {
        ArrayList<KhuyenMaiDTO> list = bus.getAll();  
        for(KhuyenMaiDTO km : list){
            model.addRow(new Object[]{
                km.getMaKhuyenMai(),    
                km.getTenKhuyenMai(),   
                km.getPhuongThucKM(),  
                km.getMoTa()           
            });
        }
    } catch (BusException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu khuyến mãi", "Error", JOptionPane.ERROR_MESSAGE);
    }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        maKhuyenMai = new GUI.LoginForm.CustomTextField();
        jLabel3 = new javax.swing.JLabel();
        tenKhuyenMai = new GUI.LoginForm.CustomTextField();
        jLabel4 = new javax.swing.JLabel();
        phuongThucKM = new GUI.LoginForm.CustomTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        moTa = new javax.swing.JTextArea();
        timkiem = new GUI.Menu.ActionButton();
        lammoi = new GUI.Menu.ActionButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        maKhuyenMai1 = new GUI.LoginForm.CustomTextField();
        jLabel7 = new javax.swing.JLabel();
        tenKhuyenMai1 = new GUI.LoginForm.CustomTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        moTa1 = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        phuongThucKM1 = new GUI.LoginForm.CustomTextField();
        jLabel9 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbKhuyenMai = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        sua = new GUI.Menu.ActionButton();
        xoa = new GUI.Menu.ActionButton();
        them = new GUI.Menu.ActionButton();

        setPreferredSize(new java.awt.Dimension(1353, 780));

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 36)); // NOI18N
        jLabel1.setText("Khuyến mãi");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(jLabel1)
                .addContainerGap(81, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel1)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel2.setText("Mã khuyến mãi");

        maKhuyenMai.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jLabel3.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel3.setText("Tên khuyến mãi ");

        tenKhuyenMai.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jLabel4.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel4.setText("Phương thức khuyến mãi");

        phuongThucKM.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        phuongThucKM.addActionListener(this::phuongThucKMActionPerformed);

        jLabel5.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel5.setText("Mô tả");

        moTa.setColumns(20);
        moTa.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        moTa.setRows(5);
        jScrollPane1.setViewportView(moTa);

        timkiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/find.png"))); // NOI18N
        timkiem.setText("Tìm kiếm");
        timkiem.setColorTop(new java.awt.Color(102, 204, 255));
        timkiem.addActionListener(this::timkiemActionPerformed);

        lammoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/reload2.png"))); // NOI18N
        lammoi.setText("Làm mới");
        lammoi.setColorBottom(new java.awt.Color(102, 102, 102));
        lammoi.setColorTop(new java.awt.Color(204, 204, 204));
        lammoi.addActionListener(this::lammoiActionPerformed);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lammoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timkiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phuongThucKM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tenKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(maKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel2, jLabel3, jLabel4, jLabel5, jScrollPane1, lammoi, maKhuyenMai, phuongThucKM, tenKhuyenMai, timkiem});

        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tenKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(12, 12, 12)
                .addComponent(phuongThucKM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(timkiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lammoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(95, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel2, java.awt.BorderLayout.LINE_START);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel6.setText("Mã khuyến mãi");

        maKhuyenMai1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jLabel7.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel7.setText("Tên khuyến mãi ");

        tenKhuyenMai1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        moTa1.setColumns(20);
        moTa1.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        moTa1.setRows(5);
        jScrollPane2.setViewportView(moTa1);

        jLabel8.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel8.setText("Phương thức KM");

        phuongThucKM1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        phuongThucKM1.addActionListener(this::phuongThucKM1ActionPerformed);

        jLabel9.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel9.setText("Mô tả");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(maKhuyenMai1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tenKhuyenMai1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phuongThucKM1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(142, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(maKhuyenMai1, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE))
                            .addComponent(phuongThucKM1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(27, 27, 27)))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tenKhuyenMai1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        jPanel3.add(jPanel6, java.awt.BorderLayout.PAGE_START);

        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1012, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel7.add(jPanel8, java.awt.BorderLayout.PAGE_END);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        tbKhuyenMai.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        tbKhuyenMai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã khuyến mãi", "Tên khuyến mãi", "Phương thức khuyến mãi", "Mô tả"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tbKhuyenMai);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        sua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/pencil.png"))); // NOI18N
        sua.setText("Sửa");
        sua.setColorTop(new java.awt.Color(102, 204, 255));
        sua.addActionListener(this::suaActionPerformed);

        xoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/delete.png"))); // NOI18N
        xoa.setText("Xóa");
        xoa.setColorBottom(new java.awt.Color(153, 0, 51));
        xoa.setColorTop(new java.awt.Color(255, 102, 102));
        xoa.addActionListener(this::xoaActionPerformed);

        them.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/plus.png"))); // NOI18N
        them.setText("Thêm");
        them.setColorTop(new java.awt.Color(51, 204, 255));
        them.addActionListener(this::themActionPerformed);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(105, 105, 105)
                .addComponent(them, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(105, 105, 105)
                .addComponent(sua, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 105, Short.MAX_VALUE)
                .addComponent(xoa, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(105, 105, 105))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(them, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 867, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.add(jPanel9, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel7, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void phuongThucKMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phuongThucKMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_phuongThucKMActionPerformed

    private void phuongThucKM1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phuongThucKM1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_phuongThucKM1ActionPerformed

    private void lammoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lammoiActionPerformed
        maKhuyenMai.setText("");
        tenKhuyenMai.setText("");
        phuongThucKM.setText("");
        moTa.setText("");
    }//GEN-LAST:event_lammoiActionPerformed

    private void timkiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timkiemActionPerformed
        DefaultTableModel model = (DefaultTableModel) tbKhuyenMai.getModel();
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    tbKhuyenMai.setRowSorter(sorter);

    ArrayList<RowFilter<DefaultTableModel, Integer>> filters = new ArrayList<>();

    if(!maKhuyenMai.getText().trim().isEmpty()){
        filters.add(RowFilter.regexFilter("(?i)" + maKhuyenMai.getText(), 0));
    }

    if(!tenKhuyenMai.getText().trim().isEmpty()){
        filters.add(RowFilter.regexFilter("(?i)" + tenKhuyenMai.getText(), 1));
    }

    if(!phuongThucKM.getText().trim().isEmpty()){
        filters.add(RowFilter.regexFilter(phuongThucKM.getText(), 2));
    }

    if(!moTa.getText().trim().isEmpty()){
        filters.add(RowFilter.regexFilter("(?i)" + moTa.getText(), 3));
    }

    sorter.setRowFilter(RowFilter.andFilter(filters));
    }//GEN-LAST:event_timkiemActionPerformed

    private void themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_themActionPerformed
        DefaultTableModel model = (DefaultTableModel) tbKhuyenMai.getModel();
        model.addRow(new Object[]{
        maKhuyenMai1.getText(),
        tenKhuyenMai1.getText(),
        phuongThucKM1.getText(),
        moTa1.getText()
    });
    }//GEN-LAST:event_themActionPerformed

    private void suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suaActionPerformed
        int i = tbKhuyenMai.getSelectedRow();
        if(i >= 0){
        DefaultTableModel model = (DefaultTableModel) tbKhuyenMai.getModel();
        model.setValueAt(maKhuyenMai1.getText(), i, 0);
        model.setValueAt(tenKhuyenMai1.getText(), i, 1);
        model.setValueAt(phuongThucKM1.getText(), i, 2);
        model.setValueAt(moTa1.getText(), i, 3);
    }
    }//GEN-LAST:event_suaActionPerformed

    private void xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xoaActionPerformed
        int i = tbKhuyenMai.getSelectedRow();
        if(i >= 0){
        DefaultTableModel model = (DefaultTableModel) tbKhuyenMai.getModel();
        model.removeRow(i);
    }
    }//GEN-LAST:event_xoaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private GUI.Menu.ActionButton lammoi;
    private GUI.LoginForm.CustomTextField maKhuyenMai;
    private GUI.LoginForm.CustomTextField maKhuyenMai1;
    private javax.swing.JTextArea moTa;
    private javax.swing.JTextArea moTa1;
    private GUI.LoginForm.CustomTextField phuongThucKM;
    private GUI.LoginForm.CustomTextField phuongThucKM1;
    private GUI.Menu.ActionButton sua;
    private javax.swing.JTable tbKhuyenMai;
    private GUI.LoginForm.CustomTextField tenKhuyenMai;
    private GUI.LoginForm.CustomTextField tenKhuyenMai1;
    private GUI.Menu.ActionButton them;
    private GUI.Menu.ActionButton timkiem;
    private GUI.Menu.ActionButton xoa;
    // End of variables declaration//GEN-END:variables

}
