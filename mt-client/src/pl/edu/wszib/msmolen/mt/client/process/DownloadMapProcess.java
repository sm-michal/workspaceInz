package pl.edu.wszib.msmolen.mt.client.process;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import pl.edu.wszib.msmolen.mt.client.utils.ClientProperties;
import pl.edu.wszib.msmolen.mt.client.utils.ClientUtils;
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
		return new URL("https://" + ClientProperties.getServerAddress() + "/mt-server/exchange/downloadMap");
	}

	@Override
	protected void sendRequest(ObjectOutputStream pmOutput) throws IOException
	{
		pmOutput.writeObject(ClientProperties.getMapCheckSum());
	}

	@Override
	protected void processResponse(ObjectInputStream pmInput) throws Exception
	{
		String lvResponse = (String) pmInput.readObject();
		if (Const.MESSAGE_OK.equals(lvResponse))
		{
			lvResponse = (String) pmInput.readObject();
			if (Const.MAP_UPDATED.equals(lvResponse))
			{
				byte[] lvData = (byte[]) pmInput.readObject();
				ClientUtils.saveMapFile(lvData);
				ClientUtils.updateMapCheckSum(lvData);
				mMapLabel.setIcon(new ImageIcon(lvData));
			}
			else if (Const.MAP_CURRENT.equals(lvResponse))
			{
				mMapLabel.setIcon(new ImageIcon(ClientUtils.loadMapFile()));
			}
		}
		else
		{
			throw (Exception) pmInput.readObject();
		}
	}
}
