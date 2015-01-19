package pl.edu.wszib.msmolen.mt.client.process;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import pl.edu.wszib.msmolen.mt.client.gui.StartWindow;
import pl.edu.wszib.msmolen.mt.client.utils.TokenManager;
import pl.edu.wszib.msmolen.mt.common.exchange.Const;
import pl.edu.wszib.msmolen.mt.common.utils.exceptions.ApplicationException;

public abstract class AbstractProcess
{
	private final StartWindow mParentWindow;

	public AbstractProcess()
	{
		mParentWindow = StartWindow.getInstance();
	}

	public void process()
	{
		ObjectOutputStream lvOOS = null;
		ObjectInputStream lvOIS = null;
		try
		{
			HttpURLConnection lvConn = (HttpURLConnection) getURL().openConnection();
			lvConn.setRequestMethod("POST");
			lvConn.setDoInput(true);
			lvConn.setDoOutput(true);

			lvOOS = new ObjectOutputStream(lvConn.getOutputStream());
			lvOOS.writeObject(TokenManager.getInstance().getToken());

			sendRequest(lvOOS);

			lvOOS.writeObject(Const.EOF);

			lvOIS = new ObjectInputStream(lvConn.getInputStream());

			processResponse(lvOIS);
		}
		catch (ApplicationException e)
		{
			mParentWindow.displayErrorMessage(e.getTitle(), e.getMessage());
		}
		catch (Exception e)
		{
			mParentWindow.displayErrorMessage("B³¹d", "Wyst¹pi³ nieoczekiwany b³¹d:" + e.getMessage());
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

	protected abstract URL getURL() throws Exception;

	protected abstract void sendRequest(ObjectOutputStream pmOutput) throws IOException;

	protected abstract void processResponse(ObjectInputStream pmInput) throws Exception;
}
