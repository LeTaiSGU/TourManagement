package GUI.Menu;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

public class CustomComboBox<E> extends JComboBox<E> {

    private Color borderColor = new Color(200, 200, 200);
    private Color focusBorderColor = new Color(52, 152, 219);
    private Color backgroundColor = Color.WHITE;
    private Color arrowColor = new Color(100, 100, 100);
    private int radius = 20;
    private boolean isFocused = false;

    public CustomComboBox() {
        init();
    }

    public CustomComboBox(E[] items) {
        super(items);
        init();
    }

    public CustomComboBox(DefaultComboBoxModel<E> model) {
        super(model);
        init();
    }

    private void init() {
        setOpaque(false);
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setForeground(new Color(50, 50, 50));
        setBackground(backgroundColor);
        setPreferredSize(new Dimension(250, 45));
        setFocusable(true);
        updateBorder();
        applyUI();
        initFocusListener();
    }

    private void updateBorder() {
        setBorder(new EmptyBorder(0, 15, 0, 36));
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

    private void applyUI() {
        setUI(new BasicComboBoxUI() {

            @Override
            protected JButton createArrowButton() {
                // Trả về button trong suốt — mũi tên tự vẽ trong paintComponent
                JButton btn = new JButton() {
                    @Override
                    public void paint(Graphics g) {
                    }
                };
                btn.setOpaque(false);
                btn.setContentAreaFilled(false);
                btn.setBorderPainted(false);
                btn.setPreferredSize(new Dimension(0, 0));
                return btn;
            }

            @Override
            protected ComboPopup createPopup() {
                BasicComboPopup popup = new BasicComboPopup(comboBox) {
                    @Override
                    protected void configurePopup() {
                        super.configurePopup();
                        setBorderPainted(false);
                        setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
                    }

                    @Override
                    protected JScrollPane createScroller() {
                        JScrollPane scroller = super.createScroller();
                        scroller.setBorder(BorderFactory.createEmptyBorder());
                        return scroller;
                    }
                };

                // Bo góc popup bằng cách wrap vào panel custom
                popup.setBackground(Color.WHITE);
                popup.getList().setFont(new Font("Segoe UI", Font.PLAIN, 14));
                popup.getList().setSelectionBackground(new Color(52, 152, 219, 40));
                popup.getList().setSelectionForeground(new Color(50, 50, 50));
                popup.getList().setBorder(new EmptyBorder(2, 8, 2, 8));
                popup.getList().setFixedCellHeight(34);

                popup.getList().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        popup.getList().repaint();
                    }
                });

                return popup;
            }

            @Override
            public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                // Không vẽ gì — nền đã vẽ trong paintComponent của combobox
            }

            @Override
            public void paint(Graphics g, JComponent c) {
                // Chỉ vẽ selected item text — không vẽ nền mặc định
                paintCurrentValue(g, rectangleForCurrentValue(), hasFocus);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Nền bo tròn
        g2.setColor(isEnabled() ? getBackground() : new Color(240, 240, 240));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.dispose();

        super.paintComponent(g);

        paintArrow(g);
    }

    private void paintArrow(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(isEnabled() ? arrowColor : new Color(180, 180, 180));
        g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        int cx = getWidth() - 18;
        int cy = getHeight() / 2;
        int aw = 7;
        int ah = 4;

        g2.drawLine(cx - aw, cy - ah / 2, cx, cy + ah / 2);
        g2.drawLine(cx, cy + ah / 2, cx + aw, cy - ah / 2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isFocused || isPopupVisible()) {
            g2.setColor(focusBorderColor);
            g2.setStroke(new BasicStroke(2));
        } else {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1));
        }

        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, radius, radius);
        g2.dispose();
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
        setBackground(backgroundColor);
        repaint();
    }

    public Color getArrowColor() {
        return arrowColor;
    }

    public void setArrowColor(Color arrowColor) {
        this.arrowColor = arrowColor;
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
