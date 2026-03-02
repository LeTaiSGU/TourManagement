package GUI.LoginForm;

import java.awt.*;
import javax.swing.Icon;

/**
 * Helper class để tạo các icon cho TextField
 */
public class IconHelper {
    
    /**
     * Tạo icon người dùng (user icon)
     */
    public static Icon createUserIcon() {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(150, 150, 150));
                
                // Vẽ đầu người
                g2.fillOval(x + 5, y + 2, 10, 10);
                
                // Vẽ thân người
                g2.fillArc(x, y + 8, 20, 14, 0, -180);
                
                g2.dispose();
            }
            
            @Override
            public int getIconWidth() {
                return 20;
            }
            
            @Override
            public int getIconHeight() {
                return 20;
            }
        };
    }
    
    /**
     * Tạo icon khóa (lock icon)
     */
    public static Icon createLockIcon() {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(150, 150, 150));
                g2.setStroke(new BasicStroke(2));
                
                // Vẽ thân khóa
                g2.fillRoundRect(x + 4, y + 10, 12, 9, 3, 3);
                
                // Vẽ móc khóa
                g2.drawArc(x + 6, y + 3, 8, 8, 0, 180);
                
                g2.dispose();
            }
            
            @Override
            public int getIconWidth() {
                return 20;
            }
            
            @Override
            public int getIconHeight() {
                return 20;
            }
        };
    }
    
    /**
     * Tạo icon email
     */
    public static Icon createEmailIcon() {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(150, 150, 150));
                g2.setStroke(new BasicStroke(1.5f));
                
                // Vẽ hình chữ nhật (thư)
                g2.drawRect(x + 2, y + 5, 16, 11);
                
                // Vẽ nắp thư
                g2.drawLine(x + 2, y + 5, x + 10, y + 11);
                g2.drawLine(x + 18, y + 5, x + 10, y + 11);
                
                g2.dispose();
            }
            
            @Override
            public int getIconWidth() {
                return 20;
            }
            
            @Override
            public int getIconHeight() {
                return 20;
            }
        };
    }
    
    /**
     * Tạo icon điện thoại
     */
    public static Icon createPhoneIcon() {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(150, 150, 150));
                g2.setStroke(new BasicStroke(1.5f));
                
                // Vẽ thân điện thoại
                g2.drawRoundRect(x + 5, y + 2, 10, 16, 3, 3);
                
                // Vẽ nút home
                g2.fillOval(x + 8, y + 15, 4, 4);
                
                g2.dispose();
            }
            
            @Override
            public int getIconWidth() {
                return 20;
            }
            
            @Override
            public int getIconHeight() {
                return 20;
            }
        };
    }
}
