package pl.edu.wszib.msmolen.mt.exchange;

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

import pl.edu.wszib.msmolen.mt.common.exchange.Const;
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
		addOrder(lvLattitude, lvLongitude);

		ObjectOutputStream lvOOS = null;
		try
		{
			lvOOS = new ObjectOutputStream(pmResponse.getOutputStream());
			lvOOS.writeObject(Const.MESSAGE_OK);
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

	private void addOrder(double pmLattitude, double pmLongitude) throws Exception
	{
		Connection lvConnection = null;
		PreparedStatement lvStmt = null;
		try
		{
			lvConnection = DbUtils.getConnection();
			lvStmt = lvConnection.prepareStatement("INSERT INTO MT_ZAMOWIENIA (ID, CZAS_ZAMOWIENIA, START_X, START_Y) VALUES (NEXTVAL('MT_ZAMOWIENIA_SEQ'), CURRENT_TIMESTAMP, ?, ?)");
			lvStmt.setDouble(1, pmLongitude);
			lvStmt.setDouble(2, pmLattitude);
			lvStmt.executeUpdate();
		}
		finally
		{
			DbUtils.close(lvStmt, lvConnection);
		}
	}

}
