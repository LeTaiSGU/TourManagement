package GUI.Dialog;

import BUS.CTHDBUS;
import DTO.CTHD;
import Exception.BusException;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public class CTHDDialog extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CTHDDialog.class.getName());

    private static String maHD, maNV, maKH;
    DefaultTableModel modelcthd;
    CTHDBUS cthdbus = new CTHDBUS();
    
    public CTHDDialog(java.awt.Frame parent, boolean modal, String mahd, String manv, String makh) {
        super(parent, modal);
        initComponents();
        this.maHD = mahd;
        this.maNV = manv;
        this.maKH = makh;
        initGUI();
    }
    
    public void initGUI() {
        setLocationRelativeTo(null);
        modelcthd = (DefaultTableModel) tableCTHD.getModel();
        txtMaHD.setText(maHD);
        txtMaNV.setText(maNV);
        txtMaKH.setText(maKH);
        txtMaHD.setEditable(false);
        txtMaNV.setEditable(false);
        txtMaKH.setEditable(false);
        loadDataCTHD();
        designTable(tableCTHD);
    }
    
    public void loadDataCTHD() {
        try {
            modelcthd.setRowCount(0);
            ArrayList<CTHD> dscthd = new ArrayList<>();
            dscthd = cthdbus.getDSCTHDTheoMaHD(maHD);
            
            for (CTHD cthd : dscthd) {
                String matour = cthd.getMaTour();
                String tentour = cthd.getTenTour();
                double giatour = cthd.getGiaTour();
                int sl = cthd.getSoLuongVe();

                Object[] row = {matour, tentour, giatour, sl};
                modelcthd.addRow(row);
            }
        }
        catch (BusException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblCTHD = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblMaHD = new javax.swing.JLabel();
        lblMaKH = new javax.swing.JLabel();
        lblMaNV = new javax.swing.JLabel();
        txtMaHD = new javax.swing.JTextField();
        txtMaKH = new javax.swing.JTextField();
        txtMaNV = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPaneDSKH = new javax.swing.JScrollPane();
        tableCTHD = new javax.swing.JTable() {
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
        setTitle("Xem Chi tiết hóa đơn");
        setMinimumSize(new java.awt.Dimension(900, 500));
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        lblCTHD.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblCTHD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCTHD.setText("CHI TIẾT HÓA ĐƠN");
        lblCTHD.setAlignmentX(0.5F);
        lblCTHD.setMaximumSize(new java.awt.Dimension(85000, 60));
        lblCTHD.setMinimumSize(new java.awt.Dimension(850, 40));
        lblCTHD.setPreferredSize(new java.awt.Dimension(850, 60));
        getContentPane().add(lblCTHD);

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 70));
        jPanel1.setMinimumSize(new java.awt.Dimension(100, 70));
        jPanel1.setPreferredSize(new java.awt.Dimension(850, 70));
        jPanel1.setLayout(new java.awt.GridLayout(2, 3, 35, 5));

        lblMaHD.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        lblMaHD.setText("Mã hóa đơn");
        jPanel1.add(lblMaHD);

        lblMaKH.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        lblMaKH.setText("Mã khách hàng");
        jPanel1.add(lblMaKH);

        lblMaNV.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        lblMaNV.setText("Mã nhân viên");
        jPanel1.add(lblMaNV);

        txtMaHD.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel1.add(txtMaHD);

        txtMaKH.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel1.add(txtMaKH);

        txtMaNV.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel1.add(txtMaNV);

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

        jScrollPaneDSKH.setMaximumSize(new java.awt.Dimension(32767, 250));
        jScrollPaneDSKH.setPreferredSize(new java.awt.Dimension(1000, 250));

        tableCTHD.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        tableCTHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã tour", "Tên tour", "Đơn giá", "Số lượng vé"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        tableCTHD.setRowHeight(25);
        jScrollPaneDSKH.setViewportView(tableCTHD);

        getContentPane().add(jScrollPaneDSKH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void designTable(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        final TableColumnModel columnModel = table.getColumnModel();
        
        for (int column = 0; column < table.getColumnCount(); column++) {
            // 1. Lấy độ rộng của Header (Tiêu đề)
        TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
        Object headerValue = columnModel.getColumn(column).getHeaderValue();
        Component headerComp = headerRenderer.getTableCellRendererComponent(
                table, headerValue, false, false, -1, column);
        int width = headerComp.getPreferredSize().width + 10; // +10 để có khoảng trống (padding)

        // 2. Lấy độ rộng của nội dung ô dài nhất trong cột (nếu có dữ liệu)
        for (int row = 0; row < table.getRowCount(); row++) {
            TableCellRenderer renderer = table.getCellRenderer(row, column);
            Component comp = table.prepareRenderer(renderer, row, column);
            width = Math.max(comp.getPreferredSize().width + 10, width);
        }

        // 3. Áp dụng độ rộng đã tính (giới hạn tối đa nếu cần)
        if (width > 500) width = 500;
            
            columnModel.getColumn(column).setPreferredWidth(width);
        }
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
                CTHDDialog dialog = new CTHDDialog(new javax.swing.JFrame(), true, maHD, maNV, maKH);
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPaneDSKH;
    private javax.swing.JLabel lblCTHD;
    private javax.swing.JLabel lblMaHD;
    private javax.swing.JLabel lblMaKH;
    private javax.swing.JLabel lblMaNV;
    private javax.swing.JTable tableCTHD;
    private javax.swing.JTextField txtMaHD;
    private javax.swing.JTextField txtMaKH;
    private javax.swing.JTextField txtMaNV;
    // End of variables declaration//GEN-END:variables
}
