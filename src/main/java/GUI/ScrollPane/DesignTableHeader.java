package GUI.ScrollPane;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

class DesignTableHeader extends DefaultTableCellRenderer {
    public DesignTableHeader(Color headerColor) {
        setHorizontalAlignment(SwingConstants.CENTER);
        setForeground(Color.WHITE);          
        setBackground(headerColor); 
        setOpaque(true);
        setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(30, 60, 150)));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setFont(new Font("Segoe UI", Font.BOLD, 18));       // ✅ set lại sau super
        return this;
    }
}
