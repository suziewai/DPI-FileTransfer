package au.com.vocus.ipfix;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class IPFixProperty {

	public static class PropertyKey {
		public static String SERVER_URL = "server.url";
		public static String USERNAME = "server.access.username";
		public static String PASSWORD = "server.access.password";
		public static String LAST_FILE_DATE = "remoteFile.lastFile.date";
		public static String DATE_FORMAT = "remoteFile.dateFormat";
		public static String LOCAL_PATH = "localFile.path";
	}

	private static final String filename = "ipfix.properties";
	private Properties PROPS = new Properties();
	private SimpleDateFormat df;
	private Date lastFileDate;
	
	public IPFixProperty() {
		try {
            final InputStream inputStream = new FileInputStream(filename);
            PROPS.load(inputStream);
            inputStream.close();
            
            df = new SimpleDateFormat(PROPS.getProperty(PropertyKey.DATE_FORMAT));
            
        } catch (IOException e) {
            System.out.println("Error loading properties file.");
            e.printStackTrace();
        }
	}
	
	public String getServerURL() {
		return PROPS.getProperty(PropertyKey.SERVER_URL);
	}
	
	public String getUsername() {
		return PROPS.getProperty(PropertyKey.USERNAME);
	}
	
	public String getPassword() {
		return PROPS.getProperty(PropertyKey.PASSWORD);
	}
	
	public String getDateFormat() {
		return PROPS.getProperty(PropertyKey.DATE_FORMAT);
	}
	
	public String getLocalPath() {
		return PROPS.getProperty(PropertyKey.LOCAL_PATH);
	}
	
	public Date getLastFileDate() {
		if(PROPS.getProperty(PropertyKey.LAST_FILE_DATE) == null || PROPS.getProperty(PropertyKey.LAST_FILE_DATE).trim().isEmpty())
			return null;
		
		if (lastFileDate == null) {
			
			try {
				lastFileDate = df.parse(PROPS.getProperty(PropertyKey.LAST_FILE_DATE));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return lastFileDate;
	}
	
	public void setLastFileDate(Date datetime) {
		PROPS.put(PropertyKey.LAST_FILE_DATE, df.format(datetime));
	}
	
	public void save() {
		try {
			PROPS.store(new FileOutputStream(filename), null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
