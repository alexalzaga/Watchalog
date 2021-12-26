package icai.dtc.isw.dao;

import icai.dtc.isw.client.Client;
import icai.dtc.isw.domain.Pelicula;
import icai.dtc.isw.domain.Usuario;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class CustomerDAOTest {
    private static ArrayList<Pelicula> lista = new ArrayList<>();
    private static ArrayList<Pelicula> listaPeliculas = new ArrayList<>();
    private static Usuario user;
    @BeforeClass
    public static void testInitialization(){

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
    }

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
}