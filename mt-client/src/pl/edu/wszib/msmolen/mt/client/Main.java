package pl.edu.wszib.msmolen.mt.client;

import java.security.MessageDigest;

import javax.swing.SwingUtilities;

import pl.edu.wszib.msmolen.mt.client.gui.StartWindow;
import pl.edu.wszib.msmolen.mt.client.utils.SSLUtils;
import pl.edu.wszib.msmolen.mt.client.utils.TokenManager;

public class Main
{

	public static void main(String[] args)
	{
		SSLUtils.zezwalajNaPolaczeniaSsl();

		try
		{
			TokenManager.getInstance().obtainToken();

			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					StartWindow.startGUI();
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public static String getMessageDigest(String pmMessage)
	{
		char[] lvHex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		MessageDigest lvMD5 = null;
		byte[] lvDigest = null;
		byte lvByte;
		String lvResult;

		try
		{
			lvMD5 = MessageDigest.getInstance("MD5");
			lvMD5.update(pmMessage.getBytes());
			lvDigest = lvMD5.digest();
			lvResult = "";
			for (int i = 0; i < lvDigest.length; ++i)
			{
				lvByte = lvDigest[i];
				lvResult += lvHex[(lvByte & 0xF0) >> 4];
				lvResult += lvHex[(lvByte & 0x0F)];
			}
			return lvResult;
		}
		catch (Exception e)
		{
			return "";
		}
	}

}
