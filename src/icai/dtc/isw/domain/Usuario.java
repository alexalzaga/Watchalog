package icai.dtc.isw.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Clase usada para generar el objeto 'Usuario', que contiene sus datos de login y sus servicios marcados como preferidos
 */
public class Usuario implements Serializable{

	private static final long serialVersionUID = 1L;
	private String password;
	private String name;
	private int id;
	ArrayList<String> servicios;
	
	public Usuario() {
		this.setPass(new String());
		this.setName(new String());
		this.setServicios(new ArrayList<>());
	}
	
	public Usuario(int id, String name, String password, ArrayList<String> servicios) {
		this.setId(id);
		this.setName(name);
		this.setPass(password);
		this.setServicios(servicios);
	}

	public String getPass() {
		return password;
	}

	public void setPass(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<String> getServicios() { return servicios; }

	public void setServicios(ArrayList<String> servicios) { this.servicios = servicios; }
}
