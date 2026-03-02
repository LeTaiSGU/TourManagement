package GUI.Dialog;

import BUS.KhachHangBUS;
import DTO.KhachHang;
import Exception.BusException;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

public class KhachHangDialog extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(KhachHangDialog.class.getName());
    private int x, y; 
    
    private static final int BORDER_SIZE = 5;
    private Point mouseDownCompCoords = null;
    private int resizeDirection = -1;
    
     private static final int RESIZE_NONE = -1;
    private static final int RESIZE_TOP = 0;
    private static final int RESIZE_BOTTOM = 1;
    private static final int RESIZE_LEFT = 2;
    private static final int RESIZE_RIGHT = 3;
    private static final int RESIZE_TOP_LEFT = 4;
    private static final int RESIZE_TOP_RIGHT = 5;
    private static final int RESIZE_BOTTOM_LEFT = 6;
    private static final int RESIZE_BOTTOM_RIGHT = 7;
    
    DefaultTableModel modelkh;
    TableRowSorter dskhSorter;
    KhachHangBUS khbus = new KhachHangBUS();
    private String makhselect = null;

    
    public KhachHangDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        initTopSideMoving();
        initResizable();
        
        modelkh = (DefaultTableModel) tableDSKH.getModel();
        loadKhachHang();
        designTable(tableDSKH);
    }
    
    public String getSelectedMaKH() {
        return makhselect;
    }
    
    public void loadKhachHang() {
        try {
            modelkh.setRowCount(0);
            ArrayList<KhachHang> dskh = new ArrayList<>();
            dskh = khbus.getAllKhachHang();

            for (KhachHang kh : dskh) {
                String makh = kh.getMaKhachHang();
                String maloaikh = kh.getMaLoaiKH();
                String tenkh = kh.getTenKhachHang();
                String gioitinh = kh.getGioiTinh();
                int namsinh = kh.getNamSinh();
                String diachi = kh.getDiaChi();
                String sdt = kh.getSoDienThoai();
                String email = kh.getEmail();
                boolean tt = kh.getTrangThai();
                
                Object[] row = {makh, maloaikh, tenkh, gioitinh, namsinh, diachi, sdt, email, tt};
                modelkh.addRow(row);
            }
        }
        catch (BusException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }
    
    public void designTable(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        final TableColumnModel columnModel = table.getColumnModel();
        
        for (int column = 0; column < table.getColumnCount(); column++) {
            TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
            Object headerValue = columnModel.getColumn(column).getHeaderValue();
            Component headerComp = headerRenderer.getTableCellRendererComponent(
                    table, headerValue, false, false, -1, column);
            int width = headerComp.getPreferredSize().width + 10; 

            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 10, width);
            }

            if (width > 700) width = 700;

                columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        topSide = new javax.swing.JPanel();
        btnExit = new javax.swing.JLabel();
        lbCN = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtTenKHDialog = new javax.swing.JTextField();
        txtSDTDialog = new javax.swing.JTextField();
        txtMaKHDialog = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnTimKH = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPaneDSKH = new javax.swing.JScrollPane();
        tableDSKH = new javax.swing.JTable() {
            @Override
            public String getToolTipText(java.awt.event.MouseEvent e) {
                int row = rowAtPoint(e.getPoint());
                int col = columnAtPoint(e.getPoint());
                if (row > -1 && col > -1) {
                    Object value = getValueAt(row, col);
                    return value == null ? null : value.toString();
                }
                return null;
            }
        };

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CHỌN KHÁCH HÀNG");
        setMaximumSize(new java.awt.Dimension(1000000, 7000000));
        setMinimumSize(new java.awt.Dimension(100, 100));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(1200, 700));
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        topSide.setBackground(new java.awt.Color(0, 153, 153));
        topSide.setMaximumSize(new java.awt.Dimension(32767, 50));
        topSide.setPreferredSize(new java.awt.Dimension(1150, 50));

        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/close.png"))); // NOI18N
        btnExit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnExitMouseClicked(evt);
            }
        });

        lbCN.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbCN.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout topSideLayout = new javax.swing.GroupLayout(topSide);
        topSide.setLayout(topSideLayout);
        topSideLayout.setHorizontalGroup(
            topSideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, topSideLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(lbCN)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 751, Short.MAX_VALUE)
                .addComponent(btnExit)
                .addContainerGap())
        );
        topSideLayout.setVerticalGroup(
            topSideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topSideLayout.createSequentialGroup()
                .addGroup(topSideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(topSideLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnExit))
                    .addGroup(topSideLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(lbCN)))
                .addContainerGap(7, Short.MAX_VALUE))
        );

        getContentPane().add(topSide);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("DANH SÁCH KHÁCH HÀNG");
        jLabel1.setAlignmentX(0.5F);
        jLabel1.setMaximumSize(new java.awt.Dimension(85000, 60));
        jLabel1.setMinimumSize(new java.awt.Dimension(850, 40));
        jLabel1.setPreferredSize(new java.awt.Dimension(850, 60));
        getContentPane().add(jLabel1);

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 70));
        jPanel1.setMinimumSize(new java.awt.Dimension(100, 70));
        jPanel1.setPreferredSize(new java.awt.Dimension(850, 70));
        jPanel1.setLayout(new java.awt.GridLayout(2, 3, 35, 5));

        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        jLabel2.setText("Tên khách hàng");
        jPanel1.add(jLabel2);

        jLabel3.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        jLabel3.setText("Số điện thoại");
        jPanel1.add(jLabel3);

        jLabel4.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        jLabel4.setText("Mã khách hàng");
        jPanel1.add(jLabel4);

        txtTenKHDialog.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel1.add(txtTenKHDialog);

        txtSDTDialog.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel1.add(txtSDTDialog);

        txtMaKHDialog.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel1.add(txtMaKHDialog);

        getContentPane().add(jPanel1);

        jPanel2.setMaximumSize(new java.awt.Dimension(850, 20));
        jPanel2.setMinimumSize(new java.awt.Dimension(850, 20));
        jPanel2.setName(""); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel2);

        btnTimKH.setBackground(new java.awt.Color(0, 0, 0));
        btnTimKH.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        btnTimKH.setForeground(new java.awt.Color(255, 255, 255));
        btnTimKH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/find.png"))); // NOI18N
        btnTimKH.setText("Tìm kiếm");
        btnTimKH.setAlignmentX(0.5F);
        btnTimKH.setBorder(null);
        btnTimKH.setMaximumSize(new java.awt.Dimension(20000, 35));
        btnTimKH.setMinimumSize(new java.awt.Dimension(30, 35));
        btnTimKH.setPreferredSize(new java.awt.Dimension(850, 35));
        btnTimKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKHActionPerformed(evt);
            }
        });
        getContentPane().add(btnTimKH);

        jPanel3.setMaximumSize(new java.awt.Dimension(850, 20));
        jPanel3.setMinimumSize(new java.awt.Dimension(850, 20));
        jPanel3.setName(""); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel3);

        jScrollPaneDSKH.setMaximumSize(new java.awt.Dimension(32767, 400));
        jScrollPaneDSKH.setPreferredSize(new java.awt.Dimension(1000, 400));

        tableDSKH.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        tableDSKH.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã khách hàng", "Mã loại khách hàng", "Tên khách hàng", "Giới tính", "Năm sinh", "Địa chỉ", "Số điện thoại", "Email", "Trạng thái"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Byte.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        tableDSKH.setRowHeight(25);
        tableDSKH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableDSKHMouseClicked(evt);
            }
        });
        jScrollPaneDSKH.setViewportView(tableDSKH);

        getContentPane().add(jScrollPaneDSKH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tableDSKHMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableDSKHMouseClicked
        if (evt.getClickCount() == 2) {
            makhselect = tableDSKH.getValueAt(tableDSKH.getSelectedRow(), 0).toString();
            dispose();
        }
    }//GEN-LAST:event_tableDSKHMouseClicked

    private void btnTimKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKHActionPerformed
        dskhSorter = new TableRowSorter(modelkh);
        tableDSKH.setRowSorter(dskhSorter);
        
        ArrayList<RowFilter<Object, Object>> filters = new ArrayList<>();
        
        String matour = txtMaKHDialog.getText().trim();
        if (!matour.isEmpty()) 
            filters.add(RowFilter.regexFilter("(?i)" + matour, 0));
        
        String tentour = txtTenKHDialog.getText().trim();
        if (!tentour.isEmpty())
            filters.add(RowFilter.regexFilter("(?i)" + tentour, 2));
        
        String sdt = txtSDTDialog.getText().trim();
        if (!sdt.isEmpty())
            filters.add(RowFilter.regexFilter("(?i)" + sdt, 6));
        
        if (filters.isEmpty())
            dskhSorter.setRowFilter(null);
        else
            dskhSorter.setRowFilter(RowFilter.andFilter(filters));
    }//GEN-LAST:event_btnTimKHActionPerformed

    private void btnExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExitMouseClicked
        this.dispose();
    }//GEN-LAST:event_btnExitMouseClicked
        
    private void initTopSideMoving() {
        topSide.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX() + 6;
                y = e.getY() + 6;
            }
        });

        topSide.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setLocation(e.getXOnScreen() - x, e.getYOnScreen() - y);
            }
        });
    }
    
    private int getResizeDirection(Point point) {
        int width = getWidth();
        int height = getHeight();

        if (point.x < BORDER_SIZE && point.y < BORDER_SIZE) {
            return RESIZE_TOP_LEFT;
        } else if (point.x > width - BORDER_SIZE && point.y < BORDER_SIZE) {
            return RESIZE_TOP_RIGHT;
        } else if (point.x < BORDER_SIZE && point.y > height - BORDER_SIZE) {
            return RESIZE_BOTTOM_LEFT;
        } else if (point.x > width - BORDER_SIZE && point.y > height - BORDER_SIZE) {
            return RESIZE_BOTTOM_RIGHT;
        } else if (point.y < BORDER_SIZE) {
            return RESIZE_TOP;
        } else if (point.y > height - BORDER_SIZE) {
            return RESIZE_BOTTOM;
        } else if (point.x < BORDER_SIZE) {
            return RESIZE_LEFT;
        } else if (point.x > width - BORDER_SIZE) {
            return RESIZE_RIGHT;
        }
        return RESIZE_NONE;
    }
    
    private void setCursorForDirection(int direction) {
        Cursor cursor;
        switch (direction) {
            case RESIZE_TOP:
                cursor = Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
                break;
            case RESIZE_BOTTOM:
                cursor = Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
                break;
            case RESIZE_LEFT:
                cursor = Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR);
                break;
            case RESIZE_RIGHT:
                cursor = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
                break;
            case RESIZE_TOP_LEFT:
                cursor = Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);
                break;
            case RESIZE_TOP_RIGHT:
                cursor = Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);
                break;
            case RESIZE_BOTTOM_LEFT:
                cursor = Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);
                break;
            case RESIZE_BOTTOM_RIGHT:
                cursor = Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
                break;
            default:
                cursor = Cursor.getDefaultCursor();
        }
        setCursor(cursor);
    }

    private void handleResize(java.awt.event.MouseEvent e) {
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();

        int mouseX = e.getXOnScreen();
        int mouseY = e.getYOnScreen();

        switch (resizeDirection) {
            case RESIZE_TOP:
                int newHeight = height + (y - mouseY);
                setBounds(x, mouseY, width, newHeight);
                break;
            case RESIZE_BOTTOM:
                setBounds(x, y, width, mouseY - y);
                break;
            case RESIZE_LEFT:
                int newWidth = width + (x - mouseX);
                setBounds(mouseX, y, newWidth, height);
                break;
            case RESIZE_RIGHT:
                setBounds(x, y, mouseX - x, height);
                break;
            case RESIZE_TOP_LEFT:
                int newWidthTL = width + (x - mouseX);
                int newHeightTL = height + (y - mouseY);
                setBounds(mouseX, mouseY, newWidthTL, newHeightTL);
                break;
            case RESIZE_TOP_RIGHT:
                int newHeightTR = height + (y - mouseY);
                setBounds(x, mouseY, mouseX - x, newHeightTR);
                break;
            case RESIZE_BOTTOM_LEFT:
                int newWidthBL = width + (x - mouseX);
                setBounds(mouseX, y, newWidthBL, mouseY - y);
                break;
            case RESIZE_BOTTOM_RIGHT:
                setBounds(x, y, mouseX - x, mouseY - y);
                break;
        }
    }
    
    private void initResizable() {
        java.awt.event.MouseAdapter resizeListener = new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                mouseDownCompCoords = e.getPoint();
                resizeDirection = getResizeDirection(e.getPoint());
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                mouseDownCompCoords = null;
                resizeDirection = RESIZE_NONE;
                setCursor(Cursor.getDefaultCursor());
            }

            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int direction = getResizeDirection(e.getPoint());
                setCursorForDirection(direction);
            }

            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                if (mouseDownCompCoords != null && resizeDirection != RESIZE_NONE) {
                    handleResize(e);
                }
            }
        };
        addMouseListener(resizeListener);
        addMouseMotionListener(resizeListener);
    }
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                KhachHangDialog dialog = new KhachHangDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnExit;
    private javax.swing.JButton btnTimKH;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPaneDSKH;
    private javax.swing.JLabel lbCN;
    private javax.swing.JTable tableDSKH;
    private javax.swing.JPanel topSide;
    private javax.swing.JTextField txtMaKHDialog;
    private javax.swing.JTextField txtSDTDialog;
    private javax.swing.JTextField txtTenKHDialog;
    // End of variables declaration//GEN-END:variables
}
