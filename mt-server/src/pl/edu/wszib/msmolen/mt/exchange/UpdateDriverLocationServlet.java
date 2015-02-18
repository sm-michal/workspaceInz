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
 * Servlet implementation class UpdateDriverLocationServlet
 */
@WebServlet("/exchange/updateDriver")
public class UpdateDriverLocationServlet extends BasicExchangeServlet implements Servlet
{

	private static final long serialVersionUID = -4791181774164560920L;

	@Override
	protected void processRequest(HttpServletResponse pmResponse, Object[] pmObjects) throws Exception
	{
		int lvDriverId = (int) pmObjects[0];
		double lvLatitude = (double) pmObjects[1];
		double lvLongitude = (double) pmObjects[2];
		boolean lvFinished = (boolean) pmObjects[3];

		ObjectOutputStream lvOOS = null;
		Connection lvConnection = null;
		try
		{
			lvConnection = DbUtils.getConnection();

			updateDriverLocation(lvConnection, lvDriverId, lvLatitude, lvLongitude);

			if (lvFinished)
			{
				increaseOrderStatus(lvConnection, lvDriverId);
			}

			lvOOS = new ObjectOutputStream(pmResponse.getOutputStream());
			lvOOS.writeObject(Const.MESSAGE_OK);
		}
		finally
		{
			DbUtils.close(lvConnection);

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

	private void updateDriverLocation(Connection pmConnection, int pmDriverId, double pmLat, double pmLon)
	{
		PreparedStatement lvStmt = null;
		try
		{
			lvStmt = pmConnection.prepareStatement("UPDATE MT_TAKSOWKARZE SET POLOZENIE_Y = ?, POLOZENIE_X = ? WHERE ID = ?");
			lvStmt.setDouble(1, pmLat);
			lvStmt.setDouble(2, pmLon);
			lvStmt.setInt(3, pmDriverId);
			lvStmt.executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.close(lvStmt);
		}
	}

	private void increaseOrderStatus(Connection pmConnection, int pmDriverId)
	{
		PreparedStatement lvStmt = null;
		try
		{
			lvStmt = pmConnection.prepareStatement("UPDATE MT_ZAMOWIENIA SET STATUS = TO_CHAR(TO_NUMBER(STATUS,'9')+1,'9') "
					+ "WHERE TAKSOWKARZ_ID = ? AND STATUS <> '4' ORDER BY ID DESC");
			lvStmt.setInt(1, pmDriverId);
			lvStmt.executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.close(lvStmt);
		}
	}
}
