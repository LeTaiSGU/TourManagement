/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.Menu;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import javax.swing.JOptionPane;

public class MenuDrop extends javax.swing.JPanel {

    /**
     * Creates new form MenuDrop
     */
    public MenuDrop() {
        initComponents();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        paintComponent1 = new GUI.Menu.PaintComponent();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lbChucvu = new javax.swing.JLabel();
        btnChangePass = new javax.swing.JLabel();
        btnChangePass1 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Name");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Chức vụ");

        lbChucvu.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbChucvu.setForeground(new java.awt.Color(255, 255, 255));
        lbChucvu.setText("Chức vụ");

        btnChangePass.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnChangePass.setForeground(new java.awt.Color(255, 255, 255));
        btnChangePass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/padlock.png"))); // NOI18N
        btnChangePass.setText("Change Pass");
        btnChangePass.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnChangePass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnChangePassMouseClicked(evt);
            }
        });

        btnChangePass1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnChangePass1.setForeground(new java.awt.Color(255, 255, 255));
        btnChangePass1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/logout.png"))); // NOI18N
        btnChangePass1.setText("Logout");
        btnChangePass1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnChangePass1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnChangePass1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout paintComponent1Layout = new javax.swing.GroupLayout(paintComponent1);
        paintComponent1.setLayout(paintComponent1Layout);
        paintComponent1Layout.setHorizontalGroup(
            paintComponent1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paintComponent1Layout.createSequentialGroup()
                .addGroup(paintComponent1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paintComponent1Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(jLabel1))
                    .addGroup(paintComponent1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(paintComponent1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paintComponent1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(28, 28, 28)
                                .addComponent(lbChucvu))
                            .addComponent(jLabel2))))
                .addContainerGap(60, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paintComponent1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(paintComponent1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnChangePass1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnChangePass, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );
        paintComponent1Layout.setVerticalGroup(
            paintComponent1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paintComponent1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paintComponent1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lbChucvu))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 118, Short.MAX_VALUE)
                .addComponent(btnChangePass)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnChangePass1)
                .addGap(34, 34, 34))
        );

        add(paintComponent1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnChangePassMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChangePassMouseClicked
        JOptionPane.showMessageDialog(null, "Change Pass word");
    }//GEN-LAST:event_btnChangePassMouseClicked

    private void btnChangePass1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChangePass1MouseClicked
        JOptionPane.showMessageDialog(null, "Logout");
    }//GEN-LAST:event_btnChangePass1MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnChangePass;
    private javax.swing.JLabel btnChangePass1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lbChucvu;
    private GUI.Menu.PaintComponent paintComponent1;
    // End of variables declaration//GEN-END:variables
}
