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
	public int cambioEstado(int user, String contenido, String estado) {
		int cambioOk = CustomerDAO.cambioEstado(user, contenido, estado);
		return cambioOk;
	}
	public void buscaF(ArrayList<Contenido> lista, String serviciosQuery,String catalogo,int user) {
		CustomerDAO.buscaF(lista,serviciosQuery,catalogo,user);
	}
	public void buscaN(ArrayList<Contenido> lista, String nombre,String catalogo,int user) {
		CustomerDAO.buscaN(lista,nombre,catalogo,user);
	}
	public void buscaFull(ArrayList<Contenido> lista,String catalogo,int user) {
		CustomerDAO.buscaFull(lista,catalogo,user);
	}
	public void aplicarServicios(Boolean n, Boolean h, Boolean p, Boolean d, Boolean c, String user) {
		CustomerDAO.aplicarServicios(n,h,p,d,c,user);
	}
}
