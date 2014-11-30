package pl.edu.wszib.msmolen.mt.exchange;

import java.io.IOException;
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
import pl.edu.wszib.msmolen.mt.common.utils.EncryptUtils;
import pl.edu.wszib.msmolen.mt.db.DbUtils;

/**
 * Servlet przydzielajacy identyfikatory poszczegolnym klientom i zapisujacy je
 * w bazie
 */
@WebServlet("/exchange/token")
public class TokenServlet extends HttpServlet
{

	private static final long serialVersionUID = 876221085444631091L;

	private static final String OPERACJA_PARAM = "operacja";
	private static final String OPERACJA_GET = "GET";
	private static final String OPERACJA_DISPOSE = "DISPOSE";

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String lvOperacja = request.getParameter(OPERACJA_PARAM);
		ObjectOutputStream lvOOS = null;
		try
		{
			lvOOS = new ObjectOutputStream(response.getOutputStream());
			if (OPERACJA_GET.equals(lvOperacja))
			{
				lvOOS.writeObject(getNextToken());
			}
			else if (OPERACJA_DISPOSE.equals(lvOperacja))
			{
				disposeToken(request.getParameter("TOKEN"));
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

	private void disposeToken(String pmTokenValue)
	{
		Connection lvConn = null;
		PreparedStatement lvStmt = null;
		try
		{
			lvConn = DbUtils.getConnection();
			lvStmt = lvConn.prepareStatement("DELETE FROM MT_AKTUALNE_TOKENY WHERE TOKEN = ?");
			lvStmt.setString(1, pmTokenValue);
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
