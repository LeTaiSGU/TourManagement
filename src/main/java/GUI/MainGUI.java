
package GUI;

import DTO.CTCN_NQ;
import DTO.TaiKhoan;
import Exception.BusException;
import GUI.Menu.EventMenu;
import javax.swing.UIManager;

import BUS.CTCN_NQBUS;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class MainGUI extends javax.swing.JFrame {

    private CTCN_NQBUS ctcnnqbus = new CTCN_NQBUS();
    private TaiKhoan account = new TaiKhoan();
    private int previousState = JFrame.NORMAL; // Lưu trạng thái trước khi minimize
    private int x, y; // Tọa độ cho drag

    // MenuDrop
    private javax.swing.JPopupMenu popupMenuDrop;
    private GUI.Menu.MenuDrop menuDropPanel;

    // Cache panel để tránh tạo lại
    private String currentPanelKey = null;
    private javax.swing.JPanel currentPanel = null;

    // Resize
    private static final int RESIZE_BORDER = 8;
    private AWTEventListener resizeListener;
    private boolean resizingRight, resizingBottom;
    private int resizeStartX, resizeStartY, resizeStartW, resizeStartH;

    private void initResizable() {
        if (resizeListener != null) {
            return;
        }
        resizeListener = new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                if (!isShowing()) {
                    return;
                }
                if (getExtendedState() == JFrame.MAXIMIZED_BOTH)
                    return;
                MouseEvent e = (MouseEvent) event;
                Point p;
                Point frameLocation;
                try {
                    p = e.getLocationOnScreen();
                    frameLocation = getLocationOnScreen();
                } catch (IllegalComponentStateException ex) {
                    return;
                }
                int fx = p.x - frameLocation.x;
                int fy = p.y - frameLocation.y;
                boolean onRight = fx >= getWidth() - RESIZE_BORDER && fx <= getWidth();
                boolean onBottom = fy >= getHeight() - RESIZE_BORDER && fy <= getHeight();

                switch (e.getID()) {
                    case MouseEvent.MOUSE_MOVED:
                        if (onRight && onBottom)
                            setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                        else if (onRight)
                            setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                        else if (onBottom)
                            setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
                        else
                            setCursor(Cursor.getDefaultCursor());
                        break;

                    case MouseEvent.MOUSE_PRESSED:
                        resizingRight = onRight;
                        resizingBottom = onBottom;
                        if (resizingRight || resizingBottom) {
                            resizeStartX = p.x;
                            resizeStartY = p.y;
                            resizeStartW = getWidth();
                            resizeStartH = getHeight();
                        }
                        break;

                    case MouseEvent.MOUSE_DRAGGED:
                        if (!resizingRight && !resizingBottom)
                            break;
                        int dx = p.x - resizeStartX;
                        int dy = p.y - resizeStartY;
                        int newW = resizingRight ? resizeStartW + dx : getWidth();
                        int newH = resizingBottom ? resizeStartH + dy : getHeight();
                        setSize(Math.max(newW, getMinimumSize().width),
                                Math.max(newH, getMinimumSize().height));
                        break;

                    case MouseEvent.MOUSE_RELEASED:
                        resizingRight = false;
                        resizingBottom = false;
                        break;
                }
            }
        };
        Toolkit.getDefaultToolkit().addAWTEventListener(resizeListener,
                AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
    }

    @Override
    public void dispose() {
        if (resizeListener != null) {
            Toolkit.getDefaultToolkit().removeAWTEventListener(resizeListener);
            resizeListener = null;
        }
        super.dispose();
    }

    public void initGUI() {
        setLocationRelativeTo(null);
        initResizable();
        mainSide.setLayout(new BorderLayout());
        // initMenuDrop();

        menu1.initMoving(this);
        List<CTCN_NQ> ctcnnq = new ArrayList();
        try {
            ctcnnq = ctcnnqbus.getCTCN_NQbyMa(account.getMaNhomQuyen());
        } catch (BusException e) {
            JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
        }
        for (CTCN_NQ ct : ctcnnq) {
            System.out.println(ct.getMaCN() + "\n");
        }
        menu1.initData(ctcnnq);
        menu1.addEventMenu(new EventMenu() {
            @Override
            public void menuIndexChange(int index) {

                GUI.Menu.Model_Menu menuItem = menu1.getMenuItem(index);

                lbCN.setText("  " + menuItem.getName());
                lbCN.setIcon(menuItem.toIconSelected());

                showPanel(menuItem.getIcon());
            }
        });

        // Click logo → hiển thị Dashboard (ThongKePanel), không thuộc menu item nào
        menu1.setLogoClickAction(() -> {
            lbCN.setText("  Dashboard");
            lbCN.setIcon(null);
            mainSide.removeAll();
            mainSide.setLayout(new java.awt.BorderLayout());
            mainSide.add(new ThongKePanel(), java.awt.BorderLayout.CENTER);
            mainSide.revalidate();
            mainSide.repaint();
            currentPanel = null;
            // currentPanelIndex = -1;
        });

        lbMenuDrop.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                toggleMenuDrop();
            }
        });
    }

    private javax.swing.JPanel createPanel(String menuKey) {
        if (menuKey == null) {
            return null;
        }
        switch (menuKey) {
            case "1":
                return new TourPanel();
            case "2":
                return new LichTrinhPanel();
            case "3":
                return new DiaDiemPanel();
            case "4": 
                return new PhuongTienPanel();
            case "5": 
                return new HuongDanVienPanel();
            case "6": 
            return new NhanVienPanel();
            case "7": 
            return new KhachHangPanel();
            case "9": // Khuyến mãi
                // Chưa có panel tương ứng, hiển thị "đang phát triển"
                return null;
            case "8":
                return new HoaDonPanel(account.getMaNhanVien());
            case "10":
                return new PhanQuyenPanel();
            case "13":
                return new ThongKeBaoCaoPanel();
            default:
                return null;
        }
    }

    private void showPanel(String menuKey) {

        if (menuKey != null && menuKey.equals(currentPanelKey) && currentPanel != null) {
            return;
        }

        JPanel panel = createPanel(menuKey);

        mainSide.removeAll();

        if (panel != null) {
            // Lưu panel hiện tại
            currentPanel = panel;
            currentPanelKey = menuKey;

            // Add panel mới
            mainSide.setLayout(new java.awt.BorderLayout());
            mainSide.add(panel, java.awt.BorderLayout.CENTER);
            // Bổ sung listener resize cho panel mới

        } else {
            // Reset current panel
            currentPanel = null;
            currentPanelKey = null;

            JLabel lblComingSoon = new javax.swing.JLabel("Chức năng đang phát triển...");
            lblComingSoon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lblComingSoon.setFont(new java.awt.Font("Segoe UI", 0, 18));
            lblComingSoon.setForeground(new java.awt.Color(100, 100, 100));
            mainSide.setLayout(new java.awt.BorderLayout());
            mainSide.add(lblComingSoon, java.awt.BorderLayout.CENTER);

        }

        // Refresh UI
        mainSide.revalidate();
        mainSide.repaint();
    }

    private void toggleMenuDrop() {
        if (popupMenuDrop == null) {
            menuDropPanel = new GUI.Menu.MenuDrop(this, account.getMaNhanVien());
            popupMenuDrop = new javax.swing.JPopupMenu();
            popupMenuDrop.setBorder(null);
            popupMenuDrop.setLayout(new java.awt.BorderLayout());
            popupMenuDrop.add(menuDropPanel, java.awt.BorderLayout.CENTER);
        }

        if (popupMenuDrop.isVisible()) {
            popupMenuDrop.setVisible(false);
        } else {
            popupMenuDrop.show(lbMenuDrop, 0, lbMenuDrop.getHeight());
        }
    }

    private java.awt.Rectangle previousBounds;

    public void fullscreen() {
        if (this.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
            this.setExtendedState(JFrame.NORMAL);
            if (previousBounds != null) {
                this.setBounds(previousBounds);
            }
        } else {
            previousBounds = this.getBounds(); // lưu size & vị trí hiện tại
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    }

    public void minimizeWindow() {
        int currentState = this.getExtendedState();
        if (currentState != JFrame.ICONIFIED) {
            previousState = currentState;
        }
        this.setExtendedState(JFrame.ICONIFIED);
    }

    public MainGUI(TaiKhoan tk) {
        initComponents();
        account = tk;
        SwingUtilities.invokeLater(() -> {
            initGUI();
        });
    }

    @SuppressWarnings("unchecked")

    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        leftSide = new javax.swing.JPanel();
        menu1 = new GUI.Menu.Menu();
        jPanel2 = new javax.swing.JPanel();
        topSide = new javax.swing.JPanel();
        btnExit = new javax.swing.JLabel();
        btnFullscreen = new javax.swing.JLabel();
        btnMinimize = new javax.swing.JLabel();
        lbCN = new javax.swing.JLabel();
        lbMenuDrop = new javax.swing.JLabel();
        mainSide = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(860, 720));
        setSize(new java.awt.Dimension(1600, 1000));

        jPanel1.setBackground(new java.awt.Color(245, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(250, 820));
        jPanel1.setLayout(new java.awt.BorderLayout());

        leftSide.setBackground(new java.awt.Color(255, 255, 255));
        leftSide.setLayout(new java.awt.BorderLayout());
        leftSide.add(menu1, java.awt.BorderLayout.CENTER);

        jPanel1.add(leftSide, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.LINE_START);

        jPanel2.setBackground(new java.awt.Color(246, 247, 248));
        jPanel2.setLayout(new java.awt.BorderLayout());

        topSide.setBackground(new java.awt.Color(41, 128, 185));
        topSide.setPreferredSize(new java.awt.Dimension(1150, 80));

        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/close.png"))); // NOI18N
        btnExit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnExitMouseClicked(evt);
            }
        });

        btnFullscreen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/expand.png"))); // NOI18N
        btnFullscreen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFullscreen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnFullscreenMouseClicked(evt);
            }
        });

        btnMinimize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/minus-sign.png"))); // NOI18N
        btnMinimize.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMinimize.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMinimizeMouseClicked(evt);
            }
        });

        lbCN.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbCN.setForeground(new java.awt.Color(255, 255, 255));

        lbMenuDrop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/menu.png"))); // NOI18N
        lbMenuDrop.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout topSideLayout = new javax.swing.GroupLayout(topSide);
        topSide.setLayout(topSideLayout);
        topSideLayout.setHorizontalGroup(
                topSideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, topSideLayout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(lbMenuDrop)
                                .addGap(18, 18, 18)
                                .addComponent(lbCN)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1176,
                                        Short.MAX_VALUE)
                                .addComponent(btnMinimize)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnFullscreen)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnExit)
                                .addContainerGap()));
        topSideLayout.setVerticalGroup(
                topSideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(topSideLayout.createSequentialGroup()
                                .addGroup(topSideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(topSideLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(topSideLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(btnMinimize)
                                                        .addComponent(btnFullscreen)
                                                        .addComponent(btnExit)))
                                        .addGroup(topSideLayout.createSequentialGroup()
                                                .addGap(28, 28, 28)
                                                .addGroup(topSideLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lbCN)
                                                        .addComponent(lbMenuDrop))))
                                .addContainerGap(28, Short.MAX_VALUE)));

        jPanel2.add(topSide, java.awt.BorderLayout.PAGE_START);

        mainSide.setBackground(new java.awt.Color(255, 255, 255));
        mainSide.setName(""); // NOI18N
        mainSide.setRequestFocusEnabled(false);
        mainSide.setRequestFocusEnabled(false);

        javax.swing.GroupLayout mainSideLayout = new javax.swing.GroupLayout(mainSide);
        mainSide.setLayout(mainSideLayout);
        mainSideLayout.setHorizontalGroup(
                mainSideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1350, Short.MAX_VALUE));
        mainSideLayout.setVerticalGroup(
                mainSideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 920, Short.MAX_VALUE));

        jPanel2.add(mainSide, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFullscreenMouseClicked(java.awt.event.MouseEvent evt) {
        fullscreen();
    }

    private void btnMinimizeMouseClicked(java.awt.event.MouseEvent evt) {
        minimizeWindow();
    }

    private void btnExitMouseClicked(java.awt.event.MouseEvent evt) {
        this.dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnExit;
    private javax.swing.JLabel btnFullscreen;
    private javax.swing.JLabel btnMinimize;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lbCN;
    private javax.swing.JLabel lbMenuDrop;
    private javax.swing.JPanel leftSide;
    private javax.swing.JPanel mainSide;
    private GUI.Menu.Menu menu1;
    private javax.swing.JPanel topSide;
    // End of variables declaration//GEN-END:variables
}
