package pl.edu.wszib.msmolen.mt.exchange;

import java.io.ObjectOutputStream;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

import pl.edu.wszib.msmolen.mt.common.exchange.Const;
import pl.edu.wszib.msmolen.mt.common.utils.Orders;

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
		try
		{
			int lvDriverId = (int) pmObjects[0];
			Orders lvOrders = getActualOrder(lvDriverId);

			lvOOS = new ObjectOutputStream(pmResponse.getOutputStream());
			lvOOS.writeObject(Const.MESSAGE_OK);
			lvOOS.writeObject(lvOrders != null ? lvOrders : Const.MESSAGE_NOTHING);
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

	private Orders getActualOrder(int pmDriverId)
	{
		return null;
	}

}
