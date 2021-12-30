package project.misc;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Clase para renderizar las celdas del estado de visualización de un contenido. Precarga las imagenes asociadas y cambia la imagen
 * según el estado recibido.
 */
public class EstadoRenderer extends JLabel implements TableCellRenderer {

    private ImageIcon viendoIcon;
    private ImageIcon vistoIcon;
    private ImageIcon listaIcon;

    public EstadoRenderer() {
        viendoIcon = new ImageIcon(new ImageIcon(getClass().getResource("/img/viendo.png")).getImage().getScaledInstance(40,25,Image.SCALE_SMOOTH));
        vistoIcon = new ImageIcon(new ImageIcon(getClass().getResource("/img/visto.png")).getImage().getScaledInstance(40,25,Image.SCALE_SMOOTH));
        listaIcon = new ImageIcon(new ImageIcon(getClass().getResource("/img/lista.png")).getImage().getScaledInstance(32,35,Image.SCALE_SMOOTH));
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.NORTH);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        try {
            if (value.equals("VIENDO")){
                this.setIcon(viendoIcon);
            } else if (value.equals("VISTO")){
                this.setIcon(vistoIcon);
            } else if (value.equals("LISTA")){
                this.setIcon(listaIcon);
            }
        } catch (NullPointerException ignored){
        }
        return this;
    }
}