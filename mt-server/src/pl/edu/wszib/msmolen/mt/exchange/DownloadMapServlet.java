package pl.edu.wszib.msmolen.mt.exchange;

import java.io.File;
import java.io.ObjectOutputStream;
import java.nio.file.Files;

import javax.servlet.Servlet;
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

	@Override
	protected void processRequest(HttpServletResponse pmResponse, Object[] pmObjects) throws Exception
	{
		ObjectOutputStream lvOOS = null;

		File lvMapFile = null;
		try
		{
			lvOOS = new ObjectOutputStream(pmResponse.getOutputStream());
			lvOOS.writeObject(Const.MESSAGE_OK);
			lvMapFile = MapRequest.getMapImage();
			lvOOS.writeObject(Files.readAllBytes(lvMapFile.toPath()));
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

			if (lvMapFile != null)
				lvMapFile.delete();
		}
	}

}
