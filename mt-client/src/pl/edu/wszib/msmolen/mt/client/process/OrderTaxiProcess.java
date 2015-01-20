package pl.edu.wszib.msmolen.mt.client.process;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

import javax.swing.JOptionPane;

import pl.edu.wszib.msmolen.mt.client.gui.StartWindow;
import pl.edu.wszib.msmolen.mt.client.utils.ClientUtils;
import pl.edu.wszib.msmolen.mt.common.exchange.Const;

public class OrderTaxiProcess extends AbstractProcess
{
	private final double mLattitude;
	private final double mLongitude;

	private final String mOperation;

	public OrderTaxiProcess(double pmLattitude, double pmLongitude, String pmOperation)
	{
		mLattitude = pmLattitude;
		mLongitude = pmLongitude;
		mOperation = pmOperation;
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
		pmOutput.writeObject(mOperation);
	}

	@Override
	protected void processResponse(ObjectInputStream pmInput) throws Exception
	{
		String lvResponse = (String) pmInput.readObject();
		if (Const.MESSAGE_OK.equals(lvResponse))
		{
			int lvTime = (int) pmInput.readObject();
			JOptionPane.showMessageDialog(
					StartWindow.getInstance(),
					(Const.ORDER_OP_ORDER.equals(mOperation) ? "Taksówka zosta³a zamówiona. " : "") + "Czas oczekiwania na przybycie: "
							+ ClientUtils.getTimeMessage(lvTime) + ".", "Informacja",
					JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			throw (Exception) pmInput.readObject();
		}
	}

}
