package pl.edu.wszib.msmolen.mt.exchange;

import java.io.ObjectOutputStream;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

import pl.edu.wszib.msmolen.mt.common.auth.User;
import pl.edu.wszib.msmolen.mt.common.exchange.Const;
import pl.edu.wszib.msmolen.mt.common.utils.exceptions.ApplicationException;
import pl.edu.wszib.msmolen.mt.login.LoginUtils;

/**
 * Servlet pobierajacy dane uzytkownika. Wykonuje operacje logowania oraz
 * rejestracji.
 */
@WebServlet("/exchange/getUser")
public class GetUserServlet extends BasicExchangeServlet
{

	private static final long serialVersionUID = -7598580791683645501L;

	@Override
	protected void processRequest(HttpServletResponse pmResponse, Object[] pmObjects) throws Exception
	{
		ObjectOutputStream lvOOS = null;
		try
		{
			String lvMode = (String) pmObjects[0];
			String lvLogin = (String) pmObjects[1];
			String lvPassword = null;
			if (pmObjects.length > 2)
			{
				lvPassword = (String) pmObjects[2];
			}

			User lvUser = null;
			if (Const.MODE_REGISTER.equals(lvMode))
			{
				lvUser = LoginUtils.registerUser(lvLogin, lvPassword);
			}
			else if (Const.MODE_LOGIN.equals(lvMode))
			{
				lvUser = LoginUtils.loginUser(lvLogin, lvPassword);
			}
			else if (Const.MODE_LOGOUT.equals(lvMode))
			{
				LoginUtils.logDriverLogout(lvLogin);

				lvOOS = new ObjectOutputStream(pmResponse.getOutputStream());
				lvOOS.writeObject(Const.MESSAGE_OK);

				return;
			}

			if (lvUser != null)
			{
				lvOOS = new ObjectOutputStream(pmResponse.getOutputStream());
				lvOOS.writeObject(Const.MESSAGE_OK);
				lvOOS.writeObject(lvUser);
			}
			else
			{
				throw new ApplicationException("Błąd", "Nie udało się pobrać użytkownika.");
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
		}
	}
}
