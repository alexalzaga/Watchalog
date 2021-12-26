package icai.dtc.isw.domain;

import java.util.ArrayList;

public abstract class AbstractFactory {
    abstract Contenido getContenido(String tipo, String titulo, String director, ArrayList<String> servicios, String portada);
}
