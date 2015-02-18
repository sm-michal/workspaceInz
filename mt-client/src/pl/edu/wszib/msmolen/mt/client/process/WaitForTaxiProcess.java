package pl.edu.wszib.msmolen.mt.client.process;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

import pl.edu.wszib.msmolen.mt.common.exchange.Const;

public class WaitForTaxiProcess extends AbstractProcess
{
	private final int mOrderId;

	private String mOrderStatus;

	public WaitForTaxiProcess(int pmOrderId)
	{
		mOrderId = pmOrderId;
	}

	@Override
	protected URL getURL() throws Exception
	{
		return new URL("https://localhost:8443/mt-server/exchange/waitForTaxi");
	}

	@Override
	protected void sendRequest(ObjectOutputStream pmOutput) throws IOException
	{
		pmOutput.writeObject(mOrderId);
	}

	@Override
	protected void processResponse(ObjectInputStream pmInput) throws Exception
	{
		String lvResponse = (String) pmInput.readObject();
		if (Const.MESSAGE_OK.equals(lvResponse))
		{
			mOrderStatus = (String) pmInput.readObject();
		}
		else
		{
			throw (Exception) pmInput.readObject();
		}
	}

	public String getOrderStatus()
	{
		return mOrderStatus;
	}

}
