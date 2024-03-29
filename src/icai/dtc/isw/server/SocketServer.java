package icai.dtc.isw.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import icai.dtc.isw.controler.CustomerControler;
import icai.dtc.isw.domain.Contenido;
import icai.dtc.isw.domain.Usuario;
import icai.dtc.isw.domain.Pelicula;
import icai.dtc.isw.message.Message;

public class SocketServer extends Thread {
	public static final int PORT_NUMBER = 8081;

	protected Socket socket;

	private SocketServer(Socket socket) {
		this.socket = socket;
		System.out.println("New client connected from " + socket.getInetAddress().getHostAddress());
		start();
	}

	public void run() {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = socket.getInputStream();
			out = socket.getOutputStream();
			
			//first read the object that has been sent
			ObjectInputStream objectInputStream = new ObjectInputStream(in);
		    Message mensajeIn= (Message)objectInputStream.readObject();
		    //Object to return informations 
		    ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
		    Message mensajeOut=new Message();
			CustomerControler customerControler=new CustomerControler();
		    switch (mensajeIn.getContext()) {
		    	case "/userRegistro":
					int regOk = customerControler.userRegistro((String)mensajeIn.getSession().get("password"),(String)mensajeIn.getSession().get("nombre"));
					mensajeOut.setContext("/userRegistroResponse");
					HashMap<String,Object> sessionRegistro=new HashMap<String, Object>();
					sessionRegistro.put("registro",regOk);
					mensajeOut.setSession(sessionRegistro);
					objectOutputStream.writeObject(mensajeOut);
					break;

				case "/userLogin":
					Usuario user = (Usuario)mensajeIn.getSession().get("user");
					boolean loginOK = customerControler.userLogin((String)mensajeIn.getSession().get("password"),(String)mensajeIn.getSession().get("nombre"),user);
					mensajeOut.setContext("/userLoginResponse");
					HashMap<String,Object> sessionLogin=new HashMap<String, Object>();
					sessionLogin.put("loginStatus",loginOK);
					sessionLogin.put("user",user);
					mensajeOut.setSession(sessionLogin);
					objectOutputStream.writeObject(mensajeOut);
					break;

				case "/cambioEstado":
					int cambioOk = customerControler.cambioEstado((int)mensajeIn.getSession().get("usuario"),(String)mensajeIn.getSession().get("titulo"),(String)mensajeIn.getSession().get("estado"));
					mensajeOut.setContext("/cambioEstadoResponse");
					HashMap<String,Object> sessionEstado=new HashMap<String, Object>();
					sessionEstado.put("cambio",cambioOk);
					mensajeOut.setSession(sessionEstado);
					objectOutputStream.writeObject(mensajeOut);
					break;

				case "/buscaF":
					ArrayList<Contenido> listaContenido=new ArrayList<>();
					customerControler.buscaF(listaContenido,(String)mensajeIn.getSession().get("serviciosQuery"),(String)mensajeIn.getSession().get("catalogo"),
							(int)mensajeIn.getSession().get("usuario"));
					mensajeOut.setContext("/buscaFResponse");
					HashMap<String,Object> sessionBuscaF= new HashMap<>();
					sessionBuscaF.put("listaPeliculas",listaContenido);
					mensajeOut.setSession(sessionBuscaF);
					objectOutputStream.writeObject(mensajeOut);
					break;

				case "/buscaN":
					ArrayList<Contenido> listaNombre=new ArrayList<>();
					customerControler.buscaN(listaNombre,(String)mensajeIn.getSession().get("nombre"),(String)mensajeIn.getSession().get("catalogo"),
							(int)mensajeIn.getSession().get("usuario"));
					mensajeOut.setContext("/buscaNResponse");
					HashMap<String,Object> sessionBuscaN= new HashMap<>();
					sessionBuscaN.put("listaNombre",listaNombre);
					mensajeOut.setSession(sessionBuscaN);
					objectOutputStream.writeObject(mensajeOut);
					break;

				case "/buscaFull":
					ArrayList<Contenido> listaFull=new ArrayList<>();
					customerControler.buscaFull(listaFull,(String)mensajeIn.getSession().get("catalogo"),(int)mensajeIn.getSession().get("usuario"));
					mensajeOut.setContext("/buscaFullResponse");
					HashMap<String,Object> sessionFull=new HashMap<String, Object>();
					sessionFull.put("listaFull",listaFull);
					mensajeOut.setSession(sessionFull);
					objectOutputStream.writeObject(mensajeOut);
					break;

				case "/aplicarServicios":
					customerControler.aplicarServicios((Boolean)mensajeIn.getSession().get("netflix"),(Boolean)mensajeIn.getSession().get("hbo"),
							(Boolean)mensajeIn.getSession().get("prime"),(Boolean)mensajeIn.getSession().get("disney"),
							(Boolean)mensajeIn.getSession().get("crunchy"),(String)mensajeIn.getSession().get("usuario"));
					mensajeOut.setContext("/aplicarServiciosResponse");
					objectOutputStream.writeObject(mensajeOut);
					break;
		    	
		    	default:
		    		System.out.println("\nParámetro no encontrado");
		    		break;
		    }
		    
		    //Lógica del controlador 
		    //System.out.println("\n1.- He leído: "+mensaje.getContext());
		    //System.out.println("\n2.- He leído: "+(String)mensaje.getSession().get("Nombre"));
		    
		    
		    
		    //Prueba para esperar
		    /*try {
		    	System.out.println("Me duermo");
				Thread.sleep(15000);
				System.out.println("Me levanto");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			// create an object output stream from the output stream so we can send an object through it
			/*ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
			
			//Create the object to send
			String cadena=((String)mensaje.getSession().get("Nombre"));
			cadena+=" añado información";
			mensaje.getSession().put("Nombre", cadena);
			//System.out.println("\n3.- He leído: "+(String)mensaje.getSession().get("Nombre"));
			objectOutputStream.writeObject(mensaje);*
			*/

		} catch (IOException ex) {
			System.out.println("Unable to get streams from client");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
				socket.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		System.out.println("SocketServer Example");
		ServerSocket server = null;
		try {
			server = new ServerSocket(PORT_NUMBER);
			while (true) {
				/**
				 * create a new {@link SocketServer} object for each connection
				 * this will allow multiple client connections
				 */
				new SocketServer(server.accept());
			}
		} catch (IOException ex) {
			System.out.println("Unable to start server.");
		} finally {
			try {
				if (server != null)
					server.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}