package pl.edu.wszib.msmolen.mt.exchange;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.edu.wszib.msmolen.mt.common.auth.Token;
import pl.edu.wszib.msmolen.mt.common.utils.exceptions.ApplicationException;
import pl.edu.wszib.msmolen.mt.common.utils.exceptions.TokenException;
import pl.edu.wszib.msmolen.mt.common.exchange.Const;

/**
 * Servlet implementation class BasicExchangeServlet
 */
@WebServlet("/exchange/exchangeData")
public abstract class BasicExchangeServlet extends HttpServlet
{

	private static final long serialVersionUID = -3675941988405666858L;

	/**
	 * 
	 */
	@Override
	protected final void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		System.out.println("BASIC GET");
	}

	/**
	 * 
	 */
	@Override
	protected final void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			processRequest(response, readRequest(request));
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			sendErrorToClient(e, response);
		}
	}

	/**
	 * 
	 * @param pmResponse
	 * @param pmObjects
	 * @throws Exception
	 */
	abstract protected void processRequest(HttpServletResponse pmResponse, Object[] pmObjects) throws Exception;

	/**
	 * 
	 * @param pmRequest
	 * @return
	 * @throws Exception
	 */
	private Object[] readRequest(HttpServletRequest pmRequest) throws Exception
	{
		List<Object> lvObjects = new ArrayList<Object>();

		ObjectInputStream lvOIS = null;
		Object lvRead = null;
		try
		{
			lvOIS = new ObjectInputStream(pmRequest.getInputStream());
			while (!Const.EOF.equals(lvRead = lvOIS.readObject()))
			{
				lvObjects.add(lvRead);
			}

			if (checkToken(lvObjects))
				lvObjects.remove(0);
		}
		catch (TokenException te)
		{
			throw te;
		}
		catch (Exception e)
		{
			throw new ApplicationException("Błąd", "Wystąpił nieoczekiwany błąd.s");
		}
		finally
		{
			if (lvOIS != null)
				try
				{
					lvOIS.close();
				}
				catch (Exception e)
				{
				}
		}
		return lvObjects.toArray();
	}

	/**
	 * 
	 * @param pmException
	 * @param pmResponse
	 */
	private void sendErrorToClient(Exception pmException, HttpServletResponse pmResponse)
	{
		ObjectOutputStream lvOOS = null;
		try
		{
			lvOOS = new ObjectOutputStream(pmResponse.getOutputStream());
			lvOOS.writeObject(Const.MESSSAGE_ERROR);
			if (pmException instanceof ApplicationException)
			{
				lvOOS.writeObject(pmException);
			}
			else
			{
				pmException.printStackTrace();
				lvOOS.writeObject(new ApplicationException("Błąd", "Wystąpił nieoczekiwany błąd: " + pmException.getMessage()));
			}
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
				catch (Exception e)
				{
				}
		}
	}

	/**
	 * 
	 * @param pmRequestObjs
	 * @return
	 * @throws Exception
	 */
	private boolean checkToken(List<Object> pmRequestObjs) throws Exception
	{
		if (pmRequestObjs == null || pmRequestObjs.size() == 0 || !(pmRequestObjs.get(0) instanceof Token))
			throw new TokenException("Niepoprawny identyfikator aplikacji. Zalecane jest ponowne uruchomienie aplikacji.");

		return true;
	}

}
