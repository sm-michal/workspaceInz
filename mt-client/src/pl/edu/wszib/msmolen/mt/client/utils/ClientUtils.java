package pl.edu.wszib.msmolen.mt.client.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;

import pl.edu.wszib.msmolen.mt.common.utils.exceptions.ApplicationException;

/**
 * Klasa z metodami pomocniczymi wykorzystywanymi w module klienckim
 * 
 * @author msmolen
 * 
 */
public class ClientUtils
{
	private static final String DATA_DIR = "data";
	private static final String SETTINGS_FILE_PATH = "data" + File.separator + "settings.conf";
	private static final String MAP_FILE_PATH = "data" + File.separator + "map.dat";

	public static String getTimeMessage(int pmTime)
	{
		if (pmTime == 1)
			return "1 minuta";
		else if (pmTime > 5 && pmTime < 21 || pmTime % 10 >= 5 || pmTime % 10 == 0)
			return pmTime + " minut";
		else if (pmTime % 10 > 1 && pmTime % 10 < 5)
			return pmTime + " minuty";

		return "";
	}

	public static void initializeWorkDirs() throws Exception
	{
		File lvDataDir = new File(DATA_DIR);
		if (!lvDataDir.exists())
		{
			if (!lvDataDir.mkdir())
				throw new Exception("");
		}
		else if (lvDataDir.exists() && !lvDataDir.isDirectory())
		{
			if (!lvDataDir.delete() && lvDataDir.mkdir())
				throw new Exception("");
		}

		File lvSettings = new File(SETTINGS_FILE_PATH);
		if (!lvSettings.exists())
			if (!lvSettings.createNewFile())
				throw new Exception("");

		ClientProperties.setServerAddress();
	}

	public static File getDataDir()
	{
		return new File(DATA_DIR);
	}

	public static File getSettingsFile()
	{
		return new File(SETTINGS_FILE_PATH);
	}

	public static void saveMapFile(byte[] pmMapData) throws Exception
	{
		FileOutputStream lvFOS = null;
		try
		{
			File lvMapFile = new File(MAP_FILE_PATH);
			if (!lvMapFile.exists())
				if (!lvMapFile.createNewFile())
					throw new ApplicationException("", "Nie mo¿na zapisaæ pliku mapy.");

			lvFOS = new FileOutputStream(new File(MAP_FILE_PATH));
			lvFOS.write(pmMapData);
			lvFOS.flush();
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

	public static byte[] loadMapFile() throws Exception
	{
		FileInputStream lvFIS = null;
		ByteArrayOutputStream lvBAOS = new ByteArrayOutputStream(2048);
		try
		{
			File lvMapFile = new File(MAP_FILE_PATH);
			if (!lvMapFile.exists())
				return null;

			lvFIS = new FileInputStream(lvMapFile);

			byte[] lvBuffer = new byte[2048];
			int lvRead;
			while ((lvRead = lvFIS.read(lvBuffer)) != -1)
				lvBAOS.write(lvBuffer, 0, lvRead);

			return lvBAOS.toByteArray();
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

	public static void updateMapCheckSum(byte[] pmMapDate) throws Exception
	{
		MessageDigest lvDigest = MessageDigest.getInstance("MD5");
		lvDigest.update(pmMapDate);

		byte[] lvResult = lvDigest.digest();

		StringBuffer lvBuffer = new StringBuffer();
		for (byte b : lvResult)
			lvBuffer.append((char) b);

		ClientProperties.setMapCheckSum(lvBuffer.toString());
	}

}
