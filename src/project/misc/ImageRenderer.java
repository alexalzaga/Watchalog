package project.misc;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Creado a partir de la clase descrita aquí: https://mdsaputra.wordpress.com/2011/06/13/swing-hack-show-image-in-jtable/
 * Clase usada como CellRenderer para mostrar imágenes en la celda, convirtiendo la ruta de cada imagen (String) a ImageIcon
 */
public class ImageRenderer extends DefaultTableCellRenderer
{
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        this.setHorizontalAlignment(JLabel.CENTER);
        setIcon((Icon)value);
        return this;
    }
}