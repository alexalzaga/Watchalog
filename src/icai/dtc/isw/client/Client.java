package icai.dtc.isw.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import icai.dtc.isw.domain.Contenido;
import icai.dtc.isw.domain.Pelicula;
import org.apache.log4j.Logger;

import icai.dtc.isw.configuration.PropertiesISW;
import icai.dtc.isw.domain.Usuario;
import icai.dtc.isw.message.Message;

public class Client {
	private String host;
	private int port;
	final static Logger logger = Logger.getLogger(Client.class);

	public void startClient(String tipoMensaje, HashMap<String, Object> session) {
		//Configure connections
		String host = PropertiesISW.getInstance().getProperty("host");
		int port = Integer.parseInt(PropertiesISW.getInstance().getProperty("port"));
		Logger.getRootLogger().info("Host: "+host+" port"+port);
		//Create a cliente class
		Client cliente=new Client(host, port);
		
		Message mensajeEnvio=new Message();
		Message mensajeVuelta=new Message();
		mensajeEnvio.setContext(tipoMensaje);
		mensajeEnvio.setSession(session);
		cliente.sent(mensajeEnvio,mensajeVuelta);
		
		
		switch (mensajeVuelta.getContext()) {
			case "/userRegistroResponse":
				int regOk = (int)(mensajeVuelta.getSession().get("registro"));
				session.put("registro",regOk);
				break;

			case "/userLoginResponse":
				boolean respuesta = (boolean)(mensajeVuelta.getSession().get("loginStatus"));
				session.put("loginStatus",respuesta);
				session.put("user",mensajeVuelta.getSession().get("user"));
				break;

			case "/cambioEstadoResponse":
				int cambioOk = (int)(mensajeVuelta.getSession().get("cambio"));
				session.put("cambio",cambioOk);
				break;

			case "/buscaFResponse":
				ArrayList<Contenido> listaPeliculas=(ArrayList<Contenido>)(mensajeVuelta.getSession().get("listaPeliculas"));
				session.put("listaPeliculas",listaPeliculas);
				break;

			case "/buscaNResponse":
				ArrayList<Contenido> listaNombre=(ArrayList<Contenido>)(mensajeVuelta.getSession().get("listaNombre"));
				session.put("listaNombre",listaNombre);
				break;

			case "/buscaFullResponse":
				ArrayList<Contenido> listaFull=(ArrayList<Contenido>)(mensajeVuelta.getSession().get("listaFull"));
				session.put("listaFull",listaFull);
				break;

			case "/aplicarServiciosResponse":
				break;
				
			default:
				Logger.getRootLogger().info("Option not found");
				System.out.println("\nError a la vuelta");
				break;
		
		}
	}

	public Client(){};

	public Client(String host, int port) {
		this.host=host;
		this.port=port;
	}
	

	public void sent(Message messageOut, Message messageIn) {
		try {

			System.out.println("Connecting to host " + host + " on port " + port + ".");

			Socket echoSocket = null;
			OutputStream out = null;
			InputStream in = null;

			try {
				echoSocket = new Socket(host, port);
				in = echoSocket.getInputStream();
				out = echoSocket.getOutputStream();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
				
				//Create the objetct to send
				objectOutputStream.writeObject(messageOut);
				
				// create a DataInputStream so we can read data from it.
		        ObjectInputStream objectInputStream = new ObjectInputStream(in);
		        Message msg=(Message)objectInputStream.readObject();
		        messageIn.setContext(msg.getContext());
		        messageIn.setSession(msg.getSession());
				
			} catch (UnknownHostException e) {
				System.err.println("Unknown host: " + host);
				System.exit(1);
			} catch (IOException e) {
				System.err.println("Unable to get streams from server");
				System.exit(1);
			}		

			/** Closing all the resources */
			out.close();
			in.close();			
			echoSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}