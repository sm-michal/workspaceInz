package pl.edu.wszib.msmolen.mt.client.process;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

import pl.edu.wszib.msmolen.mt.client.gui.StartWindow;
import pl.edu.wszib.msmolen.mt.client.utils.UserManager;
import pl.edu.wszib.msmolen.mt.common.auth.User;
import pl.edu.wszib.msmolen.mt.common.exchange.Const;
import pl.edu.wszib.msmolen.mt.common.utils.EncryptUtils;

public class LoginProcess extends AbstractProcess
{
	protected final String mUserName;
	protected final char[] mPassword;

	public LoginProcess(StartWindow pmParent, String pmUserName, char[] pmPassword)
	{
		super(pmParent);
		mUserName = pmUserName;
		mPassword = pmPassword;
	}

	@Override
	protected URL getURL()
	{
		try
		{
			return new URL("https://localhost:8443/mt-server/exchange/getUser");
		}
		catch (Exception e)
		{
			return null;
		}
	}

	@Override
	protected void sendRequest(ObjectOutputStream pmOutput) throws IOException
	{
		pmOutput.writeObject(Const.MODE_LOGIN);
		pmOutput.writeObject(mUserName);
		pmOutput.writeObject(EncryptUtils.encrypt(new String(mPassword)));
	}

	@Override
	protected void processResponse(ObjectInputStream pmInput) throws Exception
	{
		String lvResponse = (String) pmInput.readObject();
		if (Const.MESSAGE_OK.equals(lvResponse))
		{
			User lvUser = (User) pmInput.readObject();
			UserManager.getInstance().setUser(lvUser);
		}
		else
		{
			throw (Exception) pmInput.readObject();
		}
	}
}
