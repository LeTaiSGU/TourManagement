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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Path2D;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 *
 * @author letan
 */
public class Menu extends javax.swing.JPanel {

    public void addEventMenu(EventMenu event) {
        this.event = event;
    }

    private int selectedIndex = -1;
    private final Timer timer;
    private boolean toUp; // Thời gian chuyển động đi lên
    private int menuYTarget;
    int menuY;
    private int speed = 2;
    private EventMenuCallBack callBack;
    private EventMenu event;

    public Menu() {
        initComponents();
        setOpaque(false);
        listMenu.setOpaque(false);
        lbLogo.setOpaque(false);
        listMenu.addEventSelectedMenu(new EventMenuSelected() {
            @Override
            public void menuSelected(int index, EventMenuCallBack callBack) {
                if (index != selectedIndex) {
                    Menu.this.callBack = callBack;
                    toUp = selectedIndex > index;
                    if (selectedIndex == -1) {
                        speed = 20;
                    } else {
                        speed = selectedIndex - index;
                        if (speed < 0) {
                            speed *= -1;
                            // If speed valus <0 change it to <0 Ex : -1 to 1
                        }
                    }
                    speed++; // Add 1 speed
                    selectedIndex = index;
                    menuYTarget = selectedIndex * 50 + listMenu.getY(); // menuYTarget is location y
                    if (!timer.isRunning()) {
                        timer.start();
                    }
                }
            }
        });
        timer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (toUp) {
                    if (menuY <= menuYTarget - 5) {
                        menuY = menuYTarget;
                        repaint();
                        timer.stop();
                        callBack.call(selectedIndex);
                        if (event != null) {
                            event.menuIndexChange(selectedIndex);
                        }
                    } else {
                        menuY -= speed;
                        repaint();
                    }
                } else {
                    if (menuY >= menuYTarget + 5) { // Add style
                        menuY = menuYTarget;
                        repaint();
                        timer.stop();
                        callBack.call(selectedIndex);
                        if (event != null) {
                            event.menuIndexChange(selectedIndex);
                        }
                    } else {
                        menuY += speed;
                        repaint();
                    }
                }
            }
        });
        initData();
    }

    private void initData() {
        listMenu.addItem(new Model_Menu("1", "Tour", Model_Menu.MenuType.MENU));
        listMenu.addItem(new Model_Menu("2", "Lịch trình", Model_Menu.MenuType.MENU));
        listMenu.addItem(new Model_Menu("3", "Địa điểm", Model_Menu.MenuType.MENU));
        listMenu.addItem(new Model_Menu("4", "Phương tiện", Model_Menu.MenuType.MENU));
        listMenu.addItem(new Model_Menu("5", "Hướng dẫn viên", Model_Menu.MenuType.MENU));
        listMenu.addItem(new Model_Menu("6", "Nhân viên", Model_Menu.MenuType.MENU));
        listMenu.addItem(new Model_Menu("7", "Khách hàng", Model_Menu.MenuType.MENU));
        listMenu.addItem(new Model_Menu("8", "Hóa đơn", Model_Menu.MenuType.MENU));
        listMenu.addItem(new Model_Menu("9", "Khuyến mãi", Model_Menu.MenuType.MENU));
        listMenu.addItem(new Model_Menu("10", "Phân quyền", Model_Menu.MenuType.MENU));

    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint g = new GradientPaint(0, 0, Color.decode("#2980B9"), 0, getHeight(), Color.decode("#6DD5FA"));
        g2.setPaint(g);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
        if (selectedIndex >= 0) {
            int menuX = 10;
            int height = 50;
            int width = getWidth();
            g2.setColor(new Color(255, 255, 255));
            g2.fillRoundRect(menuX, menuY, width, height, 50, 50);
            Path2D.Float f = new Path2D.Float();
            f.moveTo(width - 45, menuY);
            f.curveTo(width - 10, menuY, width, menuY, width, menuY - 45);
            f.lineTo(width, menuY + height + 45);
            f.curveTo(width, menuY + height, width - 10, menuY + height, width - 45, menuY + height);
            g2.fill(f);
        }
        super.paintComponent(grphcs);
    }

    private int x;
    private int y;

    public void initMoving(JFrame fram) {
        lbLogo.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX() + 6;
                y = e.getY() + 6;
            }
        });
        lbLogo.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                fram.setLocation(e.getXOnScreen() - x, e.getYOnScreen() - y);
            }

        });

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbLogo = new javax.swing.JLabel();
        listMenu = new GUI.Menu.ListMenu<>();

        setLayout(new java.awt.BorderLayout());

        lbLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/logo1.png"))); // NOI18N
        lbLogo.setPreferredSize(new java.awt.Dimension(248, 248));
        add(lbLogo, java.awt.BorderLayout.PAGE_START);

        add(listMenu, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lbLogo;
    private GUI.Menu.ListMenu<String> listMenu;
    // End of variables declaration//GEN-END:variables
}
