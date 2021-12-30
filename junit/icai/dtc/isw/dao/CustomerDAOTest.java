package icai.dtc.isw.dao;

import icai.dtc.isw.client.Client;
import icai.dtc.isw.domain.Contenido;
import icai.dtc.isw.domain.ContenidoFactory;
import icai.dtc.isw.domain.Pelicula;
import icai.dtc.isw.domain.Usuario;
import org.junit.BeforeClass;
import org.junit.Test;
import project.ui.Ventana;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class CustomerDAOTest {
    //private static ArrayList<Pelicula> lista = new ArrayList<>();
    //private static ArrayList<Pelicula> listaPeliculas = new ArrayList<>();
    //private static Usuario user;

    private static ArrayList<Contenido> listaContenido = new ArrayList<>();
    private static Usuario userReg;
    private static ContenidoFactory factory = new ContenidoFactory();

    @BeforeClass
    public static void testInitialization(){

        //ya comprobados en 0.2
        /*
        //busqueda por nombre (buscaN)
        Pelicula pelicula = new Pelicula();
        ArrayList<String> servicios = new ArrayList<>();
        servicios.add("Disney+");
        pelicula.setTitulo("SPIDERMAN 2");
        pelicula.setDirector("SAM RAIMI");
        pelicula.setServicios(servicios);
        lista.add(pelicula);

        //busqueda filtrada (en este supuesto solo debería mostrar 1 resultado)
        ArrayList<String> servicios2 = new ArrayList<>();
        servicios2.add("Netflix");
        servicios2.add("HBO");
        Pelicula pelicula2 = new Pelicula("RESERVOIR DOGS", "QUENTIN TARANTINO",servicios2);
        lista.add(pelicula2);

        //login
        ArrayList<String> serviciosUser = new ArrayList<>();
        serviciosUser.add("HBO");
        serviciosUser.add("Disney+");
        user = new Usuario(123,"Alex","alex123",serviciosUser);
        */

        //modificar estado
        ArrayList<String> serviciosC = new ArrayList<>();
        serviciosC.add("Netflix");
        serviciosC.add("Prime Video");
        Contenido c = factory.getContenido("serie","MR ROBOT","SAM ESMAIL",serviciosC,"ooa");
        c.setEstado("VISTO");

        //registro
        userReg = new Usuario(3,"JUnitTest","JU3",null);
    }

    /*
    @Test
    public void buscaN() {
        Client client = new Client();
        HashMap<String, Object> session = new HashMap<>();
        session.put("nombre", "spid");
        client.startClient("/buscaN",session);
        listaPeliculas = (ArrayList<Pelicula>) session.get("listaNombre");
        assertEquals(lista.get(0).getTitulo(),listaPeliculas.get(0).getTitulo());
        assertEquals(lista.get(0).getDirector(),listaPeliculas.get(0).getDirector());
        assertEquals(lista.get(0).getServicios(),listaPeliculas.get(0).getServicios());
        session.put("nombre", "SPideR");
        client.startClient("/buscaN",session);
        listaPeliculas = (ArrayList<Pelicula>) session.get("listaNombre");
        assertEquals(lista.get(0).getTitulo(),listaPeliculas.get(0).getTitulo());
        assertEquals(lista.get(0).getDirector(),listaPeliculas.get(0).getDirector());
        assertEquals(lista.get(0).getServicios(),listaPeliculas.get(0).getServicios());
    }

    @Test
    public void buscaF() {
        Client client = new Client();
        HashMap<String, Object> session = new HashMap<>();
        session.put("serviciosQuery", "HBO, Disney+");
        client.startClient("/buscaF",session);
        listaPeliculas = (ArrayList<Pelicula>) session.get("listaPeliculas");
        for(int i = 0; i<lista.size(); i++) {
            if(listaPeliculas.get(i).getTitulo().equals("RESERVOIR DOGS")) {
                assertEquals(lista.get(1).getTitulo(),listaPeliculas.get(i).getTitulo());
                assertEquals(lista.get(1).getDirector(),listaPeliculas.get(i).getDirector());
                assertEquals(lista.get(1).getServicios(),listaPeliculas.get(i).getServicios());
            } else {
                assertEquals(lista.get(0).getTitulo(),listaPeliculas.get(i).getTitulo());
                assertEquals(lista.get(0).getDirector(),listaPeliculas.get(i).getDirector());
                assertEquals(lista.get(0).getServicios(),listaPeliculas.get(i).getServicios());
            }
        }
    }

    @Test
    public void userLogin() {
        Usuario usuario = new Usuario();
        Client client = new Client();
        HashMap<String, Object> session = new HashMap<>();
        session.put("nombre", "Alex");
        session.put("password", "alex123");
        session.put("user",usuario);
        client.startClient("/userLogin",session);
        boolean respuesta = (boolean) session.get("loginStatus");
        usuario = (Usuario)session.get("user");
        assertTrue(respuesta);
        assertEquals(user.getName(),usuario.getName());
        assertEquals(user.getPass(),usuario.getPass());
        assertEquals(user.getServicios(),usuario.getServicios());
        assertEquals(user.getId(),usuario.getId());
    }
    */

    @Test
    public void userRegistro(){
        String nombre = "JUnitTest";
        String password =  "JU3";
        Client client = new Client();
        HashMap<String, Object> session = new HashMap<>();
        session.put("nombre", nombre);
        session.put("password", password);
        client.startClient("/userRegistro",session);
        int regOk = (int) session.get("registro");
        assertEquals(regOk,1);

        Client client2 = new Client();
        HashMap<String, Object> session2 = new HashMap<>();
        Usuario usuario = new Usuario();
        session2.put("nombre", "JUnitTest");
        session2.put("password", "JU3");
        session2.put("user",usuario);
        client2.startClient("/userLogin",session2);
        boolean respuesta = (boolean) session2.get("loginStatus");
        usuario = (Usuario)session2.get("user");
        assertTrue(respuesta);
        assertEquals(userReg.getName(),usuario.getName());
        assertEquals(userReg.getPass(),usuario.getPass());
        assertEquals(userReg.getId(),usuario.getId());
    }

    @Test
    public void cambioEstado() {
        String estado = "VISTO";
        Client client = new Client();
        HashMap<String, Object> session = new HashMap<>();
        session.put("usuario",2);
        session.put("titulo","MR ROBOT");
        session.put("estado",estado);
        client.startClient("/cambioEstado",session);
        int cambioOk = (int) session.get("cambio");
        assertEquals(cambioOk,1);

        Client client2 = new Client();
        HashMap<String, Object> session2 = new HashMap<>();
        session2.put("nombre", "MR ROBOT");
        session2.put("catalogo","S");
        session2.put("usuario",2);
        client2.startClient("/buscaN",session2);
        listaContenido = (ArrayList<Contenido>) session2.get("listaNombre");
        assertEquals("MR ROBOT",listaContenido.get(0).getTitulo());
        assertEquals("VISTO",listaContenido.get(0).getEstado());
    }
}