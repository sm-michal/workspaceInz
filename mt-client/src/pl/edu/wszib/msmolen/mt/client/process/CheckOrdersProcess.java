package pl.edu.wszib.msmolen.mt.client.process;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

import pl.edu.wszib.msmolen.mt.client.gui.DriverPanel;
import pl.edu.wszib.msmolen.mt.client.utils.UserManager;
import pl.edu.wszib.msmolen.mt.common.exchange.Const;
import pl.edu.wszib.msmolen.mt.common.utils.Orders;

public class CheckOrdersProcess extends AbstractProcess
{
	private final DriverPanel mDriverPanel;

	public CheckOrdersProcess(DriverPanel pmPanel)
	{
		mDriverPanel = pmPanel;
	}

	@Override
	protected URL getURL() throws Exception
	{
		return new URL("https://localhost:8443/mt-server/exchange/checkOrders");
	}

	@Override
	protected void sendRequest(ObjectOutputStream pmOutput) throws IOException
	{
		pmOutput.writeObject(UserManager.getInstance().getUser().getForeignId());
	}

	@Override
	protected void processResponse(ObjectInputStream pmInput) throws Exception
	{
		String lvResponse = (String) pmInput.readObject();
		if (Const.MESSAGE_OK.equals(lvResponse))
		{
			Object lvResult = pmInput.readObject();
			if (lvResult instanceof String && Const.MESSAGE_NOTHING.equals(lvResult))
			{
				mDriverPanel.showNothingToDoLabel();
			}
			else if (lvResult instanceof Orders)
			{
				mDriverPanel.stopTimer();

				mDriverPanel.loadMapOntoPanel((byte[]) pmInput.readObject());
			}

		}
		else
		{
			throw (Exception) pmInput.readObject();
		}
	}

}
