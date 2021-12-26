package icai.dtc.isw.domain;

import java.util.ArrayList;

public class ContenidoFactory extends AbstractFactory {
    @Override
    public Contenido getContenido(String tipo, String titulo, String director, ArrayList<String> servicios,String portada){
        Contenido c = null;
        if (tipo.equals("pelicula")){
            c = new Pelicula(titulo,director,servicios,portada);
        } else if (tipo.equals("serie")){
            c = new Serie(titulo,director,servicios,portada);
        }
        return c;
    }
}
