/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.Menu;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JButton;

public class ActionButton extends JButton {
    private Color colorTop = new Color(52, 152, 219);
    private Color colorBottom = new Color(41, 128, 185);
    private int radius = 20;
    private Icon icon;
    private int iconGap = 8;

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

        // Vẽ nền
        GradientPaint gp = new GradientPaint(0, 0, top, 0, getHeight(), bottom);
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        // Tính toán kích thước tổng (icon + gap + text)
        FontMetrics fm = g2.getFontMetrics(getFont());
        String text = getText();
        int textWidth = fm.stringWidth(text);

        int iconWidth = 0;
        int totalWidth = textWidth;

        if (icon != null) {
            iconWidth = icon.getIconWidth();
            totalWidth = iconWidth + iconGap + textWidth;
        }

        // Vị trí bắt đầu để căn giữa
        int startX = (getWidth() - totalWidth) / 2;

        // Vẽ icon nếu có
        if (icon != null) {
            int iconY = (getHeight() - icon.getIconHeight()) / 2;
            icon.paintIcon(this, g2, startX, iconY);
            startX += iconWidth + iconGap;
        }

        // Vẽ text
        int textY = (getHeight() - fm.getAscent() - fm.getDescent()) / 2 + fm.getAscent();
        g2.setFont(getFont());
        g2.setColor(getForeground());
        g2.drawString(text, startX, textY);

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

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
        repaint();
    }

    public int getIconGap() {
        return iconGap;
    }

    public void setIconGap(int iconGap) {
        this.iconGap = iconGap;
        repaint();
    }
}
