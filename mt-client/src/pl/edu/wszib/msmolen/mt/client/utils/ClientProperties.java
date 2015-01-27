package pl.edu.wszib.msmolen.mt.client.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class ClientProperties
{
	private static final String PROPERTY_MAP_SUM = "client.map.checksum";
	private static final String PROPERTY_SERVER_ADDRESS = "client.server_address";

	private static final String PROPERTY_USER_LOGIN = "client.user.login";
	private static final String PROPERTY_USER_PASSWORD = "client.user.password";

	private static final String SERVER_ADDRESS = "localhost:8443";

	private static ClientProperties instance = new ClientProperties();

	private final Properties properties = new Properties();

	private ClientProperties()
	{
		loadProperties();
	}

	public static String getMapCheckSum()
	{
		return instance.properties.getProperty(PROPERTY_MAP_SUM);
	}

	public static void setMapCheckSum(String pmSum)
	{
		instance.properties.setProperty(PROPERTY_MAP_SUM, pmSum);
		saveProperties();
	}

	public static String getServerAddress()
	{
		return instance.properties.getProperty(PROPERTY_SERVER_ADDRESS);
	}

	public static void setServerAddress()
	{
		instance.properties.setProperty(PROPERTY_SERVER_ADDRESS, SERVER_ADDRESS);
		saveProperties();
	}

	public static String getUserLogin()
	{
		return instance.properties.getProperty(PROPERTY_USER_LOGIN);
	}

	public static void setUserLogin(String pmLogin)
	{
		instance.properties.setProperty(PROPERTY_USER_LOGIN, pmLogin);
		saveProperties();
	}

	public static String getUserPassword()
	{
		return instance.properties.getProperty(PROPERTY_USER_PASSWORD);
	}

	public static void setUserPassword(String pmPassword)
	{
		instance.properties.setProperty(PROPERTY_USER_PASSWORD, pmPassword);
		saveProperties();
	}

	private static void saveProperties()
	{
		FileOutputStream lvFOS = null;
		try
		{
			lvFOS = new FileOutputStream(ClientUtils.getSettingsFile());

			instance.properties.store(lvFOS, "");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (lvFOS != null)
				try
				{
					lvFOS.close();
				}
				catch (Exception e)
				{
				}
		}
	}

	private void loadProperties()
	{
		FileInputStream lvFIS = null;
		try
		{
			lvFIS = new FileInputStream(ClientUtils.getSettingsFile());

			properties.load(lvFIS);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (lvFIS != null)
				try
				{
					lvFIS.close();
				}
				catch (Exception e)
				{
				}
		}
	}
}
