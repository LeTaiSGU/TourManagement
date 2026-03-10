package GUI.ScrollPane;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class DesignTable {
    
    public static void designTable(JTable table, Color headerColor) {
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        DesignTableHeader renderer = new DesignTableHeader(headerColor);
            TableColumnModel columnModel = table.getColumnModel();
            for (int i = 0; i < columnModel.getColumnCount(); i++) {
                TableColumn column = columnModel.getColumn(i);
                column.setHeaderRenderer(renderer);  
            }
        
        for (int column = 0; column < table.getColumnCount(); column++) {
            TableColumn col = columnModel.getColumn(column);
    
            Component headerComp = col.getHeaderRenderer().getTableCellRendererComponent(
                    table, col.getHeaderValue(), false, false, -1, column);
            int width = headerComp.getPreferredSize().width + 20;

            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer2 = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer2, row, column);
                width = Math.max(comp.getPreferredSize().width + 10, width); 
            }

            if (width > 700) width = 700;
            col.setPreferredWidth(width);
        }
    }
}
