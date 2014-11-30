package pl.edu.wszib.msmolen.mt.db;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Klasa z metodami pomocniczymi do obslugi bazy danych
 * 
 * @author msmolen
 * 
 */
public class DbUtils
{
	/** Link do bazy danych */
	private static final String DB_URL = "jdbc:postgresql://localhost:5432/mobiltaxidb";
	/** Uzytkownik bazy danych */
	private static final String DB_USER = "postgres";
	/** Haslo bazy danych */
	private static final String DB_PASSWORD = "12345678";

	// zaladowanie klasy sterownika bazy danych
	static
	{
		try
		{
			Class.forName("org.postgresql.Driver");
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Pobiera polaczenie do bazy danych
	 * 
	 * @return polaczenie
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException
	{
		Properties lvProps = new Properties();
		lvProps.put("user", DB_USER);
		lvProps.put("password", DB_PASSWORD);
		return DriverManager.getConnection(DB_URL, lvProps);
	}

	/**
	 * Zamyka poszczegolne obiekty dostepu do bazy danych poprzez wywolanie
	 * metody close() dla kazdego z nich. Wyjatki sa ukrywane.
	 * 
	 * @param pmObjects
	 */
	public static void close(Object... pmObjects)
	{
		if (pmObjects != null)
			for (Object o : pmObjects)
			{
				try
				{
					Method lvMethod = o.getClass().getMethod("close");
					lvMethod.invoke(o);
				}
				catch (Exception e)
				{
				}
			}
	}

	/**
	 * Wycofuje transakcje, nie wyrzuca wyjatku.
	 * 
	 * @param pmConnection
	 */
	public static void rollback(Connection pmConnection)
	{
		if (pmConnection != null)
		{
			try
			{
				pmConnection.rollback();
			}
			catch (Exception e)
			{
			}
		}
	}
}
