package pl.edu.wszib.msmolen.mt.client.utils;

import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import pl.edu.wszib.msmolen.mt.common.auth.Token;

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
		ObjectInputStream lvOIS = null;
		int lvLicznikProb = 0;

		while (token == null && lvLicznikProb < 5)
		{
			try
			{
				HttpURLConnection lvConnection = (HttpURLConnection) new URL(SERVLET_PATH + "?operacja=GET").openConnection();
				lvConnection.setDoInput(true);
				lvConnection.setDoOutput(true);
				lvConnection.setRequestMethod("POST");

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
			}
			lvLicznikProb++;
		}

		if (token == null)
			throw new Exception("Nie uda³o siê nawi¹zaæ po³¹czenia z serwerem.");
	}

	public void disposeToken()
	{
		ObjectInputStream lvOIS = null;
		try
		{
			HttpURLConnection lvConnection = (HttpURLConnection) new URL(addTokenToPath(SERVLET_PATH + "?operacja=DISPOSE")).openConnection();
			lvConnection.setDoInput(true);
			lvConnection.setDoOutput(true);
			lvConnection.setRequestMethod("POST");

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
		}
	}

	public static String addTokenToPath(String pmPath)
	{
		if (pmPath == null || "".equals(pmPath))
			return pmPath;

		StringBuilder lvPath = new StringBuilder(pmPath);
		if (!pmPath.contains("?"))
		{
			lvPath.append("?");
		}
		else if (!pmPath.contains("&"))
		{
			lvPath.append("&");
		}

		return lvPath.append("TOKEN=").append(getInstance().token.getTokenValue()).toString();
	}

	public Token getToken()
	{
		return token;
	}
}
