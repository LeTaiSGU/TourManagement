package GUI;

import BUS.KhachHangBUS;
import DTO.KhachHangDTO;
import Exception.BusException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class KhachHangPanel extends javax.swing.JPanel {
    KhachHangBUS bus = new KhachHangBUS();
    TableRowSorter<DefaultTableModel> sorter;
    ArrayList<RowFilter<Object, Object>> filters = new ArrayList<>();

    public KhachHangPanel() {
        initComponents();
        customTableHeader();
        setupTableResize();
        loadData();
        khoiTaoTableSorter();

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
            for (KhachHangDTO kh : list) {
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
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
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
                                .addContainerGap(103, Short.MAX_VALUE)));
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(jLabel1)
                                .addContainerGap(19, Short.MAX_VALUE)));

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

        maLoaiKH.setModel(
                new javax.swing.DefaultComboBoxModel(new String[] { "Tất cả", "LKH01", "LKH02", "LKH03", " " }));
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
                                        .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 252,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jpanel7Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jpanel7Layout.createSequentialGroup()
                                                        .addGap(21, 21, 21)
                                                        .addComponent(soDienThoai,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(jpanel7Layout.createSequentialGroup()
                                                        .addGap(22, 22, 22)
                                                        .addGroup(jpanel7Layout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLabel42,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 250,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGroup(jpanel7Layout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        false)
                                                                        .addComponent(maKhachHang,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(tenKhachHang,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(gioiTinh,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(namSinh,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(diaChi,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(lammoi,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                        .addGroup(jpanel7Layout.createSequentialGroup()
                                                                                .addGroup(jpanel7Layout
                                                                                        .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                false)
                                                                                        .addComponent(them,
                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                        .addComponent(timkiem,
                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                                .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addGroup(jpanel7Layout
                                                                                        .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addComponent(sua,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                        .addComponent(xoa,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                        .addComponent(email,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addComponent(jLabel43,
                                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 250,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jLabel44,
                                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 250,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jLabel45,
                                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 250,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jLabel46,
                                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 250,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jLabel47,
                                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 250,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(jpanel7Layout.createSequentialGroup()
                                                        .addGap(20, 20, 20)
                                                        .addGroup(jpanel7Layout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLabel48,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 251,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(maLoaiKH,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addContainerGap(28, Short.MAX_VALUE)));
        jpanel7Layout.setVerticalGroup(
                jpanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jpanel7Layout.createSequentialGroup()
                                .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(maKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 33,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tenKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 33,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(gioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 33,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(namSinh, javax.swing.GroupLayout.PREFERRED_SIZE, 33,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(diaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 33,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(soDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 33,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel48, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(maLoaiKH, javax.swing.GroupLayout.PREFERRED_SIZE, 33,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel49, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 33,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jpanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(timkiem, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(sua, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jpanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(them, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(xoa, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(lammoi, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(195, Short.MAX_VALUE)));

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
                new Object[][] {
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null }
                },
                new String[] {
                        "Mã Khách Hàng", "Tên khách hàng", "Giới tính", "Năm sinh", "Địa chỉ", "Số điện thoại",
                        "Mã loại khách hàng", "Email"
                }) {
            Class[] types = new Class[] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class,
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbKhachHang);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 973,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(35, Short.MAX_VALUE)));
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 519,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(361, Short.MAX_VALUE)));

        jPanel3.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void lammoiActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_lammoiActionPerformed
        maKhachHang.setText("");
        tenKhachHang.setText("");
        gioiTinh.setText("");
        namSinh.setText("");
        diaChi.setText("");
        soDienThoai.setText("");
        email.setText("");
    }// GEN-LAST:event_lammoiActionPerformed

    private void timkiemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_timkiemActionPerformed
        DefaultTableModel model = (DefaultTableModel) tbKhachHang.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        ArrayList<RowFilter<DefaultTableModel, Integer>> filters = new ArrayList<>();
        tbKhachHang.setRowSorter(sorter);
        if (!maKhachHang.getText().trim().isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + maKhachHang.getText(), 0));
        }
        if (!tenKhachHang.getText().trim().isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + tenKhachHang.getText(), 1));
        }
        if (!gioiTinh.getText().trim().isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + gioiTinh.getText(), 2));
        }
        if (!namSinh.getText().trim().isEmpty()) {
            filters.add(RowFilter.regexFilter(namSinh.getText(), 3));
        }
        if (!diaChi.getText().trim().isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + diaChi.getText(), 4));
        }
        if (!soDienThoai.getText().trim().isEmpty()) {
            filters.add(RowFilter.regexFilter(soDienThoai.getText(), 5));
        }
        String loai = maLoaiKH.getSelectedItem().toString();
        if (!loai.equals("Tất cả")) {
            filters.add(RowFilter.regexFilter(loai, 6));
        }
        if (!email.getText().trim().isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + email.getText(), 7));
        }
        sorter.setRowFilter(RowFilter.andFilter(filters));
    }// GEN-LAST:event_timkiemActionPerformed

    private void themActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_themActionPerformed
        DefaultTableModel model = (DefaultTableModel) tbKhachHang.getModel();
        model.addRow(new Object[] {
                maKhachHang.getText(),
                tenKhachHang.getText(),
                gioiTinh.getText(),
                namSinh.getText(),
                diaChi.getText(),
                soDienThoai.getText(),
                email.getText()
        });
    }// GEN-LAST:event_themActionPerformed

    private void suaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_suaActionPerformed
        int i = tbKhachHang.getSelectedRow();
        if (i >= 0) {
            DefaultTableModel model = (DefaultTableModel) tbKhachHang.getModel();
            model.setValueAt(maKhachHang.getText(), i, 0);
            model.setValueAt(tenKhachHang.getText(), i, 1);
            model.setValueAt(gioiTinh.getText(), i, 2);
            model.setValueAt(namSinh.getText(), i, 3);
            model.setValueAt(diaChi.getText(), i, 4);
            model.setValueAt(soDienThoai.getText(), i, 5);
            model.setValueAt(email.getText(), i, 6);
        }
    }// GEN-LAST:event_suaActionPerformed

    private void xoaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_xoaActionPerformed
        int i = tbKhachHang.getSelectedRow();
        if (i >= 0) {
            DefaultTableModel model = (DefaultTableModel) tbKhachHang.getModel();
            model.removeRow(i);
        }
    }// GEN-LAST:event_xoaActionPerformed

    private void maLoaiKHActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_maLoaiKHActionPerformed

    }// GEN-LAST:event_maLoaiKHActionPerformed

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
