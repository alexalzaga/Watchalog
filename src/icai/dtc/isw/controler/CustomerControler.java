package icai.dtc.isw.controler;

import java.util.ArrayList;

import icai.dtc.isw.dao.CustomerDAO;
import icai.dtc.isw.domain.Contenido;
import icai.dtc.isw.domain.Usuario;
import icai.dtc.isw.domain.Pelicula;

import javax.net.ssl.ExtendedSSLSession;

public class CustomerControler {

	public int userRegistro(String password, String nombre) {
		int regOk = CustomerDAO.userRegistro(password, nombre);
		return regOk;
	}
	public boolean userLogin(String password, String nombre,Usuario user) {
		boolean ok = CustomerDAO.userLogin(password, nombre, user);
		return ok;
	}
	public void buscaF(ArrayList<Contenido> lista, String serviciosQuery,String catalogo) {
		CustomerDAO.buscaF(lista,serviciosQuery,catalogo);
	}
	public void buscaN(ArrayList<Contenido> lista, String nombre,String catalogo) {
		CustomerDAO.buscaN(lista,nombre,catalogo);
	}
	public void buscaFull(ArrayList<Contenido> lista,String catalogo) {
		CustomerDAO.buscaFull(lista,catalogo);
	}
	public void aplicarServicios(Boolean n, Boolean h, Boolean p, Boolean d, Boolean c, String user) {
		CustomerDAO.aplicarServicios(n,h,p,d,c,user);
	}
}
