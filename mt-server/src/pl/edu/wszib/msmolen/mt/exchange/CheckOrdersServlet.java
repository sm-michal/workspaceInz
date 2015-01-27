package pl.edu.wszib.msmolen.mt.exchange;

import java.io.File;
import java.io.ObjectOutputStream;
import java.nio.file.Files;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

import pl.edu.wszib.msmolen.mt.common.exchange.Const;
import pl.edu.wszib.msmolen.mt.common.utils.Orders;
import pl.edu.wszib.msmolen.mt.exchange.map.MapRequest;

/**
 * Servlet pobierajacy aktualne zlecenie dla taksowkarza
 * 
 * @author msmolen
 * 
 */
@WebServlet("/exchange/checkOrders")
public class CheckOrdersServlet extends BasicExchangeServlet
{
	private static final long serialVersionUID = -1019866005331353759L;

	@Override
	protected void processRequest(HttpServletResponse pmResponse, Object[] pmObjects) throws Exception
	{
		ObjectOutputStream lvOOS = null;

		File lvMapFile = null;
		try
		{
			lvMapFile = File.createTempFile("orderMap", ".dat");
			int lvDriverId = (int) pmObjects[0];
			Orders lvOrders = getActualOrder(lvDriverId);

			lvOOS = new ObjectOutputStream(pmResponse.getOutputStream());
			lvOOS.writeObject(Const.MESSAGE_OK);
			lvOOS.writeObject(lvOrders != null ? lvOrders : Const.MESSAGE_NOTHING);
			if (lvOrders != null)
			{
				MapRequest.downloadMapImage(lvMapFile);
				lvOOS.writeObject(Files.readAllBytes(lvMapFile.toPath()));
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

			if (lvMapFile != null)
				lvMapFile.delete();
		}
	}

	private Orders getActualOrder(int pmDriverId)
	{
		return new Orders();
	}

}
