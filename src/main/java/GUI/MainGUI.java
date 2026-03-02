
package GUI;

import DTO.TaiKhoan;
import GUI.Menu.EventMenu;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 *
 * @author letan
 */
public class MainGUI extends javax.swing.JFrame {

    private TaiKhoan account = new TaiKhoan();
    private int previousState = JFrame.NORMAL; // Lưu trạng thái trước khi minimize
    private int x, y; // Tọa độ cho drag

    // Variables for resizing
    private static final int BORDER_SIZE = 5;
    private Point mouseDownCompCoords = null;
    private int resizeDirection = -1;
    private java.awt.event.MouseAdapter resizeListener;

    private static final int RESIZE_NONE = -1;
    private static final int RESIZE_TOP = 0;
    private static final int RESIZE_BOTTOM = 1;
    private static final int RESIZE_LEFT = 2;
    private static final int RESIZE_RIGHT = 3;
    private static final int RESIZE_TOP_LEFT = 4;
    private static final int RESIZE_TOP_RIGHT = 5;
    private static final int RESIZE_BOTTOM_LEFT = 6;
    private static final int RESIZE_BOTTOM_RIGHT = 7;

    // MenuDrop
    private javax.swing.JPopupMenu popupMenuDrop;
    private GUI.Menu.MenuDrop menuDropPanel;

    // Cache panel để tránh tạo lại
    private int currentPanelIndex = -1;
    private javax.swing.JPanel currentPanel = null;

    public void initGUI() {
        setLocationRelativeTo(null);
        initTopSideMoving();
        initResizable();
        // initMenuDrop();
        txtTest.setText(account.getMaNhanVien());

        menu1.initMoving(this);
        menu1.addEventMenu(new EventMenu() {
            @Override
            public void menuIndexChange(int index) {

                GUI.Menu.Model_Menu menuItem = menu1.getMenuItem(index);

                lbCN.setText("  " + menuItem.getName());
                lbCN.setIcon(menuItem.toIconSelected());

                showPanel(index);
            }
        });
        lbMenuDrop.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                toggleMenuDrop();
            }
        });
    }

    private javax.swing.JPanel createPanel(int index) {
        switch (index) {
            case 0:
                return new TourPanel();
            case 1:
                return new LichTrinhPanel();
            case 2:
                return new DiaDiemPanel();
            case 9:
                return new PhanQuyenPanel();
            // case 3:
            // return new PhuongTienPanel();
            // case 4:
            // return new HuongDanVienPanel();
            default:
                return null;
        }
    }

    private void showPanel(int index) {

        if (currentPanelIndex == index && currentPanel != null) {
            return;
        }

        JPanel panel = createPanel(index);

        mainSide.removeAll();

        if (panel != null) {
            // Lưu panel hiện tại
            currentPanel = panel;
            currentPanelIndex = index;

            // Add panel mới
            mainSide.setLayout(new java.awt.BorderLayout());
            mainSide.add(panel, java.awt.BorderLayout.CENTER);
            // Bổ sung listener resize cho panel mới
            addResizeSupport(panel);
        } else {
            // Reset current panel
            currentPanel = null;
            currentPanelIndex = -1;

            JLabel lblComingSoon = new javax.swing.JLabel("Chức năng đang phát triển...");
            lblComingSoon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lblComingSoon.setFont(new java.awt.Font("Segoe UI", 0, 18));
            lblComingSoon.setForeground(new java.awt.Color(100, 100, 100));
            mainSide.setLayout(new java.awt.BorderLayout());
            mainSide.add(lblComingSoon, java.awt.BorderLayout.CENTER);
            addResizeSupport(lblComingSoon);
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

    public void fullscreen() {
        previousState = this.getExtendedState();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public void minimizeWindow() {
        int currentState = this.getExtendedState();
        if (currentState != JFrame.ICONIFIED) {
            previousState = currentState;
        }
        this.setExtendedState(JFrame.ICONIFIED);
    }

    private void initResizable() {
        resizeListener = new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                mouseDownCompCoords = e.getPoint();
                resizeDirection = getResizeDirectionFromScreen(e);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                mouseDownCompCoords = null;
                resizeDirection = RESIZE_NONE;
                setCursor(Cursor.getDefaultCursor());
            }

            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int direction = getResizeDirectionFromScreen(e);
                setCursorForDirection(direction);
            }

            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                if (mouseDownCompCoords != null && resizeDirection != RESIZE_NONE) {
                    handleResize(e);
                }
            }
        };

        addResizeSupport(this);
    }

    private void addResizeSupport(java.awt.Component comp) {
        if (comp == null || resizeListener == null) {
            return;
        }

        if (comp instanceof javax.swing.JTabbedPane
                || comp instanceof javax.swing.AbstractButton
                || comp instanceof javax.swing.JTable
                || comp instanceof javax.swing.JScrollPane
                || comp instanceof javax.swing.JScrollBar
                || comp instanceof javax.swing.JViewport
                || comp instanceof javax.swing.text.JTextComponent
                || comp instanceof javax.swing.JComboBox
                || comp instanceof javax.swing.JList) {
            return;
        }
        comp.addMouseListener(resizeListener);
        comp.addMouseMotionListener(resizeListener);
        if (comp instanceof java.awt.Container) {
            for (java.awt.Component child : ((java.awt.Container) comp).getComponents()) {
                addResizeSupport(child);
            }
        }
    }

    private int getResizeDirectionFromScreen(java.awt.event.MouseEvent e) {
        Point screen = e.getLocationOnScreen();
        int fx = screen.x - getX();
        int fy = screen.y - getY();
        int fw = getWidth();
        int fh = getHeight();

        if (fx < BORDER_SIZE && fy < BORDER_SIZE) {
            return RESIZE_TOP_LEFT;
        } else if (fx > fw - BORDER_SIZE && fy < BORDER_SIZE) {
            return RESIZE_TOP_RIGHT;
        } else if (fx < BORDER_SIZE && fy > fh - BORDER_SIZE) {
            return RESIZE_BOTTOM_LEFT;
        } else if (fx > fw - BORDER_SIZE && fy > fh - BORDER_SIZE) {
            return RESIZE_BOTTOM_RIGHT;
        } else if (fy < BORDER_SIZE) {
            return RESIZE_TOP;
        } else if (fy > fh - BORDER_SIZE) {
            return RESIZE_BOTTOM;
        } else if (fx < BORDER_SIZE) {
            return RESIZE_LEFT;
        } else if (fx > fw - BORDER_SIZE) {
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
        txtTest = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(860, 720));
        setUndecorated(true);
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
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1179,
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

        txtTest.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtTest.setText("jLabel1");

        javax.swing.GroupLayout mainSideLayout = new javax.swing.GroupLayout(mainSide);
        mainSide.setLayout(mainSideLayout);
        mainSideLayout.setHorizontalGroup(
                mainSideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainSideLayout.createSequentialGroup()
                                .addGap(139, 139, 139)
                                .addComponent(txtTest, javax.swing.GroupLayout.PREFERRED_SIZE, 115,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(1096, Short.MAX_VALUE)));
        mainSideLayout.setVerticalGroup(
                mainSideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainSideLayout.createSequentialGroup()
                                .addGap(126, 126, 126)
                                .addComponent(txtTest, javax.swing.GroupLayout.PREFERRED_SIZE, 37,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(757, Short.MAX_VALUE)));

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
    private javax.swing.JLabel txtTest;
    // End of variables declaration//GEN-END:variables
}
