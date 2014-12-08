package pl.edu.wszib.msmolen.mt.exchange;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.edu.wszib.msmolen.mt.common.auth.Token;
import pl.edu.wszib.msmolen.mt.common.auth.User;
import pl.edu.wszib.msmolen.mt.common.exchange.Const;
import pl.edu.wszib.msmolen.mt.common.utils.EncryptUtils;
import pl.edu.wszib.msmolen.mt.db.DbUtils;
import pl.edu.wszib.msmolen.mt.login.LoginUtils;

/**
 * Servlet przydzielajacy identyfikatory poszczegolnym klientom i zapisujacy je
 * w bazie
 */
@WebServlet("/exchange/token")
public class TokenServlet extends HttpServlet
{

	private static final long serialVersionUID = 876221085444631091L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		ObjectInputStream lvOIS = null;
		ObjectOutputStream lvOOS = null;
		try
		{
			lvOIS = new ObjectInputStream(request.getInputStream());

			String lvOperacja = (String) lvOIS.readObject();

			lvOOS = new ObjectOutputStream(response.getOutputStream());
			if (Const.OPERACJA_GET_TOKEN.equals(lvOperacja))
			{
				lvOOS.writeObject(getNextToken());
			}
			else if (Const.OPERACJA_DISPOSE_TOKEN.equals(lvOperacja))
			{
				disposeToken((Token) lvOIS.readObject());
				LoginUtils.logDriverLogout((User) lvOIS.readObject());

				lvOOS.writeObject("OK");
			}
			lvOOS.flush();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (lvOOS != null)
				try
				{
					lvOOS.close();
				}
				catch (Exception ex)
				{
				}
		}
	}

	private Token getNextToken()
	{
		Connection lvConn = null;
		PreparedStatement lvSelectStmt = null;
		PreparedStatement lvInsertStmt = null;
		ResultSet lvResult = null;
		try
		{
			lvConn = DbUtils.getConnection();
			lvSelectStmt = lvConn.prepareStatement("SELECT MAX(ID) FROM MT_AKTUALNE_TOKENY");
			lvResult = lvSelectStmt.executeQuery();
			if (lvResult.next())
			{
				String lvNewToken = EncryptUtils.encrypt((lvResult.getInt(1) + 1) + "" + System.currentTimeMillis());

				lvInsertStmt = lvConn.prepareStatement("INSERT INTO MT_AKTUALNE_TOKENY (ID, TOKEN) VALUES (?, ?)");
				lvInsertStmt.setInt(1, lvResult.getInt(1) + 1);
				lvInsertStmt.setString(2, lvNewToken);
				if (lvInsertStmt.executeUpdate() == 1)
					return new Token(lvNewToken);
			}
			else
			{
				String lvNewToken = EncryptUtils.encrypt(1 + "" + System.currentTimeMillis());

				lvInsertStmt = lvConn.prepareStatement("INSERT INTO MT_AKTUALNE_TOKENY(ID, TOKEN) VALUES (?, ?)");
				lvInsertStmt.setInt(1, 1);
				lvInsertStmt.setString(2, lvNewToken);
				if (lvInsertStmt.executeUpdate() == 1)
					return new Token(lvNewToken);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.close(lvResult, lvSelectStmt, lvConn);
		}
		return null;
	}

	private void disposeToken(Token pmToken)
	{
		Connection lvConn = null;
		PreparedStatement lvStmt = null;
		try
		{
			lvConn = DbUtils.getConnection();
			lvStmt = lvConn.prepareStatement("DELETE FROM MT_AKTUALNE_TOKENY WHERE TOKEN = ?");
			lvStmt.setString(1, pmToken.getTokenValue());
			lvStmt.executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.close(lvStmt, lvConn);
		}
	}
}
