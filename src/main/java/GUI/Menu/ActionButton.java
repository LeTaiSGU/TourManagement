/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.Menu;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

public class ActionButton extends JButton {
    private Color colorTop = new Color(52, 152, 219);
    private Color colorBottom = new Color(41, 128, 185);
    private int radius = 20;

    private boolean hover;
    private boolean pressed;

    public ActionButton() {
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setPreferredSize(new Dimension(120, 40));

        initMouse();
    }

    private void initMouse() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                pressed = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                pressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color top = colorTop;
        Color bottom = colorBottom;

        if (pressed) {
            top = top.darker();
            bottom = bottom.darker();
        } else if (hover) {
            top = top.brighter();
            bottom = bottom.brighter();
        }

        // nền
        GradientPaint gp = new GradientPaint(0, 0, top, 0, getHeight(), bottom);
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        // ==== TEXT CENTER CHUẨN ====
        FontMetrics fm = g2.getFontMetrics(getFont());
        String text = getText();

        int textWidth = fm.stringWidth(text);

        // Căn giữa text theo cả chiều ngang và chiều dọc
        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() - fm.getAscent() - fm.getDescent()) / 2 + fm.getAscent();

        g2.setFont(getFont());
        g2.setColor(getForeground());
        g2.drawString(text, x, y);

        g2.dispose();
    }

    /* ================= PROPERTIES ================= */

    public Color getColorTop() {
        return colorTop;
    }

    public void setColorTop(Color colorTop) {
        this.colorTop = colorTop;
        repaint();
    }

    public Color getColorBottom() {
        return colorBottom;
    }

    public void setColorBottom(Color colorBottom) {
        this.colorBottom = colorBottom;
        repaint();
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        repaint();
    }
}
