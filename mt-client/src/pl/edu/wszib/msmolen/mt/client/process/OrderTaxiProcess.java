package pl.edu.wszib.msmolen.mt.client.process;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

import javax.swing.JOptionPane;

import pl.edu.wszib.msmolen.mt.client.gui.StartWindow;
import pl.edu.wszib.msmolen.mt.common.exchange.Const;

public class OrderTaxiProcess extends AbstractProcess
{
	private final double mLattitude;
	private final double mLongitude;

	public OrderTaxiProcess(double pmLattitude, double pmLongitude)
	{
		mLattitude = pmLattitude;
		mLongitude = pmLongitude;
	}

	@Override
	protected URL getURL() throws Exception
	{
		return new URL("https://localhost:8443/mt-server/exchange/orderTaxi");
	}

	@Override
	protected void sendRequest(ObjectOutputStream pmOutput) throws IOException
	{
		pmOutput.writeObject(mLattitude);
		pmOutput.writeObject(mLongitude);
	}

	@Override
	protected void processResponse(ObjectInputStream pmInput) throws Exception
	{
		String lvResponse = (String) pmInput.readObject();
		if (Const.MESSAGE_OK.equals(lvResponse))
		{
			JOptionPane.showMessageDialog(StartWindow.getInstance(), "Taksówka zosta³a zamówiona. Oczekuj na przybycie.", "Informacja", JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			throw (Exception) pmInput.readObject();
		}
	}

}
