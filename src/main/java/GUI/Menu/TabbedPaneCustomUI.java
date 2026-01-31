/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.Menu;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 *
 * @author letan
 */
public class TabbedPaneCustomUI extends BasicTabbedPaneUI {
    private final TabbedPaneCustom tab;

    public TabbedPaneCustomUI(TabbedPaneCustom tab) {
        this.tab = tab;
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();
    }

    @Override
    protected Insets getTabInsets(int tabPlacement, int tabIndex) {
        return new Insets(5, 30, 5, 30);
    }

    @Override
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int width, int height,
            boolean isSelected) {

    }

    @Override
    protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex,
            Rectangle iconRect, Rectangle textRect, boolean isSelected) {
    }

    @Override
    protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ background cho tất cả tabs (unselected trước)
        for (int i = tabPane.getTabCount() - 1; i >= 0; i--) {
            if (i != selectedIndex) {
                paintTabBackground(g2, i, false);
            }
        }

        // Vẽ background cho selected tab (vẽ sau để nằm trên)
        if (selectedIndex >= 0) {
            paintTabBackground(g2, selectedIndex, true);
        }

        g2.dispose();

        // Vẽ text và icon cho tất cả tabs
        Graphics2D g2Text = (Graphics2D) g.create();
        g2Text.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int i = 0; i < tabPane.getTabCount(); i++) {
            Rectangle tabRect = getTabBounds(tabPane, i);
            if (tabRect.intersects(g.getClipBounds())) {
                paintTabContent(g2Text, tabPlacement, i, tabRect);
            }
        }

        g2Text.dispose();
    }

    // Vẽ text và icon cho tab
    private void paintTabContent(Graphics2D g2, int tabPlacement, int tabIndex, Rectangle tabRect) {
        String title = tabPane.getTitleAt(tabIndex);
        boolean isSelected = tabIndex == tabPane.getSelectedIndex();

        // Set màu text
        if (isSelected) {
            g2.setColor(Color.WHITE); // Màu trắng cho selected
        } else {
            g2.setColor(Color.BLACK); // Màu đen cho unselected
        }

        // Set font
        g2.setFont(tabPane.getFont());

        // Tính vị trí text (center trong tab)
        java.awt.FontMetrics metrics = g2.getFontMetrics();
        int textWidth = metrics.stringWidth(title);
        int textHeight = metrics.getHeight();
        int textX = tabRect.x + (tabRect.width - textWidth) / 2;
        int textY = tabRect.y + (tabRect.height - textHeight) / 2 + metrics.getAscent();

        // Vẽ text
        g2.drawString(title, textX, textY);

        // Vẽ icon nếu có
        javax.swing.Icon icon = tabPane.getIconAt(tabIndex);
        if (icon != null) {
            int iconX = tabRect.x + (tabRect.width - icon.getIconWidth()) / 2;
            int iconY = tabRect.y + (tabRect.height - icon.getIconHeight()) / 2;
            icon.paintIcon(tabPane, g2, iconX, iconY);
        }
    }

    protected void paintTabBackground(Graphics2D g2, int index, boolean selected) {
        Rectangle rec = getTabBounds(tabPane, index);
        Color color = getTabColor(selected);
        g2.setPaint(new GradientPaint(rec.x, rec.y, color.brighter(), rec.x, rec.y + rec.height, color));
        Shape shape = createTabArea(rec);

        // Vẽ shadow với offset đúng (shadow size = 6, offset theo y để shadow ở dưới)
        if (selected) {
            g2.drawImage(new ShadowRenderer(6, 0.3f, new Color(100, 100, 100)).createShadow(shape), rec.x - 3,
                    rec.y + 2, null);
        }

        g2.fill(shape);
    }

    @Override
    protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int height = calculateTabAreaWidth(tabPlacement, runCount, maxTabHeight) - 1;
        g2.setColor(tabPane.getBackground());
        Area area = new Area(
                new RoundRectangle2D.Double(0, height, tabPane.getWidth(), tabPane.getHeight() - height, 15, 15));
        if (selectedIndex == 0) {
            area.add(new Area(new Rectangle2D.Double(0, height, 15, 15)));
        }
        g2.fill(area);
        g2.dispose();
    }

    private Shape createTabArea(Rectangle rec) {
        int x = rec.x;
        int y = rec.y;
        int width = x + rec.width;
        int height = y + rec.height;
        int round = 10;
        int curveOut = 15; // Độ cong ra ngoài của tail

        Path2D p = new Path2D.Double();

        // Bắt đầu từ bottom-left
        p.moveTo(x, height);

        // Line lên và curve top-left
        p.lineTo(x, y + round);
        p.curveTo(x, y + round, x, y, x + round, y);

        // Line sang phải ở top
        p.lineTo(width - round, y);

        // Curve top-right (smooth)
        p.curveTo(width - round, y, width, y, width, y + round);

        // Line xuống gần bottom
        p.lineTo(width, height - round * 2);

        // Curve "tail" ra ngoài và xuống - giống hình 2
        // Dùng CubicCurve để tạo smooth curve
        CubicCurve2D curve = new CubicCurve2D.Double(
                width, height - round * 2, // Start point
                width + curveOut / 2, height - round, // Control point 1: ra ngoài một chút
                width + curveOut, height - 5, // Control point 2: ra ngoài và gần bottom
                width + curveOut * 1.5, height // End point: ra ngoài và ở bottom
        );
        p.append(curve, true);

        // Line về left
        p.lineTo(x, height);
        p.closePath();

        return p;
    }

    private Color getTabColor(boolean selected) {
        if (selected) {
            return tab.getSelectedColor();
        } else {
            return tab.getUnselectedColor();
        }
    }
}
