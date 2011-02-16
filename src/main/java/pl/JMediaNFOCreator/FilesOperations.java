package pl.JMediaNFOCreator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class FilesOperations {
	
	public String[] readingFile(String fileName) {
		try {
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/"+fileName)));
			String[] items;
			int i = 0;
			while (inputStream.readLine() != null) {
				++i;
			}
			int rozmiarTablicy = i;
			items = new String[rozmiarTablicy]; 
			inputStream.close();
			inputStream = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/"+fileName)));
			for (i = 0; i < rozmiarTablicy; i++) {				
				items[i] = inputStream.readLine();	
			}
			inputStream.close();
			return items;
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getCfgProperty(String cfgFilename, String cfgKeyProperty) {
		String cfgValueProperty;
		try {
			String userHome = System.getProperty("user.home");
			if(userHome == null) {
		        throw new IllegalStateException("user.home==null");
		    }
			File settingsDirectory = new File(new File(userHome), "." + JMediaNFOCreator.PROGRAM_NAME);
			if(!settingsDirectory.exists()) {
				settingsDirectory.mkdir();
				File configurationFile = new File(settingsDirectory + File.separator + cfgFilename);
		        if (!configurationFile.exists()) {
					createCfgFile(cfgFilename);
				}
			}
			Properties property = new Properties();
			FileInputStream fileInputStream = new FileInputStream(settingsDirectory+File.separator+cfgFilename); 
			property.loadFromXML(fileInputStream);
			cfgValueProperty = property.getProperty(cfgKeyProperty);
			fileInputStream.close();
			return cfgValueProperty;
		} catch (FileNotFoundException e) {
			return null;
		} catch (InvalidPropertiesFormatException e) {
			return cfgValueProperty = "";
		} catch (IOException e) {
			return cfgValueProperty = "";
		}
	}
	
	public void setCfgProperty(String cfgFileName, String cfgKeyProperty, String cfgValueProperty) {
		try {
			String userHome = System.getProperty("user.home");
			if(userHome == null) {
		        throw new IllegalStateException("user.home==null");
		    }
			File settingsDirectory = new File(new File(userHome), "." + JMediaNFOCreator.PROGRAM_NAME);
			if(!settingsDirectory.exists()) {
				settingsDirectory.mkdir();
		        if(!settingsDirectory.mkdir()) {
		            throw new IllegalStateException(settingsDirectory.toString());
		        }
			}
			if (settingsDirectory+File.separator+cfgFileName == null ) {
				createCfgFile(cfgFileName);
			}
			Properties properties = new Properties();
			FileInputStream fileInputStream = new FileInputStream(settingsDirectory+File.separator+cfgFileName); 
			properties.loadFromXML(fileInputStream);
			Hashtable<String, String> hashTable =  new Hashtable<String, String>();
			Enumeration<?> enumeration = properties.keys();
			while (enumeration.hasMoreElements()) {
				String key = (String) enumeration.nextElement(); //key is a string in property file
				String value = properties.getProperty(key);
				hashTable.put(key, value);
			}
			fileInputStream.close();
			FileOutputStream fileOutputStream = new FileOutputStream(settingsDirectory+File.separator+cfgFileName);
			enumeration = hashTable.keys(); 
			while (enumeration.hasMoreElements()) {
				String key = (String) enumeration.nextElement(); //key is a string in property file
				String value = properties.getProperty(key);
				properties.setProperty(key, value);
			}
			properties.setProperty(cfgKeyProperty, cfgValueProperty); 
			properties.storeToXML(fileOutputStream, "Konfiguracja programu " + JMediaNFOCreator.PROGRAM_NAME);
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			System.out.println("Brak pliku");
		} catch (InvalidPropertiesFormatException e) {
			System.out.println("Awaria1");
		} catch (IOException e) {
			System.out.println("Awaria2");
		}
	}
	
	public void createCfgFile(String cfgFilename) {
		try {
			String userHome = System.getProperty("user.home");
			if(userHome == null) {
		        throw new IllegalStateException("user.home==null");
		    }
			File settingsDirectory = new File(new File(userHome), "." + JMediaNFOCreator.PROGRAM_NAME);
			if(!settingsDirectory.exists()) {
				settingsDirectory.mkdir();
		        if(!settingsDirectory.mkdir()) {
		            throw new IllegalStateException(settingsDirectory.toString());
		        }
			}					
			File confFile = new File(userHome+File.separator+"." + JMediaNFOCreator.PROGRAM_NAME + File.separator+cfgFilename);
			if(!confFile.exists()) {
				confFile.createNewFile();
				Properties property = new Properties();
				FileOutputStream fileOutputStream = new FileOutputStream(settingsDirectory+File.separator+cfgFilename);
				property.setProperty("autocrc", "0");
				property.setProperty("language", "polish.lng");
				property.setProperty("defaultpath", "C:");
				property.setProperty("addbitrate", "0");
				property.setProperty("saveunicode", "0");
				property.setProperty("crc", "MD5");
				property.setProperty("rippedby", "Nieznany");
				property.setProperty("postedby", "Nieznany");
				property.setProperty("notes", "1");
				property.setProperty("sources", "CD");
				property.setProperty("VA", "VA");
				property.setProperty("server", "news4.euro.net");
				property.setProperty("cd/dvd", "Delta OME-W141");
				property.setProperty("ripper", "Audiograbber 1.82");
				property.storeToXML(fileOutputStream, "Konfiguracja programu " + JMediaNFOCreator.PROGRAM_NAME);
				fileOutputStream.close();
			}	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}