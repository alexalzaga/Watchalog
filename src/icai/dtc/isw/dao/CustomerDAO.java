package icai.dtc.isw.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import icai.dtc.isw.domain.Contenido;
import icai.dtc.isw.domain.ContenidoFactory;
import icai.dtc.isw.domain.Usuario;
import icai.dtc.isw.domain.Pelicula;

public class CustomerDAO {

	private static ContenidoFactory factory = new ContenidoFactory();
	/**
	 * Método para el registro de nuevos usuarios. Realiza una comprobación para asegurar que no se repitan nombres
	 * de usuario, y le otorga al usuario un id para uso interno de búsquedas en la base de datos.
	 * @param password Contraseña elegida por el usuario
	 * @param nombre Nombre elegido por el usuario
	 * @return Comfirmacion de que el registro se ha realizado correctamente
	 */
	public static int userRegistro(String password, String nombre) {
		Connection con=ConnectionDAO.getInstance().getConnection();
		int regOk = 0;
		try (PreparedStatement pst = con.prepareStatement("SELECT nombre FROM usuarios WHERE nombre = '"+nombre+"'");
			 ResultSet rs = pst.executeQuery()) {
			while(rs.next()) {
				regOk = -1; //error de nombre ya existente
				return regOk;
			}
		} catch (SQLException ex) {

			System.out.println(ex.getMessage());
		}

		Integer id = 0;
		try (PreparedStatement pst = con.prepareStatement("SELECT MAX(id) FROM usuarios");
			 ResultSet rs = pst.executeQuery()) {
			while(rs.next()) {
				id = rs.getInt(1);
				id++;
			}
		} catch (SQLException ex) {

			System.out.println(ex.getMessage());
		}

		try {
			PreparedStatement pst = con.prepareStatement("INSERT INTO usuarios(id,nombre,password,servicios) VALUES(" +
					id +",'"+nombre+"','"+password+"','{}')");
			int i = pst.executeUpdate();
			if (i > 0) {
				regOk = 1; //registro correcto
				return regOk;
			} else {
				return regOk; //0 si ha habido error
			}
		} catch (SQLException ex) {

			System.out.println(ex.getMessage());
		}
		return 0;
	}

	/**
	 * Método para hacer el login del usuario. Realiza una query en la base de datos 'usuarios' para encontrar al usuario
	 * con el nombre y password introducidos y confirma la coincidencia del resultado devuelto.
	 * @param password contraseña introducida en la ventana
	 * @param nombre nombre introducido en la ventana
	 * @param user array donde guardaremos todos los datos del usuario una vez autenticado
	 * @return boolean para la confirmar si el login ha sido realizado correctamente
	 */
	public static boolean userLogin(String password, String nombre,Usuario user) {
		Connection con=ConnectionDAO.getInstance().getConnection();
		try (PreparedStatement pst = con.prepareStatement("SELECT * FROM usuarios WHERE nombre = '"+nombre+ "' AND password = '"+password+"'");
			 ResultSet rs = pst.executeQuery()) {
			boolean loginOK = false;
			while(rs.next()) {
				String nombreEncontrado = rs.getString("nombre");
				String passwordEcontrado = rs.getString("password");
				if (nombre.equals(nombreEncontrado) && password.equals(passwordEcontrado)) {
					loginOK = true;
					String nombreUser = rs.getString("nombre");
					String passwordUser = rs.getString("password");
					int idUser = rs.getInt("id");
					Array a = rs.getArray(4);
					ArrayList<String> servicios = new ArrayList<>();
					if (a != null){
						String[] a2 = (String[])a.getArray();
						List<String> list = Arrays.asList(a2);
						servicios.addAll(list);
					}
					user.setId(idUser);
					user.setName(nombreUser);
					user.setPass(passwordUser);
					user.setServicios(servicios);
					return loginOK;
				}
			}

		} catch (SQLException ex) {

			System.out.println(ex.getMessage());
		}
		return false;
	}

	/**
	 * Método para realizar la búsqueda de películas filtrada por los servicios establecidos como favoritos por el usuario.
	 * <p>
	 * Realiza dos querys: una para seleccionar los servicios favoritos, y otra para seleccionar el contenido disponible en
	 * al menos uno de esos servicios. No devuelve nada, sino que rellena el parámetro 'lista' que tiene como input.
	 * @param lista array donde se guardarán los resultados para su display en la ventana
	 * @param serviciosQuery string que contiene los servicios favoritos del usuario redactados entre comas para
	 *                       ser introducido en la query
	 * @param catalogo string donde se especifica en que base datos se va a buscar
	 */
	public static void buscaF(ArrayList<Contenido> lista, String serviciosQuery, String catalogo) {
		Connection con=ConnectionDAO.getInstance().getConnection();

		if(catalogo.equals("P")||catalogo.equals("A")) {
			try (PreparedStatement pst = con.prepareStatement("SELECT * FROM peliculas WHERE servicios && ('{"+serviciosQuery+"}')");
				 ResultSet rs = pst.executeQuery()) {

				while (rs.next()) {
					ArrayList<String> servicios = new ArrayList<>();
					Array a = rs.getArray(3);
					String[] a2 = (String[])a.getArray();
					List<String> list = Arrays.asList(a2);
					String titulo = rs.getString("titulo");
					String director = rs.getString("director");
					servicios.addAll(list);
					String portada = rs.getString("portada");
					lista.add(factory.getContenido("pelicula",titulo,director,servicios,portada));
				}

			} catch (SQLException ex) {

				System.out.println(ex.getMessage());
			}
		}

		if(catalogo.equals("S")||catalogo.equals("A")) {
			try (PreparedStatement pst = con.prepareStatement("SELECT * FROM series WHERE servicios && ('{"+serviciosQuery+"}')");
				 ResultSet rs = pst.executeQuery()) {

				while (rs.next()) {
					ArrayList<String> servicios = new ArrayList<>();
					Array a = rs.getArray(3);
					String[] a2 = (String[])a.getArray();
					List<String> list = Arrays.asList(a2);
					String titulo = rs.getString("titulo");
					String director = rs.getString("director");
					servicios.addAll(list);
					String portada = rs.getString("portada");
					lista.add(factory.getContenido("serie",titulo,director,servicios,portada));
				}

			} catch (SQLException ex) {

				System.out.println(ex.getMessage());
			}
		}
	}

	/**
	 * Método para realizar la búsqueda de contenido filtrado por el nombre introducido en la ventana por el usuario.
	 * <p>
	 * La query devolverá todas aquellas películas y/o series que contengan en algún punto de su título el string introducido. Permite
	 * tanto mayúsuclas como minúsculas.
	 * @param lista array donde se guardarán los resultados para su display en la ventana
	 * @param nombre string introducido por el usuario
	 * @param catalogo string donde se especifica en que base datos se va a buscar
	 */
	public static void buscaN(ArrayList<Contenido> lista, String nombre,String catalogo) {
		Connection con=ConnectionDAO.getInstance().getConnection();

		if(catalogo.equals("P")||catalogo.equals("A")) {
			try (PreparedStatement pst = con.prepareStatement("SELECT * FROM peliculas WHERE titulo LIKE UPPER('%"+nombre+"%')");
				 ResultSet rs = pst.executeQuery()) {

				while (rs.next()) {
					ArrayList<String> servicios = new ArrayList<>();
					Array a = rs.getArray(3);
					String[] a2 = (String[])a.getArray();
					List<String> list = Arrays.asList(a2);
					String titulo = rs.getString("titulo");
					String director = rs.getString("director");
					String portada = rs.getString("portada");
					servicios.addAll(list);
					lista.add(factory.getContenido("pelicula",titulo,director,servicios,portada));
				}

			} catch (SQLException ex) {

				System.out.println(ex.getMessage());
			}
		}

		if(catalogo.equals("S")||catalogo.equals("A")) {
			try (PreparedStatement pst = con.prepareStatement("SELECT * FROM series WHERE titulo LIKE UPPER('%"+nombre+"%')");
				 ResultSet rs = pst.executeQuery()) {

				while (rs.next()) {
					ArrayList<String> servicios = new ArrayList<>();
					Array a = rs.getArray(3);
					String[] a2 = (String[])a.getArray();
					List<String> list = Arrays.asList(a2);
					String titulo = rs.getString("titulo");
					String director = rs.getString("director");
					String portada = rs.getString("portada");
					servicios.addAll(list);
					lista.add(factory.getContenido("serie",titulo,director,servicios,portada));
				}

			} catch (SQLException ex) {

				System.out.println(ex.getMessage());
			}
		}
	}

	/**
	 * Método para la muestra del catálogo completo contenido en la base de datos especificada en catalogo.
	 * @param lista array donde se guardarán los resultados para su display en la ventana
	 * @param catalogo string donde se especifica en que base datos se va a buscar
	 */
	public static void buscaFull(ArrayList<Contenido> lista,String catalogo) {
		Connection con=ConnectionDAO.getInstance().getConnection();

		if(catalogo.equals("P")||catalogo.equals("A")){
			try (PreparedStatement pst = con.prepareStatement("SELECT * FROM peliculas");
				 ResultSet rs = pst.executeQuery()) {

				while (rs.next()) {
					ArrayList<String> servicios = new ArrayList<>();
					Array a = rs.getArray(3);
					String[] a2 = (String[])a.getArray();
					List<String> list = Arrays.asList(a2);
					String titulo = rs.getString("titulo");
					String director = rs.getString("director");
					servicios.addAll(list);
					String portada = rs.getString("portada");
					lista.add(factory.getContenido("pelicula",titulo,director,servicios,portada));
				}

			} catch (SQLException ex) {

				System.out.println(ex.getMessage());
			}
		}

		if(catalogo.equals("S")||catalogo.equals("A")){
			try (PreparedStatement pst = con.prepareStatement("SELECT * FROM series");
				 ResultSet rs = pst.executeQuery()) {

				while (rs.next()) {
					ArrayList<String> servicios = new ArrayList<>();
					Array a = rs.getArray(3);
					String[] a2 = (String[])a.getArray();
					List<String> list = Arrays.asList(a2);
					String titulo = rs.getString("titulo");
					String director = rs.getString("director");
					servicios.addAll(list);
					String portada = rs.getString("portada");
					lista.add(factory.getContenido("serie",titulo,director,servicios,portada));
				}

			} catch (SQLException ex) {

				System.out.println(ex.getMessage());
			}
		}
	}

	/**
	 * Método para actualizar la lista de servicios favoritos del usuario. Se comprobarán uno a uno los valores booleanos
	 * relacionados con cada servicio que han sido marcados por el usuario en la ventana, y se actualizarán en la base
	 * de datos los servicios favoritos relacionados con el usuario en cuestión
	 * @param n boolean que determina si el servicio 'Netflix' ha sido marcado en la ventana por el usuario
	 * @param h boolean que determina si el servicio 'HBO' ha sido marcado en la ventana por el usuario
	 * @param p boolean que determina si el servicio 'Prime Video' ha sido marcado en la ventana por el usuario
	 * @param d boolean que determina si el servicio 'Disney+' ha sido marcado en la ventana por el usuario
	 * @param c boolean que determina si el servicio 'Crunchyroll' ha sido marcado en la ventana por el usuario
	 * @param user nombre del usuario que ha iniciado sesión
	 */
	public static void aplicarServicios(Boolean n, Boolean h, Boolean p, Boolean d, Boolean c, String user) {
		Connection con=ConnectionDAO.getInstance().getConnection();
		String servicios = "";
		if(n) {
			servicios = "Netflix";
		}
		if(h){
			if (servicios.equals("")){
				servicios = "HBO";
			} else {
				servicios = servicios + ",HBO";
			}
		}
		if(p){
			if (servicios.equals("")) {
				servicios = "Prime Video";
			} else {
				servicios = servicios + ",Prime Video";
			}
		}
		if(d) {
			if (servicios.equals("")) {
				servicios = "Disney+";
			} else {
				servicios = servicios + ",Disney+";
			}
		}
		if(c){
			if (servicios.equals("")) {
				servicios = "Crunchyroll";
			} else {
				servicios = servicios + ",Crunchyroll";
			}
		}
		try {
			PreparedStatement pst = con.prepareStatement("UPDATE usuarios SET servicios='{"+servicios+"}' WHERE nombre='"+user+"'");
			pst.executeQuery();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
