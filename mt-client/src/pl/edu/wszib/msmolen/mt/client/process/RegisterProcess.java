package pl.edu.wszib.msmolen.mt.client.process;

import java.io.IOException;
import java.io.ObjectOutputStream;

import pl.edu.wszib.msmolen.mt.client.gui.StartWindow;
import pl.edu.wszib.msmolen.mt.common.exchange.Const;
import pl.edu.wszib.msmolen.mt.common.utils.EncryptUtils;

public class RegisterProcess extends LoginProcess
{

	public RegisterProcess(StartWindow pmParent, String pmUserName, char[] pmPassword)
	{
		super(pmParent, pmUserName, pmPassword);
	}

	@Override
	protected void sendRequest(ObjectOutputStream pmOutput) throws IOException
	{
		pmOutput.writeObject(Const.MODE_REGISTER);
		pmOutput.writeObject(mUserName);
		pmOutput.writeObject(EncryptUtils.encrypt(new String(mPassword)));
	}

}
