package pl.edu.wszib.msmolen.mt.client.process;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import pl.edu.wszib.msmolen.mt.common.exchange.Const;

public class DownloadMapProcess extends AbstractProcess
{
	private JLabel mMapLabel = new JLabel();

	public DownloadMapProcess(JLabel pmLabel)
	{
		mMapLabel = pmLabel;
	}

	@Override
	protected URL getURL() throws Exception
	{
		return new URL("https://localhost:8443/mt-server/exchange/downloadMap");
	}

	@Override
	protected void sendRequest(ObjectOutputStream pmOutput) throws IOException
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void processResponse(ObjectInputStream pmInput) throws Exception
	{
		String lvResponse = (String) pmInput.readObject();
		if (Const.MESSAGE_OK.equals(lvResponse))
		{
			mMapLabel.setIcon(new ImageIcon((byte[]) pmInput.readObject()));
		}
		else
		{
			throw (Exception) pmInput.readObject();
		}
	}

}
