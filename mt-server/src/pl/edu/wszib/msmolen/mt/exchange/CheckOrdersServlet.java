package pl.edu.wszib.msmolen.mt.exchange;

import java.io.File;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

import pl.edu.wszib.msmolen.mt.common.exchange.Const;
import pl.edu.wszib.msmolen.mt.common.utils.Orders;
import pl.edu.wszib.msmolen.mt.core.PathFinder;
import pl.edu.wszib.msmolen.mt.db.DbUtils;
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
		Connection lvConnection = null;
		Statement lvStmt = null;
		PreparedStatement lvStmt2 = null;
		ResultSet lvResult = null;
		ResultSet lvResult2 = null;
		try
		{
			lvConnection = DbUtils.getConnection();
			lvStmt = lvConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			lvResult = lvStmt.executeQuery("SELECT ID, START_Y, START_X, STATUS, TAKSOWKARZ_ID FROM MT_ZAMOWIENIA "
					+ "WHERE STATUS = '1' AND TAKSOWKARZ_ID IS NULL ORDER BY CZAS_ZAMOWIENIA FOR UPDATE");
			if (lvResult.next())
			{
				lvStmt2 = lvConnection.prepareStatement("SELECT POLOZENIE_Y, POLOZENIE_X FROM MT_TAKSOWKARZE WHERE ID = ?");
				lvStmt2.setInt(1, pmDriverId);
				lvResult2 = lvStmt2.executeQuery();
				if (lvResult2.next())
				{
					lvResult.updateString(4, "2");
					lvResult.updateInt(5, pmDriverId);
					lvResult.updateRow();

					return new Orders(lvResult.getInt(1), "", lvResult2.getDouble(1), lvResult2.getDouble(2),
							PathFinder.getPathForDriver(lvResult2.getDouble(1), lvResult2.getDouble(2), lvResult.getDouble(2), lvResult.getDouble(3)));
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.close(lvResult, lvStmt, lvConnection);
		}
		return null;
	}

}
