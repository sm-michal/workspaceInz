package pl.edu.wszib.msmolen.mt.exchange;

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

import pl.edu.wszib.msmolen.mt.common.exchange.Const;
import pl.edu.wszib.msmolen.mt.core.OrdersProvider;
import pl.edu.wszib.msmolen.mt.db.DbUtils;

/**
 * Servlet implementation class OrderTaxiServlet
 */
@WebServlet("/exchange/orderTaxi")
public class OrderTaxiServlet extends BasicExchangeServlet implements Servlet
{

	private static final long serialVersionUID = -395760422554791626L;

	@Override
	protected void processRequest(HttpServletResponse pmResponse, Object[] pmObjects) throws Exception
	{
		double lvLattitude = (double) pmObjects[0];
		double lvLongitude = (double) pmObjects[1];
		String lvOperation = (String) pmObjects[2];
		int lvArriveTime = OrdersProvider.calculateTaxiArriveTime(lvLattitude, lvLongitude);
		int lvOrderId = -1;
		if (Const.ORDER_OP_ORDER.equals(lvOperation))
		{
			lvOrderId = addOrder(lvLattitude, lvLongitude);
		}

		ObjectOutputStream lvOOS = null;
		try
		{
			lvOOS = new ObjectOutputStream(pmResponse.getOutputStream());
			lvOOS.writeObject(Const.MESSAGE_OK);
			lvOOS.writeObject(lvArriveTime);
			if (Const.ORDER_OP_ORDER.equals(lvOperation))
			{
				lvOOS.writeObject(lvOrderId);
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

	private int addOrder(double pmLattitude, double pmLongitude) throws Exception
	{
		Connection lvConnection = null;
		PreparedStatement lvStmt = null;
		ResultSet lvResult = null;
		try
		{
			lvConnection = DbUtils.getConnection();
			lvStmt = lvConnection.prepareStatement("INSERT INTO MT_ZAMOWIENIA (ID, CZAS_ZAMOWIENIA, START_X, START_Y) "
					+ "(SELECT NEXTVAL('MT_ZAMOWIENIA_SEQ'), CURRENT_TIMESTAMP, ?, ?) RETURNING ID");
			lvStmt.setDouble(1, pmLongitude);
			lvStmt.setDouble(2, pmLattitude);
			lvResult = lvStmt.executeQuery();
			if (lvResult.next())
			{
				return lvResult.getInt(1);
			}
		}
		finally
		{
			DbUtils.close(lvStmt, lvConnection);
		}
		throw new Exception("Nie udało się zapisać zamówienia.");
	}

}
