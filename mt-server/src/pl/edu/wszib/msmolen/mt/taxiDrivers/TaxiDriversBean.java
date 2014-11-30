package pl.edu.wszib.msmolen.mt.taxiDrivers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pl.edu.wszib.msmolen.mt.common.auth.User;
import pl.edu.wszib.msmolen.mt.db.DbUtils;
import pl.edu.wszib.msmolen.mt.display.Column;

/**
 * Klasa dostepu do danych taksowkarzy
 * 
 * @author msmolen
 * 
 */
public class TaxiDriversBean
{
	/**
	 * Przechowuje liste taksowkarzy
	 */
	private List<TaxiDriver> data = new ArrayList<TaxiDriver>();

	private final List<Column> columns = new ArrayList<Column>();

	public TaxiDriversBean()
	{
		initColumns();
	}

	/**
	 * Pobiera dane taksowkarzy z bazy
	 */
	public void load()
	{
		data = new ArrayList<TaxiDriver>();

		Connection lvConn = null;
		Statement lvStmt = null;
		ResultSet lvResult = null;
		try
		{
			lvConn = DbUtils.getConnection();
			lvStmt = lvConn.createStatement();
			lvResult = lvStmt.executeQuery("SELECT T.ID, T.IMIE, T.NAZWISKO, T.UZYTKOWNIK_ID, U.NAZWA_UZYTKOWNIKA, U.HASLO "
					+ "FROM MT_TAKSOWKARZE T JOIN MT_UZYTKOWNICY U ON U.ID = T.UZYTKOWNIK_ID");
			while (lvResult.next())
			{
				data.add(new TaxiDriver(
						lvResult.getInt(1),
						lvResult.getString(2),
						lvResult.getString(3),
						new User(
								lvResult.getInt(4),
								lvResult.getString(5),
								lvResult.getString(6).toCharArray())));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.close(lvResult, lvStmt, lvConn);
		}
	}

	/**
	 * 
	 * @param pmDriverId
	 * @return
	 */
	public TaxiDriver getById(int pmDriverId)
	{
		for (TaxiDriver driver : data)
			if (driver.getId() == pmDriverId)
				return driver;

		return null;
	}

	/**
	 * Zwraca dane taksowkarzy
	 * 
	 * @return
	 */
	public List<TaxiDriver> getData()
	{
		return data;
	}

	public List<Column> getColumns()
	{
		return columns;
	}

	private void initColumns()
	{
		columns.add(new Column(0, "Imię", "MT_TAKSOWKARZE.IMIE", true, false));
		columns.add(new Column(1, "Nazwisko", "MT_TAKSOWKARZE.NAZWISKO", true, false));
		columns.add(new Column(2, "Login", "MT_UZYTKOWNICY.LOGIN", true, false));
	}
}
