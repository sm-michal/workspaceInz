package pl.edu.wszib.msmolen.mt.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import pl.edu.wszib.msmolen.mt.common.utils.exceptions.ApplicationException;
import pl.edu.wszib.msmolen.mt.core.model.Crossing;
import pl.edu.wszib.msmolen.mt.db.DbUtils;

public class OrdersProvider
{
	/**
	 * 
	 * Oblicza czas przybycia najblizszej taksowki
	 * 
	 * petla po taksowkarzach, dla kazdego aktywnego:
	 * 
	 * jesli stoi, to obliczenie czasu dojazdu
	 * jesli jedzie, to pozostaly czas dojazdu + obliczony czas dojazdu
	 * wyznaczenie najmniejszego czasu i zwrocenie
	 * 
	 * jesli zaden nie jest aktywnym to exception
	 * 
	 * @param pmDestinationLattitude
	 * @param pmDestinationLongitude
	 * @return
	 * @throws Exception
	 */
	public static int calculateTaxiArriveTime(double pmDestinationLattitude, double pmDestinationLongitude) throws Exception
	{
		int lvResult = Integer.MAX_VALUE;

		Connection lvConnection = null;
		PreparedStatement lvStmt = null;
		ResultSet lvResultSet = null;
		try
		{
			lvConnection = DbUtils.getConnection();
			lvStmt = lvConnection.prepareStatement(
					"SELECT T.ID, T.POLOZENIE_Y, T.POLOZENIE_X, Z.STATUS, Z.KONIEC_Y, Z.KONIEC_X FROM MT_TAKSOWKARZE T "
							+ "LEFT OUTER JOIN MT_ZAMOWIENIA Z ON Z.TAKSOWKARZ_ID = T.ID "
							+ "WHERE T.CZY_AKTYWNY = ? AND COALESCE(Z.STATUS,'3') = '3'");
			lvStmt.setBoolean(1, true);
			lvResultSet = lvStmt.executeQuery();
			while (lvResultSet.next())
			{
				String lvOrderStatus = lvResultSet.getString(4);
				int lvTime;
				if (lvOrderStatus == null)
				{
					lvTime = calculateArriveTime(lvResultSet.getDouble(2), lvResultSet.getDouble(3), pmDestinationLattitude, pmDestinationLongitude);
				}
				else
				{
					lvTime = calculateArriveTime(lvResultSet.getDouble(2), lvResultSet.getDouble(3), lvResultSet.getDouble(5), lvResultSet.getDouble(6));
					lvTime += calculateArriveTime(lvResultSet.getDouble(5), lvResultSet.getDouble(6), pmDestinationLattitude, pmDestinationLongitude);
				}
				if (lvTime < lvResult)
				{
					lvResult = lvTime;
				}
			}

			if (lvResult == Integer.MAX_VALUE)
				throw new ApplicationException("Informacja", "Aktualnie nie ma żadnej dostępnej taksówki. Spróbuj ponownie za chwilę.");
		}
		finally
		{
			DbUtils.close(lvResultSet, lvStmt, lvConnection);
		}

		return lvResult;
	}

	private static int calculateArriveTime(double pmSourceLattitude, double pmSourceLongitude, double pmDestinationLattitude, double pmDestinationLongitude)
	{
		Crossing lvSource = PathFinder.getClosestCrosing(pmSourceLattitude, pmSourceLongitude);
		Crossing lvDestination = PathFinder.getClosestCrosing(pmDestinationLattitude, pmDestinationLongitude);
		return PathFinder.getRideTime(lvSource.getId(), lvDestination.getId());
	}
}
