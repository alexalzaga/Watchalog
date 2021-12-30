package icai.dtc.isw.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Clase usada para generar los objetos 'Serie', que contienen su título, director y los servicios donde está disponible
 */
public class Serie implements Serializable,Contenido{

    private static final long serialVersionUID = 1L;
    private String titulo;
    private String director;
    ArrayList<String> servicios;
    private String portada;
    private String estado;

    public Serie() {
        this.setTitulo(new String());
        this.setDirector(new String());
        this.setServicios(new ArrayList<>());
        this.setPortada(new String());
    }

    public Serie(String titulo, String director, ArrayList<String> servicios, String portada) {
        this.setTitulo(titulo);
        this.setDirector(director);
        this.setServicios(servicios);
        this.setPortada(portada);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public ArrayList<String> getServicios() { return servicios; }

    public void setServicios(ArrayList<String> servicios) { this.servicios = servicios; }

    public String getPortada() {
        return portada;
    }

    public void setPortada(String portada) {
        this.portada = portada;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
