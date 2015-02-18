package pl.edu.wszib.msmolen.mt.client.process;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

import pl.edu.wszib.msmolen.mt.client.utils.UserManager;

public class UpdateDriverLocationProcess extends AbstractProcess
{
	private final Double mLatitude;
	private final Double mLongitude;
	private final Boolean mFinished;

	public UpdateDriverLocationProcess(Double pmLatitude, Double pmLongitude, boolean pmFinished)
	{
		mLatitude = pmLatitude;
		mLongitude = pmLongitude;
		mFinished = pmFinished;
	}

	@Override
	protected URL getURL() throws Exception
	{
		return new URL("https://localhost:8443/mt-server/exchange/updateDriver");
	}

	@Override
	protected void sendRequest(ObjectOutputStream pmOutput) throws IOException
	{
		pmOutput.writeObject(UserManager.getInstance().getUser().getForeignId());
		pmOutput.writeObject(mLatitude);
		pmOutput.writeObject(mLongitude);
		pmOutput.writeObject(mFinished);
	}

	@Override
	protected void processResponse(ObjectInputStream pmInput) throws Exception
	{
	}

}
