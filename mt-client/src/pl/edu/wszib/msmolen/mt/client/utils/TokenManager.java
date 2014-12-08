package pl.edu.wszib.msmolen.mt.client.utils;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import pl.edu.wszib.msmolen.mt.common.auth.Token;
import pl.edu.wszib.msmolen.mt.common.exchange.Const;

public class TokenManager
{
	private static TokenManager instance;

	private Token token = null;

	private final String SERVLET_PATH = "https://localhost:8443/mt-server/exchange/token";

	private TokenManager()
	{
	}

	public static TokenManager getInstance()
	{
		if (instance == null)
			instance = new TokenManager();

		return instance;
	}

	public void obtainToken() throws Exception
	{
		ObjectOutputStream lvOOS = null;
		ObjectInputStream lvOIS = null;
		int lvLicznikProb = 0;

		while (token == null && lvLicznikProb < 5)
		{
			try
			{
				HttpURLConnection lvConnection = (HttpURLConnection) new URL(SERVLET_PATH).openConnection();
				lvConnection.setDoInput(true);
				lvConnection.setDoOutput(true);
				lvConnection.setRequestMethod("POST");

				lvOOS = new ObjectOutputStream(lvConnection.getOutputStream());
				lvOOS.writeObject(Const.OPERACJA_GET_TOKEN);

				lvOIS = new ObjectInputStream(lvConnection.getInputStream());
				token = (Token) lvOIS.readObject();
			}
			catch (Exception e)
			{
				e.printStackTrace();
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

				if (lvOOS != null)
					try
					{
						lvOOS.close();
					}
					catch (Exception e)
					{
					}
			}
			lvLicznikProb++;
		}

		if (token == null)
			throw new Exception("Nie uda³o siê nawi¹zaæ po³¹czenia z serwerem.");
	}

	public void disposeToken()
	{
		ObjectOutputStream lvOOS = null;
		ObjectInputStream lvOIS = null;
		try
		{
			HttpURLConnection lvConnection = (HttpURLConnection) new URL(SERVLET_PATH).openConnection();
			lvConnection.setDoInput(true);
			lvConnection.setDoOutput(true);
			lvConnection.setRequestMethod("POST");

			lvOOS = new ObjectOutputStream(lvConnection.getOutputStream());
			lvOOS.writeObject(Const.OPERACJA_DISPOSE_TOKEN);
			lvOOS.writeObject(token);
			lvOOS.writeObject(UserManager.getInstance().getUser());

			lvOIS = new ObjectInputStream(lvConnection.getInputStream());
		}
		catch (Exception e)
		{
			e.printStackTrace();
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

	public Token getToken()
	{
		return token;
	}
}
