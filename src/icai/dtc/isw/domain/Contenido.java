package icai.dtc.isw.domain;

import java.util.ArrayList;

public interface Contenido {
    void setTitulo(String titulo);
    void setDirector(String director);
    void setServicios(ArrayList<String> servicios);
    void setPortada(String portada);
    void setEstado(String estado);
    String getTitulo();
    String getDirector();
    ArrayList<String> getServicios();
    String getPortada();
    String getEstado();
}