package pl.edu.wszib.msmolen.mt.exchange;

import java.io.File;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

import pl.edu.wszib.msmolen.mt.common.exchange.Const;
import pl.edu.wszib.msmolen.mt.exchange.map.MapRequest;

/**
 * Servlet implementation class DownloadMapServlet
 */
@WebServlet("/exchange/downloadMap")
public class DownloadMapServlet extends BasicExchangeServlet implements Servlet
{
	private static final long serialVersionUID = 3946038008970019214L;

	private static final String MAP_DIR = "data";
	private static final String MAP_FILE_PATH = MAP_DIR + File.separator + "map.dat";

	private static final int ONE_DAY = 24 * 60 * 60 * 1000;

	@Override
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);

		File lvMapDir = new File(MAP_DIR);
		if (!lvMapDir.exists())
			lvMapDir.mkdir();

		Timer lvTimer = new Timer();
		lvTimer.scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run()
			{
				File lvMapFile = new File(MAP_FILE_PATH);
				if (!lvMapFile.exists())
				{
					MapRequest.downloadMapImage(lvMapFile);
				}
				else if (lvMapFile.exists())
				{
					long lvDelay = System.currentTimeMillis() - lvMapFile.lastModified();
					if (lvDelay / ONE_DAY >= 1)
					{
						lvMapFile.delete();
						MapRequest.downloadMapImage(lvMapFile);
					}
				}

			}
		}, 0, ONE_DAY);
	}

	@Override
	protected void processRequest(HttpServletResponse pmResponse, Object[] pmObjects) throws Exception
	{
		ObjectOutputStream lvOOS = null;

		try
		{
			File lvMapFile = new File(MAP_FILE_PATH);

			String lvClientSum = (String) pmObjects[0];
			String lvServerSum = computeFileChecksum(lvMapFile);

			lvOOS = new ObjectOutputStream(pmResponse.getOutputStream());
			lvOOS.writeObject(Const.MESSAGE_OK);
			if (!lvServerSum.equals(lvClientSum))
			{
				lvOOS.writeObject(Const.MAP_UPDATED);
				lvOOS.writeObject(Files.readAllBytes(lvMapFile.toPath()));
			}
			else
			{
				lvOOS.writeObject(Const.MAP_CURRENT);
			}
		}
		finally
		{
			if (lvOOS != null)
				try
				{
					lvOOS.close();
				}
				catch (Exception e)
				{
				}
		}
	}

	private String computeFileChecksum(File pmFile) throws Exception
	{
		MessageDigest lvDigest = MessageDigest.getInstance("MD5");
		lvDigest.update(Files.readAllBytes(pmFile.toPath()));

		byte[] lvResult = lvDigest.digest();

		StringBuffer lvBuffer = new StringBuffer();
		for (byte b : lvResult)
			lvBuffer.append((char) b);

		return lvBuffer.toString();
	}

}
