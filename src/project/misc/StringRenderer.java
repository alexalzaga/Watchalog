package project.misc;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Clase para crear un renderer que permita hacer text wrap en una celda de la lista. En la actual versión (0.2), no
 * está en uso.
 * <p>
 * Código recogido de una fuente externa: http://flyingjxswithjava.blogspot.com/2016/07/resolved-jtable-wrap-text-in-cell.html
 */
public class StringRenderer extends JTextArea implements TableCellRenderer {
    public StringRenderer() {
        setOpaque(true);
        setLineWrap(true);
        setWrapStyleWord(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.setText(value == null ? "" : value.toString());
        return this;
    }
}