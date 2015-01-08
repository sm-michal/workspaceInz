package pl.edu.wszib.msmolen.mt.taxiDrivers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pl.edu.wszib.msmolen.mt.common.auth.User;
import pl.edu.wszib.msmolen.mt.common.auth.UserType;
import pl.edu.wszib.msmolen.mt.common.utils.EncryptUtils;
import pl.edu.wszib.msmolen.mt.common.utils.exceptions.ApplicationException;
import pl.edu.wszib.msmolen.mt.db.DbUtils;
import pl.edu.wszib.msmolen.mt.display.Column;
import pl.edu.wszib.msmolen.mt.login.LoginUtils;

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
	public void load() throws Exception
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
					+ "FROM MT_TAKSOWKARZE T JOIN MT_UZYTKOWNICY U ON U.ID = T.UZYTKOWNIK_ID ORDER BY ID");
			while (lvResult.next())
			{
				data.add(new TaxiDriver(
						lvResult.getInt(1),
						lvResult.getString(2),
						lvResult.getString(3),
						new User(
								lvResult.getInt(4),
								lvResult.getString(5),
								lvResult.getString(6).toCharArray(),
								UserType.DRIVER,
								lvResult.getInt(1))));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new Exception("Wystąpił nieoczekiwany błąd podczas ładowania listy.");
		}
		finally
		{
			DbUtils.close(lvResult, lvStmt, lvConn);
		}
	}

	/**
	 * Aktualizuje rekord taksowkarza oraz dane uzytkownika
	 * 
	 * @param pmDriver
	 */
	public void update(TaxiDriver pmDriver) throws Exception
	{
		Connection lvConn = null;
		PreparedStatement lvTaxiStmt = null;
		PreparedStatement lvUserStmt = null;
		try
		{
			lvConn = DbUtils.getConnection();
			lvConn.setAutoCommit(false);

			lvTaxiStmt = lvConn.prepareStatement("UPDATE MT_TAKSOWKARZE SET IMIE = ?, NAZWISKO = ? WHERE ID = ?");
			lvTaxiStmt.setString(1, pmDriver.getName());
			lvTaxiStmt.setString(2, pmDriver.getSurname());
			lvTaxiStmt.setInt(3, pmDriver.getId());
			lvTaxiStmt.executeUpdate();

			lvUserStmt = lvConn.prepareStatement("UPDATE MT_UZYTKOWNICY SET NAZWA_UZYTKOWNIKA = ?" + (pmDriver.getUser().getPassword() != null ? ", HASLO = ?" : "") +
					" WHERE ID = ? AND NOT EXISTS (SELECT 1 FROM MT_UZYTKOWNICY WHERE NAZWA_UZYTKOWNIKA = ? AND ID <> ?)");
			lvUserStmt.setString(1, pmDriver.getUser().getName());
			if (pmDriver.getUser().getPassword() != null)
			{
				lvUserStmt.setString(2, EncryptUtils.encrypt(new String(pmDriver.getUser().getPassword())));
				lvUserStmt.setInt(3, pmDriver.getUser().getId());
				lvUserStmt.setString(4, pmDriver.getUser().getName());
				lvUserStmt.setInt(5, pmDriver.getUser().getId());
			}
			else
			{
				lvUserStmt.setInt(2, pmDriver.getUser().getId());
				lvUserStmt.setString(3, pmDriver.getUser().getName());
				lvUserStmt.setInt(4, pmDriver.getUser().getId());
			}
			if (lvUserStmt.executeUpdate() == 0)
				throw new ApplicationException("", "Istnieje już użytkownik o takim samym loginie. Należy podać inny login.");

			lvConn.commit();
		}
		catch (ApplicationException e)
		{
			e.printStackTrace();
			DbUtils.rollback(lvConn);
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			DbUtils.rollback(lvConn);
			throw new Exception("Nie udało się zapisać danych taksówkarza.");
		}
		finally
		{
			DbUtils.close(lvUserStmt, lvTaxiStmt, lvConn);
		}
	}

	/**
	 * Tworzy nowy rekord taksowkarza
	 * 
	 * @param pmDriver
	 */
	public void create(TaxiDriver pmDriver) throws Exception
	{
		Connection lvConn = null;
		PreparedStatement lvStmt = null;
		try
		{
			lvConn = DbUtils.getConnection();
			lvConn.setAutoCommit(false);

			User lvUser = LoginUtils.registerUser(lvConn, pmDriver.getUser().getName(), EncryptUtils.encrypt(new String(pmDriver.getUser().getPassword())), false);

			lvStmt = lvConn.prepareStatement("INSERT INTO MT_TAKSOWKARZE (ID, IMIE, NAZWISKO, UZYTKOWNIK_ID) VALUES (NEXTVAL('MT_TAKSOWKARZE_SEQ'), ?, ?, ?)");
			lvStmt.setString(1, pmDriver.getName());
			lvStmt.setString(2, pmDriver.getSurname());
			lvStmt.setInt(3, lvUser.getId());
			lvStmt.executeUpdate();

			lvConn.commit();
		}
		catch (ApplicationException e)
		{
			e.printStackTrace();
			DbUtils.rollback(lvConn);
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			DbUtils.rollback(lvConn);
			throw new Exception("Nie udało się zapisać danych taksówkarza.");
		}
		finally
		{
			DbUtils.close(lvStmt, lvConn);
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
