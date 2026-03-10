/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI.LoginForm;

import BUS.TaiKhoanBUS;
import DTO.TaiKhoan;
import Exception.BusException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Image;
import java.net.URL;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class LoginFrame extends javax.swing.JFrame {

    private TaiKhoanBUS taiKhoanBUS = new TaiKhoanBUS();

    public LoginFrame() {
        initComponents();
        initGUI();
    }

    public void initGUI() {
        setLocationRelativeTo(null);
        setImage(image, "/image/icon/Untitled.png");
        lb1.setHorizontalAlignment(SwingConstants.CENTER);
        lb1.setVerticalAlignment(SwingConstants.CENTER);

        txtTendangnhap.setPlaceholder("Nhập tên đăng nhập");
        txtMatkhau.setPlaceholder("Nhập mật khẩu");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        image = new javax.swing.JLabel();
        loginPanel = new javax.swing.JPanel();
        login1 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        lb1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtTendangnhap = new GUI.LoginForm.CustomTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtMatkhau = new GUI.LoginForm.CustomPasswordField();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        btnDangnhap = new GUI.Menu.ActionButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lbSuco = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1000, 750));

        image.setPreferredSize(new java.awt.Dimension(600, 750));
        getContentPane().add(image, java.awt.BorderLayout.LINE_START);

        loginPanel.setBackground(new java.awt.Color(255, 255, 255));
        loginPanel.setLayout(new java.awt.BorderLayout());

        login1.setBackground(new java.awt.Color(255, 255, 255));
        login1.setPreferredSize(new java.awt.Dimension(607, 220));
        login1.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.BorderLayout());

        lb1.setBackground(new java.awt.Color(255, 255, 255));
        lb1.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lb1.setText("Chào mừng bạn quay lại");
        lb1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel1.add(lb1, java.awt.BorderLayout.CENTER);

        login1.add(jPanel1, java.awt.BorderLayout.PAGE_END);

        loginPanel.add(login1, java.awt.BorderLayout.PAGE_START);

        jPanel3.setPreferredSize(new java.awt.Dimension(607, 300));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setPreferredSize(new java.awt.Dimension(607, 300));
        jPanel5.setLayout(new java.awt.GridLayout(2, 0));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.GridLayout(2, 0));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel4.setLayout(new java.awt.GridLayout(2, 0));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setText("Tên đăng nhập");
        jPanel4.add(jLabel1);

        txtTendangnhap.setText("NV001");
        txtTendangnhap.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTendangnhap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/user.png"))); // NOI18N
        txtTendangnhap.setIconGap(15);
        jPanel4.add(txtTendangnhap);

        jPanel2.add(jPanel4);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel6.setLayout(new java.awt.GridLayout(2, 0));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel2.setText("Mật khẩu");
        jPanel6.add(jLabel2);

        txtMatkhau.setText("12345678q");
        txtMatkhau.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/lock.png"))); // NOI18N
        txtMatkhau.setIconGap(15);
        txtMatkhau.addActionListener(this::txtMatkhauActionPerformed);
        jPanel6.add(txtMatkhau);

        jPanel2.add(jPanel6);

        jPanel5.add(jPanel2);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setLayout(new java.awt.GridLayout(2, 0));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel8.setLayout(new java.awt.GridLayout(2, 0));

        btnDangnhap.setBorder(null);
        btnDangnhap.setText("Đăng nhập");
        btnDangnhap.setColorBottom(new java.awt.Color(19, 127, 236));
        btnDangnhap.setColorTop(new java.awt.Color(19, 127, 236));
        btnDangnhap.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnDangnhap.addActionListener(this::btnDangnhapActionPerformed);
        jPanel8.add(btnDangnhap);

        jPanel7.add(jPanel8);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel10.setLayout(new java.awt.GridLayout(2, 0));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel3.setText("Bạn gặp sự cố khi đang nhập");
        jPanel10.add(jLabel3);

        lbSuco.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbSuco.setText("Liên hệ với kĩ thuật viên tại đây");
        jPanel10.add(lbSuco);

        jPanel7.add(jPanel10);

        jPanel5.add(jPanel7);

        jPanel3.add(jPanel5, java.awt.BorderLayout.CENTER);

        loginPanel.add(jPanel3, java.awt.BorderLayout.CENTER);

        getContentPane().add(loginPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtMatkhauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMatkhauActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMatkhauActionPerformed

    private void btnDangnhapActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnDangnhapActionPerformed
        try {
            TaiKhoan acc = taiKhoanBUS.login(txtTendangnhap.getText(), new String(txtMatkhau.getPassword()));
            if (!acc.getTrangThai()){
                JOptionPane.showMessageDialog(this, "Tài khoản đã bị khóa. Vui lòng gọi hỗ trợ để mở khóa");
            }
            else {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
                GUI.MainGUI main = new GUI.MainGUI(acc);
                main.setVisible(true);
                this.dispose();
            }
        } catch (BusException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }// GEN-LAST:event_btnDangnhapActionPerformed

    public void setImage(JLabel label, String resourcePath) {
        URL imgURL = getClass().getResource(resourcePath);

        if (imgURL == null) {
            System.out.println("Không tìm thấy ảnh: " + resourcePath);
            return;
        }

        ImageIcon icon = new ImageIcon(imgURL);
        Image img = icon.getImage();

        // Scale ảnh theo kích thước hiện tại của JLabel
        Image scaledImg = img.getScaledInstance(
                label.getWidth(),
                label.getHeight(),
                Image.SCALE_SMOOTH);

        label.setIcon(new ImageIcon(scaledImg));
    }

    public static void main(String args[]) {

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.awt.EventQueue.invokeLater(() -> new LoginFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private GUI.Menu.ActionButton btnDangnhap;
    private javax.swing.JLabel image;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel lb1;
    private javax.swing.JLabel lbSuco;
    private javax.swing.JPanel login1;
    private javax.swing.JPanel loginPanel;
    private GUI.LoginForm.CustomPasswordField txtMatkhau;
    private GUI.LoginForm.CustomTextField txtTendangnhap;
    // End of variables declaration//GEN-END:variables
}
