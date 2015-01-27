package pl.edu.wszib.msmolen.mt.exchange.map;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class MapRequest
{
	private static final String MAP_ADDRESS = "https://maps.googleapis.com/maps/api/staticmap?center=50.061781,19.937242&size=640x640&zoom=15&scale=1";

	public static void downloadMapImage(File pmDestinationFile)
	{
		InputStream lvIS = null;
		FileOutputStream lvFOS = null;
		try
		{
			lvIS = new URL(MAP_ADDRESS).openStream();

			if (!pmDestinationFile.exists())
				pmDestinationFile.createNewFile();

			lvFOS = new FileOutputStream(pmDestinationFile);

			byte[] lvTmpTab = new byte[2048];
			int lvRead;
			while ((lvRead = lvIS.read(lvTmpTab)) != -1)
				lvFOS.write(lvTmpTab, 0, lvRead);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (lvIS != null)
				try
				{
					lvIS.close();
				}
				catch (Exception e)
				{
				}
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
}
