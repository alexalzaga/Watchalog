package icai.dtc.isw.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class PropertiesISW extends Properties{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static PropertiesISW prop;
	private static final String path="properties/properties.xml";
	
	
	private PropertiesISW() {
		try {
			//para generar .jar (y para intellij tambien?)
			this.loadFromXML(getClass().getClassLoader().getResourceAsStream("properties.xml"));

			//para arrancar desde intellij
			//this.loadFromXML(new FileInputStream(path));
		} catch (InvalidPropertiesFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
	
	public static PropertiesISW getInstance() {
		if (prop==null) {
			prop=new PropertiesISW();
		}
		return prop;
		
	}
	
}
