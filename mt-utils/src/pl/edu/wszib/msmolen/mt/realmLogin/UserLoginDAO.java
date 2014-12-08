package pl.edu.wszib.msmolen.mt.realmLogin;

import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import pl.edu.wszib.msmolen.mt.common.auth.UserType;
import pl.edu.wszib.msmolen.mt.db.DbUtils;
import pl.edu.wszib.msmolen.mt.display.MenuItem;

public class UserLoginDAO
{
	private Connection mConnection;

	public UserLoginDAO()
	{
		try
		{
			mConnection = DbUtils.getConnection();
		}
		catch (Exception e)
		{
			mConnection = null;
		}
	}

	@Override
	protected void finalize() throws Throwable
	{
		DbUtils.close(mConnection);
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private Connection getConnection() throws Exception
	{
		if (mConnection == null || mConnection.isClosed())
			mConnection = DbUtils.getConnection();

		return mConnection;
	}

	/**
	 * 
	 * @param pmUser
	 * @return
	 */
	public String getUserPassword(String pmUserName)
	{
		PreparedStatement lvStmt = null;
		ResultSet lvResult = null;
		try
		{
			lvStmt = getConnection().prepareStatement("SELECT HASLO FROM MT_UZYTKOWNICY WHERE NAZWA_UZYTKOWNIKA = ?");
			lvStmt.setString(1, pmUserName);
			lvResult = lvStmt.executeQuery();
			if (lvResult.next())
				return lvResult.getString(1);
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
		finally
		{
			DbUtils.close(lvResult, lvStmt);
		}
		return null;
	}

	public Principal getUser(String pmUserName)
	{
		PreparedStatement lvStmt = null;
		PreparedStatement lvStmtRole = null;
		PreparedStatement lvStmtMenu = null;
		ResultSet lvResult = null;
		ResultSet lvResultRole = null;
		ResultSet lvResultMenu = null;
		try
		{
			lvStmt = getConnection().prepareStatement("SELECT U.ID, U.HASLO, T.ID FROM MT_UZYTKOWNICY U "
					+ "LEFT JOIN MT_TAKSOWKARZE T ON T.UZYTKOWNIK_ID = U.ID "
					+ "WHERE U.NAZWA_UZYTKOWNIKA = ?");
			lvStmt.setString(1, pmUserName);
			lvResult = lvStmt.executeQuery();
			if (lvResult.next())
			{
				int lvId = lvResult.getInt(1);
				String lvPassword = lvResult.getString(2);
				List<CustomRole> lvRoles = new ArrayList<CustomRole>();
				List<MenuItem> lvMenus = new ArrayList<MenuItem>();

				lvStmtRole = getConnection().prepareStatement(
						"SELECT R.ID_ROLI, R.NAZWA_ROLI, R.OPIS FROM MT_ROLE R "
								+ "JOIN MT_ROLE_UZYTKOWNIKOW RU ON R.ID_ROLI = RU.ROLA_ID "
								+ "WHERE RU.UZYTKOWNIK_ID = ?");
				lvStmtRole.setInt(1, lvId);
				lvResultRole = lvStmtRole.executeQuery();
				while (lvResultRole.next())
				{
					lvRoles.add(new CustomRole(lvResultRole.getInt(1), lvResultRole.getString(2), lvResultRole.getString(3)));
				}

				lvStmtMenu = getConnection().prepareStatement(
						"SELECT M.ID, M.TEKST, M.URL FROM MT_POZYCJE_MENU M "
								+ "JOIN MT_MENU_ROLE MR ON MR.MENU_ID = M.ID "
								+ "WHERE MR.ROLA_ID IN (SELECT ROLA_ID FROM MT_ROLE_UZYTKOWNIKOW WHERE UZYTKOWNIK_ID = ?)");
				lvStmtMenu.setInt(1, lvId);
				lvResultMenu = lvStmtMenu.executeQuery();
				while (lvResultMenu.next())
				{
					lvMenus.add(new MenuItem(lvResultMenu.getInt(1), lvResultMenu.getString(2), lvResultMenu.getString(3)));
				}

				return new CustomUser(lvId, pmUserName, lvPassword, lvMenus, lvRoles,
						!lvRoles.isEmpty() ? UserType.ADMIN : lvResult.getString(3) != null ? UserType.DRIVER : UserType.CLIENT);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.close(lvResult, lvResultRole, lvStmtRole, lvStmt);
		}
		return null;
	}
}
