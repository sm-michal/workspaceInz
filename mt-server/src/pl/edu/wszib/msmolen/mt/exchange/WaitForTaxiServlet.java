package pl.edu.wszib.msmolen.mt.exchange;

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

import pl.edu.wszib.msmolen.mt.common.exchange.Const;
import pl.edu.wszib.msmolen.mt.db.DbUtils;

/**
 * Servlet implementation class WaitForTaxiServlet
 */
@WebServlet("/exchange/waitForTaxi")
public class WaitForTaxiServlet extends BasicExchangeServlet
{
	private static final long serialVersionUID = -2716770990902628880L;

	@Override
	protected void processRequest(HttpServletResponse pmResponse, Object[] pmObjects) throws Exception
	{
		int lvOrderId = (int) pmObjects[0];

		ObjectOutputStream lvOOS = null;
		try
		{
			lvOOS = new ObjectOutputStream(pmResponse.getOutputStream());
			lvOOS.writeObject(Const.MESSAGE_OK);
			lvOOS.writeObject(getOrderStatus(lvOrderId));
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

	private String getOrderStatus(int pmOrderId) throws Exception
	{
		Connection lvConnection = null;
		PreparedStatement lvStmt = null;
		ResultSet lvResult = null;
		try
		{
			lvConnection = DbUtils.getConnection();
			lvStmt = lvConnection.prepareStatement("SELECT STATUS FROM MT_ZAMOWIENIA WHERE ID = ?");
			lvStmt.setInt(1, pmOrderId);
			lvResult = lvStmt.executeQuery();
			if (lvResult.next())
				return lvResult.getString(1);
		}
		finally
		{
			DbUtils.close(lvResult, lvStmt, lvConnection);
		}
		throw new Exception("Nie odnaleziono zamówienia id = " + pmOrderId);
	}
}
