package org.ioarmband.controler.tools;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesManager {
	private static final Logger logger = Logger.getLogger(PropertiesManager.class);
	private static String configFileName = "src/main/resources/config.properties";
	private static Properties properties;

	public static void init(){
		properties = new Properties();
		try {
			properties.load(new FileReader(configFileName));
		} catch (FileNotFoundException e) {
			logger.error("Cannot find config file ["+configFileName+"]",e);
		} catch (IOException e) {
			logger.error("Cannot open config file ["+configFileName+"]",e);
		}
		
	}
	public static Object get( String propertyKey ){
		if(properties == null)
			init();
		return properties.get(propertyKey);
	}
	
	public static String getString( String propertyKey ){
		return (String) get(propertyKey);
	}
	
	public static Integer getInt( String propertyKey ){
		return new Integer(getString(propertyKey));
	}
}
