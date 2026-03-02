package GUI.LoginForm;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Custom TextField với icon, placeholder và border bo tròn
 * Có thể kéo thả trong NetBeans Form Designer
 */
public class CustomTextField extends JTextField {

    private String placeholder = "";
    private Icon icon;
    private Color placeholderColor = new Color(160, 160, 160);
    private Color borderColor = new Color(200, 200, 200);
    private Color focusBorderColor = new Color(52, 152, 219);
    private Color backgroundColor = Color.WHITE;
    private int radius = 20;
    private int iconGap = 5;
    private boolean isFocused = false;

    public CustomTextField() {
        setOpaque(false);
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setForeground(new Color(50, 50, 50));
        setBackground(backgroundColor);
        setPreferredSize(new Dimension(250, 45));
        updateBorder();

        initFocusListener();
    }

    private void initFocusListener() {
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                isFocused = true;
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                isFocused = false;
                repaint();
            }
        });
    }

    private void updateBorder() {
        int left = 15;
        if (icon != null) {
            left = 15 + icon.getIconWidth() + iconGap;
        }
        setBorder(new EmptyBorder(10, left, 10, 15));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ nền
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        g2.dispose();

        // Vẽ text trước
        super.paintComponent(g);

        // Vẽ icon sau cùng để đảm bảo hiển thị
        if (icon != null) {
            Graphics2D g2Icon = (Graphics2D) g.create();
            g2Icon.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int iconX = 12;
            int iconY = (getHeight() - icon.getIconHeight()) / 2;
            icon.paintIcon(this, g2Icon, iconX, iconY);

            g2Icon.dispose();
        }
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ border
        if (isFocused) {
            g2.setColor(focusBorderColor);
            g2.setStroke(new BasicStroke(2));
        } else {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1));
        }

        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, radius, radius);
        g2.dispose();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Vẽ placeholder nếu text trống
        if (getText().isEmpty() && !placeholder.isEmpty()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(placeholderColor);
            g2.setFont(getFont());

            FontMetrics fm = g2.getFontMetrics();
            int x = getInsets().left;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

            g2.drawString(placeholder, x, y);
            g2.dispose();
        }
    }

    // ============== GETTERS & SETTERS (JavaBean Properties) ==============

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
        updateBorder();
        repaint();
    }

    public Color getPlaceholderColor() {
        return placeholderColor;
    }

    public void setPlaceholderColor(Color placeholderColor) {
        this.placeholderColor = placeholderColor;
        repaint();
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
    }

    public Color getFocusBorderColor() {
        return focusBorderColor;
    }

    public void setFocusBorderColor(Color focusBorderColor) {
        this.focusBorderColor = focusBorderColor;
        repaint();
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        repaint();
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        repaint();
    }

    public int getIconGap() {
        return iconGap;
    }

    public void setIconGap(int iconGap) {
        this.iconGap = iconGap;
        updateBorder();
        repaint();
    }
}
