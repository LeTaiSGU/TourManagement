package GUI.LoginForm;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class CustomPasswordField extends JPasswordField {

    private String placeholder = "";
    private Icon icon;
    private Color placeholderColor = new Color(160, 160, 160);
    private Color borderColor = new Color(200, 200, 200);
    private Color focusBorderColor = new Color(52, 152, 219);
    private Color backgroundColor = Color.WHITE;
    private int radius = 20;
    private int iconGap = 5;
    private boolean isFocused = false;

    private boolean showPassword = false;
    private boolean eyeHovered = false;
    private static final int EYE_SIZE = 22;
    private static final int EYE_RIGHT_MARGIN = 12;

    public CustomPasswordField() {
        setOpaque(false);
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setForeground(new Color(50, 50, 50));
        setBackground(backgroundColor);
        setPreferredSize(new Dimension(250, 45));
        setEchoChar('•');
        updateBorder();
        initFocusListener();
        initEyeListener();
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

    private void initEyeListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isInsideEye(e.getPoint())) {
                    toggleShowPassword();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                eyeHovered = false;
                setCursor(Cursor.getDefaultCursor());
                repaint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                boolean inside = isInsideEye(e.getPoint());
                if (inside != eyeHovered) {
                    eyeHovered = inside;
                    setCursor(inside ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                            : Cursor.getDefaultCursor());
                    repaint();
                }
            }
        });
    }

    private boolean isInsideEye(Point p) {
        Rectangle eyeRect = getEyeRect();
        return eyeRect.contains(p);
    }

    private Rectangle getEyeRect() {
        int x = getWidth() - EYE_SIZE - EYE_RIGHT_MARGIN;
        int y = (getHeight() - EYE_SIZE) / 2;
        return new Rectangle(x, y, EYE_SIZE, EYE_SIZE);
    }

    private void toggleShowPassword() {
        showPassword = !showPassword;
        setEchoChar(showPassword ? (char) 0 : '•');
        repaint();
    }

    private void updateBorder() {
        int left = 15;
        if (icon != null) {
            left = 15 + icon.getIconWidth() + iconGap;
        }
        setBorder(new EmptyBorder(10, left, 10, EYE_SIZE + EYE_RIGHT_MARGIN + 8));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.dispose();

        super.paintComponent(g);

        if (icon != null) {
            Graphics2D g2Icon = (Graphics2D) g.create();
            g2Icon.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int iconX = 12;
            int iconY = (getHeight() - icon.getIconHeight()) / 2;
            icon.paintIcon(this, g2Icon, iconX, iconY);
            g2Icon.dispose();
        }

        paintEyeIcon(g);
    }

    private void paintEyeIcon(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        Rectangle r = getEyeRect();
        int cx = r.x + r.width / 2;
        int cy = r.y + r.height / 2;

        Color eyeColor = eyeHovered ? new Color(52, 152, 219) : new Color(140, 140, 140);
        g2.setColor(eyeColor);
        g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        if (showPassword) {
            // Mắt mở: vẽ hình elip ngoài + con ngươi
            int ew = 10, eh = 6;
            g2.drawArc(cx - ew, cy - eh, ew * 2, eh * 2, 0, 180);
            g2.drawArc(cx - ew, cy - eh, ew * 2, eh * 2, 180, 180);

            // Con ngươi
            g2.fill(new Ellipse2D.Double(cx - 2.5, cy - 2.5, 5, 5));
        } else {
            // Mắt nhắm: vẽ đường cong trên + đường gạch chéo
            int ew = 10, eh = 6;
            g2.drawArc(cx - ew, cy - eh, ew * 2, eh * 2, 0, 180);

            // Các đường lông mi nhắm
            g2.drawLine(cx - ew + 2, cy + 1, cx - ew, cy + 4);
            g2.drawLine(cx - 3, cy + 3, cx - 3, cy + 6);
            g2.drawLine(cx + 3, cy + 3, cx + 3, cy + 6);
            g2.drawLine(cx + ew - 2, cy + 1, cx + ew, cy + 4);
        }

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

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

        if (getPassword().length == 0 && !placeholder.isEmpty()) {
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

    public boolean isShowPassword() {
        return showPassword;
    }

    public void setShowPassword(boolean showPassword) {
        this.showPassword = showPassword;
        setEchoChar(showPassword ? (char) 0 : '•');
        repaint();
    }
}
