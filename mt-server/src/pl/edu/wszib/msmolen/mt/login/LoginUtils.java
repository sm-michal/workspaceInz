package pl.edu.wszib.msmolen.mt.login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import pl.edu.wszib.msmolen.mt.common.auth.User;
import pl.edu.wszib.msmolen.mt.common.auth.UserType;
import pl.edu.wszib.msmolen.mt.common.utils.exceptions.ApplicationException;
import pl.edu.wszib.msmolen.mt.db.DbUtils;

/**
 * Klasa z metodami pomocniczymi dotyczacymi procesu logowania i rejestracji
 * 
 * @author msmolen
 * 
 */
public class LoginUtils
{

	/**
	 * Pobiera uzytkownika wg podanego loginu i hasla
	 * 
	 * @param pmUserName
	 * @param pmPassword
	 * @return
	 * @throws ApplicationException
	 *             jesli logowanie sie nie powiodlo
	 */
	public static User loginUser(String pmUserName, String pmPassword) throws ApplicationException
	{
		Connection lvConn = null;
		PreparedStatement lvStmt = null;
		ResultSet lvResult = null;
		try
		{
			lvConn = DbUtils.getConnection();
			lvStmt = lvConn.prepareStatement("SELECT U.ID, U.NAZWA_UZYTKOWNIKA, U.HASLO, T.ID FROM MT_UZYTKOWNICY U "
					+ "LEFT JOIN MT_TAKSOWKARZE T ON T.UZYTKOWNIK_ID = U.ID"
					+ "WHERE U.NAZWA_UZYTKOWNIKA = ? AND U.HASLO = ?");
			lvStmt.setString(1, pmUserName);
			lvStmt.setString(2, pmPassword);
			lvResult = lvStmt.executeQuery();
			if (lvResult.next())
				return new User(lvResult.getInt(1), lvResult.getString(2), lvResult.getString(3).toCharArray(), lvResult.getString(4) != null ? UserType.DRIVER : UserType.CLIENT);
			else
				throw new ApplicationException("Błąd logowania", "Logowanie nie powiodło się. Podano nieprawidłowy login lub hasło.");
		}
		catch (ApplicationException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new ApplicationException("Błąd logowania", "Wystąpił nieoczekiwany błąd podczas rejestracji użytkownika: " + e.getMessage());
		}
		finally
		{
			DbUtils.close(lvResult, lvStmt, lvConn);
		}
	}

	/**
	 * Rejestruje uzytkownika - dodaje go do bazy, jezeli nie istnieje inny
	 * uzytkownik o takim samym loginie.
	 * 
	 * @param pmUserName
	 * @param pmPassword
	 * @return
	 * @throws ApplicationException
	 *             jesli rejestracja sie nie powiodla
	 */
	public static User registerUser(String pmUserName, String pmPassword) throws ApplicationException
	{
		Connection lvConn = null;
		try
		{
			lvConn = DbUtils.getConnection();
			return registerUser(lvConn, pmUserName, pmPassword);
		}
		catch (ApplicationException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new ApplicationException("Błąd rejestracji", "Wystąpił nieoczekiwany błąd podczas rejestracji użytkownika: " + e.getMessage());
		}
		finally
		{
			DbUtils.close(lvConn);
		}
	}

	public static User registerUser(Connection pmConnection, String pmUserName, String pmPassword) throws ApplicationException
	{
		PreparedStatement lvStmt = null;
		ResultSet lvResult = null;
		try
		{
			lvStmt = pmConnection.prepareStatement("INSERT INTO MT_UZYTKOWNICY (ID, NAZWA_UZYTKOWNIKA, HASLO) "
					+ "(SELECT NEXTVAL('MT_UZYTKOWNICY_SEQ'), ?, ? "
					+ "WHERE (SELECT COUNT(*) FROM MT_UZYTKOWNICY WHERE NAZWA_UZYTKOWNIKA = ?) = 0) RETURNING ID");
			lvStmt.setString(1, pmUserName);
			lvStmt.setString(2, pmPassword);
			lvStmt.setString(3, pmUserName);
			lvResult = lvStmt.executeQuery();
			if (lvResult.next())
			{
				return new User(lvResult.getInt(1), pmUserName, pmPassword.toCharArray(), UserType.CLIENT);
			}
			else
			{
				throw new ApplicationException(
						"Błędny login",
						"Istnieje już użytkownik o takim samym loginie. Należy podać inny login.");
			}
		}
		catch (ApplicationException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new ApplicationException("Błąd rejestracji", "Wystąpił nieoczekiwany błąd podczas rejestracji użytkownika: " + e.getMessage());
		}
		finally
		{
			DbUtils.close(lvResult, lvStmt);
		}
	}
}
